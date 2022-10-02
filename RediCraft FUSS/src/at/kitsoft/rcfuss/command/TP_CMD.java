package at.kitsoft.rcfuss.command;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import at.kitsoft.rcfuss.api.APIs;
import at.kitsoft.rcfuss.api.ActionLogger;
import at.kitsoft.rcfuss.api.PrefixType;
import at.kitsoft.rcfuss.main.Main;

public class TP_CMD implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(!(sender instanceof Player)) {
			Bukkit.getConsoleSender().sendMessage(Main.consolesend);
		}else {
			Player p = (Player)sender;
			APIs api = new APIs();
			if(cmd.getName().equalsIgnoreCase("tp")) {
				if(p.hasPermission("mlps.admin.tp")) {
					if(args.length == 1) {
						String name = args[0];
						if(!(name.length() <= 2) || !(name.length() >= 17)) {
							Player p2 = Bukkit.getPlayerExact(name);
							if(p2 != null) {
								p.teleport(p2);
								p.sendMessage(api.getPrefix(PrefixType.MAIN) + api.returnStringReady(p, "cmd.tp").replace("%displayer", p2.getDisplayName()));
								ActionLogger.log(api.getServerName(), p, "Player executed tp to " + p2.getName() + ".");
							}else {
								api.sendMSGReady(p, "notonline");
							}
						}else {
							api.sendMSGReady(p, "nameexceedslimit");
						}
					}else if(args.length == 3) {
						String s_x = args[0];
						String s_y = args[1];
						String s_z = args[2];
						if(s_x.matches("^[0-9 -]+$") && s_y.matches("^[0-9 -]+$") && s_z.matches("^[0-9 -]+$")) {
							int x = Integer.parseInt(s_x);
							int y = Integer.parseInt(s_y);
							int z = Integer.parseInt(s_z);
							Location loc = new Location(p.getWorld(), x, y, z);
							p.teleport(loc);
							p.sendMessage(api.getPrefix(PrefixType.MAIN) + api.returnStringReady(p, "cmd.tp.location").replace("%x", s_x).replace("%y", s_y).replace("%z", s_z));
							ActionLogger.log(api.getServerName(), p, "Player executed tp to X:" + x + ", Y:" + y + ", Z: " + z + ".");
						}else {
							p.sendMessage(api.getPrefix(PrefixType.MAIN) + api.returnStringReady(p, "usage") + "§7/tp <Player> | /tp <x> <y> <z>");
						}
					}else {
						p.sendMessage(api.getPrefix(PrefixType.MAIN) + api.returnStringReady(p, "usage") + "§7/tp <Player> | /tp <x> <y> <z>");
					}
				}else {
					api.noPerm(p);
					ActionLogger.log(api.getServerName(), p, "Player attempted to execute tp.");
				}
			}else if(cmd.getName().equalsIgnoreCase("tphere")) {
				if(p.hasPermission("mlps.admin.tp")) {
					if(args.length == 1) {
						String name = args[0];
						if(!(name.length() <= 2) || !(name.length() >= 17)) {
							Player p2 = Bukkit.getPlayerExact(name);
							if(p2 != null) {
								p2.teleport(p);
								p.sendMessage(api.getPrefix(PrefixType.MAIN) + api.returnStringReady(p, "cmd.tphere").replace("%displayer", p2.getDisplayName()));
								ActionLogger.log(api.getServerName(), p, "Player executed tphere, " + p2.getName() + " has been teleported to player.");
							}else {
								api.sendMSGReady(p, "notonline");
							}
						}else {
							api.sendMSGReady(p, "nameexceedslimit");
						}
					}else {
						p.sendMessage(api.getPrefix(PrefixType.MAIN) + api.returnStringReady(p, "usage") + "§7/tphere <Player>");
					}
				}else {
					api.noPerm(p);
					ActionLogger.log(api.getServerName(), p, "Player attempted to execute tphere.");
				}
			}
		}
		return false;
	}

}
