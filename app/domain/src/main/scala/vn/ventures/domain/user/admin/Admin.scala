package vn.ventures.domain.user.admin

import com.github.nscala_time.time.Imports.DateTime
import vn.ventures.domain.ids.RubbishID
import vn.ventures.domain.user.UserBase

case class Admin(
    id: Option[RubbishID],
    identifier: AdminId,
    email: String,
    accountId: String,
    name: String,
    nameFurigana: Option[String] = None,
    phoneNumber: Option[String] = None,
    encryptedPassword: String,
    createdAt: DateTime
) extends UserBase
