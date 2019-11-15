package com.wasteofplastic.askyblock.inventory.inventories;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.wasteofplastic.askyblock.ASkyBlock;
import com.wasteofplastic.askyblock.challenges.Challenge;
import com.wasteofplastic.askyblock.challenges.ChallengeType;
import com.wasteofplastic.askyblock.challenges.ChallengesManager;
import com.wasteofplastic.askyblock.challenges.PlayerChallenges;
import com.wasteofplastic.askyblock.inventory.ItemButton;
import com.wasteofplastic.askyblock.inventory.VInventory;

public class InventoryChallenges extends VInventory {

	@SuppressWarnings("unchecked")
	@Override
	public boolean openInventory(ASkyBlock main, Player player, int page, Object... args) throws Exception {

		ChallengeType type = (ChallengeType) args[0];
		List<Challenge> challenges = (List<Challenge>) args[1];
		PlayerChallenges playerChallenges = (PlayerChallenges) args[2];

		createInventory("§bChallenges§o§l " + type.getName());

		AtomicInteger slot = new AtomicInteger(0);
		challenges.forEach(challenge -> {

			ItemStack item = challenge.getDisplayItem();

			ItemMeta itemMeta = item.getItemMeta();
			itemMeta.setDisplayName("§a" + challenge.getName());

			List<String> lore = new ArrayList<String>();

			if (playerChallenges.hasFinish(challenge.getId())) {
				itemMeta.addEnchant(Enchantment.ARROW_DAMAGE, 1, true);
				itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
			}

			lore.add("");
			lore.add("§f» §7Description:");
			challenge.getDesc().forEach(desc -> lore.add("  §f- §7" + desc));
			if (challenge.getPresentItem() != null && challenge.getPresentItem().size() >= 1) {
				lore.add("§f» §c/§7!§c\\ §7Les items seront retiré de votre inventaire §c/§7!§c\\");
				lore.add("");
			}
			lore.add("");
			if (challenge.getItemsReward() != null && challenge.getItemsReward().size() != 0) {
				lore.add("§f» §7Récompense" + (challenge.getItemsReward().size() == 1 ? "" : "s") + ":");
				challenge.getItemsReward().forEach(reward -> {
					lore.add("  §f- §7x" + reward.getAmount() + " " + betterName(reward));
				});
			}
			lore.add("");
			if (challenge.getXpReward() != 0)
				lore.add("§f» §7Récompense en xp: §2" + challenge.getXpReward() + "§axp");
			if (challenge.getMoneyReward() != 0)
				lore.add("§f» §7Récompense en $: §2" + challenge.getMoneyReward() + "§a$");
			lore.add("");

			itemMeta.setLore(lore);
			item.setItemMeta(itemMeta);

			addItem(slot.getAndIncrement(), new ItemButton(item).setClick(event -> {
				// if (!playerChallenges.hasFinish(challenge.getId()))
				challenge.perform(player, playerChallenges, challenges);
			}));
		});

		int currentSlot = 47;
		for (ChallengeType currentType : ChallengeType.values()) {

			ItemStack item = new ItemStack(Material.BOOK);
			ItemMeta itemMeta = item.getItemMeta();
			itemMeta.setDisplayName("§b" + currentType.getName());

			if (currentType.equals(type)) {
				itemMeta.addEnchant(Enchantment.ARROW_DAMAGE, 1, true);
				itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
			}

			List<String> lore = new ArrayList<String>();

			lore.add("");
			lore.add((currentType.equals(type) ? "§f» §cVous êtes déjà sur la bonne page"
					: (playerChallenges.can(currentType)
							? "§f» §7Clique pour accéder au challenges §2" + currentType.getName()
							: "§f» §cVous n'avez pas encore débloquer cette catégorie !")));
			lore.add("");

			itemMeta.setLore(lore);

			item.setItemMeta(itemMeta);

			addItem(currentSlot++, new ItemButton(item).setClick(event -> {
				if (playerChallenges.can(currentType))
					ChallengesManager.getInstance().open(currentType, player);
			}));
		}

		return true;
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
		return new InventoryChallenges();
	}

}
