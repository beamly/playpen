package playpen.requestid.ws

import java.util.UUID

import org.specs2.mutable.Specification
import playpen.requestid.{ RequestId, RequestIdHeader }

class TracingNingWSClientTest extends Specification {


  "NingWSRequestSpec" should {

    "add the correct header for the implicit RequestId in scope" in {
      val config = TracingWSClientConfig("test-agent")
      val id = "test-id"
      implicit val requestId = RequestId(id)
      val tracingClient = TracingNingWSClient(config)
      val request = tracingClient.url("http://playframework.com/")
      request.headers must contain(RequestIdHeader.header -> Seq(id))

    }

    "add the correct header using the implicit random RequestId generation" in {
      import playpen.requestid.RequestIdGenerator.Implicits.random
      val config = TracingWSClientConfig("test-agent")

      val tracingClient = TracingNingWSClient(config)
      val request = tracingClient.url("http://playframework.com/")

      request.headers must haveKey(RequestIdHeader.header)
      request.headers(RequestIdHeader.header) must have size (1)

      val actualRequestId = request.headers(RequestIdHeader.header).head
      actualRequestId must startWith("req")
      UUID.fromString(actualRequestId.replaceFirst("req", "")) must not(throwA[Exception])
    }
  }

}
