package com.wasteofplastic.askyblock.command.commands.island;

import java.util.Map;
import java.util.UUID;
import java.util.Map.Entry;

import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachmentInfo;

import com.wasteofplastic.askyblock.ASkyBlock;
import com.wasteofplastic.askyblock.Settings;
import com.wasteofplastic.askyblock.command.CommandType;
import com.wasteofplastic.askyblock.command.commands.IslandCommandBase;
import com.wasteofplastic.askyblock.util.Util;

public class IslandCommandInvite extends IslandCommandBase {

	public IslandCommandInvite() {
		this.addSubCommand("invite");
		this.setSyntaxe("/is invite <player>");
		this.setDescription("Inviter un joueur sur son île");
		this.setIgnoreArgs(true);
		this.setPermission(Settings.PERMPREFIX + "team.create");
		this.setArgsLength(2);
	}

	@Override
	public CommandType postPerform(ASkyBlock plugin) {
		// If the player who is doing the inviting has a team
		// Only online players can be invited
		Player invitedPlayer = plugin.getServer().getPlayer(args[1]);
		if (invitedPlayer == null || !player.canSee(invitedPlayer)) {
			Util.sendMessage(player, ChatColor.RED + plugin.myLocale(player.getUniqueId()).errorOfflinePlayer);
			return CommandType.DEFAULT;
		}
		final UUID invitedPlayerUUID = invitedPlayer.getUniqueId();
		// Player issuing the command must have an island
		if (!plugin.getPlayers().hasIsland(player.getUniqueId())) {
			Util.sendMessage(player,
					ChatColor.RED + plugin.myLocale(player.getUniqueId()).inviteerrorYouMustHaveIslandToInvite);
			return CommandType.DEFAULT;
		}
		// Player cannot invite themselves
		if (player.getUniqueId().equals(invitedPlayer.getUniqueId())) {
			Util.sendMessage(player,
					ChatColor.RED + plugin.myLocale(player.getUniqueId()).inviteerrorYouCannotInviteYourself);
			return CommandType.DEFAULT;
		}
		// Check if this player can be invited to this island, or
		// whether they are still on cooldown
		long time = plugin.getPlayers().getInviteCoolDownTime(invitedPlayerUUID,
				plugin.getPlayers().getIslandLocation(playerUUID));
		if (time > 0 && !player.isOp()) {
			Util.sendMessage(player, ChatColor.RED + plugin.myLocale(player.getUniqueId()).inviteerrorCoolDown
					.replace("[time]", String.valueOf(time)));
			return CommandType.DEFAULT;
		}
		// If the player already has a team then check that they are
		// the leader, etc
		if (plugin.getPlayers().inTeam(player.getUniqueId())) {
			// Leader?
			if (teamLeader.equals(player.getUniqueId())) {
				// Invited player is free and not in a team
				if (!plugin.getPlayers().inTeam(invitedPlayerUUID)) {
					// Player has space in their team
					int maxSize = Settings.maxTeamSize;
					// Dynamic team sizes with permissions
					for (PermissionAttachmentInfo perms : player.getEffectivePermissions()) {
						if (perms.getPermission().startsWith(Settings.PERMPREFIX + "team.maxsize.")) {
							if (perms.getPermission().contains(Settings.PERMPREFIX + "team.maxsize.*")) {
								maxSize = Settings.maxTeamSize;
								break;
							} else {
								// Get the max value should there be
								// more than one
								String[] spl = perms.getPermission().split(Settings.PERMPREFIX + "team.maxsize.");
								if (spl.length > 1) {
									if (!NumberUtils.isDigits(spl[1])) {
										plugin.getLogger()
												.severe("Player " + player.getName() + " has permission: "
														+ perms.getPermission()
														+ " <-- the last part MUST be a number! Ignoring...");

									} else {
										maxSize = Math.max(maxSize, Integer.valueOf(spl[1]));
									}
								}
							}
						}
						// Do some sanity checking
						if (maxSize < 1) {
							maxSize = 1;
						}
					}
					if (teamMembers.size() < maxSize) {
						// If that player already has an invite out
						// then retract it.
						// Players can only have one invite out at a
						// time - interesting
						if (perform.inviteList.containsValue(playerUUID)) {
							perform.inviteList.remove(getKeyByValue(perform.inviteList, player.getUniqueId()));
							Util.sendMessage(player,
									ChatColor.YELLOW + plugin.myLocale(player.getUniqueId()).inviteremovingInvite);
						}
						// Put the invited player (key) onto the
						// list with inviter (value)
						// If someone else has invited a player,
						// then this invite will overwrite the
						// previous invite!
						perform.inviteList.put(invitedPlayerUUID, player.getUniqueId());
						Util.sendMessage(player,
								ChatColor.GREEN + plugin.myLocale(player.getUniqueId()).inviteinviteSentTo
										.replace("[name]", invitedPlayer.getName()));
						// Send message to online player
						Util.sendMessage(Bukkit.getPlayer(invitedPlayerUUID),
								plugin.myLocale(invitedPlayerUUID).invitenameHasInvitedYou.replace("[name]",
										player.getName()));
						Util.sendMessage(Bukkit.getPlayer(invitedPlayerUUID),
								ChatColor.WHITE + "/" + label + " [accept/reject]" + ChatColor.YELLOW + " "
										+ plugin.myLocale(invitedPlayerUUID).invitetoAcceptOrReject);
						if (plugin.getPlayers().hasIsland(invitedPlayerUUID)) {
							Util.sendMessage(Bukkit.getPlayer(invitedPlayerUUID),
									ChatColor.RED + plugin.myLocale(invitedPlayerUUID).invitewarningYouWillLoseIsland);
						}
						// Start timeout on invite
						if (Settings.inviteTimeout > 0) {
							plugin.getServer().getScheduler().runTaskLater(plugin, new Runnable() {

								@Override
								public void run() {
									if (perform.inviteList.containsKey(invitedPlayerUUID)
											&& perform.inviteList.get(invitedPlayerUUID).equals(playerUUID)) {
										perform.inviteList.remove(invitedPlayerUUID);
										if (plugin.getServer().getPlayer(playerUUID) != null) {
											Util.sendMessage(plugin.getServer().getPlayer(playerUUID), ChatColor.YELLOW
													+ plugin.myLocale(player.getUniqueId()).inviteremovingInvite);
										}
										if (plugin.getServer().getPlayer(invitedPlayerUUID) != null) {
											Util.sendMessage(plugin.getServer().getPlayer(invitedPlayerUUID),
													ChatColor.YELLOW + plugin
															.myLocale(player.getUniqueId()).inviteremovingInvite);
										}
									}

								}
							}, Settings.inviteTimeout);
						}
					} else {
						Util.sendMessage(player,
								ChatColor.RED + plugin.myLocale(player.getUniqueId()).inviteerrorYourIslandIsFull);
					}
				} else {
					Util.sendMessage(player, ChatColor.RED
							+ plugin.myLocale(player.getUniqueId()).inviteerrorThatPlayerIsAlreadyInATeam);
				}
			} else {
				Util.sendMessage(player,
						ChatColor.RED + plugin.myLocale(player.getUniqueId()).inviteerrorYouMustHaveIslandToInvite);
			}
		} else {
			// First-time invite player does not have a team
			// Check if invitee is in a team or not
			if (!plugin.getPlayers().inTeam(invitedPlayerUUID)) {
				// If the inviter already has an invite out, remove
				// it
				if (perform.inviteList.containsValue(playerUUID)) {
					perform.inviteList.remove(getKeyByValue(perform.inviteList, player.getUniqueId()));
					Util.sendMessage(player,
							ChatColor.YELLOW + plugin.myLocale(player.getUniqueId()).inviteremovingInvite);
				}
				// Place the player and invitee on the invite list
				perform.inviteList.put(invitedPlayerUUID, player.getUniqueId());
				Util.sendMessage(player, ChatColor.GREEN + plugin.myLocale(player.getUniqueId()).inviteinviteSentTo
						.replace("[name]", invitedPlayer.getName()));
				Util.sendMessage(Bukkit.getPlayer(invitedPlayerUUID),
						plugin.myLocale(invitedPlayerUUID).invitenameHasInvitedYou.replace("[name]", player.getName()));
				Util.sendMessage(Bukkit.getPlayer(invitedPlayerUUID), ChatColor.WHITE + "/" + label + " [accept/reject]"
						+ ChatColor.YELLOW + " " + plugin.myLocale(invitedPlayerUUID).invitetoAcceptOrReject);
				// Check if the player has an island and warn
				// accordingly
				// plugin.getLogger().info("DEBUG: invited player =
				// "
				// + invitedPlayerUUID.toString());
				if (plugin.getPlayers().hasIsland(invitedPlayerUUID)) {
					// plugin.getLogger().info("DEBUG: invited
					// player has island");
					Util.sendMessage(Bukkit.getPlayer(invitedPlayerUUID),
							ChatColor.RED + plugin.myLocale(invitedPlayerUUID).invitewarningYouWillLoseIsland);
				}
				// Start timeout on invite
				if (Settings.inviteTimeout > 0) {
					plugin.getServer().getScheduler().runTaskLater(plugin, new Runnable() {

						@Override
						public void run() {

							if (perform.inviteList.containsKey(invitedPlayerUUID)
									&& perform.inviteList.get(invitedPlayerUUID).equals(playerUUID)) {
								perform.inviteList.remove(invitedPlayerUUID);
								if (plugin.getServer().getPlayer(playerUUID) != null) {
									Util.sendMessage(plugin.getServer().getPlayer(playerUUID), ChatColor.YELLOW
											+ plugin.myLocale(player.getUniqueId()).inviteremovingInvite);
								}
								if (plugin.getServer().getPlayer(invitedPlayerUUID) != null) {
									Util.sendMessage(plugin.getServer().getPlayer(invitedPlayerUUID), ChatColor.YELLOW
											+ plugin.myLocale(player.getUniqueId()).inviteremovingInvite);
								}
							}

						}
					}, Settings.inviteTimeout);
				}
			} else {
				Util.sendMessage(player,
						ChatColor.RED + plugin.myLocale(player.getUniqueId()).inviteerrorThatPlayerIsAlreadyInATeam);
			}
		}
		return CommandType.SUCCESS;
	}
	
	/**
	 * One-to-one relationship, you can return the first matched key
	 *
	 * @param map
	 * @param value
	 * @return key
	 */
	public static <T, E> T getKeyByValue(Map<T, E> map, E value) {
		for (Entry<T, E> entry : map.entrySet()) {
			if (value.equals(entry.getValue())) {
				return entry.getKey();
			}
		}
		return null;
	}

}
