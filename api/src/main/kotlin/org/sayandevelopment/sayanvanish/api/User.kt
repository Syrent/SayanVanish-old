package org.sayandevelopment.sayanvanish.api

import net.kyori.adventure.text.Component
import java.util.*

interface User {

    val uniqueId: UUID
    val username: String

    var isVanished: Boolean

    fun vanish() {
        isVanished = true
    }

    fun unVanish() {
        isVanished = false
    }

    fun sendMessage(content: Component)

    fun sendActionbar(content: Component)

}