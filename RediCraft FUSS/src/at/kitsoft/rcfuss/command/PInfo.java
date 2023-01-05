package at.kitsoft.rcfuss.command;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import at.kitsoft.rcfuss.api.APIs;
import at.kitsoft.rcfuss.api.ActionLogger;
import at.kitsoft.rcfuss.api.PrefixType;
import at.kitsoft.rcfuss.main.Main;
import at.kitsoft.rcfuss.mysql.lb.MySQL;

public class PInfo implements CommandExecutor{
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(!(sender instanceof Player)) {
			Bukkit.getConsoleSender().sendMessage(Main.consolesend);
		}else {
			APIs api = new APIs();
			Player p = (Player)sender;
			if(args.length == 1) {
				if(args[0].matches("^[0-9]+$")) {
					int id = Integer.parseInt(args[0]);
					try {
						ActionLogger.log(api.getServerName(), p, "Player used pinfo command.");
						PreparedStatement ps = MySQL.getConnection().prepareStatement("SELECT * FROM redicore_userstats WHERE userid = ?");
						ps.setInt(1, id);
						ResultSet rs = ps.executeQuery();
						if(rs.next()) {
							p.sendMessage("§7================[§aPlayerInfo§7]================");
							p.sendMessage("§7Username: §a" + rs.getString("username"));
							p.sendMessage("§7UUID: §a" + rs.getString("uuid"));
							p.sendMessage("§7Custom Prefix:§f " + rs.getString("userprefix"));
							p.sendMessage("§7Role: §a" + rs.getString("userrank"));
							p.sendMessage("§7Playtime: §a" + retPTime(rs.getInt("playtime")));
							p.sendMessage("§7First Join: §a" + rs.getString("firstjoinstring"));
							String lang = rs.getString("language");
							switch(lang) {
								case "en-UK": p.sendMessage("§7Language: §aEnglish"); break;
								case "de-de": p.sendMessage("§7Language: §aDeutsch"); break;
								default: p.sendMessage("§7Language: §aunknown Language"); break;
							}
							if(rs.getBoolean("online")) {
								p.sendMessage("§7Online: §ayes");
								p.sendMessage("§7Server: §a" + rs.getString("server"));
							}else {
								p.sendMessage("§7Online: §cno");
							}
							if(rs.getBoolean("isstaff")) {
								p.sendMessage("§7Staff Member: §ayes");
							}else {
								p.sendMessage("§7Staff Member: §cno");
							}
							if(p.hasPermission("mlps.canBan")) {
								p.sendMessage("§7Last Join: §a" + rs.getString("lastjoinstring"));
								p.sendMessage("§7Money Cash/Bank: §a" + rs.getInt("money") + " §7/§a " + rs.getInt("bankmoney") + " §7Coins");
							}
							if(p.hasPermission("*")) {
								p.sendMessage("§7Firstjoin IP: §a" + rs.getString("firstjoinip"));
								p.sendMessage("§7Last login IP: §a" + rs.getString("lastloginip"));
							}
						}else {
							p.sendMessage(api.getPrefix(PrefixType.MAIN) + "§cSorry, we do not have an player for that ID.");
						}
						
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}else {
					api.sendMSGReady(p, "cmd.pinfo.onlynumchars");
				}
			}else {
				p.sendMessage(api.getPrefix(PrefixType.MAIN) + api.returnStringReady(p, "usage") + " §7/pinfo <PlayerID>");
			}
		}
		return false;
	}
	
	private String retPTime(int time) {
		long seconds = time;
		long minutes = 0;
		long hours = 0;
		while(seconds > 60) {
			seconds -= 60;
			minutes++;
		}
		while(minutes > 60) {
			minutes -= 60;
			hours++;
		}
		return hours + " Hours, " + minutes + " Minutes and " + seconds + " Seconds";
	}

}
