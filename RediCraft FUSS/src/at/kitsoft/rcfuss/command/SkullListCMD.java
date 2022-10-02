package at.kitsoft.rcfuss.command;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

import at.kitsoft.rcfuss.api.APIs;
import at.kitsoft.rcfuss.api.ActionLogger;
import at.kitsoft.rcfuss.api.PrefixType;

public class SkullListCMD implements CommandExecutor, Listener{
	
	//Message: You've got the head of %head - Du hast den Kopf von %head bekommen

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(!(sender instanceof Player)) {
			
		}else {
			Player p = (Player)sender;
			openInvSelector(p);
			APIs api = new APIs();
			ActionLogger.log(api.getServerName(), p, "Player executed the headlist command.");
		}
		return true;
	}
	
	@EventHandler
	public void onInteract(InventoryClickEvent e) {
		Player p = (Player) e.getWhoClicked();
		if(e.getView().getTitle().equalsIgnoreCase("§aHead List")) {
			APIs api = new APIs();
			e.setCancelled(true);
			if(e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§aMobs")) {
				p.closeInventory();
				openInvMobs(p);
				p.sendMessage(api.getPrefix(PrefixType.MAIN) + api.returnStringReady(p, "event.head.inv").replace("%head", "§aMobs"));
			}else if(e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§aBlocks")) {
				p.closeInventory();
				openInvBlocks(p);
				p.sendMessage(api.getPrefix(PrefixType.MAIN) + api.returnStringReady(p, "event.head.inv").replace("%head", "§aBlocks"));
			}else if(e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§aBonus")) {
				p.closeInventory();
				openInvBonus(p);
				p.sendMessage(api.getPrefix(PrefixType.MAIN) + api.returnStringReady(p, "event.head.inv").replace("%head", "§aBonus"));
			}
		}else if(e.getView().getTitle().equalsIgnoreCase("§6Bonus")) {
			e.setCancelled(true);
			APIs api = new APIs();
			if(e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§aQuestion")) {
				p.getInventory().addItem(api.skullItem(1, "§aQuestion", "MHF_Question"));
				p.sendMessage(api.getPrefix(PrefixType.MAIN) + api.returnStringReady(p, "event.head.get").replace("%head", "Question"));
			}else if(e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§aExclamation")) {
				p.getInventory().addItem(api.skullItem(1, "§aExclamation", "MHF_Exclamation"));
				p.sendMessage(api.getPrefix(PrefixType.MAIN) + api.returnStringReady(p, "event.head.get").replace("%head", "Exclamation"));
			}else if(e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§aArrow Up")) {
				p.getInventory().addItem(api.skullItem(1, "§aArrow Up", "MHF_ArrowUp"));
				p.sendMessage(api.getPrefix(PrefixType.MAIN) + api.returnStringReady(p, "event.head.get").replace("%head", "Arrow Up"));
			}else if(e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§aArrow Down")) {
				p.getInventory().addItem(api.skullItem(1, "§aArrow Down", "MHF_ArrowDown"));
				p.sendMessage(api.getPrefix(PrefixType.MAIN) + api.returnStringReady(p, "event.head.get").replace("%head", "Arrow Down"));
			}else if(e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§aArrow Left")) {
				p.getInventory().addItem(api.skullItem(1, "§aArrow Left", "MHF_ArrowLeft"));
				p.sendMessage(api.getPrefix(PrefixType.MAIN) + api.returnStringReady(p, "event.head.get").replace("%head", "Arrow Left"));
			}else if(e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§aArrow Right")) {
				p.getInventory().addItem(api.skullItem(1, "§aArrow Right", "MHF_ArrowRight"));
				p.sendMessage(api.getPrefix(PrefixType.MAIN) + api.returnStringReady(p, "event.head.get").replace("%head", "Arrow Right"));
			}else if(e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§cback")) {
				p.closeInventory();
				openInvSelector(p);
			}else if(e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§cclose")) {
				p.closeInventory();
			}
		}else if(e.getView().getTitle().equalsIgnoreCase("§7Mobs")) {
			e.setCancelled(true);
			APIs api = new APIs();
			switch(e.getCurrentItem().getItemMeta().getDisplayName()) {
			case "§aAlex": p.sendMessage(api.getPrefix(PrefixType.MAIN) + api.returnStringReady(p, "event.head.get").replace("%head", "Alex")); p.getInventory().addItem(api.skullItem(1, "§aAlex", "MHF_Alex")); break;
			case "§aHerobrine": p.sendMessage(api.getPrefix(PrefixType.MAIN) + api.returnStringReady(p, "event.head.get").replace("%head", "Herobrine")); p.getInventory().addItem(api.skullItem(1, "§aHerobrine", "MHF_Herobrine")); break;
			case "§aSpider": p.sendMessage(api.getPrefix(PrefixType.MAIN) + api.returnStringReady(p, "event.head.get").replace("%head", "Spider")); p.getInventory().addItem(api.skullItem(1, "§aSpider", "MHF_Spider")); break;
			case "§aBlaze": p.sendMessage(api.getPrefix(PrefixType.MAIN) + api.returnStringReady(p, "event.head.get").replace("%head", "Blaze")); p.getInventory().addItem(api.skullItem(1, "§aBlaze", "MHF_Blaze")); break;
			case "§aLava Slime": p.sendMessage(api.getPrefix(PrefixType.MAIN) + api.returnStringReady(p, "event.head.get").replace("%head", "Lava Slime")); p.getInventory().addItem(api.skullItem(1, "§aLava Slime", "MHF_LavaSlime")); break;
			case "§aSquid": p.sendMessage(api.getPrefix(PrefixType.MAIN) + api.returnStringReady(p, "event.head.get").replace("%head", "Squid")); p.getInventory().addItem(api.skullItem(1, "§aSquid", "MHF_Squid")); break;
			case "§aCave Spider": p.sendMessage(api.getPrefix(PrefixType.MAIN) + api.returnStringReady(p, "event.head.get").replace("%head", "Cave Spider")); p.getInventory().addItem(api.skullItem(1, "§aCave Spider", "MHF_CaveSpider")); break;
			case "§aMushroom Cow": p.sendMessage(api.getPrefix(PrefixType.MAIN) + api.returnStringReady(p, "event.head.get").replace("%head", "Mushroom Cow")); p.getInventory().addItem(api.skullItem(1, "§aMushroom Cow", "MHF_MushroomCow")); break;
			case "§aSteve": p.sendMessage(api.getPrefix(PrefixType.MAIN) + api.returnStringReady(p, "event.head.get").replace("%head", "Steve")); p.getInventory().addItem(api.skullItem(1, "§aSteve", "MHF_Steve")); break;
			case "§aChicken": p.sendMessage(api.getPrefix(PrefixType.MAIN) + api.returnStringReady(p, "event.head.get").replace("%head", "Chicken")); p.getInventory().addItem(api.skullItem(1, "§aChicken", "MHF_Chicken")); break;
			case "§aOcelot": p.sendMessage(api.getPrefix(PrefixType.MAIN) + api.returnStringReady(p, "event.head.get").replace("%head", "Ocelot")); p.getInventory().addItem(api.skullItem(1, "§aOcelot", "MHF_Ocelot")); break;
			case "§aVillager": p.sendMessage(api.getPrefix(PrefixType.MAIN) + api.returnStringReady(p, "event.head.get").replace("%head", "Villager")); p.getInventory().addItem(api.skullItem(1, "§aVillager", "MHF_Villager")); break;
			case "§aCow": p.sendMessage(api.getPrefix(PrefixType.MAIN) + api.returnStringReady(p, "event.head.get").replace("%head", "Cow")); p.getInventory().addItem(api.skullItem(1, "§aCow", "MHF_Cow")); break;
			case "§aPig": p.sendMessage(api.getPrefix(PrefixType.MAIN) + api.returnStringReady(p, "event.head.get").replace("%head", "Pig")); p.getInventory().addItem(api.skullItem(1, "§aPig", "MHF_Pig")); break;
			case "§aCreeper": p.sendMessage(api.getPrefix(PrefixType.MAIN) + api.returnStringReady(p, "event.head.get").replace("%head", "Creeper")); p.getInventory().addItem(api.skullItem(1, "§aCreeper", "MHF_Creeper")); break;
			case "§aZombie Pigman": p.sendMessage(api.getPrefix(PrefixType.MAIN) + api.returnStringReady(p, "event.head.get").replace("%head", "Zombie Pigman")); p.getInventory().addItem(api.skullItem(1, "§aZombie Pigman", "MHF_PigZombie")); break;
			case "§aWither Skeleton": p.sendMessage(api.getPrefix(PrefixType.MAIN) + api.returnStringReady(p, "event.head.get").replace("%head", "Wither Skeleton")); p.getInventory().addItem(api.skullItem(1, "§aWither Skeleton", "MHF_WSkeleton")); break;
			case "§aEnderman": p.sendMessage(api.getPrefix(PrefixType.MAIN) + api.returnStringReady(p, "event.head.get").replace("%head", "Enderman")); p.getInventory().addItem(api.skullItem(1, "§aEnderman", "MHF_Enderman")); break;
			case "§aSheep": p.sendMessage(api.getPrefix(PrefixType.MAIN) + api.returnStringReady(p, "event.head.get").replace("%head", "Sheep")); p.getInventory().addItem(api.skullItem(1, "§aSheep", "MHF_Sheep")); break;
			case "§aZombie": p.sendMessage(api.getPrefix(PrefixType.MAIN) + api.returnStringReady(p, "event.head.get").replace("%head", "Zombie")); p.getInventory().addItem(api.skullItem(1, "§aZombie", "MHF_Zombie")); break;
			case "§aSkeleton": p.sendMessage(api.getPrefix(PrefixType.MAIN) + api.returnStringReady(p, "event.head.get").replace("%head", "Skeleton")); p.getInventory().addItem(api.skullItem(1, "§aSkeleton", "MHF_Skeleton")); break;
			case "§aGhast": p.sendMessage(api.getPrefix(PrefixType.MAIN) + api.returnStringReady(p, "event.head.get").replace("%head", "Ghast")); p.getInventory().addItem(api.skullItem(1, "§aGhast", "MHF_Ghast")); break;
			case "§aSlime": p.sendMessage(api.getPrefix(PrefixType.MAIN) + api.returnStringReady(p, "event.head.get").replace("%head", "Slime")); p.getInventory().addItem(api.skullItem(1, "§aSlime", "MHF_Slime")); break;
			case "§aIron Golem": p.sendMessage(api.getPrefix(PrefixType.MAIN) + api.returnStringReady(p, "event.head.get").replace("%head", "Iron Golem")); p.getInventory().addItem(api.skullItem(1, "§aIron Golem", "MHF_Golem")); break;
			case "§cback": p.closeInventory(); openInvSelector(p); break;
			case "§cclose": p.closeInventory(); break;
			}
		}else if(e.getView().getTitle().equalsIgnoreCase("§7Blocks")) {
			e.setCancelled(true);
			APIs api = new APIs();
			switch(e.getCurrentItem().getItemMeta().getDisplayName()) {
			case "§aCactus": p.sendMessage(api.getPrefix(PrefixType.MAIN) + api.returnStringReady(p, "event.head.get").replace("%head", "Cactus")); p.getInventory().addItem(api.skullItem(1, "§aCactus", "MHF_Cactus")); break;
			case "§aMelon": p.sendMessage(api.getPrefix(PrefixType.MAIN) + api.returnStringReady(p, "event.head.get").replace("%head", "Melon")); p.getInventory().addItem(api.skullItem(1, "§aMelon", "MHF_Melon")); break;
			case "§aPumpkin": p.sendMessage(api.getPrefix(PrefixType.MAIN) + api.returnStringReady(p, "event.head.get").replace("%head", "Pumpkin")); p.getInventory().addItem(api.skullItem(1, "§aPumpkin", "MHF_Pumpkin")); break;
			case "§aCake": p.sendMessage(api.getPrefix(PrefixType.MAIN) + api.returnStringReady(p, "event.head.get").replace("%head", "Cake")); p.getInventory().addItem(api.skullItem(1, "§aCake", "MHF_Cake")); break;
			case "§aChest": p.sendMessage(api.getPrefix(PrefixType.MAIN) + api.returnStringReady(p, "event.head.get").replace("%head", "Chest")); p.getInventory().addItem(api.skullItem(1, "§aChest", "MHF_Chest")); break;
			case "§aOak Log": p.sendMessage(api.getPrefix(PrefixType.MAIN) + api.returnStringReady(p, "event.head.get").replace("%head", "Oak Log")); p.getInventory().addItem(api.skullItem(1, "§aOak Log", "MHF_OakLog")); break;
			case "§aTNT 1": p.sendMessage(api.getPrefix(PrefixType.MAIN) + api.returnStringReady(p, "event.head.get").replace("%head", "TNT 1")); p.getInventory().addItem(api.skullItem(1, "§aTNT 1", "MHF_TNT")); break;
			case "§aTNT 2": p.sendMessage(api.getPrefix(PrefixType.MAIN) + api.returnStringReady(p, "event.head.get").replace("%head", "TNT 2")); p.getInventory().addItem(api.skullItem(1, "§aTNT 2", "MHF_TNT2")); break;
			case "§aPresent 1": p.sendMessage(api.getPrefix(PrefixType.MAIN) + api.returnStringReady(p, "event.head.get").replace("%head", "Present 1")); p.getInventory().addItem(api.skullItem(1, "§aPresent 1", "MHF_Present1")); break;
			case "§aPresent 2": p.sendMessage(api.getPrefix(PrefixType.MAIN) + api.returnStringReady(p, "event.head.get").replace("%head", "Present 2")); p.getInventory().addItem(api.skullItem(1, "§aPresent 2", "MHF_Present2")); break;
			case "§aBrown Coconut": p.sendMessage(api.getPrefix(PrefixType.MAIN) + api.returnStringReady(p, "event.head.get").replace("%head", "Brown Coconut")); p.getInventory().addItem(api.skullItem(1, "§aBrown Coconut", "MHF_CoconutB")); break;
			case "§aGreen Coconut": p.sendMessage(api.getPrefix(PrefixType.MAIN) + api.returnStringReady(p, "event.head.get").replace("%head", "Green Coconut")); p.getInventory().addItem(api.skullItem(1, "§aGreen Coconut", "MHF_CoconutG")); break;
			case "§cclose": p.closeInventory(); break;
			case "§cback": p.closeInventory(); openInvSelector(p); break;
			}
			if(e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§cback")) {
				p.closeInventory();
				openInvSelector(p);
			}else if(e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§cclose")) {
				p.closeInventory();
			}
		}
	}
	
	private static void openInvMobs(Player p) {
		Inventory inv = Bukkit.createInventory(null, 9*3, "§7Mobs");
		APIs api = new APIs();
		inv.addItem(api.skullItem(1, "§aAlex", "MHF_Alex"));
		inv.addItem(api.skullItem(1, "§aHerobrine", "MHF_Herobrine"));
		inv.addItem(api.skullItem(1, "§aSpider", "MHF_Spider"));
		inv.addItem(api.skullItem(1, "§aBlaze", "MHF_Blaze"));
		inv.addItem(api.skullItem(1, "§aLava Slime", "MHF_LavaSlime"));
		inv.addItem(api.skullItem(1, "§aSquid", "MHF_Squid"));
		inv.addItem(api.skullItem(1, "§aCave Spider", "MHF_CaveSpider"));
		inv.addItem(api.skullItem(1, "§aMushroom Cow", "MHF_MushroomCow"));
		inv.addItem(api.skullItem(1, "§aSteve", "MHF_Steve"));
		inv.addItem(api.skullItem(1, "§aChicken", "MHF_Chicken"));
		inv.addItem(api.skullItem(1, "§aOcelot", "MHF_Ocelot"));
		inv.addItem(api.skullItem(1, "§aVillager", "MHF_Villager"));
		inv.addItem(api.skullItem(1, "§aCow", "MHF_Cow"));
		inv.addItem(api.skullItem(1, "§aPig", "MHF_Pig"));
		inv.addItem(api.skullItem(1, "§aCreeper", "MHF_Creeper"));
		inv.addItem(api.skullItem(1, "§aZombie Pigman", "MHF_PigZombie"));
		inv.addItem(api.skullItem(1, "§aWither Skeleton", "MHF_WSkeleton"));
		inv.addItem(api.skullItem(1, "§aEnderman", "MHF_Enderman"));
		inv.addItem(api.skullItem(1, "§aSheep", "MHF_Sheep"));
		inv.addItem(api.skullItem(1, "§aZombie", "MHF_Zombie"));
		inv.addItem(api.skullItem(1, "§aSkeleton", "MHF_Skeleton"));
		inv.addItem(api.skullItem(1, "§aGhast", "MHF_Ghast"));
		inv.addItem(api.skullItem(1, "§aSlime", "MHF_Slime"));
		inv.addItem(api.skullItem(1, "§aIron Golem", "MHF_Golem"));
		inv.setItem(25, api.defItem(Material.BARRIER, 1, 0, "§cback"));
		inv.setItem(26, api.defItem(Material.BARRIER, 1, 0, "§cclose"));
		p.openInventory(inv);
		p.updateInventory();
	}
	
	private static void openInvBlocks(Player p) {
		Inventory inv = Bukkit.createInventory(null, 9*2, "§7Blocks");
		APIs api = new APIs();
		inv.addItem(api.skullItem(1, "§aCactus", "MHF_Cactus"));
		inv.addItem(api.skullItem(1, "§aMelon", "MHF_Melon"));
		inv.addItem(api.skullItem(1, "§aPumpkin", "MHF_Pumpkin"));
		inv.addItem(api.skullItem(1, "§aCake", "MHF_Cake"));
		inv.addItem(api.skullItem(1, "§aChest", "MHF_Chest"));
		inv.addItem(api.skullItem(1, "§aOak Log", "MHF_OakLog"));
		inv.addItem(api.skullItem(1, "§aTNT 1", "MHF_TNT"));
		inv.addItem(api.skullItem(1, "§aPresent 1", "MHF_Present1"));
		inv.addItem(api.skullItem(1, "§aPresent 2", "MHF_Present2"));
		inv.addItem(api.skullItem(1, "§aTNT 2", "MHF_TNT2"));
		inv.addItem(api.skullItem(1, "§aBrown Coconut", "MHF_CoconutB"));
		inv.addItem(api.skullItem(1, "§aGreen Coconut", "MHF_CoconutG"));
		inv.setItem(16, api.defItem(Material.BARRIER, 1, 0, "§cback"));
		inv.setItem(17, api.defItem(Material.BARRIER, 1, 0, "§cclose"));
		p.openInventory(inv);
		p.updateInventory();
	}
	
	private static void openInvBonus(Player p) {
		Inventory inv = Bukkit.createInventory(null, 9*2, "§6Bonus");
		APIs api = new APIs();
		inv.setItem(0, api.skullItem(1, "§aQuestion", "MHF_Question"));
		inv.setItem(1, api.skullItem(1, "§aExclamation", "MHF_Exclamation"));
		inv.setItem(3, api.skullItem(1, "§aArrow Up", "MHF_ArrowUp"));
		inv.setItem(5, api.skullItem(1, "§aArrow Down", "MHF_ArrowDown"));
		inv.setItem(7, api.skullItem(1, "§aArrow Left", "MHF_ArrowLeft"));
		inv.setItem(8, api.skullItem(1, "§aArrow Right", "MHF_ArrowRight"));
		inv.setItem(12, api.defItem(Material.BARRIER, 1, 0, "§cback"));
		inv.setItem(14, api.defItem(Material.BARRIER, 1, 0, "§cclose"));
		p.openInventory(inv);
		p.updateInventory();
	}
	
	private static void openInvSelector(Player p) {
		Inventory inv = Bukkit.createInventory(null, 9*1, "§aHead List");
		APIs api = new APIs();
		inv.setItem(2, api.skullItem(1, "§aMobs", "MHF_Pig"));
		inv.setItem(4, api.skullItem(1, "§aBlocks", "MHF_OakLog"));
		inv.setItem(6, api.skullItem(1, "§aBonus", "MHF_Question"));
		p.openInventory(inv);
		p.updateInventory();
	}

}
