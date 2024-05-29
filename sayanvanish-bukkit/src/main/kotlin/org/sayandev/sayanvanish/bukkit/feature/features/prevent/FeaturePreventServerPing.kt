package org.sayandev.sayanvanish.bukkit.feature.features.prevent

import com.destroystokyo.paper.event.server.PaperServerListPingEvent
import org.bukkit.event.EventHandler
import org.sayandev.sayanvanish.api.feature.RegisteredFeature
import org.sayandev.sayanvanish.bukkit.api.SayanVanishBukkitAPI
import org.sayandev.sayanvanish.bukkit.feature.ListenedFeature
import org.sayandev.stickynote.bukkit.StickyNote
import org.sayandev.stickynote.lib.xseries.ReflectionUtils
import kotlin.collections.contains
import kotlin.collections.count
import kotlin.collections.map

@RegisteredFeature
class FeaturePreventServerPing(
    override val id: String = "prevent_server_ping",
    override var enabled: Boolean = true
) : ListenedFeature() {

    @Transient
    override var condition: Boolean = StickyNote.isPaper() && ReflectionUtils.supports(16)

    @EventHandler
    private fun onPreSpawn(event: PaperServerListPingEvent) {
        if (!isActive()) return
        val vanishedPlayers = SayanVanishBukkitAPI.getInstance().getUsers { it.player() != null }
        event.numPlayers -= vanishedPlayers.count()
        event.playerSample.removeIf { profile -> vanishedPlayers.map { vanishedPlayer -> vanishedPlayer.uniqueId }.contains(profile.id) }
    }

}