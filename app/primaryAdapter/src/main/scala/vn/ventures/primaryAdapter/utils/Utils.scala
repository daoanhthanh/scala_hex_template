package vn.ventures.primaryAdapter.utils

import vn.ventures.primaryAdapter.utils.Extensions.*
import vn.ventures.domain.*
import zio.*
import zio.http.*

private[primaryAdapter] object Utils:

  def extractLong(str: String): IO[ValidationError, Long] =
    ZIO
      .attempt(str.toLong)
      .refineToOrDie[NumberFormatException]
      .mapError(err => ValidationError(err.getMessage))

  def handleError(err: DomainError): UIO[Response] = err match {
    case NotFoundError          => ZIO.succeed(Response.status(Status.NotFound))
    case ValidationError(msg)   => msg.toResponseZIO(Status.BadRequest)
    case RepositoryError(cause) =>
      ZIO.logErrorCause(cause.getMessage, Cause.fail(cause)) *>
        "Internal server error, contact system administrator".toResponseZIO(Status.InternalServerError)
  }
