package controllers

import javax.inject._
import models.LoginData
import play.api.data.Forms._
import play.api.data._
import play.api.i18n.I18nSupport
import play.api.mvc._
import services.LoginService


@Singleton
class HomeController @Inject()(cc: ControllerComponents)(loginService: LoginService) extends AbstractController(cc) with I18nSupport {

  val loginForm = Form(
    mapping(
      "userName" -> nonEmptyText,
    )(LoginData.apply)(LoginData.unapply)
  )

  def index() = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.index(loginForm)(""))
  }

  val login = Action(parse.form(loginForm)) { implicit request =>
    val loginData = request.body
    loginService.login(loginData.userName) match {
      case Left(errorMessage) => Ok(views.html.index(loginForm)(errorMessage))
      case Right(_) => Redirect(routes.ListController.index)
            .withSession("userName" -> loginData.userName)
    }
  }
}
