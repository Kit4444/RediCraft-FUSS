package at.kitsoft.rcfuss.command;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import at.kitsoft.rcfuss.api.APIs;
import at.kitsoft.rcfuss.api.ActionLogger;
import at.kitsoft.rcfuss.api.PrefixType;
import at.kitsoft.rcfuss.mysql.lb.MySQL;

public class TPA_System implements CommandExecutor{
	
    static HashMap<UUID, UUID> tprequests = new HashMap<>();
    static HashMap<UUID, Boolean> tprequesttype = new HashMap<>();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(!(sender instanceof Player)) {
            Bukkit.getConsoleSender().sendMessage("§cBitte ingame ausf§hren.");
        }else {
        	APIs api = new APIs();
            Player p = (Player)sender;
            if(cmd.getName().equalsIgnoreCase("tpa")) {
                if(args.length == 0) {
                    p.sendMessage(api.getPrefix(PrefixType.MAIN) + api.returnStringReady(p, "usage") + "§7/tpa <Player>");
                }else if(args.length == 1) {
                    Player p2 = Bukkit.getPlayerExact(args[0]);
                    if(p2 == null) {
                        api.sendMSGReady(p, "cmd.tpa.notonline");
                    }else {
                        if(p2.getName() == p.getName()) {
                        	api.sendMSGReady(p, "cmd.tpa.ownrequest");
                        }else {
                            if(hasTPABlocked(p2)) {
                            	api.sendMSGReady(p, "cmd.tpa.playerblocked");
                            	ActionLogger.log(api.getServerName(), p, "Player attempted to execute tpa, but the recipient has tp-requests blocked.");
                            }else {
                                tprequests.put(p2.getUniqueId(), p.getUniqueId());
                                tprequesttype.put(p2.getUniqueId(), false);
                                p.sendMessage(api.getPrefix(PrefixType.MAIN) + api.returnStringReady(p, "cmd.tpa.success.ownmsg").replace("%displayer", p2.getDisplayName()));
                                p2.sendMessage(api.getPrefix(PrefixType.MAIN) + api.returnStringReady(p2, "cmd.tpa.success.othermsg.main").replace("%displayer", p.getDisplayName()));
                                api.sendMSGReady(p2, "cmd.tpa.success.othermsg.info");
                                ActionLogger.log(api.getServerName(), p, "Player executed tpa, recipient " + p2.getName() + " has accepted.");
                            }
                        }
                    }
                }else {
                	p.sendMessage(api.getPrefix(PrefixType.MAIN) + api.returnStringReady(p, "usage") + "§7/tpa <Player>");
                }
            }else if(cmd.getName().equalsIgnoreCase("tpahere")) {
            	if(args.length == 0) {
                    p.sendMessage(api.getPrefix(PrefixType.MAIN) + api.returnStringReady(p, "usage") + "§7/tpahere <Player>");
                }else if(args.length == 1) {
                    Player p2 = Bukkit.getPlayerExact(args[0]);
                    if(p2 == null) {
                        api.sendMSGReady(p, "cmd.tpahere.notonline");
                    }else {
                        if(p2.getName() == p.getName()) {
                        	api.sendMSGReady(p, "cmd.tpahere.ownrequest");
                        }else {
                            if(hasTPABlocked(p2)) {
                            	api.sendMSGReady(p, "cmd.tpahere.playerblocked");
                            	ActionLogger.log(api.getServerName(), p, "Player attempted to execute tpahere, recipient has blocked tp-requests.");
                            }else {
                                tprequests.put(p2.getUniqueId(), p.getUniqueId());
                                tprequesttype.put(p2.getUniqueId(), true);
                                p.sendMessage(api.getPrefix(PrefixType.MAIN) + api.returnStringReady(p, "cmd.tpahere.success.ownmsg").replace("%displayer", p2.getDisplayName()));
                                p2.sendMessage(api.getPrefix(PrefixType.MAIN) + api.returnStringReady(p2, "cmd.tpahere.success.othermsg.main").replace("%displayer", p.getDisplayName()));
                                api.sendMSGReady(p2, "cmd.tpahere.success.othermsg.info");
                                ActionLogger.log(api.getServerName(), p, "Player executed tpahere, recipient " + p2.getName() + " has accepted.");
                            }
                        }
                    }
                }else {
                	p.sendMessage(api.getPrefix(PrefixType.MAIN) + api.returnStringReady(p, "usage") + "§7/tpa <Player>");
                }
            }else if(cmd.getName().equalsIgnoreCase("tpaccept")) {
                if(tprequests.containsKey(p.getUniqueId())) {
                    Player p2 = Bukkit.getPlayer(tprequests.get(p.getUniqueId()));
                    boolean state = tprequesttype.get(p.getUniqueId());
                    if(state == true) {
                    	p.teleport(p2);
                    }else {
                    	p2.teleport(p);
                    }
                    tprequests.remove(p.getUniqueId());
                    tprequesttype.remove(p.getUniqueId());
                    ActionLogger.log(api.getServerName(), p, "Player executed tpaccept.");
                }else {
                    api.sendMSGReady(p, "cmd.tpaccept.noreqopen");
                }
            }else if(cmd.getName().equalsIgnoreCase("tpdeny")) {
                if(tprequests.containsKey(p.getUniqueId())) {
                    tprequests.remove(p.getUniqueId());
                    tprequesttype.remove(p.getUniqueId());
                    api.sendMSGReady(p, "cmd.tpdeny.declined");
                    ActionLogger.log(api.getServerName(), p, "Player executed tpdeny.");
                }else {
                	api.sendMSGReady(p, "cmd.tpdeny.noreqopen");
                }
            }else if(cmd.getName().equalsIgnoreCase("blocktpa")) {
                if(hasTPABlocked(p)) {
                    setTPABlocked(p, false);
                    api.sendMSGReady(p, "cmd.blocktpa.removed");
                    ActionLogger.log(api.getServerName(), p, "Player executed blocktpa, disabled it.");
                }else {
                	setTPABlocked(p, true);
                    api.sendMSGReady(p, "cmd.blocktpa.added");
                    ActionLogger.log(api.getServerName(), p, "Player executed blocktpa, enabled it.");
                }
            }
        }
        return false;
    }
    
    private boolean hasTPABlocked(Player p) {
    	boolean block = false;
    	try {
			PreparedStatement ps = MySQL.getConnection().prepareStatement("SELECT disableTPAR FROM redicore_userstats WHERE uuid = ?");
			ps.setString(1, p.getUniqueId().toString().replace("-", ""));
			ResultSet rs = ps.executeQuery();
			if(rs.next()) {
				block = rs.getBoolean("disableTPAR");
			}
			rs.close();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
    	return block;
    }
    
    private void setTPABlocked(Player p, boolean status) {
    	try {
			PreparedStatement ps = MySQL.getConnection().prepareStatement("UPDATE redicore_userstats SET disableTPAR = ? WHERE uuid = ?");
			ps.setBoolean(1, status);
			ps.setString(2, p.getUniqueId().toString().replace("-", ""));
			ps.executeUpdate();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
    }

}
