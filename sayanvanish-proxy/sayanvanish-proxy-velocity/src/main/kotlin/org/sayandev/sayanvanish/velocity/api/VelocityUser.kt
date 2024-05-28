package org.sayandev.sayanvanish.velocity.api

import com.velocitypowered.api.proxy.Player
import net.kyori.adventure.text.Component
import org.sayandev.sayanvanish.api.User
import org.sayandev.sayanvanish.api.VanishOptions
import org.sayandev.sayanvanish.velocity.config.settings
import org.sayandev.stickynote.velocity.StickyNote
import org.sayandev.stickynote.velocity.utils.AdventureUtils.component
import java.util.UUID


open class VelocityUser(
    override val uniqueId: UUID,
    override var username: String
) : User {

    override var serverId = settings.general.serverId
    override var currentOptions = VanishOptions.defaultOptions()
    override var isVanished = false
    override var isOnline: Boolean = false
    override var vanishLevel: Int = 1

    fun stateText(isVanished: Boolean = this.isVanished) = if (isVanished) "<green>ON</green>" else "<red>OFF</red>"

    fun player(): Player? = StickyNote.getPlayer(uniqueId)

    /** TODO: Implement both Redis and PluginMessage way for users
     * PluginMessage will be somehow suitable for single velocity instance
     * Redis will be more suitable in all situation and multiple velocity instances
     * I have to create a Queue system for requests and remove them from the queue after the request is completed
     * The redis version can be platform independent so it will be better to not implement it inside the velocity module
     * Also Redis could be used instead of MySQL/SQLite as database for storing user data so implementing a Redis method for Database interface is also an option to consider
     */
    override fun vanish(options: VanishOptions) {
        // TODO: Send vanish request to all backend servers. (requires custom event servers)
        TODO("vanish player from velocity is not implemented")
    }

    override fun unVanish(options: VanishOptions) {
        // TODO: Send unvanish request to all backend servers. (requires custom event servers)
        TODO("unvanish player from velocity is not implemented")
    }

    override fun hasPermission(permission: String): Boolean {
        return player()?.hasPermission(permission) == true
    }

    override fun sendMessage(content: String) {
        player()?.sendMessage(content.component())
    }

    fun sendMessage(content: Component) {
        player()?.sendMessage(content)
    }

    override fun sendActionbar(content: String) {
        player()?.sendActionBar(content.component())
    }

    fun sendActionbar(content: Component) {
        player()?.sendActionBar(content)
    }

    companion object {
        @JvmStatic
        fun fromUser(user: User): VelocityUser {
            return VelocityUser(user.uniqueId, user.username).apply {
                this.isOnline = user.isOnline
                this.isVanished = user.isVanished
                this.vanishLevel = user.vanishLevel
            }
        }
    }

}