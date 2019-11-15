package com.wasteofplastic.askyblock.challenges;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.bukkit.Material;
import org.bukkit.WorldType;
import org.bukkit.block.Biome;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.wasteofplastic.askyblock.ASkyBlock;
import com.wasteofplastic.askyblock.ASkyBlockAPI;
import com.wasteofplastic.askyblock.listener.ListenerAdapter;
import com.wasteofplastic.askyblock.zcore.Logger;
import com.wasteofplastic.askyblock.zcore.Logger.LogType;
import com.wasteofplastic.askyblock.zcore.storage.Persist;
import com.wasteofplastic.askyblock.zcore.storage.Saveable;

public class ChallengesManager extends ListenerAdapter implements Saveable {

	private static Map<String, PlayerChallenges> players = new HashMap<>();
	private transient Map<Integer, Challenge> challenges = new HashMap<>();

	private transient static ChallengesManager instance;

	public ChallengesManager() {

		/**
		 * Default
		 */
		int id = 1;
		addChallenge(new Challenge(ChallengeType.DEFAULT, id, "Fabricant de verre",
				Arrays.asList("Créer §2x10§7 verres"), new ItemStack(Material.GLASS)).setMoneyReward(100)
						.setXpReward(50).setPresentItem(new ItemStack(Material.GLASS, 10))
						.setItemsReward(new ItemStack(Material.ICE, 1)));
		id++;

		addChallenge(new Challenge(ChallengeType.DEFAULT, id, "Boulanger", Arrays.asList("Créer §2x32§7 pains"),
				new ItemStack(Material.BREAD)).setPresentItem(new ItemStack(Material.BREAD, 32))
						.setItemsReward(new ItemStack(Material.GRASS, 32)).setMoneyReward(100).setXpReward(20));
		id++;

		addChallenge(new Challenge(ChallengeType.DEFAULT, id, "Colorant", Arrays.asList("Créer §2x64§7 colorant vert"),
				new ItemStack(Material.INK_SACK, 1, (byte) 2))
						.setPresentItem(new ItemStack(Material.INK_SACK, 64, (byte) 2))
						.setItemsReward(new ItemStack(Material.SAPLING, 1, (byte) 1),
								new ItemStack(Material.SAPLING, 1, (byte) 0),
								new ItemStack(Material.SAPLING, 1, (byte) 3),
								new ItemStack(Material.SAPLING, 1, (byte) 4),
								new ItemStack(Material.SAPLING, 1, (byte) 5),
								new ItemStack(Material.SAPLING, 1, (byte) 2))
						.setMoneyReward(500).setXpReward(20));
		id++;

		addChallenge(new Challenge(ChallengeType.DEFAULT, id, "Imprimeur", Arrays.asList("Créer §2x64§7 papier"),
				new ItemStack(Material.PAPER)).setPresentItem(new ItemStack(Material.PAPER, 64))
						.setItemsReward(new ItemStack(Material.DIRT, 16), new ItemStack(Material.CLAY_BALL, 64))
						.setMoneyReward(100).setXpReward(20));
		id++;

		addChallenge(new Challenge(ChallengeType.DEFAULT, id, "Mineur", Arrays.asList("Miner §2x64§7 cobblestone"),
				new ItemStack(Material.COBBLESTONE)).setPresentItem(new ItemStack(Material.COBBLESTONE, 64))
						.setItemsReward(new ItemStack(Material.IRON_INGOT, 5)).setMoneyReward(200).setXpReward(50));
		id++;

		addChallenge(new Challenge(ChallengeType.DEFAULT, id, "Banque de semance",
				Arrays.asList("Récolter §2x128§7 graines de melon", "de citrouille et de blé"),
				new ItemStack(Material.SEEDS))
						.setPresentItem(new ItemStack(Material.SEEDS, 64), new ItemStack(Material.SEEDS, 64),
								new ItemStack(Material.MELON_SEEDS, 64), new ItemStack(Material.MELON_SEEDS, 64),
								new ItemStack(Material.PUMPKIN_SEEDS, 64), new ItemStack(Material.PUMPKIN_SEEDS, 64))
						.setItemsReward(new ItemStack(Material.DIAMOND, 3)).setMoneyReward(500).setXpReward(50));
		id++;

		addChallenge(new Challenge(ChallengeType.DEFAULT, id, "Constructeur",
				Arrays.asList("Avoir un niveau d'île supérieur ou égal à §2100§7."), new ItemStack(Material.WOOD))
						.setManuelVerif(
								player -> ASkyBlockAPI.getInstance().getLongIslandLevel(player.getUniqueId()) >= 100)
						.setItemsReward(new ItemStack(Material.GRASS, 128)).setMoneyReward(1000).setXpReward(250));
		id++;

		addChallenge(new Challenge(ChallengeType.DEFAULT, id, "Pistonneur", Arrays.asList("Créer §2x2§7 pistons"),
				new ItemStack(Material.PISTON_BASE)).setPresentItem(new ItemStack(Material.PISTON_BASE, 2))
						.setItemsReward(new ItemStack(Material.IRON_INGOT, 16)).setMoneyReward(200).setXpReward(50));
		id++;

		addChallenge(
				new Challenge(ChallengeType.DEFAULT, id, "KFC", Arrays.asList("Avoir §2x32§7 poulet cuit"),
						new ItemStack(Material.COOKED_CHICKEN))
								.setPresentItem(new ItemStack(Material.COOKED_CHICKEN, 32))
								.setItemsReward(new ItemStack(Material.DIAMOND_PICKAXE),
										new ItemStack(Material.IRON_SPADE), new ItemStack(Material.IRON_AXE))
								.setMoneyReward(200).setXpReward(50));
		id++;

		addChallenge(new Challenge(ChallengeType.DEFAULT, id, "Pistonneur", Arrays.asList("Créer §2x2§7 pistons"),
				new ItemStack(Material.PISTON_BASE)).setPresentItem(new ItemStack(Material.PISTON_BASE, 2))
						.setItemsReward(new ItemStack(Material.IRON_INGOT, 16)).setMoneyReward(200).setXpReward(50));
		id++;

		addChallenge(
				new Challenge(ChallengeType.DEFAULT, id, "Feuilles", Arrays.asList("Avoir §2x64§7 feuilles de chaine"),
						new ItemStack(Material.LEAVES)).setPresentItem(new ItemStack(Material.LEAVES, 64, (byte) 0))
								.setItemsReward(new ItemStack(Material.GRASS, 64), new ItemStack(Material.WOOD, 10))
								.setMoneyReward(200).setXpReward(50));
		id++;

		/**
		 * Nether
		 */

		addChallenge(new Challenge(ChallengeType.COMPETANT, id, "Nether", Arrays.asList("Etre dans le nether"),
				new ItemStack(Material.NETHERRACK))
						.setManuelVerif(player -> player.getLocation().getBlock().getBiome().equals(Biome.HELL))
						.setItemsReward(new ItemStack(Material.NETHER_STALK, 16))
						.setMoneyReward(400).setXpReward(200));
		id++;

	}

	/**
	 * 
	 * @param challenge
	 */
	public void addChallenge(Challenge challenge) {
		if (challenges.containsKey(challenge.getId()))
			Logger.info("Erreur avec l'id " + challenge.getId(), LogType.ERROR);
		else
			challenges.put(challenge.getId(), challenge);
	}

	/**
	 * @param type
	 * @param player
	 */
	public void open(ChallengeType type, Player player) {
		ASkyBlock.getPlugin().getInventoryManager().createInventory(3, player, 1, type, getChallenges(type),
				getPlayer(player.getName()));
	}

	public List<Challenge> getChallenges(ChallengeType type) {
		return this.challenges.values().stream().filter(c -> c.getType().equals(type)).collect(Collectors.toList());
	}

	/**
	 * @param name
	 * @return
	 */
	public PlayerChallenges createPlayer(String name) {
		PlayerChallenges playerChallenges = new PlayerChallenges(name, getDefault(), ChallengeType.DEFAULT);
		players.put(name, playerChallenges);
		return playerChallenges;
	}

	/**
	 * @return map
	 */
	public Map<Integer, Boolean> getDefault() {
		Map<Integer, Boolean> map = new HashMap<>();
		this.challenges.forEach((id, c) -> map.put(id, false));
		return map;
	}

	public PlayerChallenges getPlayer(String name) {
		if (!players.containsKey(name))
			return createPlayer(name);
		return players.get(name);
	}

	/**
	 * Singleton
	 * 
	 * @return {@link ChallengesManager}
	 */
	public static ChallengesManager getInstance() {
		if (instance == null)
			instance = new ChallengesManager();
		return instance;
	}

	@Override
	public void save(Persist persist) {
		persist.save(this, "challenge");
	}

	@Override
	public void load(Persist persist) {
		persist.loadOrSaveDefault(this, ChallengesManager.class, "challenge");
	}

}
