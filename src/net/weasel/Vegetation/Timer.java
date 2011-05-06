package net.weasel.Vegetation;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;

public class Timer implements Runnable {
	
	public void logOutput( String text ) { Vegetation.logOutput( text ); }
	
	private VegetationWorld vWorld;
	private Settings settings;
	private long LastTick = System.currentTimeMillis();
	private long UpdateTicks = 200;
	private int ActiveTickCount = 100;
	
	private int plantTicks = 0;
	private int grassTicks = 0;
	//private int lilyPadTicks = 0;
	private int mossTicks = 0;
	private int vineTicks = 0;
	private int grazeTicks = 0;
	
	public Timer(VegetationWorld vw)
	{
		vWorld = vw;
		settings = vWorld.getSettings();
	}

	@Override
	public void run()
	{
		long Tick = System.currentTimeMillis();
		
		if ( ActiveTickCount >= 100 )
		{
			plantTicks = settings.plantsPercent;
			grassTicks = settings.grassPercent;
			//lilyPadTicks = 0;
			mossTicks = settings.mossPercent;
			grazeTicks = settings.grazePercent;
			vineTicks = settings.vinePercent;
			ActiveTickCount = 0;
		}
		
		if ( ( Tick - LastTick >= UpdateTicks ) )
		{	
			Player currentPlayer = vWorld.playerList.getNextPlayer();
			
			if( currentPlayer != null )
			{
				Block CB = vWorld.blocks.getRandomTopBlock( currentPlayer.getLocation(), Material.AIR );
				
				//Todo: Implement delegates instead of switch case
				if ( CB != null )
				{
					switch( CB.getType() )
					{
					case GRASS:
						if ( settings.enableGrass && ( grassTicks > 0 ) )
						{
							vWorld.grass.growGrass( CB );
							grassTicks--;
						}
						break;
					
					case CACTUS:
						if ( settings.enablePlants && settings.enableCacti && ( plantTicks > 0 ) )
						{
							vWorld.cacti.growCacti( CB );
							plantTicks--;
						}
						break;
						
					case SUGAR_CANE_BLOCK:
						if ( settings.enablePlants && settings.enableCanes && ( plantTicks > 0 ) )
						{
							vWorld.canes.growCanes( CB );
							plantTicks--;
						}
						break;
						
					case YELLOW_FLOWER:
						if ( settings.enablePlants && settings.enableFlowers && ( plantTicks > 0 ) )
						{
							vWorld.plants.growPlant( CB , Material.YELLOW_FLOWER );
							plantTicks--;
						}
						
						if ( settings.enableGrass && ( grassTicks > 0 ) )
						{
							if( CB.getRelative(BlockFace.DOWN).getType() == Material.GRASS )
							{
								vWorld.grass.growGrass( CB.getRelative(BlockFace.DOWN) );
								grassTicks--;
							}
						}
						break;
					
					case RED_ROSE:
						if ( settings.enablePlants && settings.enableFlowers && ( plantTicks > 0 ) )
						{
							vWorld.plants.growPlant( CB , Material.RED_ROSE );
							plantTicks--;
						}
						
						if ( settings.enableGrass && ( grassTicks > 0 ) )
						{
							if( CB.getRelative(BlockFace.DOWN).getType() == Material.GRASS )
							{
								vWorld.grass.growGrass( CB.getRelative(BlockFace.DOWN) );
								grassTicks--;
							}
						}
						break;
					
					case BROWN_MUSHROOM:
						if ( settings.enablePlants && settings.enableFungi && ( plantTicks > 0 ) )
						{
							vWorld.plants.growPlant( CB , Material.BROWN_MUSHROOM );
							plantTicks--;
						}
						
						if ( settings.enableGrass && ( grassTicks > 0 ) )
						{
							if( CB.getRelative(BlockFace.DOWN).getType() == Material.GRASS )
							{
								vWorld.grass.growGrass( CB.getRelative(BlockFace.DOWN) );
								grassTicks--;
							}
						}
						break;
						
					case RED_MUSHROOM:
						if ( settings.enablePlants && settings.enableFungi && ( plantTicks > 0 ) )
						{
							vWorld.plants.growPlant( CB , Material.RED_MUSHROOM );
							plantTicks--;
						}
						
						if ( settings.enableGrass && ( grassTicks > 0 ) )
						{
							if( CB.getRelative(BlockFace.DOWN).getType() == Material.GRASS )
							{
								vWorld.grass.growGrass( CB.getRelative(BlockFace.DOWN) );
								grassTicks--;
							}
						}
						break;
					
					case PUMPKIN:
						if ( settings.enablePlants && settings.enablePumpkins && ( plantTicks > 0 ) )
						{
							vWorld.plants.growPlant( CB , Material.PUMPKIN );
							plantTicks--;
						}
						break;
						
					case COBBLESTONE:
						if ( settings.enableMoss && settings.waterGrowsMoss && ( mossTicks > 0 ) )
						{
							vWorld.moss.growMoss( CB );
							mossTicks--;
						}
						break;
						
					case MOSSY_COBBLESTONE:
						if ( settings.enableMoss  && ( mossTicks > 0 ) )
						{
							vWorld.moss.growMoss( CB );
							mossTicks--;
						}
						break;
						
					default:
						break;
					}
				}
				
				CB = vWorld.blocks.getRandomBlock( currentPlayer.getLocation(), Material.LOG );
				if( CB != null )
				{
					if ( settings.enableVines && ( vineTicks > 0 ) )
					{
						vWorld.vines.growVines( CB );
						vineTicks--;
					}
				}
				
				if( settings.enableGrazers && grazeTicks > 0 )
				{
					vWorld.grazers.grazeAnimals();
					grazeTicks--;
				}
			}
			LastTick = Tick;
			ActiveTickCount++;
		}
	}
}
