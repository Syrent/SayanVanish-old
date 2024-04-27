package org.sayandevelopment.sayanvanish.api.database

import org.sayandevelopment.sayanvanish.api.User
import java.util.UUID

interface Database<U: User> {

    fun initialize()

    fun connect()

    fun disconnect()

    fun getUser(uniqueId: UUID): U?

    fun updateUser(user: U)

    fun addUser(user: U)

}