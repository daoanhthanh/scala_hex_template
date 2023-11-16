package vn.ventures.domain.authentication

import vn.ventures.domain.user.UserRole
import zio.{IO, ZIO}
import vn.ventures.domain.RepositoryError
import vn.ventures.domain.user.UserBase
import vn.ventures.domain.user.admin.Admin

trait AuthRepo:
  def findUserByLoginId(userType: UserRole, loginId: String): IO[RepositoryError, Option[Admin]]

object AuthRepo:

  def findUserByLoginId(userType: UserRole, loginId: String): ZIO[AuthRepo, RepositoryError, Option[Admin]] =
    ZIO.serviceWithZIO[AuthRepo](_.findUserByLoginId(userType, loginId))
