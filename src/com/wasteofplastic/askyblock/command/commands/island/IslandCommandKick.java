package com.wasteofplastic.askyblock.command.commands.island;

import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;

import com.wasteofplastic.askyblock.ASkyBlock;
import com.wasteofplastic.askyblock.CoopPlay;
import com.wasteofplastic.askyblock.Settings;
import com.wasteofplastic.askyblock.command.CommandType;
import com.wasteofplastic.askyblock.command.commands.IslandCommandBase;
import com.wasteofplastic.askyblock.util.Util;

public class IslandCommandKick extends IslandCommandBase {

	public IslandCommandKick() {
		this.addSubCommand("kick");
		this.setSyntaxe("/is kick <joueur>");
		this.setDescription("Kick un joueur de votre île");
		this.setIgnoreArgs(true);
		this.setPermission(Settings.PERMPREFIX + "island.kick");
		this.setArgsLength(2);
	}

	@Override
	public CommandType postPerform(ASkyBlock plugin) {
		if (!plugin.getPlayers().inTeam(playerUUID)) {
			Util.sendMessage(player, ChatColor.RED + plugin.myLocale(player.getUniqueId()).kickerrorNoTeam);
			return CommandType.DEFAULT;
		}
		// Only leaders can kick
		if (teamLeader != null && !teamLeader.equals(playerUUID)) {
			Util.sendMessage(player, ChatColor.RED + plugin.myLocale(player.getUniqueId()).kickerrorOnlyLeaderCan);
			return CommandType.DEFAULT;
		}
		// The main thing to do is check if the player name to kick
		// is in the list of players in the team.
		targetPlayer = null;
		for (UUID member : teamMembers) {
			if (plugin.getPlayers().getName(member).equalsIgnoreCase(args[1])) {
				targetPlayer = member;
			}
		}
		if (targetPlayer == null) {
			Util.sendMessage(player, ChatColor.RED + plugin.myLocale(player.getUniqueId()).kickerrorNotPartOfTeam);
			return CommandType.DEFAULT;
		}
		if (teamMembers.contains(targetPlayer)) {
			// If the player leader tries to kick or remove
			// themselves
			if (player.getUniqueId().equals(targetPlayer)) {
				Util.sendMessage(player,
						ChatColor.RED + plugin.myLocale(player.getUniqueId()).leaveerrorLeadersCannotLeave);
				return CommandType.DEFAULT;
			}
			// Try to kick player
			if (!perform.removePlayerFromTeam(targetPlayer, teamLeader, false)) {
				// Util.sendMessage(player, ChatColor.RED +
				// plugin.myLocale(player.getUniqueId()).leaveerrorYouCannotLeaveIsland);
				// If this is canceled, fail silently
				return CommandType.DEFAULT;
			}
			// Log the location that this player left so they
			// cannot join again before the cool down ends
			plugin.getPlayers().startInviteCoolDownTimer(targetPlayer,
					plugin.getPlayers().getIslandLocation(playerUUID));
			if (Settings.resetChallenges) {
				// Reset the player's challenge status
				plugin.getPlayers().resetAllChallenges(targetPlayer, false);
			}
			// Reset the island level
			plugin.getPlayers().setIslandLevel(targetPlayer, 0);

			// If target is online
			Player target = plugin.getServer().getPlayer(targetPlayer);
			if (target != null) {
				// plugin.getLogger().info("DEBUG: player is
				// online");
				Util.sendMessage(target, ChatColor.RED
						+ plugin.myLocale(targetPlayer).kicknameRemovedYou.replace("[name]", player.getName()));
				// Clear any coop inventories
				// CoopPlay.getInstance().returnAllInventories(target);
				// Remove any of the target's coop invitees and
				// anyone they invited
				CoopPlay.getInstance().clearMyInvitedCoops(target);
				CoopPlay.getInstance().clearMyCoops(target);
				// Clear the player out and throw their stuff at the
				// leader
				if (target.getWorld().equals(ASkyBlock.getIslandWorld())) {
					if (!Settings.kickedKeepInv) {
						for (ItemStack i : target.getInventory().getContents()) {
							if (i != null) {
								try {
									// Fire an event to see if this
									// item should be dropped or not
									// Some plugins may not want
									// items to be dropped
									Item drop = player.getWorld().dropItemNaturally(player.getLocation(), i);
									PlayerDropItemEvent event = new PlayerDropItemEvent(target, drop);
									plugin.getServer().getPluginManager().callEvent(event);
								} catch (Exception e) {
								}
							}
						}
						// plugin.resetPlayer(target); <- no good if
						// reset inventory is false
						// Clear their inventory and equipment and
						// set
						// them as survival
						target.getInventory().clear(); // Javadocs
														// are
						// wrong - this
						// does not
						// clear armor slots! So...
						// plugin.getLogger().info("DEBUG: Clearing
						// kicked player's inventory");
						target.getInventory().setArmorContents(null);
						target.getInventory().setHelmet(null);
						target.getInventory().setChestplate(null);
						target.getInventory().setLeggings(null);
						target.getInventory().setBoots(null);
						target.getEquipment().clear();
						// Update the inventory
						target.updateInventory();
					}
				}
				if (!target.performCommand(Settings.SPAWNCOMMAND)) {
					target.teleport(ASkyBlock.getIslandWorld().getSpawnLocation());
				}
			} else {
				// Tell offline player they were kicked
				plugin.getMessages().setMessage(targetPlayer, ChatColor.RED
						+ plugin.myLocale(player.getUniqueId()).kicknameRemovedYou.replace("[name]", player.getName()));
			}
			// Remove any warps
			plugin.getWarpSignsListener().removeWarp(targetPlayer);
			// Tell leader they removed the player
			Util.sendMessage(player,
					ChatColor.RED + plugin.myLocale(player.getUniqueId()).kicknameRemoved.replace("[name]", args[1]));
			// removePlayerFromTeam(targetPlayer, teamLeader);
			teamMembers.remove(targetPlayer);
			if (teamMembers.size() < 2) {
				if (!perform.removePlayerFromTeam(player.getUniqueId(), teamLeader, false)) {
					// If cancelled, return silently
					return CommandType.SUCCESS;
				}
			}
			plugin.getPlayers().save(targetPlayer);
		}
		return CommandType.SUCCESS;
	}

}
