package org.sayandevelopment.sayanvanish.api

import java.util.UUID

open class SayanVanishAPI<U: User> {

    // TODO: Get from database
    val vanishedUsers = mutableMapOf<UUID, U>()

    fun getPlatform(): Platform {
        return Platform
    }

    fun getVanishedUsers(): Collection<U> {
        return vanishedUsers.values
    }

    fun addUser(user: U) {
        vanishedUsers[user.uniqueId] = user
        TODO("Not yet implemented")
    }

    fun removeUser(uniqueId: UUID) {
        vanishedUsers.remove(uniqueId)
        TODO("Not yet implemented")
    }

    fun removeUser(user: U) {
        removeUser(user.uniqueId)
    }

    fun getUser(uniqueId: UUID): U? {
        return vanishedUsers[uniqueId]
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