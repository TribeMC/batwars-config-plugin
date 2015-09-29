package de.tribemc.batwarsconfig.main;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class BatWarsConfig {

	private List<BatWarsTeam> teams;
	private ItemStack display;
	private List<BatWarsSpawner> spawner;

	private int minplayer;
	private int maxplayer;
	private int maxteamplayer;

	private String name;
	private String world;

	private Location minLoc;
	private Location maxLoc;

	private Location endLoc;
	private Location specLoc;

	public BatWarsConfig(String world) {
		this.world = world;
		this.teams = new LinkedList<>();
		this.spawner = new LinkedList<>();
	}

	public void sendNext(Player p) {
		if (this.name == null) {
			p.sendMessage("Lege einen Namen mit §e/batwars name [Name] §rfest!");
			return;
		}
		p.sendMessage("Für die BatWarsWelt " + name
				+ " muss folgendes erledigt werden:");
		if (this.teams.size() == 0) {
			p.sendMessage("§9TeamAmount:\n§7Lege die Anzahl der Teams fest!\n§e/batwars teams [Integer]");

		} else if (this.spawner.size() == 0
				|| this.spawner.size() - 4 < this.teams.size() * 2) {
			p.sendMessage("§9Spawner\n§7Setze die einzelnen Spawner!"
					+ "\n§e/batwars spawner BRONZE feet "
					+ "\n§e/batwars spawner BRONZE head "
					+ "\n§e/batwars spawner EISEN feet "
					+ "\n§e/batwars spawner EISEN head "
					+ "\n§e/batwars spawner GOLD feet "
					+ "\n§e/batwars spawner GOLD head \n\n§7Die Location wird automatisch auf die Mitte des Blocks gesetzt");
		} else if (this.minLoc == null || this.maxLoc == null) {
			p.sendMessage("§9Boarder\n§7Setzte die Boarder!\n§e/batwars boarder");
		} else if (!allTeamsComplete()) {
			for (BatWarsTeam team : this.teams)
				if (!team.isComplete()) {
					team.sendNext(p);
					return;
				}
		} else if (specLoc == null) {
			p.sendMessage("§9SpectatorLocation:\n§7Lege die Spawn-Location der Spectator fest!\n§e/batwars specloc");

		} else if (endLoc == null) {
			p.sendMessage("§9EndLocation:\n§7Lege die Spawn-Location der Endshow fest!\n§e/batwars endloc");

		} else if (this.display == null) {
			p.sendMessage("§9Display-Item:\n§7Lege das Display- Item fest!\n§e/batwars display");

		} else {
			p.sendMessage("Scheinst endlich fertig zu sein! §e/batwars save");
		}
	}

	private boolean allTeamsComplete() {
		for (BatWarsTeam team : this.teams)
			if (!team.isComplete())
				return false;
		return true;
	}

	public void addBoarder(Location loc) {
		if (this.minLoc == null) {
			this.minLoc = loc.toVector().toBlockVector()
					.toLocation(loc.getWorld());
		} else if (this.maxLoc == null) {
			this.maxLoc = loc.toVector().toBlockVector()
					.toLocation(loc.getWorld());
			formateBoarder();
		} else {
			formateBoarder(loc.getBlockX(), loc.getBlockZ());
		}
	}

	private void formateBoarder(int blockX, int blockZ) {
		int minX = this.minLoc.getBlockX();
		int maxX = this.maxLoc.getBlockX();
		int minZ = this.minLoc.getBlockZ();
		int maxZ = this.maxLoc.getBlockZ();

		this.minLoc.setZ(Math.min(minZ, (Math.min(blockZ, maxZ))));
		this.maxLoc.setZ(Math.max(minZ, (Math.max(blockZ, maxZ))));

		this.minLoc.setX(Math.min(minX, (Math.min(blockX, maxX))));
		this.maxLoc.setX(Math.max(minX, (Math.max(blockX, maxX))));
	}

	private void formateBoarder() {
		int minX = this.minLoc.getBlockX();
		int maxX = this.maxLoc.getBlockX();
		int minZ = this.minLoc.getBlockZ();
		int maxZ = this.maxLoc.getBlockZ();

		this.minLoc.setZ(Math.min(minZ, maxZ));
		this.maxLoc.setZ(Math.max(minZ, maxZ));

		this.minLoc.setX(Math.min(minX, maxX));
		this.maxLoc.setX(Math.max(minX, maxX));

	}

	public void saveToFile() {
		File f = new File(this.world + "/config.yml");
		try {
			f.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		YamlConfiguration cfg = YamlConfiguration.loadConfiguration(f);
		cfg.set("Map.Name", this.name);
		cfg.set("Map.Display", this.display);
		cfg.set("Map.Player.Min", this.minplayer);
		cfg.set("Map.Player.Max", this.maxplayer);
		cfg.set("Map.Player.MaxTeam", this.maxteamplayer);

		// Boarder
		cfg.set("Map.Boarder.MinX", this.minLoc.getBlockX());
		cfg.set("Map.Boarder.MaxX", this.maxLoc.getBlockX());
		cfg.set("Map.Boarder.MinZ", this.minLoc.getBlockZ());
		cfg.set("Map.Boarder.MaxZ", this.maxLoc.getBlockZ());

		// SetLocation Spectator
		cfg.set("Map.Location.Spectator.GetX", this.specLoc.getX());
		cfg.set("Map.Location.Spectator.GetY", this.specLoc.getY());
		cfg.set("Map.Location.Spectator.GetZ", this.specLoc.getZ());
		cfg.set("Map.Location.Spectator.GetYaw", this.specLoc.getYaw());
		cfg.set("Map.Location.Spectator.GetPitch", this.specLoc.getPitch());

		// SetLocation End
		cfg.set("Map.Location.End.GetX", this.endLoc.getX());
		cfg.set("Map.Location.End.GetY", this.endLoc.getY());
		cfg.set("Map.Location.End.GetZ", this.endLoc.getZ());
		cfg.set("Map.Location.End.GetYaw", this.endLoc.getYaw());
		cfg.set("Map.Location.End.GetPitch", this.endLoc.getPitch());

		// Spawner
		for (int i = 0; i < this.spawner.size(); i++) {
			cfg.set("Map.Spawner.ID " + i + ".Type", this.spawner.get(i)
					.getType());
			cfg.set("Map.Spawner.ID " + i + ".Location.GetX",
					this.spawner.get(i).getLocation().getX());
			cfg.set("Map.Spawner.ID " + i + ".Location.GetY",
					this.spawner.get(i).getLocation().getY());
			cfg.set("Map.Spawner.ID " + i + ".Location.GetZ",
					this.spawner.get(i).getLocation().getZ());
		}

		// Teams
		for (int i = 0; i < this.teams.size(); i++) {
			BatWarsTeam t = this.teams.get(i);

			cfg.set("Team." + t.name() + ".Name", t.name());
			cfg.set("Team." + t.name() + ".Color", t.getColor().name());
			cfg.set("Team." + t.name() + ".ColorInt", t.getDataColor());
			// BatSpawn
			cfg.set("Team." + t.name() + ".Location.BatSpawn.GetX", t
					.getBatSpawn().getX());
			cfg.set("Team." + t.name() + ".Location.BatSpawn.GetY", t
					.getBatSpawn().getY());
			cfg.set("Team." + t.name() + ".Location.BatSpawn.GetZ", t
					.getBatSpawn().getZ());

			// TradeSpawn
			cfg.set("Team." + t.name() + ".Location.TradeSpawn.GetX", t
					.getTradeSpawn().getX());
			cfg.set("Team." + t.name() + ".Location.TradeSpawn.GetY", t
					.getTradeSpawn().getY());
			cfg.set("Team." + t.name() + ".Location.TradeSpawn.GetZ", t
					.getTradeSpawn().getZ());

			// PlayerSpawn
			cfg.set("Team." + t.name() + ".Location.PlayerSpawn.GetX", t
					.getPlayerSpawn().getX());
			cfg.set("Team." + t.name() + ".Location.PlayerSpawn.GetY", t
					.getPlayerSpawn().getY());
			cfg.set("Team." + t.name() + ".Location.PlayerSpawn.GetZ", t
					.getPlayerSpawn().getZ());
			cfg.set("Team." + t.name() + ".Location.PlayerSpawn.GetYaw", t
					.getPlayerSpawn().getYaw());
			cfg.set("Team." + t.name() + ".Location.PlayerSpawn.GetPitch", t
					.getPlayerSpawn().getPitch());
		}
		try {
			cfg.save(f);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void setTeamAmount(int i) {
		this.minplayer = i;
		this.maxplayer = i * 4;
		this.maxteamplayer = 4;
		if (i == 2) {
			this.teams.add(new BatWarsTeam("Rot", ChatColor.RED, 14));
			this.teams.add(new BatWarsTeam("Gruen", ChatColor.GREEN, 5));
		}
		if (i == 4) {

		}
	}

	public void addSpawner(BatWarsSpawner spawner) {
		this.spawner.add(spawner);
	}

	public String getWorldName() {
		return this.world;
	}

	public boolean isReady() {
		return (this.teams.size() != 0
				&& this.spawner.size() - 4 > this.teams.size() * 2
				&& this.minLoc != null && this.maxLoc != null && name != null
				&& this.endLoc != null && this.specLoc != null);
	}

	public void setName(String name2) {
		this.name = name2;
	}

	public BatWarsTeam getTeam(String string) {
		for (BatWarsTeam team : this.teams) {
			if (team.getName().equalsIgnoreCase(string))
				return team;
		}
		return null;
	}

	public List<BatWarsTeam> getTeams() {
		return this.teams;
	}

	public void setSpecLoc(Location location) {
		this.specLoc = location.add(0, 0.2, 0);
	}

	public void setEndLoc(Location location) {
		this.endLoc = location.add(0, 0.2, 0);
	}

	public String getName() {
		return this.name;
	}

	public void setDisplay(ItemStack itemInHand) {
		this.display = itemInHand.clone();
		ItemMeta im = this.display.getItemMeta();
		im.setDisplayName("§6§l" + this.name);
		this.display.setItemMeta(im);
	}
}
