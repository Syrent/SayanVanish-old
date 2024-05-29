package org.sayandev.sayanvanish.api.feature

import org.sayandev.stickynote.lib.reflections.Reflections
import org.sayandev.stickynote.lib.spongepowered.configurate.CommentedConfigurationNode
import java.lang.reflect.Constructor
import java.util.function.Consumer


object RegisteredFeatureHandler {

    fun process(featureSection: CommentedConfigurationNode) {
        val reflections = Reflections("org.sayandev.sayanvanish")
        val annotatedClasses = reflections.getTypesAnnotatedWith(RegisteredFeature::class.java)

        for (annotatedClass in annotatedClasses) {
            createNewInstance(annotatedClass, featureSection)
        }
    }

    private fun createNewInstance(clazz: Class<*>, featureSection: CommentedConfigurationNode) {
        try {
            if (Features.features.map { it.javaClass }.contains(clazz)) return
            val constructor: Constructor<out Feature> = clazz.getDeclaredConstructor() as Constructor<out Feature>
            constructor.setAccessible(true)
            val freshInstance = constructor.newInstance()
//            val instance = featureSection.node(clazz.getMethod("getId")).get(clazz)
//            val instance = clazz.getDeclaredMethod("deserialize", CommentedConfigurationNode::class.java).invoke(freshInstance, featureSection.node(clazz.getMethod("getId")))
            when (freshInstance) {
                is Feature -> {
                    Features.addFeature(freshInstance)
                }
                else -> {
                    throw NullPointerException("Tried to add item to Items but the type ${clazz.name} is not supported")
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}