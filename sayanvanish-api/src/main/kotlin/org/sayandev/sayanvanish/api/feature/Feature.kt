package org.sayandev.sayanvanish.api.feature

import org.sayandev.stickynote.lib.spongepowered.configurate.ConfigurationNode
import org.sayandev.stickynote.lib.spongepowered.configurate.objectmapping.ConfigSerializable
import org.sayandev.stickynote.lib.spongepowered.configurate.serialize.TypeSerializer
import java.lang.reflect.Type

@ConfigSerializable
abstract class Feature {

    abstract val id: String
    abstract var enabled: Boolean
    @Transient open var condition: Boolean = true

    open fun isActive(): Boolean {
        return enabled && condition
    }

    open fun enable() {
        enabled = true
    }

    open fun disable() {
        enabled = false
    }
}

class FeatureTypeSerializer : TypeSerializer<Feature> {
    override fun deserialize(type: Type, node: ConfigurationNode): Feature {
        return Features.features().first { it.id == node.node("id").string }
    }

    override fun serialize(type: Type, obj: Feature?, node: ConfigurationNode) {
        obj!!.javaClass.getDeclaredMethod("serialize", ConfigurationNode::class.java).invoke(obj, node)
    }
}