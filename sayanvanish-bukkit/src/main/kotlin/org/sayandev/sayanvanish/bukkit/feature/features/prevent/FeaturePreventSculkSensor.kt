package org.sayandev.sayanvanish.bukkit.feature.features.prevent

import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.block.BlockReceiveGameEvent
import org.sayandev.sayanvanish.api.feature.RegisteredFeature
import org.sayandev.sayanvanish.bukkit.api.SayanVanishBukkitAPI.Companion.user
import org.sayandev.sayanvanish.bukkit.feature.ListenedFeature
import org.sayandev.stickynote.lib.xseries.ReflectionUtils

@RegisteredFeature
class FeaturePreventSculkSensor(
    override val id: String = "prevent_sculk_sensor",
    override var enabled: Boolean = true
) : ListenedFeature() {

    @Transient
    override var condition: Boolean = ReflectionUtils.supports(19)

    @EventHandler
    private fun onBlockReceive(event: BlockReceiveGameEvent) {
        if (!isActive()) return
        val user = (event.entity as? Player)?.user() ?: return
        if (user.isVanished) {
            event.isCancelled = true
        }
    }

}