package de.notjansel.palladium.commands

import com.google.gson.JsonObject
import com.google.gson.JsonParser
import de.notjansel.palladium.Palladium
import de.notjansel.palladium.enums.FileTypes
import de.notjansel.palladium.enums.VersionTypes
import de.notjansel.palladium.errorMessages
import io.ktor.client.request.*
import io.ktor.util.*
import io.ktor.util.cio.*
import io.ktor.utils.io.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabExecutor
import java.io.File
import java.io.IOException
import java.net.URI
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import kotlin.io.path.fileSize
import kotlin.time.Duration

class PalladiumCommand : TabExecutor, CommandExecutor{

    @OptIn(InternalAPI::class)
    override fun onCommand(p0: CommandSender, p1: Command, p2: String, p3: Array<String>?): Boolean {
        runBlocking {
            val audience = Palladium.adventure.sender(p0)
            if (p3?.isEmpty() == true) {
                audience.sendMessage(MiniMessage.miniMessage().deserialize(errorMessages().missingArgsPalladiumCommand))
                return@runBlocking true
            }
            when (p3?.first()) {
                "download" -> {
                    if (p0.hasPermission("palladium.download") || p0.hasPermission("*") || p0.isOp) {
                        runBlocking {
                            Palladium.client.get(p3[1]) {}.content.copyAndClose(File(Palladium.instance.getDownloadDirectoryPath() + p3[2]).writeChannel())
                        }
                        audience.sendMessage(MiniMessage.miniMessage().deserialize("<yellow>[Palladium] <green>Downloaded File (${File(Palladium.instance.getDownloadDirectoryPath() + p3[2]).toPath().fileSize()/1024} kB)"))
                        Palladium.instance.logger.info("Downloaded ${p3[2]} (requested by ${p0.name})")

                    } else {
                        audience.sendMessage(MiniMessage.miniMessage().deserialize(errorMessages().missingPermission))
                    }
                }

                "copy" -> {
                    if (p0.hasPermission("palladium.copy") || p0.hasPermission("*") || p0.isOp) {
                        if (p3[1].endsWith(".jar")) {
                            launch{
                                File(Palladium.instance.getDownloadDirectoryPath() + p3[1])
                                    .copyTo(File("./plugins/${p3[1]}"), true)
                                audience.sendMessage(MiniMessage.miniMessage().deserialize("<red>Restart the Server for the plugin to be enabled. ('/reload confirm' may break stuff)"))
                            }
                        }
                        if (p3[1].endsWith(".zip")) {
                            launch {
                                File(Palladium.instance.getDownloadDirectoryPath() + p3[1])
                                    .copyTo(File(Palladium.instance.server.worldContainer.absolutePath + "/datapacks/${p3[1]}"), true)
                            }
                        }
                        if (!p3[1].endsWith(".jar") && !p3[1].endsWith(".zip") && p3[1] != "debug") {
                            audience.sendMessage(
                                MiniMessage.miniMessage().deserialize(errorMessages().invalidFile)
                            ); return@runBlocking true
                        }
                        if (p3.last() == "debug") {
                            launch {
                                delay(Duration.parse("5s"))
                                audience.sendMessage(MiniMessage.miniMessage().deserialize("<green>Debug-thread executed."))
                            }
                            launch {
                                delay(Duration.parse("5s"))
                                Palladium.instance.server.logger.info(Palladium.instance.server.ip)
                                Palladium.instance.server.logger.info(Palladium.instance.server.version)
                                Palladium.instance.server.logger.info(Palladium.instance.server.bukkitVersion)
                                Palladium.instance.server.logger.info(Palladium.instance.server.motd)
                                Palladium.instance.server.logger.info(Palladium.instance.server.name)
                                Palladium.instance.server.logger.info("Debug Thread was executed successfully")
                            }
                            return@runBlocking true
                        }
                    } else {
                        audience.sendMessage(MiniMessage.miniMessage().deserialize(errorMessages().missingPermission))
                    }
                }

                "info" -> {
                    if (p0.hasPermission("palladium.info") || p0.hasPermission("*") || p0.isOp) {

                        audience.sendMessage(
                            MiniMessage.miniMessage().deserialize("<yellow> ----------[ Palladium ]----------")
                        )
                        audience.sendMessage(
                            MiniMessage.miniMessage()
                                .deserialize("<yellow>Version: <#32cd32>${Palladium.Things.version} ${isUpdateOrDev()}")
                        )
                        audience.sendMessage(
                            MiniMessage.miniMessage()
                                .deserialize("<yellow>GitHub: <blue><click:open_url:https://github.com/NotJansel/Palladium><hover:show_text:'<blue>Click to open the Repository in your browser'>Click here</hover></click>")
                        )
                        audience.sendMessage(
                            MiniMessage.miniMessage().deserialize("<yellow>Author: <#32cd32>NotJansel")
                        )
                        audience.sendMessage(
                            MiniMessage.miniMessage()
                                .deserialize("<yellow>Contributors: <#32cd32>None yet! Help the Project to be better by committing some code!")
                        )
                        audience.sendMessage(
                            MiniMessage.miniMessage()
                                .deserialize("<yellow>Sponsor links: <#FF4800><click:open_url:https://ko-fi.com/jansel>Ko-Fi</click>")
                        )
                        audience.sendMessage(
                            MiniMessage.miniMessage().deserialize("<yellow> ----------[ Palladium ]----------")
                        )
                    } else {
                        audience.sendMessage(MiniMessage.miniMessage().deserialize(errorMessages().missingPermission))
                    }
                }
            }
            return@runBlocking true
        }
        return true
    }

    override fun onTabComplete(
        sender: CommandSender,
        p1: Command,
        p2: String,
        args: Array<out String>?
    ): MutableList<String>? {
        val list: ArrayList<String> = ArrayList()
        if (args?.size == 1) {
            list.add("info")
            list.add("download")
            list.add("copy")
        }
        return list
    }

    fun isUpdateOrDev(): String {
        val obj: JsonObject = try {
            JsonParser.parseString(Files.readString(Paths.get(Palladium.Things.instance.getDownloadDirectoryPath() + "versions.json"))).asJsonObject
        } catch (e: IOException) {
            throw RuntimeException(e)
        }
        val fileversion = obj["latest"]
        if (fileversion.asString != Palladium.version || Palladium.Things.versionType == VersionTypes.DEVELOPMENT) {
            return "<hover:show_text:'${errorMessages().updateAvailableOrDevVersion}'><red>(Info)</red></hover>"
        }
        return ""
    }

    companion object {
        lateinit var fileTypes: FileTypes
    }
}