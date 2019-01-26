package controllers

import javax.inject._
import play.api.mvc._
import play.api.libs.json.JsValue
import repositories.ChatRoomRepository
import services.ChatService


@Singleton
class RoomController @Inject()(cc: ControllerComponents)(chatService: ChatService)(chatRoomRepository: ChatRoomRepository) extends AbstractController(cc) {

  def index(id: Long) = Action { implicit request: Request[AnyContent] =>
    val userName = request.session.get("userName")
    userName match {
      case Some(_) => {
        if (chatRoomRepository.isExists(id)) {
          Ok(views.html.room())
        } else {
          NotFound("")
        }
      }
      case None => Redirect(routes.HomeController.index)
    }
  }

  def getLog(id: Long) = Action { implicit request: Request[AnyContent] =>
    Ok(chatService.getLog(id))
  }

  def connect(id: Long) = WebSocket.accept[JsValue, JsValue] { request =>
    chatService.connect(id)
  }
}
