plugins {
    id("xyz.jpenilla.run-paper") version "2.3.0"
}

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

    implementation("org.sayandev:stickynote-bukkit:1.0.0")

    api(project(":sayanvanish-api"))
    api(kotlin("reflect"))

    testImplementation("com.github.seeseemelk:MockBukkit-v1.20:3.9.0")
}

tasks {
    runServer {
        minecraftVersion("1.20.6")

        downloadPlugins {
            url("https://download.luckperms.net/1539/bukkit/loader/LuckPerms-Bukkit-5.4.126.jar")
            url("https://github.com/Insprill/custom-join-messages/releases/download/v17.5.0/custom-join-messages-17.5.0.jar")
        }
    }
}