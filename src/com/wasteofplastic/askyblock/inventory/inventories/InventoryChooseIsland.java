package com.wasteofplastic.askyblock.inventory.inventories;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.wasteofplastic.askyblock.ASkyBlock;
import com.wasteofplastic.askyblock.command.commands.IslandPerfom;
import com.wasteofplastic.askyblock.inventory.ItemButton;
import com.wasteofplastic.askyblock.inventory.VInventory;
import com.wasteofplastic.askyblock.schematics.Schematic;

public class InventoryChooseIsland extends VInventory {

	@Override
	@SuppressWarnings("unchecked")
	public boolean openInventory(ASkyBlock main, Player player, int page, Object... args) throws Exception {

		List<Schematic> schems = (List<Schematic>) args[1];

		createInventory("§bChoix de ton île", 27);

		AtomicInteger slot = new AtomicInteger(0);

		schems.forEach(schem -> {

			addItem(slot.getAndIncrement(), new ItemButton(getItem(schem.getName(), schem)).setRightClick(event -> {
				IslandPerfom.getInstance().newIsland(player, schem);
			}).setLeftClick(event -> {
				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "ewarp " + schem.getName() + " " + player.getName());
			}));

		});

		return true;
	}

	private ItemStack getItem(String key, Schematic s) {
		switch (key) {
		case "default": {
			ItemStack item = new ItemStack(Material.DIRT);
			ItemMeta itemMeta = item.getItemMeta();
			itemMeta.setDisplayName("§bIle par default");
			List<String> lore = new ArrayList<String>();
			lore.add("");
			lore.add("§f» §7Description:");
			lore.add("  §f- §7Une île assez grande, quelque arbres.");
			lore.add("  §f- §7De quoi commencer dans les meilleurs conditions");
			lore.add("");
			lore.add("§f» §7Biome§7: §2" + s.getBiome().name());
			lore.add("");
			lore.add("§f» §7Clique §e§ldroit §7pour créer ton île");
			lore.add("§f» §7Clique §cgauche§7 pour visiter l'île");
			lore.add("");
			itemMeta.setLore(lore);
			item.setItemMeta(itemMeta);
			return item;
		}
		case "classique1": {
			ItemStack item = new ItemStack(Material.GRASS);
			ItemMeta itemMeta = item.getItemMeta();
			itemMeta.setDisplayName("§bIle Classique");
			List<String> lore = new ArrayList<String>();
			lore.add("");
			lore.add("§f» §7Description:");
			lore.add("  §f- §7Trois petite île relier enssemble");
			lore.add("  §f- §7Quelque minerais, beaucoup de bois");
			lore.add("  §f- §7Une île vraiment bien pour jouer avec simplicité");
			lore.add("");
			lore.add("§f» §7Biome§7: §2" + s.getBiome().name());
			lore.add("");
			lore.add("§f» §7Clique §e§ldroit §7pour créer ton île");
			lore.add("§f» §7Clique §c§lgauche§7 pour visiter l'île");
			lore.add("");
			itemMeta.setLore(lore);
			item.setItemMeta(itemMeta);
			return item;
		}
		case "classique2": {
			ItemStack item = new ItemStack(Material.GRASS);
			ItemMeta itemMeta = item.getItemMeta();
			itemMeta.setDisplayName("§bIle Classique 2");
			itemMeta.addEnchant(Enchantment.ARROW_DAMAGE, 1, true);
			itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
			List<String> lore = new ArrayList<String>();
			lore.add("");
			lore.add("§f» §7Description:");
			lore.add("  §f- §7Une très petite île");
			lore.add("  §f- §7Assez difficile");
			lore.add("");
			lore.add("§f» §7Biome§7: §2" + s.getBiome().name());
			lore.add("");
			lore.add("§f» §7Clique §e§ldroit §7pour créer ton île");
			lore.add("§f» §7Clique §c§lgauche§7 pour visiter l'île");
			lore.add("");
			itemMeta.setLore(lore);
			item.setItemMeta(itemMeta);
			return item;
		}
		case "ice1": {
			ItemStack item = new ItemStack(Material.SNOW_BALL);
			ItemMeta itemMeta = item.getItemMeta();
			itemMeta.setDisplayName("§bIle Neige");
			List<String> lore = new ArrayList<String>();
			lore.add("");
			lore.add("§f» §7Description:");
			lore.add("  §f- §7Une grande île avec un petit igloo");
			lore.add("  §f- §7Vraiment très facile de s'y développer");
			lore.add("");
			lore.add("§f» §7Biome§7: §2" + s.getBiome().name());
			lore.add("");
			lore.add("§f» §7Clique §e§ldroit §7pour créer ton île");
			lore.add("§f» §7Clique §c§lgauche§7 pour visiter l'île");
			lore.add("");
			itemMeta.setLore(lore);
			item.setItemMeta(itemMeta);
			return item;
		}
		case "ice2": {
			ItemStack item = new ItemStack(Material.SNOW_BLOCK);
			ItemMeta itemMeta = item.getItemMeta();
			itemMeta.setDisplayName("§bIle Neige 2");
			List<String> lore = new ArrayList<String>();
			lore.add("");
			lore.add("§f» §7Description:");
			lore.add("  §f- §7Deux petite île en neige");
			lore.add("  §f- §7Pas beaucoup de stone et aucun arbre");
			lore.add("");
			lore.add("§f» §7Biome§7: §2" + s.getBiome().name());
			lore.add("");
			lore.add("§f» §7Clique §e§ldroit §7pour créer ton île");
			lore.add("§f» §7Clique §c§lgauche§7 pour visiter l'île");
			lore.add("");
			itemMeta.setLore(lore);
			item.setItemMeta(itemMeta);
			return item;
		}
		case "mesa1": {
			ItemStack item = new ItemStack(Material.STAINED_CLAY, 1, (byte) 1);
			ItemMeta itemMeta = item.getItemMeta();
			itemMeta.setDisplayName("§bIle Mesa");
			List<String> lore = new ArrayList<String>();
			lore.add("");
			lore.add("§f» §7Description:");
			lore.add("  §f- §7Une très grande île en clay avec une petite cabanne");
			lore.add("  §f- §7Pas de stone sur cette île");
			lore.add("");
			lore.add("§f» §7Biome§7: §2" + s.getBiome().name());
			lore.add("");
			lore.add("§f» §7Clique §e§ldroit §7pour créer ton île");
			lore.add("§f» §7Clique §c§lgauche§7 pour visiter l'île");
			lore.add("");
			itemMeta.setLore(lore);
			item.setItemMeta(itemMeta);
			return item;
		}
		case "mesa2": {
			ItemStack item = new ItemStack(Material.STAINED_CLAY, 1, (byte) 7);
			ItemMeta itemMeta = item.getItemMeta();
			itemMeta.setDisplayName("§bIle Mesa 2");
			List<String> lore = new ArrayList<String>();
			lore.add("");
			lore.add("§f» §7Description:");
			lore.add("  §f- §7Trois très petite île en clay qui ne sont pas relier");
			lore.add("  §f- §7Pas de stone sur cette île");
			lore.add("  §f- §7/§c!§7\\ Très difficile");
			lore.add("");
			lore.add("§f» §7Biome§7: §2" + s.getBiome().name());
			lore.add("");
			lore.add("§f» §7Clique §e§ldroit §7pour créer ton île");
			lore.add("§f» §7Clique §c§lgauche§7 pour visiter l'île");
			lore.add("");
			itemMeta.setLore(lore);
			item.setItemMeta(itemMeta);
			return item;
		}
		case "sand1": {
			ItemStack item = new ItemStack(Material.SAND, 1, (byte) 0);
			ItemMeta itemMeta = item.getItemMeta();
			itemMeta.setDisplayName("§bIle Sable");
			List<String> lore = new ArrayList<String>();
			lore.add("");
			lore.add("§f» §7Description:");
			lore.add("  §f- §7Ile de taille moyenne avec une petite maison");
			lore.add("  §f- §7Pas de stone sur cette île");
			lore.add("");
			lore.add("§f» §7Biome§7: §2" + s.getBiome().name());
			lore.add("");
			lore.add("§f» §7Clique §e§ldroit §7pour créer ton île");
			lore.add("§f» §7Clique §c§lgauche§7 pour visiter l'île");
			lore.add("");
			itemMeta.setLore(lore);
			item.setItemMeta(itemMeta);
			return item;
		}
		case "sand2": {
			ItemStack item = new ItemStack(Material.SAND, 1, (byte) 1);
			ItemMeta itemMeta = item.getItemMeta();
			itemMeta.setDisplayName("§bIle Sable");
			List<String> lore = new ArrayList<String>();
			lore.add("");
			lore.add("§f» §7Description:");
			lore.add("  §f- §7Une petite île avec un palmier");
			lore.add("  §f- §7Pas de stone sur cette île");
			lore.add("  §f- §7/§c!§7\\ Très difficile");
			lore.add("");
			lore.add("§f» §7Biome§7: §2" + s.getBiome().name());
			lore.add("");
			lore.add("§f» §7Clique §e§ldroit §7pour créer ton île");
			lore.add("§f» §7Clique §c§lgauche§7 pour visiter l'île");
			lore.add("");
			itemMeta.setLore(lore);
			item.setItemMeta(itemMeta);
			return item;
		}
		case "mush": {
			ItemStack item = new ItemStack(Material.RED_MUSHROOM);
			ItemMeta itemMeta = item.getItemMeta();
			itemMeta.setDisplayName("§bIle Champignon");
			List<String> lore = new ArrayList<String>();
			lore.add("");
			lore.add("§f» §7Description:");
			lore.add("  §f- §7Deux petite île en champignon");
			lore.add("");
			lore.add("§f» §7Biome§7: §2" + s.getBiome().name());
			lore.add("");
			lore.add("§f» §7Clique §e§ldroit §7pour créer ton île");
			lore.add("§f» §7Clique §c§lgauche§7 pour visiter l'île");
			lore.add("");
			itemMeta.setLore(lore);
			item.setItemMeta(itemMeta);
			return item;
		}
		default:
			return new ItemStack(Material.DIAMOND);
		}
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
		// TODO Auto-generated method stub
		return new InventoryChooseIsland();
	}

}
