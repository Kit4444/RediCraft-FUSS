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

public class PingCMD implements CommandExecutor{
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(!(sender instanceof Player)) {
			Bukkit.getConsoleSender().sendMessage(Main.consolesend);
		}else {
			Player p = (Player)sender;
			APIs api = new APIs();
			if(args.length == 0) {
				p.sendMessage(api.getPrefix(PrefixType.MAIN) + api.returnStringReady(p, "usage") + "§7 /ping <me|Player|all>");
			}else if(args.length == 1) {
				if(args[0].equalsIgnoreCase("me")) {
					ActionLogger.log(api.getServerName(), p, "Player used ping command, subcommand me.");
					p.sendMessage(api.getPrefix(PrefixType.MAIN) + api.returnStringReady(p, "cmd.ping.own").replace("%ping", String.valueOf(api.getPlayerPing(p))));
				}else if(args[0].equalsIgnoreCase("all")) {
					ActionLogger.log(api.getServerName(), p, "Player used ping command, subcommand all.");
					for(Player all : Bukkit.getOnlinePlayers()) {
						int ping1 = api.getPlayerPing(all);
						p.sendMessage(api.getPrefix(PrefixType.MAIN) + api.returnStringReady(p, "cmd.ping.all").replace("%displayer", all.getDisplayName()).replace("%ping", String.valueOf(ping1)));
					}
				}else {
					Player p2 = Bukkit.getPlayerExact(args[0]);
					if(p2 == null) {
						api.sendMSGReady(p, "cmd.ping.notonline");
					}else {
						int ping2 = api.getPlayerPing(p2);
						ActionLogger.log(api.getServerName(), p, "Player used ping command, specified player.");
						p.sendMessage(api.getPrefix(PrefixType.MAIN) + api.returnStringReady(p, "cmd.ping.other").replace("%displayer", p2.getDisplayName()).replace("%ping", String.valueOf(ping2)));
					}
				}
			}else {
				p.sendMessage(api.getPrefix(PrefixType.MAIN) + api.returnStringReady(p, "usage") + "§7 /ping <me|Player|all>");
			}
		}
		return false;
	}

}
