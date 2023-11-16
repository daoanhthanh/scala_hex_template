package vn.ventures.domain.commons

case class Pagination[T](
    data: Seq[T],
    total: Long,
    size: Int,
    page: Int
)
