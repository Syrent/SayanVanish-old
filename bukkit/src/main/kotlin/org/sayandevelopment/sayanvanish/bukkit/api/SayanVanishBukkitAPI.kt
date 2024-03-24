package org.sayandevelopment.sayanvanish.bukkit.api

import org.sayandevelopment.sayanvanish.api.SayanVanishAPI
import org.bukkit.OfflinePlayer
import java.util.*

class SayanVanishBukkitAPI : SayanVanishAPI<BukkitUser>() {
    companion object {
        fun getInstance(): SayanVanishAPI<BukkitUser> {
            return SayanVanishBukkitAPI()
        }

        fun UUID.user(): BukkitUser? {
            return getInstance().getUser(this)
        }

        fun OfflinePlayer.user(): BukkitUser? {
            return getInstance().getUser(this.uniqueId)
        }
    }
}