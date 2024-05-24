/*
package org.sayandev.sayanvanish.bukkit.feature.features

import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityPickupItemEvent
import org.sayandev.sayanvanish.api.feature.Features
import org.sayandev.sayanvanish.api.feature.RegisteredFeature
import org.sayandev.sayanvanish.bukkit.api.SayanVanishBukkitAPI.Companion.user
import org.sayandev.sayanvanish.bukkit.feature.ListenedFeature
import org.sayandev.stickynote.bukkit.event.registerListener
import org.sayandev.stickynote.lib.spongepowered.configurate.ConfigurationNode
import org.sayandev.stickynote.lib.spongepowered.configurate.serialize.TypeSerializer
import java.lang.reflect.Type

//@RegisteredFeature
class FeatureTest(
    val moz: String = "sosis"
) : ListenedFeature<FeatureTest>("test") {

    override var isEnabled = false

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

    override fun deserialize(type: Type, node: ConfigurationNode): FeatureTest {
        return Features.features().first { it.id == node.node("id").string } as FeatureTest
    }

    override fun serialize(type: Type, obj: FeatureTest?, node: ConfigurationNode) {
        node.node("id").set(obj!!.id)
        node.node("enabled").set(obj.isEnabled)
        node.node("moz").set(obj.moz)
    }
}*/
