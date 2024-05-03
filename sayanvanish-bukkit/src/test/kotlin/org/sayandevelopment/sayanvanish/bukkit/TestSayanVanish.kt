package org.sayandevelopment.sayanvanish.bukkit

import be.seeseemelk.mockbukkit.MockBukkit
import be.seeseemelk.mockbukkit.ServerMock
import org.bukkit.event.player.PlayerJoinEvent
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.sayandevelopment.stickynote.core.database.sqlite.SQLiteDatabase
import org.sayandevelopment.sayanvanish.api.Permission
import org.sayandevelopment.sayanvanish.api.database.DatabaseConfig
import org.sayandevelopment.sayanvanish.api.database.DatabaseExecutor
import org.sayandevelopment.sayanvanish.api.database.DatabaseMethod
import org.sayandevelopment.sayanvanish.bukkit.api.BukkitUser
import org.sayandevelopment.sayanvanish.bukkit.api.SayanVanishBukkitAPI
import java.io.File
import java.util.*
import java.util.logging.Logger
import kotlin.test.assertTrue

class TestSayanVanish {

    companion object {
        val executor = SayanVanishBukkitAPI.getInstance().databaseExecutor

        @JvmStatic
        lateinit var plugin: SayanVanish

        @JvmStatic
        lateinit var server: ServerMock

        @JvmStatic
        @BeforeAll
        fun load() {
            executor.connect()
            executor.initialize()
            server = MockBukkit.mock()
            plugin = MockBukkit.load(SayanVanish::class.java)
        }

        @JvmStatic
        @AfterAll
        fun unload() {
            executor.disconnect()
            MockBukkit.unmock()
        }
    }

    @Test
    fun addVanishOnJoinPlayer() {
        val player = server.addPlayer("TestUser")
        server.pluginManager.assertEventFired(PlayerJoinEvent::class.java)
        val bukkitUser = executor.getUser(player.uniqueId, false) ?: return
        if (bukkitUser.isVanished) {
            assertTrue(bukkitUser.isVanished)
        } else {
            if (bukkitUser.hasPermission(Permission.VANISH_ON_JOIN.permission())) {
                bukkitUser.isVanished = true
                executor.addUser(bukkitUser)
                assertTrue(executor.getUser(bukkitUser.uniqueId, false)?.isVanished == true)
            }
        }
    }
}