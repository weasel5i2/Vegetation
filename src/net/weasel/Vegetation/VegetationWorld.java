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

import org.bukkit.World;

public class VegetationWorld {
	
	public void logOutput( String text ) { Vegetation.logOutput( text ); }
	
	private World world;
	private Vegetation plugin;
	private String settingsFile;
	
	//Player related settings
	public PlayerList pList;
	public boolean trampleGrass = false;
	
	// Growth-related stuff
	public int grassPercent = 40;
	public int plantsPercent = 5;
	public int mossPercent = 5;
	public int vinePercent = 5;
	public int lilyPadPercent = 5;
	
	public int grazePercent = 10;
	
	public int growthRange = 0;
	public int verticalRadius = 5;
	
	public int maxGrassHeight = 9;

	// On/off switches
	public boolean enableGrass = false;
	public boolean enablePlants = false;
	public boolean enablePumpkins = false;
	public boolean enableFlowers = false;
	public boolean enableFungi = false;
	public boolean enableCacti = false;
	public boolean enableCanes = false;
	public boolean enableMoss = false;
	public boolean enableLilyPads = false;
	public boolean enableVines = false;
	public boolean enableGrazers = false;
	public boolean waterGrowsMoss = false;
	
	// Plant spreading amount for player commands
	public int spreadAmountFlowers = 5;
	public int spreadAmountCacti = 5;
	public int spreadAmountFungi = 5;
	public int spreadAmountSugarCane = 5;
	public int spreadAmountMoss = 5;

	// Grazing-related stuff..
	public int maxGrazingAnimalsCount = 10;
	public boolean grazingSheep = true;
	public boolean grazingCows = true;
	public boolean grazingPigs = true;
	public boolean grazingChickens = true;
	
	// Biome-related stuff..
	public boolean growForestBiome = true;
	public boolean growRainforestBiome = true;
	public boolean growShrublandBiome = true;
	public boolean growSavannahBiome = true;
	public boolean growPlainsBiome = true;
	public boolean growSeasonalForestBiome = true;
	public boolean growIceDesertBiome = true;
	public boolean growHellBiome = true;
	public boolean growDesertBiome = true;
	public boolean growSwamplandBiome = true;
	public boolean growTaigaBiome = true;
	public boolean growTundraBiome = true;
	
	public VegetationWorld(Vegetation p, World w)
	{
		plugin = p;
		world = w;
		String wName = w.getName();
		settingsFile = "plugins/Vegetation/" + wName + ".ini";
		
		logOutput("New VegetationWorld: " + wName + " - " + settingsFile);
		
		if( new File(settingsFile).exists() )
		{
			readSettings();
		}
		else
		{
			if( createIniFile() )
			{
				readSettings();
			}
		}
		
		pList = new PlayerList(plugin, world);
	}
	
	public World getWorld()
	{
		return world;
	}
	
	public PlayerList getPlayerList()
	{
		return pList;
	}
	
	public void readSettings()
	{
	       enableGrass = getBooleanSetting( "enableGrass", false );
	        maxGrassHeight = getIntSetting( "maxGrassHeight", 9 );
	        
	        if( maxGrassHeight < 0 ) maxGrassHeight = 0;
	        else if( maxGrassHeight > 9 ) maxGrassHeight = 9;
	        
	        enablePlants = getBooleanSetting( "enablePlants", false );
	    	enablePumpkins = getBooleanSetting( "enablePumpkins", false );
	    	enableFlowers = getBooleanSetting( "enableFlowers", false );
	    	enableFungi = getBooleanSetting( "enableFungi", false );
	    	enableCacti = getBooleanSetting( "enableCacti", false );
	    	enableCanes = getBooleanSetting( "enableCanes", false );
	    	enableMoss = getBooleanSetting( "enableMoss", false );
	    	enableLilyPads = getBooleanSetting( "enableLilyPads", false );
	    	enableVines = getBooleanSetting( "enableVines", false );
	    	enableGrazers = getBooleanSetting( "enableGrazers", false );
	    	maxGrazingAnimalsCount = getIntSetting( "grazerMaxCount", 10 ) ;
	    	waterGrowsMoss = getBooleanSetting( "waterGrowsMoss", false );
	    	
	    	spreadAmountFlowers = getIntSetting( "spreadAmountFlowers", 5 );
	    	spreadAmountFungi = getIntSetting( "spreadAmountFungi", 5 );
	    	spreadAmountCacti = getIntSetting( "spreadAmountCacti", 5 );
	    	spreadAmountSugarCane = getIntSetting( "spreadAmountSugarCane", 5 );
	    	spreadAmountMoss = getIntSetting( "spreadAmountMoss", 5 );
	    	trampleGrass = getBooleanSetting( "trampleGrass", false );

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

	        growthRange = getIntSetting( "growthRange", 30 );
	    	verticalRadius = getIntSetting( "verticalRadius", 10 );
	    	
	    	grassPercent = getIntSetting( "grassPercent", 40 );
	    	plantsPercent = getIntSetting( "plantsPercent", 5 );
	    	mossPercent = getIntSetting( "mossPercent", 5 );
	    	vinePercent = getIntSetting( "lilyPadPercent", 5 );
	    	lilyPadPercent = getIntSetting( "lilyPadPercent", 5 );
	    	grazePercent = getIntSetting( "grazePercent", 10 );
	}
	
    public String[] getSetting( String which, String Default )
    {
        return( getSettingValue(settingsFile, which, Default, "" ) );
    }

    public boolean getBooleanSetting( String which, boolean dValue )
    {
    	boolean retVal;
    	
    	String dString = ( dValue ? "true" : "false" );
    	
        if( getSettingValue(settingsFile, which, dString, "" )[0].equalsIgnoreCase("true") )
        	retVal = true;
        else
        	retVal = false;
        
        return( retVal );
    }
	
    public Integer getIntSetting( String item, Integer dValue )
    {
    	Integer retVal = Integer.parseInt( getSettingValue(settingsFile, item, dValue.toString(), "" )[0]);
    	if( retVal < 0 ) retVal = 0;
    	return retVal;
    }

    public double getDblSetting( String item, double d )
    {
    	double retVal = Double.parseDouble( getSettingValue(settingsFile, item, Double.toString(d), "" )[0]);
    	if( retVal < 0 ) retVal = 0;
    	return retVal;
    }

    public Float getFloatSetting( String item, Float dValue )
    {
    	Float retVal = Float.valueOf(getSettingValue(settingsFile, item, dValue.toString(), "" )[0]);
    	return retVal;
    }
    
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
	
    public boolean createIniFile()
    {
    	boolean retVal = false;
    	
    	try 
    	{
			FileWriter outFile = new FileWriter(settingsFile);
			PrintWriter outP = new PrintWriter(outFile);
			
			outP.println( "/* Block Search Settings:" );
			outP.println( "growthRange=30" );
			outP.println( "verticalRadius=10" );
			outP.println( "" );
			outP.println( "/* Vegetation Settings:" );
			outP.println( "enableGrass=false" );
			outP.println( "" );
			outP.println( "/* Set max grass height from 1-9" );
			outP.println( "maxGrassHeight=9" );
			outP.println( "enablePlants=false" );
			outP.println( "enableFlowers=false" );
			outP.println( "enableFungi=false" );
			outP.println( "enablePumpkins=false" );
			outP.println( "enableCacti=false" );
			outP.println( "enableCanes=false" );
			outP.println( "enableMoss=false" );
			//outP.println( "enableLilyPads=false" );
			outP.println( "enableVines=false" );
			outP.println( "" );
			outP.println( "/* If this option is set to true," );
			outP.println( "/* moss will grow on any cobblestones touching water" );
			outP.println( "/* regardless if there was a moss block to spread from or not." );
			outP.println( "waterGrowsMoss=true" );
			outP.println( "" );
			outP.println( "/* Player related Settings:" );
			//outP.println( "maxActivePlayerCommands=40" );
			outP.println( "spreadAmountFlowers=5" );
			outP.println( "spreadAmountFungi=5" );
			outP.println( "spreadAmountCacti=5" );
			outP.println( "spreadAmountSugarCane=5" );
			outP.println( "spreadAmountMoss=5" );
			outP.println( "/* The player will trample a path through the grass if set to true." );
			outP.println( "trampleGrass=false" );
			outP.println( "" );
			outP.println( "/* ENTITIES:" );
			outP.println( "enableGrazers=true" );
			outP.println( "/* Sets max number of grazing animals." );
			outP.println( "/* You should decrease this number if you encounter server lag." );
			outP.println( "grazerMaxCount=10" );
			outP.println( "grazingSheep=true" );
			outP.println( "grazingCows=true" );
			outP.println( "grazingPigs=true" );
			outP.println( "grazingChickens=true" );
			outP.println( "" );
			outP.println( "/* BIOMES: " );
			outP.println( "/* Enabled/Disables the growth of vegetation on " );
			outP.println( "/* certain biomes." );
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
			//outP.println( "grassPerGrow=1" );
			outP.println( "" );
			outP.println( "/* EVENTS: " );
			outP.println( "/* The following parameters determine how many ticks of " );
			outP.println( "/* 100 ticks a specific type of action is being executed. " );
			outP.println( "/* (Example: If grassPercent is set to 60, there is the possibility " );
			outP.println( "/*  of grass growing at 60/100 ticks if a grass block is found 60 times. " );
			outP.println( "grassPercent=40" );
			outP.println( "plantsPercent=5" );
			outP.println( "mossPercent=5" );
			//outP.println( "lilyPadPercent=5" );
			outP.println( "vinePercent=5" );
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
	
}