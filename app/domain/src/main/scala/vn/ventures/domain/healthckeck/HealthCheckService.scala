package vn.ventures.domain.healthckeck

import zio.*

trait HealthCheckService:
  def check: UIO[DbStatus]

object HealthCheckService:

  def check: URIO[HealthCheckService, DbStatus] = ZIO.serviceWithZIO(_.check)
