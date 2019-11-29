package com.wasteofplastic.askyblock.command.commands.island;

import org.bukkit.ChatColor;
import org.bukkit.World.Environment;
import org.bukkit.inventory.Inventory;

import com.wasteofplastic.askyblock.ASkyBlock;
import com.wasteofplastic.askyblock.Settings;
import com.wasteofplastic.askyblock.command.CommandType;
import com.wasteofplastic.askyblock.command.commands.IslandCommandBase;
import com.wasteofplastic.askyblock.util.Util;

public class IslandCommandBiome extends IslandCommandBase {

	public IslandCommandBiome() {
		this.addSubCommand("biome");
		this.setSyntaxe("/is biome");
		this.setDescription("Changer le biome de votre île");
		this.setPermission(Settings.PERMPREFIX + "island.biomes");
	}

	@Override
	public CommandType postPerform(ASkyBlock plugin) {

		// Only the team leader can do this
		if (teamLeader != null && !teamLeader.equals(playerUUID)) {
			Util.sendMessage(player,
					ChatColor.RED + plugin.myLocale(player.getUniqueId()).levelerrornotYourIsland);
			return CommandType.SUCCESS;
		}
		if (!plugin.getPlayers().hasIsland(playerUUID)) {
			// Player has no island
			Util.sendMessage(player, ChatColor.RED + plugin.myLocale(player.getUniqueId()).errorNoIsland);
			return CommandType.SUCCESS;
		}
		if (!plugin.getGrid().playerIsOnIsland(player)) {
			Util.sendMessage(player,
					ChatColor.RED + plugin.myLocale(player.getUniqueId()).challengeserrorNotOnIsland);
			return CommandType.SUCCESS;
		}
		// Not allowed in the nether
		if (plugin.getPlayers().getIslandLocation(playerUUID).getWorld().getEnvironment()
				.equals(Environment.NETHER)) {
			Util.sendMessage(player, ChatColor.RED + plugin.myLocale(player.getUniqueId()).errorWrongWorld);
			return CommandType.SUCCESS;
		}
		// Util.sendMessage(player,
		// plugin.myLocale(player.getUniqueId()).helpColor +
		// "[Biomes]");
		Inventory inv = plugin.getBiomes().getBiomePanel(player);
		if (inv != null) {
			player.openInventory(inv);
		}

		return CommandType.SUCCESS;
	}

}
