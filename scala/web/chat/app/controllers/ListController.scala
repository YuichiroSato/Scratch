package controllers

import javax.inject._
import models.ChatRoomData
import org.joda.time.DateTime
import play.api.data._
import play.api.data.Forms._
import play.api.mvc._
import play.api.i18n.I18nSupport
import repositories.ChatRoomRepository

@Singleton
class ListController @Inject()(cc: ControllerComponents)(chatRoomRepository: ChatRoomRepository) extends AbstractController(cc) with I18nSupport {

  val createChatRoomForm = Form(
    mapping(
      "description" -> nonEmptyText,
    )(ChatRoomData.apply)(ChatRoomData.unapply)
  )

  def index() = Action { implicit request: Request[AnyContent] =>
    val userName = request.session.get("userName")
    userName match {
      case Some(_) => {
        val result = chatRoomRepository.getRooms()
        Ok(views.html.list(result)(createChatRoomForm))
      }
      case None => Redirect(routes.HomeController.index)
    }
  }

  val createChatRoom = Action(parse.form(createChatRoomForm)) { implicit request =>
    val createRoomData = request.body
    chatRoomRepository.insert(createRoomData.description)
    val result = chatRoomRepository.getRooms()
    Ok(views.html.list(result)(createChatRoomForm))
  }
}
