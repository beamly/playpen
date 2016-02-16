package playpen

import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.mvc._

import scala.concurrent.Future


class RequestIdRequest[A](val requestId: RequestId, request: Request[A]) extends WrappedRequest[A](request)

object RequestIdTransformer extends ActionTransformer[Request, RequestIdRequest] {

  private def extractRequestId[A](request: Request[A]) = request.headers.get(RequestId.HTTP_REQUEST_HEADER).getOrElse(RequestId.random.id)

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
      result.withHeaders(RequestId.HTTP_REQUEST_HEADER -> requestIdRequest.requestId.id)
    }
  }
}