package com.wasteofplastic.askyblock.command.commands.island;

import java.util.List;
import java.util.UUID;

import org.bukkit.ChatColor;

import com.wasteofplastic.askyblock.ASkyBlock;
import com.wasteofplastic.askyblock.Settings;
import com.wasteofplastic.askyblock.command.CommandType;
import com.wasteofplastic.askyblock.command.commands.IslandCommandBase;
import com.wasteofplastic.askyblock.util.Util;

public class IslandCommandBanList extends IslandCommandBase {

	public IslandCommandBanList() {
		this.addSubCommand("banlist");
		this.setSyntaxe("/is banlist");
		this.setDescription("Voir la liste des joueurs bannis de votre île");
		this.setPermission(Settings.PERMPREFIX + "island.ban");
	}

	@Override
	public CommandType postPerform(ASkyBlock plugin) {

		Util.sendMessage(player,
				ChatColor.GREEN + plugin.myLocale(playerUUID).adminInfoBannedPlayers + ":");
		List<UUID> bannedList = plugin.getPlayers().getBanList(playerUUID);
		if (bannedList.isEmpty()) {
			Util.sendMessage(player, ChatColor.RED + plugin.myLocale(playerUUID).banNone);
		} else {
			for (UUID bannedPlayers : bannedList) {
				Util.sendMessage(player,
						plugin.myLocale(playerUUID).helpColor + plugin.getPlayers().getName(bannedPlayers));
			}
		}

		return CommandType.SUCCESS;
	}

}
