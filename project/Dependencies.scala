import sbt.*

object Dependencies {

  lazy val defaultDependencies: Seq[ModuleID] = Seq(
    "io.monix"               %% "monix"       % "3.4.1",
    "com.github.nscala-time" %% "nscala-time" % "2.32.0",
    // test
    "dev.zio"       %% "zio-test"                        % zioVersion            % Test,
    "dev.zio"       %% "zio-test-sbt"                    % zioVersion            % Test,
    "dev.zio"       %% "zio-test-junit"                  % zioVersion            % Test,
    "dev.zio"       %% "zio-mock"                        % zioMockVersion        % Test,
    "com.dimafeng"  %% "testcontainers-scala-postgresql" % testContainersVersion % Test,
    "dev.zio"       %% "zio-test-magnolia"               % zioVersion            % Test,
    "dev.zio"       %% "zio-config-magnolia"             % zioConfigVersion,
    "dev.zio"       %% "zio-config"                      % zioConfigVersion,
    "dev.zio"       %% "zio-config-typesafe"             % zioConfigVersion,
    "org.scalameta" %% "munit"                           % "0.7.29"              % Test
  ) ++ logDependencies

  lazy val logDependencies: Seq[ModuleID] = Seq(
    "dev.zio"       %% "zio-logging"       % zioLoggingVersion,
    "dev.zio"       %% "zio-logging-slf4j" % zioLoggingVersion,
    "ch.qos.logback" % "logback-classic"   % logbackClassicVersion
  )

  lazy val dbDependencies: Seq[ModuleID] = Seq(
//    "org.postgresql" % "postgresql"          % postgresqlVersion,
    "com.h2database" % "h2"                % "2.2.224",
    "com.mysql"      % "mysql-connector-j" % "8.0.33",
    "io.getquill"   %% "quill-jdbc"        % "4.8.0",
    "io.getquill" %% "quill-jdbc-zio" % quillVersion excludeAll ExclusionRule(organization = "org.scala-lang.modules"),
    "dev.zio"     %% "zio"            % zioVersion,
    "dev.zio"     %% "zio-streams"    % zioVersion,
    "org.flywaydb" % "flyway-mysql"   % "10.0.0"

//    "org.flywaydb"  %% "flyway-play"       % "9.0.0"
  )

  lazy val primaryAdapterDependencies: Seq[ModuleID] = Seq(
    "dev.zio" %% "zio-http" % zioHttpVersion,
    "dev.zio" %% "zio-json" % zioJsonVersion
  )
  lazy val bcryptDependency = "org.mindrot"               % "jbcrypt"           % "0.4"
  lazy val paseto           = "io.github.nbaars"          % "paseto4j-version3" % "2023.1"
  lazy val snowflake4s      = "com.softwaremill.common"  %% "id-generator"      % "1.4.0"
  val zioVersion            = "2.0.13"
  val zioJsonVersion        = "0.5.0"
  val zioConfigVersion      = "3.0.7"
  val zioLoggingVersion     = "2.1.15"
  val logbackClassicVersion = "1.4.7"
  val postgresqlVersion     = "42.6.0"
  val testContainersVersion = "0.40.15"
  val zioMockVersion        = "1.0.0-RC11"
  val zioHttpVersion        = "3.0.0-RC1"
  val quillVersion          = "4.6.0.1"
}
