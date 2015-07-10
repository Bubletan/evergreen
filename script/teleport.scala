val AlKharid       = (3293, 3174)
val ArdougneEast   = (2662, 3305)
val ArdougneWest   = (2529, 3307)
val Camelot        = (2757, 3477)
val DraynorVillage = (3093, 3244)
val DuelArena      = (3360, 3213)
val Edgeville      = (3093, 3493)
val Falador        = (2964, 3378)
val Lumbridge      = (3222, 3218)
val PortPhasmatys  = (3687, 3502)
val PortSarim      = (3023, 3208)
val Rimmington     = (2957, 3214)
val Varrock        = (3210, 3424)
val Yanille        = (2606, 3093)

on[Command] {
  case Command(player, command) => {
    val coord = command match {
      
      case "alkharid" => AlKharid
      case "eastardy" => ArdougneEast
      case "westardy" => ArdougneWest
      case "camelot" => Camelot
      case "draynor" => DraynorVillage
      case "duel" => DuelArena
      case "edge" => Edgeville
      case "fally" => Falador
      case "lumb" => Lumbridge
      case "phasmatys" => PortPhasmatys
      case "portsarim" => PortSarim
      case "rimmington" => Rimmington
      case "varrock" => Varrock
      case "yanille" => Yanille
      
      case _ => null
    }
    if (coord != null) 
      player.getMovement.setCoordinate(coord)
  }
}
