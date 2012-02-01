package me.Bambusstock.Madis;
import java.util.logging.Logger;
import org.bukkit.plugin.java.JavaPlugin;

public class Madis extends JavaPlugin{
	Logger log = Logger.getLogger("Minecraft");

	public void onEnable() {
		log.info("Madis 0.1 enabled. It's magic!");
		getServer().getPluginManager().registerEvents(new BlockListener(), this);
	}
	
	public void onDisable() {
		log.info("Madis disabled. Magic is gone!");
	}
	
}
