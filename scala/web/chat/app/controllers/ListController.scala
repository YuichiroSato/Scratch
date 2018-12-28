package controllers

import javax.inject._
import play.api.mvc._

@Singleton
class ListController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

  def index() = Action { implicit request: Request[AnyContent] =>
    val userName = request.session.get("userName")
    userName match {
      case Some(_) => Ok(views.html.list())
      case None => Redirect(routes.HomeController.index)
    }
  }
}
