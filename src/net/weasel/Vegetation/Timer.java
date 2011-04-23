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
							if( Vegetation.debugging ) logOutput( "Found Block of Type: " + Material.GRASS );
							Grass.growGrass( CB );
						}
						break;
					
					case CACTUS:
						if ( Vegetation.enablePlants && Vegetation.enableCacti && ( ActiveTickCount < Vegetation.plantsPercent ) )
						{
							if( Vegetation.debugging ) logOutput( "Found Block of Type: " + Material.CACTUS );
							Cacti.spreadCacti( CB );
						}
						break;
						
					case SUGAR_CANE_BLOCK:
						if ( Vegetation.enablePlants && Vegetation.enableCanes && ( ActiveTickCount < Vegetation.plantsPercent ) )
						{
							if( Vegetation.debugging ) logOutput( "Found Block of Type: " + Material.SUGAR_CANE_BLOCK );
							Canes.spreadCanes( CB );
						}
						break;
						
					case YELLOW_FLOWER:
						if ( Vegetation.enablePlants && Vegetation.enableFlowers && ( ActiveTickCount < Vegetation.plantsPercent ) )
						{
							if( Vegetation.debugging ) logOutput( "Found Block of Type: " + Material.YELLOW_FLOWER );
							Plants.spreadPlant( CB , Material.YELLOW_FLOWER );
						}
						break;
					
					case RED_ROSE:
						if ( Vegetation.enablePlants && Vegetation.enableFlowers && ( ActiveTickCount < Vegetation.plantsPercent ) )
						{
							if( Vegetation.debugging ) logOutput( "Found Block of Type: " + Material.RED_ROSE );
							Plants.spreadPlant( CB , Material.RED_ROSE );
						}
						break;
					
					case BROWN_MUSHROOM:
						if ( Vegetation.enablePlants && Vegetation.enableFungi && ( ActiveTickCount < Vegetation.plantsPercent ) )
						{
							if( Vegetation.debugging ) logOutput( "Found Block of Type: " + Material.BROWN_MUSHROOM );
							Plants.spreadPlant( CB , Material.BROWN_MUSHROOM );
						}
						break;
						
					case RED_MUSHROOM:
						if ( Vegetation.enablePlants && Vegetation.enableFungi && ( ActiveTickCount < Vegetation.plantsPercent ) )
						{
							if( Vegetation.debugging ) logOutput( "Found Block of Type: " + Material.RED_MUSHROOM );
							Plants.spreadPlant( CB , Material.RED_MUSHROOM );
						}
						break;
					
					case PUMPKIN:
						if ( Vegetation.enablePlants && Vegetation.enablePumpkins && ( ActiveTickCount < Vegetation.plantsPercent ) )
						{
							if( Vegetation.debugging ) logOutput( "Found Block of Type: " + Material.PUMPKIN );
							Plants.spreadPlant( CB , Material.PUMPKIN );
						}
						break;
						
					case COBBLESTONE:
						if ( Vegetation.enableMoss && Vegetation.waterGrowsMoss && ( ActiveTickCount < Vegetation.plantsPercent ) )
						{
							if( Vegetation.debugging ) logOutput( "Found Block of Type: " + Material.COBBLESTONE );
							Moss.spreadMoss( CB );
						}
						break;
						
					case MOSSY_COBBLESTONE:
						if ( Vegetation.enableMoss  && ( ActiveTickCount < Vegetation.plantsPercent ) )
						{
							if( Vegetation.debugging ) logOutput( "Found Block of Type: " + Material.MOSSY_COBBLESTONE );
							Moss.spreadMoss( CB );
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
	}
}
