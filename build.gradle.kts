plugins {
    kotlin("jvm") version "1.9.23"
    java
    `maven-publish`
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

allprojects {
    group = "org.sayandevelopment"
    version = "1.0.0-SNAPSHOT"

    plugins.apply("java")
    plugins.apply("maven-publish")
    plugins.apply("kotlin")
    plugins.apply("com.github.johnrengelman.shadow")

    repositories {
        mavenCentral()
        mavenLocal()

        maven("https://repo.sayandevelopment.org/snapshots")
        maven("https://repo.sayandevelopment.org/releases")

        maven("https://maven.pkg.github.com/mohamad82bz/ruom") {
            credentials {
                username = project.findProperty("gpr.user") as String
                password = project.findProperty("gpr.key") as String
            }
        }
    }

    java {
        withJavadocJar()
        withSourcesJar()
    }
}

subprojects {
    dependencies {
        testImplementation(kotlin("test"))

        testImplementation("org.xerial:sqlite-jdbc:3.45.3.0")
        testImplementation("org.apache.logging.log4j:log4j-slf4j2-impl:2.23.1")
    }

    tasks {
        test {
            useJUnitPlatform()
        }

        build {
            dependsOn(shadowJar)
        }

        shadowJar {
            archiveFileName.set("${rootProject.name}-${version}-${this@subprojects.name}.jar")
            destinationDirectory.set(file(rootProject.projectDir.path + "/bin"))
            exclude("META-INF/**")
            from("LICENSE")
            minimize()
        }
    }
}

task("buildAll") {
    subprojects.forEach { project ->
        dependsOn(project.tasks.shadowJar)
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

