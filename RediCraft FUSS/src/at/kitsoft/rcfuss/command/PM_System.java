package at.kitsoft.rcfuss.command;

import java.io.File;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import at.kitsoft.rcfuss.api.APIs;
import at.kitsoft.rcfuss.api.ActionLogger;
import at.kitsoft.rcfuss.api.PrefixType;
import at.kitsoft.rcfuss.main.Main;
import at.kitsoft.rcfuss.mysql.lb.MySQL;

public class PM_System implements CommandExecutor{
	
	File msgf = new File("plugins/RCUSS/msg.yml");
	
	private String prefix() {
		APIs api = new APIs();
		return api.getPrefix(PrefixType.PMSYSTEM);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(!(sender instanceof Player)) {
			Bukkit.getConsoleSender().sendMessage(Main.consolesend);
		}else {
			APIs api = new APIs();
			Player p = (Player)sender;
			if(cmd.getName().equalsIgnoreCase("msg")) {
				if(args.length == 0) {
					p.sendMessage(api.getPrefix(PrefixType.MAIN) + api.returnStringReady(p, "usage") + "§7 /msg <Player> <Message>");
				}else {
					Player p2 = Bukkit.getPlayerExact(args[0]);
					if(p2 == null) {
						api.sendMSGReady(p, "notonline");
					}else {
						YamlConfiguration cfg = YamlConfiguration.loadConfiguration(msgf);
						StringBuilder sb = new StringBuilder();
						for(int i = 1; i < args.length; i++) {
							sb.append(args[i]).append(" ");
						}
						String msg = sb.toString().trim();
						if(hasPMBlocked(p)) {
							api.sendMSGReady(p, "cmd.msg.blockedmsg");
						}else {
							if(hasPMBlocked(p2)) {
								if(p.hasPermission("mlps.bypassbpm")) {
									api.sendMSGReady(p, "cmd.msg.bypassmsg");
									p.sendMessage(prefix() + api.returnStringReady(p, "cmd.msg.you") + " §7§ " + p2.getDisplayName() + "§7: " + ChatColor.translateAlternateColorCodes('&', msg));
									p2.sendMessage(prefix() + p.getDisplayName() + " §7§ " + api.returnStringReady(p2, "cmd.msg.you") + "§7: " + ChatColor.translateAlternateColorCodes('&', msg));
									cfg.set(p.getName(), p2.getName());
									cfg.set(p2.getName(), p.getName());
									try {
										cfg.save(msgf);
									} catch (IOException e) {
										e.printStackTrace();
									}
								}else {
									api.sendMSGReady(p, "cmd.msg.playerblocked");
								}
							}else {
								p.sendMessage(prefix() + api.returnStringReady(p, "cmd.msg.you") + " §7§ " + p2.getDisplayName() + "§7: " + ChatColor.translateAlternateColorCodes('&', msg));
								p2.sendMessage(prefix() + p.getDisplayName() + " §7§ " + api.returnStringReady(p2, "cmd.msg.you") + "§7: " + ChatColor.translateAlternateColorCodes('&', msg));
								cfg.set(p.getName(), p2.getName());
								cfg.set(p2.getName(), p.getName());
								try {
									cfg.save(msgf);
								} catch (IOException e) {
									e.printStackTrace();
								}
							}
						}
					}
				}
			}else if(cmd.getName().equalsIgnoreCase("r")) {
				if(args.length == 0) {
					p.sendMessage(api.getPrefix(PrefixType.MAIN) + api.returnStringReady(p, "usage") + "§7 /r <Message>");
				}else {
					YamlConfiguration cfg = YamlConfiguration.loadConfiguration(msgf);
					Player p2 = Bukkit.getPlayer(cfg.getString(p.getName()));
					if(p2 == null) {
						api.sendMSGReady(p, "notonline");
					}else {
						StringBuilder sb = new StringBuilder();
						for(int i = 0; i < args.length; i++) {
							sb.append(args[i]).append(" ");
						}
						String msg = sb.toString().trim();
						if(hasPMBlocked(p2)) {
							if(p.hasPermission("mlps.bypassbpm")) {
								ActionLogger.log(api.getServerName(), p, "Player used msg command, bypassed the pm block of " + p2.getName() + " - Message: " + msg + ".");
								api.sendMSGReady(p, "cmd.msg.bypassmsg");
								p.sendMessage(prefix() + api.returnStringReady(p, "cmd.msg.you") + " §7§ " + p2.getDisplayName() + "§7: " + ChatColor.translateAlternateColorCodes('&', msg));
								p2.sendMessage(prefix() + p.getDisplayName() + " §7§ " + api.returnStringReady(p2, "cmd.msg.you") + "§7: " + ChatColor.translateAlternateColorCodes('&', msg));
								cfg.set(p.getName(), p2.getName());
								cfg.set(p2.getName(), p.getName());
								try {
									cfg.save(msgf);
								} catch (IOException e) {
									e.printStackTrace();
								}
							}else {
								api.sendMSGReady(p, "cmd.msg.playerblocked");
								ActionLogger.log(api.getServerName(), p, "Player attempted to pm " + p2.getName() + " but they blocked pms.");
							}
						}else {
							ActionLogger.log(api.getServerName(), p, "Player used msg command, recipient is " + p2.getName() + ", Message: " + msg + ".");
							p.sendMessage(prefix() + api.returnStringReady(p, "cmd.msg.you") + " §7§ " + p2.getDisplayName() + "§7: " + ChatColor.translateAlternateColorCodes('&', msg));
							p2.sendMessage(prefix() + p.getDisplayName() + " §7§ " + api.returnStringReady(p2, "cmd.msg.you") + "§7: " + ChatColor.translateAlternateColorCodes('&', msg));
						}
					}
				}
			}else if(cmd.getName().equalsIgnoreCase("blockmsg")) {
				if(hasPMBlocked(p)) {
					setBlockPM(p, false);
					api.sendMSGReady(p, "cmd.blockpm.remove");
					ActionLogger.log(api.getServerName(), p, "Player used blockmsg command, disabled it.");
				}else {
					setBlockPM(p, true);
					api.sendMSGReady(p, "cmd.blockpm.add");
					ActionLogger.log(api.getServerName(), p, "Player used blockmsg command, enabled it.");
				}
			}
		}
		return true;
	}
	
	private void setBlockPM(Player p, boolean status) {
		try {
			PreparedStatement ps = MySQL.getConnection().prepareStatement("UPDATE redicore_userstats SET disablePMs = ? WHERE uuid = ?");
			ps.setBoolean(1, status);
			ps.setString(2, p.getUniqueId().toString().replace("-", ""));
			ps.executeUpdate();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private boolean hasPMBlocked(Player p) {
		boolean pm = false;
		try {
			PreparedStatement ps = MySQL.getConnection().prepareStatement("SELECT disablePMs FROM redicore_userstats WHERE uuid = ?");
			ps.setString(1, p.getUniqueId().toString().replace("-", ""));
			ResultSet rs = ps.executeQuery();
			if(rs.next()) {
				pm = rs.getBoolean("disablePMs");
			}
			rs.close();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return pm;
	}

}
