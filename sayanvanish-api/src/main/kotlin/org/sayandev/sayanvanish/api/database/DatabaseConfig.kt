package org.sayandev.sayanvanish.api.database

import org.sayandev.sayanvanish.api.Platform
import org.sayandev.stickynote.core.configuration.Config
import org.spongepowered.configurate.objectmapping.ConfigSerializable
import org.spongepowered.configurate.objectmapping.meta.Setting
import java.io.File

@ConfigSerializable
data class DatabaseConfig(
    val method: DatabaseMethod = DatabaseMethod.SQLITE,
    val host: String = "localhost",
    val port: Int = 3306,
    val database: String = "root",
    val username: String = "minecraft",
    val password: String = "",
    val poolProperties: PoolProperties = PoolProperties(),
    val tablePrefix: String = "sayanvanish_"
) : Config(Platform.get().rootDirectory, FILE_NAME) {

    init {
        load()
    }

    @ConfigSerializable
    data class PoolProperties(
        val maximumPoolSize: Int = 10,
        val minimumIdle: Int = 10,
        val maximumLifetime: Long = 1800000,
        val keepaliveTime: Int = 0,
        val connectionTimeout: Int = 5000,
        @Setting("use-ssl") val useSSL: Boolean = false
    )

    companion object {
        private val FILE_NAME = "database.yml"

        @JvmStatic
        fun defaultConfig(): DatabaseConfig {
            return DatabaseConfig()
        }

        @JvmStatic
        fun fromConfig(): DatabaseConfig? {
            return fromConfig<DatabaseConfig>(File(Platform.get().rootDirectory, FILE_NAME))
        }
    }

}