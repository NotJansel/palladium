package de.notjansel.palladium.threading

import de.notjansel.palladium.Palladium
import de.notjansel.palladium.enums.FileTypes
import net.kyori.adventure.audience.Audience
import net.kyori.adventure.text.TextComponent
import net.kyori.adventure.text.minimessage.MiniMessage
import java.io.File

class CopyThread(val fileName: String, val fileTypes: FileTypes, private val audience: Audience): Thread() {
    override fun run() {
        if (fileTypes == FileTypes.PLUGIN) {
            File(Palladium.instance.getDownloadDirectoryPath() + fileName).copyTo(File("./plugins/$fileName"), true)
            Palladium.instance.pluginLoader.enablePlugin(Palladium.instance.pluginLoader.loadPlugin(File("./plugins/$fileName")))
        }
        if (fileTypes == FileTypes.DATAPACK) {
            File(Palladium.instance.getDownloadDirectoryPath() + fileName).copyTo(File(Palladium.instance.server.worldContainer.absolutePath + "/datapacks/$fileName"), true)
        }
        Palladium.instance.logger.info("File copied.")
        audience.sendMessage(MiniMessage.miniMessage().deserialize("<green>File copied!"))
    }
}