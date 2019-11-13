package com.wasteofplastic.askyblock.zcore;

public enum Message {

	PREFIX("�7[�bNeralia�7]"),

	NO_PERMISSION("�cVous n'avez pas la permission de faire cette commande."),
	
	ISLAND_CREATE("�aCr�ation de votre �le en cours, merci de choisir le type d'�le que vous voulez."),
	ISLAND_CREATE_1("�aEn cr�ant votre �le vous allez pouvoir vous d�velopper"),
	ISLAND_CREATE_2("�aSi vous avez besoin d'aide vous avez le �2/help �apour vous aider."),
	ISLAND_CREATE_3("�aPour tout autre question, vous pouvez rejoindre le discord."),
	ISLAND_CREATE_4("�4Merci de signaler tous les bugs que vous pouvez trouver directement sur le discord � un membre du staff �chttps://discord.gg/vuTRf7C"),
	ISLAND_CREATE_5("�cTout type d'insulte de provocation est interdit sur le serveur, si un joueur insult� merci de rapporter cela au staff sur le discord. "),
	
	ISLAND_RESET_ERROR("�cVous ne pouvez plus cr�er d'�le, merci de contacter un administrateur."),
	
	ISLAND_LOCK("�cImpossible d'entrer dans cette �le, elle est ferm� au public."),
	
	ISLAND_ENTER("�fVous entrez sur l'�le de �o%s"),
	ISLAND_LEAVE("�fVous quittez l'�le de �o%s"),
	
	ISLAND_LIMIT_HOPPER("�cVous ne pouvez pas placer plus de �6%s�c hoppers."),
	ISLAND_LIMIT_PISTON("�cVous ne pouvez pas placer plus de �6%s�c pistons."),
	
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
