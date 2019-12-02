package com.wasteofplastic.askyblock.command.commands.island.coop;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.wasteofplastic.askyblock.ASkyBlock;
import com.wasteofplastic.askyblock.CoopPlay;
import com.wasteofplastic.askyblock.command.CommandType;
import com.wasteofplastic.askyblock.command.commands.IslandCommandBase;
import com.wasteofplastic.askyblock.util.Util;

public class IslandCommandCoopAccept extends IslandCommandBase {

	public IslandCommandCoopAccept() {
		this.addSubCommand("coopaccept");
		this.setSyntaxe("/is coopaccept");
		this.setDescription("Accepter une invitation dans une île");
	}

	@Override
	public CommandType postPerform(ASkyBlock plugin) {

		if (perform.coopInviteList.containsKey(playerUUID)) {
			// Check if inviter is online
			Player inviter = plugin.getServer().getPlayer(perform.coopInviteList.get(playerUUID));
			if (inviter == null || !inviter.isOnline()) {
				Util.sendMessage(player,
						"§f» " + ChatColor.RED + plugin.myLocale(player.getUniqueId()).errorOfflinePlayer);
				perform.coopInviteList.remove(playerUUID);
				return CommandType.SUCCESS;
			}
			if (CoopPlay.getInstance().addCoopPlayer(inviter, player)) {
				// Tell everyone what happened
				Util.sendMessage(inviter, "§f» " + ChatColor.GREEN + plugin.myLocale(inviter.getUniqueId()).coopSuccess
						.replace("[name]", ChatColor.DARK_GREEN + player.getName() + ChatColor.GREEN));
				Util.sendMessage(player, "§f» " + ChatColor.GREEN + plugin.myLocale(playerUUID).coopMadeYouCoop
						.replace("[name]", ChatColor.DARK_GREEN + inviter.getName() + ChatColor.GREEN));
				// TODO: Give perms if the player is on the coop island
			}
			perform.setResetWaitTime(player);
			// Remove the invite
			perform.coopInviteList.remove(playerUUID);
			return CommandType.SUCCESS;
		}
		Util.sendMessage(player, "§f» " + ChatColor.RED + plugin.myLocale(player.getUniqueId()).errorCommandNotReady);

		return CommandType.SUCCESS;
	}

}
