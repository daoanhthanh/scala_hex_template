package vn.ventures.primaryAdapter.utils

import com.softwaremill.id.DefaultIdGenerator
import com.softwaremill.id.pretty.{IdPrettifier, PrettyIdGenerator, StringIdGenerator}

object ErrorIdGenerator {

  private val generator: StringIdGenerator = new PrettyIdGenerator(
    new DefaultIdGenerator(),
    IdPrettifier.custom(
      delimiter = '_'
    )
  )

  def get: String = generator.nextId()

}
