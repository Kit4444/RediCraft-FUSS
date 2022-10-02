package at.kitsoft.rcfuss.event;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import at.kitsoft.rcfuss.api.APIs;
import at.kitsoft.rcfuss.api.PrefixType;
import at.kitsoft.rcfuss.main.Main;
import at.kitsoft.rcfuss.mysql.lb.MySQL;

public class Serverteleporter implements CommandExecutor, Listener{
	
	private static Main plugin;
	public Serverteleporter(Main m) {
		plugin = m;
	}

	public static String title = "§aServer§cNavigator";
	static String lobby = "§6Lobby";
	static String spawn = "§aSpawn";
	static String creative = "§eCreative";
	static String survival = "§cSurvival";
	
	static String wt_inventory = "§aWorld§cTeleporter";
	static String wt_freebuild = "§aFreebuild";
	static String wt_plotworld = "§aPlotworld";
	static String wt_theend = "§5The End";
	static String wt_nether = "§cNether";
	
	static String locked = " §7- §4locked";
	static String monitored = " §7- §9monitoring";
	static String offline = " §7- §eoffline";
	
	static File spawnfile = new File("plugins/RCFUSS/spawn.yml");
	
	public static void mainnavi(Player p) {
		APIs api = new APIs();
		Inventory inv = Bukkit.createInventory(null, 9*3, title);
		for(int i = 0; i < 26; i++) {
			inv.setItem(i, api.defItem(Material.STAINED_GLASS_PANE, 1, 15, "§0"));
		}
		if(Main.serverlist.contains(api.getServerName())) {
			inv.setItem(10, api.naviItem(Material.IRON_AXE, survival, "-FSurvival"));
			inv.setItem(11, api.naviItem(Material.DIAMOND_PICKAXE, creative, "-FCreative"));
			inv.setItem(13, api.defItem(Material.EMERALD, 1, 0, wt_inventory));
			inv.setItem(15, api.naviItem(Material.NETHER_STAR, lobby, "Lobby"));
		}else {
			inv.setItem(10, api.naviItem(Material.IRON_AXE, survival, "-FSurvival"));
			inv.setItem(12, api.defItem(Material.EMERALD, 1, 0, spawn));
			inv.setItem(14, api.naviItem(Material.NETHER_STAR, lobby, "Lobby"));
			inv.setItem(16, api.naviItem(Material.DIAMOND_PICKAXE, creative, "-FCreative"));
		}
		p.openInventory(inv);
		p.updateInventory();
	}
	
	public static void worldTPer(Player p) {
		APIs api = new APIs();
		Inventory inv = Bukkit.createInventory(null, 9*1, wt_inventory);
		ItemStack glassPane = api.defItem(Material.STAINED_GLASS_PANE, 1, 15, "§c");
		inv.setItem(0, glassPane);
		inv.setItem(1, api.wt_item(Material.GRASS, wt_freebuild, "world"));
		inv.setItem(2, glassPane);
		inv.setItem(3, api.wt_item(Material.GRASS, wt_plotworld, "plotworld"));
		inv.setItem(4, glassPane);
		inv.setItem(5, api.wt_item(Material.NETHERRACK, wt_nether, "world_nether"));
		inv.setItem(6, glassPane);
		inv.setItem(7, api.wt_item(Material.END_BRICKS, wt_theend, "world_the_end"));
		inv.setItem(8, glassPane);
		p.openInventory(inv);
		p.updateInventory();
	}
	
	@EventHandler
	public void onInventoryClick(InventoryClickEvent e) {
		APIs api = new APIs();
		HumanEntity he = e.getWhoClicked();
		if(he.getType() == EntityType.PLAYER) {
			Player p = (Player) he;
			if(e.getClickedInventory().getName().equalsIgnoreCase(title)) {
				e.setCancelled(true);
				if(e.getCurrentItem() == null) return;
				YamlConfiguration cfg = YamlConfiguration.loadConfiguration(spawnfile);
				if(e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(creative)) {
					e.setCancelled(true);
					boolean lock = getData("-FCreative", "locked");
					if(lock) {
						p.sendMessage(api.getPrefix(PrefixType.MAIN) + api.returnStringReady(p, "event.navigator.sendPlayer.locked").replace("%server", "Creative"));
					}else {
						boolean monitor = getData("-FCreative", "monitoring");
						boolean online = getData("-FCreative", "online");
						if(online) {
							if(monitor) {
								api.sendMSGReady(p, "event.navigator.sendPlayer.monitorinfo");
								sendPlayer(p, "forgeCreative", creative);
							}else {
								sendPlayer(p, "forgeCreative", creative);
							}
						}else {
							p.sendMessage(api.getPrefix(PrefixType.MAIN) + api.returnStringReady(p, "event.navigator.sendPlayer.offline").replace("%server", "Creative"));
						}
					}
				}else if(e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(survival)) {
					e.setCancelled(true);
					boolean lock = getData("-FSurvival", "locked");
					if(lock) {
						p.sendMessage(api.getPrefix(PrefixType.MAIN) + api.returnStringReady(p, "event.navigator.sendPlayer.locked").replace("%server", "Survival"));
					}else {
						boolean monitor = getData("-FSurvival", "monitoring");
						boolean online = getData("-FSurvival", "online");
						if(online) {
							if(monitor) {
								api.sendMSGReady(p, "event.navigator.sendPlayer.monitorinfo");
								sendPlayer(p, "forgeSurvival", survival);
							}else {
								sendPlayer(p, "forgeSurvival", survival);
							}
						}else {
							p.sendMessage(api.getPrefix(PrefixType.MAIN) + api.returnStringReady(p, "event.navigator.sendPlayer.offline").replace("%server", "Survival"));
						}
					}
				}else if(e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(lobby)) {
					e.setCancelled(true);
					boolean lock = getData("Lobby", "locked");
					if(lock) {
						p.sendMessage(api.getPrefix(PrefixType.MAIN) + api.returnStringReady(p, "event.navigator.sendPlayer.locked").replace("%server", "Lobby"));
					}else {
						boolean monitor = getData("Lobby", "monitoring");
						boolean online = getData("Lobby", "online");
						if(online) {
							if(monitor) {
								api.sendMSGReady(p, "event.navigator.sendPlayer.monitorinfo");
								sendPlayer(p, "lobby", lobby);
							}else {
								sendPlayer(p, "lobby", lobby);
							}
						}else {
							p.sendMessage(api.getPrefix(PrefixType.MAIN) + api.returnStringReady(p, "event.navigator.sendPlayer.offline").replace("%server", "Lobby"));
						}
					}
				}else if(e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(spawn)) {
					e.setCancelled(true);
					p.closeInventory();
					p.teleport(retLoc(cfg, "plotworld"));
					api.sendMSGReady(p, "event.navigator.local.dailyreward");
				}else if(e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(wt_inventory)) {
					e.setCancelled(true);
					p.closeInventory();
					worldTPer(p);
				}
			}else if(e.getClickedInventory().getName().equalsIgnoreCase(wt_inventory)) {
				YamlConfiguration cfg = YamlConfiguration.loadConfiguration(spawnfile);
				if(e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(wt_freebuild)) {
					if(retLoc(cfg, "world") == null) {
						api.sendMSGReady(p, "event.worldteleporter.notset");
					}else {
						p.teleport(retLoc(cfg, "world"));
						p.sendMessage(api.getPrefix(PrefixType.MAIN) + api.returnStringReady(p, "event.worldteleporter.success").replace("%type", "§aOverworld"));
					}
					e.setCancelled(true);
					p.closeInventory();
				}else if(e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(wt_nether)) {
					if(retLoc(cfg, "nether") == null) {
						api.sendMSGReady(p, "event.worldteleporter.notset");
					}else {
						p.teleport(retLoc(cfg, "nether"));
						p.sendMessage(api.getPrefix(PrefixType.MAIN) + api.returnStringReady(p, "event.worldteleporter.success").replace("%type", "§cNether"));
					}
					e.setCancelled(true);
					p.closeInventory();
				}else if(e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(wt_plotworld)) {
					if(retLoc(cfg, "plotworld") == null) {
						api.sendMSGReady(p, "event.worldteleporter.notset");
					}else {
						p.teleport(retLoc(cfg, "plotworld"));
						p.sendMessage(api.getPrefix(PrefixType.MAIN) + api.returnStringReady(p, "event.worldteleporter.success").replace("%type", "§aPlotworld"));
					}
					e.setCancelled(true);
					p.closeInventory();
				}else if(e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(wt_theend)) {
					if(retLoc(cfg, "theend") == null) {
						api.sendMSGReady(p, "event.worldteleporter.notset");
					}else {
						p.teleport(retLoc(cfg, "theend"));
						p.sendMessage(api.getPrefix(PrefixType.MAIN) + api.returnStringReady(p, "event.worldteleporter.success").replace("%type", "§1The End"));
					}
					p.closeInventory();
					e.setCancelled(true);
				}else {
					e.setCancelled(true);
				}
			}
		}
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(!(sender instanceof Player)) {
			Bukkit.getConsoleSender().sendMessage(Main.consolesend);
		}else {
			Player p = (Player)sender;
			mainnavi(p);
			APIs api = new APIs();
			api.sendMSGReady(p, "cmd.openinv");
		}
		return false;
	}
	
	private static boolean getData(String server, String column) {
		boolean boo = false;
		try {
			PreparedStatement ps = MySQL.getConnection().prepareStatement("SELECT * FROM redicore_serverstats WHERE servername = ?");
			ps.setString(1, server);
			ResultSet rs = ps.executeQuery();
			rs.next();
			boo = rs.getBoolean(column);
			ps.close();
			rs.close();
		}catch (SQLException e) { e.printStackTrace(); }
		return boo;
	}
	
	private static void sendPlayer(Player p, String server, String dserver) {
		APIs api = new APIs();
		ByteArrayOutputStream b = new ByteArrayOutputStream();
		DataOutputStream out = new DataOutputStream(b);
		try {
			p.sendMessage(api.getPrefix(PrefixType.MAIN) + api.returnStringReady(p, "event.navigator.sendPlayer.success").replace("%server", dserver));
			out.writeUTF("Connect");
			out.writeUTF(server);
		} catch (IOException e) {
			api.sendMSGReady(p, "event.navigator.sendPlayer.failed");
		}
		p.sendPluginMessage(plugin, "BungeeCord", b.toByteArray());
	}
	
	private Location retLoc(YamlConfiguration cfg, String type) {
		Location loc = null;
		if(cfg.contains("Spawn." + type + ".WORLD")) {
			loc = new Location(Bukkit.getWorld(cfg.getString("Spawn." + type + ".WORLD")), cfg.getDouble("Spawn." + type + ".X"), cfg.getDouble("Spawn." + type + ".Y"), cfg.getDouble("Spawn." + type + ".Z"), (float)cfg.getDouble("Spawn." + type + ".YAW"), (float)cfg.getDouble("Spawn." + type + ".PITCH"));
		}else {
			loc = null;
		}
		return loc;
	}
}