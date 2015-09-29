package de.tribemc.batwarsconfig.commands;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.tribemc.batwarsconfig.main.BatWarsConfig;
import de.tribemc.batwarsconfig.main.BatWarsConfigurator;
import de.tribemc.batwarsconfig.main.BatWarsSpawner;
import de.tribemc.batwarsconfig.main.BatWarsTeam;
import de.tribemc.batwarsconfig.main.Main;

public class BatWarsCMD implements CommandExecutor {

	BatWarsConfigurator bw;

	public BatWarsCMD(Main m) {
		this.bw = m.getBWConfigurator();
	}

	// TODO 1
	// batwars setup
	// batwars boarder
	// batwars help

	// TODO 2
	// batwars name NAME
	// batwars teams [2 oder 4]
	// TODO 3
	// batwars team NAME tradespawn
	// batwars team NAME playerspawn
	// batwars spawner BRONZE feed
	// batwars spawner BRONZE head
	// batwars spawner EISEN feed
	// batwars spawner EISEN head
	// batwars spawner GOLD feed
	// batwars spawner GOLD head

	// TODO 4
	// batwars team NAME batspawn feet
	// batwars team NAME batspawn head

	@Override
	public boolean onCommand(CommandSender cs, Command arg1, String arg2,
			String[] args) {
		if (!(cs instanceof Player)) {
			cs.sendMessage("NeedToBeAPlayer");
			return true;
		}
		Player p = (Player) cs;
		World w = p.getWorld();
		if (args.length == 0) {
			cs.sendMessage("Nutze §e/batwars help");
		} else if (args.length == 1) {
			if (args[0].equalsIgnoreCase("setup")) {
				if (bw.isInConfig(w)) {
					p.sendMessage("ist bereits in config");
				} else if (bw.hasConfigFile(w)) {
					p.sendMessage("Wurde bereits configuriert!");
				} else {
					BatWarsConfig cfg = bw.startConfig(w);
					p.sendMessage("Die Configuration für die Welt "
							+ w.getName() + " wurde gestartet!");
					cfg.sendNext(p);
				}
			} else if (args[0].equalsIgnoreCase("next")) {
				if (!bw.isInConfig(w)) {
					p.sendMessage("Diese Welt wird nicht configuriert");
					return true;
				}
				bw.getConfig(w).sendNext(p);
			} else if (args[0].equalsIgnoreCase("save")) {
				if (!bw.isInConfig(w)) {
					p.sendMessage("Diese Welt wird nicht configuriert");
					return true;
				}
				BatWarsConfig cfg = bw.getConfig(w);
				if (!cfg.isReady()) {
					p.sendMessage("Die Welt ist nicht fertig configuriert");
					return true;
				}
				w.save();
				p.sendMessage("Die Welt wurde gespeichert.");
				cfg.saveToFile();
				p.sendMessage("Config wurde gespeichert!");
				bw.copy(w, cfg.getName());
				p.sendMessage("Map in entsprechenden Ordner kopiert");
				bw.removeConfig(cfg);
				p.sendMessage("Configuration der Map erfolgreich beendet!");
			} else if (args[0].equalsIgnoreCase("boarder")) {
				if (!bw.isInConfig(w)) {
					p.sendMessage("Diese Welt wird nicht configuriert");
					return true;
				}
				bw.getConfig(w).addBoarder(p.getLocation());
				p.sendMessage("Boarder Punkt wurde hinzugefügt");
			} else if (args[0].equalsIgnoreCase("specloc")) {
				if (!bw.isInConfig(w)) {
					p.sendMessage("Diese Welt wird nicht configuriert");
					return true;
				}
				bw.getConfig(w).setSpecLoc(p.getLocation());
				p.sendMessage("Die Location für die Spectator wurde gesetzt!");
			} else if (args[0].equalsIgnoreCase("endloc")) {
				if (!bw.isInConfig(w)) {
					p.sendMessage("Diese Welt wird nicht configuriert");
					return true;
				}
				bw.getConfig(w).setEndLoc(p.getLocation());
				p.sendMessage("Die Location für die EndShow wurde gesetzt!");
			} else if (args[0].equalsIgnoreCase("display")) {
				if (!bw.isInConfig(w)) {
					p.sendMessage("Diese Welt wird nicht configuriert");
					return true;
				}
				bw.getConfig(w).setDisplay(p.getItemInHand());
				p.sendMessage("Das Item in deiner Hand wurde als DisplayItem gesetzt");
			} else {
				p.sendMessage("Starte die Config mit §e/batwars setup §rfür den nächsten Schritt nutze §e/batwars next");
			}
		} else if (args.length >= 2) {
			if (!bw.isInConfig(w)) {
				p.sendMessage("Diese Welt wird nicht configuriert");
				return true;
			}
			BatWarsConfig cfg = bw.getConfig(w);
			if (args[0].equalsIgnoreCase("name")) {
				String name = "";
				for (int i = 1; i < args.length; i++) {
					name += args[i] + ((i + 1 == args.length) ? "" : " ");
				}
				if (bw.isSaved(name)) {
					p.sendMessage("Die Map wurde bereits gespeichert!");
					return true;
				}
				if (name.length() > 14) {
					p.sendMessage("Der Map-Name darf maximal 14 Zeichen betragen!");
					return true;
				}
				cfg.setName(name);
				p.sendMessage("Der Name der BatWarsWelt wurde auf " + name
						+ " §rgesetzt");
				return true;
			} else if (args[0].equalsIgnoreCase("teams")) {
				try {
					Integer i = Integer.valueOf(args[1]);
					if (i != 2) {
						p.sendMessage("Bitte erstelle nur Maps für 2 Teams!");
						return true;
					}
					cfg.setTeamAmount(i);
					p.sendMessage("Die Menge der Teams wurde auf " + i
							+ " gesetzt!");
				} catch (NumberFormatException e) {
					p.sendMessage("Kein Integer aus " + args[1]);
				}
				return true;
			}
			if (args.length >= 3) {
				if (args[0].equalsIgnoreCase("spawner")) {
					String type = args[1];
					if (!type.equals("BRONZE") && !type.equals("EISEN")
							&& !type.equals("GOLD")) {
						p.sendMessage("Du kannst Spawner nur für BRONZE EISEN oder GOLD einstellen!");
						return true;
					}
					Location loc = p.getLocation();
					if (args[2].equalsIgnoreCase("head")) {
						loc = p.getEyeLocation();
					}
					cfg.addSpawner(new BatWarsSpawner(type, loc));
					p.sendMessage("Ein Spanwer vom Typ " + type
							+ " wurde hinzugefügt!");
				} else if (args[0].equalsIgnoreCase("team")) {
					BatWarsTeam team = cfg.getTeam(args[1]);
					if (team == null) {
						p.sendMessage("Diese TeamFarbe wird nicht configuriert!\n"
								+ cfg.getTeams().toString());
						return true;
					}
					if (args[2].equalsIgnoreCase("tradespawn")) {
						Location loc = p.getLocation();
						if (args.length > 3 && args[3].equalsIgnoreCase("head")) {
							loc = p.getEyeLocation();
						}
						team.setTradeSpawn(loc);
						p.sendMessage("Tradespawn für " + team + " gesetzt");
					} else if (args[2].equalsIgnoreCase("batspawn")) {
						Location loc = p.getLocation();
						if (args.length > 3 && args[3].equalsIgnoreCase("head")) {
							loc = p.getEyeLocation();
						}
						team.setBatSpawn(loc);
						p.sendMessage("BatSpawn für " + team + " gesetzt");
					} else if (args[2].equalsIgnoreCase("playerspawn")) {
						Location loc = p.getLocation();
						if (args.length > 3 && args[3].equalsIgnoreCase("head")) {
							loc = p.getEyeLocation();
						}
						team.setPlayerSpawn(loc);
						p.sendMessage("PlayerSpawn für " + team + " gesetzt");
					} else {
						p.sendMessage("Nutze §e/batwars help");
					}

				} else {
					p.sendMessage("Nutze §e/batwars help");
				}
			} else {
				p.sendMessage("Nutze §e/batwars help");
			}
		} else {
			p.sendMessage("Nutze §e/batwars help");
		}
		return false;
	}
}
