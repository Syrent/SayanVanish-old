package org.sayandev.sayanvanish.api

import org.sayandev.sayanvanish.api.database.DatabaseExecutor
import org.sayandev.sayanvanish.api.database.databaseConfig
import java.util.*
import kotlin.reflect.KClass

open class SayanVanishAPI<U: User>(val type: KClass<out User>, val useCache: Boolean) {
    constructor(type: KClass<out User>): this(type, false)
    constructor(): this(User::class)

    val databaseExecutor = DatabaseExecutor<U>(databaseConfig).apply {
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

    fun getBasicUsers(): List<BasicUser> {
        return databaseExecutor.getBasicUsers(useCache)
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

    fun addBasicUser(user: BasicUser) {
        databaseExecutor.addBasicUser(user)
    }

    fun removeUser(uniqueId: UUID) {
        databaseExecutor.removeUser(uniqueId)
    }

    fun removeBasicUser(uniqueId: UUID) {
        databaseExecutor.removeBasicUser(uniqueId)
    }

    fun removeUser(user: U) {
        removeUser(user.uniqueId)
    }

    fun getUser(uniqueId: UUID): U? {
        return databaseExecutor.getUser(uniqueId, useCache, type)
    }

    companion object {
        private val defaultInstance = SayanVanishAPI<User>()

        @JvmStatic
        fun getInstance(): SayanVanishAPI<User> {
            return defaultInstance
        }

        fun UUID.user(): User? {
            return getInstance().getUser(this)
        }
    }

}