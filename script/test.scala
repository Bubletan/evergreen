
on[Command](e => {
  val player = e.getAuthor
  val command = e.getCommand
  command match {
    case "select" => player tab(3) select
    case "flash" => player tab(3) flash
    case "sail" => playe.camera.waveRoll(0, 200, 100)
    case _ => player message ("Unknown command: " + command)
  }
})