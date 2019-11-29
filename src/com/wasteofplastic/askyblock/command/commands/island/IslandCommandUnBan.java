package com.wasteofplastic.askyblock.command.commands.island;

import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.wasteofplastic.askyblock.ASkyBlock;
import com.wasteofplastic.askyblock.Settings;
import com.wasteofplastic.askyblock.command.CommandType;
import com.wasteofplastic.askyblock.command.commands.IslandCommandBase;
import com.wasteofplastic.askyblock.util.Util;

public class IslandCommandUnBan extends IslandCommandBase {

	public IslandCommandUnBan() {
		this.addSubCommand("unban");
		this.setSyntaxe("/is unban <joueur>");
		this.setDescription("Révoquer le banissement d'un joueur");
		this.setIgnoreArgs(true);
		this.setPermission(Settings.PERMPREFIX + "island.ban");
		this.setArgsLength(2);
	}

	@Override
	public CommandType postPerform(ASkyBlock plugin) {
		// Find out who they want to unban
		final UUID targetPlayerUUID = plugin.getPlayers().getUUID(args[1]);
		// Player must be known
		if (targetPlayerUUID == null) {
			Util.sendMessage(player, ChatColor.RED + plugin.myLocale(player.getUniqueId()).errorUnknownPlayer);
			return CommandType.SUCCESS;
		}
		// Target should not be themselves
		if (targetPlayerUUID.equals(playerUUID)) {
			Util.sendMessage(player, ChatColor.RED + plugin.myLocale(player.getUniqueId()).banNotYourself);
			return CommandType.SUCCESS;
		}
		// Check that the player is actually banned
		if (!plugin.getPlayers().isBanned(playerUUID, targetPlayerUUID)) {
			Util.sendMessage(player,
					ChatColor.RED + plugin.myLocale(player.getUniqueId()).banNotBanned.replace("[name]", args[1]));
			return CommandType.SUCCESS;
		}
		// Notifications
		// Online check
		Player target = plugin.getServer().getPlayer(targetPlayerUUID);
		// Target
		if (target != null) {
			// Online
			Util.sendMessage(target, ChatColor.RED
					+ plugin.myLocale(target.getUniqueId()).banLifted.replace("[name]", player.getName()));
		} else {
			plugin.getMessages().setMessage(targetPlayerUUID,
					ChatColor.GREEN + plugin.myLocale(targetPlayerUUID).banLifted.replace("[name]", player.getName()));
		}
		// OfflinePlayer offlineTarget =
		// plugin.getServer().getOfflinePlayer(targetPlayerUUID);
		// Player
		Util.sendMessage(player,
				ChatColor.GREEN + plugin.myLocale(player.getUniqueId()).banLiftedSuccess.replace("[name]", args[1]));
		// Console
		plugin.getLogger().info(player.getName() + " unbanned " + args[1] + " from their island.");
		// Tell team
		plugin.getMessages().tellTeam(playerUUID,
				ChatColor.GREEN + plugin.myLocale(player.getUniqueId()).banLiftedSuccess.replace("[name]", args[1]));
		plugin.getMessages().tellOfflineTeam(playerUUID,
				ChatColor.GREEN + plugin.myLocale(player.getUniqueId()).banLiftedSuccess.replace("[name]", args[1]));
		// Unban the redeemed one
		plugin.getPlayers().unBan(playerUUID, targetPlayerUUID);
		plugin.getGrid().saveGrid();
		return CommandType.SUCCESS;
	}

}
