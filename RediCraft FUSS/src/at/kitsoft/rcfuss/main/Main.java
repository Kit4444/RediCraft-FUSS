package at.kitsoft.rcfuss.main;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import at.kitsoft.rcfuss.api.APIs;
import at.kitsoft.rcfuss.api.PrefixType;
import at.kitsoft.rcfuss.mysql.lb.MySQL;

public class Main extends JavaPlugin{
	
	static APIs api = new APIs();
	public static Main instance;
	public static String mysql_prefix = "§aMYSQL";
	public static String consolesend = api.getPrefix(PrefixType.MAIN) + "§cPlease use this command ingame!";
	public static ArrayList<String> serverlist = new ArrayList<>();
	public static boolean isLobby = false;
	
	public void onEnable() {
		instance = this;
		Bukkit.getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
		Bukkit.getMessenger().registerOutgoingPluginChannel(this, "redicraft:advbc");
		fillList();
		Manager manager = new Manager();
		manager.preInit();
		manager.init();
		manager.postInit();
		UpdateOnline(true);
	}
	
	public void onDisable() {
		instance = null;
		UpdateOnline(false);
		MySQL.disconnect();
	}
	
	private void fillList() {
		serverlist.add("FSurvival");
	}
	
	private void UpdateOnline(boolean online) {
		APIs api = new APIs();
		try {
			PreparedStatement ps = MySQL.getConnection().prepareStatement("UPDATE redicore_serverstats SET online = ? WHERE servername = ?");
			ps.setBoolean(1, online);
			ps.setString(2, api.getServerName());
			ps.executeUpdate();
			ps.closeOnCompletion();
			if(online == true) {
				PreparedStatement ps1 = MySQL.getConnection().prepareStatement("UPDATE redicore_serverstats SET onlinesince = ? WHERE servername = ?");
				SimpleDateFormat time = new SimpleDateFormat("dd/MM/yy - HH:mm:ss");
				ps1.setString(1, time.format(new Date()));
				ps1.setString(2, api.getServerName());
				ps1.executeUpdate();
			}
		}catch (SQLException e) { }
	}
}