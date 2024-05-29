package org.sayandev.sayanvanish.bukkit.config

import org.bukkit.potion.PotionEffectType
import org.sayandev.sayanvanish.api.feature.Feature
import org.sayandev.sayanvanish.api.feature.FeatureTypeSerializer
import org.sayandev.sayanvanish.api.feature.Features
import org.sayandev.sayanvanish.bukkit.feature.HookFeature
import org.sayandev.sayanvanish.bukkit.feature.ListenedFeature
import org.sayandev.stickynote.bukkit.pluginDirectory
import org.sayandev.stickynote.core.configuration.Config
import org.sayandev.stickynote.lib.spongepowered.configurate.objectmapping.ConfigSerializable
import org.sayandev.stickynote.lib.spongepowered.configurate.objectmapping.meta.Setting
import org.sayandev.stickynote.lib.spongepowered.configurate.serialize.TypeSerializerCollection
import java.io.File

public var settings: SettingsConfig = SettingsConfig.fromConfig() ?: SettingsConfig.defaultConfig()

@ConfigSerializable
data class SettingsConfig(
    val general: General = General(),
    val vanish: Vanish = Vanish(),
) : Config(
    pluginDirectory,
    fileName,
    typeSerializerCollection
) {

    init {
        load()
    }

    @ConfigSerializable
    data class General(
        val language: String = LanguageConfig.Language.EN_US.id,
        val updateChecker: Boolean = true,
        val proxyMode: Boolean = false
    )

    @ConfigSerializable
    data class Vanish(
        val remember: Boolean = true,
        val seeAsSpectator: Boolean = true, // TODO
        val effects: List<String> = listOf(PotionEffectType.NIGHT_VISION.key.key), // TODO
        val level: Level = Level(),
        val sneakToggleGameMode: Boolean = true,
        val state: State = State(),
        val fly: Fly = Fly(),
        val invulnerability: Invunerability = Invunerability(),
        val prevention: Prevention = Prevention(),
        val joinLeaveMessage: JoinLeaveMessage = JoinLeaveMessage(),
        val features: MutableList<Feature> = Features.features,
    ) {
        @ConfigSerializable
        data class JoinLeaveMessage(
            val getFromJoinEvent: Boolean = true,
            val getFromQuitEvent: Boolean = true,
        )

        @ConfigSerializable
        data class Level(
            val enabled: Boolean = true
        )

        @ConfigSerializable
        data class State(
            val vanishOnJoin: Boolean = false,
            val reappearOnQuit: Boolean = false,
            val checkPermissionOnQuit: Boolean = false,
            val checkPermissionOnJoin: Boolean = false,
        )

        @ConfigSerializable
        data class Fly(
            val enabled: Boolean = true,
            val disableOnReappear: Boolean = false,
        )

        @ConfigSerializable
        data class Invunerability(
            val enabled: Boolean = true,
            val disableOnReappear: Boolean = true,
        )

        @ConfigSerializable
        data class Prevention(
            val playerTablistPackets: Boolean = true, // TODO
            val containerOpenPacket: Boolean = true, // TODO
        )

        @ConfigSerializable
        data class Hooks(
            val squareMap: SquareMap = SquareMap(),
            @Setting("placeholder-api") val placeholderAPI: PlaceholderAPI = PlaceholderAPI(), // TODO
            val citizens: Citizens = Citizens(), // TODO
        ) {
            @ConfigSerializable
            data class Essentials(
                val enabled: Boolean = true,
                val preventAfkStatusChange: Boolean = true,
                val preventPrivateMessage: Boolean = true,
            )

            @ConfigSerializable
            data class Dynmap(
                val enabled: Boolean = true,
                val sendJoinLeaveMessage: Boolean = true,
            )

            @ConfigSerializable
            data class SquareMap(
                val enabled: Boolean = true,
            )

            @ConfigSerializable
            data class PlaceholderAPI(
                val enabled: Boolean = true,
            )

            @ConfigSerializable
            data class Citizens(
                val enabled: Boolean = true,
            )
        }
    }

    companion object {
        private val typeSerializerCollection = TypeSerializerCollection.builder()
            .apply {
                register(Feature::class.java, FeatureTypeSerializer())
            }
            .build()

        private val fileName = "settings.yml"
        val settingsFile = File(pluginDirectory, fileName)

        @JvmStatic
        fun defaultConfig(): SettingsConfig {
            return SettingsConfig()
        }

        @JvmStatic
        fun fromConfig(): SettingsConfig? {
            return fromConfig<SettingsConfig>(settingsFile, typeSerializerCollection)
        }
    }
}