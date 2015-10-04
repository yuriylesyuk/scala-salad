package bootstrap.liftweb

import net.liftweb._
import http._

import provider.HTTPParam
import salad.intro.server.ArgonautRest

class Boot {
  def boot{
    
    // Allow Cross-Origin Resource Sharing
    LiftRules.supplimentalHeaders = s => s.addHeaders(
      List(HTTPParam("X-Lift-Version", LiftRules.liftVersion),
        HTTPParam("Access-Control-Allow-Origin", "*"),
        HTTPParam("Access-Control-Allow-Credentials", "true"),
        HTTPParam("Access-Control-Allow-Methods", "GET, POST, PUT, OPTIONS"),
        HTTPParam("Access-Control-Allow-Headers", "WWW-Authenticate,Keep-Alive,User-Agent,X-Requested-With,Cache-Control,Content-Type")
      ))
      
    ArgonautRest.init()
    }
}