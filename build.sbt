import Dependencies.*

//lazy val root = project
//  .in(file("."))
//  .settings(
//    name := "b_api_new_zio",
//    version := "0.1.0-SNAPSHOT",
//
//    scalaVersion := scala3Version,
//
//    libraryDependencies += "org.scalameta" %% "munit" % "0.7.29" % Test
//  )

name := """b_api_new_zio"""
organization := "vn.designtool"

version := "1.0-SNAPSHOT"

ThisBuild / scalaVersion := "3.3.1"

lazy val runMigrate = taskKey[Unit]("Migrates the database schema.")
addCommandAlias("run-db-migrations", "runMigrate")

lazy val root = (project in file("."))
  .aggregate(domain, primaryAdapter, secondaryAdapter, utility)
  .dependsOn(domain, primaryAdapter, secondaryAdapter, utility)
  .settings(
    Compile / scalaSource := baseDirectory.value / "src" / "main" / "scala",
    Compile / resourceDirectory := baseDirectory.value / "src" / "main" / "resources",
    Global / onChangedBuildSource := ReloadOnSourceChanges,
    libraryDependencies ++= logDependencies,
    fullRunTask(runMigrate, Compile, "vn.ventures.secondaryAdapter.flyway.DBMigrationsCommand"),
    fork in runMigrate := true
  )

lazy val domain = (project in file("app/domain"))
  .settings(
    resolvers +=
      "Sonatype OSS Snapshots" at "https://s01.oss.sonatype.org/content/repositories/snapshots",
    libraryDependencies ++= defaultDependencies
  )

lazy val primaryAdapter = (project in file("app/primaryAdapter"))
  .dependsOn(domain, utility)
  .settings(
    libraryDependencies ++= defaultDependencies,
    libraryDependencies ++= primaryAdapterDependencies
  )

lazy val secondaryAdapter = (project in file("app/secondaryAdapter"))
  .dependsOn(domain, utility)
  .settings(
    libraryDependencies ++= defaultDependencies,
    libraryDependencies ++= dbDependencies
  )

lazy val utility = (project in file("app/utility"))
  .dependsOn(domain)
  .settings(
    libraryDependencies ++= defaultDependencies,
    libraryDependencies ++= Seq {
      paseto
    }
  )
