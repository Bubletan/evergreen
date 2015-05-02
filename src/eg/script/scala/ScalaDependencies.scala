package eg.script.scala

import java.lang.reflect.ParameterizedType

import eg.Server
import eg.game.event.EventListener
import eg.game.event.player.PlayerEvent
import eg.game.event.npc.NpcEvent
import eg.game.model.player.Player
import eg.net.game.out._

object ScalaDependencies {
  
  // allows shortened syntax for adding event listeners
  
  def onPlayerEvent[T <: PlayerEvent](action: T => Unit): Boolean = {
    
    val classOfT = action.getClass.getGenericSuperclass.asInstanceOf[ParameterizedType]
        .getActualTypeArguments()(0).asInstanceOf[Class[T]]
    
    Server.world.getPlayerEventDispatcher.addEventListener(classOfT, new EventListener[T] {
      def onEvent(event: T) = action(event)
    })
  }
  
  def onNpcEvent[T <: NpcEvent](action: T => Unit): Boolean = {
    
    val classOfT = action.getClass.getGenericSuperclass.asInstanceOf[ParameterizedType]
        .getActualTypeArguments()(0).asInstanceOf[Class[T]]
    
    Server.world.getNpcEventDispatcher.addEventListener(classOfT, new EventListener[T] {
      def onEvent(event: T) = action(event)
    })
  }
  
  // player helpers
  
  implicit class PlayerHelpers(player: Player) {
    def mes(m: String) = player.getSession.send(new GameMessagePacket(m))
  }
}