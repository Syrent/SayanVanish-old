package org.sayandevelopment.sayanvanish.bukkit

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.sayandevelopment.sayanvanish.bukkit.api.BukkitUser
import org.sayandevelopment.sayanvanish.bukkit.api.SayanVanishBukkitAPI
import org.sayandevelopment.sayanvanish.bukkit.api.SayanVanishBukkitAPI.Companion.user
import org.sayandevelopment.stickynote.bukkit.registerListener

class VanishManager : Listener {

    init {
        registerListener(this)
    }

    @EventHandler
    private fun onPlayerJoin(event: PlayerJoinEvent) {
        val player = event.player
        SayanVanishBukkitAPI.getInstance().addUser(BukkitUser(player.uniqueId, player.name))
    }

    @EventHandler
    private fun onPlayerJoin(event: PlayerQuitEvent) {
        val player = event.player
        val user = player.user() ?: return
        user.save()
    }
}