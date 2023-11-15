package vn.ventures.primaryAdapter.controllers

import vn.ventures.domain.healthckeck.HealthCheckService
import zio.*
import zio.http.*

object HealthCheckController:

  val routes: HttpApp[HealthCheckService, Nothing] = Http.collectZIO {

    case Method.HEAD -> !! / "healthcheck" =>
      ZIO.succeed {
        Response.status(Status.NoContent)
      }

    case Method.GET -> !! / "healthcheck" =>
      HealthCheckService.check.map { dbStatus =>
        if (dbStatus.status) Response.ok
        else Response.status(Status.InternalServerError)
      }

  }
