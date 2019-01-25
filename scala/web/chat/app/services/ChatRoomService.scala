package services

import java.util.concurrent.ConcurrentHashMap

import akka.stream.{KillSwitches, Materializer, UniqueKillSwitch}
import akka.stream.scaladsl.{BroadcastHub, Flow, Keep, MergeHub}
import play.api.libs.json.JsValue
import javax.inject.{Inject, Singleton}
import play.api.cache.redis.CacheApi

import scala.concurrent.duration._

@Singleton
class ChatRoomService @Inject()(cache: CacheApi) {
  val rooms = new ConcurrentHashMap[Long, Flow[JsValue, JsValue, UniqueKillSwitch]]()

  def join(id: Long, materializer: Materializer) = {
    if (!rooms.containsKey(id)) {
      val (sink, source) = MergeHub.source[JsValue](perProducerBufferSize = 16)
          .toMat(BroadcastHub.sink(bufferSize = 256))(Keep.both)
          .run()(materializer)

      val flow = Flow.fromSinkAndSource(sink, source)
        .joinMat(KillSwitches.singleBidi[JsValue, JsValue])(Keep.right)
        .backpressureTimeout(3.seconds)
        .map(msg => {
          cache.list[String]("room_" + id).prepend(Json.stringify(msg))
          cache.expire("room_" + id, 1.hours)
          msg
        })

      rooms.put(id, flow)
    }
    rooms.get(id)
  }
}
