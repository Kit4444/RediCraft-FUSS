package at.kitsoft.rcfuss.event;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerExpChangeEvent;

import at.kitsoft.rcfuss.api.APIs;
import at.kitsoft.rcfuss.mysql.lb.MySQL;

public class XP_Boost implements Listener{
	
	@EventHandler
	public void getXP(PlayerExpChangeEvent e) {
		Player p = e.getPlayer();
		APIs api = new APIs();
		if(hasBoost(p)) {
			e.setAmount((e.getAmount() * 2));
			api.sendHotbarMessage(p, "§aYou have received double XP. §7(" + (e.getAmount() * 2) + " XP)");
		}else {
			e.setAmount(e.getAmount());
		}
	}
	
	private boolean hasBoost(Player p) {
		boolean boost = false;
		long current = System.currentTimeMillis() / 1000;
		try {
			PreparedStatement ps = MySQL.getConnection().prepareStatement("SELECT gift_time,gift_key FROM redicore_userstats WHERE uuid = ?");
			ps.setString(1, p.getUniqueId().toString().replace("-", ""));
			ResultSet rs = ps.executeQuery();
			rs.next();
			long toTime = rs.getLong("gift_time");
			String giftkey = rs.getString("gift_key");
			if(giftkey.equalsIgnoreCase("3d:xpboost")) {
				if(current <= toTime) {
					boost = true;
				}
			}
			rs.close();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return boost;
	}

}
