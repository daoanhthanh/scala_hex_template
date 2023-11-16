package vn.ventures.secondaryAdapter.flyway
import scala.jdk.CollectionConverters._

final case class JdbcDatabaseConfig(
    url: String,
    driver: String,
    user: String,
    password: String,
    migrationsTable: String,
    migrationsLocations: List[String],
    autoRun: Boolean
)

object JdbcDatabaseConfig {
  def loadFromGlobal(configNamespace: String): JdbcDatabaseConfig = {
    val config = io.getquill.util.LoadConfig(configNamespace)
    JdbcDatabaseConfig(
      config.getString("url"),
      config.getString("driver"),
      config.getString("user"),
      config.getString("password"),
      config.getString("migrationsTable"),
      config.getStringList("migrationsLocations").asScala.toList,
      config.getBoolean("autoRun")
    )
  }

}

// docker run --rm --name mysql-local -e MYSQL_ROOT_PASSWORD=example -p 3666:3306 mysql:8.1.0 --restart always
