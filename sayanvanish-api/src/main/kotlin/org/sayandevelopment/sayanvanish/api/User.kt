package org.sayandevelopment.sayanvanish.api

import net.kyori.adventure.text.Component
import org.sayandevelopment.sayanvanish.api.exception.UnsupportedPlatformException
import java.util.*

interface User {

    val uniqueId: UUID
    val username: String

    var isVanished: Boolean
    var isOnline: Boolean
    var vanishLevel: Int

    fun vanish() {
        isVanished = true
    }

    fun unVanish() {
        isVanished = false
    }

    open fun sendMessage(content: Component) {
        throw UnsupportedPlatformException("sendMessage")
    }

    open fun sendActionbar(content: Component) {
        throw UnsupportedPlatformException("sendActionbar")
    }

    open fun hasPermission(permission: String): Boolean {
        return false
    }

    fun save() {
        SayanVanishAPI.getInstance().addUser(this)
    }

}