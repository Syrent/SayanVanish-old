package org.sayandev.sayanvanish.bukkit.api

import org.sayandev.sayanvanish.api.SayanVanishAPI
import org.bukkit.OfflinePlayer
import org.sayandev.sayanvanish.api.database.databaseConfig
import org.sayandev.stickynote.bukkit.warn
import java.util.*

class SayanVanishBukkitAPI(useCache: Boolean) : SayanVanishAPI<BukkitUser>(BukkitUser::class, useCache) {
    companion object {
        fun getInstance(useCache: Boolean): SayanVanishAPI<BukkitUser> {
            return SayanVanishBukkitAPI(useCache)
        }

        fun getInstance(): SayanVanishAPI<BukkitUser> {
            return getInstance(databaseConfig.useCacheWhenAvailable)
        }

        fun UUID.user(): BukkitUser? {
            return getInstance().getUser(this)
        }

        fun OfflinePlayer.user(useCache: Boolean = databaseConfig.useCacheWhenAvailable): BukkitUser? {
            return getInstance(useCache).getUser(this.uniqueId)
        }

        fun OfflinePlayer.getOrCreateUser(): BukkitUser {
            return getInstance().getUser(this.uniqueId) ?: BukkitUser(this.uniqueId, this.name ?: "N/A")
        }

        fun OfflinePlayer.getOrAddUser(): BukkitUser {
            return getInstance().getUser(this.uniqueId) ?: let {
                val newUser = BukkitUser(this.uniqueId, this.name ?: "N/A")
                getInstance().addUser(newUser)
                newUser
            }
        }
    }
}