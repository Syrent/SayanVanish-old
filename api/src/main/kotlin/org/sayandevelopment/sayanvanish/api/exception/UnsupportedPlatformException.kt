package org.sayandevelopment.sayanvanish.api.exception

import org.sayandevelopment.sayanvanish.api.Platform

class UnsupportedPlatformException(action: String?, message: String) : Exception(message) {
    constructor(action: String) : this(action, "This action is not supported on this platform yet. (platform: ${Platform.getId()}, action: ${action})")
    constructor() : this(null, "This platform is not supported yet. (platform: ${Platform.getId()})")
}