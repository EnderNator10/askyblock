package com.wasteofplastic.askyblock.command.commands.island;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import com.wasteofplastic.askyblock.ASkyBlock;
import com.wasteofplastic.askyblock.Settings;
import com.wasteofplastic.askyblock.command.CommandType;
import com.wasteofplastic.askyblock.command.commands.IslandCommandBase;
import com.wasteofplastic.askyblock.util.Util;

public class IslandCommandReject extends IslandCommandBase {

	public IslandCommandReject() {
		this.addSubCommand("reject");
		this.setSyntaxe("/is reject");
		this.setDescription("Refuser une invitation");
		this.setPermission(Settings.PERMPREFIX + "team.reject");
	}

	@Override
	public CommandType postPerform(ASkyBlock plugin) {

		if (perform.inviteList.containsKey(player.getUniqueId())) {
			Util.sendMessage(player, ChatColor.YELLOW + plugin.myLocale(playerUUID).rejectyouHaveRejectedInvitation);
			// If the player is online still then tell them directly
			// about the rejection
			if (Bukkit.getPlayer(perform.inviteList.get(player.getUniqueId())) != null) {
				Util.sendMessage(Bukkit.getPlayer(perform.inviteList.get(playerUUID)),
						ChatColor.RED + plugin.myLocale(perform.inviteList.get(playerUUID)).rejectnameHasRejectedInvite
								.replace("[name]", player.getName()));
			}
			// Remove this player from the global invite list
			perform.inviteList.remove(playerUUID);
		} else {
			// Someone typed /island reject and had not been invited
			Util.sendMessage(player, ChatColor.RED + plugin.myLocale(playerUUID).rejectyouHaveNotBeenInvited);
		}

		return CommandType.SUCCESS;
	}

}
