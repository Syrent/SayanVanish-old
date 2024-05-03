plugins {
    kotlin("jvm")
}

group = "org.sayandevelopment.sayanvanish.api"

dependencies {
    api("org.sayandevelopment:stickynote-core:1.0.0")

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