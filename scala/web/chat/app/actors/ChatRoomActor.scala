package actors

import akka.actor._
import play.api.libs.json.JsValue
import services.ChatService

class ChatRoomActor(out: ActorRef, id: Long, chatService: ChatService) extends Actor {
  def receive = {
    case msg: JsValue => {
      out ! msg
    }
  }

  override def postStop(): Unit = {
    chatService.cleanUp(id)
  }
}

object ChatRoomActor {
  def props(out: ActorRef, id: Long, chatService: ChatService) = Props(new ChatRoomActor(out, id, chatService))
}
