package org.sayandev.sayanvanish.bukkit

import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.sayandev.sayanvanish.api.Permission
import org.sayandev.sayanvanish.api.VanishOptions
import org.sayandev.sayanvanish.bukkit.api.SayanVanishBukkitAPI
import org.sayandev.sayanvanish.bukkit.api.SayanVanishBukkitAPI.Companion.getOrCreateUser
import org.sayandev.sayanvanish.bukkit.api.SayanVanishBukkitAPI.Companion.user
import org.sayandev.stickynote.bukkit.registerListener

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
        val user = player.user()
        val vanishJoinOptions = VanishOptions.Builder().sendMessage(false).build()

        if (user == null) {
            val tempUser = player.getOrCreateUser()
            if (tempUser.hasPermission(Permission.VANISH_ON_JOIN)) {

                tempUser.isOnline = true
                tempUser.isVanished = true
                tempUser.vanish(vanishJoinOptions)
                tempUser.save()
            }
            return
        }

        user.isOnline = true
        if (user.hasPermission(Permission.VANISH_ON_JOIN) || user.isVanished) {
            user.isVanished = true
            user.vanish(vanishJoinOptions)
        }
        user.save()

        if (player.user()?.isVanished == true) {
            generalJoinMessage = event.joinMessage
            event.joinMessage = REMOVAL_MESSAGE_ID
        }
        return
    }

    @EventHandler(priority = EventPriority.LOWEST)
    private fun updateUserOnQuit(event: PlayerQuitEvent) {
        val player = event.player
        val user = player.user() ?: return
        user.isOnline = false
        user.save()

        if (user.isVanished) {
            generalQuitMessage = event.quitMessage
            event.quitMessage = REMOVAL_MESSAGE_ID
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    private fun hideVanishedPlayersOnJoin(event: PlayerJoinEvent) {
        for (user in SayanVanishBukkitAPI.getInstance().getUsers { it.isVanished && it.player != null }) {
            user.hideUser(event.player)
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    private fun setJoinMessage(event: PlayerJoinEvent) {
        if (event.joinMessage != REMOVAL_MESSAGE_ID) {
            generalJoinMessage = event.joinMessage
        } else {
            event.joinMessage = null
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    private fun setQuitMessage(event: PlayerQuitEvent) {
        if (event.quitMessage != REMOVAL_MESSAGE_ID) {
            generalQuitMessage = event.quitMessage
        } else {
            event.quitMessage = null
        }
    }

}