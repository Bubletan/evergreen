import eg.game.event.player.impl._

on ((e: CommandEvent) => {
  if (e.getCommand == "hello")
    e.getAuthor mes "Hello, Scala!"
})

on command ((plr, cmd) => {
  if (cmd == "hello2")
    plr mes "Hello, Scala! 2"
})