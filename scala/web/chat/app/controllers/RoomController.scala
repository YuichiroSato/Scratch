package controllers

import actors.ChatRoomActor
import akka.actor._
import akka.stream.Materializer
import javax.inject._
import models.ChatRoom
import org.joda.time.DateTime
import play.api.mvc._
import play.api.libs.streams.ActorFlow
import play.api.db.Database
import play.api.libs.json.JsValue
import anorm._
import anorm.SqlParser._


@Singleton
class RoomController @Inject()(cc: ControllerComponents, system: ActorSystem, materializer: Materializer)(database: Database) extends AbstractController(cc) {

  val chatRoomParser = get[Long]("room_id") ~ get[String]("description") ~ get[DateTime]("created_time")

  def index(id: Long) = Action { implicit request: Request[AnyContent] =>
    val userName = request.session.get("userName")
    userName match {
      case Some(_) => {
        val room = database.withConnection { implicit conn =>
          SQL("select * from rooms where room_id = {id}")
            .on("id" -> id)
            .as(chatRoomParser.*)
            .map { case room_id ~ description ~ created_time => ChatRoom(room_id, description, created_time) }
        }
        room.headOption match {
          case Some(_) => Ok(views.html.room())
          case None => NotFound("")
        }
      }
      case None => Redirect(routes.HomeController.index)
    }
  }

  def connect(id: Long) = WebSocket.accept[JsValue, JsValue] { request =>
      ActorFlow.actorRef(out => ChatRoomActor.props(out))(system, materializer)
  }
}
