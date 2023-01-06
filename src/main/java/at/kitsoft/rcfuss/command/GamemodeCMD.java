package at.kitsoft.rcfuss.command;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import at.kitsoft.rcfuss.api.APIs;
import at.kitsoft.rcfuss.api.ActionLogger;
import at.kitsoft.rcfuss.api.PrefixType;
import at.kitsoft.rcfuss.main.Main;

public class GamemodeCMD implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(!(sender instanceof Player)) {
			Bukkit.getConsoleSender().sendMessage(Main.consolesend);
		}else {
			Player p = (Player)sender;
			APIs api = new APIs();
			if(args.length == 0 ) {
				if(p.hasPermission("mlps.gamemode.other")) {
					p.sendMessage(api.getPrefix(PrefixType.MAIN) + "§7Usage: /gm* <0|1|2|3> [Player]");
					p.sendMessage(api.getPrefix(PrefixType.MAIN) + "§7* -> §a/gm §7|§a /gamemode §7are useable");
				}else if(p.hasPermission("mlps.gamemode.own")) {
					p.sendMessage(api.getPrefix(PrefixType.MAIN) + "§7Usage: /gm* <0|1|2|3>");
					p.sendMessage(api.getPrefix(PrefixType.MAIN) + "§7* -> §a/gm §7|§a /gamemode §7are useable");
				}else {
					api.noPerm(p);
				}
			}else if(args.length == 1) {
				if(p.hasPermission("mlps.gamemode.own")){
					String gm = args[0];
					if(gm.equalsIgnoreCase("0")) {
						p.setGameMode(GameMode.SURVIVAL);
						p.sendMessage(api.getPrefix(PrefixType.MAIN) + api.returnStringReady(p, "cmd.gamemode.own").replace("%gamemode", "Survival"));
						ActionLogger.log(api.getServerName(), p, "Player changed gamemode to survival.");
						if(FlyCMD.flylist.contains(p.getUniqueId())) {
							p.setAllowFlight(true);
						}
					}else if(gm.equalsIgnoreCase("1")) {
						p.setGameMode(GameMode.CREATIVE);
						p.sendMessage(api.getPrefix(PrefixType.MAIN) + api.returnStringReady(p, "cmd.gamemode.own").replace("%gamemode", "Creative"));
						ActionLogger.log(api.getServerName(), p, "Player changed gamemode to creative.");
					}else if(gm.equalsIgnoreCase("2")) {
						p.setGameMode(GameMode.ADVENTURE);
						p.sendMessage(api.getPrefix(PrefixType.MAIN) + api.returnStringReady(p, "cmd.gamemode.own").replace("%gamemode", "Adventure Mode"));
						ActionLogger.log(api.getServerName(), p, "Player changed gamemode to adventure mode.");
						if(FlyCMD.flylist.contains(p.getUniqueId())) {
							p.setAllowFlight(true);
						}
					}else if(gm.equalsIgnoreCase("3")) {
						p.setGameMode(GameMode.SPECTATOR);
						p.sendMessage(api.getPrefix(PrefixType.MAIN) + api.returnStringReady(p, "cmd.gamemode.own").replace("%gamemode", "Spectator"));
						ActionLogger.log(api.getServerName(), p, "Player changed gamemode to spectator.");
					}else {
						p.sendMessage(api.getPrefix(PrefixType.MAIN) + "§7Usage: /gm* <0|1|2|3>");
					}
				}else {
					api.noPerm(p);
					ActionLogger.log(api.getServerName(), p, "Player attempted to change gamemode.");
				}
			}else if(args.length >= 2 && args.length <= 2) {
				if(p.hasPermission("mlps.gamemode.other")) {
					String gm = args[0];
					Player p2 = Bukkit.getPlayerExact(args[1]);
					if(p2 == null) {
						api.sendMSGReady(p, "notonline");
					}else {
						if(gm.equalsIgnoreCase("0")) {
							p2.setGameMode(GameMode.SURVIVAL);
							p.sendMessage(api.getPrefix(PrefixType.MAIN) + api.returnStringReady(p, "cmd.gamemode.other").replace("%gamemode", "Survival").replace("%displayer", p2.getDisplayName()));
							ActionLogger.log(api.getServerName(), p, "Player changed gamemode for " + p2.getName() + " to survival.");
							if(FlyCMD.flylist.contains(p2.getUniqueId())) {
								p2.setAllowFlight(true);
							}
						}else if(gm.equalsIgnoreCase("1")) {
							p2.setGameMode(GameMode.CREATIVE);
							p.sendMessage(api.getPrefix(PrefixType.MAIN) + api.returnStringReady(p, "cmd.gamemode.other").replace("%gamemode", "Creative").replace("%displayer", p2.getDisplayName()));
							ActionLogger.log(api.getServerName(), p, "Player changed gamemode for " + p2.getName() + " to creative.");
						}else if(gm.equalsIgnoreCase("2")) {
							p2.setGameMode(GameMode.ADVENTURE);
							p.sendMessage(api.getPrefix(PrefixType.MAIN) + api.returnStringReady(p, "cmd.gamemode.other").replace("%gamemode", "Adventure Mode").replace("%displayer", p2.getDisplayName()));
							ActionLogger.log(api.getServerName(), p, "Player changed gamemode for " + p2.getName() + " to adventure mode.");
							if(FlyCMD.flylist.contains(p2.getUniqueId())) {
								p2.setAllowFlight(true);
							}
						}else if(gm.equalsIgnoreCase("3")) {
							p2.setGameMode(GameMode.SPECTATOR);
							p.sendMessage(api.getPrefix(PrefixType.MAIN) + api.returnStringReady(p, "cmd.gamemode.other").replace("%gamemode", "Spectator").replace("%displayer", p2.getDisplayName()));
							ActionLogger.log(api.getServerName(), p, "Player changed gamemode for " + p2.getName() + " to spectator.");
						}
					}
				}else {
					api.noPerm(p);
					ActionLogger.log(api.getServerName(), p, "Player attempted to change gamemode for others.");
				}
			}else {
				if(p.hasPermission("mlps.gamemode.other")) {
					p.sendMessage(api.getPrefix(PrefixType.MAIN) + "§7Usage: /gm <0|1|2|3> [Player]");
				}else if(p.hasPermission("mlps.gamemode.own")) {
					p.sendMessage(api.getPrefix(PrefixType.MAIN) + "§7Usage: /gm <0|1|2|3>");
				}else {
					api.noPerm(p);
				}
			}
		}
		return false;
	}

}
