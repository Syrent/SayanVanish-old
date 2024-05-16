package org.sayandev.sayanvanish.api.feature

import org.sayandev.stickynote.lib.reflections.Reflections
import java.lang.reflect.Constructor


object RegisteredFeatureHandler {

    fun process() {
        val reflections = Reflections("org.sayandev.sayanvanish.bukkit")
        val annotatedClasses = reflections.getTypesAnnotatedWith(RegisteredFeature::class.java)

        for (annotatedClass in annotatedClasses) {
            createNewInstance(annotatedClass)
        }
    }

    private fun createNewInstance(clazz: Class<*>) {
        try {
            val constructor: Constructor<*> = clazz.getDeclaredConstructor()
            constructor.setAccessible(true)
            val instance = constructor.newInstance()
            when (instance) {
                is Feature -> {
                    Features.addFeature(instance)
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