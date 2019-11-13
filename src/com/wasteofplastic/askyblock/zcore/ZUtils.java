package com.wasteofplastic.askyblock.zcore;

import java.util.Timer;
import java.util.TimerTask;

import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

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
						runnable.run();
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
	
}
