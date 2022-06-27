package de.notjansel.palladium.threading

import org.bukkit.command.CommandSender
import java.net.URL

class DownloadThread(val sender: CommandSender, val args: Array<String>) : Thread() {
    override fun run() {
        sleep(5000)
        sender.sendRichMessage("<green>Waited 5 Seconds successfully.")
    }
}