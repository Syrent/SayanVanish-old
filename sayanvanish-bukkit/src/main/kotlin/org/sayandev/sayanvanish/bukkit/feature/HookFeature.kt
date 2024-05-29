package org.sayandev.sayanvanish.bukkit.feature

import org.sayandev.stickynote.bukkit.hasPlugin
import org.sayandev.stickynote.bukkit.registerListener
import org.sayandev.stickynote.bukkit.unregisterListener

abstract class HookFeature(val plugin: String) : ListenedFeature() {

    override fun enable() {
        if (!condition) return
        registerListener(this)
        super.enable()
    }

    override fun disable() {
        unregisterListener(this)
        super.disable()
    }

    fun hasPlugin(): Boolean {
        return hasPlugin(plugin)
    }

    override fun isActive(): Boolean {
        return super.isActive() && hasPlugin()
    }


}