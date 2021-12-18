package com.asecave;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;

public class PlaytimeCounter extends JavaPlugin implements Listener {
	
	public static List<Playtime> playtimes;
	
	public static Logger logger = Bukkit.getLogger();
	
	public static PlaytimeCounter instance;
	
	private Objective objective;
	
	@Override
	public void onEnable() {
		
		instance = this;
		
		getServer().getPluginManager().registerEvents(this, this);
		
		getCommand("playtime").setExecutor(new CommandPlaytime());
		
		saveDefaultConfig();
		
		playtimes = new ArrayList<Playtime>();
		
		Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
			
			@Override
			public void run() {
				// Find players that are on the server but not in the playtime tracker
				FileConfiguration config = getConfig();
				List<String> times = config.getStringList("Playtimes");
				for (Player p : getServer().getOnlinePlayers()) {
					boolean found = false;
					for (Playtime pt : playtimes) {
						if (p.getUniqueId().toString().equals(pt.getUUID())) {
							found = true;
							break;
						}
					}
					if (!found) {
						boolean hasPlaytime = false;
						for (String s : times) {
							String[] split = s.split(";");
							String uuid = split[0];
							long time = Long.parseLong(split[1]);
							if (uuid.equals(p.getUniqueId().toString())) {
								playtimes.add(new Playtime(uuid, time, p.getDisplayName()));
								hasPlaytime = true;
								break;
							}
						}
						if (!hasPlaytime) {
							playtimes.add(new Playtime(p.getUniqueId().toString(), 0, p.getDisplayName()));
						}
						logger.info("Picked up playtime tracking for " + p.getDisplayName());
					}
				}
				
				// Autosave
				for (Playtime pt : playtimes) {
					for (int i = 0; i < times.size(); i++) {
						if (times.get(i).startsWith(pt.getUUID())) {
							times.remove(i);
						}
					}
					times.add(pt.getUUID() + ";" + pt.getTotalTime());
					objective.getScore(pt.getDisplayName()).setScore((int) (pt.getTotalTime() / 3600000));
				}
				config.set("Playtimes", times);
				saveConfig();
				
				// Remove offline players from playtime tracker
				for (int i = 0; i < playtimes.size(); i++) {
					Player p = null;
					for (Player sp : getServer().getOnlinePlayers()) {
						if (sp.getUniqueId().toString().equals(playtimes.get(i).getUUID())) {
							p = sp;
							break;
						}
					}
					if (p == null) {
						logger.info("Released playtime tracking for " + playtimes.get(i).getDisplayName());
						playtimes.remove(i);
					}
				}
			}
		}, 0, 100);
		
		objective = getServer().getScoreboardManager().getMainScoreboard().getObjective("playtime");
		if (objective == null) {
			objective = getServer().getScoreboardManager().getMainScoreboard().registerNewObjective("playtime", "dummy", "Playtime");
		}
		objective.setDisplaySlot(DisplaySlot.PLAYER_LIST);
	}
}
