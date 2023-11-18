import Dependencies.*

name         := """b_api_new_zio"""
organization := "vn.designtool"

version := "0.0.1-SNAPSHOT"

ThisBuild / scalaVersion := "3.3.1"
lazy val runMigrate = taskKey[Unit]("Migrates the database schema.")

lazy val root = (project in file("."))
  .aggregate(domain, primaryAdapter, secondaryAdapter, utility)
  .dependsOn(domain, primaryAdapter, secondaryAdapter, utility)
  .settings(
    Compile / scalaSource         := baseDirectory.value / "src" / "main" / "scala",
    Compile / resourceDirectory   := baseDirectory.value / "src" / "main" / "resources",
    Global / onChangedBuildSource := ReloadOnSourceChanges,
    libraryDependencies ++= logDependencies,
    runMigrate / fork := true,
    commands ++= Seq(
      SbtCommands.greet
    ),
    mainClass in (Compile, run) := Some("vn.ventures.root.Main"),
    fullRunTask(runMigrate, Compile, "vn.ventures.secondaryAdapter.flyway.DBMigrationsCommand"),
    addCommandAlias("run-db-migrations", "runMigrate"),
    addCommandAlias("start", "; greet ; run-db-migrations ; run")
  )

lazy val domain = (project in file("app/domain"))
  .disablePlugins(RevolverPlugin)
  .settings(
    libraryDependencies ++= defaultDependencies
      :+ snowflake4s
  )

lazy val primaryAdapter = (project in file("app/primaryAdapter"))
  .disablePlugins(RevolverPlugin)
  .dependsOn(domain, utility)
  .settings(
    resolvers +=
      "Sonatype OSS Snapshots" at "https://s01.oss.sonatype.org/content/repositories/snapshots",
    libraryDependencies ++= defaultDependencies,
    libraryDependencies ++= primaryAdapterDependencies :+ snowflake4s
  )

lazy val secondaryAdapter = (project in file("app/secondaryAdapter"))
  .disablePlugins(RevolverPlugin)
  .dependsOn(domain, utility)
  .settings(
    libraryDependencies ++= defaultDependencies,
    libraryDependencies ++= dbDependencies :+ snowflake4s
  )

lazy val utility = (project in file("app/utility"))
  .disablePlugins(RevolverPlugin)
  .dependsOn(domain)
  .settings(
    libraryDependencies ++= defaultDependencies,
    libraryDependencies ++= Seq {
      paseto
    }
  )
