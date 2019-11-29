package com.wasteofplastic.askyblock.command.commands.island.mics;

import java.util.Calendar;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.wasteofplastic.askyblock.ASkyBlock;
import com.wasteofplastic.askyblock.LevelCalcByChunk;
import com.wasteofplastic.askyblock.Settings;
import com.wasteofplastic.askyblock.command.CommandType;
import com.wasteofplastic.askyblock.command.commands.IslandCommandBase;
import com.wasteofplastic.askyblock.util.Util;
import com.wasteofplastic.askyblock.util.VaultHelper;

public class IslandCommandLevel extends IslandCommandBase {

	private HashMap<UUID, Long> levelWaitTime = new HashMap<>();

	public IslandCommandLevel() {
		this.addSubCommand("level");
		this.setSyntaxe("/is level");
		this.setDescription("Calculer le niveau de votre île");
		this.setPermission(Settings.PERMPREFIX + "intopten");
	}

	@Override
	public CommandType postPerform(ASkyBlock plugin) {
		if (!plugin.getPlayers().inTeam(playerUUID) && !plugin.getPlayers().hasIsland(playerUUID)) {
			Util.sendMessage(player, ChatColor.RED + plugin.myLocale(player.getUniqueId()).errorNoIsland);
			return CommandType.DEFAULT;
		} else 
			calculateIslandLevel(player, playerUUID);
		return CommandType.SUCCESS;
	}

	/**
	 * Calculates the island level
	 *
	 * @param sender
	 *            - Player object of player who is asking
	 * @param targetPlayer
	 *            - UUID of the player's island that is being requested
	 * @return - true if successful.
	 */
	public boolean calculateIslandLevel(final CommandSender sender, final UUID targetPlayer) {
		return calculateIslandLevel(sender, targetPlayer, false);
	}

	/**
	 * Calculates the island level
	 * 
	 * @param sender
	 *            - asker of the level info
	 * @param targetPlayer
	 * @param report
	 *            - if true, a detailed report will be provided
	 * @return - false if this is cannot be done
	 */
	public boolean calculateIslandLevel(final CommandSender sender, final UUID targetPlayer, boolean report) {
		if (sender instanceof Player) {
			Player asker = (Player) sender;
			// Player asking for their own island calc
			if (asker.getUniqueId().equals(targetPlayer) || asker.isOp()
					|| VaultHelper.checkPerm(asker, Settings.PERMPREFIX + "mod.info")) {
				// Newer better system - uses chunks
				if (!onLevelWaitTime(asker) || Settings.levelWait <= 0 || asker.isOp()
						|| VaultHelper.checkPerm(asker, Settings.PERMPREFIX + "mod.info")) {
					Util.sendMessage(asker, ChatColor.GREEN + plugin.myLocale(asker.getUniqueId()).levelCalculating);
					setLevelWaitTime(asker);
					new LevelCalcByChunk(plugin, plugin.getGrid().getIsland(targetPlayer), targetPlayer, asker, report);
				} else {
					Util.sendMessage(asker, ChatColor.YELLOW + plugin.myLocale(asker.getUniqueId()).islandresetWait
							.replace("[time]", String.valueOf(getLevelWaitTime(asker))));
				}

			} else {
				// Asking for the level of another player
				Util.sendMessage(asker, ChatColor.GREEN + plugin.myLocale(asker.getUniqueId()).islandislandLevelis
						.replace("[level]", String.valueOf(plugin.getPlayers().getIslandLevel(targetPlayer))));
			}
		} else {
			// Console request
			Util.sendMessage(sender, ChatColor.GREEN + plugin.myLocale().levelCalculating);
			new LevelCalcByChunk(plugin, plugin.getGrid().getIsland(targetPlayer), targetPlayer, sender, report);
		}
		return true;
	}

	public boolean onLevelWaitTime(final Player player) {
		if (levelWaitTime.containsKey(player.getUniqueId())) {
			if (levelWaitTime.get(player.getUniqueId()).longValue() > Calendar.getInstance().getTimeInMillis()) {
				return true;
			}

			return false;
		}

		return false;
	}

	private void setLevelWaitTime(final Player player) {
		levelWaitTime.put(player.getUniqueId(),
				Long.valueOf(Calendar.getInstance().getTimeInMillis() + Settings.levelWait * 1000));
	}

	private long getLevelWaitTime(final Player player) {
		if (levelWaitTime.containsKey(player.getUniqueId())) {
			if (levelWaitTime.get(player.getUniqueId()).longValue() > Calendar.getInstance().getTimeInMillis()) {
				return (levelWaitTime.get(player.getUniqueId()).longValue() - Calendar.getInstance().getTimeInMillis())
						/ 1000;
			}
			return 0L;
		}
		return 0L;
	}

}
