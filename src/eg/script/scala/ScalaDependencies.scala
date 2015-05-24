package eg.script.scala

import scala.reflect.ClassTag
import scala.reflect._

import eg.Server
import eg.game.event.Event
import eg.game.event.EventListener
import eg.game.model.MobileEntity
import eg.game.model.npc.Npc
import eg.game.model.player.Player
import eg.game.world.Coordinate
import eg.net.game.out._
import eg.game.event.impl.ButtonEvent
import eg.game.event.impl.CommandEvent
import eg.game.event.impl.MovementEvent
import eg.game.event.impl.PlayerOptionFiveEvent
import eg.game.event.impl.PlayerOptionFourEvent
import eg.game.event.impl.PlayerOptionOneEvent
import eg.game.event.impl.PlayerOptionThreeEvent
import eg.game.event.impl.PlayerOptionTwoEvent

object ScalaDependencies {
  
  // global constants
  
  val World = Server.world
  
  
  // shortened syntax for adding event listeners
  
  def on[E <: Event: ClassTag](action: E => Unit): Boolean = {
    val classOfE = classTag[E].runtimeClass.asInstanceOf[Class[E]]
    World.getEventDispatcher.addEventListener(classOfE, new EventListener[E] {
      def onEvent(event: E) = action(event)
    })
  }
  
  
  // shortened syntax for dispatching events
  
  def dispatch[E <: Event](event: E): Boolean =
    World.getEventDispatcher.dispatchEvent(event)
  
  
  // event types
  
  import eg.game.event.impl._
  
  type Button = ButtonEvent
  type Command = CommandEvent
  type Movement = MovementEvent
  type PlayerOp1 = PlayerOptionOneEvent
  type PlayerOp2 = PlayerOptionTwoEvent
  type PlayerOp3 = PlayerOptionThreeEvent
  type PlayerOp4 = PlayerOptionFourEvent
  type PlayerOp5 = PlayerOptionFiveEvent
  
  
  // implicit conversions
  
  implicit def tuple2ToCoordinate(t: (Int, Int)) = new Coordinate(t._1, t._2)
  implicit def tuple3ToCoordinate(t: (Int, Int, Int)) = new Coordinate(t._1, t._2, t._3)
  
  
  class AttributeHelpers {
    def apply[A](key: String): A = ???
    def update(key: String, value: Any): Unit = ???
  }
  
  
  // char helpers
  
  implicit class MobileEntityHelpers(me: MobileEntity) {
    
    def attr = new AttributeHelpers
  }
  
  
  // player helpers
  
  implicit class PlayerHelpers(player: Player) {
    
    // packet senders
    def message(message: String) = player.getSession.send(new GameMessagePacket(message))
  }
  
  
  // npc helpers
  
  implicit class NpcHelpers(npc: Npc) {
    
  }
}