package org.sayandev.sayanvanish.api.feature.category

enum class FeatureCategories(override val directory: String?) : FeatureCategory {
    HOOK("hooks"),
    PREVENTION("preventions"),
    DEFAULT(null),
    CUSTOM("custom")
}