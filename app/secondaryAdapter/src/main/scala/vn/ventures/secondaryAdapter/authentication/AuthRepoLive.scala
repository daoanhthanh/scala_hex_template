package vn.ventures.secondaryAdapter.authentication

import io.getquill.*
import io.getquill.jdbczio.Quill
import vn.ventures.domain.{NotFoundError, RepositoryError}
import vn.ventures.domain.authentication.AuthRepo
import vn.ventures.domain.user.UserRole.*
import vn.ventures.domain.user.admin.Admin
import vn.ventures.domain.user.{UserBase, UserRole}
import vn.ventures.domain.user.UserRole._
import zio.{IO, URLayer, ZIO, ZLayer}

final class AuthRepoLive(quill: Quill.Mysql[SnakeCase]) extends AuthRepo {
  override def findUserByLoginId(userType: UserRole, loginId: String): IO[RepositoryError, Option[Admin]] = {
    userType match {
      case ADMIN => AdminDAO(quill).findByAccountId(loginId)
      case USER  => ???
      case GUEST => ???
      case MYCLO => ???
    }
  }

}

object AuthRepoLive {
  val layer: URLayer[Quill.Mysql[SnakeCase], AuthRepo] = ZLayer {
    for {
      quill <- ZIO.service[Quill.Mysql[SnakeCase]]
    } yield AuthRepoLive(quill)
  }

}
