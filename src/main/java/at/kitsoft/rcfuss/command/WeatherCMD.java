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

public class WeatherCMD implements CommandExecutor{
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(!(sender instanceof Player)) {
			Bukkit.getConsoleSender().sendMessage(Main.consolesend);
		}else {
			APIs api = new APIs();
			Player p = (Player)sender;
			if(args.length == 0) {
				p.sendMessage(api.getPrefix(PrefixType.MAIN) + api.returnStringReady(p, "usage") + "§7 /weather <clear|rain|thunder> [time in seconds]");
			}else if(args.length == 1) {
				if(args[0].equalsIgnoreCase("clear")) {
					if(p.hasPermission("mlps.setweather")) {
						p.getWorld().setThundering(false);
						p.getWorld().setStorm(false);
						p.sendMessage(api.getPrefix(PrefixType.MAIN) + api.returnStringReady(p, "cmd.weather.clear").replace("%world", p.getWorld().getName()));
						ActionLogger.log(api.getServerName(), p, "Player executed weather command, cleared it.");
					}else {
						api.noPerm(p);
						ActionLogger.log(api.getServerName(), p, "Player attempted to execute weather command.");
					}
				}else if(args[0].equalsIgnoreCase("rain")) {
					if(p.hasPermission("mlps.setweather")) {
						p.getWorld().setThundering(false);
						p.getWorld().setStorm(true);
						p.sendMessage(api.getPrefix(PrefixType.MAIN) + api.returnStringReady(p, "cmd.weather.notime").replace("%world", p.getWorld().getName()).replace("%weather", "rain").replace("%wetter", "Regen"));
						ActionLogger.log(api.getServerName(), p, "Player executed weather command, changed to rain.");
					}else {
						api.noPerm(p);
						ActionLogger.log(api.getServerName(), p, "Player attempted to execute weather command.");
					}
				}else if(args[0].equalsIgnoreCase("thunder")) {
					if(p.hasPermission("mlps.setweather")) {
						p.getWorld().setStorm(true);
						p.getWorld().setThundering(true);
						p.sendMessage(api.getPrefix(PrefixType.MAIN) + api.returnStringReady(p, "cmd.weather.notime").replace("%world", p.getWorld().getName()).replace("%weather", "thunder").replace("%wetter", "Gewitter"));
						ActionLogger.log(api.getServerName(), p, "Player executed weather command, changed to thunder.");
					}else {
						api.noPerm(p);
						ActionLogger.log(api.getServerName(), p, "Player attempted to execute weather command.");
					}
				}else {
					p.sendMessage(api.getPrefix(PrefixType.MAIN) + api.returnStringReady(p, "usage") + "§7 /weather <clear|rain|thunder> [time in seconds]");
				}
			}else if(args.length == 2) {
				if(args[0].equalsIgnoreCase("rain")) {
					if(p.hasPermission("mlps.setweather")) {
						int time = Integer.valueOf(args[1]);
						p.getWorld().setThundering(false);
						p.getWorld().setStorm(true);
						p.getWorld().setWeatherDuration(time);
						p.sendMessage(api.getPrefix(PrefixType.MAIN) + api.returnStringReady(p, "cmd.weather.time").replace("%world", p.getWorld().getName()).replace("%weather", "rain").replace("%wetter", "Regen").replace("%seconds", String.valueOf(time)));
						ActionLogger.log(api.getServerName(), p, "Player executed weather command, changed to rain, timed it for " + time + " seconds.");
					}else {
						api.noPerm(p);
						ActionLogger.log(api.getServerName(), p, "Player attempted to execute weather command.");
					}
				}else if(args[0].equalsIgnoreCase("thunder")) {
					if(p.hasPermission("mlps.setweather")) {
						int time = Integer.valueOf(args[1]);
						p.getWorld().setStorm(true);
						p.getWorld().setThundering(true);
						p.getWorld().setThunderDuration(time);
						p.sendMessage(api.getPrefix(PrefixType.MAIN) + api.returnStringReady(p, "cmd.weather.time").replace("%world", p.getWorld().getName()).replace("%weather", "thunder").replace("%wetter", "Gewitter").replace("%seconds", String.valueOf(time)));
						ActionLogger.log(api.getServerName(), p, "Player executed weather command, changed to thunder, timed it for " + time + " seconds.");
					}else {
						api.noPerm(p);
						ActionLogger.log(api.getServerName(), p, "Player attempted to execute weather command.");
					}
				}else {
					p.sendMessage(api.getPrefix(PrefixType.MAIN) + api.returnStringReady(p, "usage") + "§7 /weather <clear|rain|thunder> [time in seconds]");
				}
			}
		}
		return true;
	}
}