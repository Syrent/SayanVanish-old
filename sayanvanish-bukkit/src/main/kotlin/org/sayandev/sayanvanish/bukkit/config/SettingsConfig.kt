package org.sayandev.sayanvanish.bukkit.config

import org.bukkit.potion.PotionEffectType
import org.sayandev.sayanvanish.api.database.DatabaseMethod
import org.sayandev.sayanvanish.api.database.databaseConfig
import org.sayandev.sayanvanish.api.database.sql.SQLConfig
import org.sayandev.sayanvanish.api.feature.Feature
import org.sayandev.sayanvanish.api.feature.FeatureTypeSerializer
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
        val actionbar: Actionbar = Actionbar(),
        val state: State = State(),
        val fly: Fly = Fly(),
        val invulnerability: Invunerability = Invunerability(),
        val prevention: Prevention = Prevention(),
        val joinLeaveMessage: JoinLeaveMessage = JoinLeaveMessage(),
        val hooks: Hooks = Hooks(),
//        val features: List<Feature> = Features.features(),
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
            val pickup: Boolean = true,
            val blockPlace: Boolean = true,
            val blockBreak: Boolean = true,
            val interact: Boolean = false,
            val advancement: Boolean = true,
            val damage: Boolean = true,
            val mobTarget: Boolean = true,
            val push: Boolean = false,
            val pressurePlateTrigger: Boolean = true,
            val blockGrief: Boolean = true,
            val playerTablistPackets: Boolean = true, // TODO
            val sculkSensorActivation: Boolean = true,
            val containerOpenPacket: Boolean = true, // TODO
            val dripLeaf: Boolean = true,
            val raidTrigger: Boolean = true,
            val mobSpawning: Boolean = true,
            val serverListCount: Boolean = true
        )

        @ConfigSerializable
        data class Actionbar(
            val enabled: Boolean = true,
            val content: String = "<gray>You are currenly vanished!",
            val repeatEvery: Long = 20
        )

        @ConfigSerializable
        data class Hooks(
            val essentials: Essentials = Essentials(),
            val dynmap: Dynmap = Dynmap(), // TODO
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