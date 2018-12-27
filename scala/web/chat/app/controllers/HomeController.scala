package controllers

import javax.inject._
import models.LoginData
import play.api._
import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc._

@Singleton
class HomeController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

  val loginForm = Form(
    mapping(
      "userName" -> nonEmptyText,
    )(LoginData.apply)(LoginData.unapply)
  )

  def index() = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.index())
  }
}
