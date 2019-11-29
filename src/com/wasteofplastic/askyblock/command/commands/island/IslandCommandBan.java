package com.wasteofplastic.askyblock.command.commands.island;

import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import com.wasteofplastic.askyblock.ASkyBlock;
import com.wasteofplastic.askyblock.CoopPlay;
import com.wasteofplastic.askyblock.Settings;
import com.wasteofplastic.askyblock.command.CommandType;
import com.wasteofplastic.askyblock.command.commands.IslandCommandBase;
import com.wasteofplastic.askyblock.util.Util;
import com.wasteofplastic.askyblock.util.VaultHelper;

public class IslandCommandBan extends IslandCommandBase {

	public IslandCommandBan() {
		this.addSubCommand("ban");
		this.setSyntaxe("/is ban <joueur>");
		this.setDescription("Bannir un un joueur");
		this.setPermission(Settings.PERMPREFIX + "island.ban");
		this.setArgsLength(2);
	}

	@Override
	public CommandType postPerform(ASkyBlock plugin) {
		// Find out who they want to ban
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
		// Target cannot be on the same team
		if (plugin.getPlayers().inTeam(playerUUID) && plugin.getPlayers().inTeam(targetPlayerUUID)) {
			if (plugin.getPlayers().getTeamLeader(playerUUID)
					.equals(plugin.getPlayers().getTeamLeader(targetPlayerUUID))) {
				// Same team!
				Util.sendMessage(player,
						ChatColor.RED + plugin.myLocale(player.getUniqueId()).banNotTeamMember);
				return CommandType.SUCCESS;
			}
		}
		// Check that the player is not banned already
		if (plugin.getPlayers().isBanned(playerUUID, targetPlayerUUID)) {
			Util.sendMessage(player,
					ChatColor.RED + plugin.myLocale(playerUUID).banAlreadyBanned.replace("[name]", args[1]));
			return CommandType.SUCCESS;
		}
		// Check online/offline status
		Player target = plugin.getServer().getPlayer(targetPlayerUUID);
		// Get offline player
		OfflinePlayer offlineTarget = plugin.getServer().getOfflinePlayer(targetPlayerUUID);
		// Target cannot be op
		if (offlineTarget.isOp() || (offlineTarget.isOnline() && !player.canSee(offlineTarget.getPlayer()))) {
			Util.sendMessage(player,
					ChatColor.RED + plugin.myLocale(player.getUniqueId()).banFail.replace("[name]", args[1]));
			return CommandType.SUCCESS;
		}
		if (target != null) {
			// Do not ban players with the mod.noban permission
			if (VaultHelper.checkPerm(target, Settings.PERMPREFIX + "admin.noban")) {
				Util.sendMessage(player, ChatColor.RED
						+ plugin.myLocale(player.getUniqueId()).banFail.replace("[name]", args[1]));
				return CommandType.SUCCESS;
			}
			// Remove them from the coop list
			boolean coop = CoopPlay.getInstance().removeCoopPlayer(player, target);
			if (coop) {
				Util.sendMessage(target, ChatColor.RED + plugin.myLocale(target.getUniqueId()).coopRemoved
						.replace("[name]", player.getName()));
				Util.sendMessage(player,
						ChatColor.GREEN + plugin.myLocale(player.getUniqueId()).coopRemoveSuccess
								.replace("[name]", target.getName()));
			}
			// See if target is on this player's island and if so send
			// them away
			if (plugin.getGrid().isOnIsland(player, target)) {
				// Check to see if this player has an island or is just
				// helping out
				if (plugin.getPlayers().inTeam(targetPlayerUUID)
						|| plugin.getPlayers().hasIsland(targetPlayerUUID)) {
					plugin.getGrid().homeTeleport(target);
				} else {
					// Just move target to spawn
					if (!target.performCommand(Settings.SPAWNCOMMAND)) {
						target.teleport(player.getWorld().getSpawnLocation());
					}
				}
			}
			// Notifications
			// Target
			Util.sendMessage(target, ChatColor.RED
					+ plugin.myLocale(targetPlayerUUID).banBanned.replace("[name]", player.getName()));
		} else {
			// Offline notification
			plugin.getMessages().setMessage(targetPlayerUUID, ChatColor.RED
					+ plugin.myLocale(targetPlayerUUID).banBanned.replace("[name]", player.getName()));
		}
		// Console
		plugin.getLogger().info(player.getName() + " banned " + args[1] + " from their island.");
		// Player
		Util.sendMessage(player,
				ChatColor.GREEN + plugin.myLocale(player.getUniqueId()).banSuccess.replace("[name]", args[1]));
		// Tell team
		plugin.getMessages().tellTeam(playerUUID,
				ChatColor.GREEN + plugin.myLocale(player.getUniqueId()).banSuccess.replace("[name]", args[1]));
		plugin.getMessages().tellOfflineTeam(playerUUID,
				ChatColor.GREEN + plugin.myLocale(player.getUniqueId()).banSuccess.replace("[name]", args[1]));
		// Ban the sucker
		plugin.getPlayers().ban(playerUUID, targetPlayerUUID);
		plugin.getGrid().saveGrid();
		return CommandType.SUCCESS;
	}

}
