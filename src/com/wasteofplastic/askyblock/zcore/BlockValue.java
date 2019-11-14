package com.wasteofplastic.askyblock.zcore;

import java.util.Arrays;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;

public class BlockValue {

	private final MaterialData data;
	private final int point;
	public BlockValue(MaterialData data, int point) {
		super();
		this.data = data;
		this.point = point;
	}
	/**
	 * @return the data
	 */
	public MaterialData getData() {
		return data;
	}
	/**
	 * @return the point
	 */
	public int getPoint() {
		return point;
	}
	
	@SuppressWarnings("deprecation")
	public ItemStack toItemStack(){
		Material material = data.getItemType();
		switch (material) {
		case BREWING_STAND:
			material = Material.BREWING_STAND_ITEM;
			break;
		case CAULDRON:
			material = Material.CAULDRON_ITEM;
			break;
		case SKULL:
			material = Material.SKULL_ITEM;
			break;
		case IRON_DOOR_BLOCK:
			material = Material.IRON_DOOR;
			break;
		case FLOWER_POT:
			material = Material.FLOWER_POT_ITEM;
			break;
		case TRIPWIRE:
			material = Material.TRIPWIRE_HOOK;
			break;
		case DARK_OAK_DOOR:
			material = Material.DARK_OAK_DOOR_ITEM;
			break;
		case BEETROOT_BLOCK:
			material = Material.BEETROOT_SEEDS;
			break;
		case SUGAR_CANE_BLOCK:
			material = Material.SUGAR_CANE;
			break;
		case SIGN_POST:
			material = Material.SIGN;
			break;
		default:
			break;

		}
		return ItemBuilder.getCreatedItemWithLoreAndShort(material, 1, data.getData(), null, Arrays.asList("§f» §7Points: §b" + getPoint()));
	}
	
}
