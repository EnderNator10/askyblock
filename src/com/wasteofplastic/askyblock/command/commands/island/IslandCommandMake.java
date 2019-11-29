package com.wasteofplastic.askyblock.command.commands.island;

import org.bukkit.ChatColor;

import com.wasteofplastic.askyblock.ASkyBlock;
import com.wasteofplastic.askyblock.Island;
import com.wasteofplastic.askyblock.command.CommandType;
import com.wasteofplastic.askyblock.command.commands.IslandCommandBase;
import com.wasteofplastic.askyblock.util.Util;

public class IslandCommandMake extends IslandCommandBase {

	public IslandCommandMake() {
		this.addSubCommand("make");
		this.setSyntaxe("/is make");
		this.setDescription("Créer une île");
	}

	@Override
	public CommandType postPerform(ASkyBlock plugin) {
		
		if (!perform.pendingNewIslandSelection.contains(playerUUID)) {
			Util.sendMessage(player, ChatColor.RED + plugin.myLocale().errorUnknownCommand);
			return CommandType.DEFAULT;
		}
		perform.pendingNewIslandSelection.remove(playerUUID);
		Island oldIsland = plugin.getGrid().getIsland(player.getUniqueId());
		perform.newIsland(player);
		if (perform.resettingIsland.contains(playerUUID)) {
			perform.resettingIsland.remove(playerUUID);
			perform.resetPlayer(player, oldIsland);
		}
		return CommandType.SUCCESS;

	}
	
}
