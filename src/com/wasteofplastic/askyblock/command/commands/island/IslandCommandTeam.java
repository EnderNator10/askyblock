package com.wasteofplastic.askyblock.command.commands.island;

import java.util.UUID;

import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.ChatColor;
import org.bukkit.permissions.PermissionAttachmentInfo;

import com.wasteofplastic.askyblock.ASkyBlock;
import com.wasteofplastic.askyblock.Settings;
import com.wasteofplastic.askyblock.command.CommandType;
import com.wasteofplastic.askyblock.command.commands.IslandCommandBase;
import com.wasteofplastic.askyblock.util.Util;

public class IslandCommandTeam extends IslandCommandBase {

	public IslandCommandTeam() {
		this.addSubCommand("team");
		this.setSyntaxe("/is team");
	}

	@Override
	public CommandType postPerform(ASkyBlock plugin) {

		if (plugin.getPlayers().inTeam(playerUUID)) {
			if (teamLeader.equals(playerUUID)) {
				int maxSize = Settings.maxTeamSize;
				for (PermissionAttachmentInfo perms : player.getEffectivePermissions()) {
					if (perms.getPermission().startsWith(Settings.PERMPREFIX + "team.maxsize.")) {
						if (perms.getPermission().contains(Settings.PERMPREFIX + "team.maxsize.*")) {
							maxSize = Settings.maxTeamSize;
							break;
						} else {
							// Get the max value should there be more
							// than one
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
					Util.sendMessage(player, "§f» §7Vous pouvez encore inviter §2[number]§7 joueurs."
							.replace("[number]", String.valueOf(maxSize - teamMembers.size())));
				}
			}
			Util.sendMessage(player, "§f» §7Membres de votre île§8:");
			for (UUID m : plugin.getPlayers().getMembers(teamLeader)) {
				Util.sendMessage(player, "  §7- §f" + plugin.getPlayers().getName(m));
			}
		} else if (perform.inviteList.containsKey(playerUUID)) {
			Util.sendMessage(player, "§f» §2" + plugin.getPlayers().getName(perform.inviteList.get(playerUUID))
					+ "§7 vous a invité à rejoindre son île !");
			Util.sendMessage(player, "§f» §7Faite §2/is accept/reject §7pour accepter ou refuser l'invitation.");
		} else {
			Util.sendMessage(player, ChatColor.RED + plugin.myLocale(player.getUniqueId()).kickerrorNoTeam);
		}
		return CommandType.SUCCESS;

	}

}
