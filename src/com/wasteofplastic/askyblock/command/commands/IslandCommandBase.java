package com.wasteofplastic.askyblock.command.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.wasteofplastic.askyblock.ASkyBlock;
import com.wasteofplastic.askyblock.Settings;
import com.wasteofplastic.askyblock.command.CommandType;
import com.wasteofplastic.askyblock.command.VCommand;
import com.wasteofplastic.askyblock.util.Util;
import com.wasteofplastic.askyblock.util.VaultHelper;
import com.wasteofplastic.askyblock.zcore.Message;

public abstract class IslandCommandBase extends VCommand {

	protected UUID teamLeader;
	protected List<UUID> teamMembers;
	protected UUID playerUUID;
	protected IslandPerfom perform = IslandPerfom.getInstance();
	protected ASkyBlock plugin = ASkyBlock.getPlugin();
	protected UUID targetPlayer = null;
	private boolean hasIsland = false;

	@Override
	public CommandType perform(ASkyBlock main) {

		if (!(sender instanceof Player)) {
			Util.sendMessage(sender, main.myLocale().errorUseInGame);
			return CommandType.EXCEPTION_ERROR;
		}

		final Player player = (Player) sender;
		if (!VaultHelper.checkPerm(player, Settings.PERMPREFIX + "island.create")) {
			message(sender, Message.NO_PERMISSION);
			return CommandType.EXCEPTION_ERROR;
		}

		playerUUID = player.getUniqueId();
		if (playerUUID == null) {
			main.getLogger().severe("Player " + sender.getName() + " has a null UUID - this should never happen!");
			Util.sendMessage(sender, ChatColor.RED + main.myLocale().errorCommandNotReady + " (No UUID)");
			return CommandType.EXCEPTION_ERROR;
		}

		teamLeader = main.getPlayers().getTeamLeader(playerUUID);
		teamMembers = new ArrayList<UUID>();
		if (teamLeader != null) 
			teamMembers = main.getPlayers().getMembers(teamLeader);
		targetPlayer = null;

		if (hasIsland && !plugin.getPlayers().hasIsland(playerUUID)){
			message("§cVous devez avoir une île.");
			return CommandType.DEFAULT;
		}
		
		return postPerform(main);
	}

	public abstract CommandType postPerform(ASkyBlock main);

	protected void message(String message) {
		sender.sendMessage(message);
	}

	public void setIsland(boolean hasIsland) {
		this.hasIsland = hasIsland;
	}
	
}
