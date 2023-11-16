package vn.ventures.domain.authentication

import vn.ventures.domain.{DomainError, ValidationError}
import vn.ventures.domain.user.UserRole
import zio.*

object AuthService:
  def login(
      userType: UserRole,
      loginId: String,
      password: String
  ): ZIO[AuthRepo, DomainError, AuthenticatedEntity] =
    for {
      maybeUser <- AuthRepo.findUserByLoginId(userType, loginId)
      user <- ZIO
        .fromOption(maybeUser)
        .orElseFail(ValidationError("Wrong login_id or key"))
      _ <- ZIO.fail(ValidationError("Wrong login_id or key"))
        .when(user.encryptedPassword != password)
    } yield user.toAuthenticatedEntity
