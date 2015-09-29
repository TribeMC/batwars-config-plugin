package de.tribemc.batwarsconfig.main;

import java.io.File;

import org.bukkit.plugin.java.JavaPlugin;

import de.tribemc.batwarsconfig.commands.BatWarsCMD;

public class Main extends JavaPlugin {

	private BatWarsConfigurator bwconfig;

	@Override
	public void onEnable() {
		saveDefaultConfig();
		File f = new File("plugins/BatWarsConfig/maps");
		if (!f.exists())
			f.mkdir();
		this.bwconfig = new BatWarsConfigurator(this);

		getCommand("batwars").setExecutor(new BatWarsCMD(this));
		super.onEnable();
	}

	public BatWarsConfigurator getBWConfigurator() {
		return this.bwconfig;
	}

}
