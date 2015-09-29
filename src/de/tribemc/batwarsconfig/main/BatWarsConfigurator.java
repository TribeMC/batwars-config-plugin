package de.tribemc.batwarsconfig.main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.LinkedList;
import java.util.List;

import org.bukkit.World;

public class BatWarsConfigurator {

	private List<BatWarsConfig> configs;
	private Main m;

	public BatWarsConfigurator(Main m) {
		this.m = m;
		this.configs = new LinkedList<>();
	}

	public BatWarsConfig getConfig(World w) {
		return getConfig(w.getName());
	}

	public BatWarsConfig getConfig(String world) {
		for (BatWarsConfig config : this.configs)
			if (config.getWorldName().equals(world))
				return config;
		return null;
	}

	public boolean isInConfig(String name) {
		return getConfig(name) != null;
	}

	public boolean isInConfig(World w) {
		return isInConfig(w.getName());
	}

	public boolean hasConfigFile(String name) {
		return new File(name + "/config.yml").exists();
	}

	public boolean hasConfigFile(World w) {
		return hasConfigFile(w.getName());
	}

	public Main getMain() {
		return this.m;
	}

	public BatWarsConfig startConfig(World w) {
		BatWarsConfig cfg = new BatWarsConfig(w.getName());
		this.configs.add(cfg);
		return cfg;
	}

	public boolean isSaved(String name) {
		return new File("plugins/BatWarsConfig/maps/" + name).exists();
	}

	public void copy(World w, String name) {
		try {
			File to = new File("plugins/BatWarsConfig/maps/" + name);
			copyDirectory(new File(w.getName()), to);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void removeConfig(BatWarsConfig cfg) {
		this.configs.remove(cfg);
	}

	private void copyDirectory(File sourceLocation, File targetLocation)
			throws IOException {

		if (sourceLocation.isDirectory()) {
			if (!targetLocation.exists()) {
				targetLocation.mkdir();
			}

			String[] children = sourceLocation.list();
			for (int i = 0; i < children.length; i++) {
				copyDirectory(new File(sourceLocation, children[i]), new File(
						targetLocation, children[i]));
			}
		} else {

			InputStream in = new FileInputStream(sourceLocation);
			OutputStream out = new FileOutputStream(targetLocation);

			byte[] buf = new byte[1024];
			int len;
			while ((len = in.read(buf)) > 0) {
				out.write(buf, 0, len);
			}
			in.close();
			out.close();
		}
	}
}
