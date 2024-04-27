package org.sayandevelopment.sayanvanish.api.database

data class DatabaseConfig(
    val method: DatabaseMethod = DatabaseMethod.SQLITE,
    val host: String = "localhost",
    val port: Int = 3306,
    val database: String = "root",
    val username: String = "minecraft",
    val password: String = "",
    val poolProperties: PoolProperties = PoolProperties(),
    val tablePrefix: String = "sayanvanish_"
) {

    data class PoolProperties(
        val maximumPoolSize: Int = 10,
        val minimumIdle: Int = 10,
        val maximumLifetime: Long = 1800000,
        val keepaliveTime: Int = 0,
        val connectionTimeout: Int = 5000,
        val useSSL: Boolean = false
    )

    companion object {
        @JvmStatic
        fun defaultConfig(): DatabaseConfig {
            return DatabaseConfig()
        }
    }

}
