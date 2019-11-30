package com.wasteofplastic.askyblock.listener.listeners;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;

import com.wasteofplastic.askyblock.ASkyBlock;
import com.wasteofplastic.askyblock.Island;
import com.wasteofplastic.askyblock.listener.ListenerAdapter;
import com.wasteofplastic.askyblock.zcore.ItemBuilder;
import com.wasteofplastic.askyblock.zcore.TreeCutter;

public class TreeListener extends ListenerAdapter {

	private final ASkyBlock plugin = ASkyBlock.getPlugin();
	public static List<UUID> currentFellers = new ArrayList<>();

	@SuppressWarnings("deprecation")
	@Override
	protected void onBlockBreak(BlockBreakEvent event, Player player) {

		Block block = event.getBlock();
		Island island = plugin.getGrid().getIslandAt(block.getLocation());

		if (island != null && block.getType().equals(Material.LOG) || block.getType().equals(Material.LOG_2)
				&& player.getItemInHand().isSimilar(ItemBuilder.getCreatedItem(Material.DIAMOND_AXE, 1, "§cL'Hache"))) {

			event.setCancelled(true);

			new TreeCutter(player, block);

		}

	}

}
