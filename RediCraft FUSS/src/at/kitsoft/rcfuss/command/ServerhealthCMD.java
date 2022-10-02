package at.kitsoft.rcfuss.command;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import at.kitsoft.rcfuss.api.APIs;
import at.kitsoft.rcfuss.api.ActionLogger;
import at.kitsoft.rcfuss.api.PerformanceMonitor;
import at.kitsoft.rcfuss.api.TPSMonitor;
import at.kitsoft.rcfuss.main.Main;

public class ServerhealthCMD implements CommandExecutor{
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		APIs api = new APIs();
		if(!(sender instanceof Player)) {
			sender.sendMessage(Main.consolesend);
		}else {
			Player p = (Player)sender;
			Runtime runtime = Runtime.getRuntime();
			SimpleDateFormat time = new SimpleDateFormat("HH:mm:ss");
			String stime = time.format(new Date());
			SimpleDateFormat date = new SimpleDateFormat("dd/MM/yyyy");
			String sdate = date.format(new Date());
			if(p.hasPermission("mlps.admin.checkServer")) {
				ActionLogger.log(api.getServerName(), p, "Player executed the gc command.");
				p.sendMessage("§7§m================§7[§cServerinfo§7]§m================");
				p.sendMessage("§7operating System: §9" + System.getProperty("os.name"));
				p.sendMessage("§7Architecture: §9" + System.getProperty("os.arch"));
				p.sendMessage("§7Java Vendor: §9" + System.getProperty("java.vendor"));
				p.sendMessage("§7Java Version: §9" + System.getProperty("java.version"));
				p.sendMessage("§7RAM: §a" + (runtime.totalMemory() - runtime.freeMemory()) / 1048576L + "§8MB §7/ §c" + runtime.totalMemory() / 1048576L + "§8MB §7(§e" + runtime.freeMemory() / 1048576L + "§8MB free§7)");
				p.sendMessage("§7CPU-Cores: §9" + runtime.availableProcessors() + " §7Cores");
				p.sendMessage("§7CPU-Load: §9" + PerformanceMonitor.getProcessCpuLoad());
				p.sendMessage("§7IP + Port: §9" + Bukkit.getIp() + "§7:§9" + Bukkit.getPort());
				p.sendMessage("§7Servername + ID: §9" + api.getServerName() + " §7/§9 " + api.getServerId());
				p.sendMessage("§7Date & Time: §9" + sdate + " §7/§9 " + stime);
				p.sendMessage("§7TPS from last 1m: §a" + TPSMonitor.getColorTPS());
			}else {
				api.noPerm(p);
				ActionLogger.log(api.getServerName(), p, "Player attempted to executed the gc command.");
			}
		}
		return true;
	}

}
