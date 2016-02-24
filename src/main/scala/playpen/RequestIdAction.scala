package playpen

import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.mvc._

import scala.concurrent.Future

class RequestContextRequest[A](val requestContext: RequestContext, request: Request[A]) extends WrappedRequest[A](request) {

  def requestId= requestContext.requestId
}

object RequestContextTransformer extends ActionTransformer[Request, RequestContextRequest] {

  private def extractRequestId[A](request: Request[A]) = request.headers.get(RequestId.HTTP_REQUEST_HEADER).getOrElse(RequestId.random.id)

  def transform[A](request: Request[A]) = Future.successful {
    val requestId = extractRequestId(request)
    new RequestContextRequest(RequestContext(RequestId(requestId)), request)
  }
}

object RequestContextAction extends ActionBuilder[RequestContextRequest] {
  def invokeBlock[A](request: Request[A], block: RequestContextRequest[A] => Future[Result]): Future[Result] = {
    for {
      requestContextRequest <- RequestContextTransformer.transform[A](request)
      result <- block(requestContextRequest)
    } yield {
      result.withHeaders(RequestId.HTTP_REQUEST_HEADER -> requestContextRequest.requestId.id)
    }
  }
}
