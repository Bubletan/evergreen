import eg.game.event.player.impl._

onPlayerEvent[CommandEvent](e => {
  e.getAuthor mes "Hello, Scala!"
})