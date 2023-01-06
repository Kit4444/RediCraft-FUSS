package at.kitsoft.rcfuss.command;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import at.kitsoft.rcfuss.api.APIs;
import at.kitsoft.rcfuss.api.ActionLogger;
import at.kitsoft.rcfuss.main.Main;

public class WorkbenchCMD implements CommandExecutor{
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(!(sender instanceof Player)) {
			Bukkit.getConsoleSender().sendMessage(Main.consolesend);
		}else {
			APIs api = new APIs();
			Player p = (Player)sender;
			if(p.hasPermission("mlps.workbench")) {
				p.openWorkbench(null, true);
				api.sendMSGReady(p, "cmd.workbench");
				ActionLogger.log(api.getServerName(), p, "Player executed workbench command.");
			}else {
				api.noPerm(p);
				ActionLogger.log(api.getServerName(), p, "Player attempted to execute workbench command.");
			}
		}
		return false;
	}

}
