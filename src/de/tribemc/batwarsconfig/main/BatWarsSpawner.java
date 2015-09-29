package de.tribemc.batwarsconfig.main;

import org.bukkit.Location;

public class BatWarsSpawner {

	private String type;
	private Location loc;

	public BatWarsSpawner(String type, Location loc) {
		this.type = type;
		this.loc = loc.getBlock().getLocation().clone().add(0.5, 0.25, 0.5);

	}

	public String getType() {
		return this.type;
	}

	public Location getLocation() {
		return this.loc;
	}
}
