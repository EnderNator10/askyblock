package com.wasteofplastic.askyblock.command.commands.island;

import java.util.UUID;

import org.bukkit.ChatColor;

import com.wasteofplastic.askyblock.ASkyBlock;
import com.wasteofplastic.askyblock.Settings;
import com.wasteofplastic.askyblock.command.CommandType;
import com.wasteofplastic.askyblock.command.commands.IslandCommandBase;
import com.wasteofplastic.askyblock.util.Util;

public class IslandCommandTeamChat extends IslandCommandBase {

	public IslandCommandTeamChat() {
		this.addSubCommand("teamchat");
		this.addSubCommand("tc");
		this.setSyntaxe("/is teamchat");
		this.setDescription("Activer/Désactiver le chat d'île");
		this.setPermission(Settings.PERMPREFIX + "team.chat");
	}

	@Override
	public CommandType postPerform(ASkyBlock plugin) {

		if (!Settings.teamChat) {
			Util.sendMessage(player, "§f» " +ChatColor.RED + plugin.myLocale().errorUnknownCommand);
			return CommandType.DEFAULT;
		}
		if (plugin.getPlayers().inTeam(playerUUID)) {
			boolean online = false;
			for (UUID teamMember : plugin.getPlayers().getMembers(playerUUID)) {
				if (!teamMember.equals(playerUUID) && plugin.getServer().getPlayer(teamMember) != null) {
					online = true;
				}
			}
			if (!online) {
				Util.sendMessage(player, "§f» " +ChatColor.RED + plugin.myLocale(playerUUID).teamChatNoTeamAround);
				Util.sendMessage(player, ChatColor.GREEN + plugin.myLocale(playerUUID).teamChatStatusOff);
				plugin.getChatListener().unSetPlayer(playerUUID);
				return CommandType.DEFAULT;
			}
			if (plugin.getChatListener().isTeamChat(playerUUID)) {
				// Toggle
				Util.sendMessage(player, "§f» " +ChatColor.GREEN + plugin.myLocale(playerUUID).teamChatStatusOff);
				plugin.getChatListener().unSetPlayer(playerUUID);
			} else {
				Util.sendMessage(player, "§f» " +ChatColor.GREEN + plugin.myLocale(playerUUID).teamChatStatusOn);
				plugin.getChatListener().setPlayer(playerUUID);
			}
		} else {
			Util.sendMessage(player, "§f» " +ChatColor.RED + plugin.myLocale(playerUUID).teamChatNoTeam);
		}

		return CommandType.SUCCESS;
	}

}
