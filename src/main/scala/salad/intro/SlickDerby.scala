package salad.intro

import slick.driver.DerbyDriver.api._
import scala.concurrent.ExecutionContext.Implicits.global

class UsersT (tag:Tag) extends Table[Users](tag, "USERS") {
  def id = column[Int]("ID", O.PrimaryKey, O.AutoInc)
  def name = column[String]("NAME")
  def age = column[Int]("AGE")
  def position = column[String]("POSITION")
  
  def * = (id,name, age, position) <> (
      (Users.apply _).tupled, Users.unapply)
}


object SlickDerby extends App{
  
  val db = Database.forConfig("test-db")
  
  val users = TableQuery[UsersT]
  try {    
    
    db.run(users.result) map( _ foreach { println } )
    
  } finally db.close()
  
  Thread.sleep(10000)
}