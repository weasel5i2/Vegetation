package net.weasel.Vegetation;

import org.bukkit.World;

public class VegetationWorld {
	
	public void logOutput( String text ) { Vegetation.logOutput( text ); }
	
	private World world;
	private Vegetation plugin;
	private Mutex mutex;
	
	private Settings settings;
	public final PlayerList playerList;
	public final Blocks blocks;
	public final Cacti cacti;
	public final Canes canes;
	public final Grass grass;
	public final Plants plants;
	public final Grazers grazers;
	public final Moss moss;
	public final Vines vines;
	
	private int activePlayerCommands = 0;
	
	public VegetationWorld(Vegetation p, World w)
	{
		plugin = p;
		world = w;
		String wName = w.getName();
		settings = new Settings("plugins/Vegetation/" + wName + ".ini");
		playerList = new PlayerList(plugin, world);
		playerList.getActivePlayerList();
		blocks = new Blocks(settings);
		cacti = new Cacti(blocks);
		canes = new Canes(blocks);
		grass = new Grass(blocks, settings.maxGrassHeight);
		plants = new Plants(blocks);
		grazers = new Grazers(world, settings);
		moss = new Moss(blocks);
		vines = new Vines(blocks);
		mutex = new Mutex();
	}
	
	public Settings getSettings()
	{
		return settings;
	}
	
	public void startTimer()
	{
		Vegetation.timer.scheduleSyncRepeatingTask(plugin, new Timer(this), 10, 1);
	}
	
	public World getWorld()
	{
		return world;
	}
	
	public void increaseActivePlayerCommands()
	{
		mutex.aquire();
		activePlayerCommands++;
		mutex.release();
	}
	
	public void decreaseActivePlayerCommands()
	{
		mutex.aquire();
		if( activePlayerCommands > 0 ) activePlayerCommands--;
		mutex.release();
	}
	
	public int getActivePlayerCommands()
	{
		return activePlayerCommands;
	}
}
