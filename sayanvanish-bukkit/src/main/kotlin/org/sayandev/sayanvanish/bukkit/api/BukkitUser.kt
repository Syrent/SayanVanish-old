package org.sayandev.sayanvanish.bukkit.api

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder
import org.bukkit.Bukkit
import org.bukkit.OfflinePlayer
import org.bukkit.entity.Creature
import org.bukkit.entity.Player
import org.bukkit.metadata.FixedMetadataValue
import org.sayandev.sayanvanish.api.Permission
import org.sayandev.sayanvanish.api.User
import org.sayandev.sayanvanish.api.VanishOptions
import org.sayandev.sayanvanish.bukkit.VanishManager
import org.sayandev.sayanvanish.bukkit.api.SayanVanishBukkitAPI.Companion.user
import org.sayandev.sayanvanish.bukkit.api.event.BukkitUserUnVanishEvent
import org.sayandev.sayanvanish.bukkit.api.event.BukkitUserVanishEvent
import org.sayandev.stickynote.bukkit.onlinePlayers
import org.sayandev.stickynote.bukkit.plugin
import org.sayandev.stickynote.bukkit.server
import org.sayandev.stickynote.bukkit.utils.AdventureUtils.component
import org.sayandev.stickynote.bukkit.utils.AdventureUtils.sendActionbar
import org.sayandev.stickynote.bukkit.utils.AdventureUtils.sendMessage
import java.util.*

open class BukkitUser(
    override val uniqueId: UUID,
    override var username: String
) : User {

    override var currentOptions = VanishOptions.defaultOptions()
    override var isVanished = false
    override var isOnline: Boolean = false
    override var vanishLevel: Int = 1

    fun stateText() = if (isVanished) "<green>ON</green>" else "<red>OFF</red>"

    val player: Player?
        get() = Bukkit.getPlayer(uniqueId)
    val offlinePlayer: OfflinePlayer
        get() = Bukkit.getOfflinePlayer(uniqueId)

    override fun vanish(options: VanishOptions) {
        val vanishEvent = BukkitUserVanishEvent(this)
        server.pluginManager.callEvent(vanishEvent)
        if (vanishEvent.isCancelled) return

        super.vanish(options)

        sendMessage("<gray>Your vanish state has been updated to <state>.".component(Placeholder.parsed("state", stateText())))

        if (options.sendMessage) {
            val quitMessage = VanishManager.generalQuitMessage
            if (quitMessage != null) {
                for (onlinePlayer in onlinePlayers) {
                    onlinePlayer.sendMessage(quitMessage)
                }
            }
        }

        player?.isCollidable = false
        player?.isSleepingIgnored = true

        if (hasPermission(Permission.FLY)) {
            player?.allowFlight = true
            player?.isFlying = true
        }
        if (hasPermission(Permission.INVULNERABLE)) {
            player?.isInvulnerable = true
        }

        player?.world?.entities?.stream()
            ?.filter { entity -> entity is Creature }
            ?.map { entity -> entity as Creature }
            ?.filter { mob -> mob.target != null }
            ?.filter { mob -> player?.uniqueId == mob.target?.uniqueId }
            ?.forEach { mob -> mob.target = null }

        player?.setMetadata("vanished", FixedMetadataValue(plugin, true))

        hideUser()
        currentOptions = options
    }

    override fun unVanish(options: VanishOptions) {
        val unVanishEvent = BukkitUserUnVanishEvent(this)
        server.pluginManager.callEvent(unVanishEvent)
        if (unVanishEvent.isCancelled) return

        super.unVanish(options)

        sendMessage("<gray>Your vanish state has been updated to <state>.".component(Placeholder.parsed("state", stateText())))

        if (options.sendMessage) {
            val joinMessage = VanishManager.generalJoinMessage
            if (joinMessage != null) {
                for (onlinePlayer in onlinePlayers) {
                    onlinePlayer.sendMessage(joinMessage)
                }
            }
        }

        player?.isCollidable = true
        player?.isSleepingIgnored = false

        if (!hasPermission(Permission.FLY)) {
            player?.allowFlight = false
            player?.isFlying = false
        }
        if (hasPermission(Permission.INVULNERABLE)) {
            player?.isInvulnerable = false
        }

        player?.setMetadata("vanished", FixedMetadataValue(plugin, false))
        showUser()
        currentOptions = options
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

    fun hideUser() {
        for (onlinePlayer in onlinePlayers) {
            hideUser(onlinePlayer)
        }
    }

    fun hideUser(target: Player) {
        val playerVanishLevel = target.user()?.vanishLevel ?: 0
        if (playerVanishLevel < vanishLevel) {
            player?.let { target.hidePlayer(plugin, it) }
        }
    }

    fun showUser() {
        for (onlinePlayer in onlinePlayers) {
            showUser(onlinePlayer)
        }
    }

    fun showUser(target: Player) {
        val playerVanishLevel = target.user()?.vanishLevel ?: 0
        if (playerVanishLevel < vanishLevel) {
            player?.let { target.showPlayer(plugin, it) }
        }
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