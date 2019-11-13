package com.wasteofplastic.askyblock.inventory;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.wasteofplastic.askyblock.ASkyBlock;
import com.wasteofplastic.askyblock.zcore.ItemBuilder;
import com.wasteofplastic.askyblock.zcore.ZUtils;

import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;

public abstract class VInventory extends ZUtils {

	private InventoryManager inventoryManager;
	private ASkyBlock plugin;
	private Map<Integer, ItemButton> items = new HashMap<Integer, ItemButton>();
	protected Player player;
	private int page;
	private Object[] args;
	private Inventory inventory;
	private String guiName;
	private boolean disableClick = true;

	protected VInventory createInventory(String name) {
		return createInventory(name, 54);
	}

	public ASkyBlock getPlugin() {
		return plugin;
	}
	
	protected VInventory createInventory(String name, int size) {
		guiName = name;
//		if (name.length() > 32)
//			Logger.getLogger().log("The name of the menu is over 32 characters!", LogType.ERROR);
		this.inventory = Bukkit.createInventory(null, size, name);
		return this;
	}

	public void addItem(int slot, ItemButton item) {
		this.items.put(slot, item);
		this.inventory.setItem(slot, item.getDisplayItem());
	}
	
	public void addItem(int slot, ItemStack item) {
		this.items.put(slot, new ItemButton(item));
		this.inventory.setItem(slot, item);
	}

	public void removeItem(int slot) {
		this.items.remove(slot);
	}

	public void clearItem() {
		this.items.clear();
	}

	/**
	 * @return the items
	 */
	public Map<Integer, ItemButton> getItems() {
		return items;
	}

	public boolean isDisableClick() {
		return disableClick;
	}
	
	public void setDisableClick(boolean disableClick) {
		this.disableClick = disableClick;
	}
	
	/**
	 * @return the player
	 */
	public Player getPlayer() {
		return player;
	}

	/**
	 * @return the page
	 */
	public int getPage() {
		return page;
	}

	/**
	 * @return the args
	 */
	public Object[] getArgs() {
		return args;
	}

	/**
	 * @param items
	 *            the items to set
	 */
	public void setItems(Map<Integer, ItemButton> items) {
		this.items = items;
	}

	/**
	 * @param player
	 *            the player to set
	 */
	public void setPlayer(Player player) {
		this.player = player;
	}

	/**
	 * @param page
	 *            the page to set
	 */
	public void setPage(int page) {
		this.page = page;
	}

	/**
	 * @param args
	 *            the args to set
	 */
	public void setArgs(Object[] args) {
		this.args = args;
	}

	/**
	 * @return the inventory
	 */
	public Inventory getInventory() {
		return inventory;
	}

	/**
	 * @return the guiName
	 */
	public String getGuiName() {
		return guiName;
	}

	public void setPlugin(ASkyBlock plugin) {
		this.plugin = plugin;
	}
	
	public void setInventoryManager(InventoryManager inventoryManager) {
		this.inventoryManager = inventoryManager;
	}
	
	public void createInventory(int id, Player player, int page, Object... args){
		inventoryManager.createInventory(id, player, page, args);
	}
	
	protected ItemStack getProfil(Player player){
		PermissionUser user = PermissionsEx.getUser(player);
		DecimalFormat format = new DecimalFormat("##.#");
		double money = (double) getBalance(player);
		return ItemBuilder.getCreatedSkullPlayerWithLoreAndName(player.getName(), "§eInformations",
				Arrays.asList("", "§f» §7Pseudo§7: §f" + player.getName(),
						"§f» §7Grade§7: §7" + user.getPrefix().replace("&", "§").replace("[", "").replace("]", ""),
						"§f» §7Money§7: §e" + format.format(money) + "$","","§f» §7Obtient un grade avec le §b/shop§7.",
						""));
	}
	
	public abstract boolean openInventory(ASkyBlock main, Player player, int page, Object... args) throws Exception;

	protected abstract void onClose(InventoryCloseEvent event, ASkyBlock plugin, Player player);

	protected abstract void onDrag(InventoryDragEvent event, ASkyBlock plugin, Player player);
	
	public abstract VInventory clone();
}
