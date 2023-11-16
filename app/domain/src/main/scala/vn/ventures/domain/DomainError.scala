package vn.ventures.domain


abstract class DomainError(val message: String) extends RuntimeException(message)

final case class RepositoryError(cause: Throwable)                 extends DomainError(cause.getMessage)
final case class ValidationError(override val message: String)     extends DomainError(message)
final case class AuthenticationError(override val message: String) extends DomainError(message)
case object NotFoundError                                          extends DomainError("Entity not found")
