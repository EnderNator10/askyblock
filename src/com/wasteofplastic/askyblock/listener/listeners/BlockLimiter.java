package com.wasteofplastic.askyblock.listener.listeners;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
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
}
