package com.wasteofplastic.askyblock.command.commands.island.coop;

import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;

import com.wasteofplastic.askyblock.ASkyBlock;
import com.wasteofplastic.askyblock.CoopPlay;
import com.wasteofplastic.askyblock.Settings;
import com.wasteofplastic.askyblock.command.CommandType;
import com.wasteofplastic.askyblock.command.commands.IslandCommandBase;
import com.wasteofplastic.askyblock.util.Util;

public class IslandCommandUnCoop extends IslandCommandBase {

	public IslandCommandUnCoop() {
		this.addSubCommand("uncoop");
		this.setSyntaxe("/is uncoop <joueur>");
		this.setDescription("Uncoop un joueur");
		this.setPermission(Settings.PERMPREFIX + "coop");
		this.setArgsLength(2);
	}

	@Override
	public CommandType postPerform(ASkyBlock plugin) {
		// Find out who they want to uncoop
		UUID targetPlayerUUID = plugin.getPlayers().getUUID(args[1]);
		if (targetPlayerUUID == null) {
			Util.sendMessage(player, ChatColor.RED + plugin.myLocale(player.getUniqueId()).errorUnknownPlayer);
			return CommandType.SUCCESS;
		}
		// Target should not be themselves
		if (targetPlayerUUID.equals(playerUUID)) {
			Util.sendMessage(player, ChatColor.RED + plugin.myLocale(player.getUniqueId()).expelNotYourself);
			return CommandType.SUCCESS;
		}
		OfflinePlayer target = plugin.getServer().getOfflinePlayer(targetPlayerUUID);
		// Remove them from the coop list
		boolean coop = CoopPlay.getInstance().removeCoopPlayer(player, targetPlayerUUID);
		if (coop) {
			if (target != null && target.isOnline()) {
				Util.sendMessage(target.getPlayer(),
						ChatColor.RED + plugin.myLocale(target.getUniqueId()).coopRemoved.replace("[name]",
								player.getName()));
			} else {
				plugin.getMessages().setMessage(targetPlayerUUID, ChatColor.RED
						+ plugin.myLocale(targetPlayerUUID).coopRemoved.replace("[name]", player.getName()));
			}
			Util.sendMessage(player, ChatColor.GREEN + plugin.myLocale(player.getUniqueId()).coopRemoveSuccess
					.replace("[name]", plugin.getPlayers().getName(targetPlayerUUID)));
		} else {
			Util.sendMessage(player, ChatColor.RED + plugin.myLocale(player.getUniqueId()).coopNotInCoop
					.replace("[name]", plugin.getPlayers().getName(targetPlayerUUID)));
		}
		return CommandType.SUCCESS;
	}

}
