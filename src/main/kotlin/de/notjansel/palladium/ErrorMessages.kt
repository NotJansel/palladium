package de.notjansel.palladium

class ErrorMessages {
    val invalidFile: String = "<red>Invalid Filetype, .jar or .zip required"
    val missingArgsPalladiumCommand: String = "<red>Please give some arguments. Current valid args: \n<green>/palladium download <url> <filename>\n<green>/palladium copy <filename>\n<green>/palladium info"
    val missingPermission: String = "<red>You don't have permission to use this command"
    val updateAvailableOrDevVersion: String = "<red>A Update is available or this is a <aqua>Development</aqua> version!"
}
