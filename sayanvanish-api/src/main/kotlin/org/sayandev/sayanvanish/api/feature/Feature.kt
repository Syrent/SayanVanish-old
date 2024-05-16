package org.sayandev.sayanvanish.api.feature

import org.sayandev.stickynote.lib.spongepowered.configurate.objectmapping.ConfigSerializable

@ConfigSerializable
abstract class Feature(val id: String) {

    var isEnabled: Boolean = false

    open fun enable() {
        isEnabled = true
    }

    open fun disable() {
        isEnabled = false
    }

}