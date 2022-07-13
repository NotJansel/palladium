package de.notjansel.palladium.commands

import de.notjansel.palladium.Palladium
import de.notjansel.palladium.enums.FileTypes
import de.notjansel.palladium.errorMessages
import de.notjansel.palladium.threading.CopyThread
import de.notjansel.palladium.threading.DownloadThread
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabExecutor

class PalladiumCommand : TabExecutor, CommandExecutor{

    override fun onCommand(p0: CommandSender, p1: Command, p2: String, p3: Array<String>?): Boolean {
        val audience = Palladium.adventure.sender(p0)
        if (p3?.isEmpty() == true) {
            audience.sendMessage(MiniMessage.miniMessage().deserialize("<red>You bitch give some args first"))
            return true
        }
        when (p3?.first()) {
            "download" -> {
                if (p0.hasPermission("palladium.download") || p0.hasPermission("*") || p0.isOp) {
                    val thread = DownloadThread(p0, p3)
                    thread.start()
                } else {
                    p0.sendMessage("You don't have permission to use this command")
                }
            }
            "copy" -> {
                if (p0.hasPermission("palladium.copy") || p0.hasPermission("*") || p0.isOp) {
                    audience.sendMessage(MiniMessage.miniMessage().deserialize("<green>${p3[1]}"))
                    if (p3[1].endsWith(".jar")) { fileTypes = FileTypes.PLUGIN }
                    if (p3[1].endsWith(".zip")) { fileTypes = FileTypes.DATAPACK }
                    if (!p3[1].endsWith(".jar") || !p3[1].endsWith(".zip")) { audience.sendMessage(MiniMessage.miniMessage().deserialize(errorMessages().invalidFile)) }
                    val thread = CopyThread(p3[1], fileTypes)
                }
            }
        }
        return true
    }

    override fun onTabComplete(
        sender: CommandSender,
        p1: Command,
        p2: String,
        args: Array<out String>?
    ): MutableList<String>? {
        return null;
    }

    companion object {
        lateinit var fileTypes: FileTypes
    }
}