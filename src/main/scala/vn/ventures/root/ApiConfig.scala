package vn.ventures.root

import com.typesafe.config.ConfigFactory
import zio.*
import zio.config.*
import zio.config.ConfigDescriptor.*
import zio.config.typesafe.TypesafeConfigSource

final case class ApiConfig(host: String, port: Int)

object ApiConfig {

  private val serverConfigDescription =
    nested("api") {
      string("host") <*>
      int("port")
    }.to[ApiConfig]

  val layer: ZLayer[Any, ReadError[String], ApiConfig] = ZLayer(
    read(
      serverConfigDescription.from(
        TypesafeConfigSource.fromTypesafeConfig(
          ZIO.attempt(ConfigFactory.defaultApplication())
        )
      )
    )
  )
}
