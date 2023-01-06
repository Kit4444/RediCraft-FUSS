package at.kitsoft.rcfuss.command;

import java.util.LinkedList;
import java.util.Objects;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import at.kitsoft.rcfuss.api.APIs;
import at.kitsoft.rcfuss.api.ActionLogger;
import at.kitsoft.rcfuss.api.PrefixType;
import at.kitsoft.rcfuss.main.Main;

public class VanishCMD implements CommandExecutor, Listener{
	
	private static final LinkedList<UUID> vanishedPlayers = new LinkedList<>();

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(!(sender instanceof Player)) {
			
		}else {
			Player p = (Player)sender;
			APIs api = new APIs();
			if(args.length == 0) {
				if(p.hasPermission("mlps.vanish.own")) {
					if(!isVanished(p)) {
						vanishPlayer(p);
						p.sendMessage(api.getPrefix(PrefixType.MAIN) + api.returnStringReady(p, "cmd.vanish.own").replace("%state", "vanished"));
						ActionLogger.log(api.getServerName(), p, "Player executed vanish, enabled it.");
					}else {
						unvanishPlayer(p);
						p.sendMessage(api.getPrefix(PrefixType.MAIN) + api.returnStringReady(p, "cmd.vanish.own").replace("%state", "unvanished"));
						ActionLogger.log(api.getServerName(), p, "Player executed vanish, disabled it.");
					}
				}else {
					api.noPerm(p);
				}
			}else if(args.length == 1) {
				if(p.hasPermission("mlps.vanish.other")) {
					Player p2 = Bukkit.getPlayer(args[0]);
					if(p2 != null) {
						if(!isVanished(p2)) {
							vanishPlayer(p2);
							p.sendMessage(api.returnStringReady(p, "cmd.vanish.other.own").replace("%state", "vanished").replace("%displayer", p2.getDisplayName()));
							p2.sendMessage(api.returnStringReady(p, "cmd.vanish.other.other").replace("%state", "vanished").replace("%displayer", p.getDisplayName()));
							ActionLogger.log(api.getServerName(), p, "Player executed vanish, enabled it for " + p2.getName() + ".");
						}else {
							unvanishPlayer(p2);
							p.sendMessage(api.returnStringReady(p, "cmd.vanish.other.own").replace("%state", "unvanished").replace("%displayer", p2.getDisplayName()));
							p2.sendMessage(api.returnStringReady(p, "cmd.vanish.other.other").replace("%state", "unvanished").replace("%displayer", p.getDisplayName()));
							ActionLogger.log(api.getServerName(), p, "Player executed vanish, disabled it for " + p2.getName() + ".");
						}
					}else {
						api.sendMSGReady(p, "playernotonline");
					}
				}else {
					api.noPerm(p);
					ActionLogger.log(api.getServerName(), p, "Player attempted to execute vanish.");
				}
			}
		}
		return true;
	}
	
	@EventHandler
	public void onPlayerLogin(PlayerLoginEvent e) {
		Player p = e.getPlayer();
		for(UUID target : vanishedPlayers) {
			p.hidePlayer(Main.instance, Objects.requireNonNull(Bukkit.getPlayer(target)));
		}
	}
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent e) {
		Player p = e.getPlayer();
		if(isVanished(p)) {
			unvanishPlayer(p);
		}
	}
	
	boolean isVanished(Player p) {
		return vanishedPlayers.contains(p.getUniqueId());
	}
	
	boolean vanishPlayer(Player p) {
		if(!vanishedPlayers.contains(p.getUniqueId())) {
			hidePlayerFromPeople(p);
			vanishedPlayers.add(p.getUniqueId());
			return true;
		}else {
			return false;
		}
	}
	
	boolean unvanishPlayer(Player p) {
		if(vanishedPlayers.contains(p.getUniqueId())) {
			unhidePlayerFromPeople(p);
			vanishedPlayers.remove(p.getUniqueId());
			return true;
		}else {
			return false;
		}
	}
	
	void hidePlayerFromPeople(Player p) {
		for(Player target : Bukkit.getOnlinePlayers()) {
			if(target != p && target != null) {
				if(p != null) {
					target.hidePlayer(Main.instance, p);
				}
			}
		}
	}
	
	void unhidePlayerFromPeople(Player p) {
		for(Player target : Bukkit.getOnlinePlayers()) {
			if(target != p && target != null) {
				if(p != null) {
					target.showPlayer(Main.instance, p);
				}
			}
		}
	}
}