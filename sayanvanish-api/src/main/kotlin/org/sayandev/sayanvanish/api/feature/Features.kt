package org.sayandev.sayanvanish.api.feature

object Features {
    val features = mutableMapOf<Class<*>, Feature>()

    @JvmStatic
    fun addFeature(feature: Feature) {
        features[feature.javaClass] = feature
    }

    @JvmStatic
    fun features(): List<Feature> {
        return features.values.toList()
    }
}