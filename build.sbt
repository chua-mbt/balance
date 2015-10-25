import sbt.Project.projectToRef

lazy val clients = Seq(scalaJSClient)
lazy val scalaV = "2.11.7"

lazy val playServer = (project in file("play")).settings(
  scalaVersion := scalaV,
  scalaJSProjects := clients,
  libraryDependencies ++= Seq(
    evolutions
    , "com.vmunier" %% "play-scalajs-scripts" % "0.3.0"
    , "org.webjars" % "jquery" % "1.11.1"
    , "com.typesafe.play" %% "play-slick" % "1.1.0"
    , "com.typesafe.play" %% "play-slick-evolutions" % "1.1.0"
    , "org.postgresql" % "postgresql" % "9.3-1102-jdbc41"
    , "com.github.tototoshi" %% "scala-csv" % "1.2.2"
    , specs2 % Test
  ),
  resolvers ++= Seq(
    "scalaz-bintray" at "https://dl.bintray.com/scalaz/releases"
  )
).enablePlugins(PlayScala).
  aggregate(clients.map(projectToRef): _*).
  dependsOn(sharedJvm)

lazy val scalaJSClient = (project in file("scalajs")).settings(
  scalaVersion := scalaV,
  persistLauncher := true,
  persistLauncher in Test := false,
  sourceMapsDirectories += sharedJs.base / "..",
  unmanagedSourceDirectories in Compile := Seq((scalaSource in Compile).value),
  libraryDependencies ++= Seq(
    "org.scala-js" %%% "scalajs-dom" % "0.8.2"
  )
).enablePlugins(ScalaJSPlugin, ScalaJSPlay).
  dependsOn(sharedJs)

lazy val shared = (crossProject.crossType(CrossType.Pure) in file("shared")).
  settings(scalaVersion := scalaV).
  jsConfigure(_ enablePlugins ScalaJSPlay).
  jsSettings(sourceMapsBase := baseDirectory.value / "..")

lazy val sharedJvm = shared.jvm
lazy val sharedJs = shared.js

// loads the Play project at sbt startup
onLoad in Global := (Command.process("project playServer", _: State)) compose (onLoad in Global).value