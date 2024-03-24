package org.sayandevelopment.sayanvanish.bukkit.utils

import net.kyori.adventure.platform.bukkit.BukkitAudiences
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

object ComponentUtils {

    val miniMessage = MiniMessage.miniMessage()
    val audience = BukkitAudiences.create(null!!)

    fun CommandSender.sendMessage(content: Component) {
        audience.sender(this).sendMessage(content)
    }

    fun Player.sendActionbar(content: Component) {
        audience.player(this).sendActionBar(content)
    }

}