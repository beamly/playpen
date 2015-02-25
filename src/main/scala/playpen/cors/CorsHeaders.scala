package playpen.cors
import play.api.Play._
import play.api.mvc.RequestHeader

object CorsHeaders {
  lazy val allowedOrigins = current.configuration.getStringSeq("playpen.cors.allowed.origins").getOrElse(Seq())
  lazy val allowedMethods = current.configuration.getStringSeq("playpen.cors.allowed.methods").getOrElse(Seq("POST", "GET", "OPTIONS", "PUT", "DELETE"))

  def headers(request: RequestHeader): Seq[(String, String)] = request match {
    case CorsParams(params) if allowedOrigins.exists(orig => orig == "*" | params.origin.endsWith(orig)) =>
      Seq(
        "Access-Control-Allow-Origin" -> params.origin,
        "Access-Control-Allow-Methods" -> allowedMethods.mkString(","),
        "Access-Control-Allow-Headers" -> params.headers.mkString(","))
    case _ => Seq()
  }
}
