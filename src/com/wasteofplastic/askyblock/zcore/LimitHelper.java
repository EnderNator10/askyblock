package com.wasteofplastic.askyblock.zcore;

public enum LimitHelper {

	HOPPER(200),
	MEMBER(8),
	ISLAND(350),
	PISTON(1000),
	
	;
	
	private final int limit;

	private LimitHelper(int limit) {
		this.limit = limit;
	}

	/**
	 * @return the limit
	 */
	public int getLimit() {
		return limit;
	}
	
	
	
}
