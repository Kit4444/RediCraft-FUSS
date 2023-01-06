package at.kitsoft.rcfuss.command;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import at.kitsoft.rcfuss.api.APIs;
import at.kitsoft.rcfuss.api.ActionLogger;
import at.kitsoft.rcfuss.api.MoneyAPI;
import at.kitsoft.rcfuss.api.PrefixType;
import at.kitsoft.rcfuss.main.Main;

public class SpawnVillagerCMD implements CommandExecutor, Listener{
	
	public static String villagername = "§aPermissions§cShop";
	public static String shop = "§7Shop § ";
	static File file = new File("plugins/RCUSS/playerdata.yml");

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(!(sender instanceof Player)) {
			Bukkit.getConsoleSender().sendMessage(Main.consolesend);
		}else {
			APIs api = new APIs();
			Player p = (Player)sender;
			if(p.hasPermission("mlps.setShopVillager")) {
				Villager v = (Villager) p.getWorld().spawnEntity(p.getLocation(), EntityType.VILLAGER);
				v.setCustomName(villagername);
				v.setCustomNameVisible(true);
				v.setAdult();
				v.setAgeLock(true);
				v.teleport(p.getLocation());
				api.sendMSGReady(p, "cmd.setvillager");
				ActionLogger.log(api.getServerName(), p, "Player executed the spawnvillager command.");
			}else {
				api.noPerm(p);
				ActionLogger.log(api.getServerName(), p, "Player attempted to executed the spawnvillager command.");
			}
		}
		return false;
	}
	
	@EventHandler
	public void onInteract(PlayerInteractEntityEvent e) {
		Player p = e.getPlayer();
		if(e.getRightClicked().getType() == EntityType.VILLAGER) {
			Villager v = (Villager) e.getRightClicked();
			if(v.getCustomName().equalsIgnoreCase(villagername)) {
				e.setCancelled(true);
				ShopInv(p);
				APIs api = new APIs();
				api.sendMSGReady(p, "event.shopvillager.open");
				ActionLogger.log(api.getServerName(), p, "Player interacted with the villager.");
			}
		}
	}
	
	@EventHandler
	public void onDamage(EntityDamageByEntityEvent e) {
		if(e.getDamager().getType() == EntityType.PLAYER) {
			Player p = (Player) e.getDamager();
			if(e.getEntityType() == EntityType.VILLAGER) {
				Villager v = (Villager) e.getEntity();
				if(v.getCustomName().equals(villagername)) {
					e.setCancelled(true);
					APIs api = new APIs();
					api.sendMSGReady(p, "event.shopvillager.hurt");
					ActionLogger.log(api.getServerName(), p, "Player attempted to hit the villager.");
				}
			}
		}
		
	}
	
	private static void ShopInv(Player p) {
		APIs api = new APIs();
		Inventory inv = Bukkit.createInventory(null, 9*6, villagername);
		YamlConfiguration cfg = YamlConfiguration.loadConfiguration(file);
		long current = System.currentTimeMillis();
		long player = cfg.getLong(p.getUniqueId().toString());
		double money = MoneyAPI.getPocketMoney(p.getUniqueId());
		ItemStack redglass = api.defItem(Material.STAINED_GLASS_PANE, 1, 14, "");
		ItemStack greenglass = api.defItem(Material.STAINED_GLASS_PANE, 1, 5, "");
		ItemStack comingsoon = api.defItem(Material.BARRIER, 1, 0, "§ccoming soon");
		inv.setItem(0, greenglass);
		inv.setItem(1, greenglass);
		inv.setItem(2, greenglass);
		inv.setItem(3, greenglass);
		inv.setItem(4, redglass);
		inv.setItem(5, redglass);
		inv.setItem(6, redglass);
		inv.setItem(7, redglass);
		inv.setItem(8, redglass);
		inv.setItem(9, greenglass);
		inv.setItem(10, greenglass);
		inv.setItem(11, greenglass);
		inv.setItem(12, greenglass);
		inv.setItem(13, greenglass);
		inv.setItem(14, redglass);
		inv.setItem(15, redglass);
		inv.setItem(16, redglass);
		inv.setItem(17, redglass);
		inv.setItem(18, greenglass);
		inv.setItem(19, greenglass);
		if(p.hasPermission("mlps.userfly") || p.hasPermission("mlps.canFly.own")) {
			inv.setItem(20, api.enchItem(Material.BOOK, 1, "§aFly", Enchantment.DURABILITY));
		}else {
			inv.setItem(20, api.l2Item(Material.BOOK, 1, "§aFly", "§7Costs:", "§625.000 §7Coins"));
		}
		
		if(p.hasPermission("mlps.colorchat")) {
			inv.setItem(21, api.enchItem(Material.BOOK, 1, "§8C§4o§5l§ao§cr §7Chat", Enchantment.DURABILITY));
		}else {
			inv.setItem(21, api.l2Item(Material.BOOK, 1, "§8C§4o§5l§ao§cr §7Chat", "§7Costs:", "§615.000 §7Coins"));
		}
		
		if(p.hasPermission("plots.plot.8")) {
			inv.setItem(22, api.enchItem(Material.BOOK, 1, "§aMore Plots", Enchantment.DURABILITY)); //more plots
		}else {
			inv.setItem(22, api.l2Item(Material.BOOK, 1, "§aMore Plots", "§7Costs:", "§640.000 §7Coins")); //more plots
		}
		
		if(p.hasPermission("mlps.subeffects")) {
			inv.setItem(23, api.enchItem(Material.BOOK, 1, "§aEffects", Enchantment.DURABILITY)); //effects
		}else {
			inv.setItem(23, api.l2Item(Material.BOOK, 1, "§aEffects", "§7Costs:", "§625.000 §7Coins")); //effects
		}
		
		if(current <= player) {
			inv.setItem(24, api.enchItem(Material.BOOK, 1, "§aHeal", Enchantment.DURABILITY));
		}else {
			inv.setItem(24, api.l2Item(Material.BOOK, 1, "§aHeal", "§7Costs:", "§6250 §7Coins")); // heal -> lives + food
		}
		inv.setItem(25, redglass);
		inv.setItem(26, redglass);
		inv.setItem(27, greenglass);
		inv.setItem(28, greenglass);
		inv.setItem(29, comingsoon);
		inv.setItem(30, comingsoon);
		inv.setItem(31, comingsoon);
		inv.setItem(32, comingsoon);
		inv.setItem(33, comingsoon);
		inv.setItem(34, redglass);
		inv.setItem(35, redglass);
		inv.setItem(36, greenglass);
		inv.setItem(37, greenglass);
		inv.setItem(38, greenglass);
		inv.setItem(39, greenglass);
		inv.setItem(40, redglass);
		inv.setItem(41, redglass);
		inv.setItem(42, redglass);
		inv.setItem(43, redglass);
		inv.setItem(44, redglass);
		inv.setItem(45, greenglass);
		inv.setItem(46, greenglass);
		inv.setItem(47, greenglass);
		inv.setItem(48, greenglass);
		inv.setItem(49, greenglass);
		inv.setItem(50, redglass);
		inv.setItem(51, redglass);
		inv.setItem(52, redglass);
		inv.setItem(53, api.l2Item(Material.IRON_NUGGET, 1, "§aCoins", "§7Your Balance:", "§a" + money + " §7Coins"));
		p.openInventory(inv);
		p.updateInventory();
	}
	
	public void shop(Player p, String name) {
		double money = MoneyAPI.getPocketMoney(p.getUniqueId());
		APIs api = new APIs();
		Inventory inv = Bukkit.createInventory(null, InventoryType.HOPPER, shop + name);
		inv.setItem(0, api.defItem(Material.STAINED_GLASS_PANE, 1, 5, "§aAccept"));
		inv.setItem(1, api.defItem(Material.STAINED_GLASS_PANE, 1, 7, ""));
		inv.setItem(2, api.l2Item(Material.IRON_NUGGET, 1, "§aCoins", "§7Your Balance:", "§a" + money + " §7Coins"));
		inv.setItem(3, api.defItem(Material.STAINED_GLASS_PANE, 1, 7, ""));
		inv.setItem(4, api.defItem(Material.STAINED_GLASS_PANE, 1, 14, "§cDecline"));
		p.openInventory(inv);
		p.updateInventory();
	}
	
	@EventHandler
	public void onClick(InventoryClickEvent e) throws IOException {
		Player p = (Player) e.getWhoClicked();
		APIs api = new APIs();
		if(e.getView().getTitle().equalsIgnoreCase(villagername)) {
			YamlConfiguration cfg = YamlConfiguration.loadConfiguration(file);
			double money = MoneyAPI.getPocketMoney(p.getUniqueId());
			long current = System.currentTimeMillis();
			long player = cfg.getLong(p.getUniqueId().toString());
			e.setCancelled(true);
			if(e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§aCoins")) {
				e.setCancelled(true);
				p.sendMessage(api.getPrefix(PrefixType.MAIN) + api.returnStringReady(p, "event.villagershop.money").replace("%money", String.valueOf(money)));
			}else if(e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§aFly")) {
				e.setCancelled(true);
				if(p.hasPermission("mlps.userfly")) {
					api.sendMSGReady(p, "event.villagershop.fly");
				}else {
					shop(p, "§aFly");
				}
			}else if(e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§8C§4o§5l§ao§cr §7Chat")) {
				e.setCancelled(true);
				if(p.hasPermission("mlps.colorchat")) {
					api.sendMSGReady(p, "event.villagershop.color");
				}else {
					shop(p, "§aColor Chat");
				}
			}else if(e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§aMore Plots")) {
				e.setCancelled(true);
				if(p.hasPermission("plots.plot.8")) {
					api.sendMSGReady(p, "event.villagershop.plots");
				}else {
					shop(p, "§aMore Plots");
				}
			}else if(e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§aEffects")) {
				e.setCancelled(true);
				if(p.hasPermission("mlps.subeffects")) {
					api.sendMSGReady(p, "event.villagershop.effects");
				}else {
					shop(p, "§aEffects");
				}
			}else if(e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§aHeal")) {
				e.setCancelled(true);
				if(current <= player) {
					long millis = (player - current);
					long secs = millis / 1000;
					long mins = secs / 60;
					long restsecs = secs % 60;
					String tf = mins + ":" + restsecs;
					p.sendMessage(api.getPrefix(PrefixType.MAIN) + api.returnStringReady(p, "event.villagershop.healwait").replace("%time", tf));
				}else {
					p.setFoodLevel(20);
					p.setHealth(20.0);
					if(money >= 250.0) {
						MoneyAPI.setPocketMoney(p.getUniqueId(), (money - 250.0));
						api.sendMSGReady(p, "event.villagershop.healsuccess");
						cfg.set(p.getUniqueId().toString(), (current + 600000));
						cfg.save(file);
					}else {
						api.sendMSGReady(p, "event.villagershop.healnomoney");
					}
				}
			}
		}else if(e.getView().getTitle().equalsIgnoreCase(shop + "§aEffects")) {
			double money = MoneyAPI.getPocketMoney(p.getUniqueId());
			e.setCancelled(true);
			if(e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§aAccept")) {
				e.setCancelled(true);
				doBank(p, money, 25000, "mlps.subeffects");
			}else if(e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§cDecline")) {
				e.setCancelled(true);
				ShopInv(p);
			}else if(e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§aCoins")) {
				e.setCancelled(true);
			}else if(e.getCurrentItem().getType() == Material.STAINED_GLASS_PANE) {
				e.setCancelled(true);
			}
		}else if(e.getView().getTitle().equalsIgnoreCase(shop + "§aFly")) {
			double money = MoneyAPI.getPocketMoney(p.getUniqueId());
			e.setCancelled(true);
			if(e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§aAccept")) {
				e.setCancelled(true);
				doBank(p, money, 25000, "mlps.userfly");
			}else if(e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§cDecline")) {
				e.setCancelled(true);
				ShopInv(p);
			}else if(e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§aCoins")) {
				e.setCancelled(true);
			}else if(e.getCurrentItem().getType() == Material.STAINED_GLASS_PANE) {
				e.setCancelled(true);
			}
		}else if(e.getView().getTitle().equalsIgnoreCase(shop + "§aColor Chat")) {
			double money = MoneyAPI.getPocketMoney(p.getUniqueId());
			e.setCancelled(true);
			if(e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§aAccept")) {
				e.setCancelled(true);
				doBank(p, money, 15000, "mlps.colorchat");
			}else if(e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§cDecline")) {
				e.setCancelled(true);
				ShopInv(p);
			}else if(e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§aCoins")) {
				e.setCancelled(true);
			}else if(e.getCurrentItem().getType() == Material.STAINED_GLASS_PANE) {
				e.setCancelled(true);
			}
		}else if(e.getView().getTitle().equalsIgnoreCase(shop + "§aMore Plots")) {
			double money = MoneyAPI.getPocketMoney(p.getUniqueId());
			e.setCancelled(true);
			if(e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§aAccept")) {
				e.setCancelled(true);
				doBank(p, money, 40000, "plots.plot.8");
			}else if(e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§cDecline")) {
				e.setCancelled(true);
				ShopInv(p);
			}else if(e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§aCoins")) {
				e.setCancelled(true);
			}else if(e.getCurrentItem().getType() == Material.STAINED_GLASS_PANE) {
				e.setCancelled(true);
			}
		}
	}
	
	private void doBank(Player p, double money, int subtractor, String permission) {
		APIs api = new APIs();
		if(money >= subtractor) {
			MoneyAPI.setPocketMoney(p.getUniqueId(), (money - subtractor));
			Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "pex user " + p.getName() + " add " + permission);
			if(permission.equalsIgnoreCase("mlps.subeffects")) {
				api.sendMSGReady(p, "event.villagershop.purchase.effects");
			}else if(permission.equalsIgnoreCase("mlps.userfly")) {
				api.sendMSGReady(p, "event.villagershop.purchase.userfly");
			}else if(permission.equalsIgnoreCase("mlps.colorchat")) {
				api.sendMSGReady(p, "event.villagershop.purchase.colorchat");
			}else if(permission.equalsIgnoreCase("plots.plot.8")) {
				api.sendMSGReady(p, "event.villagershop.purchase.plots");
			}else if(permission.equalsIgnoreCase("")) {
				
			}
		}else {
			p.sendMessage(api.getPrefix(PrefixType.MAIN) + api.returnStringReady(p, "event.villagershop.notenoughmoney"));
		}
	}

}
