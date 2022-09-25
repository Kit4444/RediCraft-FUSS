package at.kitsoft.rcfuss.mysql.lb;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;

import org.bukkit.Bukkit;

import at.kitsoft.rcfuss.main.Main;

public class MySQL {

	public static Connection con;
	
	public static void connect(HashMap<String, String> creditentials) {
		if(!creditentials.isEmpty()) {
			if(creditentials.containsKey("user") && creditentials.containsKey("password") && creditentials.containsKey("ip") && creditentials.containsKey("port") && creditentials.containsKey("database")) {
				if(!isConnected()) {
					try {
						con = DriverManager.getConnection("jdbc:mysql://" + creditentials.get("ip") + ":" + creditentials.get("port") + "/" + creditentials.get("database"), creditentials.get("user"), creditentials.get("password"));
						Bukkit.getConsoleSender().sendMessage(Main.mysql_prefix + "The database connection has been established successfully.");
					} catch (SQLException e) {
						e.printStackTrace();
						Bukkit.getConsoleSender().sendMessage(Main.mysql_prefix + "§cError whilst establishing database connection.");
					}
				}
			}
		}
	}
	
	public static void disconnect() {
		if(isConnected()) {
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static boolean isConnected() {
		return (con == null ? false : true);
	}
	
	public static Connection getConnection() {
		return con;
	}
	
}
