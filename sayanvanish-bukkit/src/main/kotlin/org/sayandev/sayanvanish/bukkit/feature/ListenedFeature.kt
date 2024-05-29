package org.sayandev.sayanvanish.bukkit.feature

import org.bukkit.event.Listener
import org.sayandev.sayanvanish.api.feature.Feature
import org.sayandev.sayanvanish.api.feature.category.FeatureCategories
import org.sayandev.stickynote.bukkit.registerListener
import org.sayandev.stickynote.bukkit.unregisterListener
import org.sayandev.stickynote.lib.spongepowered.configurate.objectmapping.ConfigSerializable

@ConfigSerializable
abstract class ListenedFeature(
    id: String,
    enabled: Boolean = true,
    category: FeatureCategories = FeatureCategories.DEFAULT
) : Feature(id, enabled, category), Listener {

    override fun enable() {
        if (!condition) return
        registerListener(this)
        super.enable()
    }

    override fun disable() {
        unregisterListener(this)
        super.disable()
    }

}