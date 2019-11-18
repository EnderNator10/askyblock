package com.wasteofplastic.askyblock.zcore;

public enum Message {

	PREFIX("§7[§bNeralia§7]"),

	NO_PERMISSION("§cVous n'avez pas la permission de faire cette commande."),
	
	ISLAND_CREATE("§aCréation de votre île en cours, merci de choisir le type d'île que vous voulez."),
	ISLAND_CREATE_1("§aEn créant votre île vous allez pouvoir vous développer"),
	ISLAND_CREATE_2("§aSi vous avez besoin d'aide vous avez le §2/help §apour vous aider."),
	ISLAND_CREATE_3("§aPour tout autre question, vous pouvez rejoindre le discord."),
	ISLAND_CREATE_4("§4Merci de signaler tous les bugs que vous pouvez trouver directement sur le discord à un membre du staff §chttps://discord.gg/vuTRf7C"),
	ISLAND_CREATE_5("§cTout type d'insulte de provocation est interdit sur le serveur, si un joueur insulté merci de rapporter cela au staff sur le discord. "),
	
	ISLAND_RESET_ERROR("§cVous ne pouvez plus créer d'île, merci de contacter un administrateur."),
	
	ISLAND_LOCK("§cImpossible d'entrer dans cette île, elle est fermée au public."),
	
	ISLAND_ENTER("§fVous entrez sur l'île de §o%s"),
	ISLAND_LEAVE("§fVous quittez l'île de §o%s"),
	
	ISLAND_LIMIT_HOPPER("§cVous ne pouvez pas placer plus de §6%s§c hoppers."),
	ISLAND_LIMIT_PISTON("§cVous ne pouvez pas placer plus de §6%s§c pistons."),
	
	CHALLENGE_ERROR("§cVous ne pouvez pas passer ce challenge."),
	CHALLENGE_SUCCESS("§aVous venez de terminer le challenge §2%s§a."),
	CHALLENGE_LEVEL_UP("§aVous venez de débloquer les challengs §2%s§a."),
	CHALLENGE_ALREADY("§aVous avec déjà terminé ce challenge !"),
	CHALLENGE_DELETE_SUCCES("§aVous venez de supprimer les challenges de §2%s§a."),
	CHALLENGE_DELETE_ERROR("§cLe joueur §6%s§c est introuvable."),
	
	HOPPER_PLACE("§aVous venez de placer un hopper."),
	HOPPER_REMOVE("§cVous venez de détruire un hopper !"),
	
	;

	private final String message;

	private Message(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public static String toMessage(Message message) {
		return PREFIX.getMessage() + " " + message.getMessage();
	}

	public static String toMessage(Message message, Object... args) {
		return String.format(toMessage(message), args);
	}

}
