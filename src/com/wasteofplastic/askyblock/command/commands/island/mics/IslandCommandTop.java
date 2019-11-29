package com.wasteofplastic.askyblock.command.commands.island.mics;

import com.wasteofplastic.askyblock.ASkyBlock;
import com.wasteofplastic.askyblock.command.CommandType;
import com.wasteofplastic.askyblock.command.commands.IslandCommandBase;

public class IslandCommandTop extends IslandCommandBase {

	public IslandCommandTop() {
		this.addSubCommand("top");
		this.setSyntaxe("/is top");
		this.setDescription("Voir le classement des îles");
	}

	@Override
	public CommandType postPerform(ASkyBlock plugin) {
		plugin.getInventoryManager().createInventory(5, player, 1);
		return CommandType.SUCCESS;
	}

}
