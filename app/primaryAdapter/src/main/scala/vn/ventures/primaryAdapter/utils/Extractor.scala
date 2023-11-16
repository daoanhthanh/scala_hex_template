package vn.ventures.primaryAdapter.utils

import vn.ventures.domain.ValidationError
import vn.ventures.domain.user.UserRole
import zio.*
import zio.http.*

private[primaryAdapter] object Extractor:
  def extractLong(str: String): IO[ValidationError, Long] =
    ZIO
      .attempt(str.toLong)
      .refineToOrDie[NumberFormatException]
      .mapError(err => ValidationError(err.getMessage))

  def extractUserRole(str: String): IO[ValidationError, UserRole] =
    ZIO
      .attempt(UserRole.valueOf(str.toUpperCase))
      .refineToOrDie[IllegalArgumentException]
      .mapError(_ => ValidationError(s"Invalid user role: $str"))
