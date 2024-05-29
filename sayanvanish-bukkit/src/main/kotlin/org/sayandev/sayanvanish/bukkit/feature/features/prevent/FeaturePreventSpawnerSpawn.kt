package org.sayandev.sayanvanish.bukkit.feature.features.prevent

import com.destroystokyo.paper.event.entity.PreSpawnerSpawnEvent
import org.bukkit.GameMode
import org.bukkit.event.EventHandler
import org.sayandev.sayanvanish.api.feature.RegisteredFeature
import org.sayandev.sayanvanish.bukkit.api.SayanVanishBukkitAPI.Companion.user
import org.sayandev.sayanvanish.bukkit.feature.ListenedFeature
import org.sayandev.stickynote.bukkit.StickyNote
import org.sayandev.stickynote.bukkit.onlinePlayers
import org.sayandev.stickynote.lib.xseries.ReflectionUtils
import kotlin.collections.all
import kotlin.collections.filter

@RegisteredFeature
class FeaturePreventSpawnerSpawn(
    override val id: String = "prevent_spawner_spawn",
    override var enabled: Boolean = true
) : ListenedFeature() {

    @Transient
    override var condition: Boolean = StickyNote.isPaper() && ReflectionUtils.supports(16)

    @EventHandler
    private fun onPreSpawn(event: PreSpawnerSpawnEvent) {
        if (!isActive()) return
        val nearPlayers = onlinePlayers
            .filter { player -> player.world == event.spawnerLocation.world && player.location.distance(event.spawnerLocation) <= 256 && player.gameMode != GameMode.SPECTATOR }
        val allIsVanished = nearPlayers.all { it.user()?.isVanished == true }
        if (allIsVanished) {
            event.isCancelled = true
        }
    }

}