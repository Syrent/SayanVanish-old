package org.sayandev.sayanvanish.api.feature

import org.sayandev.sayanvanish.api.User
import org.sayandev.sayanvanish.api.User.Companion.cast
import org.sayandev.stickynote.lib.spongepowered.configurate.ConfigurationNode
import org.sayandev.stickynote.lib.spongepowered.configurate.serialize.TypeSerializer
import java.lang.reflect.Type

abstract class Feature(val id: String) {

    open var isEnabled: Boolean = false

    open fun enable() {
        isEnabled = true
    }

    open fun disable() {
        isEnabled = false
    }

    abstract fun serialize(node: ConfigurationNode)

//    abstract fun <T: Feature> deserialize(node: ConfigurationNode): T

}

class FeatureTypeSerializer : TypeSerializer<Feature> {
    override fun deserialize(type: Type, node: ConfigurationNode): Feature {
        return Features.features().first { it.id == node.node("id").string }
    }

    override fun serialize(type: Type, obj: Feature?, node: ConfigurationNode) {
        obj!!.javaClass.getDeclaredMethod("serialize", ConfigurationNode::class.java).invoke(obj, node)
    }
}