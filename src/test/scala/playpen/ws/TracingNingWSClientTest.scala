package playpen.ws

import org.specs2.mutable.Specification
import playpen.requestid.{ RequestId, RequestIdHeader }

class TracingNingWSClientTest extends Specification {

  "NingWSRequestSpec" should {

    "add the correct header for the implicit RequestId in scope" in {
      val id = "test-id"
      implicit val requestId = RequestId(id)
      val tracingClient = TracingNingWSClient()
      val request = tracingClient.url("http://playframework.com/")
      request.headers mustEqual (Map(RequestIdHeader.header -> Seq(id)))

    }
  }

}
