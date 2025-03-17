// git
addSbtPlugin("com.github.sbt" % "sbt-git" % "2.1.0")
// linters
addSbtPlugin("org.scalameta" % "sbt-scalafmt" % "2.5.4")
addSbtPlugin("org.scoverage" % "sbt-scoverage" % "2.2.1")
// cross-compile
addSbtPlugin("com.eed3si9n" % "sbt-projectmatrix" % "0.10.1")
addSbtPlugin("com.indoorvivants" % "sbt-commandmatrix" % "0.0.5")
addSbtPlugin("org.scala-js" % "sbt-scalajs" % "1.17.0")
addSbtPlugin("org.scala-native" % "sbt-scala-native" % "0.5.7")
// publishing
addSbtPlugin("org.xerial.sbt" % "sbt-sonatype" % "3.12.2")
addSbtPlugin("com.github.sbt" % "sbt-pgp" % "2.3.1")
// MiMa
addSbtPlugin("com.typesafe" % "sbt-mima-plugin" % "1.1.4")
// disabling projects in IDE
addSbtPlugin("org.jetbrains" % "sbt-ide-settings" % "1.1.0")
// documentation
addSbtPlugin("com.github.reibitto" % "sbt-welcome" % "0.4.0")

//libraryDependencies += "org.slf4j" % "slf4j-nop" % "1.7.25"
ThisBuild / libraryDependencySchemes += "org.scala-lang.modules" %% "scala-xml" % VersionScheme.Always
