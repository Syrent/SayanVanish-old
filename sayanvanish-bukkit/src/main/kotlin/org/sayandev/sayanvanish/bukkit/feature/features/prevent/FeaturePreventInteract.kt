package org.sayandev.sayanvanish.bukkit.feature.features.prevent

import org.bukkit.event.EventHandler
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import org.sayandev.sayanvanish.api.feature.RegisteredFeature
import org.sayandev.sayanvanish.bukkit.api.SayanVanishBukkitAPI.Companion.user
import org.sayandev.sayanvanish.bukkit.feature.ListenedFeature
import kotlin.text.contains

@RegisteredFeature
class FeaturePreventInteract(
    override val id: String = "prevent_interact_event",
    override var enabled: Boolean = true,
    val pressurePlateTrigger: Boolean = true,
    val dripLeaf: Boolean = true,
    val interact: Boolean = true,
) : ListenedFeature() {

    @EventHandler
    private fun onInteract(event: PlayerInteractEvent) {
        if (!isActive()) return
        val user = event.player.user() ?: return
        if (user.isVanished) {
            val isPressurePlate = pressurePlateTrigger && event.action == Action.PHYSICAL && event.clickedBlock?.type?.name?.contains("PLATE") == true
            val isDripLeaf = dripLeaf && event.action == Action.PHYSICAL && event.clickedBlock?.type?.name?.equals("BIG_DRIPLEAF") == true
            if (interact || isPressurePlate || isDripLeaf) {
                event.isCancelled = true
            }
        }
    }

}