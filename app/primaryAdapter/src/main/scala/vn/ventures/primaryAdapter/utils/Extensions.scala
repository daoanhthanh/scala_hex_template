package vn.ventures.primaryAdapter.utils

import vn.ventures.domain.*
import vn.ventures.primaryAdapter.controllers.JsonSupport.*
import zio.*
import zio.http.*
import zio.json.*

private[primaryAdapter] object Extensions {
  implicit class RichRequest(val request: Request) extends AnyVal {
    def jsonBodyAs[T: JsonDecoder]: IO[ValidationError, T] =
      for {
        body: String <- request.body.asString.orDie
        t            <- ZIO.succeed(body.fromJson[T]).absolve.mapError(ValidationError.apply)
      } yield t
  }

  implicit class RichDomain[T](val data: T) extends AnyVal {

    def toResponse(implicit ev: JsonEncoder[T]): UIO[Response] = toResponse(Status.Ok)

    def toResponse(status: Status)(implicit ev: JsonEncoder[T]): UIO[Response] = ZIO.succeed {
      Response.json(DTResponse(data).toJson(dtResponseEncoder)).withStatus(status)
    }

    def toEmptyResponse: UIO[Response] = toEmptyResponse(Status.NoContent)

    def toEmptyResponse(status: Status): UIO[Response] = ZIO.succeed(Response.status(status))

  }

  implicit class RichException(val ex: Throwable) extends AnyVal {
    def toErrResponse: UIO[Response] = {
      ex match {
        case err: DomainError => handleDomainError(err)
        case other            => handleUnhandledError(other)
      }
    }

    private def handleDomainError(err: DomainError): UIO[Response] = {
      err match {
        case e: RepositoryError => handleUnhandledError(e)
        case _ =>
          ZIO.succeed(
            Response
              .json(DTErrResponse(err.getMessage).toJson)
              .withStatus {
                err match {
                  case NotFoundError          => Status.NotFound
                  case ValidationError(_)     => Status.BadRequest
                  case AuthenticationError(_) => Status.Unauthorized
                  case _                      => Status.ServiceUnavailable
                }
              }
          )
      }

    }

    private def handleUnhandledError(cause: Throwable): UIO[Response] = {
      for {
        errorId <- ZIO.succeed(ErrorIdGenerator.get)
        _       <- ZIO.logErrorCause(s"$errorId ${cause.getMessage}", Cause.fail(cause))
        response <- ZIO
          .succeed(
            Response
              .json(
                DTErrResponse("Internal server error, contact system administrator", errorId).toJson
              )
              .withStatus(Status.InternalServerError)
          )
      } yield response
    }
  }

}
