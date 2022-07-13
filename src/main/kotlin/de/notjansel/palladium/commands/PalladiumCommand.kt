package de.notjansel.palladium.commands

import de.notjansel.palladium.Palladium
import de.notjansel.palladium.threading.DownloadThread
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.MiniMessage
import net.md_5.bungee.api.chat.BaseComponent
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabExecutor

class PalladiumCommand : TabExecutor, CommandExecutor{

    override fun onCommand(p0: CommandSender, p1: Command, p2: String, p3: Array<String>?): Boolean {
        if (p3?.isEmpty() == true) {
            Palladium.adventure.sender(p0).sendMessage(MiniMessage.miniMessage().deserialize("<red>You bitch give some args first"))
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