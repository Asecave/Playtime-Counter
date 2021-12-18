package com.asecave;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CommandPlaytime implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (args.length == 0) {
			return false;
		}
		@SuppressWarnings("deprecation")
		OfflinePlayer p = Bukkit.getOfflinePlayer(args[0]);
		if (p.getLastPlayed() == 0) {
			sender.sendMessage(ChatColor.RED + args[0] + " never joined this server.");
			return true;
		}
		String uuid = p.getUniqueId().toString();
		String status = p.isOnline() ? ChatColor.GREEN + "online" : ChatColor.RED + "offline";
		long seconds = 0;
		if (p.isOnline()) {
			for (int i = 0; i < PlaytimeCounter.playtimes.size(); i++) {
				Playtime pt = PlaytimeCounter.playtimes.get(i);
				if (uuid.equals(pt.getUUID())) {
					seconds = pt.getTotalTime() / 1000;
					break;
				}
			}
		} else {
			List<String> times = PlaytimeCounter.instance.getConfig().getStringList("Playtimes");
			for (String s : times) {
				String[] split = s.split(";");
				String suuid = split[0];
				if (suuid.equals(uuid)) {
					seconds = Long.parseLong(split[1]) / 1000;
				}
			}
		}
		long hours = seconds / 3600;
		long minutes = (seconds / 60) % 60;
		sender.sendMessage(ChatColor.DARK_AQUA + "Playtime of " + p.getName() + " (" + status + ChatColor.DARK_AQUA + "): "
				+ ChatColor.AQUA + hours + ":" + (minutes < 10 ? "0" : "") + minutes);

		return true;
	}

}
