package net.weasel.Vegetation;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.weasel.Vegetation.commands.BiomeInfoCommand;
import net.weasel.Vegetation.commands.GrowCommand;
import net.weasel.Vegetation.commands.MowCommand;
import net.weasel.Vegetation.commands.PurgeCommand;

import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.Event.Type;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import com.nijiko.permissions.PermissionHandler;
import com.nijikokun.bukkit.Permissions.Permissions;

public class Vegetation extends JavaPlugin
{
	public static Logger Log = Logger.getLogger("Minecraft");
	public static PermissionHandler Permissions;
	public static BukkitScheduler timer;
	public static VegetationPlayerListener PlayerListener;
	public static VegetationBlockListener BlockListener;
	public static PluginManager pm;
	
	// Plugin stuff..
	public static String pluginName = "";
	public static String pluginVersion = "";
	public static boolean debugging = true;

	// System stuff
	public static HashMap<String,VegetationWorld> vWorlds = new HashMap<String,VegetationWorld>();
	public static Random generator = new Random();
	
	private boolean foundPermissions = false;
	
	@Override
    public void onEnable() 
    {
		pm = getServer().getPluginManager();
		PlayerListener = new VegetationPlayerListener(this);
		BlockListener = new VegetationBlockListener(this);
		
		pluginName = this.getDescription().getName();
		pluginVersion = this.getDescription().getVersion();
		timer = getServer().getScheduler();
		
		pm.registerEvent(Type.PLAYER_INTERACT, PlayerListener, Event.Priority.Normal, this);
		pm.registerEvent(Type.PLAYER_QUIT, PlayerListener, Event.Priority.Normal, this);
		pm.registerEvent(Type.PLAYER_JOIN, PlayerListener, Event.Priority.Normal, this);
		pm.registerEvent(Type.PLAYER_TELEPORT, PlayerListener, Event.Priority.Normal, this);
		pm.registerEvent(Type.PLAYER_MOVE, PlayerListener, Event.Priority.Low, this);
		pm.registerEvent(Type.BLOCK_BREAK, BlockListener, Event.Priority.Normal, this);
		pm.registerEvent(Type.BLOCK_PHYSICS, BlockListener, Event.Priority.Normal, this);
		pm.registerEvent(Type.LEAVES_DECAY, BlockListener, Event.Priority.Normal, this);

		
		//enable permission and register commands
		setupPermissions();
		getCommand("grow").setExecutor(new GrowCommand(this));
        getCommand("mow").setExecutor(new MowCommand(this));
        getCommand("purge").setExecutor(new PurgeCommand(this));
		getCommand("biome").setExecutor(new BiomeInfoCommand(this));
		
		if( new File("plugins/Vegetation/").exists() == false )
		{
			logOutput( "Settings folder does not exist - creating it.. ");
			
			boolean chk = new File("plugins/Vegetation/").mkdir();
			
			if( chk )
				logOutput( "Successfully created folder." );
			else
				logOutput( "Unable to create folder!" );
		}
		
		//create VegetationWorld objects for loaded worlds
		loadWorldSettings();

		debugging = new File("plugins/Vegetation/debug.txt").exists();
		if( debugging ) logOutput( "debugging is enabled." );
		
        logOutput( pluginName + " v" + pluginVersion + " enabled." );
        
        //start timers
        Iterator<String> it = vWorlds.keySet().iterator();
        while( it.hasNext() )
        {
        	vWorlds.get(it.next()).startTimer();
        }
    }
    
    @Override
    public void onDisable() 
    {
    	timer.cancelAllTasks();
        logOutput( "Plugin disabled: "+pluginName+" version "+pluginVersion);
    }
    
    public void loadWorldSettings()
    {
    	vWorlds.clear();
    	for( World w: this.getServer().getWorlds() )
    	{
    		if( w.getEnvironment() == Environment.NORMAL )
    			vWorlds.put(w.getName(), new VegetationWorld(this, w));
    	}
    }
    
	public boolean setupPermissions()
	{
	      Plugin permissions = this.getServer().getPluginManager().getPlugin("Permissions");

	      if(permissions != null)
	      {
	    	  Permissions = ((Permissions)permissions).getHandler();
	    	  logOutput("Permissions found!");
	    	  foundPermissions = true;
	    	  return true;
	      }
	      logOutput( "Permission system not detected." );
	      return false;
	}

    public static void logOutput(String text)
    {
    	Log.log( Level.INFO, "[" + pluginName + "]: " + text );
    }
    
    public boolean hasPermission(Player player, String node)
    {
    	if(foundPermissions)
    	{
    		return Permissions.has(player, node);
    	}
    	else
    	{
    		return player.isOp();
    	}
    }
}
