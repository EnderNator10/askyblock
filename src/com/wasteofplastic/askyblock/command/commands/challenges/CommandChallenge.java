package com.wasteofplastic.askyblock.command.commands.challenges;

import com.wasteofplastic.askyblock.ASkyBlock;
import com.wasteofplastic.askyblock.challenges.ChallengeType;
import com.wasteofplastic.askyblock.challenges.ChallengesManager;
import com.wasteofplastic.askyblock.command.CommandType;
import com.wasteofplastic.askyblock.command.VCommand;

public class CommandChallenge extends VCommand {

	@Override
	public CommandType perform(ASkyBlock main) {
		
		ChallengesManager.getInstance().open(ChallengeType.DEFAULT, player);
		
		return CommandType.SUCCESS;
	}

}
