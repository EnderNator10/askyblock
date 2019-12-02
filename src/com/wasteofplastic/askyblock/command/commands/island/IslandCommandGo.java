package com.wasteofplastic.askyblock.command.commands.island;

import org.bukkit.ChatColor;

import com.wasteofplastic.askyblock.ASkyBlock;
import com.wasteofplastic.askyblock.Settings;
import com.wasteofplastic.askyblock.command.CommandType;
import com.wasteofplastic.askyblock.command.commands.IslandCommandBase;
import com.wasteofplastic.askyblock.util.Util;

public class IslandCommandGo extends IslandCommandBase {

	public IslandCommandGo() {
		this.addSubCommand("go");
		this.setSyntaxe("/is go");
		this.setDescription("Se téléporter à son île");
		this.setPermission(Settings.PERMPREFIX + "island.go");
	}

	@Override
	public CommandType postPerform(ASkyBlock plugin) {

		if (!plugin.getPlayers().hasIsland(playerUUID) && !plugin.getPlayers().inTeam(playerUUID)) {
			Util.sendMessage(player, "§f» " +ChatColor.RED + plugin.myLocale(playerUUID).errorNoIsland);
			return CommandType.DEFAULT;
		}
		plugin.getGrid().homeTeleport(player);
		if (Settings.islandRemoveMobs) 
			plugin.getGrid().removeMobs(player.getLocation());

		return CommandType.SUCCESS;
	}

}
