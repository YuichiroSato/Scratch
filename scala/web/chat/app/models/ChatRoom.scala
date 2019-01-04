package models

import org.joda.time.DateTime

case class ChatRoom(val room_id: Long, val description: String, val created_time: DateTime)