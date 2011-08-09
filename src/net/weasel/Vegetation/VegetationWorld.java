package net.weasel.Vegetation;

import org.bukkit.World;
import org.bukkit.World.Environment;

public class VegetationWorld {

	public final World world;
	private Vegetation plugin;

	private Settings settings;
	public final PlayerList playerList;
	public final BlockCrawler blockCrawler;
	public final Cacti cacti;
	public final Canes canes;
	public final Grass grass;
	public final Plants plants;
	public final Grazers grazers;
	public final Moss moss;
	public final Vines vines;
	public final TallGrass tGrass;

	private int activePlayerCommands = 0;

	public VegetationWorld(Vegetation p, World w) {
		plugin = p;
		world = w;
		if (world.getEnvironment() == Environment.NETHER) {
			settings = null;
			playerList = new PlayerList(plugin, world);
			playerList.getActivePlayerList();
			blockCrawler = new BlockCrawler(settings);
			cacti = null;
			canes = null;
			grass = null;
			plants = null;
			grazers = null;
			moss = null;
			vines = null;
			tGrass = null;
		} else {
			settings = new Settings("plugins/Vegetation/" + w.getName() + ".ini");
			playerList = new PlayerList(plugin, world);
			playerList.getActivePlayerList();
			blockCrawler = new BlockCrawler(settings);
			cacti = new Cacti(blockCrawler);
			canes = new Canes(blockCrawler);
			grass = new Grass(blockCrawler, settings.maxGrassHeight);
			plants = new Plants(blockCrawler);
			grazers = new Grazers(world, settings);
			moss = new Moss(blockCrawler);
			vines = new Vines(blockCrawler);
			tGrass = new TallGrass(blockCrawler);
		}

		Vegetation.logOutput("Settings for world [" + w.getName() + "] loaded.");
	}

	public Settings getSettings() {
		return settings;
	}

	public void startTimer() {
		if (world.getEnvironment() != Environment.NETHER )
			Vegetation.timer.scheduleSyncRepeatingTask(plugin, new Timer(this), 10, 1);
	}

	public World getWorld() {
		return world;
	}

	public synchronized void increaseActivePlayerCommands() {
		activePlayerCommands++;
	}

	public synchronized void decreaseActivePlayerCommands() {
		if (activePlayerCommands > 0)
			activePlayerCommands--;
	}

	public int getActivePlayerCommands() {
		return activePlayerCommands;
	}
}
