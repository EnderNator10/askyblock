package com.wasteofplastic.askyblock.hopper;

import java.util.Collection;
import java.util.Timer;
import java.util.TimerTask;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.wasteofplastic.askyblock.ASkyBlock;
import com.wasteofplastic.askyblock.zcore.ZUtils;

public class Hopper extends ZUtils {

	private transient Location location;
	private transient boolean canRun = false;
	private final String stringLocation;

	public Hopper(Location location) {
		super();
		this.location = location;
		this.stringLocation = changeLocationToString(location);
	}

	public Location getLocation() {
		if (location == null)
			location = changeStringLocationToLocation(stringLocation);
		return location;
	}

	public String getStringLocation() {
		return stringLocation;
	}

	public void destroy() {
		canRun = false;
	}

	public Inventory getInventory() {
		// Méthode pour vérif que le block est ok

		if (!getLocation().getBlock().getType().equals(Material.HOPPER)) {
			ASkyBlock.getPlugin().getHopper().remove(this);
			return null;
		}

		org.bukkit.block.Hopper hopper = (org.bukkit.block.Hopper) getLocation().getBlock().getState();
		return hopper.getInventory();
	}

	public void run() {

		canRun = true;

		new Timer().scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				if (!canRun || !ASkyBlock.getPlugin().isEnabled()) {
					cancel();
					return;
				}

				Collection<Entity> entities = getLocation().getWorld().getNearbyEntities(getLocation(), 5.0, 5.0, 5.0);
				entities.forEach(entity -> {
					if (entity.getType().equals(EntityType.DROPPED_ITEM)) {
						Item item = (Item) entity;
						Inventory inventory = getInventory();
						if (inventory == null) {
							cancel();
							canRun = false;
							return;
						}
						if (!inventoryFull(inventory) && !item.isDead()) {
							inventory.addItem(item.getItemStack());
							item.remove();
						}
					}
				});

			}
		}, 1000, 1000);
	}

	public boolean inventoryFull(Inventory inventory) {
		for (ItemStack item : inventory.getContents())
			if (item == null || item.getType().equals(Material.AIR))
				return false;
		return true;
	}

}
