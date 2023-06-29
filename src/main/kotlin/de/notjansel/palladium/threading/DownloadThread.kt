package de.notjansel.palladium.threading

import de.notjansel.palladium.Palladium
import de.notjansel.palladium.commands.PalladiumCommand
import io.ktor.client.*
import org.bukkit.command.CommandSender
import java.io.File
import java.io.FileOutputStream
import java.net.URL

class DownloadThread(val sender: CommandSender, val args: Array<String>) : Thread() {
    override fun run() {
        if (args.size < 3) {
            sender.sendMessage("Usage: /palladium download <url> <filename>")
            return
        }
        val url = URL(args[1])
        val filename = args[2]
        val connection = url.openConnection()
        connection.connect()
        val length = connection.contentLength
        val input = url.openStream()
        val output = File(Palladium.instance.getDownloadDirectoryPath() + filename)
        output.createNewFile()
        val buffer = ByteArray(1024)
        var bytesRead = input.read(buffer)
        val outputStream = FileOutputStream(output)
        while (bytesRead > 0) {
            outputStream.write(buffer, 0, bytesRead)
            bytesRead = input.read(buffer)
        }
        outputStream.close()
        input.close()
        sender.sendMessage("Downloaded $filename (${length / 1024} kB)")
    }
}