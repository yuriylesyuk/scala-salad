Scala SALAD
===========

SALAD: 
Slick
Argonaut
Lift
Angular
Derby

The scala-salad project is a complete step-by-step walkthrough that defines a functionally complete set of components for a lightweight multi-tier web application.

The server side exposes services using REST/JSON. Lift is the web framework, used for the stack. DerbyDB is selected as a database back-end. The code uses two JSON processing frameworks: Lift JSON and Argonaut. My experience is that lift-json is a solid piece of engineering, but Argonaut implementation is more elegant. Slick is selected as an FRM framework.

The client side is written in Angular2. The important intentional decision was to use TypeScript for the client implementation, as TypeScript makes the usage of functional idioms so much easier and supports many FP constructs on a language level. An ability to write type-safe code, even with existing limitations of the type system,  facilitates a better quality of the code.

## Tools Used

|**Toolkit**| |
|----- |------|
|sbt       | simple build tool
|ScalaIDE  |
|VSCode    | TypeScript Editor, as well as markdown and many other formats.
|Postman   | REST Client Chrome application
|npm       | javascript shell
|tsc       | TypeScript compiler
|liveserver  | light http server with automatic update of changes files
|**Servers** |
|Derby       | Database
|Jetty       | Http server
|**Languages and Frameworks**|
|Scala       | The Scala
|Lift        | 
|Argonaut    |
|Angular2    |

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

## Refactor Lift JSON into Argonaut

Argonaut doesn't work out of the box with the Lift. To generate http response from JSON datagram, let's create the ArgonautResponse class and object that complies with the Lift response framework. 

1. Int the `argonaut.http` package, create `ArgonautResponse.scala`
	```scala
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
   ```

1. In the `salad.intro.server` create `ArgonautRest.scala` so that the Argonaut responses are returned.

	```scala
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
	      // [{"id":1,"name":"Janes","age":25,"role":"Developer"},{"id":1,"name":"Watson","age":25,"role":"Manager"}]
	      // converts to:
	      // val users = Vector(
	      //    Users(1, "Janes", 25, "Developer"),
	      //    Users(1, "Watson", 25, "Manager")
	      //  ) 
	
	        ArgonautResponse( users.asJson )
	      }
	      case "users" :: Nil Get req => ArgonautResponse( Vector(
	          Users(1, "Janes", 25, "Developer"),
	          Users(1, "Watson", 25, "Manager")
	        ).asJson )
	  })
	  
	  def init(): Unit = {
	    LiftRules.statelessDispatch.append(ArgonautRest)
	  }  
	}    
    ```
    
 1. Change `bootstrap.liftweb.Boot.scala` class to use ArgonautRest. Replace `LiftRest` by `ArgonautRest`

1. On sbt prompt run `package`, then `container:start`.

1. In the browser navigate to `http://localhost:8080/api/v1/users`. You should see as a result the JSON array of Users objects.

    ```json
    [{"id":1,"name":"Janes","age":25,"role":"Developer"},{"id":1,"name":"Watson","age":25,"role":"Manager"}]
    ```

## Using Postman to interact with REST service
The Postman Google Chrome application can be used to interact with our server by sending and receiving JSON requests. We are going to use `usersecho` request to POST a user object, then use Argonaut for round-trip communication: to unmarchall it into Scala Users object and marchall it back into a JSON response.
    
The `/api/v1/userecho` service accepts JSON datagram, converts it into scala object using Argonaut, then sends the object, packaging it into an ArgonautResponse.

1. Install [Postman](https://chrome.google.com/webstore/detail/postman/fhbjgbiflinjbdggehcddcbncdddomop?hl=en) REST Client application  from the Chrome Web Store.

1. Open the Postman. Configure the POST operatin with `localhost:8080/api/v1/usersecho` URL.

1. Select Row JSON request and enter 
	```json
    [{"id":1,"name":"Janes","age":25,"role":"Developer"}]
    ```
    as a request datagram.
    
1. Press `Send` button. You should obtain JSON response:
	```json
    [
	    {
    	    "id": 1,
	        "name": "Janes",
	        "age": 25,
	        "role": "Developer"
	    }
	]
    ```

## >>Read Records from the Database>

???

# Angular2 Simple Client 

## Setting up Development Environment and Angular2 Project

> NOTE: while working on a client application and developing angular-only code, we want to optimise edit-run cycle by using live-server.
 
1. Launch VSCode and use `Open Folder` button to open `src/main/webapp` folder of the scala-salad project.

1. Create `package.json` file
	```json
    {
	  "name": "userscomponent",
	  "version": "0.0.1",
	  "dependencies": {
	    "angular2": "2.0.0-alpha.42",
	    "systemjs": "0.19.2"
	  },
	  "devDependencies": {
	    "live-server": "latest"
	  },
	  "scripts": {
	    "tsc": "tsc -p src/webapp -w",
	    "liveserver": "live-server --no-browser --port=9090 --open=src/webapp"
	  }
	}
    ``` 
    
1. Execute `npm install` to load dependencies and live-server.
1. Create index.html file with `Hello, World!` contents.
1. Execute `npm run liveserver` command to start local server on port 9090.    
1. In a browser, navigate to localhost:9090/ URL. The `Hello, World!` page should be displayed.

## Creating Simple Angular2 Application

1. In the `webapp` folder create `app` folder
1. In the `app` folder create tsconfig.json file
	```json
    {
		"compilerOptions": {
			"module": "commonjs",
			"target": "es5",
			"sourceMap": true,
			"emitDecoratorMetadata": true,
			"experimentalDecorators": true,
			"noImplicitAny": false
		}		
	}
    ```
1. In a separate window launch `npm run tsc` command.

1. Create `app/usersapp.ts` TypeScript file
	```javascript
    import {bootstrap,Component} from 'angular2/angular2';

	@Component({
		selector: 'users-app',
		template: `
			<h2>Hello, World</h2>
		`	
	})
	export class UsersApp{
		constructor(){}
	}

	bootstrap(UsersApp, []);
    ```
1. Edit `index.html` file
	```html
    <html>
	<head>
		<title>Users Component</title>
     
	    <script src="node_modules/systemjs/dist/system.src.js"></script>
	    <script src="node_modules/angular2/bundles/angular2.js"></script>
	
	    <script>
	        System.config({
	            packages: {'app': {defaultExtension: 'js'}}
	        });
	        System.import('app/usersapp').catch(console.log.bind(console));
	    </script>
	</head>
	<body>	
	    <users-app></users-app>
	</body>
	</html>
	```
1. In brower refresh `localhost:9090` page. You will see `Hello World!`, but don't be fooled: this is a full-fetured Angular2 application. 

## Application fetching list of Users

1. In the `app` folder, create usermodel.ts file.
	```typescript
	    export class User{
		id: number;
		name: String;
		age: number;
		role: string;	
	}
    ```

1. Create `app/service.ts` file
	```typescript
	import {Http} from 'angular2/http';
	import {Injectable} from 'angular2/angular2';
	import {User} from './usermodel';

	@Injectable()
	export class UsersService {
		users: User[] = [];
	
		constructor( private _http: Http) {}
	
		getUsers(): Promise<User[]> {
			this.users.length = 0;
			let promise = this._http.get('http://localhost:8080/api/v1/users')
				.map((response: any) => response.json()).toPromise()
				.then((users: User[])=> {
					this.users.push(...users);
					return this.users;
				})	
				.then((_: any) => _, (e: any) => this._fetchFailed(e));
					return promise;
		}
		private _fetchFailed(error:any) {
			console.error(error);
			return Promise.reject(error);
		}
	}    
    ```
    
    
1. Refactor app.ts class to define `AppComponent`
	```typescript
	import {bootstrap,Component,CORE_DIRECTIVES} from 'angular2/angular2'
	import {HTTP_BINDINGS} from 'angular2/http';
	import {UsersService} from './service';
	import {User} from './usermodel';

	@Component({
		selector: 'users-app',
		template: `<h1>Hello</h1>
			<ul>
				<li *ng-for="#user of users">
					<span>{{user.id}}</span> {{user.name}} | {{user.age}}>
				</li>
			</ul>				
			`,
		directives: [CORE_DIRECTIVES]
	})
	class UsersApp {
		public users: User[];
	
		constructor(private _usersService: UsersService){
		
			this.getUsers();
		}
	
		getUsers(){
			this.users  = [];
			this._usersService.getUsers()
				.then(users =>
					this.users = users
				);
		}
	}

	bootstrap(UsersApp, [UsersService,HTTP_BINDINGS])    
    ```
1. Add http.dev.js link to the index.html
	```html
      <script src="node_modules/angular2/bundles/http.dev.js"></script>
    ```
1. Look at the browser window. The page should automatically re-draw contents as you save your changes.
	The final output should look like:
    ```
    Hello

	1 Janes | 25>
	1 Watson | 25>
    ```
    
## End-to-End
1. At the sbt prompt, start container and in the browser navigate to http://localhost:8080.
You should see same output, but this time it is generated by whole end-to-end processing.
