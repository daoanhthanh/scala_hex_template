package vn.ventures.primaryAdapter.utils

import vn.ventures.primaryAdapter.utils.Extensions.*
import vn.ventures.domain.*
import zio.*
import zio.http.*

private[primaryAdapter] object Utils:

  def handleError(err: Throwable): UIO[Response] = {
    err.toErrResponse
  }

  def logDeviceInfo(accountId: String, request: Request): Unit = {
    ZIO.debug(
      s"Authenticated accountId: $accountId header info ${request.headers.get("User-Agent").getOrElse("NONE")}"
    )
  }

  def logDeviceInfo(request: Request): Unit = {
    ZIO.debug(s"header info ${request.headers.get("User-Agent").getOrElse("NONE")}")
  }
