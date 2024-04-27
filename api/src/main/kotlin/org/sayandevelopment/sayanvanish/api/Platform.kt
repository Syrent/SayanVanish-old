package org.sayandevelopment.sayanvanish.api

object Platform {

    private var id = "unsupported"

    @JvmStatic
    fun getId(): String {
        return id
    }

    @JvmStatic
    fun setPlatformId(id: String) {
        this.id = id;
    }

}