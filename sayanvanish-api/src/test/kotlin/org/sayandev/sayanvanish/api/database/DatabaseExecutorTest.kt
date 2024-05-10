package org.sayandev.sayanvanish.api.database

import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.sayandev.sayanvanish.api.User
import org.sayandev.sayanvanish.api.VanishOptions
import java.util.*
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class DatabaseExecutorTest {

    companion object {
        val executor = DatabaseExecutor<User>(
            DatabaseConfig(DatabaseMethod.MYSQL, "localhost", 3306, "sayanvanish", "root", "admin")
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
    }

    @Test
    @Order(4)
    fun initialize() {
        assertDoesNotThrow { executor.initialize() }
    }

    @Test
    @Order(5)
    fun addUser() {
        assertDoesNotThrow {
            executor.addUser(
                object : User {
                    override val uniqueId = randomUUID
                    override var username = "SyrentTest"
                    override var currentOptions: VanishOptions = VanishOptions.defaultOptions()
                    override var isVanished = false
                    override var isOnline = false
                    override var vanishLevel = 1
                }
            )
        }
    }

    @Test
    @Order(6)
    fun getUser() {
        assertDoesNotThrow { executor.getUser(randomUUID, false) }
    }

    @Test
    @Order(7)
    fun getUsers() {
        assertDoesNotThrow { executor.getUsers(false) }
    }

    @Test
    @Order(8)
    fun updateUser() {
        assertDoesNotThrow {
            val user = executor.getUser(randomUUID, false)
            assertNotNull(user)

            assertEquals(randomUUID, user.uniqueId)
            assertEquals("SyrentTest", user.username)
            assertEquals(false, user.isVanished)
            assertEquals(false, user.isOnline)
            assertEquals(1, user.vanishLevel)
            user.username = "SyrentTest2"
            executor.updateUser(user)
        }
    }

    @Test
    @Order(8)
    fun purgeCache() {
        assertDoesNotThrow { executor.purgeCache() }
    }

    @Test
    @Order(9)
    fun purge() {
        assertDoesNotThrow { executor.purge() }
    }

    @Test
    @Order(10)
    fun getDatabase() {
        assertNotNull(executor.database)
    }

    @Test
    @Order(11)
    fun disconnect() {
        assertDoesNotThrow { executor.disconnect() }
    }

}