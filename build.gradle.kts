import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    id("java")
    id("com.github.johnrengelman.shadow") version "7.0.0"
    id("maven-publish")
}

group = "dev.kyriji"
version = "0.0.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation("redis.clients:jedis:5.2.0")
}

tasks.named<ShadowJar>("shadowJar") {
    archiveClassifier.set("")
}

publishing {
    publications {
        create<MavenPublication>("shadow") {
            project.shadow.component(this)

            groupId = project.group.toString()
            artifactId = project.name
            version = project.version.toString()
        }
    }
}