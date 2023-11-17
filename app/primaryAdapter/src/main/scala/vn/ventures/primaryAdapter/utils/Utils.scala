package vn.ventures.primaryAdapter.utils

import vn.ventures.primaryAdapter.utils.Extensions.*
import zio.*
import zio.http.*

private[primaryAdapter] object Utils:

  def handleError(err: Throwable): UIO[Response] = {
    err.toErrResponse
  }

  def handleError(f: Task[Nothing]): UIO[Response] = {
    f.foldZIO(
      err => err.toErrResponse,
      _ =>
        RuntimeException(
          """Is is supposed to be an 'vn.ventures.domain.DomainError'
            |or an Exception here, but got Nothing.""".stripMargin
        ).toErrResponse
    )
  }

  def logDeviceInfo(accountId: String, request: Request): Unit = {
    ZIO.debug(
      s"Authenticated accountId: $accountId header info ${request.headers.get("User-Agent").getOrElse("NONE")}"
    )
  }

  def logDeviceInfo(request: Request): Unit = {
    ZIO.debug(s"header info ${request.headers.get("User-Agent").getOrElse("NONE")}")
  }
