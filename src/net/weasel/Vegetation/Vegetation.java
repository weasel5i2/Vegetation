package net.weasel.Vegetation;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.World.Environment;
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
	public static final Logger Log = Logger.getLogger("Minecraft");
	public static PermissionHandler Permissions;
	public static BukkitScheduler timer;
	public static Vegetation plugin;
	public static Server server;
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
	
	@Override
    public void onEnable() 
    {
		plugin = this;
		server = plugin.getServer();
		pm = server.getPluginManager();
		PlayerListener = new VegetationPlayerListener(plugin);
		BlockListener = new VegetationBlockListener(plugin);
		
		pluginName = this.getDescription().getName();
		pluginVersion = this.getDescription().getVersion();
		timer = plugin.getServer().getScheduler();
		
		pm.registerEvent(Type.PLAYER_INTERACT, PlayerListener, Event.Priority.Normal, plugin);
		pm.registerEvent(Type.PLAYER_QUIT, PlayerListener, Event.Priority.Normal, plugin);
		pm.registerEvent(Type.PLAYER_LOGIN, PlayerListener, Event.Priority.Normal, plugin);
		pm.registerEvent(Type.PLAYER_TELEPORT, PlayerListener, Event.Priority.Normal, plugin);
		pm.registerEvent(Type.PLAYER_MOVE, PlayerListener, Event.Priority.Low, plugin);
		//pm.registerEvent(Type.BLOCK_BREAK, BlockListener, Event.Priority.Normal, plugin);
		
		
		//create VegetationWorld objects for loaded worlds
		loadWorldSettings();
		
		//enable permission and register commands
		if( setupPermissions() )
		{
			getCommand("grow").setExecutor(new GrowCommand(this));
	        //getCommand("growall").setExecutor(new GrowAllCommand(this));
	        getCommand("mow").setExecutor(new MowCommand(this));
		}
		
		if( new File("plugins/Vegetation/").exists() == false )
		{
			logOutput( "Settings folder does not exist - creating it.. ");
			
			boolean chk = new File("plugins/Vegetation/").mkdir();
			
			if( chk )
				logOutput( "Successfully created folder." );
			else
				logOutput( "Unable to create folder!" );
		}

		debugging = new File("plugins/Vegetation/debug.txt").exists();
		if( debugging ) logOutput( "debugging is enabled." );
		
        logOutput( pluginName + " v" + pluginVersion + " enabled." );
    	
        /*if( !enableGrass && !enableGrazers && !enableMoss && !enablePlants )
        {
        	logOutput( "No vegetation is enabled. Disabling plugin." );
            this.getPluginLoader().disablePlugin(this);
        }
        else if( !growForestBiome && !growRainforestBiome && ! growShrublandBiome &&
        		 !growSavannahBiome && !growPlainsBiome && !growSeasonalForestBiome &&
        		 !growIceDesertBiome && !growHellBiome && !growDesertBiome &&
        		 !growDesertBiome && !growSwamplandBiome && !growTaigaBiome && !growTundraBiome )
        {
        	logOutput( "No Biome Type is enabled. Disabling plugin." );
        	this.getPluginLoader().disablePlugin(this);
        }
        else
        {
            tTask = setupTimerTask( 10, 1 );
        }*/
        
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
    	for( World w: plugin.getServer().getWorlds() )
    	{
    		if( w.getEnvironment() == Environment.NORMAL )
    			vWorlds.put(w.getName(), new VegetationWorld(plugin, w));
    	}
    }
    
	public static boolean setupPermissions()
	{
	      Plugin test = plugin.getServer().getPluginManager().getPlugin("Permissions");

	      if(Permissions == null)
	      {
	          if(test != null)
	          {
	        	  Permissions = ((Permissions)test).getHandler();
	        	  logOutput( "Permissions found!" );
	        	  return true;
	          }
	          else
	          {
	              logOutput( "Permission system not detected. Commands won't work like this!" );
	              return false;
	          }
	      }
	      return false;
	}
    
	/*public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) 
    {
    	String pCommand = command.getName().toLowerCase();
    	
    	if( pCommand.equals( "mow" ) )
    	{
    		Grass.mowTheGrass( (Player) sender, 1, false );
    		sender.sendMessage( "You have cut down the grass." );
    	}
    	
    	if( pCommand.equals( "grow" )  )
    	{
    		//Todo: implement command queue
    		if( ActivePlayerCommands < 20 )
    		{
    			ActivePlayerCommands++;
    			sender.sendMessage( "Growing everything.." );
    			for( int I = 0; I < 100; I++ )
    			{
    				Block CB = Blocks.getRandomBlock( ((Player) sender).getLocation() );

    				if ( CB != null )
    				{
    					switch( CB.getType() )
    					{
    					case GRASS:
    						if( Vegetation.debugging ) logOutput( "Found Block of Type: " + Material.GRASS );
    						Grass.growGrass( CB );
    						break;

    					case CACTUS:
    						if( Vegetation.debugging ) logOutput( "Found Block of Type: " + Material.CACTUS );
    						Cacti.growCacti( CB );
    						break;

    					case SUGAR_CANE_BLOCK:
    						if( Vegetation.debugging ) logOutput( "Found Block of Type: " + Material.SUGAR_CANE_BLOCK );
    						Cranes.GrowCranes( CB );
    						break;

    					case YELLOW_FLOWER:
    						if( Vegetation.debugging ) logOutput( "Found Block of Type: " + Material.YELLOW_FLOWER );
    						Plants.growPlant( CB , Material.YELLOW_FLOWER );
    						break;

    					case RED_ROSE:
    						if( Vegetation.debugging ) logOutput( "Found Block of Type: " + Material.RED_ROSE );
    						Plants.growPlant( CB , Material.RED_ROSE );
    						break;

    					case BROWN_MUSHROOM:
    						if( Vegetation.debugging ) logOutput( "Found Block of Type: " + Material.BROWN_MUSHROOM );
    						Plants.growPlant( CB , Material.BROWN_MUSHROOM );
    						break;

    					case RED_MUSHROOM:
    						if( Vegetation.debugging ) logOutput( "Found Block of Type: " + Material.RED_MUSHROOM );
    						Plants.growPlant( CB , Material.RED_MUSHROOM );
    						break;

    					case PUMPKIN:
    						if( Vegetation.debugging ) logOutput( "Found Block of Type: " + Material.PUMPKIN );
    						Plants.growPlant( CB , Material.PUMPKIN );
    						break;

    					default:
    						break;
    					}
    				}
    			}
    		}
    	}
    	
    	if( pCommand.equals( "growgrass" )  )
    	{
    		if( args.length == 1 )
    		{
    			String arg = arrayToString( args, " " );
    			if( Integer.parseInt(arg) > 0 )
    			{
    				grassPerGrow = Integer.parseInt(arg);
    				sender.sendMessage( "Set grassPerGrow to " + arg );
    			}
    		}

    		sender.sendMessage( "Growing the grass.." );
    		
    		for( int C = 0; C < grassPerGrow; C++ )
    		{
    			//Grass.growGrass( (Player)sender );
    		}
    	}
    	
    	if( pCommand.equals( "growcacti" ) )
    	{
    		//Cacti.growCacti( (Player)sender );
    		return true;
    	}
    		
    	if( pCommand.equals( "growfield" ) )
    	{
    		if( args.length == 1 )
    		{
    			String arg = arrayToString( args, " " );
    			
    			if( Integer.parseInt(arg) > 0 && Integer.parseInt(arg) < 11 )
    			{
    				Grass.mowTheGrass( (Player)sender, Integer.parseInt(arg), false );
    				sender.sendMessage( "You grow a field of grass." );
    			}
    			else
    				return false;
    		}
    		else
    		{
				Grass.mowTheGrass( (Player)sender, 5, false );
				sender.sendMessage( "You grow a field of grass." );
    		}
    	}

    	if( pCommand.equals("growplants") )
    	{
    		sender.sendMessage( "Spreading the plants.." );
    		//Plants.growPlant( (Player)sender );
    		return true;
    	}
    	
    	if( pCommand.equals( "vine" ) )
    	{
    		Player player = (Player)sender;
    		Block targetBlock = player.getTargetBlock( null, 20 );
    		
    		if( targetBlock.getTypeId() != 18 )
    		{
    			player.sendMessage( "That's not a suitable place for a vine to grow." );
    			return true;
    		}
    		if( targetBlock.getRelative(BlockFace.DOWN).getTypeId() != 0 )
    		{
    			player.sendMessage( "There is no room for a vine to grow there." );
    			return true;
    		}
    		
    		if( args.length < 1 ) return false;
    		
    		int height = Integer.parseInt( args[0] );
    		
    		if( height < 1 || height > 10 ) return false;
    		
    		player.sendMessage( "Growing a vine.." );
    		Vines.growVineAt( player, targetBlock, height );
    		return true;
    	}
    	
    	if( pCommand.equals("growvines") )
    	{
    		if( args.length == 1 )
    		{
				String arg = arrayToString( args, " " );
				
				sender.sendMessage( "Growing the vines.." );
					
				Vines.growVines( (Player)sender, Integer.parseInt(arg) );
	    		return true;
    		}
    		else
    		{
				sender.sendMessage( "Growing the vines.." );
				
				Vines.growVine( (Player)sender, 10 );
	    		return true;
    		}
    	}
    	
    	if( pCommand.equals("growmoss") )
    	{
    		sender.sendMessage( "Spreading the moss.." );
    		Moss.growMoss( (Player)sender );
    		return true;
    	}
    	
    	if( pCommand.equals("gg") || pCommand.equals("grass") )
    	{
    		if( args.length == 1 )
    		{
    			String arg = arrayToString( args, " " );
    			int aInt = Integer.parseInt(arg);
    			
    			if( aInt > 0 )
    			{
    				if( aInt > 10 )
    				{
    					sender.sendMessage( "value too high -- set to 10." );
    				}
    				else
    				{
    					try
    					{
    						Player player = (Player)sender;
    						Block tBlock = player.getTargetBlock( null, 20 );
    						
    						if( tBlock.getTypeId() == 2 )
    							tBlock.setData((byte)aInt);
    						else
    						{
    							if( tBlock.getRelative(BlockFace.DOWN).getTypeId() == 3 
    							|| tBlock.getRelative(BlockFace.DOWN).getTypeId() == 2 )
    							{
    								if( tBlock.getTypeId() == 83 )
    								{
    	    							tBlock.getRelative(BlockFace.DOWN).setData((byte)aInt);
    	    				    		return true;
    								}
    							}
    							else
    								sender.sendMessage( "That's not grass!" );
    						}
    					}
    					catch( Exception e )
    					{
    						// Do nothing..
    					}
    				}
    			}
    			else
    				return true;
    		}
    	}
    	
    	if( pCommand.equals("overgrow") || pCommand.equals("og") )
    	{
    		if( args.length == 1 )
    		{
    			String arg = arrayToString( args, " " );
    			int aInt = Integer.parseInt(arg);
    			
    			if( aInt > 0 )
    			{
    				tempGrassPerGrow = aInt;
    				sender.sendMessage( "Setting grassPerGrow temporarily to " + aInt );
    			}
    		}

    		if( overGrowTicks > 0 )
	    	{
	    		if( overGrower != (Player)sender )
	    		{
	    			sender.sendMessage( "Overgrowth is already happening." );
	    		}
	    		else
	    		{
		       		sender.sendMessage( "Restarting overgrowth cycle.." );
		       		overGrower = (Player)sender;
		       		overGrowTicks = 1000;
	    		}
	    	}
	    	else
	    	{
	        	sender.sendMessage( "Starting overgrowth cycle.." );
	        	overGrower = (Player)sender;
	        	overGrowTicks = 1000;
	    	}
	    }

    	if( pCommand.equals("overgrowplants") || pCommand.equals("ogp") )
    	{
    		if( args.length == 1 )
    		{
    			String arg = arrayToString( args, " " );
    			int aInt = Integer.parseInt(arg);
    			
    			if( aInt > 0 )
    			{
    				tempGrassPerGrow = aInt;
    				sender.sendMessage( "Setting grassPerGrow temporarily to " + aInt );
    			}
    		}

    		if( overGrowTicks > 0 )
	    	{
	    		if( overGrower != (Player)sender )
	    		{
	    			sender.sendMessage( "Overgrowth is already happening." );
	    		}
	    		else
	    		{
		       		sender.sendMessage( "Restarting overgrowth cycle.." );
		       		overGrower = (Player)sender;
		       		overGrowingPlants = true;
		       		overGrowTicks = 1000;
	    		}
	    	}
	    	else
	    	{
	        	sender.sendMessage( "Starting overgrowth cycle.." );
	        	overGrower = (Player)sender;
	        	overGrowingPlants = true;
	        	overGrowTicks = 1000;
	    	}
	    }

    	return true;
    }*/

    public static void logOutput(String text)
    {
    	Log.log( Level.INFO, "[" + pluginName + "]: " + text );
    }
}
