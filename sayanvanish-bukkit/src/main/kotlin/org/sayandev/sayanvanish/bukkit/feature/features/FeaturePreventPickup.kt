package org.sayandev.sayanvanish.bukkit.feature.features

import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityPickupItemEvent
import org.sayandev.sayanvanish.api.feature.RegisteredFeature
import org.sayandev.sayanvanish.bukkit.api.SayanVanishBukkitAPI.Companion.user
import org.sayandev.sayanvanish.bukkit.feature.ListenedFeature
import org.sayandev.stickynote.bukkit.event.registerListener
import org.sayandev.stickynote.lib.spongepowered.configurate.ConfigurationNode

@RegisteredFeature
class FeaturePreventPickup(
    val distance: Int = 3,
    val sosis: String = "sosmast"
) : ListenedFeature("prevent_pickup") {

    override var isEnabled = true

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

    override fun serialize(node: ConfigurationNode) {
        node.node("id").set(id)
        node.node("enabled").set(isEnabled)
        node.node("distance").set(distance)
        node.node("sosis").set(sosis)
    }

    /*override fun deserialize(type: Type, node: ConfigurationNode): FeaturePreventPickup {
        return Features.features().first { it.id == node.node("id").string } as FeaturePreventPickup
    }

    override fun serialize(type: Type, obj: FeaturePreventPickup?, node: ConfigurationNode) {
        node.node("id").set(obj!!.id)
        node.node("enabled").set(obj.isEnabled)
        node.node("distance").set(obj.distance)
    }*/
}