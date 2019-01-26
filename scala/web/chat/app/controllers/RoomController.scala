package controllers

import actors.ChatRoomActor
import akka.actor._
import akka.stream._
import akka.stream.scaladsl._
import javax.inject._
import play.api.mvc._
import play.api.libs.streams.ActorFlow
import play.api.libs.json.JsValue
import repositories.ChatRoomRepository
import services.ChatRoomService


@Singleton
class RoomController @Inject()(cc: ControllerComponents, system: ActorSystem, materializer: Materializer, chatRoomService: ChatRoomService)(chatRoomRepository: ChatRoomRepository) extends AbstractController(cc) {

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
    Ok(chatRoomService.getLog(id))
  }

  def connect(id: Long) = WebSocket.accept[JsValue, JsValue] { request =>
      ActorFlow.actorRef(out => ChatRoomActor.props(out))(system, materializer)
        .viaMat(chatRoomService.join(id, materializer))(Keep.right)
  }
}
