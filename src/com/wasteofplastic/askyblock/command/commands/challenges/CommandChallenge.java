package com.wasteofplastic.askyblock.command.commands.challenges;

import org.bukkit.entity.Player;

import com.wasteofplastic.askyblock.ASkyBlock;
import com.wasteofplastic.askyblock.challenges.ChallengeType;
import com.wasteofplastic.askyblock.challenges.ChallengesManager;
import com.wasteofplastic.askyblock.command.CommandType;
import com.wasteofplastic.askyblock.command.VCommand;

public class CommandChallenge extends VCommand {

	@Override
	public CommandType perform(ASkyBlock main, Player player) {
		
		ChallengesManager.getInstance().open(ChallengeType.DEFAULT, player);
		
		return CommandType.SUCCESS;
	}

}
