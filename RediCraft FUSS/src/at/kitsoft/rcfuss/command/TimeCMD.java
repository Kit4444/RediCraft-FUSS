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

public class TimeCMD implements CommandExecutor{
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(!(sender instanceof Player)) {
			Bukkit.getConsoleSender().sendMessage(Main.consolesend);
		}else {
			APIs api = new APIs();
			Player p = (Player)sender;
			if(args.length == 0) {
				p.sendMessage(api.getPrefix(PrefixType.MAIN) + "§7Usage: /time <set|add|remove|info> [time]");
			}else if(args.length == 1) {
				if(args[0].equalsIgnoreCase("info")) {
					p.sendMessage(api.getPrefix(PrefixType.MAIN) + api.returnStringReady(p, "cmd.time.info").replace("%world", p.getWorld().getName()).replace("%timedigital", parseTimeWorld(p.getWorld().getTime())));
				}
			}else if(args.length == 2) {
				if(args[0].equalsIgnoreCase("set")) {
					if(p.hasPermission("mlps.setTime")) {
						if(args[1].matches("^[0-9]+$")) {
							long time = Long.valueOf(args[1]);
							p.getWorld().setTime(time);
							p.sendMessage(api.getPrefix(PrefixType.MAIN) + api.returnStringReady(p, "cmd.time.set").replace("%world", p.getWorld().getName()).replace("%timelong", String.valueOf(p.getWorld().getTime())).replace("%timedigital", parseTimeWorld(p.getWorld().getTime())));
							ActionLogger.log(api.getServerName(), p, "Player set the time specifically on " + p.getWorld().getName() + ".");
						}else {
							if(args[1].equalsIgnoreCase("day")) {
								p.getWorld().setTime(0);
								p.sendMessage(api.getPrefix(PrefixType.MAIN) + api.returnStringReady(p, "cmd.time.set").replace("%world", p.getWorld().getName()).replace("%timelong", String.valueOf(p.getWorld().getTime())).replace("%timedigital", parseTimeWorld(p.getWorld().getTime())));
								ActionLogger.log(api.getServerName(), p, "Player set the time to day on " + p.getWorld().getName() + ".");
							}else if(args[1].equalsIgnoreCase("midnight")) {
								p.getWorld().setTime(18000);
								p.sendMessage(api.getPrefix(PrefixType.MAIN) + api.returnStringReady(p, "cmd.time.set").replace("%world", p.getWorld().getName()).replace("%timelong", String.valueOf(p.getWorld().getTime())).replace("%timedigital", parseTimeWorld(p.getWorld().getTime())));
								ActionLogger.log(api.getServerName(), p, "Player set the time to midnight on " + p.getWorld().getName() + ".");
							}else if(args[1].equalsIgnoreCase("night")) {
								p.getWorld().setTime(13000);
								p.sendMessage(api.getPrefix(PrefixType.MAIN) + api.returnStringReady(p, "cmd.time.set").replace("%world", p.getWorld().getName()).replace("%timelong", String.valueOf(p.getWorld().getTime())).replace("%timedigital", parseTimeWorld(p.getWorld().getTime())));
								ActionLogger.log(api.getServerName(), p, "Player set the time to night on " + p.getWorld().getName() + ".");
							}else if(args[1].equalsIgnoreCase("noon")) {
								p.getWorld().setTime(6000);
								p.sendMessage(api.getPrefix(PrefixType.MAIN) + api.returnStringReady(p, "cmd.time.set").replace("%world", p.getWorld().getName()).replace("%timelong", String.valueOf(p.getWorld().getTime())).replace("%timedigital", parseTimeWorld(p.getWorld().getTime())));
								ActionLogger.log(api.getServerName(), p, "Player set the time to evening on " + p.getWorld().getName() + ".");
							}
						}
					}else {
						api.noPerm(p);
					}
				}else if(args[0].equalsIgnoreCase("remove")) {
					if(p.hasPermission("mlps.setTime")) {
						long ctime = p.getWorld().getTime();
						long time = Long.valueOf(args[1]);
						long newtime = (ctime - time);
						p.getWorld().setTime(newtime);
						p.sendMessage(api.getPrefix(PrefixType.MAIN) + api.returnStringReady(p, "cmd.time.set").replace("%world", p.getWorld().getName()).replace("%time", String.valueOf(p.getWorld().getTime())).replace("%timedigital", parseTimeWorld(p.getWorld().getTime())));
						ActionLogger.log(api.getServerName(), p, "Player removed the time for " + time + " ticks on " + p.getWorld().getName());
					}else {
						api.noPerm(p);
					}
				}else if(args[0].equalsIgnoreCase("add")) {
					if(p.hasPermission("mlps.setTime")) {
						long ctime = p.getWorld().getTime();
						long time = Long.valueOf(args[1]);
						long newtime = (ctime + time);
						p.getWorld().setTime(newtime);
						ActionLogger.log(api.getServerName(), p, "Player added the time for " + time + " ticks on " + p.getWorld().getName());
						p.sendMessage(api.getPrefix(PrefixType.MAIN) + api.returnStringReady(p, "cmd.time.set").replace("%world", p.getWorld().getName()).replace("%time", String.valueOf(p.getWorld().getTime())).replace("digitime", parseTimeWorld(p.getWorld().getTime())));
					}else {
						api.noPerm(p);
					}
				}
			}
		}
		return false;
	}
	
	private String parseTimeWorld(long time) {
		long gameTime = time;
		long hours = gameTime / 1000 + 6;
		long minutes = (gameTime % 1000) * 60 / 1000;
		String ampm = "AM";
		if(hours >= 12) {
			hours -= 12; ampm = "PM";
		}
		if(hours >= 12) {
			hours -= 12; ampm = "AM";
		}
		if(hours == 0) hours = 12;
		String mm = "0" + minutes;
		mm = mm.substring(mm.length() - 2, mm.length());
		return hours + ":" + mm + " " + ampm;
	}

}
