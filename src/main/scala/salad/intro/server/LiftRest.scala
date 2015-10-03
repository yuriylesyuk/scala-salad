package salad.intro.server

import com.typesafe.scalalogging._

import net.liftweb.http.rest.RestHelper
import net.liftweb.http.LiftRules

import net.liftweb.http.OkResponse
import net.liftweb.http.PlainTextResponse

import net.liftweb.http.JsonResponse
import net.liftweb.json.JsonAST._
import net.liftweb.json.JsonDSL._
import net.liftweb.json.Extraction._
import net.liftweb.json.Printer._
import net.liftweb.json.DefaultFormats

import salad.intro.Users

object LiftRest extends RestHelper with LazyLogging{

  serve ( "api" / "v1" prefix {
    case "ping" :: Nil JsonGet req => OkResponse()
    case "list" :: Nil JsonGet req => JsonResponse( ("xx"-> "A xx" ) ~ ("yy" -> "A yy" ) )
    case "users" :: Nil JsonGet req => JsonResponse( decompose( Users(1,"J", 15, "D") ))
  })
  
  def init(): Unit = {
    LiftRules.statelessDispatch.append(LiftRest)
  }
  
  
  
}