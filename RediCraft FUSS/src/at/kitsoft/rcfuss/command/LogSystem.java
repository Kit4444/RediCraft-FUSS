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
import at.kitsoft.rcfuss.main.Main;
import at.kitsoft.rcfuss.mysql.lb.MySQL;
import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;

public class LogSystem implements CommandExecutor{
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(!(sender instanceof Player)) {
			Bukkit.getConsoleSender().sendMessage(Main.consolesend);
		}else {
			APIs api = new APIs();
			Player p = (Player)sender;
			String uuid = p.getUniqueId().toString().replace("-", "");
			if(cmd.getName().equalsIgnoreCase("login")) {
				if(p.hasPermission("mlps.canBan")) {
					if(retStatus(uuid)) {
						api.sendMSGReady(p, "cmd.login.already");
					}else {
						api.sendMSGReady(p, "cmd.login.success");
						updateStatus(uuid, true);
					}
					ActionLogger.log(api.getServerName(), p, "Player used login command.");
				}else {
					api.noPerm(p);
					ActionLogger.log(api.getServerName(), p, "Player attempted to use login command.");
				}
			}else if(cmd.getName().equalsIgnoreCase("logout")) {
				if(p.hasPermission("mlps.canBan")) {
					if(retStatus(uuid)) {
						api.sendMSGReady(p, "cmd.logout.success");
						updateStatus(uuid, false);
					}else {
						api.sendMSGReady(p, "cmd.logout.already");
					}
					ActionLogger.log(api.getServerName(), p, "Player used logout command.");
				}else {
					api.noPerm(p);
					ActionLogger.log(api.getServerName(), p, "Player attempted to use logout command.");
				}
			}else if(cmd.getName().equalsIgnoreCase("tg") || cmd.getName().equalsIgnoreCase("togglegroup")) {
				PermissionUser po = PermissionsEx.getUser(p);
				if(po.inGroup("st") || po.inGroup("bd") || po.inGroup("rltm") || po.inGroup("rtm") || po.inGroup("aot") || po.inGroup("part") || po.inGroup("cm") || po.inGroup("fs") || po.inGroup("ct") || po.inGroup("bt") || po.inGroup("nb") || po.inGroup("friend") || po.inGroup("train")) {
					if(retStatus(uuid)) {
						updateStatus(uuid, false);
						api.sendMSGReady(p, "cmd.tg.invisible");
					}else {
						updateStatus(uuid, true);
						api.sendMSGReady(p, "cmd.tg.visible");
					}
					ActionLogger.log(api.getServerName(), p, "Player used togglegroup command.");
				}else {
					api.noPerm(p);
					ActionLogger.log(api.getServerName(), p, "Player attempted to use tg command.");
				}
			}
		}
		return false;
	}
	
	private boolean retStatus(String uuid) {
		boolean boo = true;
		try {
			PreparedStatement ps = MySQL.getConnection().prepareStatement("SELECT * FROM redicore_userstats WHERE uuid = ?");
			ps.setString(1, uuid);
			ResultSet rs = ps.executeQuery();
			rs.next();
			boo = rs.getBoolean("loggedin");
		}catch (SQLException e) { e.printStackTrace(); }
		return boo;
	}
	
	private void updateStatus(String uuid, boolean boo) {
		try {
			PreparedStatement ps = MySQL.getConnection().prepareStatement("UPDATE redicore_userstats SET loggedin = ? WHERE uuid = ?");
			ps.setBoolean(1, boo);
			ps.setString(2, uuid);
			ps.executeUpdate();
		}catch (SQLException e) { e.printStackTrace(); }
	}

}
