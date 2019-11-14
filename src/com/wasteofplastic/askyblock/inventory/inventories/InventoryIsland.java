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

		createInventory("Mon �le");

		Island island = main.getGrid().getIslandAt(main.getPlayers().getIslandLocation(player.getUniqueId()));

		addItem(20, new ItemButton(
				ItemBuilder.getCreatedItem("�bMon �le", Material.GRASS, 1, "", "�f� �7Clique acc�der � ton �le", ""))
						.setClick(event -> {
							main.getGrid().homeTeleport(player);
						}));

		addItem(22, new ItemButton( getInfoBook(island, main)).setClick(event -> {
			player.closeInventory();
			player.performCommand("is level");
		}));

		addItem(24, new ItemButton(ItemBuilder.getCreatedItem("�bOptions", Material.COMPASS, 1, "",
				"�f� �7Clique acc�der aux options", "")).setClick(event -> {
					player.performCommand("is settings");
				}));
		addItem(33, new ItemButton(ItemBuilder.getCreatedItem("�bG�n�rateur", Material.COBBLESTONE, 1, "",
				"�f� �7Niveau du g�n�rateur: �a" + island.getGeneratorLevel() + "�f/�217",
				"�f� �7Les minerais " + (island.isGeneratorOre() ? "�asont" : "�cne sont pas") + " �7activ�s.", "",
				"�f� �7Clique " + (island.isGeneratorOre() ? "�cd�sactiver" : "�2activer") + " �7les minerais.", ""))
						.setClick(event -> {
							island.setGeneratorOre(!island.isGeneratorOre());
							main.getInventoryManager().update(player);
						}));

		addItem(29, new ItemButton(ItemBuilder.getCreatedItemFlag(Material.IRON_SWORD, 1, "�bChallenges",  "",
				"�f� �7Clique acc�der aux challenges", "")).setClick(event -> {
					player.performCommand("c");
				}));
		
		addItem(31, new ItemButton(ItemBuilder.getCreatedItemFlag(Material.DIAMOND, 1, "�bTop des �les",  "",
				"�f� �7Clique pour acc�der au top 10 des �les", "")).setClick(event -> {
					player.performCommand("is top");
				}));
		addItem(40, new ItemButton(ItemBuilder.getCreatedItemFlag(Material.DIAMOND_BLOCK, 1, "�bValeur des blocs",  "",
				"�f� �7Clique pour acc�der aux valeurs des blocs", "")).setClick(event -> {
					createInventory(2, player, 1);
				}));
		
		addItem(45, ItemBuilder.getCreatedItemWithLore(Material.EMERALD, 1, "�bAide", 
				
				"",
				"�f� �7Voici la liste des commandes:",
				"",
				"  �f� �2/is go�7 Se t�l�porter � votre �le",
				"  �f� �2/is name <name> �7Changer le nom de votre �le",
				"  �f� �2/is sethome �7Changer l'home de votre �le",
				"  �f� �2/is value �7Connaitre la valeur d'un bloc",
				"  �f� �2/is warps �7Voir la liste des warps",
				"  �f� �2/is invite <joueur> �7Inviter un joueur",
				"  �f� �2/is <accept/reject> �7Accepter ou refuser une invitation",
				"  �f� �2/is makeleader <player> �7Changer le chef de votre �le",
				"  �f� �2/is kick <player> �7Expulser un joueur",
				"  �f� �2/is ban <player> �7Bannir un joueur de votre �le",
				"  �f� �2/is unban <player> �7Revoquer le bannis d'un joueur de votre �le",
				"  �f� �2/is coop <player> �7Donner des acc�s provisoir � un joueur",
				"  �f� �2/is uncoop <player> �7Retirer les acc�s provisoir � un joueur",
				""
				
				));
		
		addItem(4, getProfil(player));

		return true;
	}

	private ItemStack getInfoBook(Island island, ASkyBlock main) {
		ItemStack item = new ItemStack(Material.BOOK);
		ItemMeta itemMeta = item.getItemMeta();
		itemMeta.setDisplayName("�bInformations sur votre �le");

		List<String> lore = new ArrayList<String>();

		lore.add("");
		lore.add("�f� �7Niveau de l'�le: �a" + main.getPlayers().getIslandLevel(player.getUniqueId()));
		lore.add("�f� �7Niveau du g�n�rateur: �a" + island.getGeneratorLevel() + "�f/�217");
		lore.add("�f� �7G�n�re les minerais: " + (island.isGeneratorOre() ? "�2Oui" : "�cNon"));
		lore.add("�f� �7Limite de hopper: �a" + island.getHopperLimit() + "�f/�2" + LimitHelper.HOPPER.getLimit());
		lore.add("�f� �7Nombre d'hopper sur l'�le: �a" + island.getHopperCount() + "�f/�2" + island.getHopperLimit());
		lore.add("�f� �7Limite de piston: �a" + island.getPistonLimit() + "�f/�2" + LimitHelper.PISTON.getLimit());
		lore.add("�f� �7Nombre de piston sur l'�le: �a" + island.getPiston() + "�f/�2" + island.getPistonLimit());
		lore.add("�f� �7Limite de membre: �a" + island.getMembers().size() + "�f/�2" + LimitHelper.MEMBER.getLimit());
		lore.add("�f� �7Biome: �a" + island.getBiome().name());
		lore.add("�f� �7Taille de l'�le: �a" + island.getProtectionSize() + "�f/�2" + LimitHelper.ISLAND.getLimit());
		lore.add("�f� �7Chef de l'�le: �a" + Bukkit.getOfflinePlayer(island.getOwner()).getName());
		lore.add("");
		lore.add("�f� �7Clique pour calculer le niveau d'�le");
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
