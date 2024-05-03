package org.sayandevelopment.sayanvanish.api

import org.sayandevelopment.sayanvanish.api.database.DatabaseConfig
import org.sayandevelopment.sayanvanish.api.database.DatabaseExecutor
import org.sayandevelopment.sayanvanish.api.database.DatabaseMethod
import org.sayandevelopment.stickynote.core.database.sqlite.SQLiteDatabase
import java.io.File
import java.util.*

open class SayanVanishAPI<U: User>(val useCache: Boolean) {
    constructor(): this(false)

    val databaseExecutor = DatabaseExecutor<U>(
        SQLiteDatabase(File(Platform.getCurrentPlatform().rootDirectory, "storage.db"), Platform.getCurrentPlatform().logger),
        DatabaseConfig.fromConfig() ?: DatabaseConfig(DatabaseMethod.SQLITE)
    ).apply {
        this.connect()
        this.initialize()
    }

    fun getPlatform(): Platform {
        return Platform.getCurrentPlatform()
    }

    fun getUsers(): List<U> {
        return databaseExecutor.getUsers(useCache)
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
        return databaseExecutor.getUser(uniqueId, useCache)
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