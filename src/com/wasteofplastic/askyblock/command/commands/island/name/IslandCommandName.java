package com.wasteofplastic.askyblock.command.commands.island.name;

import org.bukkit.ChatColor;

import com.wasteofplastic.askyblock.ASkyBlock;
import com.wasteofplastic.askyblock.Settings;
import com.wasteofplastic.askyblock.command.CommandType;
import com.wasteofplastic.askyblock.command.commands.IslandCommandBase;
import com.wasteofplastic.askyblock.util.Util;

public class IslandCommandName extends IslandCommandBase {

	public IslandCommandName() {
		this.addSubCommand("name");
		this.setSyntaxe("/is name <nom>");
		this.setDescription("Changer le nom de votre île");
		this.setPermission(Settings.PERMPREFIX + "island.name");
		this.setArgsLength(2);
		this.setIsland(true);
	}

	@Override
	public CommandType postPerform(ASkyBlock plugin) {

		String name = args[1];
		for (int i = 2; i < args.length; i++) {
			name = name + " " + args[i];
		}
		if (name.length() < Settings.minNameLength) {
			Util.sendMessage(player, "§f» " +ChatColor.RED + (plugin.myLocale(player.getUniqueId()).errorTooShort)
					.replace("[length]", String.valueOf(Settings.minNameLength)));
			return CommandType.SUCCESS;
		}
		if (name.length() > Settings.maxNameLength) {
			Util.sendMessage(player, "§f» " +ChatColor.RED + (plugin.myLocale(player.getUniqueId()).errorTooLong)
					.replace("[length]", String.valueOf(Settings.maxNameLength)));
			return CommandType.SUCCESS;
		}
		plugin.getGrid().setIslandName(playerUUID, ChatColor.translateAlternateColorCodes('&', name));
		Util.sendMessage(player, "§f» " +ChatColor.GREEN + plugin.myLocale(player.getUniqueId()).generalSuccess);
		return CommandType.SUCCESS;

	}

}
