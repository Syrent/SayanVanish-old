plugins {
    kotlin("jvm") version "1.9.22"
    java
    `maven-publish`
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

repositories {
    mavenCentral()
}

allprojects {
    group = "com.github.syrent"
    version = "1.0.0"

    plugins.apply("java")
    plugins.apply("maven-publish")
    plugins.apply("com.github.johnrengelman.shadow")

    java {
        sourceCompatibility = JavaVersion.VERSION_1_8
    }
}

publishing {
    publications {
        create<MavenPublication>("shadowJar") {
            artifact(tasks["shadowJar"])
        }
    }

    repositories {
        maven {
            name = "jitpack"
            url = uri("https://jitpack.io/")
            content {
                includeGroup("ir.syrent.sayanvanish")
            }
        }
    }
}

