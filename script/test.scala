
on[Command] {
  case Command(s) if s matches "yell .+" => self.message(s.substring(5))
}

on[Button] {
  case Button(8654) if self.getUsername != "Someone" => self.message("Attack button in skill tab!")
}

def splitCommand(command: String) = {
  val indexOfSpace = command.indexOf(' ')
  if (indexOfSpace == -1) (command, Array.empty[String])
  else (command.substring(0, indexOfSpace), command.substring(indexOfSpace + 1).split(","))
}

on[Command] {
  case Command(cmd) => {
    val (command, args) = splitCommand(cmd)
    command match {
      case "select" => self.tab(3).select()
      case "flash" => self.tab(3).flash()
      
      case "camreset" => self.camera.reset()
      case "camwavex" => self.camera.waveX(args(0).toInt, args(1).toInt, args(2).toInt)
      case "camwavey" => self.camera.waveY(args(0).toInt, args(1).toInt, args(2).toInt)
      case "camwaveh" => self.camera.waveVertical(args(0).toInt, args(1).toInt, args(2).toInt)
      case "camwaveroll" => self.camera.waveRoll(args(0).toInt, args(1).toInt, args(2).toInt)
      case "camwaveyaw" => self.camera.waveYaw(args(0).toInt, args(1).toInt, args(2).toInt)
      case "cammove" => self.camera.moveTo(self.getCoordinate.translate(args(0).toInt, args(1).toInt), args(2).toInt)
      case "camturn" => self.camera.moveTo(self.getCoordinate.translate(args(0).toInt, args(1).toInt), args(2).toInt)
      case "cammovet" => self.camera.moveTo(self.getCoordinate.translate(args(0).toInt, args(1).toInt), args(2).toInt, args(3).toInt, args(4).toInt)
      case "camturnt" => self.camera.moveTo(self.getCoordinate.translate(args(0).toInt, args(1).toInt), args(2).toInt, args(3).toInt, args(4).toInt)
      
      case _ => self.message("Unknown command: " + command)
    }
  }
}
