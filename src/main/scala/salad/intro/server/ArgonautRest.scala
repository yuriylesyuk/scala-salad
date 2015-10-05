package salad.intro.server

import com.typesafe.scalalogging._

import net.liftweb.http.rest.RestHelper
import net.liftweb.http.LiftRules

import net.liftweb.http.OkResponse
import net.liftweb.http.PlainTextResponse

import salad.intro.Users
import argonaut.http.ArgonautResponse

import scalaz._, Scalaz._
import argonaut._, Argonaut._

object ArgonautRest extends RestHelper with LazyLogging{

  serve ( "api" / "v1" prefix {
    case "ping" :: Nil JsonGet req => OkResponse()
    case "usersecho" :: Nil Post req => {
      /////
      // the request feeds back to the client a Vector of Users
      // unmarchalled and marchalled by Argonaut
      
      // ArgonautRequest() proto-hack
      // json body is assumed and forced
      // TODO: infer text charset from req.contentType
                
      // body is Array[Byte]
      val json = new String(req.body.get)
      
      val users: Vector[Users] = json.decodeOption[Vector[Users]].getOrElse(Vector.empty)
      ////
      // I.e.: 
      // [{"id":1,"name":"Janes","age":25,"position":"Developer"},{"id":1,"name":"Watson","age":25,"position":"Manager"}]
      // converts to:
      // val users = Vector(
      //    Users(1, "Janes", 25, "Developer"),
      //    Users(1, "Watson", 25, "Manager")
      //  ) 

        ArgonautResponse( users.asJson )
      }
    //case "users" :: Nil JsonGet req => JsonResponse( decompose( Users(1,"J", 15, "D") ))
  })
  
  def init(): Unit = {
    LiftRules.statelessDispatch.append(ArgonautRest)
  }
  
  
  
}