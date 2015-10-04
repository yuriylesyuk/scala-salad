package salad.intro.server

import com.typesafe.scalalogging._

import net.liftweb.http.rest.RestHelper
import net.liftweb.http.LiftRules

import net.liftweb.http.OkResponse
import net.liftweb.http.PlainTextResponse

import salad.intro.Users
import argonaut.http.ArgonautResponse
import argonaut._, Argonaut._

object ArgonautRest extends RestHelper with LazyLogging{

  serve ( "api" / "v1" prefix {
    case "ping" :: Nil JsonGet req => OkResponse()
    case "list" :: Nil JsonGet req => {
       val users = Vector(
          Users(1, "Janes", 25, "Developer"),
          Users(1, "Watson", 25, "Manager")
        ) 

        ArgonautResponse( users.asJson )
      }
    //case "users" :: Nil JsonGet req => JsonResponse( decompose( Users(1,"J", 15, "D") ))
  })
  
  def init(): Unit = {
    LiftRules.statelessDispatch.append(ArgonautRest)
  }
  
  
  
}