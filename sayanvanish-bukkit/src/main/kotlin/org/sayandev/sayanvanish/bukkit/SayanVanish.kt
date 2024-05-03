package org.sayandev.sayanvanish.bukkit

import org.sayandev.sayanvanish.api.Platform
import org.sayandev.stickynote.bukkit.StickyNotePlugin
import org.sayandev.stickynote.bukkit.pluginDirectory

open class SayanVanish : StickyNotePlugin() {

    override fun onEnable() {
        Platform.setPlatform(Platform("bukkit", logger, pluginDirectory))

        VanishManager
    }

}