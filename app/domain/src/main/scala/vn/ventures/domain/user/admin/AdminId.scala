package vn.ventures.domain.user.admin

import vn.ventures.domain.ids.UuidIdentifier

import java.util.UUID

final case class AdminId(value: UUID) extends UuidIdentifier
