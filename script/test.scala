
on[Command](_.getAuthor message "Command")

on[Button](_.getAuthor message "Button")

on[Movement](_.getAuthor message "Movement")

on[Command](e => {
  val plr = e.getAuthor
  plr attr("hello") = true
  val hello: Boolean = plr attr("hello")
  plr message hello.toString
})