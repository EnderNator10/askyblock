package com.wasteofplastic.askyblock.command.commands.island;

import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;

import com.wasteofplastic.askyblock.ASkyBlock;
import com.wasteofplastic.askyblock.GridManager;
import com.wasteofplastic.askyblock.Island;
import com.wasteofplastic.askyblock.Island.SettingsFlag;
import com.wasteofplastic.askyblock.Settings;
import com.wasteofplastic.askyblock.command.CommandType;
import com.wasteofplastic.askyblock.command.commands.IslandCommandBase;
import com.wasteofplastic.askyblock.events.IslandPreTeleportEvent;
import com.wasteofplastic.askyblock.util.Util;
import com.wasteofplastic.askyblock.util.VaultHelper;

public class IslandCommandWarp extends IslandCommandBase {

	public IslandCommandWarp() {
		this.addSubCommand("warp");
		this.setSyntaxe("/is warp <name>");
		this.setDescription("Se téléporter à un warp");
		this.setIgnoreArgs(true);
		this.setPermission(Settings.PERMPREFIX + "island.warp");
		this.setArgsLength(2);
	}

	@Override
	public CommandType postPerform(ASkyBlock plugin) {
		final Set<UUID> warpList = plugin.getWarpSignsListener().listWarps();
		if (warpList.isEmpty()) {
			Util.sendMessage(player,
					ChatColor.YELLOW + plugin.myLocale(player.getUniqueId()).warpserrorNoWarpsYet);
			if (VaultHelper.checkPerm(player, Settings.PERMPREFIX + "island.addwarp")) {
				Util.sendMessage(player, ChatColor.YELLOW + plugin.myLocale().warpswarpTip);
			} else {
				Util.sendMessage(player,
						ChatColor.RED + plugin.myLocale(player.getUniqueId()).errorNoPermission);
			}
			return CommandType.SUCCESS;
		} else {
			// Check if this is part of a name
			UUID foundWarp = null;
			for (UUID warp : warpList) {
				if (warp == null)
					continue;
				if (plugin.getPlayers().getName(warp) != null) {
					if (plugin.getPlayers().getName(warp).toLowerCase().equals(args[1].toLowerCase())) {
						foundWarp = warp;
						break;
					} else if (plugin.getPlayers().getName(warp).toLowerCase()
							.startsWith(args[1].toLowerCase())) {
						foundWarp = warp;
					}
				}
			}
			if (foundWarp == null) {
				Util.sendMessage(player,
						ChatColor.RED + plugin.myLocale(player.getUniqueId()).warpserrorDoesNotExist);
				return CommandType.SUCCESS;
			} else {
				// Warp exists!
				final Location warpSpot = plugin.getWarpSignsListener().getWarp(foundWarp);
				// Check if the warp spot is safe
				if (warpSpot == null) {
					Util.sendMessage(player,
							ChatColor.RED + plugin.myLocale(player.getUniqueId()).warpserrorNotReadyYet);
					plugin.getLogger()
							.warning("Null warp found, owned by " + plugin.getPlayers().getName(foundWarp));
					return CommandType.SUCCESS;
				}
				// Find out if island is locked
				Island island = plugin.getGrid().getIslandAt(warpSpot);
				// Check bans
				if (island != null && plugin.getPlayers().isBanned(island.getOwner(), playerUUID)) {
					Util.sendMessage(player, ChatColor.RED + plugin.myLocale(playerUUID).banBanned
							.replace("[name]", plugin.getPlayers().getName(island.getOwner())));
					if (!VaultHelper.checkPerm(player, Settings.PERMPREFIX + "mod.bypassprotect")
							&& !VaultHelper.checkPerm(player, Settings.PERMPREFIX + "mod.bypasslock")) {
						return CommandType.SUCCESS;
					}
				}
				if (island != null && island.isLocked() && !player.isOp()
						&& !VaultHelper.checkPerm(player, Settings.PERMPREFIX + "mod.bypasslock")
						&& !VaultHelper.checkPerm(player, Settings.PERMPREFIX + "mod.bypassprotect")) {
					// Always inform that the island is locked
					Util.sendMessage(player,
							ChatColor.RED + plugin.myLocale(player.getUniqueId()).lockIslandLocked);
					// Check if this is the owner, team member or
					// coop
					if (!plugin.getGrid().locationIsAtHome(player, true, warpSpot)) {
						// plugin.getLogger().info("DEBUG: not at
						// home");
						return CommandType.SUCCESS;
					}
				}
				boolean pvp = false;
				if (island != null && ((warpSpot.getWorld().equals(ASkyBlock.getIslandWorld())
						&& island.getIgsFlag(SettingsFlag.PVP))
						|| (warpSpot.getWorld().equals(ASkyBlock.getNetherWorld())
								&& island.getIgsFlag(SettingsFlag.NETHER_PVP)))) {
					pvp = true;
				}
				// Find out which direction the warp is facing
				Block b = warpSpot.getBlock();
				if (b.getType().equals(Material.SIGN_POST) || b.getType().equals(Material.WALL_SIGN)) {
					Sign sign = (Sign) b.getState();
					org.bukkit.material.Sign s = (org.bukkit.material.Sign) sign.getData();
					BlockFace directionFacing = s.getFacing();
					Location inFront = b.getRelative(directionFacing).getLocation();
					Location oneDown = b.getRelative(directionFacing).getRelative(BlockFace.DOWN)
							.getLocation();
					if ((GridManager.isSafeLocation(inFront))) {
						warpPlayer(player, inFront, foundWarp, directionFacing, pvp);
						return CommandType.SUCCESS;
					} else if (b.getType().equals(Material.WALL_SIGN)
							&& GridManager.isSafeLocation(oneDown)) {
						// Try one block down if this is a wall sign
						warpPlayer(player, oneDown, foundWarp, directionFacing, pvp);
						return CommandType.SUCCESS;
					}
				} else {
					// Warp has been removed
					Util.sendMessage(player,
							ChatColor.RED + plugin.myLocale(player.getUniqueId()).warpserrorDoesNotExist);
					plugin.getWarpSignsListener().removeWarp(warpSpot);
					return CommandType.SUCCESS;
				}
				if (!(GridManager.isSafeLocation(warpSpot))) {
					Util.sendMessage(player,
							ChatColor.RED + plugin.myLocale(player.getUniqueId()).warpserrorNotSafe);
					// WALL_SIGN's will always be unsafe if the
					// place in front is obscured.
					if (b.getType().equals(Material.SIGN_POST)) {
						plugin.getLogger().warning("Unsafe warp found at " + warpSpot.toString()
								+ " owned by " + plugin.getPlayers().getName(foundWarp));

					}
					return CommandType.SUCCESS;
				} else {
					final Location actualWarp = new Location(warpSpot.getWorld(),
							warpSpot.getBlockX() + 0.5D, warpSpot.getBlockY(), warpSpot.getBlockZ() + 0.5D);
					player.teleport(actualWarp);
					if (pvp) {
						Util.sendMessage(player,
								ChatColor.BOLD + "" + ChatColor.RED
										+ plugin.myLocale(player.getUniqueId()).igs.get(SettingsFlag.PVP)
										+ " " + plugin.myLocale(player.getUniqueId()).igsAllowed);
						if (plugin.getServer().getVersion().contains("(MC: 1.8")
								|| plugin.getServer().getVersion().contains("(MC: 1.7")) {
							player.getWorld().playSound(player.getLocation(), Sound.valueOf("ARROW_HIT"),
									1F, 1F);
						} else {
							player.getWorld().playSound(player.getLocation(), Sound.ENTITY_ARROW_HIT, 1F,
									1F);
						}
					} else {
						if (plugin.getServer().getVersion().contains("(MC: 1.8")
								|| plugin.getServer().getVersion().contains("(MC: 1.7")) {
							player.getWorld().playSound(player.getLocation(), Sound.valueOf("BAT_TAKEOFF"),
									1F, 1F);
						} else {
							player.getWorld().playSound(player.getLocation(), Sound.ENTITY_BAT_TAKEOFF, 1F,
									1F);
						}
					}
					return CommandType.SUCCESS;
				}
			}
		}
	}

	/**
	 * Warps a player to a spot in front of a sign
	 * 
	 * @param player
	 * @param inFront
	 * @param foundWarp
	 * @param directionFacing
	 */
	private void warpPlayer(Player player, Location inFront, UUID foundWarp, BlockFace directionFacing, boolean pvp) {
		// convert blockface to angle
		float yaw = Util.blockFaceToFloat(directionFacing);
		final Location actualWarp = new Location(inFront.getWorld(), inFront.getBlockX() + 0.5D, inFront.getBlockY(),
				inFront.getBlockZ() + 0.5D, yaw, 30F);
		IslandPreTeleportEvent event = new IslandPreTeleportEvent(player, IslandPreTeleportEvent.Type.WARP, actualWarp);
		Bukkit.getPluginManager().callEvent(event);
		if (!event.isCancelled()) {
			player.teleport(event.getLocation());
			if (pvp) {
				Util.sendMessage(player,
						ChatColor.BOLD + "" + ChatColor.RED
								+ plugin.myLocale(player.getUniqueId()).igs.get(SettingsFlag.PVP) + " "
								+ plugin.myLocale(player.getUniqueId()).igsAllowed);
				if (plugin.getServer().getVersion().contains("(MC: 1.8")
						|| plugin.getServer().getVersion().contains("(MC: 1.7")) {
					player.getWorld().playSound(player.getLocation(), Sound.valueOf("ARROW_HIT"), 1F, 1F);
				} else {
					player.getWorld().playSound(player.getLocation(), Sound.ENTITY_ARROW_HIT, 1F, 1F);
				}
			} else {
				if (plugin.getServer().getVersion().contains("(MC: 1.8")
						|| plugin.getServer().getVersion().contains("(MC: 1.7")) {
					player.getWorld().playSound(player.getLocation(), Sound.valueOf("BAT_TAKEOFF"), 1F, 1F);
				} else {
					player.getWorld().playSound(player.getLocation(), Sound.ENTITY_BAT_TAKEOFF, 1F, 1F);
				}
			}
			Player warpOwner = plugin.getServer().getPlayer(foundWarp);
			if (warpOwner != null && !warpOwner.equals(player)) {
				Util.sendMessage(warpOwner,
						plugin.myLocale(foundWarp).warpsPlayerWarped.replace("[name]", player.getName()));
			}
		}
	}
	
}
