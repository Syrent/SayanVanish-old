package org.sayandevelopment.sayanvanish.api.database

import org.sayandevelopment.sayanvanish.api.User
import org.sayandevelopment.stickynote.core.database.Query
import java.util.*


class DatabaseExecutor<U : User>(
    val database: org.sayandevelopment.stickynote.core.database.Database,
    val config: DatabaseConfig
) : Database<U> {

    val cache = mutableMapOf<UUID, U>()

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

    override fun getUser(uniqueId: UUID, useCache: Boolean): U? {
        if (useCache) {
            return cache[uniqueId]
        }

        val result = database.runQuery(Query.query("SELECT * FROM ${config.tablePrefix}users WHERE UUID = ?;").setStatementValue(1, uniqueId.toString())).result ?: return null
        if (!result.next()) return null
        return object : User {
            override val uniqueId: UUID = UUID.fromString(result.getString("UUID"))
            override val username: String = result.getString("username")

            override var isVanished: Boolean = result.getBoolean("is_vanished")
            override var isOnline: Boolean = result.getBoolean("is_online")
            override var vanishLevel: Int = result.getInt("vanish_level")
        } as U
    }

    override fun getUsers(useCache: Boolean): List<U> {
        if (useCache) {
            return cache.values.toList()
        }

        val result = database.runQuery(Query.query("SELECT * FROM ${config.tablePrefix}users;")).result ?: return emptyList()
        val users = mutableListOf<U>()
        while (result.next()) {
            users.add(object : User {
                override val uniqueId: UUID = UUID.fromString(result.getString("UUID"))
                override val username: String = result.getString("username")

                override var isVanished: Boolean = result.getBoolean("is_vanished")
                override var isOnline: Boolean = result.getBoolean("is_online")
                override var vanishLevel: Int = result.getInt("vanish_level")
            } as U)
        }
        return users
    }

    override fun addUser(user: U) {
        cache[user.uniqueId] = user
        val result = database.runQuery(Query.query("SELECT UUID FROM ${config.tablePrefix}users WHERE UUID = ?;").setStatementValue(1, user.uniqueId.toString())).result ?: throw NullPointerException("Couldn't get result for addUser database query")
        if (!result.next()) {
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

    override fun removeUser(uniqueId: UUID) {
        cache.remove(uniqueId)
        database.runQuery(Query.query("DELETE FROM ${config.tablePrefix}users WHERE UUID = ?;").setStatementValue(1, uniqueId.toString()))
    }

    override fun purgeCache() {
        cache.clear()
    }

    override fun purge() {
        database.runQuery(Query.query("DELETE FROM ${config.tablePrefix}users;"))
    }

    override fun updateUser(user: U) {
        cache[user.uniqueId] = user
        database.queueQuery(
            Query.query("UPDATE ${config.tablePrefix}users SET username = ?, is_vanished = ?, is_online = ?, vanish_level = ? WHERE UUID = ?;")
                .setStatementValue(1, user.username)
                .setStatementValue(2, user.isVanished)
                .setStatementValue(3, user.isOnline)
                .setStatementValue(4, user.vanishLevel)
                .setStatementValue(5, user.uniqueId.toString())
        )
    }
}
