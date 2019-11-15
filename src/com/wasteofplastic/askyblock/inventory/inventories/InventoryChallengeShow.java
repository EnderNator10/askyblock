package com.wasteofplastic.askyblock.inventory.inventories;

import java.util.ArrayList;
import java.util.Collection;
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
import com.wasteofplastic.askyblock.challenges.PlayerChallenges;
import com.wasteofplastic.askyblock.inventory.ItemButton;
import com.wasteofplastic.askyblock.inventory.VInventory;
import com.wasteofplastic.askyblock.zcore.ItemBuilder;
import com.wasteofplastic.askyblock.zcore.Pagination;

public class InventoryChallengeShow extends VInventory {

	@Override
	@SuppressWarnings("unchecked")
	public boolean openInventory(ASkyBlock main, Player player, int page, Object... args) throws Exception {

		if (page <= 0) {
			page = 1;
			setPage(1);
		}

		List<Challenge> challenges = new ArrayList<>((Collection<Challenge>) args[0]);
		PlayerChallenges playerChallenges = (PlayerChallenges) args[1];
		int maxPage = getMaxPage(challenges);

		createInventory("§bChallenges §e" + page + "§7/§6" + maxPage);

		Pagination<Challenge> pagination = new Pagination<>();
		AtomicInteger slot = new AtomicInteger();

		pagination.paginate(challenges, 45, page).forEach(challenge -> {

			if (!playerChallenges.canDo(challenge)) {

				addItem(slot.getAndIncrement(), ItemBuilder.getCreatedItem(Material.STAINED_GLASS_PANE, 1, 14,
						"§cVous n'avez pas encore accès à ce challenge"));

			} else {

				ItemStack item = challenge.getDisplayItem().clone();

				ItemMeta itemMeta = item.getItemMeta();
				itemMeta.setDisplayName("§a" + challenge.getName());

				List<String> lore = new ArrayList<String>();

				if (playerChallenges.hasFinish(challenge.getId())) {
					itemMeta.addEnchant(Enchantment.ARROW_DAMAGE, 1, true);
					itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
				}
				lore.add("");
				lore.add("§f» §7Vous avez "
						+ (playerChallenges.hasFinish(challenge.getId()) ? "§aterminé" : "§cpas terminé")
						+ "§7 ce challenge");

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
				addItem(slot.getAndIncrement(), item);
			}
		});

		if (getPage() != 1)
			addItem(48, new ItemButton(ItemBuilder.getCreatedItem(Material.ARROW, 1, "§f» §7Page précédente"))
					.setClick(event -> createInventory(2, player, getPage() - 1, args)));
		if (getPage() != maxPage)
			addItem(50, new ItemButton(ItemBuilder.getCreatedItem(Material.ARROW, 1, "§f» §7Page suivante"))
					.setClick(event -> createInventory(2, player, getPage() + 1, args)));

		return true;
	}

	private int getMaxPage(List<Challenge> items) {
		return (items.size() / 45) + 1;
	}

	@Override
	protected void onClose(InventoryCloseEvent event, ASkyBlock plugin, Player player) {

	}

	@Override
	protected void onDrag(InventoryDragEvent event, ASkyBlock plugin, Player player) {

	}

	@Override
	public VInventory clone() {
		return new InventoryChallengeShow();
	}

}
