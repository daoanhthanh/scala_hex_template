package vn.ventures.primaryAdapter.controllers

import vn.ventures.domain.authentication.{AuthRepo, AuthService}
import vn.ventures.primaryAdapter.forms.LoginRequest
import vn.ventures.primaryAdapter.utils.Extensions.*
import vn.ventures.primaryAdapter.utils.{Extractor, Utils}
import zio.*
import zio.http.*

object AuthenticationController extends JsonSupport {

  val routes: HttpApp[AuthRepo, Nothing] = Http.collectZIO {
    case request @ Method.POST -> _ / "api" / userRoleStr / "authenticate" =>
      val effect = for {
        userRole <- Extractor.extractUserRole(userRoleStr)
        loginReq <- request.jsonBodyAs[LoginRequest]
        result   <- AuthService.login(userRole, loginReq.loginId, loginReq.key)
      } yield result

      effect
        .foldZIO(Utils.handleError, _.toResponse)
  }

  val test: HttpApp[Any, Nothing] = Http.collectZIO { case Method.GET -> _ / "newId" =>
    ZIO.succeed(Response.text("ok"))
  }

}
