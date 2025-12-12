import com.jsuereth.sbtpgp.PgpKeys.publishSigned
import com.typesafe.tools.mima.core.{MissingClassProblem, MissingTypesProblem, Problem, ProblemFilters}
import commandmatrix.extra.*

// Used to configure the build so that it would format on compile during development but not on CI.
lazy val isCI = sys.env.get("CI").contains("true")
ThisBuild / scalafmtOnCompile := !isCI

// Used to publish snapshots to Maven Central.
val mavenCentralSnapshots = "Maven Central Snapshots" at "https://central.sonatype.com/repository/maven-snapshots"

// Versions:

val versions = new {
  // Versions we are publishing for.
  val scala212 = "2.12.21"
  val scala213 = "2.13.18"
  val scala3 = "3.3.7"

  // Which versions should be cross-compiled for publishing.
  val scalas = List(scala212, scala213, scala3)
  val platforms = List(VirtualAxis.jvm, VirtualAxis.js, VirtualAxis.native)

  // Dependencies
  val chimney = "1.8.2"
  val enumeratum = "1.9.1"
  val munit = "1.2.1"

  // Explicitly handle Scala 2 and Scala 3 separately.
  def fold2[A](scalaVersion: String)(for2: => Seq[A], for3: => Seq[A]): Seq[A] =
    CrossVersion.partialVersion(scalaVersion) match {
      case Some((2, _)) => for2
      case Some((3, _)) => for3
      case _            => sys.error(s"Unsupported Scala version: $scalaVersion")
    }

  // Explicitly handle Scala 2.12, 2.13 and Scala 3 separately.
  def fold[A](scalaVersion: String)(for2_12: => Seq[A], for2_13: => Seq[A], for3: => Seq[A]): Seq[A] =
    CrossVersion.partialVersion(scalaVersion) match {
      case Some((2, 12)) => for2_12
      case Some((2, 13)) => for2_13
      case Some((3, _))  => for3
      case _             => sys.error(s"Unsupported Scala version: $scalaVersion")
    }
}

// Development settings:

val dev = new {

  val props = scala.util
    .Using(new java.io.FileInputStream("dev.properties")) { fis =>
      val props = new java.util.Properties()
      props.load(fis)
      props
    }
    .get

  // Which version should be used in IntelliJ
  val ideScala = props.getProperty("ide.scala") match {
    case "2.12" => versions.scala212
    case "2.13" => versions.scala213
    case "3"    => versions.scala3
  }
  val idePlatform = props.getProperty("ide.platform") match {
    case "jvm"    => VirtualAxis.jvm
    case "js"     => VirtualAxis.js
    case "native" => VirtualAxis.native
  }

  def isIdeScala(scalaVersion: String): Boolean =
    CrossVersion.partialVersion(scalaVersion) == CrossVersion.partialVersion(ideScala)
  def isIdePlatform(platform: VirtualAxis): Boolean = platform == idePlatform
}

// Common settings:

Global / excludeLintKeys += git.useGitDescribe
Global / excludeLintKeys += ideSkipProject
Global / excludeLintKeys += excludeDependencies
val only1VersionInIDE =
  // For the platform we are working with, show only the project for the Scala version we are working with.
  MatrixAction
    .ForPlatform(dev.idePlatform)
    .Configure(
      _.settings(
        ideSkipProject := !dev.isIdeScala(scalaVersion.value),
        bspEnabled := dev.isIdeScala(scalaVersion.value),
        scalafmtOnCompile := !isCI
      )
    ) +:
    // Do not show in IDE and BSP projects for the platform we are not working with.
    versions.platforms.filterNot(dev.isIdePlatform).map { platform =>
      MatrixAction
        .ForPlatform(platform)
        .Configure(_.settings(ideSkipProject := true, bspEnabled := false, scalafmtOnCompile := false))
    }

val addScalaJVM2Dir =
  MatrixAction
    .ForPlatform(VirtualAxis.jvm)
    .Configure(
      _.settings(
        Compile / unmanagedSourceDirectories ++= {
          if (scalaVersion.value.startsWith("2.")) Seq(sourceDirectory.value.toPath.resolve("main/scalajvm-2").toFile)
          else Seq()
        },
        Test / unmanagedSourceDirectories ++= {
          if (scalaVersion.value.startsWith("2.")) Seq(sourceDirectory.value.toPath.resolve("test/scalajvm-2").toFile)
          else Seq()
        }
      )
    )

val addScala213plusDir =
  MatrixAction
    .ForScala(v => (v.value == versions.scala213) || v.isScala3)
    .Configure(
      _.settings(
        Compile / unmanagedSourceDirectories += sourceDirectory.value.toPath.resolve("main/scala-2.13+").toFile,
        Test / unmanagedSourceDirectories += sourceDirectory.value.toPath.resolve("test/scala-2.13+").toFile
      )
    )

val settings = Seq(
  git.useGitDescribe := true,
  git.uncommittedSignifier := None,
  scalacOptions ++= versions.fold(scalaVersion.value)(
    for3 = Seq(
      // format: off
      "-encoding", "UTF-8",
      "-rewrite",
      "-source", "3.3-migration",
      // format: on
      "-unchecked",
      "-deprecation",
      "-explain",
      "-explain-types",
      "-feature",
      "-Wconf:msg=Unreachable case:s", // suppress fake (?) errors in internal.compiletime
      "-Wconf:msg=Missing.symbol.position:s", // https://github.com/scala/scala3/issues/21672
      // "-Wnonunit-statement",
      // "-Wunused:imports", // import x.Underlying as X is marked as unused even though it is! probably one of https://github.com/scala/scala3/issues/: #18564, #19252, #19657, #19912
      "-Wunused:privates",
      "-Wunused:locals",
      "-Wunused:explicits",
      "-Wunused:implicits",
      "-Wunused:params",
      // "-Wvalue-discard",
      "-Xfatal-warnings",
      "-Xcheck-macros",
      "-Ykind-projector:underscores"
    ),
    for2_13 = Seq(
      // format: off
      "-encoding", "UTF-8",
      "-release", "8",
      // format: on
      "-unchecked",
      "-deprecation",
      "-explaintypes",
      "-feature",
      "-language:higherKinds",
      "-Wconf:msg=discarding unmoored doc comment:s", // silence errors when scaladoc cannot comprehend nested vals
      "-Wconf:msg=Could not find any member to link for:s", // since errors when scaladoc cannot link to stdlib types or nested types
      "-Wconf:msg=Variable .+ undefined in comment for:s", // silence errors when there we're showing a buggy Expr in scaladoc comment
      "-Wunused:patvars",
      "-Xfatal-warnings",
      "-Xlint:adapted-args",
      "-Xlint:delayedinit-select",
      "-Xlint:doc-detached",
      "-Xlint:inaccessible",
      "-Xlint:infer-any",
      "-Xlint:nullary-unit",
      "-Xlint:option-implicit",
      "-Xlint:package-object-classes",
      "-Xlint:poly-implicit-overload",
      "-Xlint:private-shadow",
      "-Xlint:stars-align",
      "-Xlint:type-parameter-shadow",
      "-Xsource:3",
      "-Ywarn-dead-code",
      "-Ywarn-numeric-widen",
      "-Ywarn-unused:locals",
      "-Ywarn-unused:imports",
      "-Ywarn-macros:after",
      "-Ytasty-reader"
    ),
    for2_12 = Seq(
      // format: off
      "-encoding", "UTF-8",
      "-target:jvm-1.8",
      // format: on
      "-unchecked",
      "-deprecation",
      "-explaintypes",
      "-feature",
      "-language:higherKinds",
      "-Wconf:cat=deprecation&origin=io.scalaland.chimney.*:s", // we want to be able to deprecate APIs and test them while they're deprecated
      "-Wconf:msg=The outer reference in this type test cannot be checked at run time:s", // suppress fake(?) errors in internal.compiletime (adding origin breaks this suppression)
      "-Wconf:src=io/scalaland/chimney/cats/package.scala:s", // silence package object inheritance deprecation
      "-Wconf:msg=discarding unmoored doc comment:s", // silence errors when scaladoc cannot comprehend nested vals
      "-Wconf:msg=Could not find any member to link for:s", // since errors when scaladoc cannot link to stdlib types or nested types
      "-Wconf:msg=Variable .+ undefined in comment for:s", // silence errors when there we're showing a buggy Expr in scaladoc comment
      "-Xexperimental",
      "-Xfatal-warnings",
      "-Xfuture",
      "-Xlint:adapted-args",
      "-Xlint:by-name-right-associative",
      "-Xlint:delayedinit-select",
      "-Xlint:doc-detached",
      "-Xlint:inaccessible",
      "-Xlint:infer-any",
      "-Xlint:nullary-override",
      "-Xlint:nullary-unit",
      "-Xlint:option-implicit",
      "-Xlint:package-object-classes",
      "-Xlint:poly-implicit-overload",
      "-Xlint:private-shadow",
      "-Xlint:stars-align",
      "-Xlint:type-parameter-shadow",
      "-Xlint:unsound-match",
      "-Xsource:3",
      "-Yno-adapted-args",
      "-Ywarn-dead-code",
      "-Ywarn-inaccessible",
      "-Ywarn-infer-any",
      "-Ywarn-numeric-widen",
      "-Ywarn-unused:locals",
      "-Ywarn-unused:imports",
      "-Ywarn-macros:after",
      "-Ywarn-nullary-override",
      "-Ywarn-nullary-unit"
    )
  ),
  Compile / doc / scalacOptions ++= versions.fold2(scalaVersion.value)(
    for3 = Seq("-Ygenerate-inkuire"), // type-based search for Scala 3, this option cannot go into compile
    for2 = Seq.empty
  ),
  Compile / console / scalacOptions --= Seq("-Ywarn-unused:imports", "-Xfatal-warnings"),
  Test / compile / scalacOptions --= versions.fold(scalaVersion.value)(
    for2_12 = Seq("-Ywarn-unused:locals"), // Scala 2.12 ignores @unused warns
    for2_13 = Seq.empty,
    for3 = Seq.empty
  )
  /*,
  Compile / compile / wartremoverWarnings ++= Warts.allBut(
    Wart.Any,
    Wart.AsInstanceOf,
    Wart.DefaultArguments,
    Wart.ExplicitImplicitTypes,
    Wart.ImplicitConversion,
    Wart.ImplicitParameter,
    Wart.Overloading,
    Wart.PublicInference,
    Wart.NonUnitStatements,
    Wart.Nothing,
    Wart.ToString
  )*/
)

val versionSchemeSettings = Seq(versionScheme := Some("early-semver"))

val publishSettings = Seq(
  organization := "io.scalaland",
  homepage := Some(url("https://scalaland.io/enumz")),
  organizationHomepage := Some(url("https://scalaland.io")),
  licenses := Seq("Apache-2.0" -> url("https://www.apache.org/licenses/LICENSE-2.0")),
  scmInfo := Some(
    ScmInfo(url("https://github.com/scalalandio/enumz/"), "scm:git:git@github.com:scalalandio/enumz.git")
  ),
  startYear := Some(2017),
  developers := List(
    Developer("MateuszKubuszok", "Mateusz Kubuszok", "", url("https://github.com/MateuszKubuszok"))
  ),
  pomExtra := (
    <issueManagement>
      <system>GitHub issues</system>
      <url>https://github.com/scalalandio/enumz/issues</url>
    </issueManagement>
  ),
  publishTo := {
    if (isSnapshot.value) Some(mavenCentralSnapshots)
    else localStaging.value
  },
  publishMavenStyle := true,
  Test / publishArtifact := false,
  pomIncludeRepository := { _ =>
    false
  },
  // Sonatype ignores isSnapshot setting and only looks at -SNAPSHOT suffix in version:
  //   https://central.sonatype.org/publish/publish-maven/#performing-a-snapshot-deployment
  // meanwhile sbt-git used to set up SNAPSHOT if there were uncommitted changes:
  //   https://github.com/sbt/sbt-git/issues/164
  // (now this suffix is empty by default) so we need to fix it manually.
  git.gitUncommittedChanges := git.gitCurrentTags.value.isEmpty,
  git.uncommittedSignifier := Some("SNAPSHOT")
)

val mimaSettings = Seq(
  mimaPreviousArtifacts := {
    val previousVersions = moduleName.value match {
      case "enumz" => Set("1.0.0").filterNot(_ => scalaVersion.value == versions.scala3) ++ Set("1.1.0", "1.2.0")
      case "enumz-chimney" => Set("1.1.0", "1.2.0")
      case _               => Set()
    }
    previousVersions.map(organization.value %% moduleName.value % _)
  },
  mimaFailOnNoPrevious := true
)

val noPublishSettings =
  Seq(publish / skip := true, publishArtifact := false)

val ciCommand = (platform: String, scalaSuffix: String) => {
  val isJVM = platform == "JVM"

  val clean = Vector("clean")
  def withCoverage(tasks: String*): Vector[String] =
    "coverage" +: tasks.toVector :+ "coverageAggregate" :+ "coverageOff"

  val projects = Vector("enumz", "enumzChimney")
    .map(name => s"$name${if (isJVM) "" else platform}$scalaSuffix")
  def tasksOf(name: String): Vector[String] = projects.map(project => s"$project/$name")

  val tasks = if (isJVM) {
    clean ++
      withCoverage((tasksOf("compile") ++ tasksOf("test") ++ tasksOf("coverageReport")).toSeq *) ++
      tasksOf("mimaReportBinaryIssues")
  } else {
    clean ++ tasksOf("test")
  }

  tasks.mkString(" ; ")
}

val publishLocalForTests = {
  for {
    module <- Vector("enumz")
    moduleVersion <- Vector(module, module + "3")
  } yield moduleVersion + "/publishLocal"
}.mkString(" ; ")

val releaseCommand = (tag: Seq[String]) => if (tag.nonEmpty) "publishSigned ; sonaRelease" else "publishSigned"

lazy val root = project
  .in(file("."))
  .enablePlugins(GitVersioning, GitBranchPrompt)
  .settings(
    moduleName := "enumz-build",
    name := "enumz-build",
    description := "Build setup for Enumz modules",
    logo :=
      s"""Enumz ${(version).value} build for (${versions.scala212}, ${versions.scala213}, ${versions.scala3}) x (Scala JVM, Scala.js $scalaJSVersion, Scala Native $nativeVersion)
         |
         |This build uses sbt-projectmatrix with sbt-commandmatrix helper:
         | - Scala JVM adds no suffix to a project name seen in build.sbt
         | - Scala.js adds the "JS" suffix to a project name seen in build.sbt
         | - Scala Native adds the "Native" suffix to a project name seen in build.sbt
         | - Scala 2.12 adds the suffix "2_12" to a project name seen in build.sbt
         | - Scala 2.13 adds no suffix to a project name seen in build.sbt
         | - Scala 3 adds the suffix "3" to a project name seen in build.sbt
         |
         |When working with IntelliJ or Scala Metals, edit dev.properties to control which Scala version you're currently working with.
         |
         |If you need to test library locally in a different project, use publish-local-for-tests or manually publishLocal:
         | - enumz (obligatory)
         |for the right Scala version and platform (see projects task).
         |""".stripMargin,
    usefulTasks := Seq(
      sbtwelcome.UsefulTask("projects", "List all projects generated by the build matrix").noAlias,
      sbtwelcome
        .UsefulTask(
          "test",
          "Compile and test all projects in all Scala versions and platforms (beware! it uses a lot of memory and might OOM!)"
        )
        .noAlias,
      sbtwelcome.UsefulTask("enumz3/console", "Drop into REPL with Enumz imported (3)").noAlias,
      sbtwelcome.UsefulTask("enumz/console", "Drop into REPL with Enumz imported (2.13)").noAlias,
      sbtwelcome
        .UsefulTask(releaseCommand(git.gitCurrentTags.value), "Publish everything to release or snapshot repository")
        .alias("ci-release"),
      sbtwelcome.UsefulTask(ciCommand("JVM", "3"), "CI pipeline for Scala 3 on JVM").alias("ci-jvm-3"),
      sbtwelcome.UsefulTask(ciCommand("JVM", ""), "CI pipeline for Scala 2.13 on JVM").alias("ci-jvm-2_13"),
      sbtwelcome.UsefulTask(ciCommand("JVM", "2_12"), "CI pipeline for Scala 2.12 on JVM").alias("ci-jvm-2_12"),
      sbtwelcome.UsefulTask(ciCommand("JS", "3"), "CI pipeline for Scala 3 on Scala JS").alias("ci-js-3"),
      sbtwelcome.UsefulTask(ciCommand("JS", ""), "CI pipeline for Scala 2.13 on Scala JS").alias("ci-js-2_13"),
      sbtwelcome.UsefulTask(ciCommand("JS", "2_12"), "CI pipeline for Scala 2.12 on Scala JS").alias("ci-js-2_12"),
      sbtwelcome.UsefulTask(ciCommand("Native", "3"), "CI pipeline for Scala 3 on Scala Native").alias("ci-native-3"),
      sbtwelcome
        .UsefulTask(ciCommand("Native", ""), "CI pipeline for Scala 2.13 on Scala Native")
        .alias("ci-native-2_13"),
      sbtwelcome
        .UsefulTask(ciCommand("Native", "2_12"), "CI pipeline for Scala 2.12 on Scala Native")
        .alias("ci-native-2_12"),
      sbtwelcome
        .UsefulTask(
          publishLocalForTests,
          "Publishes all Scala 2.13 and Scala 3 JVM artifacts to test snippets in documentation"
        )
        .alias("publish-local-for-tests")
    )
  )
  .settings(settings)
  .settings(versionSchemeSettings)
  .settings(publishSettings)
  .settings(noPublishSettings)
  .aggregate(enumz.projectRefs *)
  .aggregate(enumzChimney.projectRefs *)

lazy val enumz = projectMatrix
  .in(file("enumz"))
  .someVariations(versions.scalas, versions.platforms)((addScalaJVM2Dir +: addScala213plusDir +: only1VersionInIDE) *)
  .enablePlugins(GitVersioning, GitBranchPrompt)
  .disablePlugins(WelcomePlugin)
  .settings(
    moduleName := "enumz",
    name := "enumz",
    description := "Common type class interface for various enums implementations"
  )
  .settings(settings *)
  .settings(versionSchemeSettings *)
  .settings(publishSettings *)
  .settings(mimaSettings *)
  .settings(
    libraryDependencies += "com.beachape" %%% "enumeratum" % versions.enumeratum,
    libraryDependencies += "org.scalameta" %%% "munit" % versions.munit % Test,
    mimaBinaryIssueFilters := Seq(
      // Users shouldn't rely on how we're handling implicit priorities (now we made them package private)
      ProblemFilters.exclude[MissingClassProblem]("io.scalaland.enumz.Implicits"),
      ProblemFilters.exclude[MissingClassProblem]("io.scalaland.enumz.LowPriorityImplicits"),
      ProblemFilters.exclude[MissingTypesProblem]("io.scalaland.enumz.Enum$"),
      // Changes to macros should not cause any problems
      ProblemFilters.exclude[Problem]("io.scalaland.enumz.internal.*")
    )
  )

lazy val enumzChimney = projectMatrix
  .in(file("enumz-chimney"))
  .someVariations(versions.scalas, versions.platforms)((only1VersionInIDE) *)
  .enablePlugins(GitVersioning, GitBranchPrompt)
  .disablePlugins(WelcomePlugin)
  .settings(
    moduleName := "enumz-chimney",
    name := "enumz-chimney",
    description := "Chimney integration for runtime enum conversions using Enumz"
  )
  .settings(settings *)
  .settings(versionSchemeSettings *)
  .settings(publishSettings *)
  .settings(mimaSettings *)
  .settings(
    libraryDependencies += "io.scalaland" %%% "chimney" % versions.chimney,
    libraryDependencies += "org.scalameta" %%% "munit" % versions.munit % Test
  )
  .dependsOn(enumz)
