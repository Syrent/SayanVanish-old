package org.sayandev.sayanvanish.bukkit.hook

import org.sayandev.sayanvanish.bukkit.hook.hooks.HookPlaceholderAPI
import org.sayandev.stickynote.bukkit.hasPlugin

object Hooks {
    init {
        if (hasPlugin("PlaceholderAPI")) {
            HookPlaceholderAPI().register()
        }
    }
}