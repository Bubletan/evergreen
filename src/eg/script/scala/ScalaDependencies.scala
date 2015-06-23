package eg.script.scala

import scala.language.dynamics
import scala.language.experimental.macros
import scala.reflect.ClassTag
import scala.reflect._

import eg.Server
import eg.game.event.{Event, EventListener}
import eg.game.model.MobileEntity
import eg.game.model.item.{Item, ItemType}
import eg.game.model.npc.{Npc, NpcType}
import eg.game.model.player.Player
import eg.game.model.`object`.{Object, ObjectType}
import eg.game.world.Coordinate
import eg.game.event.impl._

object ScalaDependencies {
  
  // default constants, variables, methods, imports, 'constructors' etc.
  
  val World = Server.world
  def Cycle = Server.cycle
  
  object Item {
    def apply(typ: ItemType) = new Item(typ)
    def apply(typ: ItemType, quantity: Int) = new Item(typ, quantity)
  }
  
  
  // implicit conversions and helpers
  
  implicit def tuple2ToCoordinate(t: (Int, Int)) = new Coordinate(t._1, t._2)
  implicit def tuple3ToCoordinate(t: (Int, Int, Int)) = new Coordinate(t._1, t._2, t._3)
  implicit class CoordinateHelpers(c: Coordinate) {
    def x = c.getX
    def y = c.getY
    def height = c.getHeight
  }
  implicit def tuple2ToCoordinateHelpers(t: (Int, Int)) = new CoordinateHelpers(t)
  implicit def tuple3ToCoordinateHelpers(t: (Int, Int, Int)) = new CoordinateHelpers(t)
  
  implicit def function0ToTask(f: => Unit) = new eg.util.task.Task {
    def execute = f
  }
  
  implicit def intToItemType(id: Int) = ItemType.get(id)
  implicit def intToNpcType(id: Int) = NpcType.get(id)
  implicit def intToObjectType(id: Int) = ObjectType.get(id)
  
  
  // shortened syntax for adding event listeners
  
  def on[E <: Event: ClassTag](action: E => Unit): Boolean = {
    val classOfE = classTag[E].runtimeClass.asInstanceOf[Class[E]]
    World.getEventDispatcher.addEventListener(classOfE, new EventListener[E] {
      def onEvent(event: E) = action(event)
    })
  }
  
  
  // shortened syntax for dispatching events
  
  def fire[E <: Event](event: E): Boolean =
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
  
  
  // mobile entity helpers
  
  implicit class MobileEntityHelpers(me: MobileEntity) {
    import eg.game.world.sync._
    import eg.game.model._
    
    def forceMovement = ???
    def effect = ???
    def anim = ???
    def forceChat = ???
    def target = ???
    def turn = ???
    def hit() {
      // TODO reduce health
      val set = me.getSyncBlockSet
      import SyncBlock.{PrimaryHit, SecondaryHit}
      if (!set.contains(SyncBlock.Type.PRIMARY_HIT)) set.add(new PrimaryHit(???, ???, ???))
      else if (!set.contains(SyncBlock.Type.SECONDARY_HIT)) set.add(new SecondaryHit(???, ???, ???))
    }
    
    def attr = new AttributeHelper
    class AttributeHelper private[MobileEntityHelpers] {
      def apply[A](name: String): A = me.getAttributes.getOrDeclareAttribute(name).getValue[A]
      def update[A](name: String, value: A): Unit = me.getAttributes.getOrDeclareAttribute(name).setValue(value)
    }
  }
  
  
  /*
   * Experimental feature to allow using bean getters and setters
   * 
   * a.%something      ~~~>  a.getSomething()
   * a.%something = b  ~~~>  a.setSomething(b)
   * 
   * To access directly, use:
   * 
   * (a.%something).something
   * (a%).something.something
   */
  implicit class BeanHelperCreator[T <: AnyRef](prefix: T) {
    def % = new BeanHelper[T](prefix)
  }
  
  
  // player helpers
  
  implicit class PlayerHelpers(player: Player) {
    import eg.net.game.out._
    private def send(packet: eg.net.game.AbstractGamePacket) = player.getSession.send(packet)
    
    def message(message: String) = send(new GameMessagePacket(message))
    
    def interface = ??? // TODO return current interface
    def interface_=(id: Int) = send(new GameInterfacePacket(id))
    def tab(tab: Int) = new TabHelper(tab)
    class TabHelper private[PlayerHelpers] (tab: Int) {
      def interface = ??? // TODO return current interface
      def interface_=(id: Int) = send(new TabInterfacePacket(tab, id))
      def select() = send(new TabSelectPacket(tab))
      def flash() = send(new TabFlashPacket(tab))
    }
    
    def camera = new CameraHelper
    class CameraHelper private[PlayerHelpers] {
      import CameraWavePacket.Type
      def reset() = player.getSession.send(new CameraResetPacket)
      def waveX(noise: Int, amplitude: Int, frequency: Int) = send(new CameraWavePacket(Type.X, noise, amplitude, frequency))
      def waveY(noise: Int, amplitude: Int, frequency: Int) = send(new CameraWavePacket(Type.Y, noise, amplitude, frequency))
      def waveVertical(noise: Int, amplitude: Int, frequency: Int) = send(new CameraWavePacket(Type.HEIGHT, noise, amplitude, frequency))
      def waveRoll(noise: Int, amplitude: Int, frequency: Int) = send(new CameraWavePacket(Type.ROLL, noise, amplitude, frequency))
      def waveYaw(noise: Int, amplitude: Int, frequency: Int) = send(new CameraWavePacket(Type.YAW, noise, amplitude, frequency))
      private def localXOf(coord: Coordinate) = coord.getX - player.getMovement.getSectorOrigin.getX
      private def localYOf(coord: Coordinate) = coord.getY - player.getMovement.getSectorOrigin.getY
      def turnTo(coord: Coordinate, height: Int) = send(new CameraFocusPacket(localXOf(coord), localYOf(coord), height))
      def turnTo(coord: Coordinate, height: Int, trans: Int, glide: Int) = send(new CameraFocusPacket(localXOf(coord), localYOf(coord), height, trans, glide))
      def moveTo(coord: Coordinate, height: Int) = send(new CameraPositionPacket(localXOf(coord), localYOf(coord), height))
      def moveTo(coord: Coordinate, height: Int, trans: Int, glide: Int) = send(new CameraPositionPacket(localXOf(coord), localYOf(coord), height, trans, glide))
    }
  }
  
  on[Button](e => {
    // TODO handle dialogue input
  })
  
  // npc helpers
  
  implicit class NpcHelpers(npc: Npc) {
    def transform(npct: NpcType) = ???
  }
}