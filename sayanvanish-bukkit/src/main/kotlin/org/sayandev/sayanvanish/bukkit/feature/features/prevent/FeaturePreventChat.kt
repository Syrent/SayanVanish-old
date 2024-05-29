package org.sayandev.sayanvanish.bukkit.feature.features.prevent

import org.bukkit.event.EventHandler
import org.bukkit.event.player.AsyncPlayerChatEvent
import org.sayandev.sayanvanish.api.feature.RegisteredFeature
import org.sayandev.sayanvanish.bukkit.api.SayanVanishBukkitAPI.Companion.user
import org.sayandev.sayanvanish.bukkit.config.language
import org.sayandev.sayanvanish.bukkit.feature.ListenedFeature
import org.sayandev.stickynote.bukkit.utils.AdventureUtils.component

@RegisteredFeature
class FeaturePreventChat(
    override val id: String = "prevent_chat",
    override var enabled: Boolean = true
) : ListenedFeature() {

    @EventHandler
    private fun onPlayerChat(event: AsyncPlayerChatEvent) {
        if (!isActive()) return
        val user = event.player.user() ?: return
        if (!user.isVanished) return
        val message = event.message
        if (message.startsWith("!")) {
            event.message = message.removePrefix("!")
        } else {
            user.sendMessage(language.vanish.cantChatWhileVanished.component())
            event.isCancelled = true
        }
    }

}