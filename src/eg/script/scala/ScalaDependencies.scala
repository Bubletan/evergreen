package eg.script.scala

import scala.language.dynamics
import scala.language.experimental.macros
import scala.reflect._
import scala.util.DynamicVariable

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
  
  object Animation {
    import eg.game.model.Animation
    def apply(id: Int, delay: Int = 0) = new Animation(id, delay)
    def unapply(anim: Animation) = Some(anim.getId, anim.getDelay)
  }
  object Effect {
    import eg.game.model.Effect
    def apply(id: Int, height: Int = 100, delay: Int = 0) = new Effect(id, height, delay)
    def unapply(effect: Effect) = Some(effect.getId, effect.getHeight, effect.getDelay)
  }
  
  object Item {
    def apply(typ: ItemType, quantity: Int = 1) = new Item(typ, quantity)
    def unapply(item: Item) = Some(item.getType, item.getQuantity)
  }
  
  object ItemType {
    import eg.game.model.item.{ItemType => JItemType}
    def apply(id: Int) = JItemType.get(id)
    def unapply(typ: JItemType) = Some(typ.getId)
  }
  object NpcType {
    import eg.game.model.npc.{NpcType => JNpcType}
    def apply(id: Int) = JNpcType.get(id)
    def unapply(typ: JNpcType) = Some(typ.getId)
  }
  object ObjectType {
    import eg.game.model.`object`.{ObjectType => JObjectType}
    def apply(id: Int) = JObjectType.get(id)
    def unapply(typ: JObjectType) = Some(typ.getId)
  }
  
  object Coordinate {
    def apply(x: Int, y: Int, height: Int = 0) = new Coordinate(x, y, height)
    def unapply(c: Coordinate) = Some(c.getX, c.getY, c.getHeight)
  }
  
  
  // implicit conversions and helpers
  
  implicit def tuple2ToCoordinate(t: (Int, Int)) = new Coordinate(t._1, t._2)
  implicit def tuple3ToCoordinate(t: (Int, Int, Int)) = new Coordinate(t._1, t._2, t._3)
  
  implicit def function0ToTask(f: => Unit) = new eg.util.task.Task {
    def execute = f
  }
  
  
  // shortened syntax for adding event listeners
  
  private val dynSelf = new DynamicVariable[Player](null)
  
  def on[E <: Event: ClassTag](action: PartialFunction[E, Unit]): Boolean = {
    val classOfE = classTag[E].runtimeClass.asInstanceOf[Class[E]]
    World.getEventDispatcher.addEventListener(classOfE, new EventListener[E] {
      def onEvent(self: Player, event: E) {
        dynSelf.withValue(self) { if (action.isDefinedAt(event)) action(event) }
      }
    })
  }
  
  def self = dynSelf.value
  
  
  // shortened syntax for dispatching events
  
  def fire[E <: Event](event: E): Boolean =
    World.getEventDispatcher.dispatchEvent(self, event)
  
  
  // event types
  
  import eg.game.event.impl._
  
  type Button = ButtonEvent
  object Button {
    def apply(id: Int) = new Button(id)
    def unapply(event: Button) = Some(event.getId)
  }
  
  type Command = CommandEvent
  object Command {
    def apply(command: String) = new Command(command)
    def unapply(event: Command) = Some(event.getCommand)
  }
  
  type Movement = MovementEvent
  object Movement {
    def apply(coordinate: Coordinate) = new Movement(coordinate)
    def unapply(event: Movement) = Some(event.getCoordinate)
  }
  
  /*type PlayerOp1 = PlayerOptionOneEvent
  type PlayerOp2 = PlayerOptionTwoEvent
  type PlayerOp3 = PlayerOptionThreeEvent
  type PlayerOp4 = PlayerOptionFourEvent
  type PlayerOp5 = PlayerOptionFiveEvent*/
  
  
  // mobile entity helpers
  
  implicit class MobileEntityHelpers(me: MobileEntity) {
    import eg.game.world.sync._
    import eg.game.model._
    
    def attr = new AttributeHelpers
    class AttributeHelpers {
      def apply(ident: Symbol): Any = ???
      def update(ident: Symbol, value: Any): Unit = ???
    }
    
    def apply(ident: Symbol): Any = ???
    def update(ident: Symbol, value: Any): Unit = ???
    
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
  
  // npc helpers
  
  implicit class NpcHelpers(npc: Npc) {
    def transform(npct: NpcType) = ???
  }
}