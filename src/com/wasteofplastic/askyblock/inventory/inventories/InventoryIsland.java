package com.wasteofplastic.askyblock.inventory.inventories;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.wasteofplastic.askyblock.ASkyBlock;
import com.wasteofplastic.askyblock.Island;
import com.wasteofplastic.askyblock.inventory.ItemButton;
import com.wasteofplastic.askyblock.inventory.VInventory;
import com.wasteofplastic.askyblock.zcore.ItemBuilder;
import com.wasteofplastic.askyblock.zcore.LimitHelper;

public class InventoryIsland extends VInventory {

	@Override
	public boolean openInventory(ASkyBlock main, Player player, int page, Object... args) throws Exception {

		createInventory("Mon île", 45);

		Island island = main.getGrid().getIslandAt(main.getPlayers().getIslandLocation(player.getUniqueId()));

		addItem(20, new ItemButton(
				ItemBuilder.getCreatedItem("§bMon île", Material.GRASS, 1, "", "§f» §7Clique accéder à ton île", ""))
						.setClick(event -> {
							main.getGrid().homeTeleport(player);
						}));

		addItem(22, getInfoBook(island, main));

		addItem(24, new ItemButton(
				ItemBuilder.getCreatedItem("§bOptions", Material.COMPASS, 1, "", "§f» §7Clique accéder aux options", ""))
						.setClick(event -> {
							player.performCommand("is settings");
						}));
		
		addItem(4, getProfil(player));

		return true;
	}

	private ItemStack getInfoBook(Island island, ASkyBlock main) {
		ItemStack item = new ItemStack(Material.BOOK);
		ItemMeta itemMeta = item.getItemMeta();
		itemMeta.setDisplayName("§bInformations sur votre île");

		List<String> lore = new ArrayList<String>();

		lore.add("");
		lore.add("§f» §7Niveau de l'île: §a" + main.getPlayers().getIslandLevel(player.getUniqueId()));
		lore.add("§f» §7Niveau du générateur: §a" + island.getGeneratorLevel() + "§f/§217");
		lore.add("§f» §7Génère les minerais: " + (island.isGeneratorOre() ? "§2Oui" : "§cNon"));
		lore.add("§f» §7Limite de hopper: §a" + island.getHopperLimit() + "§f/§2"+LimitHelper.HOPPER.getLimit());
		lore.add("§f» §7Nombre d'hopper sur l'île: §a" + island.getHopperCount() + "§f/§2"+island.getHopperLimit());
		lore.add("§f» §7Limite de piston: §a" + island.getPistonLimit() + "§f/§2"+LimitHelper.PISTON.getLimit());
		lore.add("§f» §7Nombre de piston sur l'île: §a" + island.getPiston() + "§f/§2"+island.getPistonLimit());
		lore.add("§f» §7Biome: §a" + island.getBiome().name());
		lore.add("§f» §7Taille de l'île: §a" + island.getProtectionSize() + "§f/§2350");
		lore.add("§f» §7Chef de l'île: §a" + Bukkit.getOfflinePlayer(island.getOwner()).getName());
		lore.add("");
		
		itemMeta.setLore(lore);
		item.setItemMeta(itemMeta);
		return item;
	}

	@Override
	protected void onClose(InventoryCloseEvent event, ASkyBlock plugin, Player player) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onDrag(InventoryDragEvent event, ASkyBlock plugin, Player player) {
		// TODO Auto-generated method stub

	}

	@Override
	public VInventory clone() {
		return new InventoryIsland();
	}

}
