import SbtMisc._

lazy val playpen = project in file(".")

organization := "com.beamly.playpen"
    licenses := Seq("Apache License, Version 2.0" -> url("http://www.apache.org/licenses/LICENSE-2.0.txt"))
 description := "A way to Play! safely"
    homepage := Some(url(s"https://github.com/beamly/playpen"))
   startYear := Some(2014)

      scalaVersion := "2.11.7"
crossScalaVersions := Seq("2.11.7", "2.10.6")

scalacOptions ++= Seq("-encoding", "utf8")
scalacOptions ++= Seq("-deprecation", "-feature", "-unchecked", "-Xlint")
scalacOptions  += "-language:higherKinds"
scalacOptions  += "-language:implicitConversions"
scalacOptions  += "-language:postfixOps"
scalacOptions  += "-Xfatal-warnings"
scalacOptions  += "-Xfuture"
scalacOptions  += "-Yno-adapted-args"
scalacOptions  += "-Ywarn-dead-code"
scalacOptions  += "-Ywarn-numeric-widen"
scalacOptions ++= "-Ywarn-unused-import".ifScala211Plus.value.toSeq
scalacOptions  += "-Ywarn-value-discard"

scalacOptions in (Compile, console) -= "-Ywarn-unused-import"
scalacOptions in (Test,    console) -= "-Ywarn-unused-import"

  // wartremoverErrors ++= Warts.unsafe // Once sbt-wartremover 0.12+ is out
//  wartremoverErrors ++= Seq(
// // Wart.Any, // Removed to keep BuildInfo
//    Wart.Any2StringAdd, Wart.AsInstanceOf, Wart.DefaultArguments,
//    Wart.EitherProjectionPartial, Wart.IsInstanceOf, Wart.NonUnitStatements, Wart.Null, Wart.OptionPartial, Wart.Product,
//    Wart.Return, Wart.Serializable, Wart.TryPartial, Wart.Var, Wart.ListOps)

maxErrors := 5
triggeredMessage := Watched.clearWhenTriggered

resolvers += "typesafe-releases" at "http://repo.typesafe.com/typesafe/releases/"
resolvers += "scalaz-bintray" at "https://dl.bintray.com/scalaz/releases"

libraryDependencies += "joda-time"          % "joda-time"    % "2.5"
libraryDependencies += "org.joda"           % "joda-convert" % "1.2"
libraryDependencies += "com.typesafe.play" %% "play"         % "2.4.0"
libraryDependencies += "com.typesafe.play" %% "play-ws"      % "2.4.0"
libraryDependencies += "com.typesafe.play" %% "play-specs2"  % "2.4.0" % "test"
libraryDependencies += "org.slf4j"          % "slf4j-api"    % "1.7.7"

parallelExecution in Test := true
fork in Test := false

bintrayOrganization := Some("beamly")

pomExtra := pomExtra.value ++ {
    <developers>
        <developer>
            <id>dwijnand</id>
            <name>Dale Wijnand</name>
            <email>dale wijnand gmail com</email>
            <url>dwijnand.com</url>
        </developer>
    </developers>
    <scm>
        <connection>scm:git:github.com/beamly/playpen.git</connection>
        <developerConnection>scm:git:git@github.com:beamly/playpen.git</developerConnection>
        <url>https://github.com/beamly/playpen</url>
    </scm>
}

bintrayReleaseOnPublish in ThisBuild := false

releaseCrossBuild := true

watchSources ++= (baseDirectory.value * "*.sbt").get
watchSources ++= (baseDirectory.value / "project" * "*.scala").get
