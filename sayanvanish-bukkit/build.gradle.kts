plugins {
    kotlin("jvm")
    id("xyz.jpenilla.run-paper") version "2.3.0"
}

group = "org.sayandevelopment.sayanvanish.bukkit"

repositories {
    // Spigot
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")

    // cloud
    maven("https://oss.sonatype.org/content/repositories/snapshots")

    // MockBukkit
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    compileOnly("org.spigotmc:spigot-api:1.20.5-R0.1-SNAPSHOT")

    implementation("org.sayandevelopment:stickynote-bukkit:1.0.0")

    api(project(":sayanvanish-api"))
    api(kotlin("reflect"))

    testImplementation("com.github.seeseemelk:MockBukkit-v1.20:3.9.0")
}

tasks {
    runServer {
        minecraftVersion("1.20.6")
    }
}

publishing {
    publications {
        create<MavenPublication>("shadowJar") {
            artifact(tasks["shadowJar"])
        }
    }
}
