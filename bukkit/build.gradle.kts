plugins {
    kotlin("jvm")
}

group = "org.sayandevelopment.sayanvanish.bukkit"

repositories {
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")

    // cloud
    maven("https://oss.sonatype.org/content/repositories/snapshots")
}

dependencies {
    compileOnly("org.spigotmc:spigot-api:1.20.4-R0.1-SNAPSHOT")

    implementation("net.kyori:adventure-api:4.16.0")
    implementation("net.kyori:adventure-text-minimessage:4.16.0")
    implementation("net.kyori:adventure-platform-bukkit:4.3.2")

    implementation("org.sayandevelopment:stickynote-bukkit:1.0.8-SNAPSHOT")
    implementation("org.sayandevelopment:stickynote-core:1.0.8-SNAPSHOT")

    implementation(project(":api"))
}

publishing {
    publications {
        create<MavenPublication>("shadowJar") {
            artifact(tasks["shadowJar"])
        }
    }
}
