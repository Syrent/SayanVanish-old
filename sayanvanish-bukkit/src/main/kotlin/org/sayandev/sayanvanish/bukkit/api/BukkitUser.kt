package org.sayandev.sayanvanish.bukkit.api

import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.OfflinePlayer
import org.bukkit.entity.Player
import org.sayandev.sayanvanish.api.User
import org.sayandev.stickynote.bukkit.utils.AdventureUtils.component
import org.sayandev.stickynote.bukkit.utils.AdventureUtils.sendActionbar
import org.sayandev.stickynote.bukkit.utils.AdventureUtils.sendMessage
import java.util.*

open class BukkitUser(
    override val uniqueId: UUID,
    override var username: String
) : User {

    override var isVanished = false
    override var isOnline: Boolean = false
    override var vanishLevel: Int = 1

    val player: Player?
        get() = Bukkit.getPlayer(uniqueId)
    val offlinePlayer: OfflinePlayer
        get() = Bukkit.getOfflinePlayer(uniqueId)

    override fun vanish() {
        super.vanish()
        sendMessage("<green>Vanished!".component())
    }

    override fun unVanish() {
        super.unVanish()
        sendMessage("<red>Unvanished!".component())
    }

    override fun hasPermission(permission: String): Boolean {
        return player?.hasPermission(permission) ?: false
    }

    override fun sendMessage(content: Component) {
        player?.sendMessage(content)
    }

    override fun sendActionbar(content: Component) {
        player?.sendActionbar(content)
    }

    companion object {
        @JvmStatic
        fun fromUser(user: User): BukkitUser {
            return BukkitUser(user.uniqueId, user.username).apply {
                this.isOnline = user.isOnline
                this.isVanished = user.isVanished
                this.vanishLevel = user.vanishLevel
            }
        }
    }

}