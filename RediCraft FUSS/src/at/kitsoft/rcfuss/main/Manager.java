package at.kitsoft.rcfuss.main;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginManager;

import at.kitsoft.rcfuss.api.APIs;
import at.kitsoft.rcfuss.api.Serverupdater;
import at.kitsoft.rcfuss.api.TPSMonitor;
import at.kitsoft.rcfuss.command.AFKCMD;
import at.kitsoft.rcfuss.command.ChatClearCMD;
import at.kitsoft.rcfuss.command.ClearLagCMD;
import at.kitsoft.rcfuss.command.FlyCMD;
import at.kitsoft.rcfuss.command.GamemodeCMD;
import at.kitsoft.rcfuss.command.Homesystem;
import at.kitsoft.rcfuss.command.InvseeCMD;
import at.kitsoft.rcfuss.command.PM_System;
import at.kitsoft.rcfuss.command.PingCMD;
import at.kitsoft.rcfuss.command.ScoreboardChangeCMD;
import at.kitsoft.rcfuss.command.ServerhealthCMD;
import at.kitsoft.rcfuss.command.SetspawnCMD;
import at.kitsoft.rcfuss.command.SkullCMD;
import at.kitsoft.rcfuss.command.SkullListCMD;
import at.kitsoft.rcfuss.command.SpawnVillagerCMD;
import at.kitsoft.rcfuss.command.SpeedCMD;
import at.kitsoft.rcfuss.command.TPA_System;
import at.kitsoft.rcfuss.command.TP_CMD;
import at.kitsoft.rcfuss.command.TimeCMD;
import at.kitsoft.rcfuss.command.TopPlaytimeCMD;
import at.kitsoft.rcfuss.command.VanishCMD;
import at.kitsoft.rcfuss.command.WeatherCMD;
import at.kitsoft.rcfuss.command.WorkbenchCMD;
import at.kitsoft.rcfuss.event.AutoAFKKickHandler;
import at.kitsoft.rcfuss.event.BlockerClass;
import at.kitsoft.rcfuss.event.ColorSigns;
import at.kitsoft.rcfuss.event.FullEventList;
import at.kitsoft.rcfuss.event.JobSigns;
import at.kitsoft.rcfuss.event.JoinQuitHandler;
import at.kitsoft.rcfuss.event.MOTDJoin;
import at.kitsoft.rcfuss.event.SBHandler;
import at.kitsoft.rcfuss.event.Serverteleporter;
import at.kitsoft.rcfuss.event.XP_Boost;
import at.kitsoft.rcfuss.mysql.lb.MySQL;

public class Manager {
	
	static File rcfussfolder = new File("plugins/RCFUSS");
	static File mainConfig = new File("plugins/RCFUSS/config.yml");
	static File ptcache = new File("plugins/RCFUSS/ptimecache.yml");
	static File msgf = new File("plugins/RCFUSS/msg.yml");
	static File pdata = new File("plugins/RCFUSS/playerdata.yml");
	
	//preInit will be called first to load various configs, connect to DB
	//init will register all commands and events
	//postInit will be called in the last step for various things like scheduler, etc.
	
	private void createFolders() throws IOException{
		if(!rcfussfolder.exists()) {
			rcfussfolder.mkdir();
		}
		if(!mainConfig.exists()) {
			mainConfig.createNewFile();
		}
		if(!ptcache.exists()) {
			ptcache.createNewFile();
		}
		if(!msgf.exists()) {
			msgf.createNewFile();
		}else {
			msgf.delete();
			msgf.createNewFile();
		}
		if(!pdata.exists()) {
			pdata.createNewFile();
		}
	}
	
	public void preInit() {
		Bukkit.getConsoleSender().sendMessage("§cPreinitialising RCFUSS...");
		long time = System.currentTimeMillis();
		try {
			createFolders();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		YamlConfiguration cfg = YamlConfiguration.loadConfiguration(mainConfig);
		cfg.addDefault("ip", "localhost");
		cfg.addDefault("port", "3306");
		cfg.addDefault("database", "database");
		cfg.addDefault("user", "username");
		cfg.addDefault("password", "password");
		cfg.options().copyDefaults(true);
		try {
			cfg.save(mainConfig);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		HashMap<String, String> sqlcred = new HashMap<>();
		for(String s : cfg.getKeys(true)) {
			sqlcred.put(s, cfg.getString(s));
		}
		MySQL.connect(sqlcred);
		long time_new = System.currentTimeMillis();
		long time_diff = time_new - time;
		Bukkit.getConsoleSender().sendMessage("§aPreinit done. §6Took " + time_diff + " ms.");
	}
	
	public void init() {
		Bukkit.getConsoleSender().sendMessage("§cInitialising RCFUSS...");
		long time = System.currentTimeMillis();
		
		/*
		 *  Homesystem is commented out, as the old MySQL Library is not used in this project anymore
		 *  A partial rewrite is neccessary; however, the base system can be kept as it is.
		 */
		
		Main.instance.getCommand("deletejobsign").setExecutor(new JobSigns());
		Main.instance.getCommand("weather").setExecutor(new WeatherCMD());
		Main.instance.getCommand("vanish").setExecutor(new VanishCMD()); //alias /v
		Main.instance.getCommand("workbench").setExecutor(new WorkbenchCMD()); //alias /wb
		Main.instance.getCommand("afk").setExecutor(new AFKCMD());
		Main.instance.getCommand("chatclear").setExecutor(new ChatClearCMD()); //alias /cc
		Main.instance.getCommand("clearlag").setExecutor(new ClearLagCMD()); //alias /cl
		Main.instance.getCommand("fly").setExecutor(new FlyCMD());
		Main.instance.getCommand("gm").setExecutor(new GamemodeCMD()); //alias /gamemode
		Main.instance.getCommand("delhome").setExecutor(new Homesystem());
		Main.instance.getCommand("home").setExecutor(new Homesystem());
		Main.instance.getCommand("sethome").setExecutor(new Homesystem());
		Main.instance.getCommand("listhomes").setExecutor(new Homesystem());
		Main.instance.getCommand("invsee").setExecutor(new InvseeCMD());
		Main.instance.getCommand("ping").setExecutor(new PingCMD());
		Main.instance.getCommand("msg").setExecutor(new PM_System());
		Main.instance.getCommand("r").setExecutor(new PM_System());
		Main.instance.getCommand("blockmsg").setExecutor(new PM_System());
		Main.instance.getCommand("sb").setExecutor(new ScoreboardChangeCMD());
		Main.instance.getCommand("gc").setExecutor(new ServerhealthCMD());
		Main.instance.getCommand("head").setExecutor(new SkullCMD());
		Main.instance.getCommand("headlist").setExecutor(new SkullListCMD());
		Main.instance.getCommand("spawnvillager").setExecutor(new SpawnVillagerCMD());
		Main.instance.getCommand("speed").setExecutor(new SpeedCMD());
		Main.instance.getCommand("time").setExecutor(new TimeCMD());
		Main.instance.getCommand("topplaytime").setExecutor(new TopPlaytimeCMD());
		Main.instance.getCommand("tpa").setExecutor(new TPA_System());
		Main.instance.getCommand("tpahere").setExecutor(new TPA_System());
		Main.instance.getCommand("tpaccept").setExecutor(new TPA_System());
		Main.instance.getCommand("tpdeny").setExecutor(new TPA_System());
		Main.instance.getCommand("blocktpa").setExecutor(new TPA_System());
		Main.instance.getCommand("tphere").setExecutor(new TP_CMD());
		Main.instance.getCommand("tp").setExecutor(new TP_CMD());
		Main.instance.getCommand("setspawn").setExecutor(new SetspawnCMD());
		Main.instance.getCommand("s").setExecutor(new Serverteleporter(Main.instance));
		
		PluginManager pM = Bukkit.getPluginManager();
		pM.registerEvents(new Serverupdater(), Main.instance);
		pM.registerEvents(new SBHandler(), Main.instance);
		pM.registerEvents(new AutoAFKKickHandler(), Main.instance);
		pM.registerEvents(new JobSigns(), Main.instance);
		pM.registerEvents(new VanishCMD(), Main.instance);
		pM.registerEvents(new AFKCMD(), Main.instance);
		pM.registerEvents(new Homesystem(), Main.instance);
		pM.registerEvents(new SpawnVillagerCMD(), Main.instance);
		pM.registerEvents(new FullEventList(), Main.instance);
		pM.registerEvents(new XP_Boost(), Main.instance);
		pM.registerEvents(new ColorSigns(), Main.instance);
		pM.registerEvents(new BlockerClass(), Main.instance);
		pM.registerEvents(new MOTDJoin(), Main.instance);
		pM.registerEvents(new Serverteleporter(Main.instance), Main.instance);
		pM.registerEvents(new JoinQuitHandler(), Main.instance);
		
		long time_new = System.currentTimeMillis();
		long time_diff = time_new - time;
		Bukkit.getConsoleSender().sendMessage("§aInit done. §6Took " + time_diff + " ms.");
	}
	
	public void postInit() {
		Bukkit.getConsoleSender().sendMessage("§cPostinitialising RCFUSS...");
		long time = System.currentTimeMillis();
		
		TPSMonitor.startTPSMonitor();
		new SBHandler().downloadStrings();
		new SBHandler().sbSched(0, 50, 20);
		APIs api = new APIs();
		api.downloadStrings();
		api.loadConfig();
		
		long time_new = System.currentTimeMillis();
		long time_diff = time_new - time;
		Bukkit.getConsoleSender().sendMessage("§aPostinit done. §6Took " + time_diff + " ms.");
	}

}