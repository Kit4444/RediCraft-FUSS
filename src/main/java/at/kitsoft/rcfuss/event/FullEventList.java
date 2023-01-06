package at.kitsoft.rcfuss.event;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerBedLeaveEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerEggThrowEvent;
import org.bukkit.event.player.PlayerExpChangeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLevelChangeEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import at.kitsoft.rcfuss.api.APIs;
import at.kitsoft.rcfuss.api.ActionLogger;

public class FullEventList implements Listener{
	
	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		APIs api = new APIs();
		Player p = e.getPlayer();
		ActionLogger.log(api.getServerName(), p, "Player joined the server.");
	}
	
	@EventHandler
	public void onQuit(PlayerQuitEvent e) {
		APIs api = new APIs();
		Player p = e.getPlayer();
		ActionLogger.log(api.getServerName(), p, "Player left the server.");
	}
	
	@EventHandler
	public void onDie(PlayerDeathEvent e) {
		APIs api = new APIs();
		Player p = e.getEntity();
		ActionLogger.logwithCoords(api.getServerName(), p, "Player died.");
		//ActionLogger.log(api.getServerName(), p, "Player died in " + loc.getWorld().getName() + " at X:" + loc.getBlockX() + ", Y:" + loc.getBlockY() + ", Z:" + loc.getBlockZ() + ".");
	}
	
	@EventHandler
	public void bedEnter(PlayerBedEnterEvent e) {
		APIs api = new APIs();
		Player p = e.getPlayer();
		ActionLogger.logwithCoords(api.getServerName(), p, "Player entered a bed.");
		//ActionLogger.log(api.getServerName(), p, "Player entered a bed at " + loc.getWorld().getName() + " at X:" + loc.getBlockX() + ", Y:" + loc.getBlockY() + ", Z:" + loc.getBlockZ() + ".");
	}
	
	@EventHandler
	public void bedLeave(PlayerBedLeaveEvent e) {
		APIs api = new APIs();
		Player p = e.getPlayer();
		ActionLogger.logwithCoords(api.getServerName(), p, "Player left a bed.");
		//ActionLogger.log(api.getServerName(), p, "Player left a bed at " + loc.getWorld().getName() + " at X:" + loc.getBlockX() + ", Y:" + loc.getBlockY() + ", Z:" + loc.getBlockZ() + ".");
	}
	
	@EventHandler
	public void dropItem(PlayerDropItemEvent e) {
		APIs api = new APIs();
		Player p = e.getPlayer();
		ActionLogger.logwithCoords(api.getServerName(), p, "Player dropped an Item: " + e.getItemDrop().getName());
	}
	
	@EventHandler
	public void eggThrow(PlayerEggThrowEvent e) {
		APIs api = new APIs();
		Player p = e.getPlayer();
		ActionLogger.logwithCoords(api.getServerName(), p, "Player threw an egg.");
	}
	
	@EventHandler
	public void expChange(PlayerExpChangeEvent e) {
		APIs api = new APIs();
		Player p = e.getPlayer();
		ActionLogger.log(api.getServerName(), p, "Player has changed Experience Points. Amount:" + e.getAmount());
	}
	
	@EventHandler
	public void levelChange(PlayerLevelChangeEvent e) {
		APIs api = new APIs();
		Player p = e.getPlayer();
		ActionLogger.log(api.getServerName(), p, "Player has changed Level. Old: " + e.getOldLevel() + ", New: " + e.getNewLevel());
	}
	
	@EventHandler
	public void itemPickup(EntityPickupItemEvent e) {
		APIs api = new APIs();
		if(e.getEntityType() == EntityType.PLAYER) {
			Player p = (Player) e.getEntity();
			ActionLogger.logwithCoords(api.getServerName(), p, "Player picked up an Item: " + e.getItem().getName());
		}
	}
	
	@EventHandler
	public void respawnPlayer(PlayerRespawnEvent e) {
		APIs api = new APIs();
		Player p = e.getPlayer();
		ActionLogger.logwithCoords(api.getServerName(), p, "Player respawned.");
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onBlockPlace(BlockPlaceEvent e) {
		APIs api = new APIs();
		Player p = e.getPlayer();
		ActionLogger.logwithBCoords(api.getServerName(), p, e.getBlock().getLocation(), "Player placed a block: " + e.getBlockPlaced().getType().getId());
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onBlockBreak(BlockBreakEvent e) {
		APIs api = new APIs();
		Player p = e.getPlayer();
		ActionLogger.logwithBCoords(api.getServerName(), p, e.getBlock().getLocation(), "Player broke a block: " + e.getBlock().getType().getId());
	}
	
	@EventHandler
	public void onChat(AsyncPlayerChatEvent e) {
		APIs api = new APIs();
		Player p = e.getPlayer();
		ActionLogger.log(api.getServerName(), p, "Player has sent a message: " + e.getMessage());
	}

}
