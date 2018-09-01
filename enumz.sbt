import sbt._
import Settings._
import sbtcrossproject.CrossPlugin.autoImport.{ crossProject, CrossType }

lazy val root = project.root
  .setName("enumz")
  .setDescription("Common type class interface for various enums implementations")
  .configureRoot
  .noPublish
  .aggregate(enumzJVM, enumzJS)

lazy val enumz = crossProject(JVMPlatform, JSPlatform).crossType(CrossType.Pure).build.from("enumz")
  .setName("enumz")
  .setDescription("Common type class interface for various enums implementations")
  .configureModule
  .configureTests()
  .publish
  .jvmSettings(Test / compileOrder := CompileOrder.JavaThenScala)
  .settings(libraryDependencies ++= Seq(
    "org.specs2" %%% "specs2-core"          % Dependencies.specs2Version % "test",
    "org.specs2" %%% "specs2-scalacheck"    % Dependencies.specs2Version % "test",
    "org.specs2" %%% "specs2-matcher-extra" % Dependencies.specs2Version % "test"
  ))

lazy val enumzJVM = enumz.jvm
lazy val enumzJS = enumz.js

addCommandAlias("fullTest", ";test;scalastyle")
addCommandAlias("fullCoverageTest", ";coverage;test;coverageReport;coverageAggregate;scalastyle")
addCommandAlias("relock", ";unlock;reload;update;lock")
