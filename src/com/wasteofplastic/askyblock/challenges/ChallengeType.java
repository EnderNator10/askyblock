package com.wasteofplastic.askyblock.challenges;

public enum ChallengeType {

	DEFAULT("D�butant", 0),

	COMPETANT("Comp�tant", 1),

	EXPERT("Expert", 2),

	ADVANCED("Avanc�", 3),

	ELITE("Elite", 4),

	END("End", 5),

	;

	private final String name;
	private final int id;

	private ChallengeType(String name, int id) {
		this.name = name;
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public ChallengeType getNext() {
		return this == DEFAULT ? ChallengeType.COMPETANT
				: this == ChallengeType.COMPETANT ? ChallengeType.EXPERT
						: this == EXPERT ? ADVANCED : this == ChallengeType.ADVANCED ? ELITE : END;
	}

}
