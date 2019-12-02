package com.wasteofplastic.askyblock.command.commands.island;

import org.bukkit.ChatColor;

import com.wasteofplastic.askyblock.ASkyBlock;
import com.wasteofplastic.askyblock.command.CommandType;
import com.wasteofplastic.askyblock.command.commands.IslandCommandBase;
import com.wasteofplastic.askyblock.fly.FlyManager;
import com.wasteofplastic.askyblock.util.Util;

public class IslandCommandFly extends IslandCommandBase {

	public IslandCommandFly() {
		this.addSubCommand("fly");
		this.setSyntaxe("/is fly");
		this.setDescription("Permet d'obtenir la permission de voler sur votre île");
	}

	@Override
	public CommandType postPerform(ASkyBlock plugin) {

		if (!plugin.getGrid().playerIsOnIsland(player)) {
			Util.sendMessage(player, "§f» " +ChatColor.RED + plugin.myLocale(player.getUniqueId()).errorNotOnIsland);
			return CommandType.DEFAULT;
		}
		FlyManager.getInstance().update(player);

		return CommandType.SUCCESS;
	}

}
