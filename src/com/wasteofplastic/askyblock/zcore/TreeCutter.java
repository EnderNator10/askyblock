package com.wasteofplastic.askyblock.zcore;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import com.wasteofplastic.askyblock.ASkyBlock;

public class TreeCutter {

	private List<String> comparisonBlockArray = new ArrayList<>();
	private List<String> comparisonBlockArrayLeaves = new ArrayList<>();
	private List<Block> blocks = new ArrayList<>();

	@SuppressWarnings("deprecation")
	public TreeCutter(Player cutter, Block startBlock) {
		runLoop(startBlock, startBlock.getX(), startBlock.getZ());
		int blocksSize = blocks.size();
		
		cutter.getItemInHand().setDurability((short) (cutter.getItemInHand().getDurability() + blocksSize));
		
		new Timer().scheduleAtFixedRate(new TimerTask() {
			int index = 0;
			@Override
			public void run() {

				if (!ASkyBlock.getPlugin().isEnabled()) {
					cancel();
					return;
				}

				if (index >= blocksSize)
					cancel();
				else
					try {
						Block block = blocks.get(index);
						Bukkit.getScheduler().runTask(ASkyBlock.getPlugin(), () -> block.breakNaturally());
						index += 1;
					} catch (Exception e) {
						cancel();
					}

			}
		}, 0, 1);
	}

	public void runLoop(Block b1, final int x1, final int z1) {
		for (int x = -2; x <= 2; x++) {
			for (int y = -2; y <= 2; y++) {
				for (int z = -2; z <= 1; z++) {
					if (x == 0 && y == 0 && z == 0)
						continue;
					Block b2 = b1.getRelative(x, y, z);
					String s = b2.getX() + ":" + b2.getY() + ":" + b2.getZ();

					if ((b2.getType() == Material.LEAVES || b2.getType() == Material.LEAVES_2)
							&& !comparisonBlockArrayLeaves.contains(s))
						comparisonBlockArrayLeaves.add(s);
					if (b2.getType() != Material.LOG && b2.getType() != Material.LOG_2)
						continue;
					int searchSquareSize = 25;
					if (b2.getX() > x1 + searchSquareSize || b2.getX() < x1 - searchSquareSize
							|| b2.getZ() > z1 + searchSquareSize || b2.getZ() < z1 - searchSquareSize)
						break;
					if (!comparisonBlockArray.contains(s)) {
						comparisonBlockArray.add(s);
						blocks.add(b2);
						this.runLoop(b2, x1, z1);
					}
				}
			}
		}
	}

}
