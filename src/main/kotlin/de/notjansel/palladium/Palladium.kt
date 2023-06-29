package de.notjansel.palladium

import com.google.gson.JsonObject
import com.google.gson.JsonParser
import de.notjansel.palladium.commands.PalladiumCommand
import de.notjansel.palladium.enums.VersionTypes
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import net.kyori.adventure.platform.bukkit.BukkitAudiences
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bstats.bukkit.Metrics
import org.bukkit.plugin.java.JavaPlugin
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL
import java.nio.file.Files
import java.nio.file.Paths


class Palladium : JavaPlugin() {

    fun adventure(): BukkitAudiences {
        checkNotNull(adventure) { "Tried to access Adventure when the plugin was disabled!" }
        return adventure
    }

    override fun onEnable() {
        adventure = BukkitAudiences.create(this);
        val pluginid: Int = 15555
        val metrics: Metrics = Metrics(this, pluginid);
        if (!File(getDownloadDirectoryPath()).exists()) {
            File(getDownloadDirectoryPath()).mkdirs()
        }
        instance = this
        downloadFile("https://raw.githubusercontent.com/NotJansel/palladium/master/versions.json", "versions.json", getDownloadDirectoryPath())
        getCommand("palladium")!!.executor = PalladiumCommand()
        val obj: JsonObject = try {
            JsonParser.parseString(Files.readString(Paths.get(getDownloadDirectoryPath() + "versions.json"))).asJsonObject
        } catch (e: IOException) {
            throw RuntimeException(e)
        }
        val fileVersion = obj["latest"]
        if (fileVersion.asString != version || versiontype == VersionTypes.DEVELOPMENT) {
            adventure.console().sendMessage(MiniMessage.miniMessage().deserialize("<red>[Palladium] Local Version does not match with remote! Either you need to update or this is a <aqua>development</aqua> build!"));
            adventure.console().sendMessage(MiniMessage.miniMessage().deserialize("<red>Latest Version according to remote: $fileVersion, but $version present!"))
        }
        adventure.console().sendMessage(MiniMessage.miniMessage().deserialize("<yellow>[Palladium] Plugin initialized successfully."));
    }

    override fun onDisable() {
        adventure.close();
    }

    private fun downloadFile(url: String, file: String, path: String) {
        val f = File(file)
        if (f.exists()) {
            f.delete()
        }
        val dl = URL(url).openConnection() as HttpURLConnection
        dl.connect()
        val input = dl.inputStream
        val output = FileOutputStream(path + file)
        val buffer = ByteArray(1024)
        var len = input.read(buffer)
        while (len > 0) {
            output.write(buffer, 0, len)
            len = input.read(buffer)
        }
        output.close()
        input.close()
    }

    fun getDownloadDirectoryPath(): String {
        return this.dataFolder.absolutePath + "/"
    }

    companion object Things {
        val version: String = "0.10.2"
        val versiontype: VersionTypes = VersionTypes.DEVELOPMENT
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