package com.wasteofplastic.askyblock.inventory.inventories;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;

import com.wasteofplastic.askyblock.ASkyBlock;
import com.wasteofplastic.askyblock.Settings;
import com.wasteofplastic.askyblock.inventory.ItemButton;
import com.wasteofplastic.askyblock.inventory.VInventory;
import com.wasteofplastic.askyblock.zcore.BlockValue;
import com.wasteofplastic.askyblock.zcore.ItemBuilder;
import com.wasteofplastic.askyblock.zcore.Pagination;

public class InventoryValue extends VInventory {

	@Override
	public boolean openInventory(ASkyBlock main, Player player, int page, Object... args) throws Exception {

		if (page <= 0)
			page = 1;

		createInventory("Valeur des blocs §e" + page + "§7/§6" + getMaxPage(Settings.values));

		Pagination<BlockValue> pagination = new Pagination<>();
		AtomicInteger slot = new AtomicInteger();

		pagination.paginateReverse(Settings.values, 45, page)
				.forEach(item -> {
					
					addItem(slot.getAndIncrement(), item.toItemStack());
				});

		if (getPage() != 1)
			addItem(48, new ItemButton(ItemBuilder.getCreatedItem(Material.ARROW, 1, "§f» §7Page précédente"))
					.setClick(event -> createInventory(2, player, getPage() - 1, args)));
		if (getPage() != getMaxPage(Settings.values))
			addItem(50, new ItemButton(ItemBuilder.getCreatedItem(Material.ARROW, 1, "§f» §7Page suivante"))
					.setClick(event -> createInventory(2, player, getPage() + 1, args)));

		return true;
	}

	private int getMaxPage(List<BlockValue> items) {
		return (items.size() / 45) + 1;
	}

	@Override
	protected void onClose(InventoryCloseEvent event, ASkyBlock plugin, Player player) {
	}

	@Override
	protected void onDrag(InventoryDragEvent event, ASkyBlock plugin, Player player) {

	}

	@Override
	public VInventory clone() {
		return new InventoryValue();
	}

}
