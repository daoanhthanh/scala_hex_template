package vn.ventures.primaryAdapter.utils

import vn.ventures.domain.ValidationError
import zio.*
import zio.http.*
import zio.json.*

private[primaryAdapter] object Extensions:

  implicit class RichRequest(val request: Request) extends AnyVal {
    def jsonBodyAs[T: JsonDecoder]: IO[ValidationError, T] =
      for {
        body: String <- request.body.asString.orDie
        t            <- ZIO.succeed(body.fromJson[T]).absolve.mapError(ValidationError.apply)
      } yield t
  }

  implicit class RichDomain[T](val data: T) extends AnyVal {

    def toResponseZIO(implicit ev: JsonEncoder[T]): UIO[Response] = toResponseZIO(Status.Ok)

    def toResponseZIO(status: Status)(implicit ev: JsonEncoder[T]): UIO[Response] = ZIO.succeed {
      Response.json(data.toJson).withStatus(status)
    }

    def toEmptyResponseZIO: UIO[Response] = toEmptyResponseZIO(Status.NoContent)

    def toEmptyResponseZIO(status: Status): UIO[Response] = ZIO.succeed(Response.status(status))

  }
