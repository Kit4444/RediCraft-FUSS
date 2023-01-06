package at.kitsoft.rcfuss.api;

import java.io.File;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import at.kitsoft.rcfuss.mysql.lb.MySQL;
import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;

public class Serverupdater implements Listener{
	
	public void updateServer() {
		if(MySQL.isConnected()) {
			userUpdater();
			updateWorlds();
			APIs api = new APIs();
			Runtime runtime = Runtime.getRuntime();
			long ramusage = (runtime.totalMemory() - runtime.freeMemory()) / 1048576L;
			long ramtotal = runtime.totalMemory() / 1048576L;
			int players = Bukkit.getOnlinePlayers().size();
			int pmax = Bukkit.getMaxPlayers();
			long ts = System.currentTimeMillis();
			SimpleDateFormat time = new SimpleDateFormat("dd/MM/yy - HH:mm:ss");
		    String stime = time.format(new Date());
		    
		    String tps = TPSMonitor.getTPSasString();
			int staffs = 0;
			for(Player all : Bukkit.getOnlinePlayers()) {
				if(isStaff(all)) {
					if(isLoggedin(all)) {
						staffs++;
					}
				}
			}
			boolean dmap = false;
			if(Bukkit.getPluginManager().isPluginEnabled("dynmap")) {
				dmap = true;
			}
			boolean hybrid = false;
			
		    try {
		    	PreparedStatement ps = MySQL.getConnection().prepareStatement("UPDATE redicore_serverstats SET ramusage = ?, serverid = ?, currPlayers = ?, maxPlayers = ?, lastupdateTS = ?, lastupdateST = ?, ramavailable = ?, version = ?, tps = ?, currStaffmembers = ?, dynmap = ?, hybrid = ? WHERE servername = ?");
		    	ps.setInt(1, (int) ramusage);
				ps.setString(2, api.getServerId());
				ps.setInt(3, players);
				ps.setInt(4, pmax);
				ps.setInt(5, (int) ts);
				ps.setString(6, stime);
				ps.setInt(7, (int) ramtotal);
				ps.setString(8, "1.19.2");
				ps.setString(9, tps);
				ps.setInt(10, staffs);
				ps.setBoolean(11, dmap);
				ps.setBoolean(12, hybrid);
				ps.setString(13, api.getServerName());
				ps.executeUpdate();
				ps.close();
		    }catch (SQLException e) { e.printStackTrace(); Bukkit.getConsoleSender().sendMessage("§cCan't update DB-Stats."); }
		}else {
			Bukkit.getConsoleSender().sendMessage("§cDB is not connected.");
		}
	}
	
	static int secs = 0;
	
	public void userUpdater() {
		APIs api = new APIs();
		for(Player all : Bukkit.getOnlinePlayers()) {
			String uuid = all.getUniqueId().toString().replace("-", "");
			try {
				PermissionUser pu = PermissionsEx.getUser(all);
	    		PreparedStatement ps = MySQL.getConnection().prepareStatement("UPDATE redicore_userstats SET userrank = ?, online = ?, server = ?, isstaff = ?, rankcolor = ?WHERE uuid = ?");
	    		if(pu.inGroup("pman")) {
	    			ps.setString(1, "Project Manager");
	    			ps.setString(5, "#7c4dff");
	    		}else if(pu.inGroup("sman")) {
	    			ps.setString(1, "Staff Manager");
	    			ps.setString(5, "#fc9403");
	    		}else if(pu.inGroup("gmmman")) {
	    			ps.setString(1, "Game Moderation Manager");
	    			ps.setString(5, "#d32f2f");
	    		}else if(pu.inGroup("dev")) {
	    			ps.setString(1, "Developer");
	    			ps.setString(5, "#aa00aa");
	    		}else if(pu.inGroup("gman")) {
	    			ps.setString(1, "General Manager");
	    			ps.setString(5, "#89cff0");
	    		}else if(pu.inGroup("sda")) {
	    			ps.setString(1, "Service & Data Analyst");
	    			ps.setString(5, "#189192");
	    		}else if(pu.inGroup("cm")) {
	    			ps.setString(1, "Community Moderator");
	    			ps.setString(5, "#00aa00");
	    		}else if(pu.inGroup("ct")) {
	    			ps.setString(1, "Content Team");
	    			ps.setString(5, "#55ff55");
	    		}else if(pu.inGroup("st")) {
	    			ps.setString(1, "Support Team");
	    			ps.setString(5, "#ffff55");
	    		}else if(pu.inGroup("bd")) {
	    			ps.setString(1, "Builder");
	    			ps.setString(5, "#55ffff");
	    		}else if(pu.inGroup("gm")) {
	    			ps.setString(1, "Game Moderator");
	    			ps.setString(5, "#f44336");
	    		}else if(pu.inGroup("aot")) {
	    			ps.setString(1, "AddOn-Team");
	    			ps.setString(5, "#ff55ff");
	    		}else if(pu.inGroup("train")) {
	    			ps.setString(1, "Trainee");
	    			ps.setString(5, "#1497eb");
	    		}else if(pu.inGroup("rltm")) {
	    			ps.setString(1, "Retired Legend");
	    			ps.setString(5, "#00aaaa");
	    		}else if(pu.inGroup("rtm")) {
	    			ps.setString(1, "Retired Team Member");
	    			ps.setString(5, "#00aaaa");
	    		}else if(pu.inGroup("part")) {
	    			ps.setString(1, "Partner");
	    			ps.setString(5, "#00e274");
	    		}else if(pu.inGroup("fs")) {
	    			ps.setString(1, "Forum Supporter");
	    			ps.setString(5, "#f47fff");
	    		}else if(pu.inGroup("nb")) {
	    			ps.setString(1, "Nitro Booster");
	    			ps.setString(5, "#f47fff");
	    		}else if(pu.inGroup("bt")) {
	    			ps.setString(1, "Beta Tester");
	    			ps.setString(5, "#b64dff");
	    		}else if(pu.inGroup("friend")) {
	    			ps.setString(1, "Friend");
	    			ps.setString(5, "#aaaaaa");
	    		}else if(pu.inGroup("vip")) {
	    			ps.setString(1, "VIP");
	    			ps.setString(5, "#b38c3d");
	    		}else if(pu.inGroup("default")) {
	    			ps.setString(1, "Player");
	    			ps.setString(5, "#ffffff");
	    		}
	    		ps.setBoolean(2, true);
	    		ps.setString(3, api.getServerName());
	    		if(all.hasPermission("mlps.isStaff")) {
	    			ps.setBoolean(4, true);
	    		}else {
	    			ps.setBoolean(4, false);
	    		}
	    		ps.setString(6, uuid);
	    		ps.executeUpdate();
	    		ps.close();
			}catch (SQLException e) {
				
			}
		}
	}
	
	private boolean isStaff(Player p) {
		try {
			PreparedStatement ps = MySQL.getConnection().prepareStatement("SELECT isstaff FROM redicore_userstats WHERE uuid = ?");
			ps.setString(1, p.getUniqueId().toString().replace("-", ""));
			ResultSet rs = ps.executeQuery();
			rs.next();
			boolean staff = rs.getBoolean("isstaff");
			return staff;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	private boolean isLoggedin(Player p) {
		try {
			PreparedStatement ps = MySQL.getConnection().prepareStatement("SELECT loggedin FROM redicore_userstats WHERE uuid = ?");
			ps.setString(1, p.getUniqueId().toString().replace("-", ""));
			ResultSet rs = ps.executeQuery();
			rs.next();
			boolean staff = rs.getBoolean("loggedin");
			ps.close();
			rs.close();
			return staff;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public void updateWorlds() {
		APIs api = new APIs();
		String server = api.getServerName();
		for(World w : Bukkit.getWorlds()) {
			HashMap<String, Object> hm = new HashMap<>();
			hm.put("server", server);
			hm.put("world", w.getName());
			String weather = "";
			if(w.isThundering()) {
				weather = "thunder";
			}else if(w.hasStorm()) {
				weather = "rain";
			}else {
				weather = "clear";
			}
			String time = parseTimeWorld(w.getTime());
			int players = w.getPlayers().size();
			try {
				PreparedStatement ps = MySQL.getConnection().prepareStatement("SELECT * FROM redicore_worldsettings WHERE server = ? AND world = ?");
				ps.setString(1, server);
				ps.setString(2, w.getName());
				ResultSet rs = ps.executeQuery();
				if(rs.next()) {
					PreparedStatement ps1 = MySQL.getConnection().prepareStatement("UPDATE redicore_worldsettings SET weather = ?, time = ?, players = ? WHERE server = ? AND world = ?");
					ps1.setString(1, weather);
					ps1.setString(2, time);
					ps1.setInt(3, players);
					ps1.setString(4, server);
					ps1.setString(5, w.getName());
					ps1.executeUpdate();
					ps1.close();
				}else {
					PreparedStatement ps1 = MySQL.getConnection().prepareStatement("INSERT INTO redicore_worldsettings(server, serverid, serverport, world, weather, time, players) VALUES (?, ?, ?, ?, ?, ?, ?)");
					ps1.setString(1, server);
					ps1.setString(2, api.getServerId());
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void Serverrestarter() {
		secs++;
		if(secs == 59) {
			secs = 0;
			System.gc();
		}
		APIs api = new APIs();
		SimpleDateFormat time = new SimpleDateFormat("HH:mm:ss");
	    String stime = time.format(new Date());
	    if(stime.equals("20:00:00")) {
	    	for(Player all : Bukkit.getOnlinePlayers()) {
	    		all.sendMessage(api.returnStringReady(all, "restarter.time").replace("%time_de", "4 Stunden").replace("%time_en", "4 hours").replace("%prefix", api.getPrefix(PrefixType.MAIN)));
	    	}
	    }else if(stime.equals("21:00:00")) {
	    	for(Player all : Bukkit.getOnlinePlayers()) {
	    		all.sendMessage(api.returnStringReady(all, "restarter.time").replace("%time_de", "3 Stunden").replace("%time_en", "3 hours").replace("%prefix", api.getPrefix(PrefixType.MAIN)));
	    	}
	    }else if(stime.equals("22:00:00")) {
	    	for(Player all : Bukkit.getOnlinePlayers()) {
	    		all.sendMessage(api.returnStringReady(all, "restarter.time").replace("%time_de", "2 Stunden").replace("%time_en", "2 hours").replace("%prefix", api.getPrefix(PrefixType.MAIN)));
	    	}
	    }else if(stime.equals("22:30:00")) {
	    	for(Player all : Bukkit.getOnlinePlayers()) {
	    		all.sendMessage(api.returnStringReady(all, "restarter.time").replace("%time_de", "90 Minuten").replace("%time_en", "90 minutes").replace("%prefix", api.getPrefix(PrefixType.MAIN)));
	    	}
	    }else if(stime.equals("23:00:00")) {
	    	for(Player all : Bukkit.getOnlinePlayers()) {
	    		all.sendMessage(api.returnStringReady(all, "restarter.time").replace("%time_de", "60 Minuten").replace("%time_en", "60 minutes").replace("%prefix", api.getPrefix(PrefixType.MAIN)));
	    	}
	    }else if(stime.equals("23:30:00")) {
	    	for(Player all : Bukkit.getOnlinePlayers()) {
	    		all.sendMessage(api.returnStringReady(all, "restarter.time").replace("%time_de", "30 Minuten").replace("%time_en", "30 minutes").replace("%prefix", api.getPrefix(PrefixType.MAIN)));
	    	}
	    }else if(stime.equals("23:45:00")) {
	    	for(Player all : Bukkit.getOnlinePlayers()) {
	    		all.sendMessage(api.returnStringReady(all, "restarter.time").replace("%time_de", "15 Minuten").replace("%time_en", "15 minutes").replace("%prefix", api.getPrefix(PrefixType.MAIN)));
	    	}
	    }else if(stime.equals("23:55:00")) {
	    	for(Player all : Bukkit.getOnlinePlayers()) {
	    		all.sendMessage(api.returnStringReady(all, "restarter.time").replace("%time_de", "5 Minuten").replace("%time_en", "5 minutes").replace("%prefix", api.getPrefix(PrefixType.MAIN)));
	    	}
	    }else if(stime.equals("23:56:00")) {
	    	for(Player all : Bukkit.getOnlinePlayers()) {
	    		all.sendMessage(api.returnStringReady(all, "restarter.time").replace("%time_de", "4 Minuten").replace("%time_en", "4 minutes").replace("%prefix", api.getPrefix(PrefixType.MAIN)));
	    	}
	    }else if(stime.equals("23:57:00")) {
	    	for(Player all : Bukkit.getOnlinePlayers()) {
	    		all.sendMessage(api.returnStringReady(all, "restarter.time").replace("%time_de", "3 Minuten").replace("%time_en", "3 minutes").replace("%prefix", api.getPrefix(PrefixType.MAIN)));
	    	}
	    }else if(stime.equals("23:58:00")) {
	    	for(Player all : Bukkit.getOnlinePlayers()) {
	    		all.sendMessage(api.returnStringReady(all, "restarter.time").replace("%time_de", "2 Minuten").replace("%time_en", "2 minutes").replace("%prefix", api.getPrefix(PrefixType.MAIN)));
	    	}
	    }else if(stime.equals("23:59:00")) {
	    	for(Player all : Bukkit.getOnlinePlayers()) {
	    		all.sendMessage(api.returnStringReady(all, "restarter.time").replace("%time_de", "1 Minute").replace("%time_en", "1 minute").replace("%prefix", api.getPrefix(PrefixType.MAIN)));
	    	}
	    }else if(stime.equals("23:59:50")) {
	    	for(Player all : Bukkit.getOnlinePlayers()) {
	    		all.sendMessage(api.returnStringReady(all, "restarter.time").replace("%time_de", "10 Sekunden").replace("%time_en", "10 seconds").replace("%prefix", api.getPrefix(PrefixType.MAIN)));
	    	}
	    }else if(stime.equals("23:59:51")) {
	    	for(Player all : Bukkit.getOnlinePlayers()) {
	    		all.sendMessage(api.returnStringReady(all, "restarter.time").replace("%time_de", "9 Sekunden").replace("%time_en", "9 seconds").replace("%prefix", api.getPrefix(PrefixType.MAIN)));
	    	}
	    }else if(stime.equals("23:59:52")) {
	    	for(Player all : Bukkit.getOnlinePlayers()) {
	    		all.sendMessage(api.returnStringReady(all, "restarter.time").replace("%time_de", "8 Sekunden").replace("%time_en", "8 seconds").replace("%prefix", api.getPrefix(PrefixType.MAIN)));
	    	}
	    }else if(stime.equals("23:59:53")) {
	    	for(Player all : Bukkit.getOnlinePlayers()) {
	    		all.sendMessage(api.returnStringReady(all, "restarter.time").replace("%time_de", "7 Sekunden").replace("%time_en", "7 seconds").replace("%prefix", api.getPrefix(PrefixType.MAIN)));
	    	}
	    }else if(stime.equals("23:59:54")) {
	    	for(Player all : Bukkit.getOnlinePlayers()) {
	    		all.sendMessage(api.returnStringReady(all, "restarter.time").replace("%time_de", "6 Sekunden").replace("%time_en", "6 seconds").replace("%prefix", api.getPrefix(PrefixType.MAIN)));
	    	}
	    }else if(stime.equals("23:59:55")) {
	    	for(Player all : Bukkit.getOnlinePlayers()) {
	    		all.sendMessage(api.returnStringReady(all, "restarter.time").replace("%time_de", "5 Sekunden").replace("%time_en", "5 seconds").replace("%prefix", api.getPrefix(PrefixType.MAIN)));
	    	}
	    }else if(stime.equals("23:59:56")) {
	    	for(Player all : Bukkit.getOnlinePlayers()) {
	    		all.sendMessage(api.returnStringReady(all, "restarter.time").replace("%time_de", "4 Sekunden").replace("%time_en", "4 seconds").replace("%prefix", api.getPrefix(PrefixType.MAIN)));
	    	}
	    }else if(stime.equals("23:59:57")) {
	    	for(Player all : Bukkit.getOnlinePlayers()) {
	    		all.sendMessage(api.returnStringReady(all, "restarter.time").replace("%time_de", "3 Sekunden").replace("%time_en", "3 seconds").replace("%prefix", api.getPrefix(PrefixType.MAIN)));
	    	}
	    }else if(stime.equals("23:59:58")) {
	    	for(Player all : Bukkit.getOnlinePlayers()) {
	    		all.sendMessage(api.returnStringReady(all, "restarter.time").replace("%time_de", "2 Sekunden").replace("%time_en", "2 seconds").replace("%prefix", api.getPrefix(PrefixType.MAIN)));
	    	}
	    }else if(stime.equals("23:59:59")) {
	    	for(Player all : Bukkit.getOnlinePlayers()) {
	    		all.sendMessage(api.returnStringReady(all, "restarter.time").replace("%time_de", "1 Sekunde").replace("%time_en", "1 second").replace("%prefix", api.getPrefix(PrefixType.MAIN)));
	    	}
	    }else if(stime.equals("00:00:01")) {
	    	Bukkit.shutdown();
	    }
	}
	
	static String prefix = "§aClear§cLag §7» ";
	public static int time = 0;
	
	
	public void clearlag() {
		time++;
		if(time == 870) {
			Bukkit.broadcastMessage(prefix + "Removing items in §a30 seconds§7.");
		}else if(time == 895) {
			Bukkit.broadcastMessage(prefix + "Removing items in §a5 seconds§7.");
		}else if(time == 896) {
			Bukkit.broadcastMessage(prefix + "Removing items in §24 seconds§7.");
		}else if(time == 897) {
			Bukkit.broadcastMessage(prefix + "Removing items in §e3 seconds§7.");
		}else if(time == 898) {
			Bukkit.broadcastMessage(prefix + "Removing items in §c2 seconds§7.");
		}else if(time == 899) {
			Bukkit.broadcastMessage(prefix + "Removing items in §41 second§7.");
		}else if(time == 900) {
			time = 0;
			int worldentities = 0;
			int worlds = 0;
			for(World world : Bukkit.getWorlds()) {
				worlds++;
				for(Entity e : world.getEntities()) {
					if(e instanceof Item) {
						worldentities++;
						e.remove();
					}
				}
			}
			APIs api = new APIs();
			for(Player all : Bukkit.getOnlinePlayers()) {
				all.sendMessage(prefix + api.returnStringReady(all, "clearlag.broadcastmsg").replace("%entities", String.valueOf(worldentities)).replace("%worlds", String.valueOf(worlds)));
			}
		}
	}
	
	public long getPlayTime(Player p) {
		long time = 0;
		try {
			PreparedStatement ps = MySQL.getConnection().prepareStatement("SELECT playtime FROM redicore_userstats WHERE uuid = ?");
			ps.setString(1, p.getUniqueId().toString().replace("-", ""));
			ResultSet rs = ps.executeQuery();
			rs.next();
			time = rs.getInt("playtime");
			ps.close();
			rs.close();
		}catch (SQLException e) {
		}
		return time;
	}
	
	public void setPlayTime(Player p, long playtime) {
		try {
			PreparedStatement ps = MySQL.getConnection().prepareStatement("UPDATE redicore_userstats SET playtime = ? WHERE uuid = ?");
			ps.setLong(1, playtime);
			ps.setString(2, p.getUniqueId().toString().replace("-", ""));
			ps.executeUpdate();
			ps.close();
		}catch (SQLException e) {
		}
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		File file = new File("plugins/RCUSS/ptimecache.yml");
		if(!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		YamlConfiguration cfg = YamlConfiguration.loadConfiguration(file);
		Player p = e.getPlayer();
		String uuid = p.getUniqueId().toString().replace("-", "");
		long systime = (System.currentTimeMillis() / 1000);
		cfg.set(uuid, systime);
		try {
			cfg.save(file);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	
	@EventHandler
	public void onQuit(PlayerQuitEvent e) {
		Player p = e.getPlayer();
		String uuid = p.getUniqueId().toString().replace("-", "");
		File file = new File("plugins/RCUSS/ptimecache.yml");
		if(!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		YamlConfiguration cfg = YamlConfiguration.loadConfiguration(file);
		long oldts = cfg.getLong(uuid);
		long newts = (System.currentTimeMillis() / 1000);
		long diffts = (newts - oldts);
		long currentptime = getPlayTime(p);
		long newptime = (diffts + currentptime);
		setPlayTime(p, newptime);
	}
	
	static int random(int low, int max) {
		Random r = new Random();
		int number = r.nextInt(max);
		while(number < low) {
			number = r.nextInt(max);
		}
		return number;
	}
	
	private String parseTimeWorld(long time) {
		long gameTime = time;
		long hours = gameTime / 1000 + 6;
		long minutes = (gameTime % 1000) * 60 / 1000;
		String ampm = "AM";
		if(hours >= 12) {
			hours -= 12; ampm = "PM";
		}
		if(hours >= 12) {
			hours -= 12; ampm = "AM";
		}
		if(hours == 0) hours = 12;
		String mm = "0" + minutes;
		mm = mm.substring(mm.length() - 2, mm.length());
		return hours + ":" + mm + " " + ampm;
	}

}
