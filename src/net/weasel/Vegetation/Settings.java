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

public class Settings {

	public void logOutput(String text) {
		Vegetation.logOutput(text);
	}

	private String settingsFile;

	// Player related settings
	public boolean trampleGrass;
	public int maxActivePlayerCommands;

	// Growth-related stuff
	public int grassPercent;
	public int tallGrassPercent;
	public int plantsPercent;
	public int mossPercent;
	public int vinePercent;
	public int lilyPadPercent;

	public int grazePercent;

	public int growthRange;
	public int verticalRadius;

	public int maxGrassHeight;

	// On/off switches
	public boolean enableGrass;
	public boolean enableTallGrass;
	public boolean enablePlants;
	public boolean enablePumpkins;
	public boolean enableFlowers;
	public boolean enableFungi;
	public boolean enableCacti;
	public boolean enableCanes;
	public boolean enableMoss;
	public boolean enableLilyPads;
	public boolean enableVines;
	public boolean enableGrazers;
	public boolean waterGrowsMoss;

	// Plant spreading amount for player commands
	public int spreadAmountFlowers;
	public int spreadAmountCacti;
	public int spreadAmountFungi;
	public int spreadAmountSugarCane;
	public int spreadAmountMoss;
	public int spreadAmountTallGrass;

	// Grazing-related stuff..
	public int maxGrazingAnimalsCount;
	public boolean grazingSheep;
	public boolean grazingCows;
	public boolean grazingPigs;
	public boolean grazingChickens;

	// Biome-related stuff..
	public boolean growForestBiome;
	public boolean growRainforestBiome;
	public boolean growShrublandBiome;
	public boolean growSavannahBiome;
	public boolean growPlainsBiome;
	public boolean growSeasonalForestBiome;
	public boolean growIceDesertBiome;
	public boolean growDesertBiome;
	public boolean growSwamplandBiome;
	public boolean growTaigaBiome;
	public boolean growTundraBiome;

	public Settings(String s) {
		settingsFile = s;

		if (new File(settingsFile).exists()) {
			readSettings();
		} else {
			if (createIniFile()) {
				readSettings();
			}
		}
	}

	public void readSettings() {

		enableGrass = getBooleanSetting("enableGrass", false);
		enableTallGrass = getBooleanSetting("enableTallGrass", false);
		maxGrassHeight = getIntSetting("maxGrassHeight", 9);

		if (maxGrassHeight < 0)
			maxGrassHeight = 0;
		else if (maxGrassHeight > 9)
			maxGrassHeight = 9;

		enablePlants = getBooleanSetting("enablePlants", false);
		enablePumpkins = getBooleanSetting("enablePumpkins", false);
		enableFlowers = getBooleanSetting("enableFlowers", false);
		enableFungi = getBooleanSetting("enableFungi", false);
		enableCacti = getBooleanSetting("enableCacti", false);
		enableCanes = getBooleanSetting("enableCanes", false);
		enableMoss = getBooleanSetting("enableMoss", false);
		enableLilyPads = getBooleanSetting("enableLilyPads", false);
		enableVines = getBooleanSetting("enableVines", false);
		enableGrazers = getBooleanSetting("enableGrazers", false);
		maxGrazingAnimalsCount = getIntSetting("grazerMaxCount", 10);
		waterGrowsMoss = getBooleanSetting("waterGrowsMoss", false);

		maxActivePlayerCommands = getIntSetting("maxActivePlayerCommands", 40);

		spreadAmountFlowers = getIntSetting("spreadAmountFlowers", 5);
		spreadAmountFungi = getIntSetting("spreadAmountFungi", 5);
		spreadAmountCacti = getIntSetting("spreadAmountCacti", 5);
		spreadAmountSugarCane = getIntSetting("spreadAmountSugarCane", 5);
		spreadAmountMoss = getIntSetting("spreadAmountMoss", 5);
		spreadAmountTallGrass = getIntSetting("spreadAmountTallGrass", 5);
		trampleGrass = getBooleanSetting("trampleGrass", false);

		grazingSheep = getBooleanSetting("grazingSheep", true);
		grazingCows = getBooleanSetting("grazingCows", true);
		grazingPigs = getBooleanSetting("grazingPigs", true);
		grazingChickens = getBooleanSetting("grazingChickens", true);

		growForestBiome = getBooleanSetting("growForestBiome", true);
		growRainforestBiome = getBooleanSetting("growRainforestBiome", true);
		growShrublandBiome = getBooleanSetting("growShrublandBiome", true);
		growSavannahBiome = getBooleanSetting("growSavannahBiome", true);
		;
		growPlainsBiome = getBooleanSetting("growPlainsBiome", true);
		growSeasonalForestBiome = getBooleanSetting("growSeasonalForestBiome", true);
		growIceDesertBiome = getBooleanSetting("growIceDesertBiome", true);
		growDesertBiome = getBooleanSetting("growDesertBiome", true);
		growSwamplandBiome = getBooleanSetting("growSwamplandBiome", true);
		growTaigaBiome = getBooleanSetting("growTaigaBiome", true);
		growTundraBiome = getBooleanSetting("growTundraBiome", true);

		growthRange = getIntSetting("growthRange", 30);
		verticalRadius = getIntSetting("verticalRadius", 10);

		grassPercent = getIntSetting("grassPercent", 40);
		tallGrassPercent = getIntSetting("tallGrassPercent", 20);
		plantsPercent = getIntSetting("plantsPercent", 5);
		mossPercent = getIntSetting("mossPercent", 5);
		vinePercent = getIntSetting("vinePercent", 5);
		lilyPadPercent = getIntSetting("lilyPadPercent", 5);
		grazePercent = getIntSetting("grazePercent", 10);
	}

	public String[] getSetting(String which, String Default) {
		return (getSettingValue(settingsFile, which, Default, ""));
	}

	public boolean getBooleanSetting(String which, boolean dValue) {
		boolean retVal = false;

		String dString = (dValue ? "true" : "false");

		if (getSettingValue(settingsFile, which, dString, "")[0].equalsIgnoreCase("true"))
			retVal = true;
		else
			retVal = false;

		return retVal;
	}

	public int getIntSetting(String item, Integer dValue) {
		int retVal;

		try {
			retVal = Integer.parseInt(getSettingValue(settingsFile, item, dValue.toString(), "")[0]);

		} catch (NumberFormatException e) {
			retVal = 0;
			logOutput("Error while reading [" + item + "] field. Option set to 0.");
		}

		return retVal;
	}

	public double getDblSetting(String item, double d) {
		double retVal;

		try {
			retVal = Double.parseDouble(getSettingValue(settingsFile, item, Double.toString(d), "")[0]);
		} catch (NumberFormatException e) {
			retVal = 0;
			logOutput("Error while reading [" + item + "] field. Option set to 0.");
		}
		return retVal;
	}

	public float getFloatSetting(String item, Float dValue) {
		float retVal;

		try {
			retVal = Float.valueOf(getSettingValue(settingsFile, item, dValue.toString(), "")[0]);

		} catch (NumberFormatException e) {
			retVal = 0;
			logOutput("Error while reading [" + item + "] field. Option set to 0.");
		}
		return retVal;
	}

	public String[] getSettingValue(String fileName, String optionName, String defaultValue, String splitValue) {
		Boolean gotLine; // Verification variable
		String[] returnValue = new String[100]; // Settings max at 100 values
		String curLine;

		gotLine = false; // We don't have the settings found yet

		if (new File(fileName).exists() == false) {
			return (new String("File not found.ZQX").split("ZQX"));
		}
		if (splitValue.equals("")) {
			splitValue = "afs4wfa3waawfa3dogrsijkge5ssioeguhwar3awwa3rar";
		}

		try {
			// Get the line from the file
			FileInputStream fstream = new FileInputStream(fileName);
			BufferedReader in = new BufferedReader(new InputStreamReader(fstream));

			while (in.ready()) {
				curLine = in.readLine().toString();
				if (curLine.split("=", 2)[0].equalsIgnoreCase(optionName)) {
					returnValue = new String[100];
					returnValue = curLine.split("=", 2)[1].split(splitValue);
					gotLine = true;
				}
			}

			in.close();

			// If the line does not exist, create it
			if (!gotLine) {
				returnValue = defaultValue.split(splitValue);
				FileOutputStream out;
				PrintStream p;
				try {
					out = new FileOutputStream(fileName, true);
					p = new PrintStream(out);
					p.println(optionName + "=" + defaultValue);
					p.close();
				} catch (Exception e) {
					logOutput("Error writing to file");
				}
			}
		} catch (Exception e) {
			logOutput("-=-");
			logOutput("File input error: " + e.toString());
			logOutput("File input error: " + e.getStackTrace().toString());
			logOutput("-=-");
		} finally {
		}

		return returnValue;
	}

	public boolean createIniFile() {
		boolean retVal = false;

		try {
			FileWriter outFile = new FileWriter(settingsFile);
			PrintWriter outP = new PrintWriter(outFile);

			outP.println("/* Block Search Settings:");
			outP.println("growthRange=20");
			outP.println("verticalRadius=5");
			outP.println("");
			outP.println("/* Vegetation Settings:");
			outP.println("enableGrass=false");
			outP.println("enableTallGrass=false");
			outP.println("");
			outP.println("/* Set max grass height from 1-9");
			outP.println("maxGrassHeight=9");
			outP.println("enablePlants=false");
			outP.println("enableFlowers=false");
			outP.println("enableFungi=false");
			outP.println("enablePumpkins=false");
			outP.println("enableCacti=false");
			outP.println("enableCanes=false");
			outP.println("enableMoss=false");
			outP.println("enableLilyPads=false");
			outP.println("enableVines=false");
			outP.println("");
			outP.println("/* If this option is set to true,");
			outP.println("/* moss will grow on any cobblestones touching water");
			outP.println("/* regardless if there was a moss block to spread from or not.");
			outP.println("waterGrowsMoss=false");
			outP.println("");
			outP.println("/* Player related Settings:");
			outP.println("maxActivePlayerCommands=40");
			outP.println("spreadAmountFlowers=5");
			outP.println("spreadAmountFungi=5");
			outP.println("spreadAmountCacti=5");
			outP.println("spreadAmountSugarCane=5");
			outP.println("spreadAmountMoss=5");
			outP.println("spreadAmountTallGrass=5");
			outP.println("/* The player will trample a path through the grass if set to true.");
			outP.println("trampleGrass=false");
			outP.println("");
			outP.println("/* ENTITIES:");
			outP.println("enableGrazers=false");
			outP.println("/* Sets max number of grazing animals.");
			outP.println("/* You should decrease this number if you encounter server lag.");
			outP.println("grazerMaxCount=10");
			outP.println("grazingSheep=true");
			outP.println("grazingCows=true");
			outP.println("grazingPigs=true");
			outP.println("grazingChickens=true");
			outP.println("");
			outP.println("/* BIOMES: ");
			outP.println("/* Enabled/Disables the growth of vegetation on ");
			outP.println("/* certain biomes.");
			outP.println("growForestBiome=true");
			outP.println("growRainforestBiome=true");
			outP.println("growShrublandBiome=true");
			outP.println("growSavannahBiome=true");
			outP.println("growPlainsBiome=true");
			outP.println("growSeasonalForestBiome=true");
			outP.println("growIceDesertBiome=true");
			outP.println("growDesertBiome=true");
			outP.println("growHellBiome=true");
			outP.println("growSwamplandBiome=true");
			outP.println("growTaigaBiome=true");
			outP.println("growTundraBiome=true");
			outP.println("");
			outP.println("/* EVENTS: ");
			outP.println("/* The following parameters determine how many ticks of ");
			outP.println("/* 100 ticks a specific type of action is being executed. ");
			outP.println("/* (Example: If grassPercent is set to 60, there is the possibility ");
			outP.println("/*  of grass growing at 60/100 ticks if a grass block is found 60 times. ");
			outP.println("grassPercent=40");
			outP.println("tallGrassPercent=20");
			outP.println("plantsPercent=5");
			outP.println("mossPercent=5");
			outP.println("lilyPadPercent=5");
			outP.println("vinePercent=5");
			outP.println("grazePercent=10");

			outP.close();
			retVal = true;
		} catch (IOException e) {
			logOutput("Error writing to ini file.");
			retVal = false;
			e.printStackTrace();
		}

		return retVal;
	}
}
