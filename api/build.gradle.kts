plugins {
    kotlin("jvm")
}

group = "ir.syrent.api"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("net.kyori:adventure-api:4.16.0")
    implementation("net.kyori:adventure-text-minimessage:4.16.0")
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