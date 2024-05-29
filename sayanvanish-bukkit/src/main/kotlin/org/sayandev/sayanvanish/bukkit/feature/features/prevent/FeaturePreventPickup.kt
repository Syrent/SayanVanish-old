package org.sayandev.sayanvanish.bukkit.feature.features.prevent

import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.entity.EntityPickupItemEvent
import org.sayandev.sayanvanish.api.feature.RegisteredFeature
import org.sayandev.sayanvanish.bukkit.api.SayanVanishBukkitAPI.Companion.user
import org.sayandev.sayanvanish.bukkit.feature.ListenedFeature

@RegisteredFeature
class FeaturePreventPickup(
    override val id: String = "prevent_pickup",
    override var enabled: Boolean = true
) : ListenedFeature() {

    @EventHandler
    private fun onPickupItem(event: EntityPickupItemEvent) {
        if (!isActive()) return
        val user = (event.entity as? Player)?.user() ?: return
        if (user.isVanished) {
            event.isCancelled = true
        }
    }

}