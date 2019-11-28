package com.wasteofplastic.askyblock.challenges;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.wasteofplastic.askyblock.ASkyBlock;
import com.wasteofplastic.askyblock.ASkyBlockAPI;
import com.wasteofplastic.askyblock.listener.ListenerAdapter;
import com.wasteofplastic.askyblock.zcore.CustomEnchant;
import com.wasteofplastic.askyblock.zcore.ItemBuilder;
import com.wasteofplastic.askyblock.zcore.Logger;
import com.wasteofplastic.askyblock.zcore.Logger.LogType;
import com.wasteofplastic.askyblock.zcore.Message;
import com.wasteofplastic.askyblock.zcore.storage.Persist;
import com.wasteofplastic.askyblock.zcore.storage.Saveable;

public class ChallengesManager extends ListenerAdapter implements Saveable {

	private static Map<String, PlayerChallenges> players = new HashMap<>();
	private transient Map<Integer, Challenge> challenges = new HashMap<>();

	private transient static ChallengesManager instance;

	private ChallengesManager() {
	}

	private ItemStack[] getColors(Material color, int how) {
		ItemStack[] colors = new ItemStack[15];
		for (int a = 0; a != 15; a++)
			colors[a] = new ItemStack(color, how, (byte) a);
		return colors;
	}

	private List<ItemStack> getLeaves(int how) {
		List<ItemStack> leaves = new ArrayList<>();
		leaves.add(new ItemStack(Material.LEAVES, how, (byte) 0));
		leaves.add(new ItemStack(Material.LEAVES, how, (byte) 1));
		leaves.add(new ItemStack(Material.LEAVES, how, (byte) 2));
		leaves.add(new ItemStack(Material.LEAVES, how, (byte) 3));
		leaves.add(new ItemStack(Material.LEAVES_2, how, (byte) 0));
		leaves.add(new ItemStack(Material.LEAVES_2, how, (byte) 1));
		return leaves;
	}

	private List<ItemStack> getPotions() {
		List<ItemStack> leaves = new ArrayList<>();
		leaves.add(new ItemStack(Material.POTION, 1, (byte) 8259));
		leaves.add(new ItemStack(Material.POTION, 1, (byte) 8266));
		leaves.add(new ItemStack(Material.POTION, 1, (byte) 8229));
		leaves.add(new ItemStack(Material.POTION, 1, (byte) 8236));
		leaves.add(new ItemStack(Material.POTION, 1, (byte) 8270));
		leaves.add(new ItemStack(Material.POTION, 1, (byte) 8265));
		leaves.add(new ItemStack(Material.POTION, 1, (byte) 8257));
		return leaves;
	}

	@SuppressWarnings("deprecation")
	private List<ItemStack> getFood() {
		List<ItemStack> food = new ArrayList<>();
		food.add(new ItemStack(Material.APPLE, 64));
		food.add(new ItemStack(Material.BEETROOT_SOUP, 1));
		food.add(new ItemStack(Material.MUSHROOM_SOUP, 1));
		food.add(new ItemStack(Material.BREAD, 64));
		food.add(new ItemStack(Material.getMaterial(319), 64));
		food.add(new ItemStack(Material.getMaterial(320), 64));
		food.add(new ItemStack(Material.RAW_FISH, 64));
		food.add(new ItemStack(Material.RAW_FISH, 64, (byte) 1));
		food.add(new ItemStack(Material.RAW_FISH, 64, (byte) 2));
		food.add(new ItemStack(Material.RAW_FISH, 64, (byte) 3));
		food.add(new ItemStack(Material.COOKED_FISH, 64));
		food.add(new ItemStack(Material.CAKE, 1));
		food.add(new ItemStack(Material.COOKIE, 64));
		food.add(new ItemStack(Material.MELON, 64));
		food.add(new ItemStack(Material.RAW_BEEF, 64));
		food.add(new ItemStack(Material.COOKED_BEEF, 64));
		food.add(new ItemStack(Material.RAW_CHICKEN, 64));
		food.add(new ItemStack(Material.COOKED_CHICKEN, 64));
		food.add(new ItemStack(Material.SPIDER_EYE, 64));
		food.add(new ItemStack(Material.CARROT_ITEM, 64));
		food.add(new ItemStack(Material.POTATO_ITEM, 64));
		food.add(new ItemStack(Material.POISONOUS_POTATO, 64));
		food.add(new ItemStack(Material.BAKED_POTATO, 64));
		food.add(new ItemStack(Material.PUMPKIN_PIE, 64));
		food.add(new ItemStack(Material.RABBIT_STEW, 1));
		food.add(new ItemStack(Material.COOKED_RABBIT, 64));
		food.add(new ItemStack(Material.RABBIT, 64));
		food.add(new ItemStack(Material.MUTTON, 64));
		food.add(new ItemStack(Material.COOKED_MUTTON, 64));
		food.add(new ItemStack(Material.BEETROOT, 64));
		return food;
	}

	private List<ItemStack> getFlower(int how) {
		List<ItemStack> flowers = new ArrayList<>();
		flowers.add(new ItemStack(Material.YELLOW_FLOWER, how));
		for (int a = 0; a != 8; a++)
			flowers.add(new ItemStack(Material.RED_ROSE, how, (byte) a));
		return flowers;
	}

	private List<ItemStack> getCobbleStone() {
		List<ItemStack> cobbles = new ArrayList<>();
		for (int a = 0; a != 36; a++)
			cobbles.add(new ItemStack(Material.COBBLESTONE, 64));
		return cobbles;
	}

	@SuppressWarnings("deprecation")
	private List<ItemStack> getDJ() {
		List<ItemStack> dj = new ArrayList<>();
		for (int a = 2256; a != 2267; a++)
			dj.add(new ItemStack(Material.getMaterial(a), 1));
		return dj;
	}

	/**
	 * House check
	 */

	private final transient List<Material> houses = Arrays.asList(Material.WOODEN_DOOR, Material.TORCH, Material.ANVIL,
			Material.THIN_GLASS, Material.ENCHANTMENT_TABLE, Material.WORKBENCH, Material.BED_BLOCK, Material.BOOKSHELF,
			Material.FURNACE);

	/**
	 * Permet de vérifier si le joueur est a coté d'une maison
	 * 
	 * @param player
	 * @return
	 */
	private boolean houseCheck(Player player) {
		List<Material> current = new ArrayList<>();
		Location defaultLocation = player.getLocation();
		for (int x = defaultLocation.getBlockX() - 1; x != defaultLocation.getBlockX() + 3; x++) {
			for (int y = defaultLocation.getBlockY() - 1; y != defaultLocation.getBlockY() + 3; y++) {
				for (int z = defaultLocation.getBlockZ() - 1; z != defaultLocation.getBlockZ() + 3; z++) {
					Block block = defaultLocation.getWorld().getBlockAt(x, y, z);
					if (block.getType().equals(Material.AIR))
						continue;
					if (houses.contains(block.getType()) && !current.contains(block.getType())) {
						current.add(block.getType());
					}
				}
			}
		}
		return current.size() == 9;
	}

	private boolean playerWalkOnBeacon(Player player) {
		return player.getLocation().getBlock().getRelative(BlockFace.DOWN).getType().equals(Material.BEACON);
	}

	private boolean nearbyEntities(Player player, EntityType type, int minim) {
		AtomicInteger count = new AtomicInteger();
		player.getWorld().getNearbyEntities(player.getLocation(), 10.0, 10.0, 10.0).forEach(entity -> {
			if (entity.getType().equals(type)) {
				count.getAndIncrement();
			}
		});
		return count.get() >= minim;
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

		PlayerChallenges playerChallenges = getPlayer(player.getName());

		ASkyBlock.getPlugin().getInventoryManager().createInventory(3, player, 1, type, getChallenges(type),
				playerChallenges);
		if (playerChallenges.canUpdate(getChallenges(playerChallenges.getType()))) {
			playerChallenges.setType(type.getNext());
			com.wasteofplastic.askyblock.zcore.Logger.info(
					player.getName() + " vient de passer au niveau " + getPlayer(player.getName()).getType().getName(),
					LogType.SUCCESS);
			String message = String.format(Message.CHALLENGE_LEVEL_UP.getMessage(),
					getPlayer(player.getName()).getType().getName());
			player.sendTitle("§e§kII§e Félicitation §e§kII", message, 10, 50, 10);
			message(player, message);
			player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
		}
	}

	public List<Challenge> getChallenges(ChallengeType type) {
		return this.challenges.values().stream().filter(c -> c.getType().equals(type)).collect(Collectors.toList());
	}

	public Collection<Challenge> getChallenges() {
		return challenges.values();
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

	public void deletePlayer(String name, CommandSender sender) {
		if (players.containsKey(name)) {
			players.remove(name);
			message(sender, Message.CHALLENGE_DELETE_SUCCES, name);
		} else {
			message(sender, Message.CHALLENGE_DELETE_ERROR, name);
		}
	}

	/**
	 * Singleton
	 * 
	 * @return {@link ChallengesManager}
	 */
	public static ChallengesManager getInstance() {
		if (instance == null) {
			instance = new ChallengesManager();
			instance.create();
		}
		return instance;
	}

	private void create() {
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

		addChallenge(new Challenge(ChallengeType.DEFAULT, id, "Pistonneur+", Arrays.asList("Créer §2x16§7 pistons"),
				new ItemStack(Material.PISTON_BASE)).setPresentItem(new ItemStack(Material.PISTON_BASE, 16))
						.setItemsReward(new ItemStack(Material.IRON_INGOT, 32)).setMoneyReward(200).setXpReward(50));
		id++;

		addChallenge(
				new Challenge(ChallengeType.DEFAULT, id, "Feuilles", Arrays.asList("Avoir §2x64§7 feuilles de chêne"),
						new ItemStack(Material.LEAVES)).setPresentItem(new ItemStack(Material.LEAVES, 64, (byte) 0))
								.setItemsReward(new ItemStack(Material.GRASS, 64), new ItemStack(Material.WOOD, 10))
								.setMoneyReward(200).setXpReward(50));
		id++;

		addChallenge(new Challenge(ChallengeType.DEFAULT, id, "Zombie", Arrays.asList("Avoir §2x64§7 viande de zombie"),
				new ItemStack(Material.ROTTEN_FLESH)).setPresentItem(new ItemStack(Material.ROTTEN_FLESH, 64))
						.setItemsReward(new ItemStack(Material.MONSTER_EGG, 2, (byte) 93)).setMoneyReward(150)
						.setXpReward(50));
		id++;
		addChallenge(new Challenge(ChallengeType.DEFAULT, id, "Skeleton", Arrays.asList("Avoir §2x64§7 os"),
				new ItemStack(Material.BONE)).setPresentItem(new ItemStack(Material.BONE, 64))
						.setItemsReward(new ItemStack(Material.MONSTER_EGG, 2, (byte) 92)).setMoneyReward(150)
						.setXpReward(50));
		id++;
		addChallenge(new Challenge(ChallengeType.DEFAULT, id, "Spider", Arrays.asList("Avoir §2x64§7 toile d'araignée"),
				new ItemStack(Material.STRING)).setPresentItem(new ItemStack(Material.STRING, 64))
						.setItemsReward(new ItemStack(Material.MONSTER_EGG, 2, (byte) 90)).setMoneyReward(150)
						.setXpReward(50));
		id++;
		addChallenge(
				new Challenge(ChallengeType.DEFAULT, id, "Creeper", Arrays.asList("Avoir §2x64§7 poudre de creeper"),
						new ItemStack(Material.SULPHUR)).setPresentItem(new ItemStack(Material.SULPHUR, 64))
								.setItemsReward(new ItemStack(Material.MONSTER_EGG, 1, (byte) 50)).setMoneyReward(150)
								.setXpReward(50));
		id++;
		addChallenge(new Challenge(ChallengeType.DEFAULT, id, "Slime", Arrays.asList("Avoir §2x16§7 boule de slime"),
				new ItemStack(Material.SLIME_BALL)).setPresentItem(new ItemStack(Material.SLIME_BALL, 16))
						.setItemsReward(new ItemStack(Material.MONSTER_EGG, 1, (byte) 55)).setMoneyReward(150)
						.setXpReward(50));
		id++;

		/**
		 * Compétant
		 */

		addChallenge(new Challenge(ChallengeType.COMPETANT, id, "Nether", Arrays.asList("Etre dans le nether"),
				new ItemStack(Material.NETHERRACK))
						.setManuelVerif(player -> player.getLocation().getBlock().getBiome().equals(Biome.HELL))
						.setItemsReward(new ItemStack(Material.NETHER_STALK, 16)).setMoneyReward(400).setXpReward(200));
		id++;

		addChallenge(new Challenge(ChallengeType.COMPETANT, id, "Farmeur",
				Arrays.asList("Avoir §2x64§7 blés, sucres, melons, carrotes, patates et citrouilles"),
				new ItemStack(Material.WHEAT))
						.setPresentItem(new ItemStack(Material.WHEAT, 64), new ItemStack(Material.SUGAR, 64),
								new ItemStack(Material.MELON, 64), new ItemStack(Material.PUMPKIN, 64),
								new ItemStack(Material.POTATO_ITEM, 64), new ItemStack(Material.CARROT_ITEM, 64))
						.setItemsReward(new ItemStack(Material.INK_SACK, 1, (byte) 3),
								new ItemStack(Material.MONSTER_EGG, 1, (byte) 90),
								new ItemStack(Material.MONSTER_EGG, 1, (byte) 91),
								new ItemStack(Material.MONSTER_EGG, 1, (byte) 95))
						.setMoneyReward(300).setXpReward(100));
		id++;

		addChallenge(new Challenge(ChallengeType.COMPETANT, id, "Pêcheur", Arrays.asList("Cuire §2x32§7 poissons"),
				new ItemStack(Material.COOKED_FISH)).setPresentItem(new ItemStack(Material.COOKED_FISH, 32))
						.setItemsReward(new ItemStack(Material.IRON_INGOT, 32)).setMoneyReward(400).setXpReward(200));
		id++;

		addChallenge(new Challenge(ChallengeType.COMPETANT, id, "Cookie", Arrays.asList("Avoir §2x512§7 cookies"),
				new ItemStack(Material.COOKIE))
						.setPresentItem(new ItemStack(Material.COOKIE, 64), new ItemStack(Material.COOKIE, 64),
								new ItemStack(Material.COOKIE, 64), new ItemStack(Material.COOKIE, 64),
								new ItemStack(Material.COOKIE, 64), new ItemStack(Material.COOKIE, 64),
								new ItemStack(Material.COOKIE, 64), new ItemStack(Material.COOKIE, 64))
						.setItemsReward(new ItemStack(Material.REDSTONE, 64)).setMoneyReward(400).setXpReward(200));
		id++;

		addChallenge(new Challenge(ChallengeType.COMPETANT, id, "Feuilles",
				Arrays.asList("Avoir §2x64§7 feuilles de chêne, de boulot et de spruce"),
				new ItemStack(Material.LEAVES)).setPresentItem(new ItemStack(Material.LEAVES, 64, (byte) 0),
						new ItemStack(Material.LEAVES, 64, (byte) 1), new ItemStack(Material.LEAVES, 64, (byte) 2))
						.setItemsReward(new ItemStack(Material.GRASS, 64), new ItemStack(Material.WOOD, 32))
						.setMoneyReward(400).setXpReward(50));
		id++;

		addChallenge(new Challenge(ChallengeType.COMPETANT, id, "Bucheron",
				Arrays.asList("Avoir §2x128§7 buche de chêne"), new ItemStack(Material.LOG))
						.setPresentItem(new ItemStack(Material.LOG, 64), new ItemStack(Material.LOG, 64))
						.setItemsReward(new ItemStack(Material.LAPIS_ORE, 32)).setMoneyReward(400).setXpReward(200));
		id++;

		addChallenge(new Challenge(ChallengeType.COMPETANT, id, "Constructeur",
				Arrays.asList("Avoir un niveau d'île supérieur ou égal à §2250§7."), new ItemStack(Material.WOOD))
						.setManuelVerif(
								player -> ASkyBlockAPI.getInstance().getLongIslandLevel(player.getUniqueId()) >= 250)
						.setItemsReward(new ItemStack(Material.GRASS, 256)).setMoneyReward(1000).setXpReward(250));
		id++;

		addChallenge(new Challenge(ChallengeType.COMPETANT, id, "Pistonneur",
				Arrays.asList("Créer §2x32§7 pistons colant"), new ItemStack(Material.PISTON_STICKY_BASE))
						.setPresentItem(new ItemStack(Material.PISTON_STICKY_BASE, 32))
						.setItemsReward(new ItemStack(Material.SLIME_BALL, 16)).setMoneyReward(500).setXpReward(500));
		id++;

		addChallenge(new Challenge(ChallengeType.COMPETANT, id, "Bienvenue chez moi",
				Arrays.asList("Créer une maison qui contient,", "une porte en bois, un lit, une bibliothèque",
						"un four, une vitre, une torche", "une table de craft, une enclume",
						"et une table d'enchantement"),
				new ItemStack(Material.BED)).setManuelVerif(player -> houseCheck(player))
						.setItemsReward(new ItemStack(Material.DIAMOND, 8)).setMoneyReward(400).setXpReward(250));
		id++;

		addChallenge(new Challenge(ChallengeType.COMPETANT, id, "EnderPearls",
				Arrays.asList("Avoir §2x32§7 EnderPearls"), new ItemStack(Material.ENDER_PEARL))
						.setPresentItem(new ItemStack(Material.ENDER_PEARL, 16),
								new ItemStack(Material.ENDER_PEARL, 16))
						.setItemsReward(new ItemStack(Material.COAL_BLOCK, 8)).setMoneyReward(400).setXpReward(200));
		id++;

		addChallenge(new Challenge(ChallengeType.COMPETANT, id, "Super épée",
				Arrays.asList("Avoir une épée sharpness §26 §7(Faite /customcraft)"),
				new ItemStack(Material.DIAMOND_SWORD)).setPresentItem(
						ItemBuilder.enchant(Material.DIAMOND_SWORD, new CustomEnchant(Enchantment.DAMAGE_ALL, 6)))
						.setItemsReward(new ItemStack(Material.DIAMOND_BLOCK, 4)).setMoneyReward(1000)
						.setXpReward(200));
		id++;

		addChallenge(new Challenge(ChallengeType.COMPETANT, id, "Zombie",
				Arrays.asList("Avoir §2x128§7 viande de zombie"), new ItemStack(Material.ROTTEN_FLESH))
						.setPresentItem(new ItemStack(Material.ROTTEN_FLESH, 64),
								new ItemStack(Material.ROTTEN_FLESH, 64))
						.setItemsReward(new ItemStack(Material.MONSTER_EGG, 1, (byte) 54)).setMoneyReward(300)
						.setXpReward(50));
		id++;
		addChallenge(new Challenge(ChallengeType.COMPETANT, id, "Skeleton", Arrays.asList("Avoir §2x128§7 os"),
				new ItemStack(Material.BONE))
						.setPresentItem(new ItemStack(Material.BONE, 64), new ItemStack(Material.BONE, 64))
						.setItemsReward(new ItemStack(Material.MONSTER_EGG, 1, (byte) 57)).setMoneyReward(300)
						.setXpReward(50));
		id++;
		addChallenge(new Challenge(ChallengeType.COMPETANT, id, "Spider",
				Arrays.asList("Avoir §2x128§7 toile d'araignée"), new ItemStack(Material.STRING))
						.setPresentItem(new ItemStack(Material.STRING, 64), new ItemStack(Material.STRING, 64))
						.setItemsReward(new ItemStack(Material.MONSTER_EGG, 1, (byte) 65)).setMoneyReward(300)
						.setXpReward(50));
		id++;
		addChallenge(new Challenge(ChallengeType.COMPETANT, id, "Creeper",
				Arrays.asList("Avoir §2x128§7 poudre de creeper"), new ItemStack(Material.SULPHUR))
						.setPresentItem(new ItemStack(Material.SULPHUR, 64), new ItemStack(Material.SULPHUR, 64))
						.setItemsReward(new ItemStack(Material.MONSTER_EGG, 1, (byte) 94)).setMoneyReward(300)
						.setXpReward(50));
		id++;
		addChallenge(new Challenge(ChallengeType.COMPETANT, id, "Slime", Arrays.asList("Avoir §2x64§7 boule de slime"),
				new ItemStack(Material.SLIME_BALL)).setPresentItem(new ItemStack(Material.SLIME_BALL, 64))
						.setItemsReward(new ItemStack(Material.MONSTER_EGG, 2, (byte) 96)).setMoneyReward(300)
						.setXpReward(50));
		id++;

		/**
		 * Expert
		 */

		addChallenge(new Challenge(ChallengeType.EXPERT, id, "SlimeBall",
				Arrays.asList("Avoir §2x128§7 boule de slime"), new ItemStack(Material.SLIME_BALL))
						.setPresentItem(new ItemStack(Material.SLIME_BALL, 64), new ItemStack(Material.SLIME_BALL, 64))
						.setItemsReward(new ItemStack(Material.DIAMOND_BLOCK, 2)).setMoneyReward(500).setXpReward(200));
		id++;

		addChallenge(new Challenge(ChallengeType.EXPERT, id, "Patissier", Arrays.asList("Faire §2x9§7 gateaux"),
				new ItemStack(Material.CAKE))
						.setPresentItem(new ItemStack(Material.CAKE, 1), new ItemStack(Material.CAKE, 1),
								new ItemStack(Material.CAKE, 1), new ItemStack(Material.CAKE, 1),
								new ItemStack(Material.CAKE, 1), new ItemStack(Material.CAKE, 1),
								new ItemStack(Material.CAKE, 1), new ItemStack(Material.CAKE, 1),
								new ItemStack(Material.CAKE, 1))
						.setItemsReward(new ItemStack(Material.HOPPER, 1)).setMoneyReward(500).setXpReward(200));
		id++;

		addChallenge(new Challenge(ChallengeType.EXPERT, id, "Chercheur",
				Arrays.asList("Avoir une map, un compas, une horloge", "§2x64 §7netherrack, §2x16§7 sable des âmes",
						"§2x2 §7larme de ghast"),
				new ItemStack(Material.MAP))
						.setPresentItem(new ItemStack(Material.WATCH, 1), new ItemStack(Material.COMPASS, 1),
								new ItemStack(Material.MAP, 1), new ItemStack(Material.NETHERRACK, 64),
								new ItemStack(Material.SOUL_SAND, 16), new ItemStack(Material.GHAST_TEAR, 2))
						.setItemsReward(new ItemStack(Material.CACTUS, 64)).setMoneyReward(500).setXpReward(200));
		id++;

		addChallenge(new Challenge(ChallengeType.EXPERT, id, "Constructeur",
				Arrays.asList("Avoir un niveau d'île supérieur ou égal à §2400§7."), new ItemStack(Material.WOOD))
						.setManuelVerif(
								player -> ASkyBlockAPI.getInstance().getLongIslandLevel(player.getUniqueId()) >= 400)
						.setItemsReward(new ItemStack(Material.GRASS, 512)).setMoneyReward(1000).setXpReward(250));
		id++;

		addChallenge(new Challenge(ChallengeType.EXPERT, id, "Pistonneur",
				Arrays.asList("Créer §2x64§7 pistons colant"), new ItemStack(Material.PISTON_STICKY_BASE))
						.setPresentItem(new ItemStack(Material.PISTON_STICKY_BASE, 64))
						.setItemsReward(new ItemStack(Material.SLIME_BALL, 32)).setMoneyReward(500).setXpReward(500));
		id++;

		addChallenge(new Challenge(ChallengeType.EXPERT, id, "La laine c'est cool",
				Arrays.asList("Avoir §2x16§7 de chaque laine"), new ItemStack(Material.WOOL))
						.setPresentItem(getColors(Material.WOOL, 16))
						.setItemsReward(new ItemStack(Material.DIAMOND_BLOCK, 2)).setMoneyReward(500).setXpReward(200));
		id++;

		addChallenge(new Challenge(ChallengeType.EXPERT, id, "L'art de gîle",
				Arrays.asList("Avoir §2x16§7 de chaque argile teinté"), new ItemStack(Material.STAINED_CLAY))
						.setPresentItem(getColors(Material.STAINED_CLAY, 16)).setItemsReward(getFlower(4))
						.setMoneyReward(500).setXpReward(200));
		id++;

		addChallenge(new Challenge(ChallengeType.EXPERT, id, "Art et fênetre",
				Arrays.asList("Avoir §2x16§7 de chaque verre"), new ItemStack(Material.STAINED_GLASS))
						.setPresentItem(getColors(Material.STAINED_GLASS, 16)).setItemsReward(getFlower(4))
						.setMoneyReward(500).setXpReward(200));
		id++;

		addChallenge(new Challenge(ChallengeType.EXPERT, id, "Art et fênetre le retour",
				Arrays.asList("Avoir §2x32§7 de chaque vitre"), new ItemStack(Material.STAINED_GLASS_PANE))
						.setPresentItem(getColors(Material.STAINED_GLASS_PANE, 16)).setItemsReward(getFlower(4))
						.setMoneyReward(500).setXpReward(200));
		id++;

		addChallenge(new Challenge(ChallengeType.EXPERT, id, "Mineur", Arrays.asList("Miner §2x2304§7 cobblestone"),
				new ItemStack(Material.COBBLESTONE)).setPresentItem(getCobbleStone()).setItemsReward(
						new ItemStack(Material.STONE, 64, (byte) 1), new ItemStack(Material.STONE, 64, (byte) 1),
						new ItemStack(Material.STONE, 64, (byte) 2), new ItemStack(Material.STONE, 64, (byte) 2),
						new ItemStack(Material.STONE, 64, (byte) 3), new ItemStack(Material.STONE, 64, (byte) 3),
						new ItemStack(Material.STONE, 64, (byte) 4), new ItemStack(Material.STONE, 64, (byte) 4),
						new ItemStack(Material.STONE, 64, (byte) 5), new ItemStack(Material.STONE, 64, (byte) 5),
						new ItemStack(Material.STONE, 64, (byte) 6), new ItemStack(Material.STONE, 64, (byte) 6))
						.setMoneyReward(500).setXpReward(50));
		id++;

		addChallenge(new Challenge(ChallengeType.EXPERT, id, "J'ADORE QUAND ÇA EXPLOSE",
				Arrays.asList("Créer §2x64§7 tnt"), new ItemStack(Material.TNT))
						.setPresentItem(new ItemStack(Material.TNT, 64))
						.setItemsReward(new ItemStack(Material.SAND, 64), new ItemStack(Material.SAND, 64, (byte) 1))
						.setMoneyReward(500).setXpReward(500));
		id++;

		addChallenge(new Challenge(ChallengeType.EXPERT, id, "Beacon", Arrays.asList("Marcher sur un beacon"),
				new ItemStack(Material.BEACON)).setManuelVerif(player -> playerWalkOnBeacon(player))
						.setItemsReward(new ItemStack(Material.EMERALD_BLOCK, 1)).setMoneyReward(500).setXpReward(500));
		id++;

		addChallenge(new Challenge(ChallengeType.EXPERT, id, "Protecteur", Arrays.asList("Être à coté d'un iron golem"),
				new ItemStack(Material.EMERALD))
						.setManuelVerif(player -> nearbyEntities(player, EntityType.IRON_GOLEM, 1))
						.setItemsReward(new ItemStack(Material.HOPPER)).setMoneyReward(500).setXpReward(250));
		id++;

		addChallenge(new Challenge(ChallengeType.EXPERT, id, "Super épée",
				Arrays.asList("Avoir une épée sharpness §26 §7(Faite /customcraft)"),
				new ItemStack(Material.DIAMOND_SWORD)).setPresentItem(
						ItemBuilder.enchant(Material.DIAMOND_SWORD, new CustomEnchant(Enchantment.DAMAGE_ALL, 6)))
						.setItemsReward(new ItemStack(Material.DIAMOND_BLOCK, 4)).setMoneyReward(1000)
						.setXpReward(200));
		id++;

		addChallenge(new Challenge(ChallengeType.EXPERT, id, "Zombie", Arrays.asList("Avoir §2x256§7 viande de zombie"),
				new ItemStack(Material.ROTTEN_FLESH))
						.setPresentItem(new ItemStack(Material.ROTTEN_FLESH, 64),
								new ItemStack(Material.ROTTEN_FLESH, 64), new ItemStack(Material.ROTTEN_FLESH, 64),
								new ItemStack(Material.ROTTEN_FLESH, 64))
						.setItemsReward(new ItemStack(Material.MONSTER_EGG, 1, (byte) 95)).setMoneyReward(300)
						.setXpReward(50));
		id++;
		addChallenge(new Challenge(ChallengeType.EXPERT, id, "Skeleton", Arrays.asList("Avoir §2x256§7 os"),
				new ItemStack(Material.BONE))
						.setPresentItem(new ItemStack(Material.BONE, 64), new ItemStack(Material.BONE, 64),
								new ItemStack(Material.BONE, 64), new ItemStack(Material.BONE, 64))
						.setItemsReward(new ItemStack(Material.MONSTER_EGG, 1, (byte) 102)).setMoneyReward(300)
						.setXpReward(50));
		id++;
		addChallenge(new Challenge(ChallengeType.EXPERT, id, "Spider", Arrays.asList("Avoir §2x256§7 toile d'araignée"),
				new ItemStack(Material.STRING))
						.setPresentItem(new ItemStack(Material.STRING, 64), new ItemStack(Material.STRING, 64),
								new ItemStack(Material.STRING, 64), new ItemStack(Material.STRING, 64))
						.setItemsReward(new ItemStack(Material.MONSTER_EGG, 1, (byte) 105)).setMoneyReward(300)
						.setXpReward(50));
		id++;
		addChallenge(new Challenge(ChallengeType.EXPERT, id, "Creeper",
				Arrays.asList("Avoir §2x256§7 poudre de creeper"), new ItemStack(Material.SULPHUR))
						.setPresentItem(new ItemStack(Material.SULPHUR, 64), new ItemStack(Material.SULPHUR, 64),
								new ItemStack(Material.SULPHUR, 64), new ItemStack(Material.SULPHUR, 64))
						.setItemsReward(new ItemStack(Material.MONSTER_EGG, 1, (byte) 103)).setMoneyReward(300)
						.setXpReward(50));
		id++;
		addChallenge(new Challenge(ChallengeType.EXPERT, id, "Slime", Arrays.asList("Avoir §2x256§7 boule de slime"),
				new ItemStack(Material.SLIME_BALL))
						.setPresentItem(new ItemStack(Material.SLIME_BALL, 64), new ItemStack(Material.SLIME_BALL, 64),
								new ItemStack(Material.SLIME_BALL, 64), new ItemStack(Material.SLIME_BALL, 64))
						.setItemsReward(new ItemStack(Material.MONSTER_EGG, 2, (byte) 98)).setMoneyReward(300)
						.setXpReward(50));
		id++;

		/**
		 * Avancé
		 */

		addChallenge(new Challenge(ChallengeType.ADVANCED, id, "KFC", Arrays.asList("Avoir §2x512§7 poulet cuit"),
				new ItemStack(Material.COOKED_CHICKEN))
						.setPresentItem(new ItemStack(Material.COOKED_CHICKEN, 64),
								new ItemStack(Material.COOKED_CHICKEN, 64), new ItemStack(Material.COOKED_CHICKEN, 64),
								new ItemStack(Material.COOKED_CHICKEN, 64),
								new ItemStack(Material.COOKED_CHICKEN, 64), new ItemStack(Material.COOKED_CHICKEN, 64),
								new ItemStack(Material.COOKED_CHICKEN, 64), new ItemStack(Material.COOKED_CHICKEN, 64))
						.setItemsReward(new ItemStack(Material.HOPPER, 4)).setMoneyReward(750).setXpReward(250));
		id++;

		addChallenge(new Challenge(ChallengeType.ADVANCED, id, "Feuilles",
				Arrays.asList("Avoir §2x64§7 feuilles de chaque feuille"), new ItemStack(Material.LEAVES))
						.setPresentItem(getLeaves(64))
						.setItemsReward(new ItemStack(Material.GRASS, 64), new ItemStack(Material.WOOD, 10))
						.setMoneyReward(750).setXpReward(250));
		id++;

		addChallenge(new Challenge(ChallengeType.ADVANCED, id, "Constructeur",
				Arrays.asList("Avoir un niveau d'île supérieur ou égal à §2750§7."), new ItemStack(Material.WOOD))
						.setManuelVerif(
								player -> ASkyBlockAPI.getInstance().getLongIslandLevel(player.getUniqueId()) >= 750)
						.setItemsReward(new ItemStack(Material.GRASS, 1024)).setMoneyReward(2500).setXpReward(250));
		id++;

		addChallenge(new Challenge(ChallengeType.ADVANCED, id, "DJ", Arrays.asList("Avoir §2x1§7 de chaque CD"),
				new ItemStack(Material.RECORD_10)).setPresentItem(getDJ())
						.setItemsReward(new ItemStack(Material.DIAMOND, 32), new ItemStack(Material.LAPIS_BLOCK, 8))
						.setMoneyReward(750).setXpReward(250));
		id++;

		addChallenge(new Challenge(ChallengeType.ADVANCED, id, "Voleur de PNJ",
				Arrays.asList("Avoir §2x64§7 emeraudes"), new ItemStack(Material.EMERALD)).setPresentItem(getDJ())
						.setItemsReward(ItemBuilder.getBag()).setMoneyReward(750).setXpReward(250));
		id++;

		addChallenge(new Challenge(ChallengeType.ADVANCED, id, "La laine c'est cool",
				Arrays.asList("Avoir §2x32§7 de chaque laine"), new ItemStack(Material.WOOL))
						.setPresentItem(getColors(Material.WOOL, 32))
						.setItemsReward(new ItemStack(Material.DIAMOND_BLOCK, 2)).setMoneyReward(1000)
						.setXpReward(1000));
		id++;

		addChallenge(new Challenge(ChallengeType.ADVANCED, id, "L'art de gîle",
				Arrays.asList("Avoir §2x32§7 de chaque argile teinté"), new ItemStack(Material.STAINED_CLAY))
						.setPresentItem(getColors(Material.STAINED_CLAY, 32)).setItemsReward(getFlower(8))
						.setMoneyReward(1000).setXpReward(1000));
		id++;

		addChallenge(new Challenge(ChallengeType.ADVANCED, id, "Art et fênetre",
				Arrays.asList("Avoir §2x32§7 de chaque verre"), new ItemStack(Material.STAINED_GLASS))
						.setPresentItem(getColors(Material.STAINED_GLASS, 32)).setItemsReward(getFlower(8))
						.setMoneyReward(1000).setXpReward(1000));
		id++;

		addChallenge(new Challenge(ChallengeType.ADVANCED, id, "Art et fênetre le retour",
				Arrays.asList("Avoir §2x32§7 de chaque vitre"), new ItemStack(Material.STAINED_GLASS_PANE))
						.setPresentItem(getColors(Material.STAINED_GLASS_PANE, 32)).setItemsReward(getFlower(8))
						.setMoneyReward(1000).setXpReward(1000));
		id++;

		addChallenge(new Challenge(ChallengeType.ADVANCED, id, "Alchemist",
				Arrays.asList("Avoir une potion de resistance au feu", "une potion de lenteur", "une potion de soin",
						"une potion de dégât", "une potion d'invisibilité", "une potion force",
						"et une potion de regénération"),
				new ItemStack(Material.POTION)).setPresentItem(getPotions())
						.setItemsReward(new ItemStack(Material.ENDER_STONE, 1)).setMoneyReward(750).setXpReward(250));
		id++;

		addChallenge(new Challenge(ChallengeType.ADVANCED, id, "Mangeur",
				Arrays.asList("Avoir §2x64§7 de chaque nourriture (sauf les pommes d'or)"),
				new ItemStack(Material.COOKED_BEEF)).setPresentItem(getFood()).setItemsReward(ItemBuilder.getHopper())
						.setMoneyReward(750).setXpReward(250));
		id++;

		addChallenge(new Challenge(ChallengeType.ADVANCED, id, "Villagoies", Arrays.asList("Être à coté de §210§7 PNJ"),
				new ItemStack(Material.EMERALD))
						.setManuelVerif(player -> nearbyEntities(player, EntityType.VILLAGER, 10))
						.setItemsReward(new ItemStack(Material.HOPPER)).setMoneyReward(750).setXpReward(250));
		id++;

		addChallenge(new Challenge(ChallengeType.ADVANCED, id, "Zombie",
				Arrays.asList("Avoir §x1024§7 viande de zombie"), new ItemStack(Material.ROTTEN_FLESH))
						.setPresentItem(new ItemStack(Material.ROTTEN_FLESH, 64),
								new ItemStack(Material.ROTTEN_FLESH, 64), new ItemStack(Material.ROTTEN_FLESH, 64),
								new ItemStack(Material.ROTTEN_FLESH, 64), new ItemStack(Material.ROTTEN_FLESH, 64),
								new ItemStack(Material.ROTTEN_FLESH, 64), new ItemStack(Material.ROTTEN_FLESH, 64),
								new ItemStack(Material.ROTTEN_FLESH, 64), new ItemStack(Material.ROTTEN_FLESH, 64),
								new ItemStack(Material.ROTTEN_FLESH, 64), new ItemStack(Material.ROTTEN_FLESH, 64),
								new ItemStack(Material.ROTTEN_FLESH, 64), new ItemStack(Material.ROTTEN_FLESH, 64),
								new ItemStack(Material.ROTTEN_FLESH, 64), new ItemStack(Material.ROTTEN_FLESH, 64),
								new ItemStack(Material.ROTTEN_FLESH, 64))
						.setItemsReward(new ItemStack(Material.MONSTER_EGG, 4, (byte) 58)).setMoneyReward(750)
						.setXpReward(50));
		id++;
		addChallenge(new Challenge(ChallengeType.ADVANCED, id, "Skeleton", Arrays.asList("Avoir §x1024§7 os"),
				new ItemStack(Material.BONE))
						.setPresentItem(new ItemStack(Material.BONE, 64), new ItemStack(Material.BONE, 64),
								new ItemStack(Material.BONE, 64), new ItemStack(Material.BONE, 64),
								new ItemStack(Material.BONE, 64), new ItemStack(Material.BONE, 64),
								new ItemStack(Material.BONE, 64), new ItemStack(Material.BONE, 64),
								new ItemStack(Material.BONE, 64), new ItemStack(Material.BONE, 64),
								new ItemStack(Material.BONE, 64), new ItemStack(Material.BONE, 64),
								new ItemStack(Material.BONE, 64), new ItemStack(Material.BONE, 64),
								new ItemStack(Material.BONE, 64), new ItemStack(Material.BONE, 64))
						.setItemsReward(new ItemStack(Material.MONSTER_EGG, 4, (byte) 61)).setMoneyReward(750)
						.setXpReward(50));
		id++;
		addChallenge(new Challenge(ChallengeType.ADVANCED, id, "Spider",
				Arrays.asList("Avoir §x1024§7 toile d'araignée"), new ItemStack(Material.STRING))
						.setPresentItem(new ItemStack(Material.STRING, 64), new ItemStack(Material.STRING, 64),
								new ItemStack(Material.STRING, 64), new ItemStack(Material.STRING, 64),
								new ItemStack(Material.STRING, 64), new ItemStack(Material.STRING, 64),
								new ItemStack(Material.STRING, 64), new ItemStack(Material.STRING, 64),
								new ItemStack(Material.STRING, 64), new ItemStack(Material.STRING, 64),
								new ItemStack(Material.STRING, 64), new ItemStack(Material.STRING, 64),
								new ItemStack(Material.STRING, 64), new ItemStack(Material.STRING, 64),
								new ItemStack(Material.STRING, 64), new ItemStack(Material.STRING, 64))
						.setItemsReward(new ItemStack(Material.MONSTER_EGG, 4, (byte) 66)).setMoneyReward(750)
						.setXpReward(50));
		id++;
		addChallenge(new Challenge(ChallengeType.ADVANCED, id, "Creeper",
				Arrays.asList("Avoir §x1024§7 poudre de creeper"), new ItemStack(Material.SULPHUR))
						.setPresentItem(new ItemStack(Material.SULPHUR, 64), new ItemStack(Material.SULPHUR, 64),
								new ItemStack(Material.SULPHUR, 64), new ItemStack(Material.SULPHUR, 64),
								new ItemStack(Material.SULPHUR, 64), new ItemStack(Material.SULPHUR, 64),
								new ItemStack(Material.SULPHUR, 64), new ItemStack(Material.SULPHUR, 64),
								new ItemStack(Material.SULPHUR, 64), new ItemStack(Material.SULPHUR, 64),
								new ItemStack(Material.SULPHUR, 64), new ItemStack(Material.SULPHUR, 64),
								new ItemStack(Material.SULPHUR, 64), new ItemStack(Material.SULPHUR, 64),
								new ItemStack(Material.SULPHUR, 64), new ItemStack(Material.SULPHUR, 64))
						.setItemsReward(new ItemStack(Material.MONSTER_EGG, 4, (byte) 68)).setMoneyReward(750)
						.setXpReward(50));
		id++;
		addChallenge(new Challenge(ChallengeType.ADVANCED, id, "Slime", Arrays.asList("Avoir §x1024§7 boule de slime"),
				new ItemStack(Material.SLIME_BALL))
						.setPresentItem(new ItemStack(Material.SLIME_BALL, 64), new ItemStack(Material.SLIME_BALL, 64),
								new ItemStack(Material.SLIME_BALL, 64), new ItemStack(Material.SLIME_BALL, 64),
								new ItemStack(Material.SLIME_BALL, 64), new ItemStack(Material.SLIME_BALL, 64),
								new ItemStack(Material.SLIME_BALL, 64), new ItemStack(Material.SLIME_BALL, 64),
								new ItemStack(Material.SLIME_BALL, 64), new ItemStack(Material.SLIME_BALL, 64),
								new ItemStack(Material.SLIME_BALL, 64), new ItemStack(Material.SLIME_BALL, 64),
								new ItemStack(Material.SLIME_BALL, 64), new ItemStack(Material.SLIME_BALL, 64),
								new ItemStack(Material.SLIME_BALL, 64), new ItemStack(Material.SLIME_BALL, 64))
						.setItemsReward(new ItemStack(Material.MONSTER_EGG, 4, (byte) 120)).setMoneyReward(750)
						.setXpReward(50));
		id++;

		/**
		 * Elite
		 */

		addChallenge(new Challenge(ChallengeType.ELITE, id, "Constructeur",
				Arrays.asList("Avoir un niveau d'île supérieur ou égal à §22000§7."), new ItemStack(Material.WOOD))
						.setManuelVerif(
								player -> ASkyBlockAPI.getInstance().getLongIslandLevel(player.getUniqueId()) >= 2000)
						.setItemsReward(new ItemStack(Material.GRASS, 2304)).setMoneyReward(10000).setXpReward(250));
		id++;

		addChallenge(new Challenge(ChallengeType.ELITE, id, "La laine c'est cool",
				Arrays.asList("Avoir §2x64§7 de chaque laine"), new ItemStack(Material.WOOL))
						.setPresentItem(getColors(Material.WOOL, 64))
						.setItemsReward(new ItemStack(Material.DIAMOND_BLOCK, 8)).setMoneyReward(1000)
						.setXpReward(2500));
		id++;

		addChallenge(new Challenge(ChallengeType.ELITE, id, "L'art de gîle",
				Arrays.asList("Avoir §2x64§7 de chaque argile teinté"), new ItemStack(Material.STAINED_CLAY))
						.setPresentItem(getColors(Material.STAINED_CLAY, 64)).setItemsReward(getFlower(32))
						.setMoneyReward(2500).setXpReward(1000));
		id++;

		addChallenge(
				new Challenge(ChallengeType.ELITE, id, "Art et fênetre", Arrays.asList("Avoir §2x64§7 de chaque verre"),
						new ItemStack(Material.STAINED_GLASS)).setPresentItem(getColors(Material.STAINED_GLASS, 64))
								.setItemsReward(getFlower(32)).setMoneyReward(2500).setXpReward(1000));
		id++;

		addChallenge(new Challenge(ChallengeType.ELITE, id, "Art et fênetre le retour",
				Arrays.asList("Avoir §2x64§7 de chaque vitre"), new ItemStack(Material.STAINED_GLASS_PANE))
						.setPresentItem(getColors(Material.STAINED_GLASS_PANE, 64)).setItemsReward(getFlower(32))
						.setMoneyReward(1000).setXpReward(1000));
		id++;

		addChallenge(new Challenge(ChallengeType.ELITE, id, "Vendeur de tapis",
				Arrays.asList("Avoir §2x64§7 de chaque tapis"), new ItemStack(Material.CARPET))
						.setPresentItem(getColors(Material.CARPET, 64)).setItemsReward(getFlower(32))
						.setMoneyReward(2500).setXpReward(1000));
		id++;

		addChallenge(new Challenge(ChallengeType.ELITE, id, "J'ADORE QUAND ÇA EXPLOSE VRAIMENT",
				Arrays.asList("Créer §2x512§7 tnt"), new ItemStack(Material.TNT))
						.setPresentItem(new ItemStack(Material.TNT, 64), new ItemStack(Material.TNT, 64),
								new ItemStack(Material.TNT, 64), new ItemStack(Material.TNT, 64),
								new ItemStack(Material.TNT, 64), new ItemStack(Material.TNT, 64),
								new ItemStack(Material.TNT, 64), new ItemStack(Material.TNT, 64))
						.setItemsReward(new ItemStack(Material.SAND, 256), new ItemStack(Material.SAND, 256, (byte) 1))
						.setMoneyReward(2500).setXpReward(500));
		id++;

		addChallenge(new Challenge(ChallengeType.ELITE, id, "Etoile dans les yeux",
				Arrays.asList("Avoir §2x64§7 nether star", "Permet d'obtenir le grade Elite"),
				new ItemStack(Material.NETHER_STAR)).setPresentItem(getColors(Material.NETHER_STAR, 64))
						.setConsumerReward(player -> {
							Bukkit.dispatchCommand(Bukkit.getConsoleSender(),
									"pex user " + player.getName() + " group set elite");
						}));
		id++;

		addChallenge(new Challenge(ChallengeType.ELITE, id, "C'est la fête !",
				Arrays.asList("Avoir §2x5§7 joueurs à moins de 10 blocs"), new ItemStack(Material.SKULL_ITEM))
						.setManuelVerif(player -> nearbyEntities(player, EntityType.PLAYER, 5))
						.setItemsReward(ItemBuilder.getCreatedSkullPlayerWithLoreAndName("Maxlego08", null, null))
						.setMoneyReward(2500).setXpReward(1000));
		id++;
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
