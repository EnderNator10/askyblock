package com.wasteofplastic.askyblock.command;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import com.wasteofplastic.askyblock.ASkyBlock;
import com.wasteofplastic.askyblock.command.commands.challenges.CommandChallenge;
import com.wasteofplastic.askyblock.command.commands.challenges.CommandChallengeHelp;
import com.wasteofplastic.askyblock.command.commands.challenges.CommandChallengeReset;
import com.wasteofplastic.askyblock.command.commands.island.IslandCommand;
import com.wasteofplastic.askyblock.command.commands.island.IslandCommandAccept;
import com.wasteofplastic.askyblock.command.commands.island.IslandCommandBan;
import com.wasteofplastic.askyblock.command.commands.island.IslandCommandBanList;
import com.wasteofplastic.askyblock.command.commands.island.IslandCommandBiome;
import com.wasteofplastic.askyblock.command.commands.island.IslandCommandConfirm;
import com.wasteofplastic.askyblock.command.commands.island.IslandCommandExpel;
import com.wasteofplastic.askyblock.command.commands.island.IslandCommandFly;
import com.wasteofplastic.askyblock.command.commands.island.IslandCommandGo;
import com.wasteofplastic.askyblock.command.commands.island.IslandCommandInvite;
import com.wasteofplastic.askyblock.command.commands.island.IslandCommandKick;
import com.wasteofplastic.askyblock.command.commands.island.IslandCommandLeave;
import com.wasteofplastic.askyblock.command.commands.island.IslandCommandLock;
import com.wasteofplastic.askyblock.command.commands.island.IslandCommandMake;
import com.wasteofplastic.askyblock.command.commands.island.IslandCommandMakeLeader;
import com.wasteofplastic.askyblock.command.commands.island.IslandCommandReStart;
import com.wasteofplastic.askyblock.command.commands.island.IslandCommandReject;
import com.wasteofplastic.askyblock.command.commands.island.IslandCommandSetHome;
import com.wasteofplastic.askyblock.command.commands.island.IslandCommandSettings;
import com.wasteofplastic.askyblock.command.commands.island.IslandCommandSpawn;
import com.wasteofplastic.askyblock.command.commands.island.IslandCommandTeam;
import com.wasteofplastic.askyblock.command.commands.island.IslandCommandTeamChat;
import com.wasteofplastic.askyblock.command.commands.island.IslandCommandUnBan;
import com.wasteofplastic.askyblock.command.commands.island.IslandCommandWarp;
import com.wasteofplastic.askyblock.command.commands.island.IslandCommandWarps;
import com.wasteofplastic.askyblock.command.commands.island.coop.IslandCommandCoop;
import com.wasteofplastic.askyblock.command.commands.island.coop.IslandCommandCoopAccept;
import com.wasteofplastic.askyblock.command.commands.island.coop.IslandCommandCoopList;
import com.wasteofplastic.askyblock.command.commands.island.coop.IslandCommandCoopReject;
import com.wasteofplastic.askyblock.command.commands.island.coop.IslandCommandUnCoop;
import com.wasteofplastic.askyblock.command.commands.island.mics.IslandCommandAbout;
import com.wasteofplastic.askyblock.command.commands.island.mics.IslandCommandHelp;
import com.wasteofplastic.askyblock.command.commands.island.mics.IslandCommandLevel;
import com.wasteofplastic.askyblock.command.commands.island.mics.IslandCommandTop;
import com.wasteofplastic.askyblock.command.commands.island.mics.IslandCommandValue;
import com.wasteofplastic.askyblock.command.commands.island.name.IslandCommandName;
import com.wasteofplastic.askyblock.command.commands.island.name.IslandCommandResetName;
import com.wasteofplastic.askyblock.zcore.Logger.LogType;

public class CommandManager implements CommandExecutor, TabExecutor {

	private final ASkyBlock main;
	private final List<VCommand> commands = new ArrayList<VCommand>();

	public CommandManager(ASkyBlock template) {
		this.main = template;
		this.registerCommands();
	}

	private void registerCommands() {

		/**
		 * Island
		 */

		VCommand island = addCommand("island", new IslandCommand());
		addCommand(new IslandCommandHelp().setParent(island));
		addCommand(new IslandCommandAbout().setParent(island));
		addCommand(new IslandCommandGo().setParent(island));
		addCommand(new IslandCommandFly().setParent(island));
		addCommand(new IslandCommandValue().setParent(island));
		addCommand(new IslandCommandResetName().setParent(island));
		addCommand(new IslandCommandTeamChat().setParent(island));
		addCommand(new IslandCommandBanList().setParent(island));
		addCommand(new IslandCommandLevel().setParent(island));
		addCommand(new IslandCommandWarps().setParent(island));
		addCommand(new IslandCommandWarp().setParent(island));
		addCommand(new IslandCommandInvite().setParent(island));
		addCommand(new IslandCommandAccept().setParent(island));
		addCommand(new IslandCommandReject().setParent(island));
		addCommand(new IslandCommandLeave().setParent(island));
		addCommand(new IslandCommandName().setParent(island));
		addCommand(new IslandCommandCoopList().setParent(island));
		addCommand(new IslandCommandBiome().setParent(island));
		addCommand(new IslandCommandSetHome().setParent(island));
		addCommand(new IslandCommandConfirm().setParent(island));
		addCommand(new IslandCommandReStart().setParent(island));
		addCommand(new IslandCommandLock().setParent(island));
		addCommand(new IslandCommandSettings().setParent(island));
		addCommand(new IslandCommandMake().setParent(island));
		addCommand(new IslandCommandTeam().setParent(island));
		addCommand(new IslandCommandCoopReject().setParent(island));
		addCommand(new IslandCommandCoopAccept().setParent(island));
		addCommand(new IslandCommandTop().setParent(island));
		addCommand(new IslandCommandSpawn().setParent(island));
		addCommand(new IslandCommandUnBan().setParent(island));
		addCommand(new IslandCommandBan().setParent(island));
		addCommand(new IslandCommandUnCoop().setParent(island));
		addCommand(new IslandCommandExpel().setParent(island));
		addCommand(new IslandCommandCoop().setParent(island));
		addCommand(new IslandCommandKick().setParent(island));
		addCommand(new IslandCommandMakeLeader().setParent(island));

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
		main.getCommand(string).setTabCompleter(this);
		return command;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		for (VCommand command : commands) {
			if (command.getSubCommands().contains(cmd.getName().toLowerCase())) {
				if ((args.length == 0 || command.isIgnoreParent()) && command.getParent() == null) {
					CommandType type = processRequirements(command, sender, cmd, label, args);
					if (!type.equals(CommandType.CONTINUE))
						return true;
				}
			} else if (args.length >= 1 && command.getParent() != null
					&& command.getParent().getSubCommands().contains(cmd.getName().toLowerCase())
					&& canExecute(args, cmd.getName().toLowerCase(), command)) {
				CommandType type = processRequirements(command, sender, cmd, label, args);
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
	 * On verifie les arguments de la commande
	 * 
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

	private final String prefix = "§7[§bNeralia§7]";

	private final String commandHelp = "§a» §2%syntaxe% §8- §7%description%";
	private final String noPermission = "§cVous n'avez pas la permission";
	private final String syntaxeError = "§cVous devez exécuter la commande comme ceci§7: §a%command%";
	private final String commandError = "§cCet argument n'existe pas !";
	private final String onlinePlayerCanUse = "§cYou must be player to do this !";

	/**
	 * Exécution de la commande
	 * 
	 * @param command
	 * @param sender
	 * @param strings
	 * @return
	 */
	private CommandType processRequirements(VCommand command, CommandSender sender, Command cmd, String label,
			String[] strings) {

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
			command.setCommand(cmd);
			command.setLabel(label);
			CommandType returnType = command.perform(main);
			if (returnType == CommandType.SYNTAX_ERROR) {
				sender.sendMessage(prefix + " " + syntaxeError.replace("%command%", command.getSyntaxe()));
			} else if (returnType == CommandType.EXCEPTION_ERROR) {
				sender.sendMessage(prefix + " §cUne erreur est survenu lors de l'éxecution de la commande !");
			}
			return returnType;
		}
		sender.sendMessage(prefix + " " + noPermission);
		return CommandType.DEFAULT;
	}

	public List<VCommand> getCommands(String string) {
		return commands.stream().filter(command -> isValid(command, string)).collect(Collectors.toList());
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

	public boolean isValid(VCommand command, String defaultCommand) {
		return command.getParent() == null ? command.getSubCommands().contains(defaultCommand.toLowerCase())
				: isValid(command.getParent(), defaultCommand);
	}

	private List<String> cmds = new ArrayList<>();

	private List<String> getCmds() {
		if (cmds.size() == 0) {
			commands.forEach(command -> {
				if (command.getParent() != null && command.getParent().getSubCommands().contains("island"))
					cmds.add(command.getSubCommands().get(0));
			});
		}
		return cmds;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String command, String[] args) {

		if (cmd.getName().equalsIgnoreCase("island") || cmd.getName().equalsIgnoreCase("is")) {
			return getCmds();
		}

		return null;
	}

}