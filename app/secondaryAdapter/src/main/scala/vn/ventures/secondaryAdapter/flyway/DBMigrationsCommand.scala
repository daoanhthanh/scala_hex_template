package vn.ventures.secondaryAdapter.flyway

import cats.implicits._
import cats.effect.IOApp
import cats.effect.{ExitCode, IO}
import com.typesafe.scalalogging.LazyLogging

object DBMigrationsCommand extends IOApp with LazyLogging {

  /** Lists all JDBC data-sources, defined in `application.conf`
    */
  private val dbConfigNamespaces = List(
    "flyway"
  )

  def run(args: List[String]): IO[ExitCode] = {
    val migrate =
      dbConfigNamespaces.traverse { namespace =>
        for {
          _ <- IO(logger.info(s"Migrating database configuration: $namespace"))
          cfg = JdbcDatabaseConfig.loadFromGlobal(namespace)
          _ <- DBMigrations.migrate[IO](cfg)
        } yield ()
      }
    migrate.as(ExitCode.Success)
  }
}
