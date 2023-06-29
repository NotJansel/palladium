import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
    kotlin("plugin.serialization")
    application

    id("com.github.johnrengelman.shadow")
}

group = "de.notjansel"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    google()
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    testImplementation(kotlin("test"))

    implementation("net.kyori:adventure-api:4.14.0")
    implementation("net.kyori:adventure-text-minimessage:4.14.0")
    implementation("net.kyori:adventure-platform-bukkit:4.3.0")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.8.21")
    implementation("org.bstats:bstats-bukkit:3.0.2")
    implementation("com.google.code.gson:gson:2.10.1")
    implementation("io.ktor:ktor-client-core:2.3.1")
    implementation("io.ktor:ktor-client-cio:2.3.1")
    implementation("io.ktor:ktor-client-content-negotiation:2.3.1")
    implementation("io.ktor:ktor-client-serialization:2.3.1")
    implementation("io.ktor:ktor-serialization-kotlinx-json:2.3.1")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:1.5.1")
    testImplementation("org.jetbrains.kotlin:kotlin-test:1.8.21")
    compileOnly("com.destroystokyo.paper:paper-api:1.13-R0.1-SNAPSHOT")
}

tasks {
    withType<KotlinCompile> {
        kotlinOptions.jvmTarget = "17"
    }
    wrapper {
        gradleVersion = "8.1.1"
        distributionType = Wrapper.DistributionType.BIN
    }
    jar {
        manifest {
            attributes(
                "Main-Class" to "de.notjansel.palladium.PalladiumKt"
            )
        }
    }
}

application {
    mainClass.set("de.notjansel.palladium.PalladiumKt")
}