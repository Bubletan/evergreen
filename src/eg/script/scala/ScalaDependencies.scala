package eg.script.scala

import scala.language.dynamics
import scala.reflect.ClassTag
import scala.reflect._

import java.lang.reflect.ParameterizedType

import eg.Server
import eg.game.event.Event
import eg.game.event.EventListener
import eg.game.model.Charactor
import eg.game.model.npc.Npc
import eg.game.model.player.Player
import eg.game.world.Coordinate
import eg.net.game.out._

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
  
  
  // event types
  
  import eg.game.event.impl._
  
  type Button = ButtonEvent
  type Command = CommandEvent
  type Movement = MovementEvent
  
  
  // implicit conversions
  
  implicit def tuple2ToCoordinate(t: (Int, Int)) = new Coordinate(t._1, t._2)
  implicit def tuple3ToCoordinate(t: (Int, Int, Int)) = new Coordinate(t._1, t._2, t._3)
  
  
  // dynamic helpers
  
  implicit class DynamicHelpers(char: Charactor) extends Dynamic {
    
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