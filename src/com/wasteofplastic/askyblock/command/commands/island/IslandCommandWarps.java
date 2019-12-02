package com.wasteofplastic.askyblock.command.commands.island;

import java.util.Collection;
import java.util.UUID;

import org.bukkit.ChatColor;

import com.wasteofplastic.askyblock.ASkyBlock;
import com.wasteofplastic.askyblock.Settings;
import com.wasteofplastic.askyblock.command.CommandType;
import com.wasteofplastic.askyblock.command.commands.IslandCommandBase;
import com.wasteofplastic.askyblock.util.Util;
import com.wasteofplastic.askyblock.util.VaultHelper;

public class IslandCommandWarps extends IslandCommandBase {

	public IslandCommandWarps() {
		this.addSubCommand("warps");
		this.setSyntaxe("/is warps");
		this.setDescription("Voir la liste des warps");
		this.setPermission(Settings.PERMPREFIX + "island.warp");
	}

	@Override
	public CommandType postPerform(ASkyBlock plugin) {

		Collection<UUID> warpList = plugin.getWarpSignsListener().listWarps();
		if (warpList.isEmpty()) {
			Util.sendMessage(player,
					"§f» " + ChatColor.YELLOW + plugin.myLocale(player.getUniqueId()).warpserrorNoWarpsYet);
			if (VaultHelper.checkPerm(player, Settings.PERMPREFIX + "island.addwarp")
					&& plugin.getGrid().playerIsOnIsland(player)) {
				Util.sendMessage(player, "§f» " + ChatColor.YELLOW + plugin.myLocale().warpswarpTip);
			}
		} else {
			if (Settings.useWarpPanel) {
				// Try the warp panel
				player.openInventory(plugin.getWarpPanel().getWarpPanel(0));
			} else {
				Boolean hasWarp = false;
				String wlist = "";
				for (UUID w : warpList) {
					if (w == null)
						continue;
					if (wlist.isEmpty()) {
						wlist = plugin.getPlayers().getName(w);
					} else {
						wlist += ", " + plugin.getPlayers().getName(w);
					}
					if (w.equals(playerUUID)) {
						hasWarp = true;
					}
				}
				Util.sendMessage(player, "§f» " + ChatColor.YELLOW
						+ plugin.myLocale(player.getUniqueId()).warpswarpsAvailable + ": " + ChatColor.WHITE + wlist);
				if (!hasWarp && (VaultHelper.checkPerm(player, Settings.PERMPREFIX + "island.addwarp"))) {
					Util.sendMessage(player, "§f» " + ChatColor.YELLOW + plugin.myLocale().warpswarpTip);
				}
			}
		}

		return CommandType.SUCCESS;
	}

}
