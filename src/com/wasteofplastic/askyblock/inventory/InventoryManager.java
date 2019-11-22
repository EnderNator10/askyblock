package com.wasteofplastic.askyblock.inventory;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;

import com.wasteofplastic.askyblock.ASkyBlock;
import com.wasteofplastic.askyblock.inventory.inventories.InventoryChallengeShow;
import com.wasteofplastic.askyblock.inventory.inventories.InventoryChallenges;
import com.wasteofplastic.askyblock.inventory.inventories.InventoryChooseIsland;
import com.wasteofplastic.askyblock.inventory.inventories.InventoryClassement;
import com.wasteofplastic.askyblock.inventory.inventories.InventoryIsland;
import com.wasteofplastic.askyblock.inventory.inventories.InventoryValue;
import com.wasteofplastic.askyblock.listener.ListenerAdapter;
import com.wasteofplastic.askyblock.zcore.Logger;
import com.wasteofplastic.askyblock.zcore.Logger.LogType;

public class InventoryManager extends ListenerAdapter {

	private final ASkyBlock plugin;
	private Map<Integer, VInventory> inventories = new HashMap<>();
	private Map<Player, VInventory> playerInventories = new HashMap<>();

	public InventoryManager(ASkyBlock plugin) {
		this.plugin = plugin;

		addInventory(1, new InventoryIsland());
		addInventory(2, new InventoryValue());
		addInventory(3, new InventoryChallenges());
		addInventory(4, new InventoryChallengeShow());
		addInventory(5, new InventoryClassement());
		addInventory(6, new InventoryChooseIsland());
		
		plugin.getLog().log("Loading " + inventories.size() + " inventories", LogType.SUCCESS);
	}

	private void addInventory(int id, VInventory inventory) {
		if (!inventories.containsKey(id))
			inventories.put(id, inventory);
	}

	public void createInventory(int id, Player player, int page, Object... objects) {
		VInventory inventory = getInventory(id);
		if (inventory == null)
			return;
		VInventory clonedInventory = inventory.clone();

		if (clonedInventory == null) {
			player.sendMessage("§cLe clone de l'inventaire est null !");
			return;
		}

		clonedInventory.setPlayer(player);
		clonedInventory.setArgs(objects);
		clonedInventory.setPage(page);
		clonedInventory.setPlugin(plugin);
		clonedInventory.setInventoryManager(this);
		try {
			if (clonedInventory.openInventory(plugin, player, page, objects)) {
				player.openInventory(clonedInventory.getInventory());
				playerInventories.put(player, clonedInventory);
			}
		} catch (Exception e) {
			player.sendMessage(" §cAn internal error occurred while opening the inventory with the id " + id);
			e.printStackTrace();
		}
	}

	public void createInventory(VInventory parent, Player player) {
		VInventory clonedInventory = parent.clone();

		if (clonedInventory == null) {
			player.sendMessage("§cLe clone de l'inventaire est null !");
			return;
		}

		clonedInventory.setPlayer(player);
		clonedInventory.setArgs(parent.getArgs());
		clonedInventory.setPage(parent.getPage());
		clonedInventory.setPlugin(plugin);
		clonedInventory.setInventoryManager(this);
		try {
			if (clonedInventory.openInventory(plugin, player, parent.getPage(), parent.getArgs())) {
				player.openInventory(clonedInventory.getInventory());
				playerInventories.put(player, clonedInventory);
			}
		} catch (Exception e) {
			player.sendMessage(" §cAn internal error occurred while opening the inventory with the id "
					+ parent.toString());
			e.printStackTrace();
		}
	}

	@Override
	protected void onInventoryClick(InventoryClickEvent event, Player player) {
		if (event.getClickedInventory() == null)
			return;
		if (event.getWhoClicked() instanceof Player) {
			if (!exist(player))
				return;
			VInventory gui = playerInventories.get(player);
			if (gui.getGuiName() == null || gui.getGuiName().length() == 0) {
				Logger.info("An error has occurred with the menu ! " + gui.getClass().getName());
				return;
			}
			if (event.getView() != null && gui.getPlayer().equals(player)
					&& event.getView().getTitle().equals(gui.getGuiName())) {
				if (gui.isDisableClick())
					event.setCancelled(true);
				ItemButton button = gui.getItems().getOrDefault(event.getSlot(), null);
				if (button != null)
					button.onClick(event);
			}
		}
	}

	@Override
	protected void onInventoryClose(InventoryCloseEvent event, Player player) {
		if (!exist(player))
			return;
		VInventory inventory = playerInventories.get(player);
		remove(player);
		inventory.onClose(event, plugin, player);
	}

	@Override
	protected void onInventoryDrag(InventoryDragEvent event, Player player) {
		if (event.getWhoClicked() instanceof Player) {
			if (!exist(player))
				return;
			playerInventories.get(player).onDrag(event, plugin, player);
		}
	}

	public void update(Player player) {
		if (!exist(player))
			return;
		VInventory inventory = playerInventories.get(player);
		createInventory(inventory, player);
	}

	public boolean exist(Player player) {
		return playerInventories.containsKey(player);
	}

	public void remove(Player player) {
		if (playerInventories.containsKey(player))
			playerInventories.remove(player);
	}

	private VInventory getInventory(int id) {
		return inventories.getOrDefault(id, null);
	}

}
