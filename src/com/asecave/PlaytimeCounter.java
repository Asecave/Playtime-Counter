package com.asecave;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class PlaytimeCounter extends JavaPlugin implements Listener {
	
	public static List<Playtime> playtimes;
	
	@Override
	public void onEnable() {
		
		getServer().getPluginManager().registerEvents(this, this);
		
		getCommand("playtime").setExecutor(new CommandPlaytime());
		
		saveDefaultConfig();
		
		playtimes = new ArrayList<Playtime>();
	}
	
	@EventHandler
	private void playerJoin(PlayerJoinEvent e) {
		FileConfiguration config = getConfig();
		List<String> times = config.getStringList("Playtimes");
		boolean found = false;
		for (String s : times) {
			String[] split = s.split(";");
			String uuid = split[0];
			long time = Long.parseLong(split[1]);
			if (uuid.equals(e.getPlayer().getUniqueId().toString())) {
				playtimes.add(new Playtime(uuid, time));
				found = true;
				break;
			}
		}
		if (!found) {
			playtimes.add(new Playtime(e.getPlayer().getUniqueId().toString(), 0));
		}
	}
	
	@EventHandler
	private void playerLeave(PlayerQuitEvent e) {
		savePlaytime(e.getPlayer().getUniqueId().toString());
	}
	
	@Override
	public void onDisable() {
		for (int i = 0; i < playtimes.size(); i++) {
			savePlaytime(playtimes.get(i).getUUID());
		}
	}
	
	private void savePlaytime(String uuid) {
		FileConfiguration config = getConfig();
		List<String> list = config.getStringList("Playtimes");
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).startsWith(uuid)) {
				list.remove(i);
			}
		}
		for (int i = 0; i < playtimes.size(); i++) {
			Playtime pt = playtimes.get(i);
			if (pt.getUUID().equals(uuid)) {
				list.add(pt.getUUID() + ";" + pt.getTotalTime());
				config.set("Playtimes", list);
				playtimes.remove(i);
				break;
			}
		}
		saveConfig();
	}
}
