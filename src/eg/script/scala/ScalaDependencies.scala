package eg.script.scala

import java.lang.reflect.ParameterizedType

import eg.Server
import eg.game.event.Event
import eg.game.event.EventListener
import eg.game.event.player.PlayerEvent
import eg.game.event.npc.NpcEvent
import eg.game.model.player.Player
import eg.net.game.out._

object ScalaDependencies {
  
  // allows shortened syntax for adding event listeners
  
  def on[T <: Event](action: T => Unit): Boolean = {
    
    val classOfT = action.getClass.getGenericSuperclass.asInstanceOf[ParameterizedType]
        .getActualTypeArguments()(0).asInstanceOf[Class[T]]
    
    return if (classOf[PlayerEvent].isAssignableFrom(classOfT)) {
      
      def on[E <: PlayerEvent](classOfE: Class[E], action: T => Unit) =
          Server.world.getPlayerEventDispatcher.addEventListener(classOfE, new EventListener[E] {
            def onEvent(event: E) = action.asInstanceOf[E => Unit](event)
          })
      on(classOfT.asInstanceOf[Class[_ <: PlayerEvent]], action)
      
    } else if (classOf[NpcEvent].isAssignableFrom(classOfT)) {
      
      def on[E <: NpcEvent](classOfE: Class[E], action: T => Unit) =
          Server.world.getNpcEventDispatcher.addEventListener(classOfE, new EventListener[E] {
            def onEvent(event: E) = action.asInstanceOf[E => Unit](event)
          })
      on(classOfT.asInstanceOf[Class[_ <: NpcEvent]], action)
      
    } else false
  }
  
  object on {
    
    import eg.game.event.player.impl._
    
    def command(action: (Player, String) => Unit) = on[CommandEvent](e => action(e.getAuthor, e.getCommand))
  }
  
  // player helpers
  
  implicit class PlayerHelpers(player: Player) {
    def mes(m: String) = player.getSession.send(new GameMessagePacket(m))
  }
}