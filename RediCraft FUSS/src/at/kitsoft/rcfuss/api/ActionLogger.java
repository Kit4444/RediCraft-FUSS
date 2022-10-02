package at.kitsoft.rcfuss.api;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import at.kitsoft.rcfuss.mysql.lb.MySQL;

public class ActionLogger {
	
	public static void log(String server, Player p, String action) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy - HH:mm:ss");
		try {
			PreparedStatement ps = MySQL.getConnection().prepareStatement("INSERT INTO redicore_actionlog(uuid, server, dateTime, actionType) VALUES (?, ?, ?, ?)");
			ps.setString(1, p.getUniqueId().toString());
			ps.setString(2, server);
			ps.setString(3, sdf.format(new Date()));
			ps.setString(4, action);
			ps.executeUpdate();
		} catch (SQLException e) {
		}
	}
	
	public static void log(String server, String issuer, String action) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy - HH:mm:ss");
		try {
			PreparedStatement ps = MySQL.getConnection().prepareStatement("INSERT INTO redicore_actionlog(uuid, server, dateTime, actionType) VALUES (?, ?, ?, ?)");
			ps.setString(1, issuer);
			ps.setString(2, server);
			ps.setString(3, sdf.format(new Date()));
			ps.setString(4, action);
			ps.executeUpdate();
		} catch (SQLException e) {
		}
	}
	
	public static void logwithCoords(String server, Player p, String action) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy - HH:mm:ss");
		try {
			PreparedStatement ps = MySQL.getConnection().prepareStatement("INSERT INTO redicore_actionlog(uuid, server, dateTime, actionType, x, y, z, world) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
			ps.setString(1, p.getUniqueId().toString());
			ps.setString(2, server);
			ps.setString(3, sdf.format(new Date()));
			ps.setString(4, action);
			ps.setInt(5, p.getLocation().getBlockX());
			ps.setInt(6, p.getLocation().getBlockY());
			ps.setInt(7, p.getLocation().getBlockZ());
			ps.setString(8, p.getLocation().getWorld().getName());
			ps.executeUpdate();
		} catch (SQLException e) {
		}
	}
	
	public static void logwithBCoords(String server, Player p, Location loc, String action) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy - HH:mm:ss");
		try {
			PreparedStatement ps = MySQL.getConnection().prepareStatement("INSERT INTO redicore_actionlog(uuid, server, dateTime, actionType, x, y, z, world) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
			ps.setString(1, p.getUniqueId().toString());
			ps.setString(2, server);
			ps.setString(3, sdf.format(new Date()));
			ps.setString(4, action);
			ps.setInt(5, loc.getBlockX());
			ps.setInt(6, loc.getBlockY());
			ps.setInt(7, loc.getBlockZ());
			ps.setString(8, p.getLocation().getWorld().getName());
			ps.executeUpdate();
		} catch (SQLException e) {
		}
	}

}
