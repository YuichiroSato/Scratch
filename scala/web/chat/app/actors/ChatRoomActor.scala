package actors

import akka.actor._
import play.api.libs.json.JsValue

class ChatRoomActor(out: ActorRef) extends Actor {
  def receive = {
    case msg: JsValue => {
      out ! msg
    }
  }
}

object ChatRoomActor {
  def props(out: ActorRef) = Props(new ChatRoomActor(out))
}
