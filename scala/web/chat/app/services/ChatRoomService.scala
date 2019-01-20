package services

import java.util.concurrent.ConcurrentHashMap

import akka.stream.{KillSwitches, Materializer, UniqueKillSwitch}
import akka.stream.scaladsl.{BroadcastHub, Flow, Keep, MergeHub}
import javax.inject.Singleton
import play.api.libs.json.JsValue

import scala.concurrent.duration._

@Singleton
class ChatRoomService {
  val rooms = new ConcurrentHashMap[Long, Flow[JsValue, JsValue, UniqueKillSwitch]]()

  def join(id: Long, materializer: Materializer) = {
    if (!rooms.containsKey(id)) {
      val (sink, source) = MergeHub.source[JsValue](perProducerBufferSize = 16)
          .toMat(BroadcastHub.sink(bufferSize = 256))(Keep.both)
          .run()(materializer)

      val flow = Flow.fromSinkAndSource(sink, source)
        .joinMat(KillSwitches.singleBidi[JsValue, JsValue])(Keep.right)
        .backpressureTimeout(3.seconds)

      rooms.put(id, flow)
    }
    rooms.get(id)
  }
}
