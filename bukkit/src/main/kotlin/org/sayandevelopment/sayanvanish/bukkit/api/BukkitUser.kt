package org.sayandevelopment.sayanvanish.bukkit.api

import org.sayandevelopment.sayanvanish.bukkit.utils.ComponentUtils.sendActionbar
import org.sayandevelopment.sayanvanish.bukkit.utils.ComponentUtils.sendMessage
import org.sayandevelopment.sayanvanish.api.exception.UnsupportedPlatformException
import org.sayandevelopment.sayanvanish.api.User
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.OfflinePlayer
import org.bukkit.entity.Player
import java.util.*

class BukkitUser(
    override val uniqueId: UUID,
    override val username: String
) : User {

    override var isVanished = false

    val player: Player?
        get() = Bukkit.getPlayer(uniqueId)
    val offlinePlayer: OfflinePlayer
        get() = Bukkit.getOfflinePlayer(uniqueId)

    override fun vanish() {
        super.vanish()
        throw UnsupportedPlatformException("vanish")
    }

    override fun unVanish() {
        super.unVanish()
        throw UnsupportedPlatformException("unVanish")
    }

    override fun sendMessage(content: Component) {
        player?.sendMessage(content)
    }

    override fun sendActionbar(content: Component) {
        player?.sendActionbar(content)
    }

}