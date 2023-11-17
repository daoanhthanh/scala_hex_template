package vn.ventures.primaryAdapter.utils

final case class Metadata(
    total: Int,
    page: Int,
    pageSize: Int,
    pageCount: Int,
    next: Option[String],
    previous: Option[String]
)

/** Represent a response from the Design Tool (DT) server
  */
final case class DTResponse[T](
    succeed: Boolean,
    data: T,
    metadata: Option[Metadata]
)

object DTResponse {
  def apply[T](data: T): DTResponse[T] = DTResponse(true, data, None)

  def apply[T](data: T, metadata: Metadata): DTResponse[T] =
    DTResponse(true, data, Some(metadata))
}

/** Represent an error response from the Design Tool (DT) server
  */
final case class DTErrResponse(
    succeed: Boolean,
    message: String,
    errorId: Option[String]
)

object DTErrResponse {
  def apply(message: String): DTErrResponse = DTErrResponse(false, message, None)

  def apply(message: String, errorId: String): DTErrResponse =
    DTErrResponse(false, message, Some(errorId))
}


