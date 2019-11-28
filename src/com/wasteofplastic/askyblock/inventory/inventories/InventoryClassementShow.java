package com.wasteofplastic.askyblock.inventory.inventories;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.ItemStack;

import com.wasteofplastic.askyblock.ASkyBlock;
import com.wasteofplastic.askyblock.Island;
import com.wasteofplastic.askyblock.inventory.ItemButton;
import com.wasteofplastic.askyblock.inventory.VInventory;
import com.wasteofplastic.askyblock.zcore.ItemBuilder;

public class InventoryClassementShow extends VInventory {

	@Override
	public boolean openInventory(ASkyBlock main, Player player, int page, Object... args) throws Exception {
		
		Island island = (Island) args[0];
		int oldPage = (int) args[1];
		
		createInventory("§cInformations");
		
		addItem(10, ItemBuilder.getIslandInfo(Material.DIRT, island));
		addItem(11, ItemBuilder.getIslandInfo(Material.GRASS, island));
		
		addItem(15, ItemBuilder.getIslandInfo(Material.STONE, island));
		addItem(16, ItemBuilder.getIslandInfo(Material.COBBLESTONE, island));
		
		addItem(4, ItemBuilder.getIslandInfo(Material.EMERALD_BLOCK, island));
		addItem(13, ItemBuilder.getIslandInfo(Material.DIAMOND_BLOCK, island));
		addItem(22, ItemBuilder.getIslandInfo(Material.IRON_BLOCK, island));
		addItem(31, ItemBuilder.getIslandInfo(Material.GOLD_BLOCK, island));
		addItem(40, ItemBuilder.getIslandInfo(Material.REDSTONE_BLOCK, island));
		
		addItem(28, ItemBuilder.getIslandInfo(Material.ENDER_STONE, island));
		addItem(29, ItemBuilder.getIslandInfo(Material.PACKED_ICE, island));
		
		addItem(33, ItemBuilder.getIslandInfo(Material.MOB_SPAWNER, island));
		addItem(34, ItemBuilder.getIslandInfo(Material.CHEST, island));
		
		addItem(47, ItemBuilder.getIslandInfo(Material.SAND, island));
		addItem(51, ItemBuilder.getIslandInfo(Material.COAL_BLOCK, island));
		
		addItem(49, new ItemButton(ItemBuilder.getCreatedItem(Material.NETHER_STAR, 1, "§cRetour")).setClick(event -> {
			main.getInventoryManager().createInventory(5, player, oldPage);
		}));
		
		return true;
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
		return new InventoryClassementShow();
	}

}
