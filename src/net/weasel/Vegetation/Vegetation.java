package net.weasel.Vegetation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.Server;
import org.bukkit.World;
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
	public static final Logger Log = Logger.getLogger("Minecraft");
	public static PermissionHandler Permissions;
	
	// Plugin stuff..
	public static String pluginName = "";
	public static String pluginVersion = "";
	public static boolean debugging = true;

	// System stuff
	public static Random generator = new Random();
	public static BukkitScheduler timer;
	public static ArrayList<String> playerList = new ArrayList<String>();
	//prevents server crash and lag if too many players issue commands
	public static int ActivePlayerCommands = 0;

	// Timer task stuff
	public static Integer tTask;
	
	public static double timerTick = 0;
	public static double plantTicks = 0;
	public static double grassTicks = 0;
	public static double lilyPadTicks = 0;
	public static double mossTicks = 0;
	public static double vineTicks = 0;
	public static double grazeTicks = 0;
	
	public static Player currentPlayer = null;
	public static Integer playerIndex = 0;
	
	public static Integer lastCycle = 0;
	public static double overGrowTicks = 0;
	
	// Growth-related stuff
	public static double grassPerGrow = 1;
	public static double tempGrassPerGrow = 1;
	public static double grassPercent = 40;
	public static double plantsPercent = 1;
	public static double mossPercent = 1;
	public static double vinePercent = 0.5;
	public static double lilyPadPercent = 0.5;
	
	public static double grazePercent = 10;
	
	public static Integer growthRange = 0;
	public static Integer verticalRadius = 5;

	// On/off switches
	public static boolean enableGrass = true;
	public static boolean enablePlants = true;
	public static boolean enablePumpkins = true;
	public static boolean enableFlowers = true;
	public static boolean enableFungi = true;
	public static boolean enableCacti = true;
	public static boolean enableCanes = true;
	public static boolean enableMoss = true;
	public static boolean enableLilyPads = true;
	public static boolean enableVines = true;
	public static boolean enableGrazers = true;
	
	public static boolean waterGrowsMoss = true;

	// Grazing-related stuff..
	public static int MaxGrazingAnimalsCount = 0;
	public static boolean grazingSheep = true;
	public static boolean grazingCows = true;
	public static boolean grazingPigs = true;
	public static boolean grazingChickens = true;
	
	// Biome-related stuff..
	public static boolean growForestBiome = true;
	public static boolean growRainforestBiome = true;
	public static boolean growShrublandBiome = true;
	public static boolean growSavannahBiome = true;
	public static boolean growPlainsBiome = true;
	public static boolean growSeasonalForestBiome = true;
	public static boolean growIceDesertBiome = true;
	public static boolean growHellBiome = true;
	public static boolean growDesertBiome = true;
	public static boolean growSwamplandBiome = true;
	public static boolean growTaigaBiome = true;
	public static boolean growTundraBiome = true;
	
	public static Player overGrower = null;
	public static boolean overGrowingPlants = false;
	
	public String pluginIni = "plugins/Vegetation/Settings.ini";
	
	public static Vegetation plugin;
	public static Server server;
	public static VegetationPlayerListener PlayerListener = null;
	public static VegetationBlockListener BlockListener = null;
	public static PluginManager pm = null;
	
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
		
		pm.registerEvent(Type.BLOCK_PLACE, BlockListener, Event.Priority.Normal, plugin );
		pm.registerEvent(Type.PLAYER_INTERACT, PlayerListener, Event.Priority.Normal, plugin );
		pm.registerEvent(Type.PLAYER_QUIT, PlayerListener, Event.Priority.Normal, plugin );
		pm.registerEvent(Type.PLAYER_LOGIN, PlayerListener, Event.Priority.Normal, plugin );
		
		//enable permission and register commands
		if( setupPermissions() )
		{
	        getCommand("grow").setExecutor(new GrowCommand(this));
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
		
		if( new File(pluginIni).exists() == false )
		{
			logOutput( "Settings file does not exist - creating a new one.. ");
			
			if( createIniFile() == true )
				logOutput( "Created successfully." );
			else
				logOutput( "Unable to create ini file!" );
		}

		debugging = new File("plugins/Vegetation/debug.txt").exists();
		if( debugging ) logOutput( "debugging is enabled." );
		
        logOutput( pluginName + " v" + pluginVersion + " enabled." );
        
        timer = this.getServer().getScheduler();

        enableGrass = getBooleanSetting( "enableGrass", true );
        enablePlants = getBooleanSetting( "enablePlants", true );
    	enablePumpkins = getBooleanSetting( "enablePumpkins", true );
    	enableFlowers = getBooleanSetting( "enableFlowers", true );
    	enableFungi = getBooleanSetting( "enableFungi", true );
    	enableCacti = getBooleanSetting( "enableCacti", true );
    	enableCanes = getBooleanSetting( "enableCanes", true );
    	enableMoss = getBooleanSetting( "enableMoss", true );
    	enableLilyPads = getBooleanSetting( "enableLilyPads", true );
    	enableVines = getBooleanSetting( "enableVines", true );
    	enableGrazers = getBooleanSetting( "enableGrazers", true );
    	MaxGrazingAnimalsCount = getIntSetting( "GrazerMaxCount", 10 ) ;

    	waterGrowsMoss = getBooleanSetting( "waterGrowsMoss", true );

    	grazingSheep = getBooleanSetting( "grazingSheep", true );
    	grazingCows = getBooleanSetting( "grazingCows", true );
    	grazingPigs = getBooleanSetting( "grazingPigs", true );
    	grazingChickens = getBooleanSetting( "grazingChickens", true );

    	growForestBiome = getBooleanSetting( "growForestBiome", true );
    	growRainforestBiome = getBooleanSetting( "growRainforestBiome", true );
    	growShrublandBiome = getBooleanSetting( "growShrublandBiome", true );
    	growSavannahBiome = getBooleanSetting( "growSavannahBiome", true );;
    	growPlainsBiome = getBooleanSetting( "growPlainsBiome", true );
    	growSeasonalForestBiome = getBooleanSetting( "growSeasonalForestBiome", true );
		growIceDesertBiome = getBooleanSetting( "growIceDesertBiome", true );
		growHellBiome = getBooleanSetting( "growHellBiome", true );
		growDesertBiome = getBooleanSetting( "growDesertBiome", true );
		growSwamplandBiome = getBooleanSetting( "growSwamplandBiome", true );
		growTaigaBiome = getBooleanSetting( "growTaigaBiome", true );
		growTundraBiome = getBooleanSetting( "growTundraBiome", true );

        growthRange = getIntSetting( "growthRange", 100 );
    	verticalRadius = getIntSetting( "verticalRadius", 5 );
    	
    	grassPercent = getDblSetting( "grassPercent", 40 );
    	plantsPercent = getDblSetting( "plantsPercent", 0.5 );
    	mossPercent = getDblSetting( "mossPercent", 0.5 );
    	vinePercent = getDblSetting( "lilyPadPercent", 0.5 );
    	lilyPadPercent = getDblSetting( "lilyPadPercent", 0.5 );
    	grazePercent = getDblSetting( "grazePercent", 10 );
    	grassPerGrow = getDblSetting( "grassPerGrow", 1 );
    	
    	tempGrassPerGrow = grassPerGrow;
    	
    	
    	/*if( ( grassPercent + plantsPercent + mossPercent + grazePercent ) > 100 )
    	{
        	logOutput( "The sum of the growth percentages is > 100. Please fix it." );
            this.getPluginLoader().disablePlugin(this);
    	}*/
    	
        if( !enableGrass && !enableGrazers && !enableMoss && !enablePlants )
        {
        	logOutput( "No vegetation is enabled. Disabling plugin." );
            this.getPluginLoader().disablePlugin(this);
        }
        /*else if( !growForestBiome && !growRainforestBiome && ! growShrublandBiome &&
        		 !growSavannahBiome && !growPlainsBiome && !growSeasonalForestBiome &&
        		 !growIceDesertBiome && !growHellBiome && !growDesertBiome &&
        		 !growDesertBiome && !growSwamplandBiome && !growTaigaBiome && !growTundraBiome )
        {
        	logOutput( "No Biome Type is enabled. Disabling plugin." );
        	this.getPluginLoader().disablePlugin(this);
        }*/
        else
        	getActivePlayerList();
            tTask = setupTimerTask( 10, 1 );
    }
    
    @Override
    public void onDisable() 
    {
    	timer.cancelAllTasks();
        logOutput( "Plugin disabled: "+pluginName+" version "+pluginVersion);
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
	              logOutput( "Permission system not detected, defaulting to OP" );
	              return false;
	          }
	      }
	      return false;
	}
    
    public String[] getSetting( String which, String Default )
    {
        return( getSettingValue(pluginIni, which, Default, "" ) );
    }

    public boolean getBooleanSetting( String which, boolean dValue )
    {
    	boolean retVal;
    	
    	String dString = ( dValue ? "true" : "false" );
    	
        if( getSettingValue(pluginIni, which, dString, "" )[0].equalsIgnoreCase("true") )
        	retVal = true;
        else
        	retVal = false;
        
        return( retVal );
    }
    
    public int setupTimerTask( int interval, int period )
    {
    	//Integer task = timer.scheduleAsyncRepeatingTask(this, new TimerTasks(this), interval, period );
    	Integer task = timer.scheduleSyncRepeatingTask(this, new Timer(this), interval, period );
        return task;
    }
    
    public Integer getIntSetting( String item, Integer dValue )
    {
    	Integer retVal = Integer.parseInt( getSettingValue(pluginIni, item, dValue.toString(), "" )[0]);
    	return retVal;
    }

    public double getDblSetting( String item, double d )
    {
    	double retVal = Double.parseDouble( getSettingValue(pluginIni, item, Double.toString(d), "" )[0]);
    	return retVal;
    }

    public Float getFloatSetting( String item, Float dValue )
    {
    	Float retVal = Float.valueOf(getSettingValue(pluginIni, item, dValue.toString(), "" )[0]);
    	return retVal;
    }

    public boolean createIniFile()
    {
    	boolean retVal = false;
    	
    	try 
    	{
			FileWriter outFile = new FileWriter(pluginIni);
			PrintWriter outP = new PrintWriter(outFile);
			
			outP.println( "growthRange=100" );
			outP.println( "verticalRadius=5" );
			outP.println( "enableGrass=true" );
			outP.println( "enablePlants=true" );
			outP.println( "enableFlowers=true" );
			outP.println( "enableFungi=true" );
			outP.println( "enablePumpkins=true" );
			outP.println( "enableCacti=true" );
			outP.println( "enableCanes=true" );
			outP.println( "enableMoss=true" );
			outP.println( "enableLilyPads=true" );
			outP.println( "enableVines=true" );
			outP.println( "enableGrazers=true" );
			outP.println( "GrazerMaxCount=10" );
			outP.println( "waterGrowsMoss=true" );
			outP.println( "grazingSheep=true" );
			outP.println( "grazingCows=true" );
			outP.println( "grazingPigs=true" );
			outP.println( "grazingChickens=true" );
			outP.println( "growForestBiome=true" );
			outP.println( "growRainforestBiome=true" );
			outP.println( "growShrublandBiome=true" );
			outP.println( "growSavannahBiome=true" );
			outP.println( "growPlainsBiome=true" );
			outP.println( "growSeasonalForestBiome=true" );
			outP.println( "growIceDesertBiome=true" );
			outP.println( "growDesertBiome=true" );
			outP.println( "growHellBiome=true" );
			outP.println( "growSwamplandBiome=true" );
			outP.println( "growTaigaBiome=true" );
			outP.println( "growTundraBiome=true" );
			outP.println( "grassPerGrow=1" );
			outP.println( "grassPercent=40" );
			outP.println( "plantsPercent=0.5" );
			outP.println( "mossPercent=0.5" );
			outP.println( "lilyPadPercent=0.5" );
			outP.println( "vinePercent=0.5" );
			outP.println( "grazePercent=10" );
			
			outP.close();
			retVal = true;
		} 
    	catch (IOException e) 
    	{
			logOutput( "Error writing to ini file." );
			retVal = false;
			e.printStackTrace();
		}
		
		return retVal;
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
	            
	public String[] getSettingValue(String fileName, String optionName, String defaultValue, String splitValue)
    {
    	Boolean gotLine; // Verification variable
    	String[] returnValue = new String[100]; // Settings max at 100 values
    	String curLine;
    	
    	gotLine = false; // We don't have the settings found yet
    	
    	if( new File(fileName).exists() == false )
    	{
    		return( new String("File not found.ZQX").split("ZQX") );
    	}
		if(splitValue.equals("")) {
			splitValue = "afs4wfa3waawfa3dogrsijkge5ssioeguhwar3awwa3rar";
		}
    	
        try {
        	// Get the line from the file
			FileInputStream fstream = new FileInputStream(fileName);
			BufferedReader in = new BufferedReader(new InputStreamReader(fstream));
			
			while(in.ready()) 
			{
				curLine = in.readLine().toString();
				if(curLine.split("=", 2)[0].equalsIgnoreCase(optionName)) {
					returnValue = new String[100];
					returnValue = curLine.split("=", 2)[1].split(splitValue);
					gotLine = true;
				}
			}
			
			in.close();
			
			// If the line does not exist, create it
			if(!gotLine) 
			{
                returnValue = defaultValue.split(splitValue);
                FileOutputStream out;
                PrintStream p;
                try {
	                out = new FileOutputStream(fileName, true);
	                p = new PrintStream( out );
	                p.println (optionName+"="+defaultValue);
	                p.close();
                } catch (Exception e) {
                	logOutput("Error writing to file");
                }
			}
		}
        catch (Exception e) {
        	logOutput("-=-");
        	logOutput("File input error: "+e.toString());
        	logOutput("File input error: "+e.getStackTrace().toString());
        	logOutput("-=-");
		}
		finally {
		}
		
		return returnValue;
    }

    public static void logOutput(String text)
    {
    	Log.log( Level.INFO, "[" + pluginName + "]: " + text );
    }

	public static void getActivePlayerList()
	{
		World world = null;

		Vegetation.playerList.clear();
		
		int WC = plugin.getServer().getWorlds().size();
		int PC = 0;
		int X, Y;
		
		for( X = 0; X < WC; X++ )
		{
			world = plugin.getServer().getWorlds().get(X);
			PC = world.getPlayers().size();
			
			for( Y = 0; Y < PC; Y++ )
			{
				if( world.getPlayers().get(Y).isOnline() )
					Vegetation.playerList.add(world.getPlayers().get(Y).getName());
			}
		}
		
		playerIndex = 0;
	}
	
	public static void getNextPlayer()
	{
		if ( playerIndex < (playerList.size() - 1) )
		{
			currentPlayer = plugin.getServer().getPlayer(playerList.get( playerIndex ) );
			playerIndex++;
		}
		else if ( playerList.size() > 0 )
		{
			playerIndex = 0;
			currentPlayer = plugin.getServer().getPlayer(playerList.get( playerIndex ) );
		}
		else
		{
			playerIndex = 0;
			currentPlayer = null;
		}
	}
	
	public static Integer getPlayerCount()
	{
		Integer retVal = 0;
		World w = null;
		int X, Y;
		
		for( X = 0; X < plugin.getServer().getWorlds().size(); X++ )
		{
			w = plugin.getServer().getWorlds().get(X);
			
			for( Y = 0; Y < w.getPlayers().size(); Y++ )
			{
				if( w.getPlayers().get(Y).isOnline() ) retVal++;
			}
		}
		
		return retVal;
	}

	public static String arrayToString(String[] a, String separator) 
    {
        String result = "";
        
        if (a.length > 0) 
        {
            result = a[0];    // start with the first element
            for (int i=1; i<a.length; i++) {
                result = result + separator + a[i];
            }
        }
        return result;
    }
}
