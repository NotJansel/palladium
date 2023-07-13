import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
    kotlin("plugin.serialization")
    application

    id("com.github.johnrengelman.shadow")
    id("net.kyori.blossom") version "1.3.1"
}

group = "de.notjansel"
version = "0.11.0-dev"
val ktorVersion = "2.3.2"

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
    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.8.22")
    implementation("org.bstats:bstats-bukkit:3.0.2")
    implementation("com.google.code.gson:gson:2.10.1")
    implementation("io.ktor:ktor-client-core:$ktorVersion")
    implementation("io.ktor:ktor-client-cio:$ktorVersion")
    implementation("io.ktor:ktor-client-content-negotiation:$ktorVersion")
    implementation("io.ktor:ktor-client-serialization:$ktorVersion")
    implementation("io.ktor:ktor-serialization-kotlinx-json:$ktorVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:1.5.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.2")
    testImplementation("org.jetbrains.kotlin:kotlin-test:1.8.22")
    compileOnly("com.destroystokyo.paper:paper-api:1.13-R0.1-SNAPSHOT")
}

tasks {
    withType<KotlinCompile> {
        kotlinOptions.jvmTarget = "17"
    }
    wrapper {
        gradleVersion = "8.2"
        distributionType = Wrapper.DistributionType.BIN
    }
    jar {
        manifest {
            attributes(
                "Main-Class" to "de.notjansel.palladium.PalladiumKt"
            )
        }
    }
    shadowJar {
        relocate("org.bstats", "de.notjansel")
    }
    processResources {
        inputs.property("version", version)
    }
}

blossom {
    replaceToken("@version@", version)
}

application {
    mainClass.set("de.notjansel.palladium.PalladiumKt")
}