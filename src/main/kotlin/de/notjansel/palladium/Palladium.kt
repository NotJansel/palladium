package de.notjansel.palladium

import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin

class Palladium : JavaPlugin() {
    override fun onEnable() {
        Bukkit.getConsoleSender().sendRichMessage("<red>Palladium is initializing")
    }

    override fun onDisable() {
        // Plugin shutdown logic
    }
}