package com.wasteofplastic.askyblock.command.commands.island;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.wasteofplastic.askyblock.ASkyBlock;
import com.wasteofplastic.askyblock.CoopPlay;
import com.wasteofplastic.askyblock.Settings;
import com.wasteofplastic.askyblock.command.CommandType;
import com.wasteofplastic.askyblock.command.commands.IslandCommandBase;
import com.wasteofplastic.askyblock.util.Util;

public class IslandCommandLeave extends IslandCommandBase {

	private List<UUID> leavingPlayers = new ArrayList<>();
	
	public IslandCommandLeave() {
		this.addSubCommand("leave");
		this.setSyntaxe("/is leave");
		this.setDescription("Quitter votre île");
		this.setPermission(Settings.PERMPREFIX + "team.join");
	}

	@Override
	public CommandType postPerform(ASkyBlock plugin) {

		if (player.getWorld().equals(ASkyBlock.getIslandWorld())
				|| (Settings.createNether && Settings.newNether && ASkyBlock.getNetherWorld() != null
						&& player.getWorld().equals(ASkyBlock.getNetherWorld()))) {
			if (plugin.getPlayers().inTeam(playerUUID)) {
				if (plugin.getPlayers().getTeamLeader(playerUUID) != null
						&& plugin.getPlayers().getTeamLeader(playerUUID).equals(playerUUID)) {
					Util.sendMessage(player, ChatColor.YELLOW
							+ plugin.myLocale(player.getUniqueId()).leaveerrorYouAreTheLeader);
					return CommandType.SUCCESS;
				}
				// Check for confirmation
				if (!leavingPlayers.contains(playerUUID)) {
					leavingPlayers.add(playerUUID);
					Util.sendMessage(player,
							ChatColor.RED + plugin.myLocale(player.getUniqueId()).leaveWarning);
					new BukkitRunnable() {

						@Override
						public void run() {
							// If the player is still on the list,
							// remove them and cancel the leave
							if (leavingPlayers.contains(playerUUID)) {
								leavingPlayers.remove(playerUUID);
								Util.sendMessage(player, ChatColor.RED
										+ plugin.myLocale(player.getUniqueId()).leaveCanceled);
							}
						}

					}.runTaskLater(plugin, Settings.resetConfirmWait * 20L);
					return CommandType.SUCCESS;
				}
				// Remove from confirmation list
				leavingPlayers.remove(playerUUID);
				// Remove from team
				if (!perform.removePlayerFromTeam(playerUUID, teamLeader, false)) {
					// Util.sendMessage(player, ChatColor.RED +
					// plugin.myLocale(player.getUniqueId()).leaveerrorYouCannotLeaveIsland);
					// If this is canceled, fail silently
					return CommandType.SUCCESS;
				}
				// Clear any coop inventories
				// CoopPlay.getInstance().returnAllInventories(player);
				// Remove any of the target's coop invitees and grab
				// their stuff
				CoopPlay.getInstance().clearMyInvitedCoops(player);
				CoopPlay.getInstance().clearMyCoops(player);

				// Log the location that this player left so they
				// cannot join again before the cool down ends
				plugin.getPlayers().startInviteCoolDownTimer(playerUUID,
						plugin.getPlayers().getTeamIslandLocation(teamLeader));

				// Remove any warps
				plugin.getWarpSignsListener().removeWarp(playerUUID);
				Util.sendMessage(player,
						ChatColor.YELLOW + plugin.myLocale(player.getUniqueId()).leaveyouHaveLeftTheIsland);
				// Tell the leader if they are online
				if (plugin.getServer().getPlayer(teamLeader) != null) {
					Player leader = plugin.getServer().getPlayer(teamLeader);
					Util.sendMessage(leader,
							ChatColor.RED + plugin.myLocale(teamLeader).leavenameHasLeftYourIsland
									.replace("[name]", player.getName()));
				} else {
					// Leave them a message
					plugin.getMessages().setMessage(teamLeader,
							ChatColor.RED + plugin.myLocale(teamLeader).leavenameHasLeftYourIsland
									.replace("[name]", player.getName()));
				}
				// Check if the size of the team is now 1
				// teamMembers.remove(playerUUID);
				if (teamMembers.size() < 2) {
					// plugin.getLogger().info("DEBUG: Party is less
					// than 2 - removing leader from team");
					if (!perform.removePlayerFromTeam(teamLeader, teamLeader, false)) {
						// If this is canceled, return silently.
						return CommandType.SUCCESS;
					}
				}
				// Clear all player variables and save
				plugin.resetPlayer(player);
				if (!player.performCommand(Settings.SPAWNCOMMAND)) {
					player.teleport(player.getWorld().getSpawnLocation());
				}
				return CommandType.SUCCESS;
			} else {
				Util.sendMessage(player,
						ChatColor.RED + plugin.myLocale(playerUUID).leaveerrorYouCannotLeaveIsland);
				return CommandType.SUCCESS;
			}
		} else {
			Util.sendMessage(player,
					ChatColor.RED + plugin.myLocale(playerUUID).leaveerrorYouMustBeInWorld);
		}

		return CommandType.SUCCESS;
	}

}
