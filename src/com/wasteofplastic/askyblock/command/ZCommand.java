package com.wasteofplastic.askyblock.command;

import java.util.List;
import java.util.function.BiConsumer;

import org.bukkit.entity.Player;

import com.wasteofplastic.askyblock.ASkyBlock;

public class ZCommand extends VCommand {

	private BiConsumer<VCommand, ASkyBlock> command;

	@Override
	public CommandType perform(ASkyBlock main, Player player) {
		
		if (command != null){
			command.accept(this, main);
		}

		return CommandType.SUCCESS;
	}

	public VCommand setCommand(BiConsumer<VCommand, ASkyBlock> command) {
		this.command = command;
		return this;
	}

	public VCommand msg(String message) {
		command = (cmd, main) -> cmd.sendMessage(message);
		return this;
	}

	public VCommand msg(List<String> message) {
		command = (cmd, main) -> cmd.sendMessage(message);
		return this;
	}


}
