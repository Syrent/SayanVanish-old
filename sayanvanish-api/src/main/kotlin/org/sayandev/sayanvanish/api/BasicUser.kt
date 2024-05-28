package org.sayandev.sayanvanish.api

import java.util.*

interface BasicUser {

    val uniqueId: UUID
    var username: String
    var serverId: String

    fun save() {
        SayanVanishAPI.getInstance().addBasicUser(this)
    }

    companion object {
        fun create(uniqueId: UUID, username: String, serverId: String?): BasicUser {
            return object : BasicUser {
                override val uniqueId: UUID = uniqueId
                override var username: String = username
                override var serverId: String = serverId ?: Platform.get().id
            }
        }
    }

}