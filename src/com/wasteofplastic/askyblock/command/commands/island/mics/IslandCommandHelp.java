package com.wasteofplastic.askyblock.command.commands.island.mics;

import com.wasteofplastic.askyblock.ASkyBlock;
import com.wasteofplastic.askyblock.command.CommandType;
import com.wasteofplastic.askyblock.command.commands.IslandCommandBase;
import com.wasteofplastic.askyblock.zcore.Logger;
import com.wasteofplastic.askyblock.zcore.Logger.LogType;

public class IslandCommandHelp extends IslandCommandBase {

	public IslandCommandHelp() {
		this.addSubCommand("help");
		this.addSubCommand("?");
		this.addSubCommand("aide");
		this.setSyntaxe("/is help");
		this.setDescription("Voir les informations des commandes");
	}

	@Override
	public CommandType postPerform(ASkyBlock main) {

		sender.sendMessage("§f» §7Liste des commandes disponible:");
		main.getCommandManager().getCommands("island").forEach(command -> {

			// On verifie si le joueur a bien la permission
			if (command.getPermission() == null || sender.hasPermission(command.getPermission())) {

				if (command.getDescription() != null && command.getSyntaxe() != null) {

					sender.sendMessage("§f» §2" + command.getSyntaxe() + " §7" + command.getDescription());

				} else {
					Logger.info("Impossible de trouver la description ou la syntaxe pour la commande: "
							+ command.toString(), LogType.ERROR);
				}

			}

		});

		return CommandType.SUCCESS;
	}

}
