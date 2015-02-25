package playpen.cors
import play.api.mvc.RequestHeader

case class CorsParams(origin: String, method: String, headers: Seq[String])

object CorsParams {
  def unapply(req: RequestHeader): Option[CorsParams] = {
    for {
      origin <- req.headers.get("Origin")
      method <- req.headers.get("Access-Control-Request-Method")
      headers <- req.headers.get("Access-Control-Request-Headers").map(_.split(",").map(_.trim))
    } yield new CorsParams(origin, method, headers)
  }
}
