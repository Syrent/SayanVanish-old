package org.sayandev.sayanvanish.bukkit

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.sayandev.sayanvanish.api.Permission
import org.sayandev.sayanvanish.bukkit.api.SayanVanishBukkitAPI.Companion.getOrCreateUser
import org.sayandev.sayanvanish.bukkit.api.SayanVanishBukkitAPI.Companion.user
import org.sayandev.stickynote.bukkit.registerListener
import org.sayandev.stickynote.bukkit.warn

object VanishManager : Listener {

    init {
        registerListener(this)
    }

    @EventHandler
    private fun onPlayerJoin(event: PlayerJoinEvent) {
        val player = event.player
        val user = player.user()

        if (user == null) {
            val tempUser = player.getOrCreateUser()
            if (tempUser.hasPermission(Permission.VANISH_ON_JOIN)) {
                tempUser.isOnline = true
                tempUser.isVanished = true
                tempUser.vanish()
                tempUser.save()
            }
            return
        }

        user.isOnline = true
        if (user.hasPermission(Permission.VANISH_ON_JOIN) || user.isVanished) {
            user.isVanished = true
            user.vanish()
        }
        user.save()
        return
    }

    @EventHandler
    private fun onPlayerQuit(event: PlayerQuitEvent) {
        val player = event.player
        val user = player.user() ?: return
        user.isOnline = false
        user.save()
    }

}