package org.sayandev.sayanvanish.api

enum class Permission(val value: String) {
    VANISH_ON_JOIN("action.vanish.onjoin");

    fun permission(): String {
        return "sayanvanish.${value}"
    }
}