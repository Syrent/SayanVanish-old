package org.sayandev.sayanvanish.bukkit.feature.features

import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityPickupItemEvent
import org.sayandev.sayanvanish.api.feature.RegisteredFeature
import org.sayandev.sayanvanish.bukkit.api.SayanVanishBukkitAPI.Companion.user
import org.sayandev.sayanvanish.bukkit.feature.ListenedFeature
import org.sayandev.stickynote.bukkit.event.registerListener
import org.sayandev.stickynote.lib.spongepowered.configurate.objectmapping.ConfigSerializable

//@ConfigSerializable
//@RegisteredFeature
class FeaturePreventPickup : ListenedFeature("prevent_pickup") {
    override fun enable() {
        registerListener<EntityPickupItemEvent> { event ->
            val user = (event.entity as? Player)?.user() ?: return@registerListener
            if (user.isVanished && isEnabled) {
                event.isCancelled = true
            }
        }
        super.enable()
    }

    override fun disable() {
        super.disable()
    }
}