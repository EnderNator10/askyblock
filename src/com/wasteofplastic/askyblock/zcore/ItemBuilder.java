package com.wasteofplastic.askyblock.zcore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import com.wasteofplastic.askyblock.Island;

public class ItemBuilder {

	// Gestions des objets
	public static ItemStack getCreatedItem(Material Material, int Number, int id) {
		return getCreatedItemAndData(Material, Number, (byte) id, "");
	}

	public static ItemStack getCreatedItem(Material Material, int Number, String... name) {
		return getCreatedItemWithLore(Material, Number, null, name);
	}

	public static ItemStack getCreatedItem(String name, Material Material, int Number, String... lore) {
		return getCreatedItemWithLore(Material, Number, name, lore);
	}

	public static ItemStack getCreatedItem(Material Material, int Number, String Name) {
		ItemStack item = new ItemStack(Material, Number);
		ItemMeta meta = item.getItemMeta();
		item.setAmount(Number);
		meta.setDisplayName(Name);
		item.setItemMeta(meta);
		return item;
	}

	public static ItemStack getCreatedItem(Material Material, int Number, int d, String Name) {
		ItemStack item = new ItemStack(Material, Number, (byte) d);
		ItemMeta meta = item.getItemMeta();
		item.setAmount(Number);
		meta.setDisplayName(Name);
		item.setItemMeta(meta);
		return item;
	}

	public static ItemStack getCreatedItemFlag(Material Material, int Number, String Name) {
		ItemStack item = new ItemStack(Material, Number);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(Name);
		meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		item.setItemMeta(meta);
		return item;
	}

	public static ItemStack getCreatedItemEnchantFlag(Material Material, int Number, String Name) {
		ItemStack item = new ItemStack(Material, Number);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(Name);
		meta.addEnchant(Enchantment.ARROW_DAMAGE, 1, true);
		meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		item.setItemMeta(meta);
		return item;
	}

	public static ItemStack getCreatedItemAndData(Material Material, int Number, byte Data, String Name) {
		ItemStack item = new ItemStack(Material, Number, Data);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(Name);
		item.setItemMeta(meta);
		return item;
	}

	public static ItemStack getCreatedItemWithEnchantement(Material Material, int Number, String Name,
			Enchantment Enchant, int EnchantLevel, boolean Visibility) {
		ItemStack item = new ItemStack(Material, Number);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(Name);
		meta.addEnchant(Enchant, EnchantLevel, Visibility);
		item.setItemMeta(meta);
		return item;
	}

	public static ItemStack getCreatedItemWithLore(Material material, int number, String name, List<String> strings) {
		ItemStack item = new ItemStack(material, number);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(name);
		meta.setLore(strings);
		item.setItemMeta(meta);
		return item;
	}

	public static ItemStack getCreatedItemWithLore(Material material, int number, String name, String... strings) {
		ItemStack item = new ItemStack(material, number);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(name);
		meta.setLore(Arrays.asList(strings));
		item.setItemMeta(meta);
		return item;
	}

	public static ItemStack getCreatedItemFlag(Material material, int number, String name, String... strings) {
		return getCreatedItemFlag(material, number, name, Arrays.asList(strings));
	}

	public static ItemStack getCreatedItemFlag(Material material, int number, String name, List<String> strings) {
		ItemStack item = new ItemStack(material, number);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(name);
		meta.setLore(strings);
		meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		item.setItemMeta(meta);
		return item;
	}

	public static ItemStack getCreatedItemWithLoreAndShort(Material Material, int Number, short data, String Name,
			List<String> Lore) {
		ItemStack item = new ItemStack(Material, Number, data);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(Name);
		meta.setLore(Lore);
		item.setItemMeta(meta);
		return item;
	}

	public static ItemStack skull(String player, String Name, String... lore) {
		return getCreatedSkullPlayerWithLoreAndName(player, Name, Arrays.asList(lore));
	}

	@SuppressWarnings("deprecation")
	public static ItemStack getCreatedSkullPlayerWithLoreAndName(String player, String Name, List<String> Lore) {
		ItemStack item = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
		SkullMeta meta = (SkullMeta) item.getItemMeta();
		meta.setOwner(player);
		meta.setDisplayName(Name);
		meta.setLore(Lore);
		item.setItemMeta(meta);
		return item;
	}

	public static ItemStack getCreatedItemWithEnchantementAndLore(Material Material, int Number, String Name,
			Enchantment Enchant, int EnchantLevel, boolean Visibility, List<String> Lore) {
		ItemStack item = new ItemStack(Material, Number);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(Name);
		meta.addEnchant(Enchant, EnchantLevel, Visibility);
		meta.setLore(Lore);
		item.setItemMeta(meta);
		return item;
	}

	public static ItemStack enchant(Material material, CustomEnchant... customEnchants) {
		return enchant(material, null, null, customEnchants);
	}

	public static ItemStack enchant(Material material, String name, List<String> lore,
			CustomEnchant... customEnchants) {
		ItemStack item = new ItemStack(material);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(name);
		for (CustomEnchant customEnchant : customEnchants)
			meta.addEnchant(customEnchant.getEnchantment(), customEnchant.getLevel(), true);
		if (lore != null)
			meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}

	public static ItemStack getCreatedItemWithLoreTicket(Material material, int number, String name,
			String... strings) {
		ItemStack item = new ItemStack(material, number);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(name);
		meta.addEnchant(Enchantment.ARROW_DAMAGE, 1, true);
		meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		meta.setLore(Arrays.asList(strings));
		item.setItemMeta(meta);
		return item;
	}

	public static ItemStack getItemP4U3(Material material) {
		ItemStack item = new ItemStack(material);
		ItemMeta meta = item.getItemMeta();
		meta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 4, true);
		meta.addEnchant(Enchantment.DURABILITY, 3, true);
		item.setItemMeta(meta);
		return item;
	}

	public static ItemStack getBag() {
		ItemStack item = new ItemStack(Material.STORAGE_MINECART);
		ItemMeta customM = item.getItemMeta();
		customM.addEnchant(Enchantment.DURABILITY, 1, true);
		customM.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		customM.setDisplayName("§6» §eBag §6«");
		ArrayList<String> loreBag = new ArrayList<String>();
		loreBag.add("§7§oListe des gains disponibles:");
		loreBag.add("§7• §8x1 Spawner à Skeleton");
		loreBag.add("§7• §8x1 Spawner à vache");
		loreBag.add("§7• §8x1 Spawner à Zombie");
		loreBag.add("§7• §8x1 Kit Dieu");
		loreBag.add("§7• §8x1 Kit Elite");
		loreBag.add("§7• §8x1 Kit Vip");
		loreBag.add("§7• §8x1 Oeuf de vache");
		loreBag.add("§7• §8x64 Stone");
		loreBag.add("§7• §8x64 Grass");
		loreBag.add("§7• §8x64 Dirt");
		loreBag.add("§7• §8x32 Fer");
		loreBag.add("§7• §8x32 Or");
		loreBag.add("§7• §8x8 Diamant");
		loreBag.add("§7• §8x32 Steak");
		loreBag.add("§7• §8x1 Bloc d'Emeraude");
		customM.setLore(loreBag);
		item.setItemMeta(customM);
		return item;
	}

	public static ItemStack createBook(Enchantment enchantment, int level) {
		ItemStack item = new ItemStack(Material.ENCHANTED_BOOK, 1);
		EnchantmentStorageMeta enchantmentStorageMeta = (EnchantmentStorageMeta) item.getItemMeta();
		enchantmentStorageMeta.addStoredEnchant(enchantment, level, true);
		item.setItemMeta(enchantmentStorageMeta);
		return item;
	}

	public static ItemStack getHammer() {
		ItemStack hammer = new ItemStack(Material.DIAMOND_PICKAXE, 1);
		ItemMeta hammerM = hammer.getItemMeta();
		hammerM.setDisplayName("a modif");
		hammerM.setLore(Arrays
				.asList(new String[] { "§7Cet outil magique vous permet de casser en 3*3", "§7Utilisation : " + 200 }));
		hammerM.addItemFlags(new ItemFlag[] { ItemFlag.HIDE_ATTRIBUTES });
		hammerM.addItemFlags(new ItemFlag[] { ItemFlag.HIDE_DESTROYS });
		hammerM.addItemFlags(new ItemFlag[] { ItemFlag.HIDE_ENCHANTS });
		hammerM.addItemFlags(new ItemFlag[] { ItemFlag.HIDE_UNBREAKABLE });
		hammer.setItemMeta(hammerM);
		hammer.addUnsafeEnchantment(Enchantment.DURABILITY, 500);
		return hammer;
	}

	public static ItemStack getHopper() {
		ItemStack hammer = new ItemStack(Material.HOPPER, 1);
		ItemMeta hammerM = hammer.getItemMeta();
		hammerM.setDisplayName("§6» §eHopper §6«");
		hammerM.setLore(
				Arrays.asList(new String[] { "§f» §7Permet de récupèrer les items à §25 §7blocs autour de lui" }));
		hammerM.addItemFlags(new ItemFlag[] { ItemFlag.HIDE_ATTRIBUTES });
		hammerM.addItemFlags(new ItemFlag[] { ItemFlag.HIDE_DESTROYS });
		hammerM.addItemFlags(new ItemFlag[] { ItemFlag.HIDE_ENCHANTS });
		hammerM.addItemFlags(new ItemFlag[] { ItemFlag.HIDE_UNBREAKABLE });
		hammer.setItemMeta(hammerM);
		hammer.addUnsafeEnchantment(Enchantment.DURABILITY, 500);
		return hammer;
	}

	public static ItemStack getIslandInfo(Material material, Island island) {
		ItemStack item = new ItemStack(material);
		ItemMeta itemM = item.getItemMeta();
		itemM.setLore(Arrays.asList("§f» §7Nombre de blocs sur l'île§7: §2" + island.getData(material)));
		item.setItemMeta(itemM);
		return item;
	}

}
