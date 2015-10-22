# Scala SALAD Walkthrough

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
	export JETTY_HOME=$SALAD_HOME/jetty-distribution-9.3.3.v20151-12
	```
1. Append `JETTY_HOME/bin` to the `PATH` variable
	```bash
    PATH=$PATH:$JETTY_HOME/bin
	```
**Note:** Add JETTY_HOME definition to the `saladenv.sh` file
## Install Derby DB

**Note:** Open separate Terminal window for Jetty and Derby DB so you can leave their instances running in it.

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

1. Enter scala.intro as a package name under `src/main/scala`.

1. In the `src/main/resources` create `application.conf` file
	```
    test-db = {
		url = "jdbc:derby://localhost:1527/test-db"
		driver = "org.apache.derby.jdbc.ClientDriver"
		connectionPool = disabled
		keepAliveConnection = true
	}
    ```
1. In the `scala-intro` package create Scala Object `SlickDerby.scala`
	```scala
    package scala.intro

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
	```
	ij> describe users;
	COLUMN_NAME|TYPE_NAME|DEC&|NUM&|COLUM&|COLUMN_DEF|CHAR_OCTE&|IS_NULL&	
    ------------------------------------------------------------------------	
    ID                  |INTEGER  |0   |10  |10    |GENERATED&|NULL      |NO  
    NAME                |VARCHAR  |NULL|NULL|254   |NULL      |508       |NO  
    AGE                 |INTEGER  |0   |10  |10    |NULL      |NULL      |NO  
    ROLE                |VARCHAR  |NULL|NULL|254   |NULL      |508       |NO  
    4 rows selected
    ```
1. Execute `insert table` statements
	```
    insert into users values (1, 'Jones', 25, 'Developer');
	insert into users values (2, 'Watson', 27, 'Manager');
    ```
1. In the `SickDerby.scala` add `SlickDerbySelectRecords` object
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



