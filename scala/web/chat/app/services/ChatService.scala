package services

import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicInteger

import actors.ChatRoomActor
import akka.actor.ActorSystem
import akka.stream.scaladsl.{BroadcastHub, Flow, Keep, MergeHub}
import akka.stream.{KillSwitches, Materializer, UniqueKillSwitch}
import javax.inject.{Inject, Singleton}
import play.api.libs.json.{JsObject, JsValue}
import play.api.libs.streams.ActorFlow
import repositories.ChatLogRepository

import scala.concurrent.duration._


@Singleton
class ChatService @Inject()(system: ActorSystem, materializer: Materializer)(chatLogRepository: ChatLogRepository) {
  val rooms = new ConcurrentHashMap[Long, Flow[JsValue, JsValue, UniqueKillSwitch]]()
  val connectCounts = new ConcurrentHashMap[Long, AtomicInteger]()

  def getLog(id: Long): JsObject = {
    chatLogRepository.getPosts(id)
  }

  private def join(id: Long): Flow[JsValue, JsValue, UniqueKillSwitch] = {
    if (!rooms.containsKey(id)) {
      val (sink, source) = MergeHub.source[JsValue](perProducerBufferSize = 16)
        .toMat(BroadcastHub.sink(bufferSize = 256))(Keep.both)
        .run()(materializer)

      val flow = Flow.fromSinkAndSource(sink, source)
        .joinMat(KillSwitches.singleBidi[JsValue, JsValue])(Keep.right)
        .backpressureTimeout(3.seconds)
        .map(msg => {
          chatLogRepository.append(id, msg)
          msg
        })

      rooms.putIfAbsent(id, flow)
      connectCounts.putIfAbsent(id, new AtomicInteger())
    }
    connectCounts.get(id).incrementAndGet()
    rooms.get(id)
  }

  def connect(id: Long): Flow[Any, JsValue, UniqueKillSwitch] = {
    ActorFlow.actorRef(out => ChatRoomActor.props(out))(system, materializer)
      .viaMat(join(id))(Keep.right)
  }

  def cleanUp(id: Long): Unit = {
    if (connectCounts.get(id).get() < 2) {
      rooms.remove(id)
      connectCounts.remove(id)
    } else {
      connectCounts.get(id).decrementAndGet()
    }
  }
}
