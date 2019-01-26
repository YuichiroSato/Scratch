package repositories

import javax.inject.{Inject, Singleton}
import models.ChatRoom
import play.api.db.Database


trait ChatRoomRepository {

  def getRooms(): List[ChatRoom]
  def find(room_id: Long): Option[ChatRoom]
  def isExists(room_id: Long): Boolean
  def insert(description: String): Boolean
}

@Singleton
class AnormChatRoomRepository @Inject()(database: Database) extends ChatRoomRepository {
  import anorm.SqlParser._
  import anorm._
  import org.joda.time.DateTime

  val chatRoomParser = get[Long]("room_id") ~ get[String]("description") ~ get[DateTime]("created_time")

  def getRooms(): List[ChatRoom] = {
    database.withConnection { implicit conn =>
      SQL("select * from rooms order by created_time desc")
        .as(chatRoomParser.*)
        .map { case room_id ~ description ~ created_time => ChatRoom(room_id, description, created_time) }
    }
  }

  def find(room_id: Long): Option[ChatRoom] = {
    val rooms = database.withConnection { implicit conn =>
      SQL("select * from rooms where room_id = {id}")
        .on("id" -> room_id)
        .as(chatRoomParser.*)
        .map { case room_id ~ description ~ created_time => ChatRoom(room_id, description, created_time) }
    }
    rooms.headOption
  }

  def isExists(room_id: Long): Boolean = {
    find(room_id).isDefined
  }

  def insert(description: String): Boolean = {
    database.withConnection { implicit conn =>
      SQL("insert into rooms (description) values ({d})")
        .on("d" -> description)
        .execute()
    }
  }
}
