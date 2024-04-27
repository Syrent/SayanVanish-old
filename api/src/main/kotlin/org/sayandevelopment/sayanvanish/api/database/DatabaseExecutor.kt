package org.sayandevelopment.sayanvanish.api.database

import org.sayandevelopment.core.database.Query
import org.sayandevelopment.sayanvanish.api.User
import java.util.*
import java.util.logging.Logger


class DatabaseExecutor<U : User>(
    val database: org.sayandevelopment.core.database.Database,
    val config: DatabaseConfig
) : Database<U> {

    val type = DatabaseMethod.entries.find { it == config.method } ?: throw IllegalArgumentException(
        "${config.method} is not a valid database method! valid methods: `${
            DatabaseMethod.entries.joinToString(
                ", "
            )
        }`"
    )

    override fun initialize() {
        database.runQuery(Query.query("CREATE TABLE IF NOT EXISTS ${config.tablePrefix}users (UUID VARCHAR(64),username VARCHAR(16),is_vanished INT,is_online INT,vanish_level INT,PRIMARY KEY (UUID));"))
    }

    override fun connect() {
        database.connect()
    }

    override fun disconnect() {
        database.shutdown()
    }

    override fun getUser(uniqueId: UUID): U? {
        val result = database.runQuery(Query.query("SELECT * FROM ${config.tablePrefix}users WHERE UUID = ?;").setStatementValue(1, uniqueId.toString())).result ?: return null
        return object : User {
            override val uniqueId: UUID = UUID.fromString(result.getString("UUID"))
            override val username: String = result.getString("username")

            override var isVanished: Boolean = result.getBoolean("is_vanished")
            override var isOnline: Boolean = result.getBoolean("is_online")
            override var vanishLevel: Int = result.getInt("vanish_level")
        } as U
    }

    override fun addUser(user: U) {
        val result = database.runQuery(
            Query.query("SELECT UUID FROM ${config.tablePrefix}users WHERE UUID = ?;")
                .setStatementValue(1, user.uniqueId.toString())
        ).result ?: throw NullPointerException("Couldn't get result for addUser database query")
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

    override fun updateUser(user: U) {
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
