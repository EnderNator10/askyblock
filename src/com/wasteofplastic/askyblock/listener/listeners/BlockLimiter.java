package com.wasteofplastic.askyblock.listener.listeners;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockFormEvent;
import org.bukkit.event.block.BlockPlaceEvent;

import com.wasteofplastic.askyblock.ASkyBlock;
import com.wasteofplastic.askyblock.Island;
import com.wasteofplastic.askyblock.listener.ListenerAdapter;
import com.wasteofplastic.askyblock.zcore.Message;

public class BlockLimiter extends ListenerAdapter {

	private final ASkyBlock main;

	public BlockLimiter(ASkyBlock main) {
		super();
		this.main = main;
	}

	protected void onBlockPlace(BlockPlaceEvent event, Player player) {

		Block block = event.getBlock();
		Island island = main.getGrid().getIslandAt(block.getLocation());

		if (island != null) {

			if (block.getType().equals(Material.HOPPER)) {

				int tmpHopper = island.getHopperCount() + 1;
				if (tmpHopper > island.getHopperLimit()) {
					event.setCancelled(true);
					message(player, Message.ISLAND_LIMIT_HOPPER, island.getHopperLimit());
					return;
				}
			}

			if (block.getType().equals(Material.PISTON_BASE) || block.getType().equals(Material.PISTON_STICKY_BASE)) {

				int tmpPiston = island.getPiston() + 1;
				if (tmpPiston > island.getPistonLimit()) {
					event.setCancelled(true);
					message(player, Message.ISLAND_LIMIT_PISTON, island.getPistonLimit());
					return;
				}
				island.addPiston();
			}

		}

	}

	@Override
	protected void onBlockBreak(BlockBreakEvent event, Player player) {

		Block block = event.getBlock();
		Island island = main.getGrid().getIslandAt(block.getLocation());

		if (island != null) {
			if (block.getType().equals(Material.PISTON_BASE) || block.getType().equals(Material.PISTON_STICKY_BASE)) {
				island.removePiston();
			}
		}
	}

	private final transient Random random = new Random();

	@Override
	public void onBlockForm(BlockFormEvent event, BlockState newState) {

		Island island = main.getGrid().getIslandAt(event.getBlock().getLocation());
//
//		if (newState.getType().equals(Material.STONE)) {
//			List<Material> generateOre = new ArrayList<Material>();
//			generateOre.add(Material.STONE);
//			generateOre.add(Material.STONE);
//			generateOre.add(Material.STONE);
//			generateOre.add(Material.SAND);
//			generateOre.add(Material.SAND);
//			generateOre.add(Material.SAND);
//			event.getNewState().setData(new MaterialData(generateOre.get(random.nextInt(generateOre.size())), (byte)random.nextInt(2)));
//			event.getNewState().update();
//		}
		
		if (newState.getType().equals(Material.COBBLESTONE)) {

			List<Material> generateOre = new ArrayList<Material>();
			generateOre.add(Material.COBBLESTONE);
			generateOre.add(Material.COBBLESTONE);
			generateOre.add(Material.STONE);
			generateOre.add(Material.STONE);

			if (island.isGeneratorOre()) {

				if (island.getGeneratorLevel() >= 2) {
					generateOre.add(Material.COAL_ORE);
				}
				if (island.getGeneratorLevel() >= 3) {
					generateOre.add(Material.IRON_ORE);
				}
				if (island.getGeneratorLevel() >= 4) {
					generateOre.add(Material.LAPIS_ORE);
				}
				if (island.getGeneratorLevel() >= 5) {
					generateOre.add(Material.GOLD_ORE);
				}
				if (island.getGeneratorLevel() >= 6) {
					generateOre.add(Material.QUARTZ_ORE);
				}
				if (island.getGeneratorLevel() >= 7) {
					generateOre.add(Material.REDSTONE_ORE);
				}
				if (island.getGeneratorLevel() >= 8) {
					generateOre.add(Material.COBBLESTONE);
					generateOre.add(Material.DIAMOND_ORE);
				}
				if (island.getGeneratorLevel() >= 9) {
					generateOre.add(Material.EMERALD_ORE);
					generateOre.add(Material.COBBLESTONE);
				}
				if (island.getGeneratorLevel() >= 10) {
					generateOre.add(Material.COAL_BLOCK);
				}
				if (island.getGeneratorLevel() >= 11) {
					generateOre.add(Material.IRON_BLOCK);
				}
				if (island.getGeneratorLevel() >= 12) {
					generateOre.add(Material.LAPIS_BLOCK);
				}
				if (island.getGeneratorLevel() >= 13) {
					generateOre.add(Material.GOLD_BLOCK);
				}
				if (island.getGeneratorLevel() >= 14) {
					generateOre.add(Material.QUARTZ_BLOCK);
				}
				if (island.getGeneratorLevel() >= 15) {
					generateOre.add(Material.REDSTONE_BLOCK);
				}
				if (island.getGeneratorLevel() >= 16) {
					generateOre.add(Material.COBBLESTONE);
					generateOre.add(Material.COBBLESTONE);
					generateOre.add(Material.DIAMOND_BLOCK);
				}
				if (island.getGeneratorLevel() >= 17) {
					generateOre.add(Material.COBBLESTONE);
					generateOre.add(Material.COBBLESTONE);
					generateOre.add(Material.COBBLESTONE);
					generateOre.add(Material.COBBLESTONE);
					generateOre.add(Material.EMERALD_BLOCK);
				}

			}

			event.getNewState().setType(generateOre.get(random.nextInt(generateOre.size())));
			event.getNewState().update();

		}

	}
}
