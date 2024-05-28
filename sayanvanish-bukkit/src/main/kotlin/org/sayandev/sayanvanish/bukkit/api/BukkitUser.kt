package org.sayandev.sayanvanish.bukkit.api

import org.bukkit.Bukkit
import org.bukkit.OfflinePlayer
import org.bukkit.entity.Creature
import org.bukkit.entity.Player
import org.bukkit.metadata.FixedMetadataValue
import org.bukkit.scoreboard.Team
import org.sayandev.sayanvanish.api.Permission
import org.sayandev.sayanvanish.api.Platform
import org.sayandev.sayanvanish.api.User
import org.sayandev.sayanvanish.api.VanishOptions
import org.sayandev.sayanvanish.bukkit.VanishManager
import org.sayandev.sayanvanish.bukkit.api.SayanVanishBukkitAPI.Companion.getOrCreateUser
import org.sayandev.sayanvanish.bukkit.api.SayanVanishBukkitAPI.Companion.user
import org.sayandev.sayanvanish.bukkit.api.event.BukkitUserUnVanishEvent
import org.sayandev.sayanvanish.bukkit.api.event.BukkitUserVanishEvent
import org.sayandev.sayanvanish.bukkit.config.language
import org.sayandev.sayanvanish.bukkit.config.settings
import org.sayandev.stickynote.bukkit.*
import org.sayandev.stickynote.bukkit.utils.AdventureUtils.component
import org.sayandev.stickynote.bukkit.utils.AdventureUtils.sendActionbar
import org.sayandev.stickynote.bukkit.utils.AdventureUtils.sendMessage
import org.sayandev.stickynote.lib.kyori.adventure.text.Component
import org.sayandev.stickynote.lib.kyori.adventure.text.minimessage.tag.resolver.Placeholder
import xyz.jpenilla.squaremap.api.SquaremapProvider
import java.util.*

open class BukkitUser(
    override val uniqueId: UUID,
    override var username: String
) : User {

    override var serverId = Platform.get().id
    override var currentOptions = VanishOptions.defaultOptions()
    override var isVanished = false
    override var isOnline: Boolean = false
    override var vanishLevel: Int = 1

    fun stateText(isVanished: Boolean = this.isVanished) = if (isVanished) "<green>ON</green>" else "<red>OFF</red>"

    fun player(): Player? = Bukkit.getPlayer(uniqueId)
    fun offlinePlayer(): OfflinePlayer = Bukkit.getOfflinePlayer(uniqueId)

    override fun vanish(options: VanishOptions) {
        val vanishEvent = BukkitUserVanishEvent(this, options)
        server.pluginManager.callEvent(vanishEvent)
        if (vanishEvent.isCancelled) return
        val options = vanishEvent.options
        currentOptions = options

        if (options.sendMessage) {
            val quitMessage = VanishManager.generalQuitMessage
            if (quitMessage != null) {
                for (onlinePlayer in onlinePlayers) {
                    onlinePlayer.sendMessage(quitMessage)
                }
            }
        }

        player()?.isCollidable = false
        player()?.isSleepingIgnored = true

        if (hasPermission(Permission.FLY) || settings.vanish.fly.enabled) {
            player()?.allowFlight = true
            player()?.isFlying = true
        }
        if (hasPermission(Permission.INVULNERABLE) || settings.vanish.invulnerability.enabled) {
            player()?.isInvulnerable = true
        }

        if (settings.vanish.prevention.push) {
            denyPush()
        }

        if (settings.vanish.prevention.mobTarget) {
            player()?.world?.entities?.stream()
                ?.filter { entity -> entity is Creature }
                ?.map { entity -> entity as Creature }
                ?.filter { mob -> mob.target != null }
                ?.filter { mob -> player()?.uniqueId == mob.target?.uniqueId }
                ?.forEach { mob -> mob.target = null }
        }

        player()?.setMetadata("vanished", FixedMetadataValue(plugin, true))

        hideUser()

        if (hasPlugin("squaremap")) {
            player()?.uniqueId?.let { SquaremapProvider.get().playerManager().hide(it, true) }
        }

        super.vanish(options)

        sendMessage(language.vanish.vanishStateUpdate.component(Placeholder.parsed("state", stateText())))
    }

    override fun unVanish(options: VanishOptions) {
        val unVanishEvent = BukkitUserUnVanishEvent(this, options)
        server.pluginManager.callEvent(unVanishEvent)
        if (unVanishEvent.isCancelled) return
        val options = unVanishEvent.options
        currentOptions = options

        if (options.sendMessage) {
            val joinMessage = VanishManager.generalJoinMessage
            if (joinMessage != null) {
                for (onlinePlayer in onlinePlayers) {
                    onlinePlayer.sendMessage(joinMessage)
                }
            }
        }

        player()?.isCollidable = true
        player()?.isSleepingIgnored = false

        if (!hasPermission(Permission.FLY) && settings.vanish.fly.disableOnReappear) {
            player()?.allowFlight = false
            player()?.isFlying = false
        }
        if (hasPermission(Permission.INVULNERABLE) || settings.vanish.invulnerability.disableOnReappear) {
            player()?.isInvulnerable = false
        }

        if (settings.vanish.prevention.push) {
            allowPush()
        }

        player()?.sendActionbar(Component.empty())

        player()?.setMetadata("vanished", FixedMetadataValue(plugin, false))
        showUser()

        if (hasPlugin("squaremap")) {
            player()?.uniqueId?.let { SquaremapProvider.get().playerManager().show(it, true) }
        }

        super.unVanish(options)

        sendMessage(language.vanish.vanishStateUpdate.component(Placeholder.parsed("state", stateText())))
    }

    override fun hasPermission(permission: String): Boolean {
        return player()?.hasPermission(permission) ?: false
    }

    override fun sendMessage(content: String) {
        player()?.sendMessage(content.component())
    }

    fun sendMessage(content: Component) {
        player()?.sendMessage(content)
    }

    override fun sendActionbar(content: String) {
        player()?.sendActionbar(content.component())
    }

    fun sendActionbar(content: Component) {
        player()?.sendActionbar(content)
    }

    fun hideUser() {
        for (onlinePlayer in onlinePlayers) {
            hideUser(onlinePlayer)
        }
        if (currentOptions.notifyOthers) {
            for (otherUsers in SayanVanishBukkitAPI.getInstance().getOnlineUsers().filter { it.username != username && it.vanishLevel >= vanishLevel }) {
                otherUsers.sendMessage(language.vanish.vanishStateOther.component(Placeholder.parsed("player", username), Placeholder.parsed("state", stateText(true))))
            }
        }
    }

    fun hideUser(target: Player) {
        if (target.user() == null && (target.isOp || target.hasPermission(Permission.VANISH.permission()))) {
            target.getOrCreateUser()
        }
        val playerVanishLevel = target.user()?.vanishLevel ?: 0
        if (playerVanishLevel < vanishLevel || !settings.vanish.level.enabled) {
            player()?.let { target.hidePlayer(plugin, it) }
        }
    }

    fun showUser() {
        for (onlinePlayer in onlinePlayers) {
            showUser(onlinePlayer)
        }
        if (currentOptions.notifyOthers) {
            for (otherUsers in SayanVanishBukkitAPI.getInstance().getOnlineUsers().filter { it.username != username && it.vanishLevel >= vanishLevel }) {
                otherUsers.sendMessage(language.vanish.vanishStateOther.component(Placeholder.parsed("player", username), Placeholder.parsed("state", stateText(false))))
            }
        }
    }

    fun showUser(target: Player) {
        player()?.let { target.showPlayer(plugin, it) }
    }

    fun denyPush() {
        val player = player() ?: return
        var team = player.scoreboard.getTeam("Vanished")
        if (team == null) {
            team = player.scoreboard.registerNewTeam("Vanished")
        }
        team.setOption(Team.Option.COLLISION_RULE, Team.OptionStatus.NEVER)
        team.addEntry(player.name)
    }

    fun allowPush() {
        val player = player() ?: return
        player.scoreboard.getTeam("Vanished")?.removeEntry(player.name)
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