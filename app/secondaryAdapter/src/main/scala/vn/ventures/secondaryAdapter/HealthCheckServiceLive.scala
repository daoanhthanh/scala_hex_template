package vn.ventures.secondaryAdapter

import io.getquill.*
import io.getquill.jdbczio.Quill
import vn.ventures.domain.healthckeck.{DbStatus, HealthCheckService}
import zio.*

final class HealthCheckServiceLive(quill: Quill.Postgres[Literal]) extends HealthCheckService {

  import quill.*

  override def check: UIO[DbStatus] = run {
    quote {
      sql"""SELECT 1""".as[Query[Int]]
    }
  }
    .fold(
      _ => DbStatus(false),
      _ => DbStatus(true)
    )

}

object HealthCheckServiceLive {

  val layer: URLayer[Quill.Postgres[Literal], HealthCheckServiceLive] = ZLayer {
    for {
      quill <- ZIO.service[Quill.Postgres[Literal]]
    } yield HealthCheckServiceLive(quill)
  }
}
