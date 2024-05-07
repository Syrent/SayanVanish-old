package org.sayandev.sayanvanish.bukkit.command

import org.bukkit.OfflinePlayer
import org.bukkit.entity.Player
import org.sayandev.sayanvanish.api.VanishOptions
import org.sayandev.sayanvanish.bukkit.api.SayanVanishBukkitAPI.Companion.getOrAddUser
import org.sayandev.sayanvanish.bukkit.api.SayanVanishBukkitAPI.Companion.user
import org.sayandev.stickynote.bukkit.command.StickyCommand
import org.sayandev.stickynote.bukkit.command.interfaces.SenderExtension
import org.sayandev.stickynote.bukkit.onlinePlayers
import org.sayandev.stickynote.bukkit.utils.AdventureUtils.component
import org.sayandev.stickynote.bukkit.utils.AdventureUtils.sendMessage
import org.sayandev.stickynote.lib.incendo.cloud.bukkit.parser.OfflinePlayerParser
import org.sayandev.stickynote.lib.incendo.cloud.component.CommandComponent
import org.sayandev.stickynote.lib.incendo.cloud.parser.flag.CommandFlag
import org.sayandev.stickynote.lib.incendo.cloud.parser.standard.IntegerParser
import org.sayandev.stickynote.lib.incendo.cloud.parser.standard.StringParser
import org.sayandev.stickynote.lib.incendo.cloud.suggestion.Suggestion
import org.sayandev.stickynote.lib.kyori.adventure.text.minimessage.tag.resolver.Placeholder
import java.util.concurrent.CompletableFuture

class SayanVanishCommand : StickyCommand("sayanvanish", "vanish") {

    val command = manager.commandBuilder(this.name, *aliases)
        .permission(constructBasePermission("vanish"))
        .optional("player", OfflinePlayerParser.offlinePlayerParser())
        .flag(
            CommandFlag.builder<SenderExtension?>("state").withComponent(
                CommandComponent.builder<SenderExtension, String>("state", StringParser.stringParser())
                    .suggestionProvider { _, _ ->
                        CompletableFuture.completedFuture(listOf("on", "off").map { Suggestion.suggestion(it) })
                    })
        )
        .flag(CommandFlag.builder<SenderExtension?>("silent").withAliases("s"))
        .handler { context ->
            val sender = context.sender().bukkitSender()
            val target = context.optional<OfflinePlayer>("player")
            val state = context.flags().get<String>("state")

            if (!target.isPresent && sender !is Player) {
                sender.sendMessage("<red>You have to provide a player".component())
                return@handler
            }

            val player =
                if (target.isPresent) context.optional<OfflinePlayer>("player").get() else context.sender().player()
                    ?: return@handler
            val user = player.getOrAddUser()

            val options = VanishOptions.defaultOptions().apply {
                if (context.flags().hasFlag("silent")) {
                    this.sendMessage = false
                }
            }

            when (state) {
                "on" -> user.vanish(options)
                "off" -> user.unVanish(options)
                else -> user.toggleVanish(options)
            }

            if (target.isPresent) {
                if (!player.isOnline) {
                    sender.sendMessage("<gray><gold><player></gold> is currently offline. The vanish state has been updated to <state> and will take effect upon their return.".component(Placeholder.unparsed("player", player.name ?: "N/A"), Placeholder.parsed("state", user.stateText())))
                } else {
                    sender.sendMessage("<gray>The vanish state of <gold><player></gold> has been updated to <state>.".component(Placeholder.unparsed("player", player.name ?: "N/A"), Placeholder.parsed("state", user.stateText())))
                }
            }
        }

    init {
        manager.command(command.build())

        manager.command(builder
            .literal("help")
            .permission(constructBasePermission("help"))
            .handler { context ->
                help.queryCommands("$name ${context.getOrDefault("query", "")}", context.sender())
            }
            .build())

        manager.command(builder
            .literal("reload")
            .permission(constructBasePermission("reload"))
            .handler { context ->
                val sender = context.sender().bukkitSender()
                // TODO: Reload config
                sender.sendMessage("<green>Plugin successfully reloaded!".component())
            }
            .build())

        val levelLiteral = builder
            .literal("level")
            .permission(constructBasePermission("level"))

        manager.command(levelLiteral
            .literal("set")
            .permission(constructBasePermission("level.set"))
            .required("player", OfflinePlayerParser.offlinePlayerParser())
            .required("level", IntegerParser.integerParser(0))
            .handler { context ->
                val sender = context.sender().bukkitSender()
                val target = context.get<OfflinePlayer>("player")

                if (!target.hasPlayedBefore()) {
                    sender.sendMessage("<red>Player not found".component())
                    return@handler
                }

                val user = target.getOrAddUser()
                user.vanishLevel = context.get("level")
                user.save()

                sender.sendMessage("<gray>Vanish level set to <gold><level></gold>".component(Placeholder.unparsed("level", user.vanishLevel.toString())))
            }
            .build())

        manager.command(levelLiteral
            .literal("get")
            .permission(constructBasePermission("level.get"))
            .required("player", OfflinePlayerParser.offlinePlayerParser())
            .handler { context ->
                val sender = context.sender().bukkitSender()
                val target = context.get<OfflinePlayer>("player")

                if (!target.hasPlayedBefore()) {
                    sender.sendMessage("<red>Player not found".component())
                    return@handler
                }

                val user = target.user()

                sender.sendMessage("<gray><gold><player></gold> vanish level is <gold><level></gold>".component(Placeholder.unparsed("player", target.name ?: "N/A"), Placeholder.unparsed("level", (user?.vanishLevel ?: 0).toString())))
            }
            .build())
    }
}