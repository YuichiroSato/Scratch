package repositories

import javax.inject.{Inject, Singleton}
import play.api.cache.redis.CacheApi
import play.api.libs.json.{JsArray, JsObject, JsValue, Json}

import scala.concurrent.duration._


trait ChatLogRepository {

  def getPosts(room_id: Long): JsObject
  def append(room_id: Long, message: JsValue): Unit
}

@Singleton
class RedisChatLogRepository @Inject()(cache: CacheApi) extends ChatLogRepository {

  def getPosts(room_id: Long): JsObject = {
    Json.obj("texts" -> cache.list[String]("room_" + room_id)
      .toList
      .map((j: String) => Json.parse(j))
      .foldLeft(Json.arr())((arr: JsArray, j: JsValue) => arr.append((j \ "text").get)))
  }

  def append(room_id: Long, message: JsValue): Unit = {
    cache.list[String]("room_" + room_id)
      .prepend(Json.stringify(message))
    cache.expire("room_" + room_id, 1.hours)
  }
}
