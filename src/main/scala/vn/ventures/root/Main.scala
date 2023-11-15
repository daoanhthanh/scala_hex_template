package vn.ventures.root

import io.getquill.jdbczio.Quill
import io.getquill.Literal
import vn.ventures.primaryAdapter.controllers.{HealthCheckController, ItemController}
import vn.ventures.secondaryAdapter.{HealthCheckServiceLive, ItemRepositoryLive}
import zio.*
import zio.http.Server
import zio.logging.backend.SLF4J

object Main extends ZIOAppDefault {

  override val bootstrap: ULayer[Unit] = Runtime.removeDefaultLoggers >>> SLF4J.slf4j

  private val dataSourceLayer = Quill.DataSource.fromPrefix("db")

  private val postgresLayer = Quill.Postgres.fromNamingStrategy(Literal)

  private val repoLayer = ItemRepositoryLive.layer

  private val healthCheckServiceLayer = HealthCheckServiceLive.layer

  private val serverLayer =
    ZLayer
      .service[ApiConfig]
      .flatMap { cfg =>
        Server.defaultWith(_.binding(cfg.get.host, cfg.get.port))
      }
      .orDie

  private val routes = ItemController.routes ++ HealthCheckController.routes

  private val program = Server.serve(routes)

  override val run: ZIO[Any, Throwable, Nothing] =
    program.provide(
      healthCheckServiceLayer,
      serverLayer,
      ApiConfig.layer,
      repoLayer,
      postgresLayer,
      dataSourceLayer
    )

}
