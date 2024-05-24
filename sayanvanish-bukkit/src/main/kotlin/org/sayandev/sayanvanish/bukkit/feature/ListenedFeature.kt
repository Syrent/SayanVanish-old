package org.sayandev.sayanvanish.bukkit.feature

import org.bukkit.event.Listener
import org.sayandev.sayanvanish.api.feature.Feature
import org.sayandev.stickynote.bukkit.registerListener
import org.sayandev.stickynote.bukkit.unregisterListener

abstract class ListenedFeature(id: String) : Feature(id), Listener {

    override fun enable() {
        registerListener(this)
        super.enable()
    }

    override fun disable() {
        unregisterListener(this)
        super.disable()
    }

}