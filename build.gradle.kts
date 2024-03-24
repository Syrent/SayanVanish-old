plugins {
    kotlin("jvm") version "1.9.22"
    java
    `maven-publish`
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

allprojects {
    group = "org.sayandevelopment.sayanvanish"
    version = "0.0.1"

    plugins.apply("java")
    plugins.apply("maven-publish")
    plugins.apply("com.github.johnrengelman.shadow")

    repositories {
        mavenCentral()
    }

    java {
        withJavadocJar()
        withSourcesJar()
    }
}

publishing {
    publications {
        create<MavenPublication>("shadowJar") {
//            group = "com.github.Syrent"
            artifact(tasks["shadowJar"])
        }
    }
}

