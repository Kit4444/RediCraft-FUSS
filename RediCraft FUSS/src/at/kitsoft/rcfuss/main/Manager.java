package at.kitsoft.rcfuss.main;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;

public class Manager {
	
	//preInit will be called first to load various configs, connect to DB
	//init will register all commands and events
	//postInit will be called in the last step for various things like scheduler, etc.
	public void preInit() {
		Bukkit.getConsoleSender().sendMessage("§cPreinitialising RCFUSS...");
		long time = System.currentTimeMillis();
		
		File rcfussfolder = new File("RCFUSS");
		if(!rcfussfolder.exists()) {
			rcfussfolder.mkdir();
		}
		File mainConfig = new File("RCFUSS/config.yml");
		if(mainConfig.exists()) {
			try {
				mainConfig.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		YamlConfiguration cfg = YamlConfiguration.loadConfiguration(mainConfig);
		
		long time_new = System.currentTimeMillis();
		long time_diff = time_new - time;
		Bukkit.getConsoleSender().sendMessage("§aPreinit done. §6Took " + time_diff + " ms.");
	}
	
	public void init() {
		Bukkit.getConsoleSender().sendMessage("§cInitialising RCFUSS...");
		long time = System.currentTimeMillis();
		
		long time_new = System.currentTimeMillis();
		long time_diff = time_new - time;
		Bukkit.getConsoleSender().sendMessage("§aInit done. §6Took " + time_diff + " ms.");
	}
	
	public void postInit() {
		Bukkit.getConsoleSender().sendMessage("§cPostinitialising RCFUSS...");
		long time = System.currentTimeMillis();
		
		long time_new = System.currentTimeMillis();
		long time_diff = time_new - time;
		Bukkit.getConsoleSender().sendMessage("§aPostinit done. §6Took " + time_diff + " ms.");
	}

}