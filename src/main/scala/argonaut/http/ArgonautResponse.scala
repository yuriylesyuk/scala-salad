package argonaut.http

import net.liftweb.http.LiftResponse
import net.liftweb.http.InMemoryResponse
import net.liftweb.http.provider.HTTPCookie
import net.liftweb.http.S
import argonaut._, Argonaut._

case class ArgonautResponse( json: Json, headers: List[(String, String)], cookies: List[HTTPCookie], code: Int ) extends LiftResponse{
  
  def toResponse = {
    val bytes = json.toString.getBytes("UTF-8")
    InMemoryResponse(bytes, ("Content-Length", bytes.length.toString) :: ("Content-Type", "application/json; charset=utf-8") :: headers, cookies, code)
  }
}
object ArgonautResponse {
  def headers: List[(String, String)] = S.getResponseHeaders(Nil)
  def cookies: List[HTTPCookie] = S.responseCookies
  
  def apply(json: Json): LiftResponse = new ArgonautResponse(json, headers, cookies, 200)
}
  