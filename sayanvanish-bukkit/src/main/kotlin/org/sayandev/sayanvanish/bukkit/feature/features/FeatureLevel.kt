package org.sayandev.sayanvanish.bukkit.feature.features

import org.bukkit.event.EventHandler
import org.sayandev.sayanvanish.api.feature.RegisteredFeature
import org.sayandev.sayanvanish.bukkit.api.event.BukkitUserVanishEvent
import org.sayandev.sayanvanish.bukkit.feature.ListenedFeature
import org.sayandev.stickynote.bukkit.onlinePlayers
import org.sayandev.sayanvanish.bukkit.api.SayanVanishBukkitAPI.Companion.user
import org.sayandev.stickynote.bukkit.plugin
import org.sayandev.stickynote.lib.spongepowered.configurate.objectmapping.ConfigSerializable

@RegisteredFeature
@ConfigSerializable
class FeatureLevel: ListenedFeature("level") {

    @EventHandler
    private fun onVanish(event: BukkitUserVanishEvent) {
        if (!isActive()) return
        val user = event.user
        for (onlinePlayer in onlinePlayers) {
            val playerVanishLevel = onlinePlayer.user()?.vanishLevel ?: 0
            if (playerVanishLevel < user.vanishLevel) {
                user.player()?.let { onlinePlayer.hidePlayer(plugin, it) }
            }
        }
    }

}