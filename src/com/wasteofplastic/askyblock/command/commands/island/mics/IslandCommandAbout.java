package com.wasteofplastic.askyblock.command.commands.island.mics;

import org.bukkit.ChatColor;

import com.wasteofplastic.askyblock.ASkyBlock;
import com.wasteofplastic.askyblock.command.CommandType;
import com.wasteofplastic.askyblock.command.commands.IslandCommandBase;

public class IslandCommandAbout extends IslandCommandBase {

	public IslandCommandAbout() {
		this.addSubCommand("about");
		this.addSubCommand("ver");
		this.addSubCommand("v");
		this.addSubCommand("version");
		this.setSyntaxe("/is version");
		this.setDescription("Voir la version du plugin");
	}

	@Override
	public CommandType postPerform(ASkyBlock main) {

		message(ChatColor.GOLD + "About " + ChatColor.GREEN + main.getDescription().getName() + ChatColor.GOLD + " v"
				+ ChatColor.AQUA + main.getDescription().getVersion() + ChatColor.GOLD + ":");
		message(ChatColor.GRAY + "This plugin is free software: you can redistribute");
		message(ChatColor.GRAY + "it and/or modify it under the terms of the GNU");
		message(ChatColor.GRAY + "General Public License as published by the Free");
		message(ChatColor.GRAY + "Software Foundation, either version 3 of the License,");
		message(ChatColor.GRAY + "or (at your option) any later version.");
		message(ChatColor.GRAY + "This plugin is distributed in the hope that it");
		message(ChatColor.GRAY + "will be useful, but WITHOUT ANY WARRANTY; without");
		message(ChatColor.GRAY + "even the implied warranty of MERCHANTABILITY or");
		message(ChatColor.GRAY + "FITNESS FOR A PARTICULAR PURPOSE. See the");
		message(ChatColor.GRAY + "GNU General Public License for more details.");
		message(ChatColor.GRAY + "You should have received a copy of the GNU");
		message(ChatColor.GRAY + "General Public License along with this plugin.");
		message(ChatColor.GRAY + "If not, see <http://www.gnu.org/licenses/>.");
		message(ChatColor.GRAY + "Source code is available on GitHub.");
		message(ChatColor.GRAY + "(c) 2014 - 2018 by tastybento, Poslovitch");
		message(ChatColor.DARK_GREEN + "Version amélioré par §aMaxlego08§2 pour le serveur neralia.");
		message(ChatColor.DARK_GREEN + "https://neralia.net");

		return CommandType.SUCCESS;
	}

}
