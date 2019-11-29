package com.wasteofplastic.askyblock.command.commands.island;

import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachmentInfo;

import com.wasteofplastic.askyblock.ASkyBlock;
import com.wasteofplastic.askyblock.Island;
import com.wasteofplastic.askyblock.Settings;
import com.wasteofplastic.askyblock.command.CommandType;
import com.wasteofplastic.askyblock.command.commands.IslandCommandBase;
import com.wasteofplastic.askyblock.util.Util;

public class IslandCommandMakeLeader extends IslandCommandBase {

	public IslandCommandMakeLeader() {
		this.addSubCommand("makeleader");
		this.setSyntaxe("/is makeleader");
		this.setDescription("Changer le chef de votre île");
		this.setPermission(Settings.PERMPREFIX + "team.makeleader");
		this.setIgnoreArgs(true);
	}

	@Override
	public CommandType postPerform(ASkyBlock plugin) {

		targetPlayer = plugin.getPlayers().getUUID(args[1]);
		if (targetPlayer == null) {
			Util.sendMessage(player, ChatColor.RED + plugin.myLocale(player.getUniqueId()).errorUnknownPlayer);
			return CommandType.SUCCESS;
		}
		if (targetPlayer.equals(playerUUID)) {
			Util.sendMessage(player, ChatColor.RED + plugin.myLocale(player.getUniqueId()).makeLeadererrorGeneralError);
			return CommandType.SUCCESS;
		}
		if (!plugin.getPlayers().inTeam(player.getUniqueId())) {
			Util.sendMessage(player,
					ChatColor.RED + plugin.myLocale(player.getUniqueId()).makeLeadererrorYouMustBeInTeam);
			return CommandType.SUCCESS;
		}

		if (plugin.getPlayers().getMembers(player.getUniqueId()).size() > 2) {
			Util.sendMessage(player,
					ChatColor.RED + plugin.myLocale(player.getUniqueId()).makeLeadererrorRemoveAllPlayersFirst);
			plugin.getLogger()
					.info(player.getName() + " tried to transfer his island, but failed because >2 people in a team");
			return CommandType.SUCCESS;
		}

		if (plugin.getPlayers().inTeam(player.getUniqueId())) {
			if (teamLeader.equals(player.getUniqueId())) {
				if (teamMembers.contains(targetPlayer)) {
					// targetPlayer is the new leader
					// plugin.getLogger().info("DEBUG: " +
					// plugin.getPlayers().getIslandLevel(teamLeader));
					// Remove the target player from the team
					if (!perform.removePlayerFromTeam(targetPlayer, teamLeader, true)) {
						// If cancelled, return silently
						return CommandType.SUCCESS;
					}
					// Remove the leader from the team
					if (!perform.removePlayerFromTeam(teamLeader, teamLeader, true)) {
						// If cancelled, return silently
						return CommandType.SUCCESS;
					}
					Util.sendMessage(player,
							ChatColor.GREEN + plugin.myLocale(player.getUniqueId()).makeLeadernameIsNowTheOwner
									.replace("[name]", plugin.getPlayers().getName(targetPlayer)));

					// plugin.getLogger().info("DEBUG: " +
					// plugin.getPlayers().getIslandLevel(teamLeader));
					// Transfer the data from the old leader to the
					// new one
					plugin.getGrid().transferIsland(player.getUniqueId(), targetPlayer);
					// Create a new team with
					perform.addPlayertoTeam(player.getUniqueId(), targetPlayer);
					perform.addPlayertoTeam(targetPlayer, targetPlayer);

					// Check if online
					Player target = plugin.getServer().getPlayer(targetPlayer);
					if (target == null) {
						plugin.getMessages().setMessage(targetPlayer,
								plugin.myLocale(player.getUniqueId()).makeLeaderyouAreNowTheOwner);

					} else {
						// Online
						Util.sendMessage(plugin.getServer().getPlayer(targetPlayer),
								ChatColor.GREEN + plugin.myLocale(targetPlayer).makeLeaderyouAreNowTheOwner);
						// Check if new leader has a lower range
						// permission than the island size
						boolean hasARangePerm = false;
						int range = Settings.islandProtectionRange;
						// Check for zero protection range
						Island islandByOwner = plugin.getGrid().getIsland(targetPlayer);
						if (islandByOwner.getProtectionSize() == 0) {
							plugin.getLogger().warning("Player " + player.getName()
									+ "'s island had a protection range of 0. Setting to default " + range);
							islandByOwner.setProtectionSize(range);
						}
						for (PermissionAttachmentInfo perms : target.getEffectivePermissions()) {
							if (perms.getPermission().startsWith(Settings.PERMPREFIX + "island.range.")) {
								if (perms.getPermission().contains(Settings.PERMPREFIX + "island.range.*")) {
									// Ignore
									break;
								} else {
									String[] spl = perms.getPermission().split(Settings.PERMPREFIX + "island.range.");
									if (spl.length > 1) {
										if (!NumberUtils.isDigits(spl[1])) {
											plugin.getLogger()
													.severe("Player " + player.getName() + " has permission: "
															+ perms.getPermission()
															+ " <-- the last part MUST be a number! Ignoring...");

										} else {
											hasARangePerm = true;
											range = Math.max(range, Integer.valueOf(spl[1]));
										}
									}
								}
							}
						}
						// Only set the island range if the player
						// has a perm to override the default
						if (hasARangePerm) {
							// Do some sanity checking
							if (range % 2 != 0) {
								range--;
							}
							// Get island range

							// Range can go up or down
							if (range != islandByOwner.getProtectionSize()) {
								Util.sendMessage(player,
										ChatColor.GOLD + plugin.myLocale(targetPlayer).adminSetRangeUpdated
												.replace("[number]", String.valueOf(range)));
								Util.sendMessage(target,
										ChatColor.GOLD + plugin.myLocale(targetPlayer).adminSetRangeUpdated
												.replace("[number]", String.valueOf(range)));
								plugin.getLogger()
										.info("Makeleader: Island protection range changed from "
												+ islandByOwner.getProtectionSize() + " to " + range + " for "
												+ player.getName() + " due to permission.");
							}
							islandByOwner.setProtectionSize(range);
						}
					}
					plugin.getGrid().saveGrid();
					return CommandType.SUCCESS;
				}
				Util.sendMessage(player,
						ChatColor.RED + plugin.myLocale(player.getUniqueId()).makeLeadererrorThatPlayerIsNotInTeam);
			} else {
				Util.sendMessage(player,
						ChatColor.RED + plugin.myLocale(player.getUniqueId()).makeLeadererrorNotYourIsland);
			}
		} else {
			Util.sendMessage(player, ChatColor.RED + plugin.myLocale(player.getUniqueId()).makeLeadererrorGeneralError);
		}

		return CommandType.SUCCESS;
	}

}
