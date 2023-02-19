import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.7.10"
    application
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    // Kord Snapshots Repository (Optional):
    maven("https://oss.sonatype.org/content/repositories/snapshots")
}

application {
    mainClass.set("meteor.Main")
    applicationDefaultJvmArgs = listOf("-Djava.rmi.server.hostname=15.204.174.14")
}

dependencies {
    implementation("dev.kord:kord-core:0.8.x-SNAPSHOT")
    implementation("com.google.code.gson:gson:2.10.1")
    runtimeOnly("org.slf4j:slf4j-simple:2.0.3")
}

tasks.test {
    useJUnitPlatform()
}