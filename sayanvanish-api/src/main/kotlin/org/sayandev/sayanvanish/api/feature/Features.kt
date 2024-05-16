package org.sayandev.sayanvanish.api.feature

object Features {
    private val features = mutableMapOf<String, Feature>()

    @JvmStatic
    fun addFeature(feature: Feature) {
        features[feature.id] = feature
    }

    @JvmStatic
    fun features(): List<Feature> {
        return features.values.toList()
    }
}