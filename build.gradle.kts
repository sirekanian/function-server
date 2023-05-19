plugins {
    val kotlinVersion = "1.8.21"
    kotlin("jvm") version kotlinVersion
    kotlin("plugin.serialization") version kotlinVersion
    application
    id("org.sirekanyan.version-checker") version "1.0.6"
}

group = "org.sirekanyan"
version = "0.1"

repositories {
    mavenCentral()
}

dependencies {
    val ktorVersion = "2.3.0"
    implementation("io.ktor:ktor-server-netty:$ktorVersion")
    implementation("io.ktor:ktor-server-websockets:$ktorVersion")
    implementation("io.ktor:ktor-serialization-kotlinx-json:$ktorVersion")
    implementation("org.slf4j:slf4j-simple:2.0.7")
    testImplementation("junit:junit:4.13.2")
    testImplementation("io.ktor:ktor-client-cio:$ktorVersion")
    testImplementation("io.ktor:ktor-client-websockets:$ktorVersion")
}

kotlin {
    jvmToolchain(11)
}

application {
    mainClass.set("org.sirekanyan.fun.server.Main")
}
