package com.asecave;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandPlaytime implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (args.length == 0) {
			return false;
		}
		Player p = Bukkit.getPlayer(args[0]);
		String uuid = p.getUniqueId().toString();
		for (int i = 0; i < PlaytimeCounter.playtimes.size(); i++) {
			Playtime pt = PlaytimeCounter.playtimes.get(i);
			if (uuid.equals(pt.getUUID())) {
				long seconds = pt.getTotalTime() / 1000;
				long hours = seconds / 3600;
				long minutes = (seconds / 60) % 60;
				sender.sendMessage("Playtime of " + p.getName() + ": " + hours + ":" + (minutes < 10 ? "0" : "") + minutes);
				break;
			}
		}
		return true;
	}

}
