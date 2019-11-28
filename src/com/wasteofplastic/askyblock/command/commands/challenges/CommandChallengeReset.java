package com.wasteofplastic.askyblock.command.commands.challenges;

import org.bukkit.entity.Player;

import com.wasteofplastic.askyblock.ASkyBlock;
import com.wasteofplastic.askyblock.challenges.ChallengesManager;
import com.wasteofplastic.askyblock.command.CommandType;
import com.wasteofplastic.askyblock.command.VCommand;

public class CommandChallengeReset extends VCommand {

	@Override
	public CommandType perform(ASkyBlock main, Player player) {
		
		try {
			String name = args[1];
			ChallengesManager.getInstance().deletePlayer(name, sender);
		}catch (Exception e) {
			return CommandType.SYNTAX_ERROR;
		}
		
		return CommandType.SUCCESS;
	}

}
