package playpen

import com.typesafe.config.ConfigFactory
import org.specs2.mutable.Specification
import play.api.Environment
import play.api.libs.ws.ning.NingWSClientConfig
import play.api.test.WithApplication

class TracingWSConfigParserTest extends Specification {

  val defaultNingConfig = NingWSClientConfig()

  "TracingWSConfigParser" should {

    "parse useragent from config" in new WithApplication {
      val actual = parseThis(
        """
          |play.ws.useragent = "FakeUserAgent"
        """.stripMargin)

      actual.userAgent mustEqual "FakeUserAgent"
    }

    "parse useragent from config" in new WithApplication {
      parseThis(
        """
          |play.ws.useragent = null
        """.stripMargin) must throwAn[Exception]
    }

  }

  def parseThis(input: String)(implicit app: play.api.Application) = {
    val config = play.api.Configuration(ConfigFactory.parseString(input).withFallback(ConfigFactory.defaultReference()))
    val parser = new TracingWSConfigParser(defaultNingConfig, config, app.injector.instanceOf[Environment])
    parser.parse()
  }

}
