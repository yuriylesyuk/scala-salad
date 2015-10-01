package salad.intro

import scalaz._, Scalaz._
import argonaut._, Argonaut._

case class Users(id: Int, name: String, age: Int, position: String)

object Users {
  implicit def UsersCodecJson: CodecJson[Users] =
    casecodec4(Users.apply, Users.unapply)("id","name","age","position")
}
