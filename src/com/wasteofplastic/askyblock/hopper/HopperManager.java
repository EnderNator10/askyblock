package com.wasteofplastic.askyblock.hopper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;

import com.wasteofplastic.askyblock.ASkyBlock;
import com.wasteofplastic.askyblock.Island;
import com.wasteofplastic.askyblock.Island.SettingsFlag;
import com.wasteofplastic.askyblock.Settings;
import com.wasteofplastic.askyblock.listener.ListenerAdapter;
import com.wasteofplastic.askyblock.listeners.EntityLimits;
import com.wasteofplastic.askyblock.listeners.IslandGuard;
import com.wasteofplastic.askyblock.zcore.ItemBuilder;
import com.wasteofplastic.askyblock.zcore.Message;
import com.wasteofplastic.askyblock.zcore.storage.Persist;
import com.wasteofplastic.askyblock.zcore.storage.Saveable;

public class HopperManager extends ListenerAdapter implements Saveable {
	private static Map<String, Hopper> hoppers = new HashMap<>();
	private transient ASkyBlock plugin = ASkyBlock.getPlugin();

	public HopperManager() {
	}

	@SuppressWarnings("deprecation")
	protected void onBlockPlace(BlockPlaceEvent event, Player player) {
		if (IslandGuard.inWorld(player) && event.getBlock().getType().equals(Material.HOPPER)
				&& this.same(player.getItemInHand(), "§6» §eHopper §6«")) {
			Island island = this.getPlugin().getGrid().getProtectedIslandAt(event.getBlock().getLocation());
			if (island == null) {
				if (!((Boolean) Settings.defaultWorldSettings.get(SettingsFlag.PLACE_BLOCKS)).booleanValue()) {
					event.setCancelled(true);
				}

				return;
			}

			if (event.isCancelled()) {
				return;
			}

			if (!EntityLimits.actionAllowed(player, event.getBlock().getLocation(), SettingsFlag.PLACE_BLOCKS)) {
				event.setCancelled(true);
			}

			this.removeItemInHand(player, 1);
			Hopper hopper = new Hopper(event.getBlock().getLocation());
			hopper.run();
			hoppers.put(this.changeLocationToString(event.getBlock().getLocation()), hopper);
			this.message(player, Message.HOPPER_PLACE);
		}

	}

	protected void onBlockBreak(BlockBreakEvent event, Player player) {

		if (IslandGuard.inWorld(player) && event.getBlock().getType().equals(Material.HOPPER)) {
			Island island = this.getPlugin().getGrid().getProtectedIslandAt(event.getBlock().getLocation());
			if (island == null) {
				if (!((Boolean) Settings.defaultWorldSettings.get(SettingsFlag.PLACE_BLOCKS)).booleanValue()) {
					event.setCancelled(true);
				}

				return;
			}

			if (event.isCancelled()) {
				return;
			}

			if (!EntityLimits.actionAllowed(player, event.getBlock().getLocation(), SettingsFlag.PLACE_BLOCKS)) {
				event.setCancelled(true);
			}

			Hopper hopper = getHopper(changeLocationToString(event.getBlock().getLocation()));
			if (hopper != null) {
				hopper.destroy();
				hoppers.remove(hopper.getStringLocation());
				event.getBlock().getDrops().clear();
				event.setDropItems(false);
				event.getBlock().getWorld().dropItem(event.getBlock().getLocation(), ItemBuilder.getHopper());
				this.message(player, Message.HOPPER_REMOVE);
			}
		}

	}

	@Override
	public void onChunkUnLoad(ChunkUnloadEvent event, Chunk chunk, World world) {
		if (IslandGuard.inWorld(world)) {
			List<Hopper> hoppers = getHopper(chunk);
			if (hoppers.size() != 0)
				hoppers.forEach(Hopper::destroy);
		}
	}

	@Override
	public void onChunkLoad(ChunkLoadEvent event, Chunk chunk, World world) {
		if (IslandGuard.inWorld(world)) {
			List<Hopper> hoppers = getHopper(chunk);
			if (hoppers.size() != 0)
				hoppers.forEach(Hopper::run);
		}
	}

	private List<Hopper> getHopper(Chunk chunk) {
		return hoppers
				.values().stream().filter(hopper -> hopper.getLocation() != null
						&& hopper.getLocation().getChunk() != null && hopper.getLocation().getChunk().equals(chunk))
				.collect(Collectors.toList());
	}

	public Hopper getHopper(String loc) {
		return hoppers.getOrDefault(loc, null);
	}

	public void remove(Hopper hopper) {
		hoppers.remove(hopper.getStringLocation());
	}

	public ASkyBlock getPlugin() {
		if (this.plugin == null) {
			this.plugin = ASkyBlock.getPlugin();
		}

		return this.plugin;
	}

	public void save(Persist persist) {
		persist.save(this, "hoppers");
		hoppers.values().forEach(Hopper::destroy);
	}

	public void load(Persist persist) {
		persist.loadOrSaveDefault(this, HopperManager.class, "hoppers");
		hoppers.values().forEach(Hopper::run);
	}
}
