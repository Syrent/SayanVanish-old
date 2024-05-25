package org.sayandev.sayanvanish.bukkit.hook

import org.sayandev.sayanvanish.bukkit.config.settings
import org.sayandev.sayanvanish.bukkit.hook.hooks.HookPlaceholderAPI
import org.sayandev.stickynote.bukkit.hasPlugin

object Hooks {
    init {
        if (hasPlugin("PlaceholderAPI") && settings.vanish.hooks.placeholderAPI.enabled) {
            HookPlaceholderAPI().register()
        }
    }
}