package playpen.requestid

import play.api.mvc.{ EssentialAction, Results }
import play.api.test.{ FakeRequest, PlaySpecification }

class RequestIdActionTest extends PlaySpecification {

  val action: EssentialAction = RequestIdAction { request =>
    val value = request.requestId.id
    Results.Ok(value)
  }

  "RequestIdAction" should {
    "use the value of the X-Request-Id to set the requestId on the requestContext" in {
      val requestId = "test-id"
      val req = FakeRequest().withHeaders(RequestIdHeader.header -> requestId)

      val result = call(action, req)

      status(result) shouldEqual OK
      contentAsString(result) mustEqual (requestId)
    }

    "return the requestId in the X-Request-Id response header" in {
      val requestId = "test-id"
      val req = FakeRequest().withHeaders(RequestIdHeader.header -> requestId)

      val result = call(action, req)

      status(result) shouldEqual OK
      header(RequestIdHeader.header, result) shouldEqual Some(requestId)
    }

    "generate a random UUID on the requestContext when the X-Request-Id header has not been provided" in {
      val req = FakeRequest()
      val result = call(action, req)

      status(result) shouldEqual OK
      contentAsString(result).matches("req[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}") should beTrue
      header(RequestIdHeader.header, result).get.matches("req[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}") should beTrue
    }

  }

}
