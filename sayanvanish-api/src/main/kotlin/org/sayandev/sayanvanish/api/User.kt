package org.sayandev.sayanvanish.api

import org.sayandev.sayanvanish.api.exception.UnsupportedPlatformException
import org.sayandev.stickynote.lib.kyori.adventure.text.Component
import java.util.*
import kotlin.reflect.KClass

interface User {

    val uniqueId: UUID
    var username: String

    var currentOptions: VanishOptions
    var isVanished: Boolean
    var isOnline: Boolean
    var vanishLevel: Int

    fun vanish(options: VanishOptions) {
        isVanished = true
        save()
    }

    fun vanish() {
        vanish(VanishOptions.defaultOptions())
    }

    fun unVanish(options: VanishOptions) {
        isVanished = false
        save()
    }

    fun unVanish() {
        unVanish(VanishOptions.defaultOptions())
    }

    fun toggleVanish(options: VanishOptions) {
        if (isVanished) unVanish(options) else vanish(options)
    }

    fun toggleVanish() {
        toggleVanish(VanishOptions.defaultOptions())
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