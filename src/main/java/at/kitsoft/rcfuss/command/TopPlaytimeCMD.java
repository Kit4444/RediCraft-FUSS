package at.kitsoft.rcfuss.command;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import at.kitsoft.rcfuss.api.APIs;
import at.kitsoft.rcfuss.api.ActionLogger;
import at.kitsoft.rcfuss.api.PrefixType;
import at.kitsoft.rcfuss.mysql.lb.MySQL;

public class TopPlaytimeCMD implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(!(sender instanceof Player)) {
			sender.sendMessage("Eh, mock det ingame");
		}else {
			Player p = (Player)sender;
			APIs api = new APIs();
			if(args.length == 0) {
				p.sendMessage(api.getPrefix(PrefixType.MAIN) + api.returnStringReady(p, "usage") + " §7/topplaytime ");
				try {
					PreparedStatement ps = MySQL.getConnection().prepareStatement("SELECT * FROM redicore_userstats ORDER BY playtime DESC LIMIT 5");
					ResultSet rs = ps.executeQuery();
					int i = 1;
					while (rs.next()) {
						p.sendMessage("§7Top §a" + i + " §7| User: §a" + rs.getString("username") + " §7| Playtime: §a" + retPTime(rs.getInt("playtime")));
						i++;
					}
					ActionLogger.log(api.getServerName(), p, "Player executed topplaytime.");
				} catch (SQLException e) {
					e.printStackTrace();
				}
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
