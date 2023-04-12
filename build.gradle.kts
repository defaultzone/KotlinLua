plugins {
    kotlin("jvm") version "1.8.0"
    id("com.github.johnrengelman.shadow") version "7.0.0"
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

shadow {
    dependencies {
        // To build .jar library that included all dependencies need to use execute gradlew with option "shadowJar".
        // That will create .jar library that have all dependencies by path $ROOT_PROJECT/build/libs/NAME_OF_LIBRARY.jar
        implementation("org.yaml:snakeyaml:2.0")
        implementation("org.json:json:20230227")
    }
}