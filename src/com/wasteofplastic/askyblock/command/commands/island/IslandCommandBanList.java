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

		Util.sendMessage(player, "§f» " + ChatColor.GRAY + plugin.myLocale(playerUUID).adminInfoBannedPlayers + ":");
		List<UUID> bannedList = plugin.getPlayers().getBanList(playerUUID);
		if (bannedList.isEmpty()) {
			Util.sendMessage(player, "§f» " + ChatColor.RED + "Aucune joueur bannis");
		} else {
			for (UUID bannedPlayers : bannedList) {
				Util.sendMessage(player, "§f» §7" + plugin.getPlayers().getName(bannedPlayers));
			}
		}

		return CommandType.SUCCESS;
	}

}
