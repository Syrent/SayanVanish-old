package org.sayandev.sayanvanish.api

import net.kyori.adventure.text.Component
import org.sayandev.sayanvanish.api.exception.UnsupportedPlatformException
import java.util.*
import kotlin.reflect.KClass

interface User {

    val uniqueId: UUID
    var username: String

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
        throw UnsupportedPlatformException("hasPermission")
    }

    fun hasPermission(permission: Permission): Boolean {
        return hasPermission(permission.permission())
    }

    fun save() {
        SayanVanishAPI.getInstance().addUser(this)
    }

    companion object {
        fun User.cast(to: KClass<out User>): Any {
            return to.java.getDeclaredMethod("fromUser", User::class.java).invoke(null, this)
        }
    }

}