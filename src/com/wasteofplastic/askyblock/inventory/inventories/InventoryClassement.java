package com.wasteofplastic.askyblock.inventory.inventories;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.wasteofplastic.askyblock.ASkyBlock;
import com.wasteofplastic.askyblock.Island;
import com.wasteofplastic.askyblock.inventory.ItemButton;
import com.wasteofplastic.askyblock.inventory.VInventory;
import com.wasteofplastic.askyblock.zcore.ItemBuilder;
import com.wasteofplastic.askyblock.zcore.Pagination;

public class InventoryClassement extends VInventory {

	@Override
	public boolean openInventory(ASkyBlock main, Player player, int page, Object... args) throws Exception {

		if (page <= 0) {
			page = 1;
			setPage(1);
		}

		List<Island> islands = main.getGrid().getSortIsland();

		createInventory("Classements §e" + page + "§7/§6" + getMaxPage(islands));

		Pagination<Island> pagination = new Pagination<>();
		AtomicInteger slot = new AtomicInteger();
		AtomicInteger rank = new AtomicInteger(((page - 1) * 45) + 1);

		pagination.paginateReverse(islands, 45, page).forEach(is -> {

			int position = rank.getAndIncrement();

			ItemStack item = new ItemStack(getMaterial(position));

			ItemMeta itemMeta = item.getItemMeta();

			itemMeta.setDisplayName(getNameByInteger(is, position));

			itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);

			List<String> lore = new ArrayList<>();

			lore.add("");
			lore.add("§f» §7Niveau de l'île§8: §2" + is.getLevel());
			lore.add("§f» §7Place§8: §2" + position);
			lore.add("");
			lore.add("§f» §7Membres§8:");
			is.getMembers().forEach(member -> lore.add("  §7- §f" + Bukkit.getOfflinePlayer(member).getName()));
			lore.add("");
			lore.add("§f» §7Clique §cgauche§7 pour avois les blocs de l'île");
			lore.add("§f» §7Clique §edroit§7 pour se téléporter au warp de l'île");

			itemMeta.setLore(lore);

			item.setItemMeta(itemMeta);

			addItem(slot.getAndIncrement(), new ItemButton(item).setLeftClick(event -> {
				main.getInventoryManager().createInventory(7, player, 1, is, getPage());
			}).setRightClick(
					event -> player.performCommand("is warp " + Bukkit.getOfflinePlayer(is.getOwner()).getName())));
		});

		if (getPage() != 1)
			addItem(48, new ItemButton(ItemBuilder.getCreatedItem(Material.ARROW, 1, "§f» §7Page précédente"))
					.setClick(event -> createInventory(5, player, getPage() - 1, args)));
		if (getPage() != getMaxPage(islands))
			addItem(50, new ItemButton(ItemBuilder.getCreatedItem(Material.ARROW, 1, "§f» §7Page suivante"))
					.setClick(event -> createInventory(5, player, getPage() + 1, args)));

		return true;
	}

	private String getNameByInteger(Island is, int position) {
		String name = "";
		switch (position) {
		case 1:
			name = "§7[§41§7]";
			break;
		case 2:
			name = "§7[§c2§7]";
			break;
		case 3:
			name = "§7[§63§7]";
			break;
		case 4:
		case 5:
		case 6:
		case 7:
		case 8:
		case 9:
			name = "§7[§e" + position + "§7]";
			break;

		default:
			name = "§7[#§7" + position + "§7]";
			break;
		}

		name += " §e§n" + Bukkit.getOfflinePlayer(is.getOwner()).getName();

		return name;
	}

	public Material getMaterial(int position) {
		return position == 1 ? Material.DIAMOND_AXE
				: position == 2 ? Material.GOLD_AXE : position == 3 ? Material.IRON_AXE : Material.WOOD_AXE;
	}

	private int getMaxPage(List<Island> items) {
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
		return new InventoryClassement();
	}

}
