package at.kitsoft.rcfuss.event;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import at.kitsoft.rcfuss.api.APIs;
import at.kitsoft.rcfuss.mysql.lb.MySQL;

public class MOTDJoin implements Listener{
	
	/*
	 * 
	 */
	
	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		APIs api = new APIs();
		p.sendMessage("§7--------[§aMessage of the Day§7]--------"); //wont be translated
		p.sendMessage("§7You have joined %server".replace("%server", api.getServerName())); //will be translated
		p.sendMessage("§7Enjoy playing on our server."); //will be translated
		sendTop3MOTDMessages(p);
	}
	
	public void sendTop3MOTDMessages(Player p) {
		try {
			PreparedStatement ps = MySQL.getConnection().prepareStatement("SELECT * FROM redicore_motd ORDER BY id DESC LIMIT 3");
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				p.sendMessage(" ");
				p.sendMessage("§7" + rs.getString("title"));
				p.sendMessage("§7" + rs.getString("main_message").replace('|', '\n'));
				SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy - HH:mm:ss");
				long timestamp = rs.getLong("timestamp");
				p.sendMessage("§7Published at: §a" + sdf.format(new Date(timestamp)));
			}
			rs.close();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
