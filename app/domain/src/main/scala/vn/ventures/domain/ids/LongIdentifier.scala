package vn.ventures.domain.ids

trait LongIdentifier extends Identifier[Long] {
  def createViewId(salt: String): String = s"$salt%06d".format(value)
}
