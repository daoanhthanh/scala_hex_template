package vn.ventures.primaryAdapter.controllers

import com.softwaremill.id.DefaultIdGenerator
import vn.ventures.domain.authentication.{AuthRepo, AuthService}
import vn.ventures.primaryAdapter.forms.LoginRequest
import vn.ventures.primaryAdapter.utils.Extensions.*
import vn.ventures.primaryAdapter.utils.{Extractor, Utils}
import zio.*
import zio.http.*

import scala.language.postfixOps

object AuthenticationController extends JsonSupport {

  val routes: HttpApp[AuthRepo, Nothing] = Http.collectZIO {
    case request @ Method.POST -> _ / "api" / userRoleStr / "authenticate" =>
      val effect = for {
        userRole <- Extractor.extractUserRole(userRoleStr)
        loginReq <- request.jsonBodyAs[LoginRequest]
        result <- AuthService
          .login(userRole, loginReq.loginId, loginReq.key)
      } yield result

      effect
        .mapError { err =>
          {
            // If login id and key are incorrect, sleep 3~4 seconds to prevent brute force attack
            val sleepTime = scala.util.Random.nextInt(1000) + 3000
            ZIO.sleep(sleepTime millis) *> ZIO.fail(err)
          }
        }
        .foldZIO(Utils.handleError, _.toResponse)
  }

  val test: HttpApp[DefaultIdGenerator, Nothing] = Http.collectZIO { case Method.GET -> _ / "newId" =>
    val effect = for {
      id <- ZIO.serviceWith[DefaultIdGenerator](_.nextId())
    } yield id.toString

    effect.map(Response.text(_))
  }

}
