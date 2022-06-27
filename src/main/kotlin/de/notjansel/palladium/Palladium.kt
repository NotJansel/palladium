package de.notjansel.palladium

import de.notjansel.palladium.commands.PalladiumCommand
import de.notjansel.palladium.enums.VersionTypes
import org.bstats.bukkit.Metrics
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin
import java.io.File
import java.io.FileOutputStream
import java.net.HttpURLConnection
import java.net.URL
import java.util.logging.Level
import java.util.logging.Logger

class Palladium : JavaPlugin() {
    override fun onEnable() {
        val pluginid: Int = 15555
        val metrics: Metrics = Metrics(this, pluginid);
        if (!File(getDownloadDirectoryPath()).exists()) {
            File(getDownloadDirectoryPath()).mkdirs()
        }
        val logger: Logger = this.logger;
        logger.log(Level.INFO, "Palladium enabled!");
        Bukkit.getConsoleSender().sendRichMessage(getDownloadDirectoryPath())
        DownloadFile("https://raw.githubusercontent.com/NotJansel/palladium/master/versions.json", "versions.json", getDownloadDirectoryPath())
        getCommand("palladium")!!.setExecutor(PalladiumCommand())
    }

    override fun onDisable() {
        // Plugin shutdown logic
    }

    fun DownloadFile(url: String, file: String, path: String) {
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

    object things{
        val version: String = "0.10.0-SNAPSHOT"
        val versiontype: VersionTypes = VersionTypes.DEVELOPMENT
    }
}