package com.wasteofplastic.askyblock.command.commands.island;

import java.util.UUID;

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

		Player localPlayer = player;
		UUID localUUID = playerUUID;

		Player invitedPlayer = plugin.getServer().getPlayer(args[1]);
		if (invitedPlayer == null || !player.canSee(invitedPlayer)) {
			Util.sendMessage(localPlayer,
					ChatColor.RED + plugin.myLocale(localPlayer.getUniqueId()).errorOfflinePlayer);
			return CommandType.DEFAULT;
		}
		final UUID invitedPlayerUUID = invitedPlayer.getUniqueId();
		// Player issuing the command must have an island
		if (!plugin.getPlayers().hasIsland(localPlayer.getUniqueId())) {
			Util.sendMessage(localPlayer,
					ChatColor.RED + plugin.myLocale(localPlayer.getUniqueId()).inviteerrorYouMustHaveIslandToInvite);
			return CommandType.DEFAULT;
		}
		// Player cannot invite themselves
		if (localPlayer.getUniqueId().equals(invitedPlayer.getUniqueId())) {
			Util.sendMessage(localPlayer,
					ChatColor.RED + plugin.myLocale(localPlayer.getUniqueId()).inviteerrorYouCannotInviteYourself);
			return CommandType.DEFAULT;
		}
		// Check if this player can be invited to this island, or
		// whether they are still on cooldown
		long time = plugin.getPlayers().getInviteCoolDownTime(invitedPlayerUUID,
				plugin.getPlayers().getIslandLocation(localUUID));
		if (time > 0 && !player.isOp()) {
			Util.sendMessage(localPlayer, ChatColor.RED + plugin.myLocale(localPlayer.getUniqueId()).inviteerrorCoolDown
					.replace("[time]", String.valueOf(time)));
			return CommandType.DEFAULT;
		}
		// If the player already has a team then check that they are
		// the leader, etc
		if (plugin.getPlayers().inTeam(localPlayer.getUniqueId())) {
			// Leader?
			if (teamLeader.equals(localPlayer.getUniqueId())) {
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
						if (perform.inviteList.containsValue(localUUID)) {
							perform.inviteList.remove(perform.inviteList.getKeyByValue(localPlayer.getUniqueId()));
							Util.sendMessage(localPlayer,
									ChatColor.YELLOW + plugin.myLocale(localPlayer.getUniqueId()).inviteremovingInvite);
						}
						perform.inviteList.put(invitedPlayerUUID, localPlayer.getUniqueId());
						Util.sendMessage(localPlayer,
								"§f» §7Vous venez d'envoyer d'une invitation à §2" + invitedPlayer.getName() + "§7.");
						// Send message to online player
						Util.sendMessage(Bukkit.getPlayer(invitedPlayerUUID),
								"§f» §2" + player.getName() + "§7 vous a invité à rejoindre son île !");
						Util.sendMessage(Bukkit.getPlayer(invitedPlayerUUID),
								"§f» §7Faite §2/is accept/reject §7pour accepter ou refuser l'invitation.");
						if (plugin.getPlayers().hasIsland(invitedPlayerUUID)) {
							Util.sendMessage(Bukkit.getPlayer(invitedPlayerUUID),
									"§f» §cAttention: Votre île sera supprimée si vous acceptez !");
						}
						// Start timeout on invite
						if (Settings.inviteTimeout > 0) {
							plugin.getServer().getScheduler().runTaskLater(plugin, new Runnable() {

								@Override
								public void run() {
									if (perform.inviteList.containsKey(invitedPlayerUUID)
											&& perform.inviteList.get(invitedPlayerUUID).equals(localUUID)) {
										perform.inviteList.remove(invitedPlayerUUID);
										if (plugin.getServer().getPlayer(localUUID) != null) {
											Util.sendMessage(plugin.getServer().getPlayer(localUUID),
													"§f» §7Suppresion de votre ancienne invitation.");
										}
										if (plugin.getServer().getPlayer(invitedPlayerUUID) != null) {
											Util.sendMessage(plugin.getServer().getPlayer(invitedPlayerUUID),
													"§f» §7Suppresion de votre ancienne invitation.");
										}
									}

								}
							}, Settings.inviteTimeout);
						}
					} else {
						Util.sendMessage(localPlayer, "§f» §cVous n'avez plus de place sur votre île !");
					}
				} else {
					Util.sendMessage(localPlayer, "§f» §cLe joueur est déjà sur votre île !");
				}
			} else {
				Util.sendMessage(localPlayer, "§f» §cVous devez avoir une île pour effectuer cette commande !");
			}
		} else {
			// First-time invite player does not have a team
			// Check if invitee is in a team or not
			if (!plugin.getPlayers().inTeam(invitedPlayerUUID)) {
				// If the inviter already has an invite out, remove
				// it
				if (perform.inviteList.containsValue(localUUID)) {
					perform.inviteList.remove(perform.inviteList.getKeyByValue(localPlayer.getUniqueId()));
					Util.sendMessage(localPlayer,
							ChatColor.YELLOW + plugin.myLocale(localPlayer.getUniqueId()).inviteremovingInvite);
				}
				// Place the player and invitee on the invite list
				perform.inviteList.put(invitedPlayerUUID, localPlayer.getUniqueId());
				Util.sendMessage(localPlayer,
						"§f» §7Vous venez d'envoyer d'une invitation à §2" + invitedPlayer.getName() + "§7.");
				// Send message to online player
				Util.sendMessage(Bukkit.getPlayer(invitedPlayerUUID),
						"§f» §2" + player.getName() + "§7 vous a invité à rejoindre son île !");
				Util.sendMessage(Bukkit.getPlayer(invitedPlayerUUID),
						"§f» §7Faite §2/is accept/reject §7pour accepter ou refuser l'invitation.");
				if (plugin.getPlayers().hasIsland(invitedPlayerUUID)) {
					Util.sendMessage(Bukkit.getPlayer(invitedPlayerUUID),
							"§f» §cAttention: Votre île sera supprimée si vous acceptez !");
				}
				// Start timeout on invite
				if (Settings.inviteTimeout > 0) {
					plugin.getServer().getScheduler().runTaskLater(plugin, new Runnable() {

						@Override
						public void run() {

							if (perform.inviteList.containsKey(invitedPlayerUUID)
									&& perform.inviteList.get(invitedPlayerUUID).equals(localUUID)) {
								perform.inviteList.remove(invitedPlayerUUID);
								if (plugin.getServer().getPlayer(localUUID) != null) {
									Util.sendMessage(plugin.getServer().getPlayer(localUUID),
											"§f» §7Suppresion de votre ancienne invitation.");
								}
								if (plugin.getServer().getPlayer(invitedPlayerUUID) != null) {
									Util.sendMessage(plugin.getServer().getPlayer(invitedPlayerUUID),
											"§f» §7Suppresion de votre ancienne invitation.");
								}
							}

						}
					}, Settings.inviteTimeout);
				}
			} else {
				Util.sendMessage(localPlayer, "§f» §cLe joueur est déjà sur votre île !");
			}
		}
		return CommandType.SUCCESS;
	}

}
