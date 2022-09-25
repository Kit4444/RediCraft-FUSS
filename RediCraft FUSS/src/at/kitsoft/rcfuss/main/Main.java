package at.kitsoft.rcfuss.main;

import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin{
	
	public static Main instance;
	public static String mysql_prefix = "§aMYSQL";
	
	public void onEnable() {
		instance = this;
	}
	
	public void onDisable() {
		instance = null;
	}

}
