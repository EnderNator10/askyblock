package com.wasteofplastic.askyblock.command.commands.island.name;

import com.wasteofplastic.askyblock.ASkyBlock;
import com.wasteofplastic.askyblock.Settings;
import com.wasteofplastic.askyblock.command.CommandType;
import com.wasteofplastic.askyblock.command.commands.IslandCommandBase;
import com.wasteofplastic.askyblock.util.Util;

public class IslandCommandResetName extends IslandCommandBase {

	public IslandCommandResetName() {
		this.addSubCommand("resetname");
		this.setSyntaxe("/is resetname");
		this.setDescription("Rénitialiser le nom de votre île");
		this.setPermission(Settings.PERMPREFIX + "island.name");
	}

	@Override
	public CommandType postPerform(ASkyBlock plugin) {

		plugin.getGrid().setIslandName(playerUUID, null);
		Util.sendMessage(sender, plugin.myLocale().generalSuccess);

		return CommandType.SUCCESS;
	}

}
