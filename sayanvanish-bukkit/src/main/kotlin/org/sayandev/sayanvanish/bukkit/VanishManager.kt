package org.sayandev.sayanvanish.bukkit

import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.*
import org.sayandev.sayanvanish.api.BasicUser
import org.sayandev.sayanvanish.api.Permission
import org.sayandev.sayanvanish.api.SayanVanishAPI
import org.sayandev.sayanvanish.api.VanishOptions
import org.sayandev.sayanvanish.bukkit.api.SayanVanishBukkitAPI
import org.sayandev.sayanvanish.bukkit.api.SayanVanishBukkitAPI.Companion.getOrCreateUser
import org.sayandev.sayanvanish.bukkit.api.SayanVanishBukkitAPI.Companion.user
import org.sayandev.sayanvanish.bukkit.config.settings
import org.sayandev.stickynote.bukkit.*

object VanishManager : Listener {

    private const val REMOVAL_MESSAGE_ID = "SAYANVANISH_DISABLE_MESSAGE"
    var generalJoinMessage: String? = null
    var generalQuitMessage: String? = null

    init {
        registerListener(this)
    }

    @EventHandler(priority = EventPriority.LOWEST)
    private fun vanishPlayerOnJoin(event: PlayerJoinEvent) {
        val player = event.player
        val user = player.user(false)
        val vanishJoinOptions = VanishOptions.Builder().sendMessage(false).notifyOthers(false).build()

        if (user == null) {
            val tempUser = player.getOrCreateUser()

            if (settings.vanish.state.checkPermissionOnQuit && !tempUser.hasPermission(Permission.VANISH)) {
                return
            }

            if (tempUser.hasPermission(Permission.VANISH_ON_JOIN) || settings.vanish.state.vanishOnJoin) {
                tempUser.isOnline = true
                tempUser.isVanished = true
                tempUser.vanish(vanishJoinOptions)
                tempUser.save()
            }
            return
        }

        user.isOnline = true

        if (settings.vanish.state.checkPermissionOnJoin && !user.hasPermission(Permission.VANISH)) {
            user.unVanish()
            user.save()
            return
        }

        if (user.hasPermission(Permission.VANISH_ON_JOIN) || (user.isVanished && settings.vanish.remember) || settings.vanish.state.vanishOnJoin) {
            user.isVanished = true
            user.vanish(vanishJoinOptions)
        }
        user.save()

        if (player.user(false)?.isVanished == true) {
            if (settings.vanish.joinLeaveMessage.getFromJoinEvent) {
                generalJoinMessage = event.joinMessage
            }
            event.joinMessage = REMOVAL_MESSAGE_ID
        }
        return
    }

    @EventHandler
    private fun addBasicUserOnJoin(event: PlayerJoinEvent) {
        if (settings.general.proxyMode) return

        val player = event.player
        SayanVanishAPI.getInstance().addBasicUser(BasicUser.create(player.uniqueId, player.name, null))
    }

    @EventHandler
    private fun removeBasicUserOnQuit(event: PlayerJoinEvent) {
        if (settings.general.proxyMode) return

        val player = event.player
        SayanVanishAPI.getInstance().removeBasicUser(player.uniqueId)
    }

    @EventHandler(priority = EventPriority.LOWEST)
    private fun updateUserOnQuit(event: PlayerQuitEvent) {
        val player = event.player
        val user = player.user() ?: return
        if ((settings.vanish.state.reappearOnQuit && user.isVanished) || (settings.vanish.state.checkPermissionOnQuit && !user.hasPermission(Permission.VANISH))) {
            user.unVanish()
        }
        user.isOnline = false
        user.save()

        if (user.isVanished) {
            if (settings.vanish.joinLeaveMessage.getFromQuitEvent) {
                generalQuitMessage = event.quitMessage
            }
            event.quitMessage = REMOVAL_MESSAGE_ID
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    private fun hideVanishedPlayersOnJoin(event: PlayerJoinEvent) {
        for (user in SayanVanishBukkitAPI.getInstance().getUsers { it.isVanished && it.player() != null }) {
            user.hideUser(event.player)
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    private fun setJoinMessage(event: PlayerJoinEvent) {
        if (event.joinMessage != REMOVAL_MESSAGE_ID) {
            if (settings.vanish.joinLeaveMessage.getFromJoinEvent) {
                generalJoinMessage = event.joinMessage
            }
        } else {
            event.joinMessage = null
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    private fun setQuitMessage(event: PlayerQuitEvent) {
        if (event.quitMessage != REMOVAL_MESSAGE_ID) {
            if (settings.vanish.joinLeaveMessage.getFromQuitEvent) {
                generalQuitMessage = event.quitMessage
            }
        } else {
            event.quitMessage = null
        }
    }

}