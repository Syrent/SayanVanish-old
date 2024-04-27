package org.sayandevelopment.sayanvanish.api

import net.kyori.adventure.text.Component
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

    }

    open fun sendActionbar(content: Component) {

    }

}