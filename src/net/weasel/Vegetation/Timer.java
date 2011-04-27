package net.weasel.Vegetation;

import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.block.Block;

public class Timer implements Runnable {
	
	public static void logOutput( String text ) { Vegetation.logOutput( text ); }
	
	private long LastTick = System.currentTimeMillis();
	private long UpdateTicks = 200;
	private static int ActiveTickCount = 100;
	
	public static Vegetation Plugin;
	public static Server Server;
	
	public static double plantTicks = 0;
	public static double grassTicks = 0;
	public static double lilyPadTicks = 0;
	public static double mossTicks = 0;
	public static double vineTicks = 0;
	public static double grazeTicks = 0;
	
	public Timer(Vegetation instance)
	{
		Plugin = instance;
		Server = Plugin.getServer();
	}

	@Override
	public void run()
	{
		long Tick = System.currentTimeMillis();
		
		if ( ActiveTickCount >= 100 )
		{
			plantTicks = Vegetation.plantsPercent;
			grassTicks = Vegetation.grassPercent;
			//lilyPadTicks = 0;
			mossTicks = Vegetation.mossPercent;
			grazeTicks = Vegetation.grazePercent;
			vineTicks = Vegetation.vinePercent;
			ActiveTickCount = 0;
		}
		
		if ( ( Tick - LastTick >= UpdateTicks ) )
		{	
			Vegetation.getNextPlayer();
			
			if( Vegetation.currentPlayer != null )
			{
				Block CB = Blocks.getRandomBlock( Vegetation.currentPlayer.getLocation(), Material.AIR );
				
				//Todo: Implement delegates instead of switch case
				if ( CB != null )
				{
					switch( CB.getType() )
					{
					case GRASS:
						if ( Vegetation.enableGrass && ( grassTicks > 0 ) )
						{
							if( Vegetation.debugging ) logOutput( "Found Block of Type: " + Material.GRASS );
							Grass.growGrass( CB );
							grassTicks--;
						}
						break;
					
					case CACTUS:
						if ( Vegetation.enablePlants && Vegetation.enableCacti && ( plantTicks > 0 ) )
						{
							if( Vegetation.debugging ) logOutput( "Found Block of Type: " + Material.CACTUS );
							Cacti.growCacti( CB );
							plantTicks--;
						}
						break;
						
					case SUGAR_CANE_BLOCK:
						if ( Vegetation.enablePlants && Vegetation.enableCanes && ( plantTicks > 0 ) )
						{
							if( Vegetation.debugging ) logOutput( "Found Block of Type: " + Material.SUGAR_CANE_BLOCK );
							Canes.growCanes( CB );
							plantTicks--;
						}
						break;
						
					case YELLOW_FLOWER:
						if ( Vegetation.enablePlants && Vegetation.enableFlowers && ( plantTicks > 0 ) )
						{
							if( Vegetation.debugging ) logOutput( "Found Block of Type: " + Material.YELLOW_FLOWER );
							Plants.growPlant( CB , Material.YELLOW_FLOWER );
							plantTicks--;
						}
						break;
					
					case RED_ROSE:
						if ( Vegetation.enablePlants && Vegetation.enableFlowers && ( plantTicks > 0 ) )
						{
							if( Vegetation.debugging ) logOutput( "Found Block of Type: " + Material.RED_ROSE );
							Plants.growPlant( CB , Material.RED_ROSE );
							plantTicks--;
						}
						break;
					
					case BROWN_MUSHROOM:
						if ( Vegetation.enablePlants && Vegetation.enableFungi && ( plantTicks > 0 ) )
						{
							if( Vegetation.debugging ) logOutput( "Found Block of Type: " + Material.BROWN_MUSHROOM );
							Plants.growPlant( CB , Material.BROWN_MUSHROOM );
							plantTicks--;
						}
						break;
						
					case RED_MUSHROOM:
						if ( Vegetation.enablePlants && Vegetation.enableFungi && ( plantTicks > 0 ) )
						{
							if( Vegetation.debugging ) logOutput( "Found Block of Type: " + Material.RED_MUSHROOM );
							Plants.growPlant( CB , Material.RED_MUSHROOM );
							plantTicks--;
						}
						break;
					
					case PUMPKIN:
						if ( Vegetation.enablePlants && Vegetation.enablePumpkins && ( plantTicks > 0 ) )
						{
							if( Vegetation.debugging ) logOutput( "Found Block of Type: " + Material.PUMPKIN );
							Plants.growPlant( CB , Material.PUMPKIN );
							plantTicks--;
						}
						break;
						
					case COBBLESTONE:
						if ( Vegetation.enableMoss && Vegetation.waterGrowsMoss && ( mossTicks > 0 ) )
						{
							if( Vegetation.debugging ) logOutput( "Found Block of Type: " + Material.COBBLESTONE );
							Moss.growMoss( CB );
							mossTicks--;
						}
						break;
						
					case MOSSY_COBBLESTONE:
						if ( Vegetation.enableMoss  && ( mossTicks > 0 ) )
						{
							if( Vegetation.debugging ) logOutput( "Found Block of Type: " + Material.MOSSY_COBBLESTONE );
							Moss.growMoss( CB );
							mossTicks--;
						}
						break;
						
					/*case LEAVES:
						if ( Vegetation.enableVines && ( vineTicks > 0 ) )
						{
							if( Vegetation.debugging ) logOutput( "Found Block of Type: " + Material.LEAVES );
							Vines.growVines( Vegetation.currentPlayer, 2 );
							vineTicks--;
						}
						break;*/
						
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
