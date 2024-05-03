package org.sayandevelopment.sayanvanish.bukkit.api

import org.sayandevelopment.sayanvanish.api.SayanVanishAPI
import org.bukkit.OfflinePlayer
import java.util.*

class SayanVanishBukkitAPI(useCache: Boolean) : SayanVanishAPI<BukkitUser>(useCache) {
    companion object {
        fun getInstance(useCache: Boolean): SayanVanishAPI<BukkitUser> {
            return SayanVanishBukkitAPI(useCache)
        }

        fun getInstance(): SayanVanishAPI<BukkitUser> {
            return getInstance(false)
        }

        fun UUID.user(): BukkitUser? {
            return getInstance().getUser(this)
        }

        fun OfflinePlayer.user(): BukkitUser? {
            return getInstance().getUser(this.uniqueId)
        }
    }
}