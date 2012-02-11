package me.Bambusstock.Madis;
import java.util.logging.Logger;

import org.bukkit.plugin.java.JavaPlugin;

public class Madis extends JavaPlugin{
    Logger log = Logger.getLogger("Minecraft");
    
    public void onEnable() {
	getServer().getPluginManager().registerEvents(new BlockListener(this), this);
	log.info("Madis 0.3 enabled. It's magic!");
    }

    public void onDisable() {
	log.info("Madis disabled. Magic is gone!");
    }

}
