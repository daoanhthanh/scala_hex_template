package vn.ventures.secondaryAdapter.authentication

import com.github.nscala_time.time.Imports.DateTime
import io.getquill.*
import io.getquill.jdbczio.Quill
import vn.ventures.domain.RepositoryError
import vn.ventures.domain.ids.RubbishID
import vn.ventures.domain.user.admin.{Admin, AdminId}
import zio.*

import java.sql.SQLException
import java.util.UUID

private final case class AdminRecord(
    id: Long,
    identify: String,
    accountId: String,
    email: String,
    name: String,
    password: String,
    createdAt: java.sql.Timestamp
) {

  def toAdmin: Admin = {
    Admin(
      id = Some(RubbishID(id)),
      identifier = AdminId(UUID.fromString(identify)),
      accountId = accountId,
      email = email,
      name = name,
      encryptedPassword = password,
      createdAt = new DateTime(createdAt)
    )
  }
}

case class AdminDAO(quill: Quill.Mysql[SnakeCase]) {

  import quill._

  private def admins: Quoted[EntityQuery[AdminRecord]] = quote {
    querySchema[AdminRecord]("admins")
  }

  def findByAccountId(accID: String): IO[RepositoryError, Option[Admin]] = {
    val effect: IO[SQLException, List[AdminRecord]] = run {
      quote {
        admins.filter(_.accountId == lift(accID))
      }
    }

    effect
      .map(_.headOption.map(_.toAdmin))
      .refineOrDie { case e: Throwable =>
        RepositoryError(e)
      }
  }
}
