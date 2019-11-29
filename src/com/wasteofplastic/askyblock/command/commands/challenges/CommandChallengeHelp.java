package com.wasteofplastic.askyblock.command.commands.challenges;

import com.wasteofplastic.askyblock.ASkyBlock;
import com.wasteofplastic.askyblock.command.CommandType;
import com.wasteofplastic.askyblock.command.VCommand;

public class CommandChallengeHelp extends VCommand {

	@Override
	public CommandType perform(ASkyBlock main) {
		
		main.getCommandManager().sendHelp("help", sender);
		
		return CommandType.SUCCESS;
	}

}
