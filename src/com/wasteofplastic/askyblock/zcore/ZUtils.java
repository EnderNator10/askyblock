package com.wasteofplastic.askyblock.zcore;

import java.util.Timer;
import java.util.TimerTask;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.wasteofplastic.askyblock.ASkyBlock;
import com.wasteofplastic.askyblock.util.VaultHelper;

import net.milkbowl.vault.economy.Economy;

public abstract class ZUtils {

	public void message(CommandSender player, Message message) {
		player.sendMessage(Message.toMessage(message));
	}

	public void message(CommandSender player, Message message, Object... args) {
		player.sendMessage(Message.toMessage(message, args));
	}

	public void message(CommandSender player, String message) {
		player.sendMessage(Message.PREFIX.getMessage() + " " + message);
	}

	public void actionMessage(Player player, Message message, Object... args) {
		player.sendActionBar(String.format(message.getMessage(), args));
	}

	public void actionMessage(Player player, Message message) {
		player.sendActionBar(message.getMessage());
	}

	public void message(Player player, int defaultDelay, int delay, Sound sound, Runnable runnable,
			Message... messages) {
		new Timer().scheduleAtFixedRate(new TimerTask() {
			private int index = 0;

			@Override
			public void run() {
				if (!ASkyBlock.getPlugin().isEnabled() || !player.isOnline()) {
					cancel();
					return;
				}

				if (index >= messages.length) {
					cancel();
					if (runnable != null)
						Bukkit.getScheduler().runTask(ASkyBlock.getPlugin(), () -> runnable.run());
					return;
				}

				if (sound != null)
					player.playSound(player.getLocation(), sound, 1.0f, 1.0f);
				Message message = messages[index];
				message(player, message);
				index++;

			}
		}, defaultDelay, delay);
	}

	private transient Economy economy = VaultHelper.econ;

	/**
	 * Player bank
	 * 
	 * @param player
	 * @return player bank
	 */
	protected double getBalance(Player player) {
		if (economy == null)
			economy = VaultHelper.econ;
		return economy.getBalance(player);
	}

	/**
	 * Player bank
	 * 
	 * @param player
	 * @return player bank
	 */
	@SuppressWarnings("deprecation")
	protected double getBalance(String player) {
		if (economy == null)
			economy = VaultHelper.econ;
		return economy.getBalance(player);
	}

	/**
	 * Player has money
	 * 
	 * @param player
	 * @param int
	 *            value
	 * @return player has value in his bank
	 */
	protected boolean hasMoney(Player player, int value) {
		return hasMoney(player, (double) value);
	}

	/**
	 * Player has money
	 * 
	 * @param player
	 * @param float
	 *            value
	 * @return player has value in his bank
	 */
	protected boolean hasMoney(Player player, float value) {
		return hasMoney(player, (double) value);
	}

	/**
	 * Player has money
	 * 
	 * @param player
	 * @param long
	 *            value
	 * @return player has value in his bank
	 */
	protected boolean hasMoney(Player player, long value) {
		return hasMoney(player, (double) value);
	}

	/**
	 * Player has money
	 * 
	 * @param player
	 * @param double
	 *            value
	 * @return player has value in his bank
	 */
	protected boolean hasMoney(Player player, double value) {
		return getBalance(player) >= value;
	}

	/**
	 * Deposit player money
	 * 
	 * @param player
	 * @param double
	 *            value
	 */
	protected void depositMoney(Player player, double value) {
		economy.depositPlayer(player, value);
	}

	/**
	 * Deposit player money
	 * 
	 * @param player
	 * @param long
	 *            value
	 */
	protected void depositMoney(Player player, long value) {
		upEconomy();
		economy.depositPlayer(player, (double) value);
	}

	/**
	 * Deposit player money
	 * 
	 * @param player
	 * @param int
	 *            value
	 */
	protected void depositMoney(Player player, int value) {
		upEconomy();
		economy.depositPlayer(player, (double) value);
	}

	/**
	 * Deposit player money
	 * 
	 * @param player
	 * @param double
	 *            value
	 */
	@SuppressWarnings("deprecation")
	protected void depositMoney(String player, double value) {
		upEconomy();
		economy.depositPlayer(player, value);
	}

	/**
	 * Deposit player money
	 * 
	 * @param player
	 * @param float
	 *            value
	 */
	protected void depositMoney(Player player, float value) {
		economy.depositPlayer(player, (double) value);
	}

	/**
	 * With draw player money
	 * 
	 * @param player
	 * @param double
	 *            value
	 */
	protected void withdrawMoney(Player player, double value) {
		economy.withdrawPlayer(player, value);
	}

	/**
	 * With draw player money
	 * 
	 * @param player
	 * @param double
	 *            value
	 */
	@SuppressWarnings("deprecation")
	protected void withdrawMoney(String player, double value) {
		economy.withdrawPlayer(player, value);
	}

	/**
	 * With draw player money
	 * 
	 * @param player
	 * @param long
	 *            value
	 */
	protected void withdrawMoney(Player player, long value) {
		economy.withdrawPlayer(player, (double) value);
	}

	/**
	 * With draw player money
	 * 
	 * @param player
	 * @param int
	 *            value
	 */
	protected void withdrawMoney(Player player, int value) {
		economy.withdrawPlayer(player, (double) value);
	}

	/**
	 * With draw player money
	 * 
	 * @param player
	 * @param float
	 *            value
	 */
	protected void withdrawMoney(Player player, float value) {
		economy.withdrawPlayer(player, (double) value);
	}

	/**
	 * 
	 * @return {@link Economy}
	 */
	protected Economy getEconomy() {
		return economy;
	}

	public void upEconomy() {
		if (economy == null)
			economy = VaultHelper.econ;
	}

	public String betterName(ItemStack item) {
		String tmpName = item.getType().name().toLowerCase().replace("_", " ");
		return tmpName.substring(0, 1).toUpperCase() + tmpName.substring(1);
	}

	public boolean hasItem(Player player, ItemStack item) {
		for (ItemStack itemc : player.getInventory().getContents())
			if (itemc != null && itemc.isSimilar(item) && itemc.getAmount() >= item.getAmount())
				return true;
		return false;
	}

	public boolean hasItem(ItemStack[] items, ItemStack item) {
		for (int a = 0; a != items.length; a++) {
			ItemStack currentItem = items[a];
			if (currentItem != null && currentItem.isSimilar(item) && currentItem.getAmount() >= item.getAmount()) {
				items[a] = null;
				return true;
			}
		}
		return false;
	}

	/**
	 * Remove somes items in player inventory
	 * 
	 * @param player
	 * @param item
	 *            number
	 * @param material
	 *            who need to remove
	 * @param data
	 *            of item
	 */
	public void removeItem(Player player, int item, Material material) {
		removeItem(player, item, material, 0);
	}

	/**
	 * Remove somes items in player inventory
	 * 
	 * @param player
	 * @param item
	 *            number
	 * @param material
	 *            who need to remove
	 * @param data
	 *            of item
	 */
	@SuppressWarnings("deprecation")
	public void removeItem(Player player, int item, Material material, int data) {
		for (ItemStack contents1 : player.getInventory().getContents()) {
			if (contents1 != null && contents1.getType().equals(material) && data == contents1.getData().getData()) {

				if (item <= 0)
					continue;

				int currentAmount = contents1.getAmount();
				if (currentAmount > item) {
					int newAmount = currentAmount - item;
					item = 0;
					contents1.setAmount(newAmount);
				} else {

					item -= contents1.getAmount();
					contents1.setAmount(0);

				}

			}
		}
	}

	/**
	 * @param player
	 * @return true if the player's inventory is full
	 */
	protected boolean hasInventoryFull(Player player) {
		int slot = 0;
		ItemStack[] arrayOfItemStack;
		int x = (arrayOfItemStack = player.getInventory().getContents()).length - 5;
		for (int i = 0; i < x; i++) {
			ItemStack contents = arrayOfItemStack[i];
			if ((contents == null)) {
				slot++;
			}
		}
		return slot == 0;
	}

	protected boolean give(ItemStack item, Player player) {
		if (hasInventoryFull(player))
			return false;
		player.getInventory().addItem(item);
		return true;
	}

	/**
	 * Gives an item to the player, if the player's inventory is full then the
	 * item will drop to the ground
	 * 
	 * @param player
	 * @param item
	 */
	protected void give(Player player, ItemStack item) {
		if (hasInventoryFull(player))
			player.getWorld().dropItem(player.getLocation(), item);
		else
			player.getInventory().addItem(item);
	}

	/**
	 * @param location
	 *            as String
	 * @return string as location
	 */
	protected Location changeStringLocationToLocation(String s) {
		String[] a = s.split(",");
		World w = Bukkit.getServer().getWorld(a[0]);
		float x = Float.parseFloat(a[1]);
		float y = Float.parseFloat(a[2]);
		float z = Float.parseFloat(a[3]);
		return new Location(w, x, y, z);
	}

	/**
	 * @param location
	 *            as string
	 * @return string as locaiton
	 */
	protected Location changeStringLocationToLocationEye(String s) {
		String[] a = s.split(",");
		World w = Bukkit.getServer().getWorld(a[0]);
		float x = Float.parseFloat(a[1]);
		float y = Float.parseFloat(a[2]);
		float z = Float.parseFloat(a[3]);
		float yaw = Float.parseFloat(a[3]);
		float pitch = Float.parseFloat(a[3]);
		return new Location(w, x, y, z, yaw, pitch);
	}

	/**
	 * @param location
	 * @return location as string
	 */
	protected String changeLocationToString(Location location) {
		String ret = location.getWorld().getName() + "," + location.getBlockX() + "," + location.getBlockY() + ","
				+ location.getBlockZ();
		return ret;
	}

	/**
	 * @param location
	 * @return location as String
	 */
	protected String changeLocationToStringEye(Location location) {
		String ret = location.getWorld().getName() + "," + location.getBlockX() + "," + location.getBlockY() + ","
				+ location.getBlockZ() + "," + location.getYaw() + "," + location.getPitch();
		return ret;
	}

	/**
	 * @param chunk
	 * @return string as Chunk
	 */
	protected Chunk changeStringChuncToChunk(String chunk) {
		String[] a = chunk.split(",");
		World w = Bukkit.getServer().getWorld(a[0]);
		return w.getChunkAt(Integer.valueOf(a[1]), Integer.valueOf(a[2]));
	}

	/**
	 * @param chunk
	 * @return chunk as string
	 */
	protected String changeChunkToString(Chunk chunk) {
		String c = chunk.getWorld().getName() + "," + chunk.getX() + "," + chunk.getZ();
		return c;
	}

	/**
	 * Check if the item name is the same as the given string
	 * 
	 * @param stack
	 * @param name
	 * @return true if the item name is the same as string
	 */
	protected boolean same(ItemStack stack, String name) {
		return stack.hasItemMeta() && stack.getItemMeta().hasDisplayName()
				&& stack.getItemMeta().getDisplayName().equals(name);
	}

	/**
	 * Remove the item from the player's hand
	 * 
	 * @param player
	 * @param number
	 *            of items to withdraw
	 */
	@SuppressWarnings("deprecation")
	protected void removeItemInHand(Player player, int how) {
		if (player.getItemInHand().getAmount() > how)
			player.getItemInHand().setAmount(player.getItemInHand().getAmount() - how);
		else
			player.setItemInHand(new ItemStack(Material.AIR));
		player.updateInventory();
	}

}
