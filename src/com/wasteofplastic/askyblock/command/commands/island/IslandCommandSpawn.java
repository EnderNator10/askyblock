package com.wasteofplastic.askyblock.command.commands.island;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.util.Vector;

import com.wasteofplastic.askyblock.ASkyBlock;
import com.wasteofplastic.askyblock.Island;
import com.wasteofplastic.askyblock.Settings;
import com.wasteofplastic.askyblock.command.CommandType;
import com.wasteofplastic.askyblock.command.commands.IslandCommandBase;
import com.wasteofplastic.askyblock.events.IslandPreTeleportEvent;

public class IslandCommandSpawn extends IslandCommandBase {

	public IslandCommandSpawn() {
		this.addSubCommand("spawn");
		this.setSyntaxe("/is spawn");
		this.setDescription("Se téléporter au spawn");
		this.setPermission(Settings.PERMPREFIX + "island.spawn");
	}

	@Override
	public CommandType postPerform(ASkyBlock plugin) {
		// go to spawn
		Location l = ASkyBlock.getIslandWorld().getSpawnLocation();
		l.add(new Vector(0.5, 0, 0.5));
		Island spawn = plugin.getGrid().getSpawn();
		if (spawn != null && spawn.getSpawnPoint() != null) {
			l = spawn.getSpawnPoint();
		}

		IslandPreTeleportEvent event = new IslandPreTeleportEvent(player, IslandPreTeleportEvent.Type.SPAWN,
				l);
		Bukkit.getPluginManager().callEvent(event);
		if (!event.isCancelled()) {
			player.teleport(event.getLocation());
		}
		return CommandType.SUCCESS;
	}

}
