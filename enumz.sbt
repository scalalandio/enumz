import sbt._
import Settings._
import sbtcrossproject.CrossPlugin.autoImport.{ crossProject, CrossType }

lazy val root = project.root
  .setName("enumz")
  .setDescription("Common type class interface for various enums implementations")
  .configureRoot
  .aggregate(enumzJVM, enumzJS, enumzTestsJVM, enumzTestsJS)

lazy val enumz = crossProject(JVMPlatform, JSPlatform).crossType(CrossType.Pure).build.from("enumz")
  .setName("enumz")
  .setDescription("Common type class interface for various enums implementations")
  .configureModule
  .publish

lazy val enumzJVM = enumz.jvm
lazy val enumzJS = enumz.js

lazy val enumzTests = crossProject(JVMPlatform, JSPlatform).crossType(CrossType.Pure).build.from("enumz-tests")
  .setName("enumz-tests")
  .setDescription("Enumz tests")
  .dependsOn(enumz)
  .configureModule
  .configureTests()
  .noPublish
  .settings(libraryDependencies ++= Seq(
    "org.specs2" %%% "specs2-core"       % Dependencies.specs2Version % "test",
    "org.specs2" %%% "specs2-scalacheck" % Dependencies.specs2Version % "test"
  ))

lazy val enumzTestsJVM = enumzTests.jvm
lazy val enumzTestsJS = enumzTests.js

addCommandAlias("fullTest", ";test;scalastyle")
addCommandAlias("fullCoverageTest", ";coverage;test;coverageReport;coverageAggregate;scalastyle")
addCommandAlias("relock", ";unlock;reload;update;lock")
