package com.wasteofplastic.askyblock.command.commands.island;

import org.bukkit.ChatColor;
import org.bukkit.Sound;

import com.wasteofplastic.askyblock.ASkyBlock;
import com.wasteofplastic.askyblock.Settings;
import com.wasteofplastic.askyblock.command.CommandType;
import com.wasteofplastic.askyblock.command.commands.IslandCommandBase;
import com.wasteofplastic.askyblock.util.Util;
import com.wasteofplastic.askyblock.zcore.Message;

public class IslandCommand extends IslandCommandBase {

	public IslandCommand() {
		this.addSubCommand("is");
		this.setSyntaxe("/is");
		this.setDescription("Permet d'accéder au menu de votre île");
	}

	@Override
	public CommandType postPerform(ASkyBlock main) {

		if (main.getPlayers().getIslandLocation(playerUUID) == null && !main.getPlayers().inTeam(playerUUID)) {
			// Check if the max number of islands is made already
			if (Settings.maxIslands > 0 && main.getGrid().getIslandCount() > Settings.maxIslands) {
				Util.sendMessage(player, ChatColor.RED + main.myLocale(player.getUniqueId()).errorMaxIslands);
				return CommandType.DEFAULT;
			}
			// Check if player has resets left
			if (main.getPlayers().getResetsLeft(playerUUID) == 0) {
				message(sender, Message.ISLAND_RESET_ERROR);
				return CommandType.DEFAULT;
			}
			// Create new island for player
			message(player, 0, 1500, Sound.BLOCK_NOTE_PLING, () -> perform.chooseIsland(player), Message.ISLAND_CREATE,
					Message.ISLAND_CREATE_1, Message.ISLAND_CREATE_2, Message.ISLAND_CREATE_3, Message.ISLAND_CREATE_4,
					Message.ISLAND_CREATE_5);
		} else {

			main.getInventoryManager().createInventory(1, player, 0);

		}
		return CommandType.SUCCESS;
	}

}
