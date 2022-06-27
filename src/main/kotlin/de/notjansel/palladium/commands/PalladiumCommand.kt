package de.notjansel.palladium.commands

import de.notjansel.palladium.threading.DownloadThread
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabExecutor

class PalladiumCommand : TabExecutor, CommandExecutor{

    override fun onCommand(p0: CommandSender, p1: Command, p2: String, p3: Array<String>?): Boolean {
        when (p3?.first()) {
            "download" -> {
                if (p0.hasPermission("palladium.download")) {
                    val thread = DownloadThread(p0, p3)
                    thread.start()
                } else {
                    p0.sendMessage("You don't have permission to use this command")
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
}