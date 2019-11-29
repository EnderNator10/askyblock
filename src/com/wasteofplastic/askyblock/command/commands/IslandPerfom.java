package com.wasteofplastic.askyblock.command.commands;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Biome;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionType;

import com.wasteofplastic.askyblock.ASkyBlock;
import com.wasteofplastic.askyblock.CoopPlay;
import com.wasteofplastic.askyblock.DeleteIslandChunk;
import com.wasteofplastic.askyblock.Island;
import com.wasteofplastic.askyblock.LevelCalcByChunk;
import com.wasteofplastic.askyblock.Settings;
import com.wasteofplastic.askyblock.events.IslandJoinEvent;
import com.wasteofplastic.askyblock.events.IslandLeaveEvent;
import com.wasteofplastic.askyblock.events.IslandNewEvent;
import com.wasteofplastic.askyblock.events.IslandPreDeleteEvent;
import com.wasteofplastic.askyblock.events.IslandResetEvent;
import com.wasteofplastic.askyblock.schematics.Schematic;
import com.wasteofplastic.askyblock.schematics.Schematic.PasteReason;
import com.wasteofplastic.askyblock.util.Util;
import com.wasteofplastic.askyblock.util.VaultHelper;

@SuppressWarnings("deprecation")
public class IslandPerfom {

	public final HashMap<UUID, UUID> inviteList = new HashMap<>();
	public final HashMap<UUID, UUID> coopInviteList = new HashMap<>();
	private HashMap<UUID, Long> resetWaitTime = new HashMap<>();
	public HashMap<UUID, Boolean> confirm = new HashMap<>();
	
	public Set<UUID> pendingNewIslandSelection = new HashSet<UUID>();
	public Set<UUID> resettingIsland = new HashSet<UUID>();
	
	private Location last = null;
	private HashMap<UUID, Location> islandSpot = new HashMap<>();
	
	private HashMap<UUID, Long> levelWaitTime = new HashMap<>();
	
	private static IslandPerfom instance;
	private final HashMap<String, Schematic> schematics = new HashMap<>();
	private final ASkyBlock plugin = ASkyBlock.getPlugin();

	private IslandPerfom() {
	}

	public static IslandPerfom getInstance() {
		if (instance == null) {
			instance = new IslandPerfom();
			instance.loadSchematics();
		}
		return instance;
	}

	
	
	/**
	 * @return the schematics
	 */
	public HashMap<String, Schematic> getSchematics() {
		return schematics;
	}

	/**
	 * Loads schematics from the config.yml file. If the default island is not
	 * included, it will be made up
	 */
	public void loadSchematics() {
		// Check if there is a schematic folder and make it if it does not exist
		File schematicFolder = new File(plugin.getDataFolder(), "schematics");
		if (!schematicFolder.exists()) {
			schematicFolder.mkdir();
		}
		// Clear the schematic list that is kept in memory
		schematics.clear();
		// Load the default schematic if it exists
		// Set up the default schematic
		File schematicFile = new File(schematicFolder, "island.schematic");
		File netherFile = new File(schematicFolder, "nether.schematic");
		if (!schematicFile.exists()) {
			// plugin.getLogger().info("Default schematic does not exist...");
			// Only copy if the default exists
			if (plugin.getResource("schematics/island.schematic") != null) {
				plugin.getLogger().info("Default schematic does not exist, saving it...");
				plugin.saveResource("schematics/island.schematic", false);
				// Add it to schematics
				try {
					schematics.put("default", new Schematic(plugin, schematicFile));
				} catch (IOException e) {
					plugin.getLogger().severe("Could not load default schematic!");
					e.printStackTrace();
				}
				// If this is repeated later due to the schematic config, fine,
				// it will only add info
			} else {
				// No islands.schematic in the jar, so just make the default
				// using
				// built-in island generation
				schematics.put("default", new Schematic(plugin));
			}
			plugin.getLogger().info("Loaded default nether schematic");
		} else {
			// It exists, so load it
			try {
				schematics.put("default", new Schematic(plugin, schematicFile));
				plugin.getLogger().info("Loaded default island schematic.");
			} catch (IOException e) {
				plugin.getLogger().severe("Could not load default schematic!");
				e.printStackTrace();
			}
		}
		// Add the nether default too
		if (!netherFile.exists()) {
			if (plugin.getResource("schematics/nether.schematic") != null) {
				plugin.saveResource("schematics/nether.schematic", false);

				// Add it to schematics
				try {
					Schematic netherIsland = new Schematic(plugin, netherFile);
					netherIsland.setVisible(false);
					schematics.put("nether", netherIsland);
					plugin.getLogger().info("Loaded default nether schematic.");
				} catch (IOException e) {
					plugin.getLogger().severe("Could not load default nether schematic!");
					e.printStackTrace();
				}
			} else {
				plugin.getLogger().severe("Could not find default nether schematic!");
			}
		} else {
			// It exists, so load it
			try {
				Schematic netherIsland = new Schematic(plugin, netherFile);
				netherIsland.setVisible(false);
				schematics.put("nether", netherIsland);
				plugin.getLogger().info("Loaded default nether schematic.");
			} catch (IOException e) {
				plugin.getLogger().severe("Could not load default nether schematic!");
				e.printStackTrace();
			}
		}
		// Set up some basic settings just in case the schematics section is
		// missing
		if (schematics.containsKey("default")) {
			schematics.get("default").setIcon(Material.GRASS);
			schematics.get("default").setOrder(1);
			schematics.get("default").setName("The Original");
			schematics.get("default").setDescription("");
			schematics.get("default").setPartnerName("nether");
			schematics.get("default").setBiome(Settings.defaultBiome);
			schematics.get("default").setIcon(Material.GRASS);
			if (Settings.chestItems.length == 0) {
				schematics.get("default").setUseDefaultChest(false);
			} else {
				schematics.get("default").setUseDefaultChest(true);
			}
		}
		if (schematics.containsKey("nether")) {
			schematics.get("nether").setName("NetherBlock Island");
			schematics.get("nether").setDescription("Nether Island");
			schematics.get("nether").setPartnerName("default");
			schematics.get("nether").setBiome(Biome.HELL);
			schematics.get("nether").setIcon(Material.NETHERRACK);
			schematics.get("nether").setVisible(false);
			schematics.get("nether").setPasteEntities(true);
			if (Settings.chestItems.length == 0) {
				schematics.get("nether").setUseDefaultChest(false);
			}
		}

		// Load the schematics from config.yml
		ConfigurationSection schemSection = plugin.getConfig().getConfigurationSection("schematicsection");
		if (plugin.getConfig().contains("schematicsection")) {
			Settings.useSchematicPanel = schemSection.getBoolean("useschematicspanel", false);
			Settings.chooseIslandRandomly = schemSection.getBoolean("chooseislandrandomly", false);
			ConfigurationSection schematicsSection = schemSection.getConfigurationSection("schematics");
			// Section exists, so go through the various sections
			for (String key : schematicsSection.getKeys(false)) {
				try {
					Schematic newSchem = null;
					// Check the file exists
					// plugin.getLogger().info("DEBUG: schematics." + key +
					// ".filename" );
					String filename = schemSection.getString("schematics." + key + ".filename", "");
					if (!filename.isEmpty()) {
						// plugin.getLogger().info("DEBUG: filename = " +
						// filename);
						// Check if this file exists or if it is in the jar
						schematicFile = new File(schematicFolder, filename);
						// See if the file exists
						if (schematicFile.exists()) {
							newSchem = new Schematic(plugin, schematicFile);
						} else if (plugin.getResource("schematics/" + filename) != null) {
							plugin.saveResource("schematics/" + filename, false);
							newSchem = new Schematic(plugin, schematicFile);
						}
					} else {
						// plugin.getLogger().info("DEBUG: filename is empty");
						if (key.equalsIgnoreCase("default")) {
							// Øplugin.getLogger().info("DEBUG: key is default,
							// so use this one");
							newSchem = schematics.get("default");
						} else {
							plugin.getLogger().severe("Schematic " + key + " does not have a filename. Skipping!");
						}
					}
					if (newSchem != null) {
						// Set the heading
						newSchem.setHeading(key);
						// Order
						newSchem.setOrder(schemSection.getInt("schematics." + key + ".order", 0));
						// Load the rest of the settings
						// Icon
						try {

							Material icon;
							String iconString = schemSection.getString("schematics." + key + ".icon", "MAP")
									.toUpperCase();
							// Support damage values
							String[] split = iconString.split(":");
							if (StringUtils.isNumeric(split[0])) {
								icon = Material.getMaterial(Integer.parseInt(split[0]));
								if (icon == null) {
									icon = Material.MAP;
									plugin.getLogger().severe(
											"Schematic's icon could not be found. Try using quotes like '17:2'");
								}
							} else {
								icon = Material.valueOf(split[0]);
							}
							int damage = 0;
							if (split.length == 2) {
								if (StringUtils.isNumeric(split[1])) {
									damage = Integer.parseInt(split[1]);
								}
							}
							newSchem.setIcon(icon, damage);
						} catch (Exception e) {
							// e.printStackTrace();
							newSchem.setIcon(Material.MAP);
						}
						// Friendly name
						String name = ChatColor.translateAlternateColorCodes('&',
								schemSection.getString("schematics." + key + ".name", ""));
						newSchem.setName(name);
						// Rating - Rating is not used right now
						int rating = schemSection.getInt("schematics." + key + ".rating", 50);
						if (rating < 1) {
							rating = 1;
						} else if (rating > 100) {
							rating = 100;
						}
						newSchem.setRating(rating);
						// Cost
						double cost = schemSection.getDouble("schematics." + key + ".cost", 0D);
						if (cost < 0) {
							cost = 0;
						}
						newSchem.setCost(cost);
						// Description
						String description = ChatColor.translateAlternateColorCodes('&',
								schemSection.getString("schematics." + key + ".description", ""));
						description = description.replace("[rating]", String.valueOf(rating));
						if (Settings.useEconomy) {
							description = description.replace("[cost]", String.valueOf(cost));
						}
						newSchem.setDescription(description);
						// Permission
						String perm = schemSection.getString("schematics." + key + ".permission", "");
						newSchem.setPerm(perm);
						// Use default chest
						newSchem.setUseDefaultChest(
								schemSection.getBoolean("schematics." + key + ".useDefaultChest", true));
						// Biomes - overrides default if it exists
						String biomeString = schemSection.getString("schematics." + key + ".biome",
								Settings.defaultBiome.toString());
						Biome biome = null;
						try {
							biome = Biome.valueOf(biomeString);
							newSchem.setBiome(biome);
						} catch (Exception e) {
							plugin.getLogger()
									.severe("Could not parse biome " + biomeString + " using default instead.");
						}
						// Use physics - overrides default if it exists
						newSchem.setUsePhysics(
								schemSection.getBoolean("schematics." + key + ".usephysics", Settings.usePhysics));
						// Paste Entities or not
						newSchem.setPasteEntities(
								schemSection.getBoolean("schematics." + key + ".pasteentities", false));
						// Paste air or not.
						newSchem.setPasteAir(schemSection.getBoolean("schematics." + key + ".pasteair", true));
						// Visible in GUI or not
						newSchem.setVisible(schemSection.getBoolean("schematics." + key + ".show", true));
						// Partner schematic
						if (biome != null && biome.equals(Biome.HELL)) {
							// Default for nether biomes is the default
							// overworld island
							newSchem.setPartnerName(
									schemSection.getString("schematics." + key + ".partnerSchematic", "default"));
						} else {
							// Default for overworld biomes is nether island
							newSchem.setPartnerName(
									schemSection.getString("schematics." + key + ".partnerSchematic", "nether"));
						}
						// Island companion
						List<String> companion = schemSection.getStringList("schematics." + key + ".companion");
						List<EntityType> companionTypes = new ArrayList<EntityType>();
						if (!companion.isEmpty()) {
							for (String companionType : companion) {
								companionType = companionType.toUpperCase();
								if (companionType.equalsIgnoreCase("NOTHING")) {
									companionTypes.add(null);
								} else {
									try {
										EntityType type = EntityType.valueOf(companionType);
										companionTypes.add(type);
									} catch (Exception e) {
										plugin.getLogger().warning(
												"Island companion is not recognized in schematic '" + name + "'.");
									}
								}
							}
							newSchem.setIslandCompanion(companionTypes);
						}
						// Companion names
						List<String> companionNames = schemSection
								.getStringList("schematics." + key + ".companionnames");
						if (!companionNames.isEmpty()) {
							List<String> names = new ArrayList<String>();
							for (String companionName : companionNames) {
								names.add(ChatColor.translateAlternateColorCodes('&', companionName));
							}
							newSchem.setCompanionNames(names);
						}
						// Get chest items
						final List<String> chestItems = schemSection.getStringList("schematics." + key + ".chestItems");
						if (!chestItems.isEmpty()) {
							ItemStack[] tempChest = new ItemStack[chestItems.size()];
							int i = 0;
							for (String chestItemString : chestItems) {
								// plugin.getLogger().info("DEBUG: chest item =
								// " + chestItemString);
								try {
									String[] amountdata = chestItemString.split(":");
									if (amountdata[0].equals("POTION")) {
										if (amountdata.length == 3) {
											Potion chestPotion = new Potion(PotionType.valueOf(amountdata[1]));
											tempChest[i++] = chestPotion.toItemStack(Integer.parseInt(amountdata[2]));
										} else if (amountdata.length == 4) {
											// Extended or splash potions
											if (amountdata[2].equals("EXTENDED")) {
												Potion chestPotion = new Potion(PotionType.valueOf(amountdata[1]))
														.extend();
												tempChest[i++] = chestPotion
														.toItemStack(Integer.parseInt(amountdata[3]));
											} else if (amountdata[2].equals("SPLASH")) {
												Potion chestPotion = new Potion(PotionType.valueOf(amountdata[1]))
														.splash();
												tempChest[i++] = chestPotion
														.toItemStack(Integer.parseInt(amountdata[3]));
											} else if (amountdata[2].equals("EXTENDEDSPLASH")) {
												Potion chestPotion = new Potion(PotionType.valueOf(amountdata[1]))
														.extend().splash();
												tempChest[i++] = chestPotion
														.toItemStack(Integer.parseInt(amountdata[3]));
											}
										}
									} else {
										Material mat;
										if (StringUtils.isNumeric(amountdata[0])) {
											mat = Material.getMaterial(Integer.parseInt(amountdata[0]));
										} else {
											mat = Material.getMaterial(amountdata[0].toUpperCase());
										}
										if (amountdata.length == 2) {
											tempChest[i++] = new ItemStack(mat, Integer.parseInt(amountdata[1]));
										} else if (amountdata.length == 3) {
											tempChest[i++] = new ItemStack(mat, Integer.parseInt(amountdata[2]),
													Short.parseShort(amountdata[1]));
										}
									}
								} catch (java.lang.IllegalArgumentException ex) {
									plugin.getLogger().severe("Problem loading chest item for schematic '" + name
											+ "' so skipping it: " + chestItemString);
									plugin.getLogger().severe("Error is : " + ex.getMessage());
									plugin.getLogger().info("Potential potion types are: ");
									for (PotionType c : PotionType.values())
										plugin.getLogger().info(c.name());
								} catch (Exception e) {
									plugin.getLogger().severe("Problem loading chest item for schematic '" + name
											+ "' so skipping it: " + chestItemString);
									plugin.getLogger().info("Potential material types are: ");
									for (Material c : Material.values())
										plugin.getLogger().info(c.name());
									// e.printStackTrace();
								}
							}

							// Store it
							newSchem.setDefaultChestItems(tempChest);
						}
						// Player spawn block
						String spawnBlock = schemSection.getString("schematics." + key + ".spawnblock");
						if (spawnBlock != null) {
							// Check to see if this block is a valid material
							try {
								Material playerSpawnBlock;
								if (StringUtils.isNumeric(spawnBlock)) {
									playerSpawnBlock = Material.getMaterial(Integer.parseInt(spawnBlock));
								} else {
									playerSpawnBlock = Material.valueOf(spawnBlock.toUpperCase());
								}
								if (newSchem.setPlayerSpawnBlock(playerSpawnBlock)) {
									plugin.getLogger().info("Player will spawn at the " + playerSpawnBlock.toString());
								} else {
									plugin.getLogger().severe("Problem with schematic '" + name + "'. Spawn block '"
											+ spawnBlock
											+ "' not found in schematic or there is more than one. Skipping...");
								}
							} catch (Exception e) {
								plugin.getLogger().severe("Problem with schematic '" + name + "'. Spawn block '"
										+ spawnBlock + "' is unknown. Skipping...");
							}
						} else {
							// plugin.getLogger().info("No spawn block found");
						}
						// Level handicap
						newSchem.setLevelHandicap(schemSection.getInt("schematics." + key + ".levelHandicap", 0));

						// Store it
						schematics.put(key, newSchem);
						if (perm.isEmpty()) {
							perm = "all players";
						} else {
							perm = "player with " + perm + " permission";
						}
						plugin.getLogger().info("Loading schematic " + ChatColor.stripColor(name) + " (" + filename
								+ ") for " + perm + ", order " + newSchem.getOrder());
					} else {
						plugin.getLogger()
								.warning("Could not find " + filename + " in the schematics folder! Skipping...");
					}
				} catch (IOException e) {
					plugin.getLogger().info("Error loading schematic in section " + key + ". Skipping...");
				}
			}
			if (schematics.isEmpty()) {
				tip();
			}
		}
	}

	private void tip() {
		// There is no section in config.yml. Save the default schematic anyway
		plugin.getLogger().warning("***************************************************************");
		plugin.getLogger().warning("* 'schematics' section in config.yml has been deprecated.     *");
		plugin.getLogger().warning("* See 'schematicsection' in config.new.yml for replacement.   *");
		plugin.getLogger().warning("***************************************************************");
	}

	public void chooseIsland(Player player) {
		List<Schematic> schems = getSchematics(player, false);
		ASkyBlock.getPlugin().getInventoryManager().createInventory(6, player, 1, this, schems);

	}

	/**
	 * List schematics this player can access. If @param ignoreNoPermission is
	 * true, then only schematics with a specific permission set will be
	 * checked. I.e., no common schematics will be returned (including the
	 * default one).
	 * 
	 * @param player
	 * @param ignoreNoPermission
	 * @return List of schematics this player can use based on their permission
	 *         level
	 */
	public List<Schematic> getSchematics(Player player, boolean ignoreNoPermission) {
		List<Schematic> result = new ArrayList<Schematic>();
		// Find out what schematics this player can choose from
		for (Schematic schematic : schematics.values())
			if ((!ignoreNoPermission && schematic.getPerm().isEmpty())
					|| VaultHelper.checkPerm(player, schematic.getPerm()))
				// Only add if it's visible
				if (schematic.isVisible())
					// Check if it's a nether island, but the nether is not
					// enables
					if (schematic.getBiome().equals(Biome.HELL)) {
						if (Settings.createNether && Settings.newNether && ASkyBlock.getNetherWorld() != null)
							result.add(schematic);
					} else
						result.add(schematic);
		// Sort according to order
		Collections.sort(result, new Comparator<Schematic>() {
			@Override
			public int compare(Schematic o1, Schematic o2) {
				return ((o2.getOrder() < o1.getOrder()) ? 1 : -1);
			}

		});
		return result;
	}

	/**
	 * Removes a player from a team run by teamleader
	 *
	 * @param playerUUID
	 *            - the player's UUID
	 * @param teamLeader
	 * @param makeLeader
	 *            - true if this is the result of switching leader
	 * @return true if successful, false if not
	 */
	public boolean removePlayerFromTeam(final UUID playerUUID, final UUID teamLeader, boolean makeLeader) {
		// Remove player from the team
		plugin.getPlayers().removeMember(teamLeader, playerUUID);
		// If player is online
		// If player is not the leader of their own team
		if (teamLeader == null || !playerUUID.equals(teamLeader)) {
			if (!plugin.getPlayers().setLeaveTeam(playerUUID)) {
				return false;
			}
			// plugin.getPlayers().setHomeLocation(player, null);
			plugin.getPlayers().clearHomeLocations(playerUUID);
			plugin.getPlayers().setIslandLocation(playerUUID, null);
			plugin.getPlayers().setTeamIslandLocation(playerUUID, null);
			if (!makeLeader) {
				OfflinePlayer offlinePlayer = plugin.getServer().getOfflinePlayer(playerUUID);
				if (offlinePlayer.isOnline()) {
					// Check perms
					if (!((Player) offlinePlayer).hasPermission(Settings.PERMPREFIX + "command.leaveexempt")) {
						runCommands(Settings.leaveCommands, offlinePlayer);
					}
				} else {
					// If offline, all commands are run, sorry
					runCommands(Settings.leaveCommands, offlinePlayer);
				}
				// Deduct a reset
				if (Settings.leaversLoseReset && Settings.resetLimit >= 0) {
					int resetsLeft = plugin.getPlayers().getResetsLeft(playerUUID);
					if (resetsLeft > 0) {
						resetsLeft--;
						plugin.getPlayers().setResetsLeft(playerUUID, resetsLeft);
					}
				}
			}
			// Fire event
			if (teamLeader != null) {
				final Island island = plugin.getGrid().getIsland(teamLeader);
				final IslandLeaveEvent event = new IslandLeaveEvent(playerUUID, island);
				plugin.getServer().getPluginManager().callEvent(event);
			}
		} else {
			// Ex-Leaders keeps their island, but the rest of the team members
			// are
			// removed
			if (!plugin.getPlayers().setLeaveTeam(playerUUID)) {
				// Event was cancelled for some reason
				return false;
			}
		}
		return true;
	}

	/**
	 * Runs commands when a player resets or leaves a team, etc. Can be run for
	 * offline players
	 *
	 * @param commands
	 * @param offlinePlayer
	 */
	public void runCommands(List<String> commands, OfflinePlayer offlinePlayer) {
		// Run commands
		for (String cmd : commands) {
			if (cmd.startsWith("[SELF]")) {
				cmd = cmd.substring(6, cmd.length()).replace("[player]", offlinePlayer.getName()).trim();
				if (offlinePlayer.isOnline()) {
					try {
						Bukkit.getLogger().info("Running command '" + cmd + "' as " + offlinePlayer.getName());
						((Player) offlinePlayer).performCommand(cmd);
					} catch (Exception e) {
						Bukkit.getLogger().severe("Problem executing island command executed by player - skipping!");
						Bukkit.getLogger().severe("Command was : " + cmd);
						Bukkit.getLogger().severe("Error was: " + e.getMessage());
						e.printStackTrace();
					}
				}
				continue;
			}
			// Substitute in any references to player
			try {
				// plugin.getLogger().info("Running command " + cmd + " as
				// console.");
				if (!Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(),
						cmd.replace("[player]", offlinePlayer.getName()))) {
					Bukkit.getLogger().severe("Problem executing island command - skipping!");
					Bukkit.getLogger().severe("Command was : " + cmd);
				}
			} catch (Exception e) {
				Bukkit.getLogger().severe("Problem executing island command - skipping!");
				Bukkit.getLogger().severe("Command was : " + cmd);
				Bukkit.getLogger().severe("Error was: " + e.getMessage());
				e.printStackTrace();
			}
		}

	}

	/**
	 * Adds a player to a team. The player and the teamleader MAY be the same
	 *
	 * @param playerUUID
	 *            - the player's UUID
	 * @param teamLeader
	 * @return true if the player is successfully added
	 */
	public boolean addPlayertoTeam(final UUID playerUUID, final UUID teamLeader) {
		// Set the player's team giving the team leader's name and the team's
		// island
		// location
		if (!plugin.getPlayers().setJoinTeam(playerUUID, teamLeader,
				plugin.getPlayers().getIslandLocation(teamLeader))) {
			return false;
		}
		// If the player's name and the team leader are NOT the same when this
		// method is called then set the player's home location to the leader's
		// home location
		// if it exists, and if not set to the island location
		if (!playerUUID.equals(teamLeader)) {
			// Clear any old home locations
			plugin.getPlayers().clearHomeLocations(playerUUID);
			// Set homes and spawn point home locations if they exist
			for (Entry<Integer, Location> homes : plugin.getPlayers().getHomeLocations(teamLeader).entrySet()) {
				if (homes.getKey() < 2) {
					plugin.getPlayers().setHomeLocation(playerUUID, homes.getValue(), homes.getKey());
				}
			}
			if (plugin.getPlayers().getHomeLocation(teamLeader, 1) == null) {
				plugin.getPlayers().setHomeLocation(playerUUID, plugin.getPlayers().getIslandLocation(teamLeader));
				// plugin.getLogger().info("DEBUG: Setting player's home to the
				// team island location");
			}

			// If the leader's member list does not contain player then add it
			if (!plugin.getPlayers().getMembers(teamLeader).contains(playerUUID)) {
				plugin.getPlayers().addTeamMember(teamLeader, playerUUID);
			}
			// If the leader's member list does not contain their own name then
			// add it
			if (!plugin.getPlayers().getMembers(teamLeader).contains(teamLeader)) {
				plugin.getPlayers().addTeamMember(teamLeader, teamLeader);
			}
			// Fire event
			final Island island = plugin.getGrid().getIsland(teamLeader);
			final IslandJoinEvent event = new IslandJoinEvent(playerUUID, island);
			plugin.getServer().getPluginManager().callEvent(event);
		}
		return true;
	}


	/**
	 * Check time out for island restarting
	 *
	 * @param player
	 * @return true if the timeout is over
	 */
	public boolean onRestartWaitTime(final Player player) {
		if (resetWaitTime.containsKey(player.getUniqueId())) {
			if (resetWaitTime.get(player.getUniqueId()).longValue() > Calendar.getInstance().getTimeInMillis()) {
				return true;
			}

			return false;
		}

		return false;
	}
	
	/**
	 * Sets a timeout for player into the Hashmap resetWaitTime
	 *
	 * @param player
	 */
	public void setResetWaitTime(final Player player) {
		resetWaitTime.put(player.getUniqueId(),
				Long.valueOf(Calendar.getInstance().getTimeInMillis() + Settings.resetWait * 1000));
	}
	

	/**
	 * Returns how long the player must wait until they can restart their island
	 * in seconds
	 *
	 * @param player
	 * @return how long the player must wait
	 */
	public long getResetWaitTime(final Player player) {
		if (resetWaitTime.containsKey(player.getUniqueId())) {
			if (resetWaitTime.get(player.getUniqueId()).longValue() > Calendar.getInstance().getTimeInMillis()) {
				return (resetWaitTime.get(player.getUniqueId()).longValue() - Calendar.getInstance().getTimeInMillis())
						/ 1000;
			}

			return 0L;
		}

		return 0L;
	}
	
	/**
	 * Makes the default island for the player
	 * 
	 * @param player
	 */
	public void newIsland(final Player player) {
		// plugin.getLogger().info("DEBUG: Making default island");
		newIsland(player, schematics.get("default"));
	}
	
	/**
	 * Makes an island using schematic. No permission checks are made. They have
	 * to be decided before this method is called.
	 * 
	 * @param player
	 * @param schematic
	 */
	public void newIsland(final Player player, final Schematic schematic) {
		// long time = System.nanoTime();
		final UUID playerUUID = player.getUniqueId();
		boolean firstTime = false;
		if (!plugin.getPlayers().hasIsland(playerUUID)) {
			firstTime = true;
		}
		// plugin.getLogger().info("DEBUG: finding island location");
		Location next = getNextIsland(player.getUniqueId());
		// plugin.getLogger().info("DEBUG: found " + next);
		// Set the player's parameters to this island
		plugin.getPlayers().setHasIsland(playerUUID, true);
		// Clear any old home locations (they should be clear, but just in case)
		plugin.getPlayers().clearHomeLocations(playerUUID);
		// Set the player's island location to this new spot
		plugin.getPlayers().setIslandLocation(playerUUID, next);

		// Set the biome
		// BiomesPanel.setIslandBiome(next, schematic.getBiome());
		// Teleport to the new home
		if (schematic.isPlayerSpawn()) {
			// Set home and teleport
			plugin.getPlayers().setHomeLocation(playerUUID, schematic.getPlayerSpawn(next), 1);
			// Save it for later reference
			plugin.getPlayers().setHomeLocation(playerUUID, schematic.getPlayerSpawn(next), -1);
		}

		// Sets a flag to temporarily disable cleanstone generation
		plugin.setNewIsland(true);
		// plugin.getBiomes();

		// Create island based on schematic
		if (schematic != null) {
			// plugin.getLogger().info("DEBUG: pasting schematic " +
			// schematic.getName() + " " + schematic.getPerm());
			// plugin.getLogger().info("DEBUG: nether world is " +
			// ASkyBlock.getNetherWorld());
			// Paste the starting island. If it is a HELL biome, then we start
			// in the Nether
			if (Settings.createNether && schematic.isInNether() && Settings.newNether
					&& ASkyBlock.getNetherWorld() != null) {
				// Nether start
				// Paste the overworld if it exists
				if (!schematic.getPartnerName().isEmpty() && schematics.containsKey(schematic.getPartnerName())) {
					// A partner schematic is available
					pastePartner(schematics.get(schematic.getPartnerName()), next, player);
				}
				// Switch home location to the Nether
				next = next.toVector().toLocation(ASkyBlock.getNetherWorld());
				// Set the player's island location to this new spot
				plugin.getPlayers().setIslandLocation(playerUUID, next);
				schematic.pasteSchematic(next, player, true, firstTime ? PasteReason.NEW_ISLAND : PasteReason.RESET);
			} else {
				// Over world start
				// plugin.getLogger().info("DEBUG: pasting");
				// long timer = System.nanoTime();
				// Paste the island and teleport the player home
				schematic.pasteSchematic(next, player, true, firstTime ? PasteReason.NEW_ISLAND : PasteReason.RESET);
				// double diff = (System.nanoTime() - timer)/1000000;
				// plugin.getLogger().info("DEBUG: nano time = " + diff + "
				// ms");
				// plugin.getLogger().info("DEBUG: pasted overworld");
				if (Settings.createNether && Settings.newNether && ASkyBlock.getNetherWorld() != null) {
					// Paste the other world schematic
					final Location netherLoc = next.toVector().toLocation(ASkyBlock.getNetherWorld());
					if (schematic.getPartnerName().isEmpty()) {
						// This will paste the over world schematic again
						// plugin.getLogger().info("DEBUG: pasting nether");
						pastePartner(schematic, netherLoc, player);
						// plugin.getLogger().info("DEBUG: pasted nether");
					} else {
						if (schematics.containsKey(schematic.getPartnerName())) {
							// plugin.getLogger().info("DEBUG: pasting
							// partner");
							// A partner schematic is available
							pastePartner(schematics.get(schematic.getPartnerName()), netherLoc, player);
						} else {
							plugin.getLogger().severe(
									"Partner schematic heading '" + schematic.getPartnerName() + "' does not exist");
						}
					}
				}
			}
			// Record the rating of this schematic - not used for anything right
			// now
			plugin.getPlayers().setStartIslandRating(playerUUID, schematic.getRating());
		}
		// Clear the cleanstone flag so events can happen again
		plugin.setNewIsland(false);
		// Add to the grid
		Island myIsland = plugin.getGrid().addIsland(next.getBlockX(), next.getBlockZ(), playerUUID);
		myIsland.setLevelHandicap(schematic.getLevelHandicap());
		// Save the player so that if the server is reset weird things won't
		// happen
		plugin.getPlayers().save(playerUUID);

		// Start the reset cooldown
		if (!firstTime) {
			setResetWaitTime(player);
		}
		// Set the custom protection range if appropriate
		// Dynamic island range sizes with permissions
		int range = Settings.islandProtectionRange;
		for (PermissionAttachmentInfo perms : player.getEffectivePermissions()) {
			if (perms.getPermission().startsWith(Settings.PERMPREFIX + "island.range.")) {
				if (perms.getPermission().contains(Settings.PERMPREFIX + "island.range.*")) {
					range = Settings.islandProtectionRange;
					break;
				} else {
					String[] spl = perms.getPermission().split(Settings.PERMPREFIX + "island.range.");
					if (spl.length > 1) {
						if (!NumberUtils.isDigits(spl[1])) {
							plugin.getLogger().severe("Player " + player.getName() + " has permission: "
									+ perms.getPermission() + " <-- the last part MUST be a number! Ignoring...");
						} else {
							range = Math.max(range, Integer.valueOf(spl[1]));
						}
					}
				}
			}
		}
		// Do some sanity checking
		if (range % 2 != 0) {
			range--;
			plugin.getLogger().warning("Protection range must be even, using " + range + " for " + player.getName());
		}
		if (range > Settings.islandDistance) {
			plugin.getLogger().warning("Player has " + Settings.PERMPREFIX + "island.range." + range);
			range = Settings.islandDistance;
			plugin.getLogger().warning(
					"Island protection range must be " + Settings.islandDistance + " or less. Setting to: " + range);
		}
		myIsland.setProtectionSize(range);

		// Save grid just in case there's a crash
		plugin.getGrid().saveGrid();
		// Done - fire event
		final IslandNewEvent event = new IslandNewEvent(player, schematic, myIsland);
		plugin.getServer().getPluginManager().callEvent(event);
		// plugin.getLogger().info("DEBUG: Done! " + (System.nanoTime()- time) *
		// 0.000001);
	}
	
	/**
	 * Does a delayed pasting of the partner island
	 * 
	 * @param schematic
	 * @param player
	 */
	private void pastePartner(final Schematic schematic, final Location loc, final Player player) {
		plugin.getServer().getScheduler().runTaskLater(plugin, new Runnable() {

			@Override
			public void run() {
				schematic.pasteSchematic(loc, player, false, PasteReason.PARTNER);
				if (schematic.isPlayerSpawn()) {
					// Set partner home
					plugin.getPlayers().setHomeLocation(player.getUniqueId(), schematic.getPlayerSpawn(loc), -2);
				}
			}
		}, 60L);

	}
	
	/**
	 * Get the location of next free island spot
	 * 
	 * @param playerUUID
	 *            - the player's UUID
	 * @return Location of island spot
	 */
	private Location getNextIsland(UUID playerUUID) {
		// See if there is a reserved spot
		if (islandSpot.containsKey(playerUUID)) {
			Location next = plugin.getGrid().getClosestIsland(islandSpot.get(playerUUID));
			// Single shot only
			islandSpot.remove(playerUUID);
			// Check if it is already occupied (shouldn't be)
			Island island = plugin.getGrid().getIslandAt(next);
			if (island == null || island.getOwner() == null) {
				// it's still free
				return next;
			}
			// Else, fall back to the random pick
		}
		// Find the next free spot
		if (last == null) {
			last = new Location(ASkyBlock.getIslandWorld(), Settings.islandXOffset + Settings.islandStartX,
					Settings.islandHeight, Settings.islandZOffset + Settings.islandStartZ);
		}
		Location next = last.clone();

		while (plugin.getGrid().islandAtLocation(next) || islandSpot.containsValue(next)) {
			next = nextGridLocation(next);
		}
		// Make the last next, last
		last = next.clone();
		return next;
	}
	
	/**
	 * Finds the next free island spot based off the last known island Uses
	 * island_distance setting from the config file Builds up in a grid fashion
	 *
	 * @param lastIsland
	 * @return Location of next free island
	 */
	private Location nextGridLocation(final Location lastIsland) {
		// plugin.getLogger().info("DEBUG nextIslandLocation");
		final int x = lastIsland.getBlockX();
		final int z = lastIsland.getBlockZ();
		final Location nextPos = lastIsland;
		if (x < z) {
			if (-1 * x < z) {
				nextPos.setX(nextPos.getX() + Settings.islandDistance);
				return nextPos;
			}
			nextPos.setZ(nextPos.getZ() + Settings.islandDistance);
			return nextPos;
		}
		if (x > z) {
			if (-1 * x >= z) {
				nextPos.setX(nextPos.getX() - Settings.islandDistance);
				return nextPos;
			}
			nextPos.setZ(nextPos.getZ() - Settings.islandDistance);
			return nextPos;
		}
		if (x <= 0) {
			nextPos.setZ(nextPos.getZ() + Settings.islandDistance);
			return nextPos;
		}
		nextPos.setZ(nextPos.getZ() - Settings.islandDistance);
		return nextPos;
	}
	

	public void resetPlayer(Player player, Island oldIsland) {
		// Deduct the reset
		plugin.getPlayers().setResetsLeft(player.getUniqueId(),
				plugin.getPlayers().getResetsLeft(player.getUniqueId()) - 1);
		// Reset deaths
		if (Settings.islandResetDeathReset) {
			plugin.getPlayers().setDeaths(player.getUniqueId(), 0);
		}
		// Clear any coop inventories
		// CoopPlay.getInstance().returnAllInventories(player);
		// Remove any coop invitees and grab their stuff
		CoopPlay.getInstance().clearMyInvitedCoops(player);
		CoopPlay.getInstance().clearMyCoops(player);
		// plugin.getLogger().info("DEBUG Reset command issued!");
		// Remove any warps
		plugin.getWarpSignsListener().removeWarp(player.getUniqueId());
		// Delete the old island, if it exists
		if (oldIsland != null) {
			plugin.getServer().getPluginManager().callEvent(new IslandPreDeleteEvent(player.getUniqueId(), oldIsland));
			// Remove any coops
			CoopPlay.getInstance().clearAllIslandCoops(oldIsland.getCenter());
			plugin.getGrid().removePlayersFromIsland(oldIsland, player.getUniqueId());
			// plugin.getLogger().info("DEBUG Deleting old island");
			new DeleteIslandChunk(plugin, oldIsland);
			// new DeleteIslandByBlock(plugin, oldIsland);
			// Fire event
			final IslandResetEvent event = new IslandResetEvent(player, oldIsland.getCenter());
			plugin.getServer().getPluginManager().callEvent(event);
		} else {
			// plugin.getLogger().info("DEBUG oldisland = null!");
		}
		plugin.getGrid().saveGrid();
	}
	
	public void reserveLocation(UUID playerUUID, Location location) {
		islandSpot.put(playerUUID, location);
	}
	
	/**
	 * Calculates the island level
	 *
	 * @param sender
	 *            - Player object of player who is asking
	 * @param targetPlayer
	 *            - UUID of the player's island that is being requested
	 * @return - true if successful.
	 */
	public boolean calculateIslandLevel(final CommandSender sender, final UUID targetPlayer) {
		return calculateIslandLevel(sender, targetPlayer, false);
	}

	/**
	 * Calculates the island level
	 * 
	 * @param sender
	 *            - asker of the level info
	 * @param targetPlayer
	 * @param report
	 *            - if true, a detailed report will be provided
	 * @return - false if this is cannot be done
	 */
	public boolean calculateIslandLevel(final CommandSender sender, final UUID targetPlayer, boolean report) {
		if (sender instanceof Player) {
			Player asker = (Player) sender;
			// Player asking for their own island calc
			if (asker.getUniqueId().equals(targetPlayer) || asker.isOp()
					|| VaultHelper.checkPerm(asker, Settings.PERMPREFIX + "mod.info")) {
				// Newer better system - uses chunks
				if (!onLevelWaitTime(asker) || Settings.levelWait <= 0 || asker.isOp()
						|| VaultHelper.checkPerm(asker, Settings.PERMPREFIX + "mod.info")) {
					Util.sendMessage(asker, ChatColor.GREEN + plugin.myLocale(asker.getUniqueId()).levelCalculating);
					setLevelWaitTime(asker);
					new LevelCalcByChunk(plugin, plugin.getGrid().getIsland(targetPlayer), targetPlayer, asker, report);
				} else {
					Util.sendMessage(asker, ChatColor.YELLOW + plugin.myLocale(asker.getUniqueId()).islandresetWait
							.replace("[time]", String.valueOf(getLevelWaitTime(asker))));
				}

			} else {
				// Asking for the level of another player
				Util.sendMessage(asker, ChatColor.GREEN + plugin.myLocale(asker.getUniqueId()).islandislandLevelis
						.replace("[level]", String.valueOf(plugin.getPlayers().getIslandLevel(targetPlayer))));
			}
		} else {
			// Console request
			Util.sendMessage(sender, ChatColor.GREEN + plugin.myLocale().levelCalculating);
			new LevelCalcByChunk(plugin, plugin.getGrid().getIsland(targetPlayer), targetPlayer, sender, report);
		}
		return true;
	}
	
	private boolean onLevelWaitTime(final Player player) {
		if (levelWaitTime.containsKey(player.getUniqueId())) {
			if (levelWaitTime.get(player.getUniqueId()).longValue() > Calendar.getInstance().getTimeInMillis()) {
				return true;
			}

			return false;
		}

		return false;
	}

	/**
	 * Sets cool down for the level command
	 *
	 * @param player
	 */
	private void setLevelWaitTime(final Player player) {
		levelWaitTime.put(player.getUniqueId(),
				Long.valueOf(Calendar.getInstance().getTimeInMillis() + Settings.levelWait * 1000));
	}
	
	private long getLevelWaitTime(final Player player) {
		if (levelWaitTime.containsKey(player.getUniqueId())) {
			if (levelWaitTime.get(player.getUniqueId()).longValue() > Calendar.getInstance().getTimeInMillis()) {
				return (levelWaitTime.get(player.getUniqueId()).longValue() - Calendar.getInstance().getTimeInMillis())
						/ 1000;
			}

			return 0L;
		}

		return 0L;
	}
	
	/**
	 * Pastes a schematic at a location for the player
	 * 
	 * @param schematic
	 * @param loc
	 * @param player
	 * @param reason
	 */
	public void pasteSchematic(final Schematic schematic, final Location loc, final Player player, PasteReason reason) {
		schematic.pasteSchematic(loc, player, false, reason);
	}
	
}
