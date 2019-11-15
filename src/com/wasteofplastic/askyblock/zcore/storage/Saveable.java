package com.wasteofplastic.askyblock.zcore.storage;

public interface Saveable {
	
	void save(Persist persist);
	void load(Persist persist);
}
