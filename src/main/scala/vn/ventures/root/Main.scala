package vn.ventures.root

import com.softwaremill.id.DefaultIdGenerator
import io.getquill.jdbczio.Quill
import io.getquill.SnakeCase
import vn.ventures.primaryAdapter.controllers.*
import vn.ventures.secondaryAdapter.authentication.AuthRepoLive
import vn.ventures.secondaryAdapter.{HealthCheckServiceLive, ItemRepositoryLive}
import zio.*
import zio.http.Server
import zio.logging.backend.SLF4J

object Main extends ZIOAppDefault {

  override val bootstrap: ULayer[Unit] = Runtime.removeDefaultLoggers >>> SLF4J.slf4j

  private val dataSourceLayer = Quill.DataSource.fromPrefix("db")

  private val mysqlLayer = Quill.Mysql.fromNamingStrategy(SnakeCase)

  private val repoLayers = ItemRepositoryLive.layer
    ++ AuthRepoLive.layer

  private val healthCheckServiceLayer = HealthCheckServiceLive.layer

  private val serverLayer =
    ZLayer
      .service[ApiConfig]
      .flatMap { cfg =>
        Server.defaultWith(_.binding(cfg.get.host, cfg.get.port))
      }
      .orDie

  private val idGeneratorLayer: ULayer[DefaultIdGenerator] = ZLayer
    .succeed {
      new DefaultIdGenerator(epoch = 1700249724L)
    }

  private val routes = ItemController.routes
    ++ HealthCheckController.routes
    ++ AuthenticationController.routes
    ++ AuthenticationController.test

  private val program = Server.serve(routes)

  override val run: ZIO[Any, Throwable, Nothing] = {
    program.provide(
      healthCheckServiceLayer,
      serverLayer,
      ApiConfig.layer,
      repoLayers,
      mysqlLayer,
      dataSourceLayer,
      idGeneratorLayer
    )
  }
}
