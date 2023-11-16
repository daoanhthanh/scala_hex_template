package vn.ventures.domain.ids

trait Identifier[T]:
  def value: T

