package org.sayandev.sayanvanish.api

data class VanishOptions(
    var sendMessage: Boolean = true,
    var notifyOthers: Boolean = true,
) {

    class Builder {
        private var sendMessage = true
        private var notifyOthers = true

        fun sendMessage(sendMessage: Boolean): Builder {
            this.sendMessage = sendMessage
            return this
        }

        fun notifyOthers(notifyOthers: Boolean): Builder {
            this.notifyOthers = notifyOthers
            return this
        }

        fun build(): VanishOptions {
            return VanishOptions(sendMessage, notifyOthers)
        }
    }

    companion object {
        @JvmStatic
        fun defaultOptions(): VanishOptions {
            return VanishOptions()
        }
    }
}