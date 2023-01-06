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

public class SpeedCMD implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(!(sender instanceof Player)) {
			Bukkit.getConsoleSender().sendMessage(Main.consolesend);
		}else {
			Player p = (Player)sender;
			APIs api = new APIs();
			if(args.length == 0) {
				p.sendMessage(api.getPrefix(PrefixType.MAIN) + api.returnStringReady(p, "usage") + " §7/speed <Speed> | Speed = Use a number between 0 - 10 or default");
			}else if(args.length == 1) {
				if(args[0].matches("^[0-9]+$")) {
					int speed = Integer.parseInt(args[0]);
					switch(speed) {
					case 0: if(p.hasPermission("mlps.speed")) {
						if(p.isFlying()) {
							p.setFlySpeed((float) 0);
							p.sendMessage(api.getPrefix(PrefixType.MAIN) + api.returnStringReady(p, "cmd.speed.fly").replace("%speed", String.valueOf(0)));
							ActionLogger.log(api.getServerName(), p, "Player executed the speed command, flymode.");
						}else {
							p.setWalkSpeed((float) 0);
							p.sendMessage(api.getPrefix(PrefixType.MAIN) + api.returnStringReady(p, "cmd.speed.walk").replace("%speed", String.valueOf(0)));
							ActionLogger.log(api.getServerName(), p, "Player executed the speed command, walkmode.");
						}
					}else {
						api.noPerm(p);
					} break;
					case 1: if(p.hasPermission("mlps.speed")) {
						if(p.isFlying()) {
							p.setFlySpeed((float) 0.1);
							p.sendMessage(api.getPrefix(PrefixType.MAIN) + api.returnStringReady(p, "cmd.speed.fly").replace("%speed", String.valueOf(1)));
							ActionLogger.log(api.getServerName(), p, "Player executed the speed command, flymode.");
						}else {
							p.setWalkSpeed((float) 0.1);
							p.sendMessage(api.getPrefix(PrefixType.MAIN) + api.returnStringReady(p, "cmd.speed.walk").replace("%speed", String.valueOf(1)));
							ActionLogger.log(api.getServerName(), p, "Player executed the speed command, walkmode.");
						}
					}else {
						api.noPerm(p);
					} break;
					case 2: if(p.hasPermission("mlps.speed")) {
						if(p.isFlying()) {
							p.setFlySpeed((float) 0.2);
							p.sendMessage(api.getPrefix(PrefixType.MAIN) + api.returnStringReady(p, "cmd.speed.fly").replace("%speed", String.valueOf(2)));
							ActionLogger.log(api.getServerName(), p, "Player executed the speed command, flymode.");
						}else {
							p.setWalkSpeed((float) 0.2);
							p.sendMessage(api.getPrefix(PrefixType.MAIN) + api.returnStringReady(p, "cmd.speed.walk").replace("%speed", String.valueOf(2)));
							ActionLogger.log(api.getServerName(), p, "Player executed the speed command, walkmode.");
						}
					}else {
						api.noPerm(p);
					} break;
					case 3: if(p.hasPermission("mlps.speed")) {
						if(p.isFlying()) {
							p.setFlySpeed((float) 0.3);
							p.sendMessage(api.getPrefix(PrefixType.MAIN) + api.returnStringReady(p, "cmd.speed.fly").replace("%speed", String.valueOf(3)));
							ActionLogger.log(api.getServerName(), p, "Player executed the speed command, flymode.");
						}else {
							p.setWalkSpeed((float) 0.3);
							p.sendMessage(api.getPrefix(PrefixType.MAIN) + api.returnStringReady(p, "cmd.speed.walk").replace("%speed", String.valueOf(3)));
							ActionLogger.log(api.getServerName(), p, "Player executed the speed command, walkmode.");
						}
					}else {
						api.noPerm(p);
					} break;
					case 4: if(p.hasPermission("mlps.speed")) {
						if(p.isFlying()) {
							p.setFlySpeed((float) 0.4);
							p.sendMessage(api.getPrefix(PrefixType.MAIN) + api.returnStringReady(p, "cmd.speed.fly").replace("%speed", String.valueOf(4)));
							ActionLogger.log(api.getServerName(), p, "Player executed the speed command, flymode.");
						}else {
							p.setWalkSpeed((float) 0.4);
							p.sendMessage(api.getPrefix(PrefixType.MAIN) + api.returnStringReady(p, "cmd.speed.walk").replace("%speed", String.valueOf(4)));
							ActionLogger.log(api.getServerName(), p, "Player executed the speed command, walkmode.");
						}
					}else {
						api.noPerm(p);
					} break;
					case 5: if(p.hasPermission("mlps.speed")) {
						if(p.isFlying()) {
							p.setFlySpeed((float) 0.5);
							p.sendMessage(api.getPrefix(PrefixType.MAIN) + api.returnStringReady(p, "cmd.speed.fly").replace("%speed", String.valueOf(5)));
							ActionLogger.log(api.getServerName(), p, "Player executed the speed command, flymode.");
						}else {
							p.setWalkSpeed((float) 0.5);
							p.sendMessage(api.getPrefix(PrefixType.MAIN) + api.returnStringReady(p, "cmd.speed.walk").replace("%speed", String.valueOf(5)));
							ActionLogger.log(api.getServerName(), p, "Player executed the speed command, walkmode.");
						}
					}else {
						api.noPerm(p);
					} break;
					case 6: if(p.hasPermission("mlps.speed")) {
						if(p.isFlying()) {
							p.setFlySpeed((float) 0.6);
							p.sendMessage(api.getPrefix(PrefixType.MAIN) + api.returnStringReady(p, "cmd.speed.fly").replace("%speed", String.valueOf(6)));
							ActionLogger.log(api.getServerName(), p, "Player executed the speed command, flymode.");
						}else {
							p.setWalkSpeed((float) 0.6);
							p.sendMessage(api.getPrefix(PrefixType.MAIN) + api.returnStringReady(p, "cmd.speed.walk").replace("%speed", String.valueOf(6)));
							ActionLogger.log(api.getServerName(), p, "Player executed the speed command, walkmode.");
						}
					}else {
						api.noPerm(p);
					} break;
					case 7: if(p.hasPermission("mlps.speed")) {
						if(p.isFlying()) {
							p.setFlySpeed((float) 0.7);
							p.sendMessage(api.getPrefix(PrefixType.MAIN) + api.returnStringReady(p, "cmd.speed.fly").replace("%speed", String.valueOf(7)));
							ActionLogger.log(api.getServerName(), p, "Player executed the speed command, flymode.");
						}else {
							p.setWalkSpeed((float) 0.7);
							p.sendMessage(api.getPrefix(PrefixType.MAIN) + api.returnStringReady(p, "cmd.speed.walk").replace("%speed", String.valueOf(7)));
							ActionLogger.log(api.getServerName(), p, "Player executed the speed command, walkmode.");
						}
					}else {
						api.noPerm(p);
					} break;
					case 8: if(p.hasPermission("mlps.speed")) {
						if(p.isFlying()) {
							p.setFlySpeed((float) 0.8);
							p.sendMessage(api.getPrefix(PrefixType.MAIN) + api.returnStringReady(p, "cmd.speed.fly").replace("%speed", String.valueOf(8)));
							ActionLogger.log(api.getServerName(), p, "Player executed the speed command, flymode.");
						}else {
							p.setWalkSpeed((float) 0.8);
							p.sendMessage(api.getPrefix(PrefixType.MAIN) + api.returnStringReady(p, "cmd.speed.walk").replace("%speed", String.valueOf(8)));
							ActionLogger.log(api.getServerName(), p, "Player executed the speed command, walkmode.");
						}
					}else {
						api.noPerm(p);
					} break;
					case 9: if(p.hasPermission("mlps.speed")) {
						if(p.isFlying()) {
							p.setFlySpeed((float) 0.9);
							p.sendMessage(api.getPrefix(PrefixType.MAIN) + api.returnStringReady(p, "cmd.speed.fly").replace("%speed", String.valueOf(9)));
							ActionLogger.log(api.getServerName(), p, "Player executed the speed command, flymode.");
						}else {
							p.setWalkSpeed((float) 0.9);
							p.sendMessage(api.getPrefix(PrefixType.MAIN) + api.returnStringReady(p, "cmd.speed.walk").replace("%speed", String.valueOf(9)));
							ActionLogger.log(api.getServerName(), p, "Player executed the speed command, walkmode.");
						}
					}else {
						api.noPerm(p);
					} break;
					case 10: if(p.hasPermission("mlps.speed")) {
						if(p.isFlying()) {
							p.setFlySpeed((float) 1);
							p.sendMessage(api.getPrefix(PrefixType.MAIN) + api.returnStringReady(p, "cmd.speed.fly").replace("%speed", String.valueOf(10)));
							ActionLogger.log(api.getServerName(), p, "Player executed the speed command, flymode.");
						}else {
							p.setWalkSpeed((float) 1);
							p.sendMessage(api.getPrefix(PrefixType.MAIN) + api.returnStringReady(p, "cmd.speed.walk").replace("%speed", String.valueOf(10)));
							ActionLogger.log(api.getServerName(), p, "Player executed the speed command, walkmode.");
						}
					}else {
						api.noPerm(p);
					} break;
					default: api.sendMSGReady(p, "cmd.speed.invalidnumber");
					}
				}else if(args[0].equalsIgnoreCase("default")){
					if(p.hasPermission("mlps.speed")) {
						if(p.isFlying()) {
							p.setFlySpeed((float) 0.2);
							p.sendMessage(api.getPrefix(PrefixType.MAIN) + api.returnStringReady(p, "cmd.speed.fly").replace("%speed", String.valueOf(2)));
							ActionLogger.log(api.getServerName(), p, "Player executed the speed command, flymode, reset.");
						}else {
							p.setWalkSpeed((float) 0.2);
							p.sendMessage(api.getPrefix(PrefixType.MAIN) + api.returnStringReady(p, "cmd.speed.walk").replace("%speed", String.valueOf(2)));
							ActionLogger.log(api.getServerName(), p, "Player executed the speed command, walkmode, reset.");
						}
					}else {
						api.noPerm(p);
					}
				}else {
					api.sendMSGReady(p, "cmd.speed.onlynumbersallowed");
				}
			}
		}
		return false;
	}

}
