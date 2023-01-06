package at.kitsoft.rcfuss.event;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;
import org.bukkit.event.player.PlayerQuitEvent;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import at.kitsoft.rcfuss.api.APIs;
import at.kitsoft.rcfuss.api.PrefixType;
import at.kitsoft.rcfuss.mysql.lb.MySQL;
import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;

public class BlockerClass implements Listener {

	static File spawnfile = new File("plugins/RCUSS/spawn.yml");
	static File spawnhandler = new File("plugins/RCUSS/spawnhandler.yml");

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onLogin(PlayerLoginEvent e) {
		Player p = e.getPlayer();
		APIs api = new APIs();
		String level = getEntryLevel(api.getServerName());
		if (level.equalsIgnoreCase("All")) {
			e.allow();
		} else if (level.equalsIgnoreCase("Staff")) {
			if (p.hasPermission("mlps.isStaff")) {
				e.allow();
			} else {
				e.disallow(Result.KICK_OTHER, "§aRedi§cCraft\n \n§7This Server is just for Staff members allowed.");
			}
		} else if (level.equalsIgnoreCase("Alpha")) {
			if (p.hasPermission("mlps.isAlphatester")) {
				e.allow();
			} else {
				e.disallow(Result.KICK_OTHER, "§aRedi§cCraft\n \n§7This Server is just for Alpha-Testers free.");
			}
		} else if (level.equalsIgnoreCase("Beta")) {
			if (p.hasPermission("mlps.isBetatester")) {
				e.allow();
			} else {
				e.disallow(Result.KICK_OTHER, "§aRedi§cCraft\n \n§7This Server is just for Beta-Testers free.");
			}
		} else {
			Bukkit.getConsoleSender().sendMessage("Level: " + level);
		}
	}
	
	private Location retLoc(YamlConfiguration cfg) {
		Location loc = null;
		if(cfg.contains("Defaultspawn.WORLD")) {
			loc = new Location(Bukkit.getWorld(cfg.getString("Defaultspawn.WORLD")), cfg.getDouble("Defaultspawn.X"), cfg.getDouble("Defaultspawn.Y"), cfg.getDouble("Defaultspawn.Z"), (float)cfg.getDouble("Defaultspawn.YAW"), (float)cfg.getDouble("Defaultspawn.PITCH"));
		}else {
			loc = null;
		}
		return loc;
	}

	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		APIs api = new APIs();
		e.setJoinMessage(null);
		Player p = e.getPlayer();
		SimpleDateFormat time = new SimpleDateFormat("dd/MM/yy - HH:mm:ss");
		String stime = time.format(new Date());
		Timestamp ts = new Timestamp(System.currentTimeMillis());
		String uuid = p.getUniqueId().toString().replace("-", "");
		YamlConfiguration cfg = YamlConfiguration.loadConfiguration(spawnhandler);
		YamlConfiguration spawn = YamlConfiguration.loadConfiguration(spawnfile);
		if(!cfg.contains(uuid)) {
			cfg.set(uuid, true);
			try {
				cfg.save(spawnhandler);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			p.teleport(retLoc(spawn));
		}else {
			if(!cfg.getBoolean(uuid)) {
				p.teleport(retLoc(spawn));
			}
		}
		for (Player all : Bukkit.getOnlinePlayers()) {
			if (all.hasPermission("mlps.canBan")) {
				all.sendMessage("§7[§a+§7] " + p.getDisplayName());
			}
		}
		try {
			PermissionUser po = PermissionsEx.getUser(p);
			PreparedStatement ps = MySQL.getConnection().prepareStatement(
					"UPDATE redicore_userstats SET userrank = ?, rankcolor = ?, online = ?, server = ?, lastjoints = ?, lastjoinstring = ?, lastloginip = ?, isstaff = ?, username = ? WHERE uuid = ?");
			if (po.inGroup("pman")) {
				ps.setString(1, "Project Manager");
				ps.setString(2, "#5555ff");
			} else if(po.inGroup("apman")) {
    			ps.setString(1, "Assistant Project Manager");
    			ps.setString(2, "#282be0");
    		}else if(po.inGroup("adprman")) {
    			ps.setString(1, "Advertisment and Public Relations Manager");
    			ps.setString(2, "#3498db");
    		}else if (po.inGroup("cman")) {
				ps.setString(1, "Community Manager");
				ps.setString(2, "#00aa00");
			} else if (po.inGroup("gmmman")) {
				ps.setString(1, "Game Mod. Manager");
				ps.setString(2, "#aa0000");
			} else if (po.inGroup("dev")) {
				ps.setString(1, "Developer");
				ps.setString(2, "#aa00aa");
			} else if (po.inGroup("hr")) {
				ps.setString(1, "Human Resources");
				ps.setString(2, "#ffaa00");
			} else if (po.inGroup("cm")) {
				ps.setString(1, "Community Moderator");
				ps.setString(2, "#55ff55");
			} else if (po.inGroup("ct")) {
				ps.setString(1, "Content Team");
				ps.setString(2, "#0000aa");
			} else if (po.inGroup("st")) {
				ps.setString(1, "Support Team");
				ps.setString(2, "#ffff55");
			} else if (po.inGroup("bd")) {
				ps.setString(1, "Builder");
				ps.setString(2, "#55ffff");
			} else if (po.inGroup("gm")) {
				ps.setString(1, "Game Moderator");
				ps.setString(2, "#ff5555");
			} else if (po.inGroup("aot")) {
				ps.setString(1, "Add-On Team");
				ps.setString(2, "#ff55ff");
			} else if (po.inGroup("train")) {
				ps.setString(1, "Trainee");
				ps.setString(2, "#55ffff");
			} else if (po.inGroup("rltm")) {
				ps.setString(1, "Retired Legend");
				ps.setString(2, "#00aaaa");
			} else if (po.inGroup("rtm")) {
				ps.setString(1, "Retired Team Member");
				ps.setString(2, "#00aaaa");
			} else if (po.inGroup("part")) {
				ps.setString(1, "Partner");
				ps.setString(2, "#00aa00");
			} else if (po.inGroup("fs")) {
				ps.setString(1, "Forum Supporter");
				ps.setString(2, "#ff55ff");
			} else if (po.inGroup("nb")) {
				ps.setString(1, "Nitro Booster");
				ps.setString(2, "#ff55ff");
			} else if (po.inGroup("bt")) {
				ps.setString(1, "Beta Tester");
				ps.setString(2, "#ff55ff");
			} else if (po.inGroup("friend")) {
				ps.setString(1, "Friend");
				ps.setString(2, "#aaaaaa");
			} else if (po.inGroup("vip")) {
				ps.setString(1, "VIP");
				ps.setString(2, "#ffff55");
			} else if (po.inGroup("default")) {
				ps.setString(1, "Player");
				ps.setString(2, "#ffffff");
			} else {
				ps.setString(1, "unknown Role");
				ps.setString(1, "#7c4dff");
			}
			ps.setBoolean(3, true);
			ps.setString(4, api.getServerName());
			ps.setLong(5, ts.getTime());
			ps.setString(6, stime);
			ps.setString(7, p.getAddress().getHostString());
			if (p.hasPermission("mlps.isStaff")) {
				ps.setBoolean(8, true);
			} else {
				ps.setBoolean(8, false);
			}
			ps.setString(9, p.getName());
			ps.setString(10, uuid);
			ps.executeUpdate();
			ps.close();
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		int notify_pm = 0;
		int notify_tpar = 0;
		try {
			PreparedStatement ps = MySQL.getConnection()
					.prepareStatement("SELECT disablePMs,disableTPAR FROM redicore_userstats WHERE uuid = ?");
			ps.setString(1, p.getUniqueId().toString().replace("-", ""));
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				if (rs.getBoolean("disablePMs")) {
					notify_pm = 1;
				}
				if (rs.getBoolean("disableTPAR")) {
					notify_tpar = 1;
				}
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		if (notify_pm == 1) {
			p.sendMessage(api.getPrefix(PrefixType.SYSTEM) + api.returnStringReady(p, "event.join.blockpmnotify"));
		}
		if (notify_tpar == 1) {
			p.sendMessage(api.getPrefix(PrefixType.SYSTEM) + api.returnStringReady(p, "event.join.tparnotify"));
		}
	}

	@EventHandler
	public void onQuit(PlayerQuitEvent e) {
		e.setQuitMessage(null);
		Player p = e.getPlayer();
		String uuid = p.getUniqueId().toString().replace("-", "");
		SimpleDateFormat time = new SimpleDateFormat("dd/MM/yy - HH:mm:ss");
		String stime = time.format(new Date());
		Timestamp ts = new Timestamp(System.currentTimeMillis());
		PermissionUser po = PermissionsEx.getUser(p);
		for (Player all : Bukkit.getOnlinePlayers()) {
			if (all.hasPermission("mlps.canBan")) {
				all.sendMessage("§7[§c-§7] " + p.getDisplayName());
			}
		}
		try {
			PreparedStatement ps = MySQL.getConnection().prepareStatement(
					"UPDATE redicore_userstats SET userrank = ?, rankcolor = ?, lastjoints = ?, lastjoinstring = ?, lastloginip = ?, online = ?, lj_ip_isp = ?, lj_ip_tz = ?, lj_ip_cty = ?, lj_ip_reg = ? WHERE uuid = ?");
			if (po.inGroup("pman")) {
				ps.setString(1, "Project Manager");
				ps.setString(2, "#5555ff");
			} else if (po.inGroup("cman")) {
				ps.setString(1, "Community Manager");
				ps.setString(2, "#00aa00");
			} else if(po.inGroup("apman")) {
    			ps.setString(1, "Assistant Project Manager");
    			ps.setString(2, "#282be0");
    		}else if(po.inGroup("adprman")) {
    			ps.setString(1, "Advertisment and Public Relations Manager");
    			ps.setString(2, "#3498db");
    		}else if (po.inGroup("gmmman")) {
				ps.setString(1, "Game Mod. Manager");
				ps.setString(2, "#aa0000");
			} else if (po.inGroup("dev")) {
				ps.setString(1, "Developer");
				ps.setString(2, "#aa00aa");
			} else if (po.inGroup("hr")) {
				ps.setString(1, "Human Resources");
				ps.setString(2, "#ffaa00");
			} else if (po.inGroup("cm")) {
				ps.setString(1, "Community Moderator");
				ps.setString(2, "#55ff55");
			} else if (po.inGroup("ct")) {
				ps.setString(1, "Content Team");
				ps.setString(2, "#0000aa");
			} else if (po.inGroup("st")) {
				ps.setString(1, "Support Team");
				ps.setString(2, "#ffff55");
			} else if (po.inGroup("bd")) {
				ps.setString(1, "Builder");
				ps.setString(2, "#55ffff");
			} else if (po.inGroup("gm")) {
				ps.setString(1, "Game Moderator");
				ps.setString(2, "#ff5555");
			} else if (po.inGroup("aot")) {
				ps.setString(1, "Add-On Team");
				ps.setString(2, "#ff55ff");
			} else if (po.inGroup("train")) {
				ps.setString(1, "Trainee");
				ps.setString(2, "#55ffff");
			} else if (po.inGroup("rltm")) {
				ps.setString(1, "Retired Legend");
				ps.setString(2, "#00aaaa");
			} else if (po.inGroup("rtm")) {
				ps.setString(1, "Retired Team Member");
				ps.setString(2, "#00aaaa");
			} else if (po.inGroup("part")) {
				ps.setString(1, "Partner");
				ps.setString(2, "#00aa00");
			} else if (po.inGroup("fs")) {
				ps.setString(1, "Forum Supporter");
				ps.setString(2, "#ff55ff");
			} else if (po.inGroup("nb")) {
				ps.setString(1, "Nitro Booster");
				ps.setString(2, "#ff55ff");
			} else if (po.inGroup("bt")) {
				ps.setString(1, "Beta Tester");
				ps.setString(2, "#ff55ff");
			} else if (po.inGroup("friend")) {
				ps.setString(1, "Friend");
				ps.setString(2, "#aaaaaa");
			} else if (po.inGroup("vip")) {
				ps.setString(1, "VIP");
				ps.setString(2, "#ffff55");
			} else if (po.inGroup("default")) {
				ps.setString(1, "Player");
				ps.setString(2, "#ffffff");
			} else {
				ps.setString(1, "unknown Role");
				ps.setString(1, "#7c4dff");
			}
			ps.setLong(3, ts.getTime());
			ps.setString(4, stime);
			ps.setString(5, p.getAddress().getHostString());
			ps.setBoolean(6, false);
			ps.setString(7, getFromAPI(p, "isp"));
			ps.setString(8, getFromAPI(p, "timezone"));
			ps.setString(9, getFromAPI(p, "country"));
			ps.setString(10, getFromAPI(p, "region"));
			ps.setString(11, uuid);
			ps.executeUpdate();
			ps.close();
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}

	@EventHandler
	public void onDeath(PlayerDeathEvent e) {
		e.setDeathMessage(null);
	}

	public String getEntryLevel(String server) {
		String level = "";
		try {
			PreparedStatement ps = MySQL.getConnection()
					.prepareStatement("SELECT entrylevel FROM redicore_serverstats WHERE servername = ?");
			ps.setString(1, server);
			ResultSet rs = ps.executeQuery();
			rs.next();
			level = rs.getString("entrylevel");
			rs.close();
			ps.closeOnCompletion();
		} catch (SQLException e) {
			level = "NONE";
			e.printStackTrace();
		}
		return level;
	}

	public String getFromAPI(Player p, String key) {
		String url_ = "http://www.ip-api.com/json/";
		String data = "";
		String useragent = "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2228.0 Safari/537.36";

		try {
			URL url = new URL(url_ + p.getAddress().getHostName());
			URLConnection con = url.openConnection();
			con.setRequestProperty("User-Agent", useragent);
			try (InputStreamReader isr = new InputStreamReader(con.getInputStream())) {
				BufferedReader bR = new BufferedReader(isr);
				JSONParser jP = new JSONParser();
				JSONObject jO = (JSONObject) jP.parse(bR);
				if (jO.get(key) == null)
					return "JSON is empty!";
				data = jO.get(key).toString();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ParseException e) {
				e.printStackTrace();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return data;
	}

}
