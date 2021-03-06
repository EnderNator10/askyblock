package com.wasteofplastic.askyblock.command.commands.island.mics;

import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;
import org.bukkit.permissions.PermissionAttachmentInfo;

import com.wasteofplastic.askyblock.ASkyBlock;
import com.wasteofplastic.askyblock.Settings;
import com.wasteofplastic.askyblock.command.CommandType;
import com.wasteofplastic.askyblock.command.commands.IslandCommandBase;
import com.wasteofplastic.askyblock.util.Util;

public class IslandCommandValue extends IslandCommandBase {

	public IslandCommandValue() {
		this.addSubCommand("value");
		this.setSyntaxe("/is value");
		this.setDescription("Affiche la valeur du bloc dans votre main");
		this.setPermission(Settings.PERMPREFIX + "island.value");
		this.setIgnoreArgs(true);
	}

	@Override
	public CommandType postPerform(ASkyBlock plugin) {

		if (!plugin.getGrid().playerIsOnIsland(player)) {
			Util.sendMessage(player, "�f� " +ChatColor.RED + plugin.myLocale(player.getUniqueId()).errorNotOnIsland);
			return CommandType.SUCCESS;
		}
		@SuppressWarnings("deprecation")
		ItemStack item = player.getItemInHand();
		if (args.length > 1) {
			// Match Material for parsed input.
			Material material = Material.matchMaterial(args[1]);
			if (material != null) {
				item = new ItemStack(material);
			}
		}
		double multiplier = 1;
		if (item != null && item.getType().isBlock()) {
			// Get permission multiplier
			for (PermissionAttachmentInfo perms : player.getEffectivePermissions()) {
				if (perms.getPermission().startsWith(Settings.PERMPREFIX + "island.multiplier.")) {
					String[] spl = perms.getPermission().split(Settings.PERMPREFIX + "island.multiplier.");
					// Get the max value should there be more than one
					if (spl.length > 1) {
						if (!NumberUtils.isDigits(spl[1])) {
							plugin.getLogger().severe("Player " + player.getName() + " has permission: "
									+ perms.getPermission() + " <-- the last part MUST be a number! Ignoring...");

						} else {
							multiplier = Math.max(multiplier, Integer.valueOf(spl[1]));
						}
					}
				}
				// Do some sanity checking
				if (multiplier < 1) {
					multiplier = 1;
				}
			}
			// Player height
			if (player.getLocation().getBlockY() < Settings.seaHeight) {
				multiplier *= Settings.underWaterMultiplier;
			}
			// Get the value. Try the specific item
			int value = 0;
			if (Settings.blockValues.containsKey(item.getData())) {
				value = (int) ((double) Settings.blockValues.get(item.getData()) * multiplier);
			} else if (Settings.blockValues.containsKey(new MaterialData(item.getType()))) {
				value = (int) ((double) Settings.blockValues.get(new MaterialData(item.getType())) * multiplier);
			}

			String tmpName = item.getType().name().toLowerCase().replace("_", " ");
			String name = tmpName.substring(0, 1).toUpperCase() + tmpName.substring(1);

			if (value > 0) {
				Util.sendMessage(player, "�f� �7Le bloc �2" + name + " �7rapporte �2�l" + value + " �7point"
						+ (value > 1 ? "s" : "") + ".");
			} else {
				Util.sendMessage(player, "�f� �7Le block �2" + name + " �7ne rapporte pas de point.");
			}
		} else {
			Util.sendMessage(player, "�f� �7L'item dans votre main n'est pas un bloc.");
		}

		return CommandType.SUCCESS;
	}

}
