package org.sayandev.sayanvanish.bukkit.feature.features.prevent

import org.bukkit.event.EventHandler
import org.bukkit.event.raid.RaidTriggerEvent
import org.sayandev.sayanvanish.api.feature.RegisteredFeature
import org.sayandev.sayanvanish.bukkit.api.SayanVanishBukkitAPI.Companion.user
import org.sayandev.sayanvanish.bukkit.feature.ListenedFeature
import org.sayandev.stickynote.lib.xseries.ReflectionUtils

@RegisteredFeature
class FeaturePreventRaidTrigger(
    override val id: String = "prevent_raid_trigger",
    override var enabled: Boolean = true
) : ListenedFeature() {

    @Transient
    override var condition: Boolean = ReflectionUtils.supports(15)

    @EventHandler
    private fun onRaidTrigger(event: RaidTriggerEvent) {
        if (!isActive()) return
        val user = event.player.user() ?: return
        if (user.isVanished) {
            event.isCancelled = true
        }
    }

}