package at.kitsoft.rcfuss.event;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.bukkit.craftbukkit.v1_12_R1.advancement.CraftAdvancement;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerAdvancementDoneEvent;

import at.kitsoft.rcfuss.api.APIs;
import at.kitsoft.rcfuss.main.Main;

public class AchievementSender implements Listener{

	private static Main plugin;
	public AchievementSender(Main m) {
		plugin = m;
	}
	
	@EventHandler
	public void onPlayerAchive(PlayerAdvancementDoneEvent e) {
		Player p = e.getPlayer();
		APIs api = new APIs();
		CraftAdvancement adv = (CraftAdvancement) e.getAdvancement();
		if(adv != null) {
			String advancement = adv.getHandle().c().a().getText();
			if(!advancement.startsWith("recipes") || !advancement.startsWith("tracks")) {
				String text = "⏫ **" + p.getName() + "** has completed the advancement **" + advancement + "**";
				ByteArrayOutputStream b = new ByteArrayOutputStream();
				DataOutputStream out = new DataOutputStream(b);
				try {
					out.writeUTF(translateServer(api.getServerName(), text));
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				p.sendPluginMessage(plugin, "redicraft:advbc", b.toByteArray());
			}
		}
	}
	
	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent e) {
		Player p = e.getEntity();
		APIs api = new APIs();
		String text = "☠️ **" + p.getName() + "** died!";
		ByteArrayOutputStream b = new ByteArrayOutputStream();
		DataOutputStream out = new DataOutputStream(b);
		try {
			out.writeUTF(translateServer(api.getServerName(), text));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		p.sendPluginMessage(plugin, "redicraft:advbc", b.toByteArray());
	}
	
	long globalchat = 757510387934560296L;
    long lobbymain = 757510355395281006L;
    long lobbygame = 840548521152872458l;
    long creative = 757510041090785312L;
    long creativeforge = 1028388350152953856l;
    long survival = 757510292425932851L;
    long survivalforge = 1028388399947718727l;
    long skyblock = 757510325204418612L;
    long farmserver = 757510458814234634L;
    
    String translateServer(String server, String text) {
		String srv = "";
		switch(server) {
		case "Gameslobby": srv = lobbygame + "-;-" + text; break;
		case "Survival": srv = survival + "-;-" + text; break;
		case "Lobby": srv = lobbymain + "-;-" + text; break;
		case "Creative": srv = creative + "-;-" + text; break;
		case "SkyBlock": srv = skyblock + "-;-" + text; break;
		case "Farmserver": srv = farmserver + "-;-" + text; break;
		case "Forge Survival": srv = survivalforge + "-;-" + text; break;
		case "Forge Creative": srv = creativeforge + "-;-" + text; break;
		default: srv = server; break;
		}
		return srv;
	}
}