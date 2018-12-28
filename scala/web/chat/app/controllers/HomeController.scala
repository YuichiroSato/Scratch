package controllers

import javax.inject._
import models.LoginData
import play.api._
import play.api.data._
import play.api.data.Forms._
import play.api.mvc._
import play.api.i18n.I18nSupport
import scala.concurrent.duration._
import play.api.cache.redis.CacheApi

@Singleton
class HomeController @Inject()(cc: ControllerComponents)(cache: CacheApi) extends AbstractController(cc) with I18nSupport {

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
    var n = 0
    var finished = false
    while (!finished) {
      val locked = cache.setIfNotExists("users-lock", "writing", 1.seconds)
      if (locked) {
        val isRegistered = cache.set[String]("users").contains(loginData.userName)
        if (isRegistered) {
          finished = true
        } else {
          cache.set[String]("users").add(loginData.userName)
          finished = true
        }
        cache.remove("users-lock")
      }
      Thread.sleep(10)
      n += 1
      if (n >= 10) {
        finished = true
      }
    }
    Ok(views.html.index(loginForm))
  }
}
