package org.sayandev.sayanvanish.api.database

import org.sayandev.sayanvanish.api.User
import java.util.*
import kotlin.reflect.KClass

interface Database<U: User> {

    fun initialize()

    fun connect()

    fun disconnect()

    fun getUser(uniqueId: UUID, useCache: Boolean, type: KClass<out User>): U?

    fun getUser(uniqueId: UUID, useCache: Boolean): U?

    fun getUsers(useCache: Boolean, type: KClass<out User>): List<U>

    fun getUsers(useCache: Boolean): List<U>

    fun updateUser(user: U)

    fun addUser(user: U)

    fun hasUser(uniqueId: UUID, useCache: Boolean): Boolean

    fun removeUser(uniqueId: UUID)

    fun purgeCache()

    fun purge()

}