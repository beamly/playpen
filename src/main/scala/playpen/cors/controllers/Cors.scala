package playpen.cors.controllers

import play.api.Play.current
import play.api.mvc.{Action, Controller}
import playpen.cors.CorsHeaders

/// Usage in the routes file:
//  OPTIONS  /*all         playpen.cors.controllers.Cors.preflight(all)
object Cors extends Controller {

  def preflight(all: String) = Action { request =>
    Ok("").withHeaders(CorsHeaders.headers(request): _*)
  }
}
