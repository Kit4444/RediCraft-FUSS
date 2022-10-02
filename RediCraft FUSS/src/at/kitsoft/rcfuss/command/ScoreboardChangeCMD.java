package at.kitsoft.rcfuss.command;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import at.kitsoft.rcfuss.api.APIs;
import at.kitsoft.rcfuss.api.ActionLogger;
import at.kitsoft.rcfuss.api.PrefixType;
import at.kitsoft.rcfuss.event.SBHandler;
import at.kitsoft.rcfuss.main.Main;
import at.kitsoft.rcfuss.mysql.lb.MySQL;

public class ScoreboardChangeCMD implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(!(sender instanceof Player)) {
			Bukkit.getConsoleSender().sendMessage(Main.consolesend);
		}else {
			Player p = (Player)sender;
			APIs api = new APIs();
			String uuid = p.getUniqueId().toString().replace("-", "");
			if(args.length == 1) {
				ActionLogger.log(api.getServerName(), p, "Player changed the Scoreboard-State.");
				if(Main.serverlist.contains(api.getServerName())) {
					if(args[0].equalsIgnoreCase("off")) {
						updateSB(uuid, 0);
						api.sendMSGReady(p, "cmd.scoreboard.off");
					}else if(args[0].equalsIgnoreCase("default")) {
						updateSB(uuid, 1);
						api.sendMSGReady(p, "cmd.scoreboard.default");
					}else if(args[0].equalsIgnoreCase("job")) {
						updateSB(uuid, 2);
						api.sendMSGReady(p, "cmd.scoreboard.job");
					}else if(args[0].equalsIgnoreCase("admin")) {
						if(p.hasPermission("mlps.canBan")) {
							updateSB(uuid, 3);
							api.sendMSGReady(p, "cmd.scoreboard.admin");
						}else {
							api.noPerm(p);
						}
					}else if(args[0].equalsIgnoreCase("data")) {
						if(p.hasPermission("mlps.isSA")) {
							updateSB(uuid, 4);
							api.sendMSGReady(p, "cmd.scoreboard.data");
						}else {
							api.noPerm(p);
						}
					}else if(args[0].equalsIgnoreCase("redifm")) {
						updateSB(uuid, 5);
					}else if(args[0].equalsIgnoreCase("players")) {
						updateSB(uuid, 6);
					}else if(args[0].equalsIgnoreCase("location")) {
						updateSB(uuid, 7);
					}
					SBHandler sb = new SBHandler();
					sb.setScoreboard(p);
				}else {
					if(args[0].equalsIgnoreCase("off")) {
						updateSB(uuid, 0);
						api.sendMSGReady(p, "cmd.scoreboard.off");
					}else if(args[0].equalsIgnoreCase("default")) {
						updateSB(uuid, 1);
						api.sendMSGReady(p, "cmd.scoreboard.default");
					}else if(args[0].equalsIgnoreCase("job")) {
						api.sendMSGReady(p, "cmd.scoreboard.creajob");
					}else if(args[0].equalsIgnoreCase("admin")) {
						if(p.hasPermission("mlps.canBan")) {
							updateSB(uuid, 3);
							api.sendMSGReady(p, "cmd.scoreboard.admin");
						}else {
							api.noPerm(p);
						}
					}else if(args[0].equalsIgnoreCase("data")) {
						if(p.hasPermission("mlps.isSA")) {
							updateSB(uuid, 4);
							api.sendMSGReady(p, "cmd.scoreboard.data");
						}else {
							api.noPerm(p);
						}
					}else if(args[0].equalsIgnoreCase("redifm")) {
						updateSB(uuid, 5);
					}else if(args[0].equalsIgnoreCase("players")) {
						updateSB(uuid, 6);
					}else if(args[0].equalsIgnoreCase("location")) {
						updateSB(uuid, 7);
					}
					SBHandler sb = new SBHandler();
					sb.setScoreboard(p);
				}
			}else {
				p.sendMessage(api.getPrefix(PrefixType.MAIN) + api.returnStringReady(p, "usage") + "§7/sb <off|default|players|job|redifm|location|admin|data>");
			}
		}
		return false;
	}
	
	private void updateSB(String uuid, int id) {
		try {
			PreparedStatement ps = MySQL.getConnection().prepareStatement("UPDATE redicore_userstats SET scoreboard = ? WHERE uuid = ?");
			ps.setInt(1, id);
			ps.setString(2, uuid);
			ps.executeUpdate();
			ps.close();
		}catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
