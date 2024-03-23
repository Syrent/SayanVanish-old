plugins {
    kotlin("jvm")
}

group = "ir.syrent.spigot"
version = "1.0.0"

repositories {
    mavenCentral()
}

dependencies {
    compileOnly(project(":api"))
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