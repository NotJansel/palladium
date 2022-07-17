package de.notjansel.palladium.threading

import de.notjansel.palladium.Palladium
import de.notjansel.palladium.enums.DebugTypes

class DebugThread(val debugType: DebugTypes): Thread() {
    override fun run() {
        if (debugType == DebugTypes.THREADINGTEST) {
            sleep(5000)
            Palladium.instance.server.logger.info("Debug Thread was executed successfully")
        }
    }
}