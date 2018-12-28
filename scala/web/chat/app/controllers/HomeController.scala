package controllers

import javax.inject._
import models.LoginData
import play.api._
import play.api.data._
import play.api.data.Forms._
import play.api.mvc._
import play.api.i18n.I18nSupport

@Singleton
class HomeController @Inject()(cc: ControllerComponents) extends AbstractController(cc) with I18nSupport {

  val loginForm = Form(
    mapping(
      "userName" -> nonEmptyText,
    )(LoginData.apply)(LoginData.unapply)
  )

  def index() = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.index(loginForm))
  }

  val login = Action(parse.form(loginForm)) { implicit request =>
    val loginData = request.body
    Ok(views.html.index(loginForm))
  }
}
