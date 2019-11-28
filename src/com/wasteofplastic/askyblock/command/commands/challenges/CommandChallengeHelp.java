package com.wasteofplastic.askyblock.command.commands.challenges;

import org.bukkit.entity.Player;

import com.wasteofplastic.askyblock.ASkyBlock;
import com.wasteofplastic.askyblock.command.CommandType;
import com.wasteofplastic.askyblock.command.VCommand;

public class CommandChallengeHelp extends VCommand {

	@Override
	public CommandType perform(ASkyBlock main, Player player) {
		
		main.getCommandManager().sendHelp("help", sender);
		
		return CommandType.SUCCESS;
	}

}
