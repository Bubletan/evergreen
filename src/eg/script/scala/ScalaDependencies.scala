package eg.script.scala

import java.lang.reflect.ParameterizedType

import eg.Server
import eg.game.event.Event
import eg.game.event.EventListener
import eg.game.model.Charactor
import eg.game.model.npc.Npc
import eg.game.model.player.Player
import eg.net.game.out._

object ScalaDependencies {
  
  // global constants
  
  val World = Server.world
  
  
  // shortened syntax for adding event listeners
  
  import scala.reflect.ClassTag
  import scala.reflect._
  
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
  
  
  // default types
  
  type Coordinate = eg.game.world.Coordinate
  
  
  // char helpers
  
  implicit class CharHelpers(char: Charactor) {
    
    def coordinate = char.getCoordinate
  }
  
  
  // player helpers
  
  implicit class PlayerHelpers(player: Player) {
    
    // getters & setters?
    def username = player.getUsername
    def password = player.getUsername
    def password_=(value: String) = player.setPassword(value)
    def hash = player.getHash
    def member = player.isMember
    def privilege = player.getPrivilege
    
    // packet senders
    def message(message: String) = player.getSession.send(new GameMessagePacket(message))
    
    // other
    def tele(x: Int, y: Int) = player.getMovement.setCoordinate(new Coordinate(x, y))
    def tele(x: Int, y: Int, height: Int) = player.getMovement.setCoordinate(new Coordinate(x, y, height))
  }
  
  
  // npc helpers
  
  implicit class NpcHelpers(npc: Npc) {
    
  }
}