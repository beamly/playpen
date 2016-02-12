package playpen.requestid

import java.util.UUID

import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.mvc._
import playpen.requestid.RequestIdHeader._

import scala.concurrent.Future


class RequestIdRequest[A](val requestId: RequestId, request: Request[A]) extends WrappedRequest[A](request)

object RequestIdTransformer extends ActionTransformer[Request, RequestIdRequest] {

  private def extractRequestId[A](request: Request[A]) = request.headers.get(header).getOrElse("req" + UUID.randomUUID().toString)

  def transform[A](request: Request[A]) = Future.successful {
    val requestId = extractRequestId(request)
    new RequestIdRequest(RequestId(requestId), request)
  }
}

object RequestIdAction extends ActionBuilder[RequestIdRequest] {
  def invokeBlock[A](request: Request[A], block: RequestIdRequest[A] => Future[Result]): Future[Result] = {
    for {
      requestIdRequest <- RequestIdTransformer.transform[A](request)
      result <- block(requestIdRequest)
    } yield {
      result.withHeaders(header -> requestIdRequest.requestId.id)
    }
  }
}


object RequestIdHeader {
  val header = "X-Request-ID"
}
