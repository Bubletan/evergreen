
on[Button] {
  case Button(author, 8654) => author.message("Attack button in skill tab!")
  case Button(author, id) => author.message(s"Button: $id")
}

def splitCommand(command: String) = {
  val indexOfSpace = command.indexOf(' ')
  if (indexOfSpace == -1) (command, Array.empty[String])
  else (command.substring(0, indexOfSpace), command.substring(indexOfSpace + 1).split(","))
}

on[Command](e => {
  val player = e.getAuthor
  val (command, args) = splitCommand(e.getCommand)
  command match {
    case "select" => player.tab(3).select()
    case "flash" => player.tab(3).flash()
    
    case "camreset" => player.camera.reset()
    case "camwavex" => player.camera.waveX(args(0).toInt, args(1).toInt, args(2).toInt)
    case "camwavey" => player.camera.waveY(args(0).toInt, args(1).toInt, args(2).toInt)
    case "camwaveh" => player.camera.waveVertical(args(0).toInt, args(1).toInt, args(2).toInt)
    case "camwaveroll" => player.camera.waveRoll(args(0).toInt, args(1).toInt, args(2).toInt)
    case "camwaveyaw" => player.camera.waveYaw(args(0).toInt, args(1).toInt, args(2).toInt)
    case "cammove" => player.camera.moveTo(player.getCoordinate.translate(args(0).toInt, args(1).toInt), args(2).toInt)
    case "camturn" => player.camera.moveTo(player.getCoordinate.translate(args(0).toInt, args(1).toInt), args(2).toInt)
    case "cammovet" => player.camera.moveTo(player.getCoordinate.translate(args(0).toInt, args(1).toInt), args(2).toInt, args(3).toInt, args(4).toInt)
    case "camturnt" => player.camera.moveTo(player.getCoordinate.translate(args(0).toInt, args(1).toInt), args(2).toInt, args(3).toInt, args(4).toInt)
    
    case _ => player message ("Unknown command: " + command)
  }
})
