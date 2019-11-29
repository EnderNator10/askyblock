package com.wasteofplastic.askyblock.command.commands.island;

import org.bukkit.ChatColor;

import com.wasteofplastic.askyblock.ASkyBlock;
import com.wasteofplastic.askyblock.Settings;
import com.wasteofplastic.askyblock.command.CommandType;
import com.wasteofplastic.askyblock.command.commands.IslandCommandBase;
import com.wasteofplastic.askyblock.util.Util;

public class IslandCommandConfirm extends IslandCommandBase {

	public IslandCommandConfirm() {
		this.addSubCommand("confirm");
		this.setSyntaxe("/is confirm");
		this.setDescription("Confirmer une action");
		this.setPermission(Settings.PERMPREFIX + "island.sethome");
	}

	@Override
	public CommandType postPerform(ASkyBlock plugin) {

		if (perform.confirm.containsKey(playerUUID) && perform.confirm.get(playerUUID)) {
			perform.confirm.remove(playerUUID);
			// Actually RESET the island
			Util.sendMessage(player,
					ChatColor.YELLOW + plugin.myLocale(player.getUniqueId()).islandresetPleaseWait);
			if (plugin.getPlayers().getResetsLeft(playerUUID) == 0) {
				Util.sendMessage(player,
						ChatColor.YELLOW + plugin.myLocale(player.getUniqueId()).islandResetNoMore);
			}
			if (plugin.getPlayers().getResetsLeft(playerUUID) > 0) {
				Util.sendMessage(player, ChatColor.YELLOW + plugin.myLocale(player.getUniqueId()).resetYouHave
						.replace("[number]", String.valueOf(plugin.getPlayers().getResetsLeft(playerUUID))));
			}
			perform.chooseIsland(player);
			return CommandType.SUCCESS;
		} else {
			Util.sendMessage(player, plugin.myLocale(player.getUniqueId()).helpColor + "/island restart: "
					+ ChatColor.WHITE + plugin.myLocale(player.getUniqueId()).islandhelpRestart);
			return CommandType.SUCCESS;
		}

	}

}
