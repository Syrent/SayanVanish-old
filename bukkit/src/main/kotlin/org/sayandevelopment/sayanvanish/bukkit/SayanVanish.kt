package org.sayandevelopment.sayanvanish.bukkit

import org.sayandevelopment.bukkit.StickyNotePlugin
import org.sayandevelopment.core.database.sqlite.SQLiteDatabase
import org.sayandevelopment.sayanvanish.api.Platform
import org.sayandevelopment.sayanvanish.api.User
import org.sayandevelopment.sayanvanish.api.database.DatabaseConfig
import org.sayandevelopment.sayanvanish.api.database.DatabaseExecutor
import org.sayandevelopment.sayanvanish.api.database.DatabaseMethod
import org.sayandevelopment.sayanvanish.bukkit.api.BukkitUser
import java.io.File
import java.util.*
import java.util.logging.Logger

class SayanVanish : StickyNotePlugin() {

    override fun onEnable() {
        Platform.setPlatformId("bukkit")

        val database = DatabaseExecutor<User>(
            SQLiteDatabase(File("C:\\Server\\Paper", "storage.db"), Logger.getGlobal()),
            DatabaseConfig(DatabaseMethod.SQLITE)
        )

        val user = BukkitUser(UUID.randomUUID(), "SyrentTestBukkit")
        database.connect()
        database.initialize()
        database.addUser(user)
        database.getUser(user.uniqueId)
        user.isVanished = true
        database.updateUser(user)
//        database.disconnect()
    }
}