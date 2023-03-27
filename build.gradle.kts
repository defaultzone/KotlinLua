plugins {
    kotlin("jvm") version "1.8.0"
    application
}

group = "me.dzone"
version = "0.1-PREVIEW"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))

    // For converting YAML/JSON to Lua table.
    implementation("org.yaml:snakeyaml:2.0")
    implementation("org.json:json:20230227")
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(8)
}

application {
    mainClass.set("MainKt")
}