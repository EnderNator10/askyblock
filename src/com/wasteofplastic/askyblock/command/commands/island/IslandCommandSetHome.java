package com.wasteofplastic.askyblock.command.commands.island;

import org.bukkit.ChatColor;

import com.wasteofplastic.askyblock.ASkyBlock;
import com.wasteofplastic.askyblock.Settings;
import com.wasteofplastic.askyblock.command.CommandType;
import com.wasteofplastic.askyblock.command.commands.IslandCommandBase;
import com.wasteofplastic.askyblock.util.Util;

public class IslandCommandSetHome extends IslandCommandBase {

	public IslandCommandSetHome() {
		this.addSubCommand("sethome");
		this.setSyntaxe("/is sethome");
		this.setDescription("Changer l'home de votre île");
		this.setPermission(Settings.PERMPREFIX + "island.sethome");
	}

	@Override
	public CommandType postPerform(ASkyBlock plugin) {

		if (plugin.getGrid().getIsland(player.getUniqueId()) == null) {
			Util.sendMessage(player, ChatColor.RED + plugin.myLocale(player.getUniqueId()).errorNoIsland);
			return CommandType.SUCCESS;
		}
		plugin.getGrid().homeSet(player);

		return CommandType.SUCCESS;
	}

}
