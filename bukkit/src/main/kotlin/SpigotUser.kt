package ir.syrent

import java.util.*

class SpigotUser(override val uniqueId: UUID, override val username: String, override var isVanished: Boolean) : User {

    override fun vanish() {

    }

    override fun unVanish() {

    }
}