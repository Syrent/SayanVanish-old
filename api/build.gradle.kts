plugins {
    kotlin("jvm")
}

group = "org.sayandevelopment.sayanvanish.api"

dependencies {
    implementation("net.kyori:adventure-api:4.16.0")
    implementation("net.kyori:adventure-text-minimessage:4.16.0")
    implementation("com.zaxxer:HikariCP:5.1.0")

    implementation("org.sayandevelopment:stickynote-core:1.0.8-SNAPSHOT")

    testImplementation(kotlin("test"))
    testImplementation("org.xerial:sqlite-jdbc:3.45.3.0")
}

tasks.test {
    useJUnitPlatform()
}

publishing {
    publications {
        create<MavenPublication>("shadowJar") {
            artifact(tasks["shadowJar"])
        }
    }
}