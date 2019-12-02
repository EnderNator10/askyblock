package com.wasteofplastic.askyblock.command.commands.island.coop;

import java.util.UUID;

import org.bukkit.ChatColor;

import com.wasteofplastic.askyblock.ASkyBlock;
import com.wasteofplastic.askyblock.CoopPlay;
import com.wasteofplastic.askyblock.Island;
import com.wasteofplastic.askyblock.Settings;
import com.wasteofplastic.askyblock.command.CommandType;
import com.wasteofplastic.askyblock.command.commands.IslandCommandBase;
import com.wasteofplastic.askyblock.util.Util;

public class IslandCommandCoopList extends IslandCommandBase {

	public IslandCommandCoopList() {
		this.addSubCommand("listcoops");
		this.setSyntaxe("/is listcoops");
		this.setDescription("Voir la liste des joueurs coops");
		this.setPermission(Settings.PERMPREFIX + "coop");
		this.setIsland(true);
	}

	@Override
	public CommandType postPerform(ASkyBlock plugin) {

		Island island = plugin.getGrid().getIsland(playerUUID);
		boolean none = true;
		for (UUID uuid : CoopPlay.getInstance().getCoopPlayers(island.getCenter())) {
			Util.sendMessage(player, ChatColor.GREEN + plugin.getPlayers().getName(uuid));
			none = false;
		}
		if (none) {
			Util.sendMessage(player,
					"§f» §2/" + label + " coop <player>: §7" + plugin.myLocale(player.getUniqueId()).islandhelpCoop);
		} else {
			Util.sendMessage(player, "§f» §7" + plugin.myLocale(playerUUID).coopUseExpel);
		}

		return CommandType.SUCCESS;
	}

}
