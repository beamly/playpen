package playpen

import org.specs2.mutable.Specification

import java.util.UUID

class TracingNingWSClientTest extends Specification {


  "NingWSRequestSpec" should {

    "add the correct header for the implicit RequestId in scope" in {
      val config = TracingWSClientConfig("test-agent")
      val id = "test-id"
      implicit val requestContext = RequestContext(RequestId(id))
      val tracingClient = TracingNingWSClient(config)
      val request = tracingClient.url("http://playframework.com/")
      request.headers must contain(RequestId.HTTP_REQUEST_HEADER -> Seq(id))

    }

    "add the correct header using the implicit random RequestId generation" in {
      import playpen.RequestContext.Implicits.random
      val config = TracingWSClientConfig("test-agent")

      val tracingClient = TracingNingWSClient(config)
      val request = tracingClient.url("http://playframework.com/")

      request.headers must haveKey(RequestId.HTTP_REQUEST_HEADER)
      request.headers(RequestId.HTTP_REQUEST_HEADER) must have size (1)

      val actualRequestId = request.headers(RequestId.HTTP_REQUEST_HEADER).head
      actualRequestId must startWith("req")
      UUID.fromString(actualRequestId.replaceFirst("req", "")) must not(throwA[Exception])
    }
  }

}
