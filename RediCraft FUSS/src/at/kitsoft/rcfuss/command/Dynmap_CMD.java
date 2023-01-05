package at.kitsoft.rcfuss.command;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import at.kitsoft.rcfuss.api.APIs;
import at.kitsoft.rcfuss.api.ActionLogger;
import at.kitsoft.rcfuss.api.PrefixType;
import at.kitsoft.rcfuss.main.Main;

public class Dynmap_CMD implements CommandExecutor{
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(!(sender instanceof Player)) {
			Bukkit.getConsoleSender().sendMessage(Main.consolesend);
		}else {
			APIs api = new APIs();
			Player p = (Player)sender;
			String server = api.getServerName();
			String mapname = p.getLocation().getWorld().getName().toLowerCase();
			int x = p.getLocation().getBlockX();
			int y = p.getLocation().getBlockY();
			int z = p.getLocation().getBlockZ();
			String url = "/?worldname=" + mapname + "&mapname=flat&zoom=6&x=" + x + "&y=" + y + "&z=" + z;
			if(server.equalsIgnoreCase("Forge Creative")) {
				p.sendMessage(api.getPrefix(PrefixType.MAIN) + "§aURL§7: http://map.redicraft.eu:23201" + url);
			}else if(server.equalsIgnoreCase("Forge Survival")) {
				p.sendMessage(api.getPrefix(PrefixType.MAIN) + "§aURL§7: http://map.redicraft.eu:23202" + url);
			}else {
				p.sendMessage(api.getPrefix(PrefixType.MAIN) + api.returnStringReady(p, "cmd.onlinemap.invalidserver"));
			}
			ActionLogger.log(api.getServerName(), p, "Player executed onlinemap command.");
		}
		return true;
	}

}
