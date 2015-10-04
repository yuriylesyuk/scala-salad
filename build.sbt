name := "salad-intro"

version := "1.0"

scalaVersion := "2.11.6"

scalacOptions := Seq("-unchecked", "-deprecation", "-encoding", "utf8")

EclipseKeys.withSource := true

libraryDependencies ++= Seq(
	"com.typesafe.scala-logging" %% "scala-logging" % "3.1.0",

	"com.typesafe.slick" %% "slick" % "3.0.0",
	"org.slf4j" % "slf4j-nop" % "1.6.4",
	
	"org.apache.derby" % "derbyclient" % "10.11.1.1",
	"io.argonaut" %% "argonaut" % "6.0.4",
	"io.argonaut" %% "argonaut-unfiltered" % "6.0.4"	
)

libraryDependencies ++= {
	val liftVersion = "2.6-RC1"
	Seq(
		"net.liftweb" %% "lift-webkit" % liftVersion % "compile"
	)
}

jetty()
