package com.wasteofplastic.askyblock.challenges;

import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.function.Consumer;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.wasteofplastic.askyblock.zcore.Logger.LogType;
import com.wasteofplastic.askyblock.zcore.Message;
import com.wasteofplastic.askyblock.zcore.ReturnConsumer;
import com.wasteofplastic.askyblock.zcore.ZUtils;

public class Challenge extends ZUtils {

	// Default field

	private final ChallengeType type;
	private final int id;
	private final String name;
	private final List<String> desc;
	private final ItemStack displayItem;

	// Need

	private List<ItemStack> presentItem;
	private ReturnConsumer<Player> manuelVerif;

	// Reward

	private int moneyReward;
	private int xpReward;
	private List<ItemStack> itemsReward;
	private Consumer<Player> consumerReward;

	/**
	 * @param type
	 * @param id
	 * @param name
	 * @param desc
	 * @param displayItem
	 */
	public Challenge(ChallengeType type, int id, String name, List<String> desc, ItemStack displayItem) {
		this.type = type;
		this.id = id;
		this.name = name;
		this.desc = desc;
		this.displayItem = displayItem;
	}

	/**
	 * @param player
	 * @param playerChallenges
	 */
	@SuppressWarnings("deprecation")
	public void perform(Player player, PlayerChallenges playerChallenges, List<Challenge> challenges) {

		boolean can = true;

		if (presentItem != null) {
			ItemStack[] items = player.getInventory().getContents();
			for (ItemStack item : presentItem) {
				if (!hasItem(items, item))
					can = false;
			}
		}
		if (manuelVerif != null)
			can = manuelVerif.accept(player);

		/**
		 * Si le mec a fini
		 */
		if (can) {

			String message = String.format(Message.CHALLENGE_SUCCESS.getMessage(), name);

			message(player, message);
			player.sendTitle("§e§kII§e Félicitation §e§kII", message, 10, 50, 10);
			player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
			player.closeInventory();

			if (itemsReward != null)
				itemsReward.forEach(item -> give(player, item));
			if (moneyReward != 0)
				depositMoney(player, moneyReward);
			if (xpReward != 0)
				player.giveExp(xpReward);
			playerChallenges.getChallenges().put(id, true);

			if (presentItem != null)
				presentItem.forEach(item -> {
					removeItem(player, item.getAmount(), item.getType(), item.getData().getData());
				});

			player.updateInventory();

			com.wasteofplastic.askyblock.zcore.Logger.info(player.getName() + " vient de terminer le challenge " + name,
					LogType.SUCCESS);

			if (playerChallenges.canUpdate(challenges) && !playerChallenges.getType().equals(type.getNext())) {
				playerChallenges.setType(type.getNext());
				new Timer().schedule(new TimerTask() {

					@Override
					public void run() {
						com.wasteofplastic.askyblock.zcore.Logger.info(
								player.getName() + " vient de passer au niveau " + playerChallenges.getType().getName(),
								LogType.SUCCESS);
						String message = String.format(Message.CHALLENGE_LEVEL_UP.getMessage(),
								playerChallenges.getType().getName());
						player.sendTitle("§e§kII§e Félicitation §e§kII", message, 10, 50, 10);
						message(player, message);
						player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
					}
				}, 1000 * 2l);
			}

		} else {
			player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1.0f, 1.0f);
			message(player, Message.CHALLENGE_ERROR);
		}

	}

	public Challenge setManuelVerif(ReturnConsumer<Player> manuelVerif) {
		this.manuelVerif = manuelVerif;
		return this;
	}

	/**
	 * @param moneyReward
	 *            the moneyReward to set
	 */
	public Challenge setMoneyReward(int moneyReward) {
		this.moneyReward = moneyReward;
		return this;
	}

	/**
	 * @param xpReward
	 *            the xpReward to set
	 */
	public Challenge setXpReward(int xpReward) {
		this.xpReward = xpReward;
		return this;
	}

	/**
	 * @param itemsReward
	 *            the itemsReward to set
	 */
	public Challenge setItemsReward(ItemStack... itemsReward) {
		this.itemsReward = Arrays.asList(itemsReward);
		return this;
	}

	public Challenge setItemsReward(List<ItemStack> itemsReward) {
		this.itemsReward = itemsReward;
		return this;
	}

	/**
	 * @param consumerReward
	 *            the consumerReward to set
	 */
	public Challenge setConsumerReward(Consumer<Player> consumerReward) {
		this.consumerReward = consumerReward;
		return this;
	}

	public Challenge setPresentItem(ItemStack... presentItem) {
		this.presentItem = Arrays.asList(presentItem);
		return this;
	}

	public Challenge setPresentItem(List<ItemStack> presentItem) {
		this.presentItem = presentItem;
		return this;
	}

	public List<ItemStack> getPresentItem() {
		return presentItem;
	}

	/**
	 * @return the type
	 */
	public ChallengeType getType() {
		return type;
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the desc
	 */
	public List<String> getDesc() {
		return desc;
	}

	/**
	 * @return the displayItem
	 */
	public ItemStack getDisplayItem() {
		return displayItem;
	}

	/**
	 * @return the moneyReward
	 */
	public int getMoneyReward() {
		return moneyReward;
	}

	/**
	 * @return the xpReward
	 */
	public int getXpReward() {
		return xpReward;
	}

	/**
	 * @return the itemsReward
	 */
	public List<ItemStack> getItemsReward() {
		return itemsReward;
	}

	/**
	 * @return the consumerReward
	 */
	public Consumer<Player> getConsumerReward() {
		return consumerReward;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Challenge [type=" + type + ", id=" + id + ", name=" + name + ", desc=" + desc + "]";
	}

}
