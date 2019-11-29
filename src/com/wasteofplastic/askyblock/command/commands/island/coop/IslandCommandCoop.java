package com.wasteofplastic.askyblock.command.commands.island.coop;

import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.wasteofplastic.askyblock.ASkyBlock;
import com.wasteofplastic.askyblock.CoopPlay;
import com.wasteofplastic.askyblock.Settings;
import com.wasteofplastic.askyblock.command.CommandType;
import com.wasteofplastic.askyblock.command.commands.IslandCommandBase;
import com.wasteofplastic.askyblock.util.Util;

public class IslandCommandCoop extends IslandCommandBase {

	public IslandCommandCoop() {
		this.addSubCommand("coop");
		this.setSyntaxe("/is coop <joueur>");
		this.setDescription("Coop un joueur");
		this.setPermission(Settings.PERMPREFIX + "coop");
		this.setArgsLength(2);
	}

	@Override
	public CommandType postPerform(ASkyBlock plugin) {
		// Only online players can be cooped
		Player target = plugin.getServer().getPlayer(args[1]);
		if (target == null || !player.canSee(target)) {
			Util.sendMessage(player, ChatColor.RED + plugin.myLocale(player.getUniqueId()).errorOfflinePlayer);
			return CommandType.SUCCESS;
		}
		final UUID targetPlayerUUID = target.getUniqueId();
		// Player issuing the command must have an island
		if (!plugin.getPlayers().hasIsland(playerUUID) && !plugin.getPlayers().inTeam(playerUUID)) {
			Util.sendMessage(player,
					ChatColor.RED + plugin.myLocale(player.getUniqueId()).inviteerrorYouMustHaveIslandToInvite);
			return CommandType.SUCCESS;
		}
		// Player cannot invite themselves
		if (playerUUID.equals(targetPlayerUUID)) {
			Util.sendMessage(player,
					ChatColor.RED + plugin.myLocale(player.getUniqueId()).inviteerrorYouCannotInviteYourself);
			return CommandType.SUCCESS;
		}
		// If target player is already on the team ignore
		if (plugin.getPlayers().getMembers(playerUUID).contains(targetPlayerUUID)) {
			Util.sendMessage(player, ChatColor.RED + plugin.myLocale(player.getUniqueId()).coopOnYourTeam);
			return CommandType.SUCCESS;
		}
		// Target has to have an island
		if (!plugin.getPlayers().inTeam(targetPlayerUUID)) {
			if (!plugin.getPlayers().hasIsland(targetPlayerUUID)) {
				Util.sendMessage(player,
						ChatColor.RED + plugin.myLocale(player.getUniqueId()).errorNoIslandOther);
				return CommandType.SUCCESS;
			}
		}
		// Check if this player can be invited to this island, or
		// whether they are still on cooldown
		long time = plugin.getPlayers().getInviteCoolDownTime(targetPlayerUUID,
				plugin.getPlayers().getIslandLocation(playerUUID));
		if (time > 0 && !player.isOp()) {
			Util.sendMessage(player, ChatColor.RED + plugin.myLocale(player.getUniqueId()).inviteerrorCoolDown
					.replace("[time]", String.valueOf(time)));
			return CommandType.SUCCESS;
		}

		if (Settings.coopIsRequest) {
			// Send out coop invite
			Util.sendMessage(player, ChatColor.GREEN + plugin.myLocale(player.getUniqueId()).inviteinviteSentTo
					.replace("[name]", target.getName()));
			Util.sendMessage(target, ChatColor.GREEN
					+ plugin.myLocale(targetPlayerUUID).coopHasInvited.replace("[name]", player.getName()));
			Util.sendMessage(target, ChatColor.WHITE + "/" + label + " [coopaccept/coopreject] "
					+ ChatColor.YELLOW + plugin.myLocale(targetPlayerUUID).invitetoAcceptOrReject);
			perform.coopInviteList.put(targetPlayerUUID, playerUUID);
			if (Settings.inviteTimeout > 0) {
				plugin.getServer().getScheduler().runTaskLater(plugin, new Runnable() {

					@Override
					public void run() {

						if (perform.coopInviteList.containsKey(targetPlayerUUID)
								&& perform.coopInviteList.get(targetPlayerUUID).equals(playerUUID)) {
							perform.coopInviteList.remove(targetPlayerUUID);
							if (plugin.getServer().getPlayer(playerUUID) != null) {
								Util.sendMessage(plugin.getServer().getPlayer(playerUUID), ChatColor.YELLOW
										+ plugin.myLocale(player.getUniqueId()).inviteremovingInvite);
							}
							if (plugin.getServer().getPlayer(targetPlayerUUID) != null) {
								Util.sendMessage(plugin.getServer().getPlayer(targetPlayerUUID),
										ChatColor.YELLOW
												+ plugin.myLocale(player.getUniqueId()).inviteremovingInvite);
							}
						}

					}
				}, Settings.inviteTimeout);
			}
		} else {
			// Add target to coop list
			if (CoopPlay.getInstance().addCoopPlayer(player, target)) {
				// Tell everyone what happened
				Util.sendMessage(player, ChatColor.GREEN + plugin.myLocale(player.getUniqueId()).coopSuccess
						.replace("[name]", target.getName()));
				Util.sendMessage(target, ChatColor.GREEN + plugin.myLocale(targetPlayerUUID).coopMadeYouCoop
						.replace("[name]", player.getName()));
				// TODO: Give perms if the player is on the coop island
			} // else fail silently
		}
		return CommandType.SUCCESS;
	}

}
