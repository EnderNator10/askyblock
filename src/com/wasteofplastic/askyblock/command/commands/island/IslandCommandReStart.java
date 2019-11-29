package com.wasteofplastic.askyblock.command.commands.island;

import org.bukkit.ChatColor;

import com.wasteofplastic.askyblock.ASkyBlock;
import com.wasteofplastic.askyblock.Settings;
import com.wasteofplastic.askyblock.command.CommandType;
import com.wasteofplastic.askyblock.command.commands.IslandCommandBase;
import com.wasteofplastic.askyblock.util.Util;

public class IslandCommandReStart extends IslandCommandBase {

	public IslandCommandReStart() {
		this.addSubCommand("restart");
		this.setSyntaxe("/is restart");
		this.setDescription("Recommencer son île");
		this.setPermission(Settings.PERMPREFIX + "island.reset");
		this.setIsland(true);
	}

	@Override
	public CommandType postPerform(ASkyBlock plugin) {

		if (plugin.getPlayers().inTeam(playerUUID)) {
			if (!plugin.getPlayers().getTeamLeader(playerUUID).equals(playerUUID)) {
				Util.sendMessage(player, ChatColor.RED + plugin.myLocale(player.getUniqueId()).islandresetOnlyOwner);
			} else {
				Util.sendMessage(player,
						ChatColor.YELLOW + plugin.myLocale(player.getUniqueId()).islandresetMustRemovePlayers);
			}
			return CommandType.SUCCESS;
		}
		// Check if the player has used up all their resets
		if (plugin.getPlayers().getResetsLeft(playerUUID) == 0) {
			Util.sendMessage(player, ChatColor.RED + plugin.myLocale(player.getUniqueId()).islandResetNoMore);
			return CommandType.SUCCESS;
		}
		if (plugin.getPlayers().getResetsLeft(playerUUID) > 0) {
			Util.sendMessage(player, ChatColor.RED + plugin.myLocale(player.getUniqueId()).resetYouHave
					.replace("[number]", String.valueOf(plugin.getPlayers().getResetsLeft(playerUUID))));
		}
		if (!perform.onRestartWaitTime(player) || Settings.resetWait == 0 || player.isOp()) {
			// Kick off the confirmation
			Util.sendMessage(player, ChatColor.RED + plugin.myLocale(player.getUniqueId()).islandresetConfirm
					.replace("[seconds]", String.valueOf(Settings.resetConfirmWait)));
			if (!perform.confirm.containsKey(playerUUID) || !perform.confirm.get(playerUUID)) {
				perform.confirm.put(playerUUID, true);
				plugin.getServer().getScheduler().runTaskLater(plugin, new Runnable() {
					@Override
					public void run() {
						perform.confirm.put(playerUUID, false);
					}
				}, (Settings.resetConfirmWait * 20));
			}
			return CommandType.SUCCESS;
		} else {
			Util.sendMessage(player, ChatColor.YELLOW + plugin.myLocale(player.getUniqueId()).islandresetWait
					.replace("[time]", String.valueOf(perform.getResetWaitTime(player))));
		}
		return CommandType.SUCCESS;

	}

}
