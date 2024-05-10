package org.sayandev.sayanvanish.bukkit.config

import org.sayandev.stickynote.bukkit.pluginDirectory
import org.sayandev.stickynote.core.configuration.Config
import org.sayandev.stickynote.lib.spongepowered.configurate.objectmapping.ConfigSerializable
import java.io.File

var language: LanguageConfig = LanguageConfig.fromConfig() ?: LanguageConfig.defaultConfig()

@ConfigSerializable
data class LanguageConfig(
    val vanish: Vanish = Vanish(),
    val general: General = General(),
) : Config(languageDirectory, "${settings.general.language}.yml") {

    init {
        load()
    }

    @ConfigSerializable
    data class General(
        val reloaded: String = "<green>Plugin successfully reloaded!",
        val playerNotFound: String = "<red>Player not found",
    )

    @ConfigSerializable
    data class Vanish(
        val vanishStateUpdate: String = "<gray>Your vanish state has been updated to <state>.",
        val offlineOnVanish: String = "<gray><gold><player></gold> is currently offline. The vanish state has been updated to <state> and will take effect upon their return.",
        val vanishStateOther: String = "<gray>The vanish state of <gold><player></gold> has been updated to <state>.",
        val levelSet: String = "<gray>Vanish level set to <gold><level></gold>",
        val levelGet: String = "<gray><gold><player></gold> vanish level is <gold><level></gold>",
    )

    enum class Language(val id: String) {
        EN_US("en_US"),
    }

    companion object {
        val languageDirectory = File(pluginDirectory, "languages")

        @JvmStatic
        fun defaultConfig(): LanguageConfig {
            return LanguageConfig()
        }

        @JvmStatic
        fun fromConfig(): LanguageConfig? {
            return fromConfig<LanguageConfig>(File(languageDirectory, "${settings.general.language}.yml"))
        }
    }
}