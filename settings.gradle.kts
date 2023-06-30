
rootProject.name = "palladium"

pluginManagement {
    plugins {
        val kotlinVersion = "1.8.22"
        kotlin("jvm") version kotlinVersion
        kotlin("plugin.serialization") version kotlinVersion

        id("com.github.johnrengelman.shadow") version "8.1.1"
    }
}