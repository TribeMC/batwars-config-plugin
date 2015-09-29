package de.tribemc.batwarsconfig.main;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class BatWarsTeam {

	private String name;
	private ChatColor color;
	private int dcolor;

	private Location tradeSpawn;
	private Location batSpawn;
	private Location playerSpawn;

	public BatWarsTeam(String name, ChatColor color, int dcolor) {
		this.name = name;
		this.color = color;
		this.dcolor = dcolor;
	}

	public boolean isComplete() {
		return (tradeSpawn != null && batSpawn != null && playerSpawn != null);
	}

	public void sendNext(Player p) {
		p.sendMessage("Du musst folgendes für Team " + color + name
				+ " §rmachen:");
		if (this.tradeSpawn == null) {
			p.sendMessage("§9TradeSpawn: \n§7Setzte den Spawnpunkt für den Händler!\n§e/batwars team "
					+ name
					+ " tradespawn \n\n§7Der Spawnpunkt wird automatisch auf die Mitte des Blocks gesetzt");
		} else if (this.batSpawn == null) {
			p.sendMessage("§BatSpawn: \n§7Setzte den Spawnpunkt für die Fledermaus!\n§e/batwars team "
					+ name
					+ " batspawn head \n§e/batwars team "
					+ name
					+ " batspawn feet\n\n§7Der Spawnpunkt wird automatisch auf die Mitte des Blocks gesetzt");
		} else if (this.playerSpawn == null) {
			p.sendMessage("§PlayerSpawn: \n§7Setzte den Spawnpunkt für die Spieler!\n§e/batwars team "
					+ name
					+ " playerspawn \n\n§7Der Spawnpunkt wird automatisch auf die Mitte des Blocks gesetzt");
		} else {
			p.sendMessage("§9Es scheint alles erledigt zu sein...");
		}
	}

	public Location getBatSpawn() {
		return this.batSpawn;
	}

	public void setBatSpawn(Location loc) {
		this.batSpawn = loc.getBlock().getLocation().clone().add(0.5, 0.5, 0.5);
	}

	public Location getTradeSpawn() {
		return this.tradeSpawn;
	}

	public void setTradeSpawn(Location loc) {
		this.tradeSpawn = loc.getBlock().getLocation().clone().add(0.5, 0, 0.5);
	}

	public Location getPlayerSpawn() {
		return this.playerSpawn;
	}

	public void setPlayerSpawn(Location loc) {
		this.playerSpawn = loc.getBlock().getLocation().clone()
				.add(0.5, 0.125, 0.5);
		this.playerSpawn.setPitch(loc.getPitch());
		this.playerSpawn.setYaw(loc.getYaw());
	}

	public String name() {
		return this.name;
	}

	public String getName() {
		return name();
	}

	public ChatColor getColor() {
		return this.color;
	}

	public int getDataColor() {
		return this.dcolor;
	}

	@Override
	public String toString() {
		return "[" + color + name + "§r]";
	}
}
