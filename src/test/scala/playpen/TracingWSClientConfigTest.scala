package playpen

import org.specs2.mutable.Specification
import play.api.libs.ws.ning.NingWSClientConfig

class TracingNingAsyncHttpClientConfigBuilderTest extends Specification {

  "TracingNingAsyncHttpClientConfigBuilder" should {

    "build a client with user-agent set according to TracingWSClientConfig" in {

      val tracingConfig = TracingWSClientConfig("FakeUserAgent", NingWSClientConfig())

      val builder = new TracingNingAsyncHttpClientConfigBuilder(tracingConfig)

      val actual = builder.build()

      actual.getUserAgent mustEqual ("FakeUserAgent")

    }
  }

}
