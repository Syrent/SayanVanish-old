package ir.syrent

import net.kyori.adventure.text.Component
import java.util.UUID

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

    fun sendMessage(message: Component, placeholders: List<Placeholder>)

}