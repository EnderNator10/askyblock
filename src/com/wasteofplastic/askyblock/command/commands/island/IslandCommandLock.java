package com.wasteofplastic.askyblock.command.commands.island;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.wasteofplastic.askyblock.ASkyBlock;
import com.wasteofplastic.askyblock.CoopPlay;
import com.wasteofplastic.askyblock.Island;
import com.wasteofplastic.askyblock.Settings;
import com.wasteofplastic.askyblock.command.CommandType;
import com.wasteofplastic.askyblock.command.commands.IslandCommandBase;
import com.wasteofplastic.askyblock.util.Util;
import com.wasteofplastic.askyblock.util.VaultHelper;

public class IslandCommandLock extends IslandCommandBase {

	public IslandCommandLock() {
		this.addSubCommand("lock");
		this.setSyntaxe("/is lock");
		this.setDescription("Fermer/Ouvrir son île");
		this.setPermission(Settings.PERMPREFIX + "island.lock");
	}

	@Override
	public CommandType postPerform(ASkyBlock plugin) {

		// Find out which island they want to lock
		Island island = plugin.getGrid().getIsland(playerUUID);
		if (island == null) {
			// plugin.getLogger().info("DEBUG: player has no island in
			// grid");
			// Player has no island in the grid
			Util.sendMessage(player, ChatColor.RED + plugin.myLocale(player.getUniqueId()).errorNoIsland);
			return CommandType.SUCCESS;
		} else {
			if (!island.isLocked()) {
				// Remove any visitors
				for (Player target : plugin.getServer().getOnlinePlayers()) {
					if (target == null || target.hasMetadata("NPC") || target.isOp() || player.equals(target)
							|| VaultHelper.checkPerm(target, Settings.PERMPREFIX + "mod.bypassprotect"))
						continue;
					// See if target is on this player's island and not
					// a coop player
					if (plugin.getGrid().isOnIsland(player, target) && !CoopPlay.getInstance()
							.getCoopPlayers(island.getCenter()).contains(target.getUniqueId())) {
						// Send them home
						if (plugin.getPlayers().inTeam(target.getUniqueId())
								|| plugin.getPlayers().hasIsland(target.getUniqueId())) {
							plugin.getGrid().homeTeleport(target);
						} else {
							// Just move target to spawn
							if (!target.performCommand(Settings.SPAWNCOMMAND)) {
								target.teleport(player.getWorld().getSpawnLocation());
							}
						}
						Util.sendMessage(target, ChatColor.RED + plugin.myLocale(target.getUniqueId()).expelExpelled);
						plugin.getLogger().info(player.getName() + " expelled " + target.getName()
								+ " from their island when locking.");
						// Yes they are
						Util.sendMessage(player, ChatColor.GREEN + plugin.myLocale(player.getUniqueId()).expelSuccess
								.replace("[name]", target.getName()));
					}
				}
				Util.sendMessage(player, ChatColor.GREEN + plugin.myLocale(playerUUID).lockLocking);
				plugin.getMessages().tellOfflineTeam(playerUUID,
						plugin.myLocale(playerUUID).lockPlayerLocked.replace("[name]", player.getName()));
				plugin.getMessages().tellTeam(playerUUID,
						plugin.myLocale(playerUUID).lockPlayerLocked.replace("[name]", player.getName()));
				island.setLocked(true);
			} else {
				Util.sendMessage(player, ChatColor.GREEN + plugin.myLocale(playerUUID).lockUnlocking);
				plugin.getMessages().tellOfflineTeam(playerUUID,
						plugin.myLocale(playerUUID).lockPlayerUnlocked.replace("[name]", player.getName()));
				plugin.getMessages().tellTeam(playerUUID,
						plugin.myLocale(playerUUID).lockPlayerUnlocked.replace("[name]", player.getName()));
				island.setLocked(false);
			}
			return CommandType.SUCCESS;
		}
	}

}
