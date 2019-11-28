package com.wasteofplastic.askyblock.command;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.wasteofplastic.askyblock.ASkyBlock;
import com.wasteofplastic.askyblock.command.commands.challenges.CommandChallenge;
import com.wasteofplastic.askyblock.command.commands.challenges.CommandChallengeHelp;
import com.wasteofplastic.askyblock.command.commands.challenges.CommandChallengeReset;
import com.wasteofplastic.askyblock.zcore.Logger.LogType;

public class CommandManager implements CommandExecutor {

	private final ASkyBlock main;
	private final List<VCommand> commands = new ArrayList<VCommand>();

	public CommandManager(ASkyBlock template) {
		this.main = template;
		this.registerCommands();
	}

	private void registerCommands() {

		/**
		 * Challenge command
		 */

		VCommand challenge = addCommand("asc",
				new CommandChallenge().addSubCommand("c", "challenge", "aschallenge", "challenges")
						.setDescription("Ouvrir le menu des challenges").setSyntaxe("/c").setConsoleCanUse(false));

		addCommand(new CommandChallengeHelp().setParent(challenge).setSyntaxe("/c help")
				.setDescription("Voir la liste des commandes").addSubCommand("help", "aide", "?")
				.setPermission("admin.askyblock"));

		addCommand(new CommandChallengeReset().setParent(challenge).setSyntaxe("/c reset <player>")
				.setDescription("Reset les challenges d'un joueur").addSubCommand("reset")
				.setPermission("admin.askyblock").setArgsMaxLength(2).setArgsMinLength(2).setIgnoreArgs(true));

		main.getLog().log("Loading " + getUniqueCommand() + " commands", LogType.SUCCESS);
	}

	public VCommand addCommand(VCommand command) {
		commands.add(command);
		return command;
	}

	public VCommand addCommand(String string, VCommand command) {
		commands.add(command.addSubCommand(string));
		main.getCommand(string).setExecutor(this);
		return command;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		for (VCommand command : commands) {
			if (command.getSubCommands().contains(cmd.getName().toLowerCase())) {
				if ((args.length == 0 || command.isIgnoreParent()) && command.getParent() == null) {
					CommandType type = processRequirements(command, sender, args);
					if (!type.equals(CommandType.CONTINUE))
						return true;
				}
			} else if (args.length >= 1 && command.getParent() != null
					&& command.getParent().getSubCommands().contains(cmd.getName().toLowerCase())
					&& canExecute(args, cmd.getName().toLowerCase(), command)) {
				CommandType type = processRequirements(command, sender, args);
				if (!type.equals(CommandType.CONTINUE))
					return true;
			}
		}
		sender.sendMessage(prefix + " " + commandError);
		return true;
	}

	/**
	 * @param args
	 * @param cmd
	 * @param command
	 * @return true if can execute
	 */
	private boolean canExecute(String[] args, String cmd, VCommand command) {
		for (int index = args.length - 1; index > -1; index--) {
			if (command.getSubCommands().contains(args[index].toLowerCase())) {
				if (command.isIgnoreArgs())
					return true;
				if (index < args.length - 1)
					return false;
				return canExecute(args, cmd, command.getParent(), index - 1);
			}
		}
		return false;
	}

	/**
	 * @param args
	 * @param cmd
	 * @param command
	 * @param index
	 * @return
	 */
	private boolean canExecute(String[] args, String cmd, VCommand command, int index) {
		if (index < 0 && command.getSubCommands().contains(cmd.toLowerCase()))
			return true;
		else if (index < 0)
			return false;
		else if (command.getSubCommands().contains(args[index].toLowerCase()))
			return canExecute(args, cmd, command.getParent(), index - 1);
		else
			return false;
	}

	public static String prefix = "§7[§bNeralia§7]";

	public static String commandHelp = "§a» §2%syntaxe% §8- §7%description%";
	public static String noPermission = "§cVous n'avez pas la permission";
	public static String syntaxeError = "§cVous devez exécuter la commande comme ceci§7: §a%command%";
	public static String commandError = "§cCet argument n'existe pas !";
	public static String onlinePlayerCanUse = "§cYou must be player to do this !";

	/**
	 * @param command
	 * @param sender
	 * @param strings
	 * @return
	 */
	private CommandType processRequirements(VCommand command, CommandSender sender, String[] strings) {

		if (!(sender instanceof Player) && !command.isConsoleCanUse()) {
			sender.sendMessage(prefix + " " + onlinePlayerCanUse);
			return CommandType.DEFAULT;
		}
		if (command.getPermission() == null || sender.hasPermission(command.getPermission())) {
			if (command.getArgsMinLength() != 0 && command.getArgsMaxLength() != 0
					&& !(strings.length >= command.getArgsMinLength()
							&& strings.length <= command.getArgsMaxLength())) {
				if (command.getSyntaxe() != null)
					sender.sendMessage(prefix + " " + syntaxeError.replace("%command%", command.getSyntaxe()));
				return CommandType.SYNTAX_ERROR;
			}
			command.setSender(sender);
			command.setArgs(strings);
			CommandType returnType = command.perform(main, (Player) sender);
			if (returnType == CommandType.SYNTAX_ERROR) {
				sender.sendMessage(prefix + " " + syntaxeError.replace("%command%", command.getSyntaxe()));
			}
			return returnType;
		}
		sender.sendMessage(prefix + " " + noPermission);
		return CommandType.DEFAULT;
	}

	public List<VCommand> getCommands() {
		return commands;
	}

	private int getUniqueCommand() {
		return (int) commands.stream().filter(command -> command.getParent() == null).count();
	}

	/**
	 * @param commandString
	 * @param sender
	 */
	public void sendHelp(String commandString, CommandSender sender) {
		commands.forEach(command -> {
			if (isValid(command, commandString) && command.getDescription() != null
					&& (command.getPermission() == null || sender.hasPermission(command.getPermission()))) {
				sender.sendMessage(commandHelp.replace("%syntaxe%", command.getSyntaxe()).replace("%description%",
						command.getDescription()));
			}
		});
	}

	public boolean isValid(VCommand command, String commandString) {
		return (command.getSubCommands().contains(commandString)
				|| (command.getParent() != null && command.getParent().getSubCommands().contains(commandString)));
	}

}