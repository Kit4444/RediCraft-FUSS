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

public class InvseeCMD implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(!(sender instanceof Player)) {
			Bukkit.getConsoleSender().sendMessage(Main.consolesend);
		}else {
			Player p = (Player)sender;
			APIs api = new APIs();
			if(args.length == 1) {
				Player p2 = Bukkit.getPlayerExact(args[0]);
				if(p2 == null) {
					api.sendMSGReady(p, "cmd.invsee.playeroffline");
				}else {
					if(p.hasPermission("mlps.invsee")) {
						if(p != p2) {
							p.openInventory(p2.getInventory());
							p.sendMessage(api.getPrefix(PrefixType.MAIN) + api.returnStringReady(p, "cmd.invsee.success").replace("%displayer", p2.getDisplayName()));
							ActionLogger.log(api.getServerName(), p, "Player used invsee command.");
						}else {
							api.sendMSGReady(p, "cmd.invsee.notown");
							ActionLogger.log(api.getServerName(), p, "Player attempted to use invsee command, was own inventory.");
						}
					}else {
						api.noPerm(p);
						ActionLogger.log(api.getServerName(), p, "Player attempted to use invsee command.");
					}
				}
			}else {
				p.sendMessage(api.getPrefix(PrefixType.MAIN) + api.returnStringReady(p, "usage") + " §7/invsee <Name>");
			}
		}
		return true;
	}

}
