package org.sayandevelopment.sayanvanish.bukkit

import com.zaxxer.hikari.pool.HikariPool
import org.sayandevelopment.sayanvanish.api.Platform
import org.sayandevelopment.sayanvanish.api.User
import org.sayandevelopment.sayanvanish.api.database.DatabaseConfig
import org.sayandevelopment.sayanvanish.api.database.DatabaseExecutor
import org.sayandevelopment.sayanvanish.api.database.DatabaseMethod
import org.sayandevelopment.stickynote.bukkit.StickyNotePlugin
import org.sayandevelopment.stickynote.core.database.sqlite.SQLiteDatabase
import java.io.File
import java.util.logging.Logger

open class SayanVanish : StickyNotePlugin() {

    val database = DatabaseExecutor<User>(
        SQLiteDatabase(File("C:\\Server\\Paper", "storage.db"), Logger.getGlobal()),
        DatabaseConfig(DatabaseMethod.SQLITE)
    )

    override fun onEnable() {
        Platform.setPlatformId("bukkit")
    }
}

fun main() {
    try {
        Class.forName("org.spongepowered.configurate.NodePath")
    } catch (e: Exception) {
        e.printStackTrace()
    }
}