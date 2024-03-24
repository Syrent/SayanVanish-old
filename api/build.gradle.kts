plugins {
    kotlin("jvm")
}

group = "org.sayandevelopment.sayanvanish.api"

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
}