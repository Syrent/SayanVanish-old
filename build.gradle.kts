plugins {
    kotlin("jvm") version "1.9.23"
    java
    `maven-publish`
    id("io.github.goooler.shadow") version "8.1.7"
}

allprojects {
    group = "org.sayndev"
    version = "1.0.0"

    plugins.apply("java")
    plugins.apply("maven-publish")
    plugins.apply("kotlin")
    plugins.apply("io.github.goooler.shadow")

    repositories {
        mavenCentral()
        mavenLocal()

        maven("https://repo.sayandev.org/snapshots")
        maven("https://repo.sayandev.org/releases")
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
            archiveFileName.set("${rootProject.name}-${version}-${this@subprojects.name.removePrefix("sayanvanish-")}.jar")
            destinationDirectory.set(file(rootProject.projectDir.path + "/bin"))
            relocate("org.sayandev.stickynote", "org.sayandev.sayanvanish.lib.stickynote")
            exclude("META-INF/**")
            from("LICENSE")
            minimize()
        }
    }
}

configurations {
    "apiElements" {
        attributes {
            attribute(Usage.USAGE_ATTRIBUTE, project.objects.named(Usage.JAVA_API))
            attribute(Category.CATEGORY_ATTRIBUTE, project.objects.named(Category.LIBRARY))
            attribute(Bundling.BUNDLING_ATTRIBUTE, project.objects.named(Bundling.SHADOWED))
            attribute(LibraryElements.LIBRARY_ELEMENTS_ATTRIBUTE, project.objects.named(LibraryElements.JAR))
            attribute(TargetJvmVersion.TARGET_JVM_VERSION_ATTRIBUTE, 17)
        }
        outgoing.artifact(tasks.named("shadowJar"))
    }
    "runtimeElements" {
        attributes {
            attribute(Usage.USAGE_ATTRIBUTE, project.objects.named(Usage.JAVA_RUNTIME))
            attribute(Category.CATEGORY_ATTRIBUTE, project.objects.named(Category.LIBRARY))
            attribute(Bundling.BUNDLING_ATTRIBUTE, project.objects.named(Bundling.SHADOWED))
            attribute(LibraryElements.LIBRARY_ELEMENTS_ATTRIBUTE, project.objects.named(LibraryElements.JAR))
            attribute(TargetJvmVersion.TARGET_JVM_VERSION_ATTRIBUTE, 17)
        }
        outgoing.artifact(tasks.named("shadowJar"))
    }
    "mainSourceElements" {
        attributes {
            attribute(Usage.USAGE_ATTRIBUTE, project.objects.named(Usage.JAVA_RUNTIME))
            attribute(Category.CATEGORY_ATTRIBUTE, project.objects.named(Category.DOCUMENTATION))
            attribute(Bundling.BUNDLING_ATTRIBUTE, project.objects.named(Bundling.SHADOWED))
            attribute(DocsType.DOCS_TYPE_ATTRIBUTE, project.objects.named(DocsType.SOURCES))
        }
    }
}

publishing {
    publications {
        create<MavenPublication>("shadowJar") {
            from(components["java"])
            setPom(this)
        }
    }
}

fun setPom(publication: MavenPublication) {
    publication.pom {
        name.set("sayanvanish")
        description.set("A modular vanish system for Minecraft servers")
        url.set("https://github.com/syrent/sayanvanish")
        licenses {
            license {
                name.set("GNU General Public License v3.0")
                url.set("https://github.com/syrent/sayanvanish/blob/master/LICENSE")
            }
        }
        developers {
            developer {
                id.set("syrent")
                name.set("abbas")
                email.set("syrent2356@gmail.com")
            }
        }
        scm {
            connection.set("scm:git:github.com/syrent/sayanvanish.git")
            developerConnection.set("scm:git:ssh://github.com/syrent/sayanvanish.git")
            url.set("https://github.com/syrent/sayanvanish/tree/master")
        }
    }
}
