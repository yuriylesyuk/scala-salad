package salad.intro

import scalaz._, Scalaz._
import argonaut._, Argonaut._

object ArgonautUsers extends App{
  val users = Vector(
        Users(1, "Janes", 25, "Developer"),
        Users(1, "Watson", 25, "Manager")
      )
  
  val usersJson = users.asJson
  
  println( usersJson )
  
  // parse the Users Vector as a Json string
  val usersJsonString = usersJson.toString
  
  val parsedUsers = usersJsonString.decodeOption[Vector[Users]].getOrElse(Nil)
  
  println( parsedUsers )
}