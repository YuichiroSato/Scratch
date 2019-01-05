package controllers

import javax.inject._
import models.ChatRoom
import models.ChatRoomData
import org.joda.time.DateTime
import play.api.data._
import play.api.data.Forms._
import play.api.mvc._
import play.api.db.Database
import anorm._
import anorm.SqlParser._
import play.api.i18n.I18nSupport

@Singleton
class ListController @Inject()(cc: ControllerComponents)(database: Database) extends AbstractController(cc) with I18nSupport {

  val createChatRoomForm = Form(
    mapping(
      "description" -> nonEmptyText,
    )(ChatRoomData.apply)(ChatRoomData.unapply)
  )

  val chatRoomParser = get[Long]("room_id") ~ get[String]("description") ~ get[DateTime]("created_time")

  def index() = Action { implicit request: Request[AnyContent] =>
    val userName = request.session.get("userName")
    userName match {
      case Some(_) => {
        val result = database.withConnection { implicit conn =>
          SQL("select * from rooms order by created_time desc")
            .as(chatRoomParser.*)
            .map { case room_id ~ description ~ created_time => ChatRoom(room_id, description, created_time) }
        }
        Ok(views.html.list(result)(createChatRoomForm))
      }
      case None => Redirect(routes.HomeController.index)
    }
  }

  val createChatRoom = Action(parse.form(createChatRoomForm)) { implicit request =>
    val createRoomData = request.body
    val result = database.withConnection { implicit conn =>
      SQL("insert into rooms (description) values ({d})")
        .on("d" -> createRoomData.description)
        .execute()
      SQL("select * from rooms order by created_time desc")
        .as(chatRoomParser.*)
        .map { case room_id ~ description ~ created_time => ChatRoom(room_id, description, created_time) }
    }
    Ok(views.html.list(result)(createChatRoomForm))
  }
}
