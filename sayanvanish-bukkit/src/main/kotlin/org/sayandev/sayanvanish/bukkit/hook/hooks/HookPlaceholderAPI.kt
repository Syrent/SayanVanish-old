package org.sayandev.sayanvanish.bukkit.hook.hooks

import me.clip.placeholderapi.expansion.PlaceholderExpansion
import org.bukkit.OfflinePlayer
import org.sayandev.sayanvanish.bukkit.api.SayanVanishBukkitAPI
import org.sayandev.sayanvanish.bukkit.api.SayanVanishBukkitAPI.Companion.user
import org.sayandev.stickynote.bukkit.StickyNote
import org.sayandev.stickynote.bukkit.onlinePlayers

class HookPlaceholderAPI : PlaceholderExpansion() {
    override fun getIdentifier(): String {
        return StickyNote.plugin().description.name.lowercase()
    }

    override fun getAuthor(): String {
        return StickyNote.plugin().description.authors.joinToString(", ")
    }

    override fun getVersion(): String {
        return StickyNote.plugin().description.version
    }

    override fun persist(): Boolean {
        return true
    }

    override fun canRegister(): Boolean {
        return true
    }

    override fun onRequest(player: OfflinePlayer, params: String): String? {
        if (params.equals("vanished", true)) {
            return if (SayanVanishBukkitAPI.getInstance().getVanishedUsers().map { it.username }.contains(player.name)) "true" else "false"
        }

        if (params.equals("count", true)) {
            return SayanVanishBukkitAPI.getInstance().getUsers { user -> user.isOnline && user.isVanished }.size.toString()
        }

        if (params.equals("level", true)) {
            return player.user()?.vanishLevel?.toString() ?: "0"
        }

        if (params.startsWith("online_")) {
            val type = params.substring(7)
            val vanishedOnlineUsers = SayanVanishBukkitAPI.getInstance().getUsers { user -> user.isVanished && user.isOnline }

            return if (type.equals("here", true)) {
                onlinePlayers.filter { onlinePlayer -> !vanishedOnlineUsers.map { vanishedOnlineUser -> vanishedOnlineUser.username }.contains(onlinePlayer.name) }.size.toString()
            } else if (type.equals("total", true)) {
                // TODO: add velocity module
                return "NOT IMPLEMENTED YET"
            } else {
                // TODO: add velocity module
                return "NOT IMPLEMENTED YET"
            }
        }

        return null
    }
}