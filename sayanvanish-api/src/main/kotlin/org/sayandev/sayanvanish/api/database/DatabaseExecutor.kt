package org.sayandev.sayanvanish.api.database

import org.sayandev.sayanvanish.api.Platform
import org.sayandev.sayanvanish.api.User
import org.sayandev.sayanvanish.api.User.Companion.cast
import org.sayandev.sayanvanish.api.VanishOptions
import org.sayandev.stickynote.core.database.Query
import org.sayandev.stickynote.core.database.mysql.MySQLCredentials
import org.sayandev.stickynote.core.database.mysql.MySQLDatabase
import org.sayandev.stickynote.core.database.sqlite.SQLiteDatabase
import java.io.File
import java.util.*
import kotlin.reflect.KClass
import kotlin.reflect.safeCast


class DatabaseExecutor<U : User>(
    val config: DatabaseConfig
) : Database<U> {

    val cache = mutableMapOf<UUID, U>()
    val database: org.sayandev.stickynote.core.database.Database = when (config.method.name.lowercase()) {
        DatabaseMethod.MYSQL.name.lowercase() -> {
            MySQLDatabase(MySQLCredentials.Companion.mySQLCredentials("localhost", 3306, "sayanvanish", false, "root", "admin"), 5)
        }
        DatabaseMethod.SQLITE.name.lowercase() -> {
            SQLiteDatabase(File(Platform.get().rootDirectory, "storage.db"), Platform.get().logger)
        }
        else -> {
            throw NullPointerException("Database method with id `${config.method.name}` doesn't exist, available database types: ${DatabaseMethod.entries.map { it.name.lowercase() }}")
        }
    }

    val type = DatabaseMethod.entries.find { it == config.method } ?: throw IllegalArgumentException("${config.method} is not a valid database method! valid methods: `${DatabaseMethod.entries.joinToString(", ")}`")

    override fun initialize() {
        database.runQuery(Query.query("CREATE TABLE IF NOT EXISTS ${config.tablePrefix}users (UUID VARCHAR(64),username VARCHAR(16),is_vanished INT,is_online INT,vanish_level INT,PRIMARY KEY (UUID));"))
    }

    override fun connect() {
        cache.clear()
        database.connect()
    }

    override fun disconnect() {
        database.shutdown()
        cache.clear()
    }

    override fun getUser(uniqueId: UUID, useCache: Boolean, type: KClass<out User>): U? {
        if (useCache) {
            return cache[uniqueId]
        }

        val result = database.runQuery(Query.query("SELECT * FROM ${config.tablePrefix}users WHERE UUID = ?;").setStatementValue(1, uniqueId.toString())).result ?: return null
        if (!result.next()) return null
        val user = object : User {
            override val uniqueId: UUID = UUID.fromString(result.getString("UUID"))
            override var username: String = result.getString("username")
            override var currentOptions: VanishOptions = VanishOptions.defaultOptions()

            override var isVanished: Boolean = result.getBoolean("is_vanished")
            override var isOnline: Boolean = result.getBoolean("is_online")
            override var vanishLevel: Int = result.getInt("vanish_level")
        }
        result.close()
        return (type.safeCast(user) as? U) ?: (user.cast(type) as U)
    }

    override fun getUsers(useCache: Boolean): List<U> {
        return getUsers(useCache, User::class)
    }

    override fun getUsers(useCache: Boolean, type: KClass<out User>): List<U> {
        if (useCache) {
            return cache.values.toList()
        }

        val result = database.runQuery(Query.query("SELECT * FROM ${config.tablePrefix}users;")).result ?: return emptyList()
        val users = mutableListOf<U>()
        while (result.next()) {
            val user = object : User {
                override val uniqueId: UUID = UUID.fromString(result.getString("UUID"))
                override var username: String = result.getString("username")
                override var currentOptions: VanishOptions = VanishOptions.defaultOptions()

                override var isVanished: Boolean = result.getBoolean("is_vanished")
                override var isOnline: Boolean = result.getBoolean("is_online")
                override var vanishLevel: Int = result.getInt("vanish_level")
            }
            users.add((type.safeCast(user) as? U) ?: (user.cast(type) as U))
        }
        result.close()
        return users
    }

    override fun getUser(uniqueId: UUID, useCache: Boolean): U? {
        return getUser(uniqueId, useCache, User::class)
    }

    override fun addUser(user: U) {
        cache[user.uniqueId] = user
        if (!hasUser(user.uniqueId, false)) {
            database.runQuery(
                Query.query("INSERT ${if (type == DatabaseMethod.MYSQL) "IGNORE " else ""}INTO ${config.tablePrefix}users (UUID, username, is_vanished, is_online, vanish_level) VALUES (?,?,?,?,?);")
                    .setStatementValue(1, user.uniqueId.toString())
                    .setStatementValue(2, user.username)
                    .setStatementValue(3, user.isVanished)
                    .setStatementValue(4, user.isOnline)
                    .setStatementValue(5, user.vanishLevel)
            )
        } else {
            updateUser(user)
        }
    }

    override fun hasUser(uniqueId: UUID, useCache: Boolean): Boolean {
        if (useCache) {
            return cache.contains(uniqueId)
        }
        val queryResult = database.runQuery(Query.query("SELECT * FROM ${config.tablePrefix}users WHERE UUID = ?;").setStatementValue(1, uniqueId.toString()))
        val result = queryResult.result ?: return false
        val hasNext = result.next()
        result.close()
        return hasNext
    }

    override fun removeUser(uniqueId: UUID) {
        cache.remove(uniqueId)
        database.runQuery(Query.query("DELETE FROM ${config.tablePrefix}users WHERE UUID = ?;").setStatementValue(1, uniqueId.toString()))
    }

    override fun updateUser(user: U) {
        cache[user.uniqueId] = user
        database.runQuery(
            Query.query("UPDATE ${config.tablePrefix}users SET username = ?, is_vanished = ?, is_online = ?, vanish_level = ? WHERE UUID = ?;")
                .setStatementValue(1, user.username)
                .setStatementValue(2, user.isVanished)
                .setStatementValue(3, user.isOnline)
                .setStatementValue(4, user.vanishLevel)
                .setStatementValue(5, user.uniqueId.toString())
        )
    }

    override fun purgeCache() {
        cache.clear()
    }

    override fun purge() {
        database.runQuery(Query.query("DELETE FROM ${config.tablePrefix}users;"))
    }

}
