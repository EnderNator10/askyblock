package com.wasteofplastic.askyblock.command.commands.island;

import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.wasteofplastic.askyblock.ASkyBlock;
import com.wasteofplastic.askyblock.CoopPlay;
import com.wasteofplastic.askyblock.Settings;
import com.wasteofplastic.askyblock.command.CommandType;
import com.wasteofplastic.askyblock.command.commands.IslandCommandBase;
import com.wasteofplastic.askyblock.util.Util;
import com.wasteofplastic.askyblock.util.VaultHelper;

public class IslandCommandExpel extends IslandCommandBase {

	public IslandCommandExpel() {
		this.addSubCommand("expel");
		this.setSyntaxe("/is expel <joueur>");
		this.setDescription("Je sais pas");
		this.setPermission(Settings.PERMPREFIX + "island.expel");
		this.setArgsLength(2);
	}

	@Override
	public CommandType postPerform(ASkyBlock plugin) {
		// Find out who they want to expel
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
		// Target cannot be op
		Player target = plugin.getServer().getPlayer(targetPlayerUUID);
		if (target != null) {
			if (!player.canSee(target)) {
				Util.sendMessage(player, ChatColor.RED + plugin.myLocale(player.getUniqueId()).errorUnknownPlayer);
				return CommandType.SUCCESS;
			}
			if (target.isOp() || VaultHelper.checkPerm(target, Settings.PERMPREFIX + "mod.bypassprotect")
					|| VaultHelper.checkPerm(target, Settings.PERMPREFIX + "mod.bypassexpel")) {
				Util.sendMessage(player, ChatColor.RED
						+ plugin.myLocale(player.getUniqueId()).expelFail.replace("[name]", target.getName()));
				return CommandType.SUCCESS;
			}
		}
		// Remove them from the coop list
		boolean coop = CoopPlay.getInstance().removeCoopPlayer(player, targetPlayerUUID);
		if (coop) {
			if (target != null) {
				Util.sendMessage(target, ChatColor.RED
						+ plugin.myLocale(target.getUniqueId()).coopRemoved.replace("[name]", player.getName()));
			} else {
				plugin.getMessages().setMessage(targetPlayerUUID, ChatColor.RED
						+ plugin.myLocale(targetPlayerUUID).coopRemoved.replace("[name]", player.getName()));
			}
			Util.sendMessage(player, ChatColor.GREEN + plugin.myLocale(player.getUniqueId()).coopRemoveSuccess
					.replace("[name]", plugin.getPlayers().getName(targetPlayerUUID)));
		}
		// See if target is on this player's island
		if (target != null && plugin.getGrid().isOnIsland(player, target)) {
			// Check to see if this player has an island or is just
			// helping out
			if (plugin.getPlayers().inTeam(targetPlayerUUID) || plugin.getPlayers().hasIsland(targetPlayerUUID)) {
				plugin.getGrid().homeTeleport(target);
			} else {
				// Just move target to spawn
				if (!target.performCommand(Settings.SPAWNCOMMAND)) {
					target.teleport(player.getWorld().getSpawnLocation());
					/*
					 * target.sendBlockChange(target.getWorld().
					 * getSpawnLocation()
					 * ,target.getWorld().getSpawnLocation().getBlock().
					 * getType()
					 * ,target.getWorld().getSpawnLocation().getBlock().
					 * getData());
					 */
				}
			}
			Util.sendMessage(target, ChatColor.RED + plugin.myLocale(target.getUniqueId()).expelExpelled);
			plugin.getLogger().info(player.getName() + " expelled " + target.getName() + " from their island.");
			// Yes they are
			Util.sendMessage(player, ChatColor.GREEN
					+ plugin.myLocale(player.getUniqueId()).expelSuccess.replace("[name]", target.getName()));
		} else if (!coop) {
			// No they're not
			Util.sendMessage(player, ChatColor.RED + plugin.myLocale(player.getUniqueId()).expelNotOnIsland);
		}
		return CommandType.SUCCESS;
	}

}
