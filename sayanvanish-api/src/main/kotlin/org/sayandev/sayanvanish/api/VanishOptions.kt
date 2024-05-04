package org.sayandev.sayanvanish.api

data class VanishOptions(
    var sendMessage: Boolean = true,
) {

    class Builder {
        private var sendMessage = true

        fun sendMessage(sendMessage: Boolean): Builder {
            this.sendMessage = sendMessage
            return this
        }

        fun build(): VanishOptions {
            return VanishOptions(sendMessage)
        }
    }

    companion object {
        @JvmStatic
        fun defaultOptions(): VanishOptions {
            return VanishOptions()
        }
    }
}