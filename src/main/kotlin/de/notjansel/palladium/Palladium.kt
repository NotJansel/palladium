package de.notjansel.palladium

import de.notjansel.palladium.commands.PalladiumCommand
import de.notjansel.palladium.enums.VersionTypes
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import net.kyori.adventure.platform.bukkit.BukkitAudiences
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bstats.bukkit.Metrics
import org.bukkit.plugin.java.JavaPlugin
import java.io.File


class Palladium : JavaPlugin() {

    fun adventure(): BukkitAudiences {
        checkNotNull(adventure) { "Tried to access Adventure when the plugin was disabled!" }
        return adventure
    }

    override fun onEnable() {
        adventure = BukkitAudiences.create(this);
        val metrics: Metrics = Metrics(this, 15555);
        if (!File(getDownloadDirectoryPath()).exists()) {
            File(getDownloadDirectoryPath()).mkdirs()
        }
        instance = this
        val remoteVersion = runBlocking {
            Json.parseToJsonElement(client.get("https://raw.githubusercontent.com/NotJansel/palladium/master/versions.json").body()).jsonObject["latest"].toString()
        }
        getCommand("palladium")!!.executor = PalladiumCommand()
        if (remoteVersion != version || versionType == VersionTypes.DEVELOPMENT) {
            adventure.console().sendMessage(MiniMessage.miniMessage().deserialize("<red>[Palladium] Local Version does not match with remote! Either you need to update or this is a <aqua>development</aqua> build!"));
            adventure.console().sendMessage(MiniMessage.miniMessage().deserialize("<red>Latest Version according to remote: ${remoteVersion}, but $version present!"))
        }
        adventure.console().sendMessage(MiniMessage.miniMessage().deserialize("<yellow>[Palladium] Plugin initialized successfully."));
    }

    override fun onDisable() {
        adventure.close();
    }

    fun getDownloadDirectoryPath(): String {
        return this.dataFolder.absolutePath + "/"
    }

    companion object Things {
        const val version: String = "@version@"
        val versionType: VersionTypes = VersionTypes.DEVELOPMENT
        lateinit var instance: Palladium
        lateinit var adventure: BukkitAudiences
        val client = HttpClient(CIO) {
            install(ContentNegotiation) {
                json(Json { ignoreUnknownKeys = true })
            }
            install(UserAgent) {
                agent = "notjansel/palladium (github@notjansel.de)"
            }
        }
    }


}