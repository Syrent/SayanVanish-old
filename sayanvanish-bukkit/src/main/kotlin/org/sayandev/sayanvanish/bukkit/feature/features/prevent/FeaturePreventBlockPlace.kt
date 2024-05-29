package org.sayandev.sayanvanish.bukkit.feature.features.prevent

import org.bukkit.event.EventHandler
import org.bukkit.event.block.BlockPlaceEvent
import org.sayandev.sayanvanish.api.feature.RegisteredFeature
import org.sayandev.sayanvanish.bukkit.api.SayanVanishBukkitAPI.Companion.user
import org.sayandev.sayanvanish.bukkit.feature.ListenedFeature

@RegisteredFeature
class FeaturePreventBlockPlace(
    override val id: String = "prevent_block_place",
    override var enabled: Boolean = true
) : ListenedFeature() {

    @EventHandler
    private fun onBlockPlace(event: BlockPlaceEvent) {
        if (!isActive()) return
        val user = event.player.user() ?: return
        if (user.isVanished) {
            event.isCancelled = true
        }
    }

}