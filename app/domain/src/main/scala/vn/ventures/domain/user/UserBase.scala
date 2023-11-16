package vn.ventures.domain.user

import vn.ventures.domain.authentication.AuthenticatedEntity
import vn.ventures.domain.ids.{Identifier, LongIdentifier}

trait UserBase {
  def id: Option[LongIdentifier]
  def identifier: Identifier[_]
  def accountId: String
  def name: String
  def encryptedPassword: String
  def phoneNumber: Option[String]
  def nameFurigana: Option[String]
  def email: String

  private def idStr: String = id.fold("")(_.createViewId("U"))

  def toAuthenticatedEntity: AuthenticatedEntity = AuthenticatedEntity(
    identifier.value.toString,
    accountId,
    name
  )
}
