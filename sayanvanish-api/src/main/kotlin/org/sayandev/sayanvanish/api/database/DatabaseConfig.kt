package org.sayandev.sayanvanish.api.database

import org.sayandev.sayanvanish.api.Platform
import org.sayandev.stickynote.core.configuration.Config
import org.sayandev.stickynote.lib.spongepowered.configurate.objectmapping.ConfigSerializable
import org.sayandev.stickynote.lib.spongepowered.configurate.objectmapping.meta.Setting
import java.io.File

val databaseConfig = DatabaseConfig.fromConfig() ?: DatabaseConfig.defaultConfig()

@ConfigSerializable
data class DatabaseConfig(
    val method: DatabaseMethod = DatabaseMethod.SQLITE,
    val host: String = "localhost",
    val port: Int = 3306,
    val database: String = "root",
    val username: String = "minecraft",
    val password: String = "",
    val poolProperties: PoolProperties = PoolProperties(),
    val tablePrefix: String = "sayanvanish_",
    val useCacheWhenAvailable: Boolean = true,
) : Config(Platform.get().rootDirectory, fileName) {

    init {
        load()
    }

    @ConfigSerializable
    data class PoolProperties(
        val maximumPoolSize: Int = 10,
        val minimumIdle: Int = 10,
        val maximumLifetime: Long = 1800000,
        val keepaliveTime: Long = 0,
        val connectionTimeout: Long = 5000,
        @Setting("use-ssl") val useSSL: Boolean = false
    )

    companion object {
        private val fileName = "database.yml"

        @JvmStatic
        fun defaultConfig(): DatabaseConfig {
            return DatabaseConfig()
        }

        @JvmStatic
        fun fromConfig(): DatabaseConfig? {
            return fromConfig<DatabaseConfig>(File(Platform.get().rootDirectory, fileName))
        }
    }

}