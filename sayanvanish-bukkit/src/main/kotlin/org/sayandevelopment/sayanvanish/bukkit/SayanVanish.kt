package org.sayandevelopment.sayanvanish.bukkit

import org.sayandevelopment.sayanvanish.api.Platform
import org.sayandevelopment.stickynote.bukkit.StickyNotePlugin
import org.sayandevelopment.stickynote.bukkit.pluginDirectory

open class SayanVanish : StickyNotePlugin() {

    override fun onEnable() {
        Platform.setPlatform(Platform("bukkit", logger, pluginDirectory))

        VanishManager()
    }

}