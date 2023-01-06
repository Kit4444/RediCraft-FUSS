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

public class ChatClearCMD implements CommandExecutor{
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(!(sender instanceof Player)) {
			Bukkit.getConsoleSender().sendMessage(Main.consolesend);
		}else {
			Player p = (Player)sender;
			APIs api = new APIs();
			if(args.length == 1) {
				if(args[0].equalsIgnoreCase("private")) {
					
					clearUserChat(10, p);
					
					api.sendMSGReady(p, "cmd.cc.private");
					ActionLogger.log(api.getServerName(), p, "Player cleared chat for themself.");
				}else if(args[0].equalsIgnoreCase("public")) {
					if(p.hasPermission("mlps.clearchat")) {
						
						clearPublicChat(7);
						
						api.sendMSGReady(p, "cmd.cc.public");
						for(Player all : Bukkit.getOnlinePlayers()) {
							all.sendMessage(api.getPrefix(PrefixType.MAIN) + api.returnStringReady(all, "cmd.cc.forall.public").replace("%displayer", p.getDisplayName()));
						}
						ActionLogger.log(api.getServerName(), p, "Player cleared chat for public, not anonymously.");
					}else {
						api.noPerm(p);
						ActionLogger.log(api.getServerName(), p, "Player attempted to clear chat for public, not anonymously.");
					}
				}else if(args[0].equalsIgnoreCase("anonymous")) {
					if(p.hasPermission("mlps.clearchat")){
						
						clearPublicChat(7);
						
						api.sendMSGReady(p, "cmd.cc.anonymous");
						for(Player all : Bukkit.getOnlinePlayers()) {
							api.sendMSGReady(all, "cmd.cc.forall.anonymous");
						}
						ActionLogger.log(api.getServerName(), p, "Player cleared chat for public, anonymously.");
					}else {
						api.noPerm(p);
						ActionLogger.log(api.getServerName(), p, "Player attempted to clear chat for public, anonymously.");
					}
				}else {
					p.sendMessage(api.getPrefix(PrefixType.MAIN) + api.returnStringReady(p, "usage") + "§7 /cc <private|public|anonymous>");
				}
			}else {
				p.sendMessage(api.getPrefix(PrefixType.MAIN) + api.returnStringReady(p, "usage") + "§7 /cc <private|public|anonymous>");
			}
		}
		return false;
	}
	
	private void clearPublicChat(int times){
		for(int i = 0; i < times; i++){
			Bukkit.broadcastMessage("\n \n \n \n \n \n \n \n \n \n");
		}
	}
	
	private void clearUserChat(int times, Player player){
		for(int i = 0; i < times; i++){
			player.sendMessage("\n \n \n \n \n \n \n \n \n \n");
		}
	}

}
