package com.wasteofplastic.askyblock.inventory.inventories;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.wasteofplastic.askyblock.ASkyBlock;
import com.wasteofplastic.askyblock.Island;
import com.wasteofplastic.askyblock.inventory.ItemButton;
import com.wasteofplastic.askyblock.inventory.VInventory;
import com.wasteofplastic.askyblock.zcore.ItemBuilder;
import com.wasteofplastic.askyblock.zcore.LimitHelper;

public class InventoryIsland extends VInventory {

	@Override
	public boolean openInventory(ASkyBlock main, Player player, int page, Object... args) throws Exception {

		createInventory("Mon île");

		Island island = main.getGrid().getIslandAt(main.getPlayers().getIslandLocation(player.getUniqueId()));

		addItem(20, new ItemButton(
				ItemBuilder.getCreatedItem("§bMon île", Material.GRASS, 1, "", "§f» §7Clique accéder à ton île", ""))
						.setClick(event -> {
							main.getGrid().homeTeleport(player);
						}));

		addItem(22, new ItemButton( getInfoBook(island, main)).setClick(event -> {
			player.closeInventory();
			player.performCommand("is level");
		}));

		addItem(24, new ItemButton(ItemBuilder.getCreatedItem("§bOptions", Material.COMPASS, 1, "",
				"§f» §7Clique accéder aux options", "")).setClick(event -> {
					player.performCommand("is settings");
				}));
		addItem(33, new ItemButton(ItemBuilder.getCreatedItem("§bGénérateur", Material.COBBLESTONE, 1, "",
				"§f» §7Niveau du générateur: §a" + island.getGeneratorLevel() + "§f/§217",
				"§f» §7Les minerais " + (island.isGeneratorOre() ? "§asont" : "§cne sont pas") + " §7activés.", "",
				"§f» §7Clique " + (island.isGeneratorOre() ? "§cdésactiver" : "§2activer") + " §7les minerais.", ""))
						.setClick(event -> {
							island.setGeneratorOre(!island.isGeneratorOre());
							main.getInventoryManager().update(player);
						}));

		addItem(29, new ItemButton(ItemBuilder.getCreatedItemFlag(Material.IRON_SWORD, 1, "§bChallenges",  "",
				"§f» §7Clique accéder aux challenges", "")).setClick(event -> {
					player.performCommand("c");
				}));
		
		addItem(31, new ItemButton(ItemBuilder.getCreatedItemFlag(Material.DIAMOND, 1, "§bTop des îles",  "",
				"§f» §7Clique pour accéder au top 10 des îles", "")).setClick(event -> {
					player.performCommand("is top");
				}));
		addItem(40, new ItemButton(ItemBuilder.getCreatedItemFlag(Material.DIAMOND_BLOCK, 1, "§bValeur des blocs",  "",
				"§f» §7Clique pour accéder aux valeurs des blocs", "")).setClick(event -> {
					createInventory(2, player, 1);
				}));
		
		addItem(45, ItemBuilder.getCreatedItemWithLore(Material.EMERALD, 1, "§bAide", 
				
				"",
				"§f» §7Voici la liste des commandes:",
				"",
				"  §f» §2/is go§7 Se téléporter à votre île",
				"  §f» §2/is name <name> §7Changer le nom de votre île",
				"  §f» §2/is sethome §7Changer l'home de votre île",
				"  §f» §2/is value §7Connaitre la valeur d'un bloc",
				"  §f» §2/is warps §7Voir la liste des warps",
				"  §f» §2/is invite <joueur> §7Inviter un joueur",
				"  §f» §2/is <accept/reject> §7Accepter ou refuser une invitation",
				"  §f» §2/is makeleader <player> §7Changer le chef de votre île",
				"  §f» §2/is kick <player> §7Expulser un joueur",
				"  §f» §2/is ban <player> §7Bannir un joueur de votre île",
				"  §f» §2/is unban <player> §7Revoquer le bannis d'un joueur de votre île",
				"  §f» §2/is coop <player> §7Donner des accès provisoir à un joueur",
				"  §f» §2/is uncoop <player> §7Retirer les accès provisoir à un joueur",
				""
				
				));
		
		addItem(4, getProfil(player));

		return true;
	}

	private ItemStack getInfoBook(Island island, ASkyBlock main) {
		ItemStack item = new ItemStack(Material.BOOK);
		ItemMeta itemMeta = item.getItemMeta();
		itemMeta.setDisplayName("§bInformations sur votre île");

		List<String> lore = new ArrayList<String>();

		lore.add("");
		lore.add("§f» §7Niveau de l'île: §a" + main.getPlayers().getIslandLevel(player.getUniqueId()));
		lore.add("§f» §7Niveau du générateur: §a" + island.getGeneratorLevel() + "§f/§217");
		lore.add("§f» §7Génère les minerais: " + (island.isGeneratorOre() ? "§2Oui" : "§cNon"));
		lore.add("§f» §7Limite de hopper: §a" + island.getHopperLimit() + "§f/§2" + LimitHelper.HOPPER.getLimit());
		lore.add("§f» §7Nombre d'hopper sur l'île: §a" + island.getHopperCount() + "§f/§2" + island.getHopperLimit());
		lore.add("§f» §7Limite de piston: §a" + island.getPistonLimit() + "§f/§2" + LimitHelper.PISTON.getLimit());
		lore.add("§f» §7Nombre de piston sur l'île: §a" + island.getPiston() + "§f/§2" + island.getPistonLimit());
		lore.add("§f» §7Limite de membre: §a" + island.getMembers().size() + "§f/§2" + LimitHelper.MEMBER.getLimit());
		lore.add("§f» §7Biome: §a" + island.getBiome().name());
		lore.add("§f» §7Taille de l'île: §a" + island.getProtectionSize() + "§f/§2" + LimitHelper.ISLAND.getLimit());
		lore.add("§f» §7Chef de l'île: §a" + Bukkit.getOfflinePlayer(island.getOwner()).getName());
		lore.add("");
		lore.add("§f» §7Clique pour calculer le niveau d'île");
		lore.add("");

		itemMeta.setLore(lore);
		item.setItemMeta(itemMeta);
		return item;
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
		return new InventoryIsland();
	}

}
