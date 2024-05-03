package org.sayandev.sayanvanish.api.database

import net.kyori.adventure.text.Component
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.sayandev.sayanvanish.api.User
import org.sayandev.stickynote.core.database.sqlite.SQLiteDatabase
import java.io.File
import java.util.*
import java.util.logging.Logger
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class SQLiteDatabaseExecutorTest {

    companion object {
        val executor = DatabaseExecutor<User>(
            SQLiteDatabase(File.createTempFile("/sayanvanish", "storage.db"), Logger.getGlobal()),
            DatabaseConfig(File.createTempFile("sayanvanish_temp_database", ".yml"), DatabaseMethod.SQLITE)
        )
        val randomUUID = UUID.randomUUID()
    }

    init {
        connect()
        initialize()
    }

    @Test
    @Order(1)
    fun generateDatabaseConfiguration() {
        assertDoesNotThrow { DatabaseConfig() }
    }

    @Test
    @Order(2)
    fun getType() {
        assertEquals(DatabaseMethod.SQLITE, executor.type)
    }

    @Test
    @Order(3)
    fun connect() {
        assertDoesNotThrow { executor.connect() }
        executor.disconnect()
    }

    @Test
    @Order(4)
    fun initialize() {
        executor.connect()
        assertDoesNotThrow { executor.initialize() }
        executor.disconnect()
    }

    @Test
    @Order(5)
    fun addUser() {
        executor.connect()
        executor.initialize()
        assertDoesNotThrow {
            executor.addUser(
                object : User {
                    override val uniqueId: UUID
                        get() = randomUUID
                    override val username: String
                        get() = "SyrentTest"
                    override var isVanished: Boolean = false
                    override var isOnline: Boolean = false
                    override var vanishLevel: Int = 1
                }
            )
        }
        executor.disconnect()
    }

    @Test
    @Order(6)
    fun getUser() {
        executor.connect()
        executor.initialize()
        assertDoesNotThrow { executor.getUser(randomUUID, false) }
        executor.disconnect()
    }

    @Test
    @Order(7)
    fun getUsers() {
        executor.connect()
        executor.initialize()
        assertDoesNotThrow { executor.getUsers(false) }
        executor.disconnect()
    }

    @Test
    @Order(8)
    fun updateUser() {
        executor.connect()
        executor.initialize()
        val user = executor.getUser(randomUUID, false)
        assertNotNull(user)

        assertEquals(randomUUID, user.uniqueId)
        assertEquals("SyrentTest", user.username)
        assertEquals(false, user.isVanished)
        assertEquals(false, user.isOnline)
        assertEquals(1, user.vanishLevel)
        executor.disconnect()

        executor.connect()
        executor.initialize()
        assertDoesNotThrow {
            executor.updateUser(
                object : User {
                    override val uniqueId: UUID
                        get() = randomUUID
                    override val username: String
                        get() = "SyrentTest2"
                    override var isVanished: Boolean = true
                    override var isOnline: Boolean = true
                    override var vanishLevel: Int = 3
                }
            )
        }
        executor.disconnect()

        executor.connect()
        executor.initialize()
        val updatedUser = executor.getUser(user.uniqueId, false)
        assertNotNull(updatedUser)
        assertEquals(randomUUID, updatedUser.uniqueId)
        assertEquals("SyrentTest2", updatedUser.username)
        assertEquals(true, updatedUser.isVanished)
        assertEquals(true, updatedUser.isOnline)
        assertEquals(3, updatedUser.vanishLevel)
        executor.disconnect()
    }

    @Test
    @Order(8)
    fun purgeCache() {
        assertDoesNotThrow { executor.purgeCache() }
    }

    @Test
    @Order(9)
    fun purge() {
        executor.connect()
        executor.initialize()
        assertDoesNotThrow { executor.purge() }
        executor.disconnect()
    }

    @Test
    @Order(10)
    fun getDatabase() {
        executor.connect()
        executor.initialize()
        assertNotNull(executor.database)
        executor.disconnect()
    }

    @Test
    @Order(11)
    fun disconnect() {
        executor.connect()
        executor.initialize()
        assertDoesNotThrow { executor.disconnect() }
    }

}