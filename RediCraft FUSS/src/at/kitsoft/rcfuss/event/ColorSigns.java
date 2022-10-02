package at.kitsoft.rcfuss.event;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;

import net.md_5.bungee.api.ChatColor;

public class ColorSigns implements Listener{
	
	@EventHandler
	public void onChange(SignChangeEvent e) {
		Player p = e.getPlayer();
		if(p.hasPermission("mlps.ColorSigns")) {
			String[] lines = e.getLines();
			for(int i = 0; i <= 3; i++) {
				e.setLine(i, ChatColor.translateAlternateColorCodes('&', lines[i]));
			}
		}
	}
}