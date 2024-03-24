package org.sayandevelopment.sayanvanish.bukkit

import org.sayandevelopment.sayanvanish.api.Platform
import org.bukkit.plugin.java.JavaPlugin
import org.sayandevelopment.sayanvanish.api.SayanVanishAPI
import org.sayandevelopment.sayanvanish.bukkit.api.SayanVanishBukkitAPI
import java.util.UUID

class SayanVanish : JavaPlugin() {

    override fun onEnable() {
        Platform.setPlatformId("bukkit")
    }
}