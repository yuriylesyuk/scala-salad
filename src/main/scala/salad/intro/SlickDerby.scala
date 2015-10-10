package salad.intro

import slick.driver.DerbyDriver.api._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Await
import scala.concurrent.duration._

class UsersT (tag:Tag) extends Table[Users](tag, "USERS") {
  def id = column[Int]("ID", O.PrimaryKey)
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

object SlickUpsert extends App {
  
  val db = Database.forConfig("test-db")
  
  val users = TableQuery[UsersT]
  
  val actions = for {
    all <- users.result
  } yield all
  
  println( "ResultSet:")
  val future = db.run(actions).map { _ foreach println }
  Await.result( future, 2 seconds)
  
  val userMike: Users = Users(2,"Mike", 28, "Manager")

  
  val upsertNew: DBIO[Int] = users.insertOrUpdate(userMike)
  println(
      Await.result(db.run(upsertNew), 5 seconds)
  )
  
  
  println( "ResultSet:")
  Await.result(db.run(users.result).map { _ foreach println }, 2 seconds)
  
  db.close  
}
