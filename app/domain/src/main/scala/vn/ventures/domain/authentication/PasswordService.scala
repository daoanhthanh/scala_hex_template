package vn.ventures.domain.authentication

trait PasswordService {

  def hash(password: String): String

  def verify(candidate: String, hashed: String): Boolean

  def randomPassword: String

}
