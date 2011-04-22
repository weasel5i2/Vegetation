package net.weasel.Vegetation;

import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.block.Block;

public class Timer implements Runnable {
	
	public static void logOutput( String text ) { Vegetation.logOutput( text ); }
	
	private long LastTick = System.currentTimeMillis();
	private long UpdateTicks = 200;
	private static int ActiveTickCount = 0;
	
	public static Vegetation Plugin;
	public static Server Server;
	
	public Timer(Vegetation instance)
	{
		Plugin = instance;
		Server = Plugin.getServer();
	}

	@Override
	public void run()
	{
		long Tick = System.currentTimeMillis();
		
		if ( ActiveTickCount >= 100 ) ActiveTickCount = 0;
		
		if ( ( Tick - LastTick >= UpdateTicks ) )
		{	
			Vegetation.getNextPlayer();
			
			if( Vegetation.currentPlayer != null )
			{
				Block CB = Blocks.getRandomBlock( Vegetation.currentPlayer.getLocation() );
				
				//Todo: Implement delegates instead of switch case
				if ( CB != null )
				{
					switch( CB.getType() )
					{
					case GRASS:
						if ( Vegetation.enableGrass && ( ActiveTickCount < Vegetation.grassPercent ) )
						{
							Grass.growGrass( CB );
						}
						break;
					
					case CACTUS:
						if ( Vegetation.enablePlants && Vegetation.enableCacti && ( ActiveTickCount < Vegetation.plantsPercent ) )
						{
							Cacti.growCacti( CB );
						}
						break;
						
					case SUGAR_CANE_BLOCK:
						if ( Vegetation.enablePlants && Vegetation.enableCanes && ( ActiveTickCount < Vegetation.grassPercent ) )
						{
							Cranes.GrowCranes( CB );
						}
						break;
						
					case YELLOW_FLOWER:
						if ( Vegetation.enablePlants && Vegetation.enableFlowers && ( ActiveTickCount < Vegetation.plantsPercent ) )
						{
							Plants.growPlant( CB , Material.YELLOW_FLOWER );
						}
						break;
					
					case RED_ROSE:
						if ( Vegetation.enablePlants && Vegetation.enableFlowers && ( ActiveTickCount < Vegetation.plantsPercent ) )
						{
							Plants.growPlant( CB , Material.RED_ROSE );
						}
						break;
					
					case BROWN_MUSHROOM:
						if ( Vegetation.enablePlants && Vegetation.enableFungi && ( ActiveTickCount < Vegetation.plantsPercent ) )
						{
							Plants.growPlant( CB , Material.BROWN_MUSHROOM );
						}
						break;
						
					case RED_MUSHROOM:
						if ( Vegetation.enablePlants && Vegetation.enableFungi && ( ActiveTickCount < Vegetation.plantsPercent ) )
						{
							Plants.growPlant( CB , Material.RED_MUSHROOM );
						}
						break;
					
					case PUMPKIN:
						if ( Vegetation.enablePlants && Vegetation.enablePumpkins && ( ActiveTickCount < Vegetation.grassPercent ) )
						{
							Plants.growPlant( CB , Material.PUMPKIN );
						}
						break;
						
					default:
						break;
					}
				}
			}
			LastTick = Tick;
			ActiveTickCount++;
		}
		else
		{
			ActiveTickCount = 0;
		}
	}
}
