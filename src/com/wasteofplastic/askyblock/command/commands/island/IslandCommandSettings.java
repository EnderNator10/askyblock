package com.wasteofplastic.askyblock.command.commands.island;

import org.bukkit.ChatColor;

import com.wasteofplastic.askyblock.ASkyBlock;
import com.wasteofplastic.askyblock.Settings;
import com.wasteofplastic.askyblock.command.CommandType;
import com.wasteofplastic.askyblock.command.commands.IslandCommandBase;
import com.wasteofplastic.askyblock.util.Util;

public class IslandCommandSettings extends IslandCommandBase {

	public IslandCommandSettings() {
		this.addSubCommand("settings");
		this.setSyntaxe("/is settings");
		this.setDescription("Changer les paramettres de votre île");
		this.setPermission(Settings.PERMPREFIX + "island.settings");
	}

	@Override
	public CommandType postPerform(ASkyBlock plugin) {
		try {
			player.openInventory(plugin.getSettingsPanel().islandGuardPanel(player));
		} catch (Exception e) {
			Util.sendMessage(player, ChatColor.RED + plugin.myLocale(player.getUniqueId()).errorCommandNotReady);
		}
		return CommandType.SUCCESS;

	}

}
