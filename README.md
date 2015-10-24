Scala SALAD
===========

SALAD: 
Slick
Argonaut
Lift
Angular
Derby

The project is meant to be a step-by-step-no-steps-missed walkthrough to create a generic but useful setup for multi-tier web-based Scala application, using  Derby as a database backend, Slick as an FRM toolkit and argonaut as a Json processor. 

# Walkthrough

## Define `SALAD_HOME`

1. Create SALAD directory
```bash
mkdir ~/SALAD
```
1. Define `SALAD_HOME` environment variable
```bash
export SALAD_HOME=~/SALAD
```
1. Create `$SALAD/saladenv.sh` file that will contain environment configuration.
Add previous command to it.
1. Create `_downloads` directory
```bash
mkdir $SALAD_HOME/_downloads
```



## Install Jetty

1. Download .zip file from http://download.eclipse.org/jetty/ into 	`$SALAD_HOME/_downloads`.
1. Unzip its contents into `$SALAD_HOME`
	```bash
	unzip $SALAD_HOME/_downloads/jetty-distribution-9.3.5.v20151012.zip -d $SALAD_HOME
	```
1. Export JETTY_HOME environment variable
	```bash
	export JETTY_HOME=$SALAD_HOME/jetty-distribution-9.3.5.v20151012
	```
1. Append `JETTY_HOME/bin` to the `PATH` variable
	```bash
    PATH=$PATH:$JETTY_HOME/bin
	```
**Note:** Add JETTY_HOME definition to the `saladenv.sh` file

## Test Jetty Installation

1. Run `java -jar $JETTY_HOME/start.jar --module=http jetty.http.port=8080` command.
2. In a browser navigate to `http://localhost:8080/`. Observe response from the server.
3. To exit the jetty, press `Ctrl+C`

## Install Derby DB

> **NOTE:** Open separate Terminal window for Derby DB so you can leave its instance running.

1. Download latest Derby DB distribution `db-derby-*-bin.zip` file (i.e., `10.12.1.1`) from http://db.apache.org/derby/releases/ .cgi link into `$SALAD_HOME/_downloads`
1. Unzip its contents into `$SALAD_HOME`
	```bash
    unzip $SALAD_HOME/_downloads/db-derby-10.12.1.1-bin.zip -d $SALAD_HOME
    ```
1. Export DERBY_HOME environment variable
	```bash
	export DERBY_HOME=$SALAD_HOME/db-derby-10.12.1.1-bin
	```
1. Append `$DERBY_HOME/bin` to the `PATH` variable
	```bash
    PATH=$PATH:$DERBY_HOME/bin
	```

## Test Derby DB Installation

**Note:** Add DERBY_HOME, DERBY_OPTS, and DERBY_HOME/bin definitions to the `saladenv.sh` file

1. Create a directory for database files
	```bash
	mkdir $SALAD_HOME/db-derby-home
    ```
1. Creave Derby DB configuration with db-derby-home directory, assuming default default port `1527`
	```bash
    export DERBY_OPTS=-Dderby.system.home=$SALAD_HOME/db-derby-home 
    ```

1. Start the database in the server mode
	```bash
    startNetworkServer
    ```
    Upon successful start you will see diagnostic output
    ```
    startNetworkServer
	Wed Oct 21 23:26:39 BST 2015 : Security manager installed using the Basic server security policy.
	Wed Oct 21 23:26:40 BST 2015 : Apache Derby Network Server - 10.12.1.1 - (1704137) started and ready to accept connections on port 1527
    ```
1. Open new Terminal window. Source the environment
	```bash
    source ~/SALAD/saladenv.sh
    ```
1. Launch derby interative scripting tool utility ij
	```bash
    ij
    ```
1. Type in `connect` ij command with ;create=true prefix to create test-db database
	```bash
    connect 'jdbc:derby://localhost:1527/test-db;create=true';
    ```
	The database should be created in $SALAD_HOME/db-derby-home directory
    ```
    $ ls -ls $SALAD_HOME/db-derby-home 
	total 8
	8 -rw-r--r--  1 nz  staff  187 21 Oct 23:29 derby.log
	0 drwxr-xr-x  9 nz  staff  306 21 Oct 23:28 test-db
    ```
1. Output contents of `sys.systables` table
	```
    ij> select tablename from sys.systables;
	TABLENAME                  
    ---------------------------
	SYSALIASES                                       
	SYSCHECKS                                         
	SYSCOLPERMS                                       
	SYSCOLUMNS                 
	...
	SYSTABLES                                         
	SYSTRIGGERS                                       
	SYSUSERS                                         
	SYSVIEWS                                         

	23 rows selected
	ij>
    ```
1. Enter `exit;` to quit ij.


## Setting up Scala project

1. Go to `$SALAD_HOME` directory `cd $SALAD_HOME`.
1. Create scala-salad directory `mkdir scala-salad`.
1. Go to the `scala-salad` directory `cd scala-salad`.

1. In the project directory create file `build.sbt`
	```
    name := "salad-intro"

	version := "1.0"

	scalaVersion := "2.11.6"

	libraryDependencies ++= Seq(
    	"com.typesafe.slick" %% "slick" % "3.0.0",
		"org.slf4j" % "slf4j-nop" % "1.6.4",
		"org.apache.derby" % "derbyclient" % "10.11.1.1"
	)
    ```
1. Create file projects/plugins.sbt
	```
    addSbtPlugin("com.typesafe.sbteclipse" % "sbteclipse-plugin" % "2.5.0")
    ```
1. In the project folder run `sbt` command. 
1. After the sbt prompt appear run `eclipse` sbt command.
	The `.classpath`, `.project` and project folder structure will be created.
    
1. Launch **ScalaIDE** and import project
	
    File\Import ...\Existing Project
    
    Choose $SALAD_HOME/scala-salad folder, click `Open`.
    
    Confirm project import defaults by selecting `Finish`.
    
# Slick and Derby. 

## Generate DDL

1. Right-click on a `src/main/scala` node and select `Create package`

1. Enter salad.intro as a package name under `src/main/scala`.

1. In the `src/main/resources` create `application.conf` file
	```
    test-db = {
		url = "jdbc:derby://localhost:1527/test-db"
		driver = "org.apache.derby.jdbc.ClientDriver"
		connectionPool = disabled
		keepAliveConnection = true
	}
    ```
1. In the `salad.intro` package create Scala Object `SlickDerby.scala`
	```scala
    package salad.intro

	import slick.driver.DerbyDriver.api._
	import scala.concurrent.ExecutionContext.Implicits.global
	import scala.concurrent.Await
	import scala.concurrent.duration._

	case class Users(id: Int, name: String, age: Int, role: String)

	class UsersTable (tag:Tag) extends Table[Users](tag, "USERS") {
	  def id = column[Int]("ID", O.PrimaryKey, O.AutoInc)
	  def name = column[String]("NAME")
	  def age = column[Int]("AGE")
	  def role = column[String]("ROLE")
  
	  def * = (id,name, age, role) <> (
	      (Users.apply _).tupled, Users.unapply)
	}

	object SlickDerbyGenerateDDL extends App{
 	  Database.forConfig("test-db")
  
	  val users = TableQuery[UsersTable]
  	  users.ddl.createStatements.foreach(println)
 
	}    
    ```
	Copy generated create table statement
    ```
    create table "USERS" (
    	"ID" INTEGER NOT NULL PRIMARY KEY GENERATED BY DEFAULT AS IDENTITY,
        "NAME" VARCHAR(254) NOT NULL,
        "AGE" INTEGER NOT NULL,
        "ROLE" VARCHAR(254) NOT NULL
    )
    ```
# Create Table Using ij and Select Records Using Slick
1. In the Terminal window with sourced environment, start ij
1. Execute `connect 'jdbc:derby://localhost:1527/test-db;';`
1. Execute create table statement. Don't forget ; at the end.
1. Run `describe users;` to display newly created table.

	```bash
	ij> describe users;
	```	

 	| COLUMN_NAME | TYPE_NAME | DEC& | NUM& | COLUM& | COLUMN_DEF | CHAR_OCTE& | IS_NULL&	|
	| ----------- | ------- | -------- | --------- | ----------- | ------------| ------- | -----|
 	| ID                  | INTEGER  | 0   | 10  | 10    | GENERATED& | NULL      | NO  |
    | NAME                |VARCHAR  |NULL|NULL|254   |NULL      |508       |NO  |
    | AGE                 |INTEGER  |0   |10  |10    |NULL      |NULL      |NO  |
    | ROLE                |VARCHAR  |NULL|NULL|254   |NULL      |508       |NO  |
    
    4 rows selected
    
1. Execute `insert table` statements
    ```
    insert into users values (1, 'Jones', 25, 'Developer');
	insert into users values (2, 'Watson', 27, 'Manager');
    ```
    In the `SickDerby.scala` add `SlickDerbySelectRecords` object
    ```scala
    object SlickDerbySelectRecords extends App {
  	  val db = Database.forConfig("test-db")
  	  val users = TableQuery[UsersTable]
  	  val actions = for {
	    all <- users.result
	  } yield all
  	println( "ResultSet:")
  	val future = db.run(actions).map { _ foreach println }
  	Await.result( future, 2 seconds)
	}
    ```
    The output should look like
    ```
    ResultSet:
	Users(1,Jones,25,Developer)
	Users(2,Watson,27,Manager)
    ```

# Argonaut

1. Add dependency to the `build.sbt`
	```scala
    "io.argonaut" %% "argonaut" % "6.0.4"
 	```
1. On sbt prompt execute `reload` and `eclipse` commands. Refresh Eclipse workspace.

1. Refactor users case class from `SlickDerby.scala` into separate file Users.scala. 
   Add implicit casecodec for the JSON generator. `Users.scala`:
   ```scala
   package salad.intro

	import scalaz._, Scalaz._
	import argonaut._, Argonaut._

	case class Users(id: Int, name: String, age: Int, role: String)

	object Users {
	  implicit def UsersCodecJson: CodecJson[Users] =
	    casecodec4(Users.apply, Users.unapply)("id","name","age","role")
	}
	```

1. In the `salad.intro` package, create `ArgonautUser.scala` file
	```scala
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
    ```
1. Execute ArgonautUsers object. The output should be
	```json
    [{"id":1,"name":"Janes","age":25,"role":"Developer"},{"id":1,"name":"Watson","age":25,"role":"Manager"}]
Vector(Users(1,Janes,25,Developer), Users(1,Watson,25,Manager))
    ```


# Lift and Jetty

## Setup libraries

1. Add plugin dependency to the project/plugins.sbt
	```bash
    addSbtPlugin("com.earldouglas" % "xsbt-web-plugin" % "1.1.0")
    ```
1. Add scala-logging dependency
	```bass
    "com.typesafe.scala-logging" %% "scala-logging" % "3.1.0"
    ```

1. Add libraryDependencies section to the `build.sbt`
	```bash
	    libraryDependencies ++= {
		val liftVersion = "2.6-RC1"
		Seq(
			"net.liftweb" %% "lift-webkit" % liftVersion % "compile",
			"net.liftweb" %% "lift-json" % liftVersion % "compile"
		)
	}

	jetty()
	```
    
1. On the sbt prompt execute `reload` and `eclipse` commands. Refresh Eclipse project to load new dependencies.
 
## Create simple lift project

1. Open src and then main nodes in the Package Explorer. Create new folders `webapp` in `src/main` and folder WEB-INF in it.
1. In the `WEB-INF` Create `web.xml` with following contents
	```xml
    <web-app xmlns="http://java.sun.com/xml/ns/javaee" version="2.5">
		<filter>
			<filter-name>LiftFilter</filter-name>
			<display-name>List Filter</display-name>
			<description>the filter that intercepts lift calls</description>
			<filter-class>net.liftweb.http.LiftFilter</filter-class>
		</filter>
		<filter-mapping>
			<filter-name>LiftFilter</filter-name>
			<url-pattern>/*</url-pattern>
		</filter-mapping>
	
		<static-files>
			<include path="/favicon.ico"/>
		</static-files>
	</web-app>
    ```
1. In the `src/main/scala` create new package `bootstap.liftweb`
1. Create new Lift Scala Boot class `Boot.scala`
	```scala
    package bootstrap.liftweb

	import net.liftweb._
	import http._

	import provider.HTTPParam
	import salad.intro.server.LiftRest

	class Boot {
	  def boot{
    
	    // Allow Cross-Origin Resource Sharing
	    LiftRules.supplimentalHeaders = s => s.addHeaders(
	      List(HTTPParam("X-Lift-Version", LiftRules.liftVersion),
	        HTTPParam("Access-Control-Allow-Origin", "*"),
	        HTTPParam("Access-Control-Allow-Credentials", "true"),
	        HTTPParam("Access-Control-Allow-Methods", "GET, POST, PUT, OPTIONS"),
	        HTTPParam("Access-Control-Allow-Headers", "WWW-Authenticate,Keep-Alive,User-Agent,X-	Requested-With,Cache-Control,Content-Type")
	      ))
      
	    LiftRest.init()
	    }
	}
    ```
1. Create `LiftRest.scala` in the `src/main/scala/salad.intro.server` package
	```scala
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
	```
## Deploy and Test the Application

1. To generate .ware file in the sbt prompt execut `package`

1. On the sbt prompt run jetty container 
	```bash
    container:start
    ```
1. In a browser navigate to `http://localhost:8080/api/v1/list`. You should see a test JSON hard-coded datagram.
1. Navigate to `http://localhost:8080/api/v1/users`. The Lift json has generated a JSON datagram from Scala Users Object.
1. Stop the jetty server by pressing Enter in the sbt window and entering 
	```bash
    container:stop
    ```
    
> **NOTE:*** To reload automatically when files change, use following sbt command
> ```bash
> ~;container:start; container: reload /
> ```

