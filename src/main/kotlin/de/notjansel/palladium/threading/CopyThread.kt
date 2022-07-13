package de.notjansel.palladium.threading

import de.notjansel.palladium.Palladium
import de.notjansel.palladium.enums.FileTypes
import java.io.File

class CopyThread(val filename: String, val fileTypes: FileTypes): Thread() {
    override fun run() {
        if (fileTypes == FileTypes.PLUGIN) {
            File(Palladium.instance.getDownloadDirectoryPath() + filename).copyTo(File("./plugins/$filename"), true)
        }
        if (fileTypes == FileTypes.DATAPACK) {
            File(Palladium.instance.getDownloadDirectoryPath() + filename).copyTo(File(Palladium.instance.server.worldContainer.absolutePath + "/datapacks/$filename"), true)
        }
    }
}