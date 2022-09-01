package de.notjansel.palladium.threading

import de.notjansel.palladium.Palladium
import de.notjansel.palladium.enums.DebugTypes

class DebugThread(val debugType: DebugTypes): Thread() {
    override fun run() {
        if (debugType == DebugTypes.THREADINGTEST) {
            sleep(5000)
            Palladium.instance.server.logger.info(Palladium.instance.server.ip)
            Palladium.instance.server.logger.info(Palladium.instance.server.version)
            Palladium.instance.server.logger.info(Palladium.instance.server.bukkitVersion)
            Palladium.instance.server.logger.info(Palladium.instance.server.motd)
            Palladium.instance.server.logger.info(Palladium.instance.server.name)
            Palladium.instance.server.logger.info("Debug Thread was executed successfully")
        }
    }
}