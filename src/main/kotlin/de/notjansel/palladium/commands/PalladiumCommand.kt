package de.notjansel.palladium.commands

import de.notjansel.palladium.Palladium
import de.notjansel.palladium.enums.DebugTypes
import de.notjansel.palladium.enums.FileTypes
import de.notjansel.palladium.errorMessages
import de.notjansel.palladium.threading.CopyThread
import de.notjansel.palladium.threading.DebugThread
import de.notjansel.palladium.threading.DownloadThread
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabExecutor
import org.jetbrains.annotations.Debug

class PalladiumCommand : TabExecutor, CommandExecutor{

    override fun onCommand(p0: CommandSender, p1: Command, p2: String, p3: Array<String>?): Boolean {
        val audience = Palladium.adventure.sender(p0)
        if (p3?.isEmpty() == true) {
            audience.sendMessage(MiniMessage.miniMessage().deserialize(errorMessages().missingArgsPalladiumCommand))
            return true
        }
        when (p3?.first()) {
            "download" -> {
                if (p0.hasPermission("palladium.download") || p0.hasPermission("*") || p0.isOp) {
                    val thread = DownloadThread(p0, p3)
                    thread.start()
                } else {
                    audience.sendMessage(MiniMessage.miniMessage().deserialize(errorMessages().missingPermission))
                }
            }
            "copy" -> {
                if (p0.hasPermission("palladium.copy") || p0.hasPermission("*") || p0.isOp) {
                    if (p3[1].endsWith(".jar")) { fileTypes = FileTypes.PLUGIN }
                    if (p3[1].endsWith(".zip")) { fileTypes = FileTypes.DATAPACK }
                    if (!p3[1].endsWith(".jar") && !p3[1].endsWith(".zip")) { audience.sendMessage(MiniMessage.miniMessage().deserialize(errorMessages().invalidFile)); return true }
                    if (p3[2] == "debug") {
                        val thread = DebugThread(DebugTypes.THREADINGTEST)
                        thread.start()
                        return true
                    }
                    val thread = CopyThread(p3[1], fileTypes)
                    thread.start()
                } else {
                    audience.sendMessage(MiniMessage.miniMessage().deserialize(errorMessages().missingPermission))
                }
            }
            "info" -> {
                if (p0.hasPermission("palladium.info") || p0.hasPermission("*") || p0.isOp) {
                    audience.sendMessage(MiniMessage.miniMessage().deserialize("<yellow> -----[ Palladium ]-----"))
                    audience.sendMessage(MiniMessage.miniMessage().deserialize("<yellow>Version: <#32cd32>${Palladium.Things.version}"))
                    audience.sendMessage(MiniMessage.miniMessage().deserialize("<yellow>GitHub: <blue><click:open_url:https://github.com/NotJansel/Palladium><hover:show_text:'<blue>Click to open the Repository in your browser'>Click here</hover></click>"))
                    audience.sendMessage(MiniMessage.miniMessage().deserialize("<yellow>Author: <#32cd32>NotJansel"))
                    audience.sendMessage(MiniMessage.miniMessage().deserialize("<yellow>Contributors: <#32cd32>None yet! Help the Project to be better by committing some code!"))
                    audience.sendMessage(MiniMessage.miniMessage().deserialize("<yellow> -----[ Palladium ]-----"))
                } else {
                    audience.sendMessage(MiniMessage.miniMessage().deserialize(errorMessages().missingPermission))
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
        val list: ArrayList<String> = ArrayList()
        if (args?.size == 1) {
            list.add("info")
            list.add("download")
            list.add("copy")
        }
        return list
    }

    companion object {
        lateinit var fileTypes: FileTypes
    }
}