package org.sayandev.sayanvanish.api

import org.sayandev.sayanvanish.api.database.DatabaseConfig
import org.sayandev.sayanvanish.api.database.DatabaseExecutor
import org.sayandev.sayanvanish.api.database.DatabaseMethod
import org.sayandev.stickynote.core.database.mysql.MySQLCredentials
import org.sayandev.stickynote.core.database.mysql.MySQLDatabase
import org.sayandev.stickynote.core.database.sqlite.SQLiteDatabase
import java.io.File
import java.util.*
import kotlin.reflect.KClass

open class SayanVanishAPI<U: User>(val type: KClass<out User>, val useCache: Boolean) {
    constructor(type: KClass<out User>): this(type, false)
    constructor(): this(User::class)

    val databaseExecutor = DatabaseExecutor<U>(
        DatabaseConfig.fromConfig() ?: DatabaseConfig(DatabaseMethod.SQLITE)
    ).apply {
        this.connect()
        this.initialize()
    }

    fun getPlatform(): Platform {
        return Platform.get()
    }

    fun getUsers(): List<U> {
        return databaseExecutor.getUsers(useCache, type)
    }

    fun getUsers(predicate: (U) -> Boolean): List<U> {
        return getUsers().filter(predicate)
    }

    fun getSortedUsers(predicate: (U) -> Int) {
        getUsers().sortedByDescending(predicate)
    }

    fun getVanishedUsers(): Collection<U> {
        return getUsers(User::isVanished)
    }

    fun getOnlineUsers(): Collection<U> {
        return getUsers(User::isOnline)
    }

    fun addUser(user: U) {
        databaseExecutor.addUser(user)
    }

    fun removeUser(uniqueId: UUID) {
        databaseExecutor.removeUser(uniqueId)
    }

    fun removeUser(user: U) {
        removeUser(user.uniqueId)
    }

    fun getUser(uniqueId: UUID): U? {
        return databaseExecutor.getUser(uniqueId, useCache, type)
    }

    companion object {
        @JvmStatic
        fun getInstance(): SayanVanishAPI<User> {
            return SayanVanishAPI()
        }

        fun UUID.user(): User? {
            return getInstance().getUser(this)
        }
    }

}