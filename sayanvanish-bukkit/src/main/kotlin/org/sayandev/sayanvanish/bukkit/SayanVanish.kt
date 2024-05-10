package org.sayandev.sayanvanish.bukkit

import org.sayandev.sayanvanish.api.Platform
import org.sayandev.sayanvanish.api.database.DatabaseConfig
import org.sayandev.sayanvanish.api.database.databaseConfig
import org.sayandev.sayanvanish.bukkit.api.SayanVanishBukkitAPI
import org.sayandev.sayanvanish.bukkit.command.SayanVanishCommand
import org.sayandev.sayanvanish.bukkit.config.LanguageConfig
import org.sayandev.sayanvanish.bukkit.config.SettingsConfig
import org.sayandev.stickynote.bukkit.StickyNotePlugin
import org.sayandev.stickynote.bukkit.pluginDirectory
import org.sayandev.stickynote.bukkit.warn

open class SayanVanish : StickyNotePlugin() {

    override fun onEnable() {
        Platform.setPlatform(Platform("bukkit", logger, pluginDirectory))

        SayanVanishBukkitAPI(databaseConfig.useCacheWhenAvailable)

        SettingsConfig
        LanguageConfig

        VanishManager

        SayanVanishCommand()
    }

}