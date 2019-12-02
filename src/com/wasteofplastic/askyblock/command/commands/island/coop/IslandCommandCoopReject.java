package com.wasteofplastic.askyblock.command.commands.island.coop;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import com.wasteofplastic.askyblock.ASkyBlock;
import com.wasteofplastic.askyblock.command.CommandType;
import com.wasteofplastic.askyblock.command.commands.IslandCommandBase;
import com.wasteofplastic.askyblock.util.Util;

public class IslandCommandCoopReject extends IslandCommandBase {

	public IslandCommandCoopReject() {
		this.addSubCommand("coopreject");
		this.setSyntaxe("/is coopreject");
		this.setDescription("Refuser une invitation dans une île");
	}

	@Override
	public CommandType postPerform(ASkyBlock plugin) {

		if (perform.coopInviteList.containsKey(playerUUID)) {
			Util.sendMessage(player,
					"§f» " + ChatColor.GRAY + plugin.myLocale(playerUUID).rejectyouHaveRejectedInvitation);
			// If the player is online still then tell them directly
			// about the rejection
			if (Bukkit.getPlayer(perform.inviteList.get(playerUUID)) != null) {
				Util.sendMessage(Bukkit.getPlayer(perform.inviteList.get(playerUUID)),
						"§f» " + ChatColor.RED
								+ plugin.myLocale(perform.inviteList.get(playerUUID)).rejectnameHasRejectedInvite
										.replace("[name]", player.getName()));
			}
			// Remove this player from the global invite list
			perform.coopInviteList.remove(playerUUID);
		} else {
			// Someone typed /island coopreject and had not been invited
			Util.sendMessage(player, "§f» " + ChatColor.RED + plugin.myLocale(playerUUID).rejectyouHaveNotBeenInvited);
		}

		return CommandType.SUCCESS;
	}

}
