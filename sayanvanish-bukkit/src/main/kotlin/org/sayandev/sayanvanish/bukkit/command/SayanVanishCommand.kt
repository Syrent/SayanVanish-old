package org.sayandev.sayanvanish.bukkit.command

import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder
import org.bukkit.OfflinePlayer
import org.bukkit.entity.Player
import org.incendo.cloud.bukkit.parser.OfflinePlayerParser
import org.incendo.cloud.component.CommandComponent
import org.incendo.cloud.description.Description
import org.incendo.cloud.kotlin.extension.buildAndRegister
import org.incendo.cloud.parser.standard.StringParser
import org.incendo.cloud.suggestion.Suggestion
import org.sayandev.sayanvanish.bukkit.api.SayanVanishBukkitAPI.Companion.getOrAddUser
import org.sayandev.stickynote.bukkit.command.StickyCommand
import org.sayandev.stickynote.bukkit.command.interfaces.SenderExtension
import org.sayandev.stickynote.bukkit.utils.AdventureUtils.component
import java.util.concurrent.CompletableFuture

class SayanVanishCommand : StickyCommand("sayanvanish", "vanish") {

    val command = manager.buildAndRegister(this.name, Description.of("main sayanvanish command"), arrayOf(*aliases)) {
        permission(constructBasePermission("vanish"))
        optional("player", OfflinePlayerParser.offlinePlayerParser())
        flag("state", arrayOf("s"), Description.empty(), CommandComponent.builder<SenderExtension, String>("state", StringParser.stringParser()).suggestionProvider { _, _ ->
            CompletableFuture.completedFuture(listOf("on", "off").map { Suggestion.suggestion(it) })
        })
        handler { context ->
            val sender = context.sender().bukkitSender()
            val target = context.optional<OfflinePlayer>("player")
            val state = context.flags().get<String>("state")

            if (!target.isPresent && sender !is Player) {
                sender.sendMessage("<red>You have to provide a player".component())
                return@handler
            }

            val player = if (target.isPresent) context.optional<OfflinePlayer>("player").get() else context.sender().player() ?: return@handler
            val user = player.getOrAddUser()

            when (state) {
                "on" -> user.vanish()
                "off" -> user.unVanish()
                else -> user.toggleVanish()
            }

            if (target.isPresent) {
                if (!player.isOnline) {
                    sender.sendMessage("<gray><gold><player></gold> is currently offline. The vanish state has been updated to <state> and will take effect upon their return.".component(Placeholder.unparsed("player", player.name ?: "N/A"), Placeholder.parsed("state", user.stateText())))
                } else {
                    sender.sendMessage("<gray>The vanish state of <gold><player></gold> has been updated to <state>.".component(Placeholder.unparsed("player", player.name ?: "N/A"), Placeholder.parsed("state", user.stateText())))
                }
            }
        }
    }
}