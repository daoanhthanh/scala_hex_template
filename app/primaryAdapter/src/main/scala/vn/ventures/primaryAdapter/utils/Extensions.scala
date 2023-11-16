package vn.ventures.primaryAdapter.utils

import vn.ventures.domain.*
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
      Response.json(JSON(data)).withStatus(status)
    }

    def toEmptyResponse: UIO[Response] = toEmptyResponse(Status.NoContent)

    def toEmptyResponse(status: Status): UIO[Response] = ZIO.succeed(Response.status(status))

  }

  implicit class RichException(val ex: Throwable) extends AnyVal {
    def toErrResponse: UIO[Response] = {
      ex match {
        case err: DomainError => handleDomainError(err)
        case other            => handleUnknownError(other)
      }
    }

    private def handleDomainError(err: DomainError): UIO[Response] = {
      ZIO.succeed(
        Response
          .json(JSON(err))
          .withStatus {
            err match {
              case NotFoundError          => Status.NotFound
              case ValidationError(_)     => Status.BadRequest
              case AuthenticationError(_) => Status.Unauthorized
              case RepositoryError(_)     => Status.Custom(555)
            }
          }
      )
    }

    private def handleUnknownError(cause: Throwable): UIO[Response] = {
      ZIO.logErrorCause(cause.getMessage, Cause.fail(cause)) *>
      ZIO
        .succeed(
          Response
            .json(JSON(new RuntimeException("Internal server error, contact system administrator")))
            .withStatus(Status.InternalServerError)
        )

    }
  }

  private def JSON[T](data: T)(implicit ev: JsonEncoder[T]): String =
    s"""{
       |  "success": "true",
       |  "data": ${data.toJson}
       |}""".stripMargin

  private def JSON(data: Throwable): String = {
    data.printStackTrace()
    s"""{
       |  "success": "false",
       |  "message": "${data.getMessage}"
       |}""".stripMargin
  }
}
