package org.sayandev.sayanvanish.bukkit

import com.alessiodp.libby.BukkitLibraryManager
import com.alessiodp.libby.Library
import org.bukkit.plugin.java.JavaPlugin
import org.sayandev.sayanvanish.api.Platform
import org.sayandev.sayanvanish.api.database.databaseConfig
import org.sayandev.sayanvanish.bukkit.api.SayanVanishBukkitAPI
import org.sayandev.sayanvanish.bukkit.command.SayanVanishCommand
import org.sayandev.sayanvanish.bukkit.config.LanguageConfig
import org.sayandev.sayanvanish.bukkit.config.SettingsConfig
import org.sayandev.sayanvanish.bukkit.hook.Hooks
import org.sayandev.stickynote.bukkit.StickyNote
import org.sayandev.stickynote.bukkit.WrappedStickyNotePlugin
import org.sayandev.stickynote.bukkit.pluginDirectory
import org.sayandev.stickynote.bukkit.runAsync

open class SayanVanish : JavaPlugin() {

    override fun onEnable() {
        downloadLibraries()
        WrappedStickyNotePlugin(this)

        Platform.setAndRegister(Platform("bukkit", logger, pluginDirectory))

        SayanVanishBukkitAPI(databaseConfig.useCacheWhenAvailable)

        SettingsConfig
        LanguageConfig

        VanishManager
        Hooks

        SayanVanishCommand()

        runAsync({
            SayanVanishBukkitAPI.getInstance().databaseExecutor.updateBasicCache()
        }, 100, 100)
    }

    override fun onDisable() {
        StickyNote.shutdown()
    }

    private fun downloadLibraries() {
        logger.info("Trying to download required libraries, make sure your machine is connected to internet.")
        val libraryManager = BukkitLibraryManager(this)
        libraryManager.addMavenLocal()
        libraryManager.addRepository("https://repo.sayandev.org/snapshots")
        libraryManager.loadLibrary(
            Library.builder()
                .groupId("org{}sayandev")
                .artifactId("stickynote-core")
                .version("1.0.26")
                .relocate("org{}sayandev{}stickynote", "org{}sayandev{}sayanvanish{}lib{}stickynote")
                .build()
        )
        libraryManager.loadLibrary(
            Library.builder()
                .groupId("org{}sayandev")
                .artifactId("stickynote-bukkit")
                .version("1.0.26")
                .relocate("org{}sayandev{}stickynote", "org{}sayandev{}sayanvanish{}lib{}stickynote")
                .build()
        )
    }


}