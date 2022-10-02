package at.kitsoft.rcfuss.command;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import at.kitsoft.rcfuss.api.APIs;
import at.kitsoft.rcfuss.api.ActionLogger;
import at.kitsoft.rcfuss.api.PrefixType;
import at.kitsoft.rcfuss.main.Main;

public class SetspawnCMD implements CommandExecutor{
	
	File file = new File("plugins/RCFUSS/spawn.yml");

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(!(sender instanceof Player)) {
			Bukkit.getConsoleSender().sendMessage(Main.consolesend);
		}else {
			APIs api = new APIs();
			if(!file.exists()) {
				try {
					file.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			Player p = (Player)sender;
			if(args.length == 0) {
				if(p.hasPermission("mlps.admin.setspawn")) {
					p.sendMessage(api.getPrefix(PrefixType.MAIN) + "§7Usage: /setspawn <type> (-default)");
					p.sendMessage("§7Default types: plotworld, freebuild, nether, theend");
					ActionLogger.log(api.getServerName(), p, "Player executed setspawn command.");
				}else {
					api.noPerm(p);
					ActionLogger.log(api.getServerName(), p, "Player attempted to execute the gc command.");
				}
			}else if(args.length == 1) {
				if(p.hasPermission("mlps.admin.setspawn")) {
					String type = args[0].toLowerCase();
					YamlConfiguration cfg = YamlConfiguration.loadConfiguration(file);
					saveCFG(cfg, p, type, false);
				}else {
					api.noPerm(p);
				}
			}else if(args.length == 2) {
				if(p.hasPermission("mlps.admin.setspawn")) {
					String type = args[0].toLowerCase();
					String par = args[1].toLowerCase();
					boolean def = false;
					if(par.equalsIgnoreCase("-default") || par.equalsIgnoreCase("-def")) {
						def = true;
					}
					YamlConfiguration cfg = YamlConfiguration.loadConfiguration(file);
					saveCFG(cfg, p, type, def);
				}
			}
		}
		return true;
	}
	
	private void saveCFG(YamlConfiguration cfg, Player p, String type, boolean defSpawn) {
		APIs api = new APIs();
		SimpleDateFormat time = new SimpleDateFormat("HH:mm:ss");
		String stime = time.format(new Date());
		SimpleDateFormat date = new SimpleDateFormat("dd.MM.yy");
		String sdate = date.format(new Date());
		Location loc = p.getLocation();
		String dpname = p.getDisplayName();
		String dateformat = stime + " / " + sdate;
		double x = loc.getX();
		double y = loc.getY();
		double z = loc.getZ();
		double pitch = loc.getPitch();
		double yaw = loc.getYaw();
		String world = loc.getWorld().getName().toString();
		if(defSpawn) {
			cfg.set("Defaultspawn.X", x);
			cfg.set("Defaultspawn.Y", y);
			cfg.set("Defaultspawn.Z", z);
			cfg.set("Defaultspawn.YAW", yaw);
			cfg.set("Defaultspawn.PITCH", pitch);
			cfg.set("Defaultspawn.ADMIN", dpname);
			cfg.set("Defaultspawn.DATE", dateformat);
			cfg.set("Defaultspawn.WORLD", world);
		}else {
			cfg.set("Spawn." + type + ".X", x);
			cfg.set("Spawn." + type + ".Y", y);
			cfg.set("Spawn." + type + ".Z", z);
			cfg.set("Spawn." + type + ".YAW", yaw);
			cfg.set("Spawn." + type + ".PITCH", pitch);
			cfg.set("Spawn." + type + ".ADMIN", dpname);
			cfg.set("Spawn." + type + ".DATE", dateformat);
			cfg.set("Spawn." + type + ".WORLD", world);
		}
		try {
			cfg.save(file);
			if(type.equalsIgnoreCase("plotworld")) {
				p.sendMessage(api.getPrefix(PrefixType.MAIN) + "§aA default Spawn has been saved to disk.");
			}else if(type.equalsIgnoreCase("freebuild")) {
				p.sendMessage(api.getPrefix(PrefixType.MAIN) + "§aA default Spawn has been saved to disk.");
			}else if(type.equalsIgnoreCase("nether")) {
				p.sendMessage(api.getPrefix(PrefixType.MAIN) + "§aA default Spawn has been saved to disk.");
			}else if(type.equalsIgnoreCase("theend")) {
				p.sendMessage(api.getPrefix(PrefixType.MAIN) + "§aA default Spawn has been saved to disk.");
			}else {
				p.sendMessage(api.getPrefix(PrefixType.MAIN) + "§aA spawn has been saved to disk.");
			}
		}catch (IOException e) {
			e.printStackTrace();
		}
	}

}
