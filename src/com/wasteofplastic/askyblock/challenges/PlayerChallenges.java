package com.wasteofplastic.askyblock.challenges;

import java.util.List;
import java.util.Map;

public class PlayerChallenges {

	private final String name;
	private Map<Integer, Boolean> challenges;
	private ChallengeType type;

	/**
	 * @param name
	 * @param challenges
	 * @param type
	 */
	public PlayerChallenges(String name, Map<Integer, Boolean> challenges, ChallengeType type) {
		this.name = name;
		this.challenges = challenges;
		this.type = type;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the challenges
	 */
	public Map<Integer, Boolean> getChallenges() {
		return challenges;
	}

	/**
	 * @return the type
	 */
	public ChallengeType getType() {
		return type;
	}

	/**
	 * @param challenges
	 *            the challenges to set
	 */
	public void setChallenges(Map<Integer, Boolean> challenges) {
		this.challenges = challenges;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(ChallengeType type) {
		this.type = type;
	}

	public boolean can(ChallengeType type) {
		return (type.getId() <= this.type.getId());
	}

	public boolean hasFinish(int id) {
		return this.challenges.getOrDefault(id, false);
	}

	public boolean canUpdate(List<Challenge> challenges) {
		int can = 0;
		for (Challenge challenge : challenges)
			can += hasFinish(challenge.getId()) ? 1 : 0;
		return can == challenges.size();
	}

	public boolean canDo(Challenge challenge) {
		return challenge.getType().canDo(this.type);
	}

	// /*
	// * (non-Javadoc)
	// *
	// * @see java.lang.Object#toString()
	// */
	// @Override
	// public String toString() {
	// return "PlayerChallenges [name=" + name + ", challenges=" + challenges +
	// ", type=" + type + "]";
	// }

}
