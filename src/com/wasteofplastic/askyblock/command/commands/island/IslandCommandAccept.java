package com.wasteofplastic.askyblock.command.commands.island;

import org.bukkit.ChatColor;

import com.wasteofplastic.askyblock.ASkyBlock;
import com.wasteofplastic.askyblock.Settings;
import com.wasteofplastic.askyblock.command.CommandType;
import com.wasteofplastic.askyblock.command.commands.IslandCommandBase;
import com.wasteofplastic.askyblock.util.Util;

public class IslandCommandAccept extends IslandCommandBase {

	public IslandCommandAccept() {
		this.addSubCommand("accept");
		this.setSyntaxe("/is accept");
		this.setDescription("Rejoindre une île");
		this.setPermission(Settings.PERMPREFIX + "team.join");
		this.setIgnoreArgs(true);
	}

	@Override
	public CommandType postPerform(ASkyBlock plugin) {

		if (!plugin.getPlayers().inTeam(playerUUID) && perform.inviteList.containsKey(playerUUID)) {
			// If the invitee has an island of their own
			if (plugin.getPlayers().hasIsland(playerUUID)) {
				plugin.getLogger().info(player.getName() + "'s island will be deleted because they joined a party.");
				plugin.getPlayers().setInTeleport(playerUUID, true);
				plugin.deletePlayerIsland(playerUUID, true);
				plugin.getLogger().info("Island deleted.");
			}
			// Add the player to the team
			perform.addPlayertoTeam(playerUUID, perform.inviteList.get(playerUUID));
			// If the leader who did the invite does not yet have a
			// team (leader is not in a team yet)
			if (!plugin.getPlayers().inTeam(perform.inviteList.get(playerUUID))) {
				// Add the leader to their own team
				perform.addPlayertoTeam(perform.inviteList.get(playerUUID), perform.inviteList.get(playerUUID));
			}
			perform.setResetWaitTime(player);
			if (Settings.teamJoinDeathReset) {
				plugin.getPlayers().setDeaths(player.getUniqueId(), 0);
			}
			plugin.getGrid().homeTeleport(player);
			plugin.resetPlayer(player);
			if (!player.hasPermission(Settings.PERMPREFIX + "command.newteamexempt")) {
				// plugin.getLogger().info("DEBUG: Executing new
				// island commands");
				perform.runCommands(Settings.teamStartCommands, player);
			}
			Util.sendMessage(player,
					"§f» §7Vous venez de rejoindre l'île de §2"
							+ plugin.getServer().getPlayer(perform.inviteList.get(playerUUID)).getName()
							+ " §7! Utilisez §2/island team§7 pour avoir la liste des membres de votre nouvelle île !");
			if (plugin.getServer().getPlayer(perform.inviteList.get(playerUUID)) != null) {
				Util.sendMessage(plugin.getServer().getPlayer(perform.inviteList.get(playerUUID)),
						"§f» §2" + player.getName() +" §7vient de rejoindre votre île !");
			} 
			// Remove the invite
			perform.inviteList.remove(player.getUniqueId());
			plugin.getGrid().saveGrid();
			return CommandType.SUCCESS;
		}
		Util.sendMessage(player, ChatColor.RED + plugin.myLocale(player.getUniqueId()).errorCommandNotReady);

		return CommandType.SUCCESS;
	}

}
