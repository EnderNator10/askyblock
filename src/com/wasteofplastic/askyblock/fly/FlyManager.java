package com.wasteofplastic.askyblock.fly;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.wasteofplastic.askyblock.ASkyBlock;
import com.wasteofplastic.askyblock.listener.ListenerAdapter;
import com.wasteofplastic.askyblock.zcore.Message;
import com.wasteofplastic.askyblock.zcore.TimerBuilder;
import com.wasteofplastic.askyblock.zcore.storage.Persist;
import com.wasteofplastic.askyblock.zcore.storage.Saveable;

public class FlyManager extends ListenerAdapter implements Saveable {

	private static FlyManager instance;

	private static Map<String, Long> flys = new HashMap<String, Long>();

	private transient List<Player> flyingPlayers = new ArrayList<Player>();

	/*
	 * Singleton
	 */
	public static FlyManager getInstance() {
		if (instance == null) {
			instance = new FlyManager();
			instance.run();
		}
		return instance;
	}

	@Override
	protected void onConnect(PlayerJoinEvent event, Player player) {
		if (!flys.containsKey(player.getName())) {
			flys.put(player.getName(), 1800l);
			message(player, Message.FLY_GIVE, TimerBuilder.getStringTime(1800));
		}

		if (!player.isOnGround() && !player.hasPermission("essentials.fly") && flys.get(player.getName()) > 0) {

			startFly(player);

		}

	}

	/**
	 * Envoyer les informations du fly
	 * 
	 * @param player
	 */
	@Deprecated
	public void sendFly(Player player) {
		message(player, Message.FLY_INFO, 0);
	}

	/**
	 * Commencer le fly
	 * 
	 * @param player
	 */
	public void startFly(Player player) {
		if (flyingPlayers.contains(player)) {
			message(player, Message.FLY_ENEABLE_ERROR);
			return;
		}
		if (flys.get(player.getName()) <= 0) {
			message(player, Message.FLY_ERROR);
			return;
		}
		message(player, Message.FLY_ENEABLE);
		message(player, Message.FLY_INFO, TimerBuilder.getStringTime(flys.get(player.getName())));
		flyingPlayers.add(player);
		player.setAllowFlight(true);
		player.setFlying(true);
	}

	/**
	 * Arrêter le fly
	 * 
	 * @param player
	 */
	public void endFly(Player player) {
		if (!isFlying(player))
			return;
		if (!flyingPlayers.contains(player)) {
			message(player, Message.FLY_DISEABLE_ERROR);
			return;
		}
		message(player, Message.FLY_DISEABLE);
		message(player, Message.FLY_INFO, TimerBuilder.getStringTime(flys.get(player.getName())));
		flyingPlayers.remove(player);
		player.setFlying(false);
		player.setAllowFlight(false);
	}

	@Override
	protected void onQuit(PlayerQuitEvent event, Player player) {
		endFly(player);
	}

	@Override
	public void onWorld(PlayerChangedWorldEvent event, Player player) {
		endFly(player);
	}

	@Override
	public void onPlayerDeath(PlayerDeathEvent event, Player player) {
		endFly(player);
	}

	/**
	 * Mettre a jour le status du joueur
	 * 
	 * @param player
	 */
	public void update(Player player) {
		if (!flyingPlayers.contains(player))
			startFly(player);
		else
			endFly(player);
	}

	/**
	 * Verifier si un joueur est en fly
	 * 
	 * @param player
	 * @return
	 */
	public boolean isFlying(Player player) {
		return flyingPlayers.contains(player);
	}

	/**
	 * Give du fly a un joueur
	 * 
	 * @param player
	 * @param sec
	 */
	public void giveFly(String player, long sec) {
		long tmpFly = flys.getOrDefault(player, 0l);
		tmpFly += sec;
		flys.put(player, tmpFly);
	}

	/**
	 * Récupérer le temps du fly en string
	 * 
	 * @param player
	 * @return
	 */
	public String getStringFly(Player player) {
		return TimerBuilder.getStringTime(flys.get(player.getName()));
	}

	/**
	 * Permet que chaque secondes, tous les joueurs en fly perde une seconde
	 */
	public void run() {

		new Timer().scheduleAtFixedRate(new TimerTask() {

			@Override
			public void run() {

				if (!ASkyBlock.getPlugin().isEnabled()) {
					cancel();
					return;
				}

				Iterator<Player> players = flyingPlayers.iterator();

				while (players.hasNext()) {

					Player player = players.next();

					/**
					 * Si le joueur est en ligne
					 */
					if (player.isOnline() && !player.isOnGround()) {

						long tmpSec = flys.get(player.getName());

						tmpSec--;

						/**
						 * Si le temps de fly est inférieur a 0
						 */

						if (tmpSec < 0) {

							message(player, Message.FLY_END);
							if (player.isFlying())
								player.setFlying(false);
							player.setAllowFlight(false);
							players.remove();

						}

						flys.put(player.getName(), tmpSec < 0 ? 0 : tmpSec);

					}

				}

			}
		}, 1000, 1000);

	}

	@Override
	public void save(Persist persist) {
		persist.save(this, "fly");
	}

	@Override
	public void load(Persist persist) {
		persist.loadOrSaveDefault(this, FlyManager.class, "fly");
	}

}
