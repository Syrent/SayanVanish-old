package org.sayandev.sayanvanish.bukkit

import com.destroystokyo.paper.event.entity.PreSpawnerSpawnEvent
import com.destroystokyo.paper.event.server.PaperServerListPingEvent
import net.ess3.api.events.AfkStatusChangeEvent
import net.ess3.api.events.PrivateMessagePreSendEvent
import org.bukkit.GameMode
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.block.BlockReceiveGameEvent
import org.bukkit.event.entity.EntityChangeBlockEvent
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityPickupItemEvent
import org.bukkit.event.player.*
import org.bukkit.event.raid.RaidTriggerEvent
import org.sayandev.sayanvanish.api.Permission
import org.sayandev.sayanvanish.api.VanishOptions
import org.sayandev.sayanvanish.bukkit.api.SayanVanishBukkitAPI
import org.sayandev.sayanvanish.bukkit.api.SayanVanishBukkitAPI.Companion.getOrCreateUser
import org.sayandev.sayanvanish.bukkit.api.SayanVanishBukkitAPI.Companion.user
import org.sayandev.sayanvanish.bukkit.config.language
import org.sayandev.sayanvanish.bukkit.config.settings
import org.sayandev.stickynote.bukkit.*
import org.sayandev.stickynote.bukkit.event.registerListener
import org.sayandev.stickynote.bukkit.utils.AdventureUtils.component
import org.sayandev.stickynote.lib.xseries.ReflectionUtils

object VanishManager : Listener {

    private const val REMOVAL_MESSAGE_ID = "SAYANVANISH_DISABLE_MESSAGE"
    var generalJoinMessage: String? = null
    var generalQuitMessage: String? = null

    init {
        registerListener(this)

        startActionbarTask()
        registerPreventions()
    }

    private fun startActionbarTask() {
        runSync({
            if (!settings.vanish.actionbar.enabled) return@runSync
            for (user in onlinePlayers.mapNotNull { it.user() }.filter { it.isVanished }) {
                user.sendActionbar(settings.vanish.actionbar.content.component())
            }
        }, settings.vanish.actionbar.repeatEvery, settings.vanish.actionbar.repeatEvery)
    }

    @EventHandler(priority = EventPriority.LOWEST)
    private fun vanishPlayerOnJoin(event: PlayerJoinEvent) {
        val player = event.player
        val user = player.user(false)
        val vanishJoinOptions = VanishOptions.Builder().sendMessage(false).build()

        if (user == null) {
            val tempUser = player.getOrCreateUser()

            if (settings.vanish.state.checkPermissionOnQuit && !tempUser.hasPermission(Permission.VANISH)) {
                return
            }

            if (tempUser.hasPermission(Permission.VANISH_ON_JOIN) || settings.vanish.state.vanishOnJoin) {
                tempUser.isOnline = true
                tempUser.isVanished = true
                tempUser.vanish(vanishJoinOptions)
                tempUser.save()
            }
            return
        }
        user.isOnline = true

        if (settings.vanish.state.checkPermissionOnQuit && !user.hasPermission(Permission.VANISH)) {
            user.unVanish()
            user.save()
            return
        }

        if (user.hasPermission(Permission.VANISH_ON_JOIN) || (user.isVanished && settings.vanish.remember) || settings.vanish.state.vanishOnJoin) {
            user.isVanished = true
            user.vanish(vanishJoinOptions)
        }
        user.save()

        if (player.user(false)?.isVanished == true) {
            if (settings.vanish.joinLeaveMessage.getFromJoinEvent) {
                generalJoinMessage = event.joinMessage
            }
            event.joinMessage = REMOVAL_MESSAGE_ID
        }
        return
    }

    @EventHandler(priority = EventPriority.LOWEST)
    private fun updateUserOnQuit(event: PlayerQuitEvent) {
        val player = event.player
        val user = player.user() ?: return
        if ((settings.vanish.state.reappearOnQuit && user.isVanished) || (settings.vanish.state.checkPermissionOnQuit && !user.hasPermission(Permission.VANISH))) {
            user.unVanish()
        }
        user.isOnline = false
        user.save()

        if (user.isVanished) {
            if (settings.vanish.joinLeaveMessage.getFromQuitEvent) {
                generalQuitMessage = event.quitMessage
            }
            event.quitMessage = REMOVAL_MESSAGE_ID
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    private fun hideVanishedPlayersOnJoin(event: PlayerJoinEvent) {
        for (user in SayanVanishBukkitAPI.getInstance().getUsers { it.isVanished && it.player() != null }) {
            user.hideUser(event.player)
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    private fun setJoinMessage(event: PlayerJoinEvent) {
        if (event.joinMessage != REMOVAL_MESSAGE_ID) {
            if (settings.vanish.joinLeaveMessage.getFromJoinEvent) {
                generalJoinMessage = event.joinMessage
            }
        } else {
            event.joinMessage = null
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    private fun setQuitMessage(event: PlayerQuitEvent) {
        if (event.quitMessage != REMOVAL_MESSAGE_ID) {
            if (settings.vanish.joinLeaveMessage.getFromQuitEvent) {
                generalQuitMessage = event.quitMessage
            }
        } else {
            event.quitMessage = null
        }
    }

    // TODO: Holding this until configuration support kotlin abstraction deserialization
    private fun registerPreventions() {
        registerListener<EntityPickupItemEvent> { event ->
            val user = (event.entity as? Player)?.user() ?: return@registerListener
            if (user.isVanished && settings.vanish.prevention.pickup) {
                event.isCancelled = true
            }
        }
        registerListener<BlockPlaceEvent> { event ->
            val user = event.player.user() ?: return@registerListener
            if (user.isVanished && settings.vanish.prevention.blockPlace) {
                event.isCancelled = true
            }
        }
        registerListener<BlockBreakEvent> { event ->
            val user = event.player.user() ?: return@registerListener
            if (user.isVanished && settings.vanish.prevention.blockBreak) {
                event.isCancelled = true
            }
        }
        registerListener<PlayerInteractEvent> { event ->
            val user = event.player.user() ?: return@registerListener
            if (user.isVanished) {
                val isPressurePlate = settings.vanish.prevention.pressurePlateTrigger && event.action == Action.PHYSICAL && event.clickedBlock?.type?.name?.contains("PLATE") == true
                val isDripLeaf = settings.vanish.prevention.dripLeaf && event.action == Action.PHYSICAL && event.clickedBlock?.type?.name?.equals("BIG_DRIPLEAF") == true
                if (settings.vanish.prevention.interact || isPressurePlate || isDripLeaf) {
                    event.isCancelled = true
                }
            }
        }
        if (StickyNote.isPaper() && ReflectionUtils.supports(13)) {
            registerListener<PlayerAdvancementDoneEvent> { event ->
                val user = event.player.user() ?: return@registerListener
                if (user.isVanished && settings.vanish.prevention.advancement) {
//                    event.message(null)
                    for (criteria in event.advancement.criteria) {
                        event.player.getAdvancementProgress(event.advancement).revokeCriteria(criteria)
                    }
                }
            }
        }
        registerListener<EntityDamageByEntityEvent> { event ->
            val user = (event.entity as? Player)?.user() ?: return@registerListener
            if (user.isVanished && settings.vanish.prevention.damage) {
                event.isCancelled = true
            }
        }
        registerListener<EntityChangeBlockEvent> { event ->
            val user = (event.entity as? Player)?.user() ?: return@registerListener
            if (user.isVanished && settings.vanish.prevention.blockGrief) {
                event.isCancelled = true
            }
        }
        if (ReflectionUtils.supports(19)) {
            registerListener<BlockReceiveGameEvent> { event ->
                val user = (event.entity as? Player)?.user() ?: return@registerListener
                if (user.isVanished && settings.vanish.prevention.sculkSensorActivation) {
                    event.isCancelled = true
                }
            }
        }
        if (ReflectionUtils.supports(15)) {
            registerListener<RaidTriggerEvent> { event ->
                val user = (event.player as? Player)?.user() ?: return@registerListener
                if (user.isVanished && settings.vanish.prevention.raidTrigger) {
                    event.isCancelled = true
                }
            }
        }
        registerListener<PreSpawnerSpawnEvent> { event ->
            if (!settings.vanish.prevention.mobSpawning) return@registerListener
            if (SayanVanishBukkitAPI.getInstance().getUsers { it.player() != null }.mapNotNull { it.player() }.any { it.world == event.spawnerLocation.world && it.location.distance(event.spawnerLocation) <= 256 }) {
                event.isCancelled = true
            }
        }
        if (StickyNote.isPaper()) {
            registerListener<PaperServerListPingEvent> { event ->
                if (settings.vanish.prevention.serverListCount) {
                    val vanishedPlayers = SayanVanishBukkitAPI.getInstance().getUsers { it.player() != null }
                    event.numPlayers -= vanishedPlayers.count()
                    event.playerSample.removeIf { profile -> vanishedPlayers.map { vanishedPlayer -> vanishedPlayer.uniqueId }.contains(profile.id) }
                }
            }
        }
        if (hasPlugin("Essentials")) {
            registerListener<AfkStatusChangeEvent> { event ->
                if (!settings.vanish.hooks.essentials.enabled && settings.vanish.hooks.essentials.preventAfkStatusChange) return@registerListener
                val user = event.affected.uuid?.user() ?: return@registerListener
                if (user.isVanished) {
                    event.isCancelled = true
                }
            }
            registerListener<PrivateMessagePreSendEvent> { event ->
                if (!settings.vanish.hooks.essentials.enabled && settings.vanish.hooks.essentials.preventPrivateMessage) return@registerListener
                val user = event.recipient.uuid?.user() ?: return@registerListener
                if (user.isVanished) {
                    event.sender.sendMessage(com.earth2me.essentials.I18n.tl("errorWithMessage", com.earth2me.essentials.I18n.tl("playerNotFound")))
                    event.isCancelled = true
                }
            }
        }

        val sneakMap = mutableMapOf<Player, GameMode>()
        val sneakList = mutableListOf<Player>()
        registerListener<PlayerToggleSneakEvent> { event ->
            val player = event.player
            if (!player.isSneaking || player.user()?.isVanished != true || !settings.vanish.sneakToggleGameMode) return@registerListener
            if (sneakList.contains(player)) {
                if (player.gameMode == GameMode.SPECTATOR) {
                    player.gameMode = sneakMap[player]!!
                } else {
                    sneakMap[player] = player.gameMode
                    player.gameMode = GameMode.SPECTATOR
                }
            } else {
                if (player.gameMode != GameMode.SPECTATOR) {
                    sneakMap[player] = player.gameMode
                }
                sneakList.add(player)
                runSync({
                    sneakList.remove(player)
                }, 8)
            }
        }
        registerListener<AsyncPlayerChatEvent> { event ->
            val user = event.player.user() ?: return@registerListener
            if (!user.isVanished) return@registerListener
            val message = event.message
            if (message.startsWith("!")) {
                event.message = message.removePrefix("!")
            } else {
                user.sendMessage(language.vanish.cantChatWhileVanished.component())
                event.isCancelled = true
            }
        }
    }
}