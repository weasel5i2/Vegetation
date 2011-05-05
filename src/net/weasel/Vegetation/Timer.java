package net.weasel.Vegetation;

import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;

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
			Player currentPlayer = Vegetation.pList.getNextPlayer();
			
			if( currentPlayer != null )
			{
				Block CB = Blocks.getRandomTopBlock( currentPlayer.getLocation(), Material.AIR );
				
				//Todo: Implement delegates instead of switch case
				if ( CB != null )
				{
					switch( CB.getType() )
					{
					case GRASS:
						if ( Vegetation.enableGrass && ( grassTicks > 0 ) )
						{
							Grass.growGrass( CB );
							grassTicks--;
						}
						break;
					
					case CACTUS:
						if ( Vegetation.enablePlants && Vegetation.enableCacti && ( plantTicks > 0 ) )
						{
							Cacti.growCacti( CB );
							plantTicks--;
						}
						break;
						
					case SUGAR_CANE_BLOCK:
						if ( Vegetation.enablePlants && Vegetation.enableCanes && ( plantTicks > 0 ) )
						{
							Canes.growCanes( CB );
							plantTicks--;
						}
						break;
						
					case YELLOW_FLOWER:
						if ( Vegetation.enablePlants && Vegetation.enableFlowers && ( plantTicks > 0 ) )
						{
							Plants.growPlant( CB , Material.YELLOW_FLOWER );
							plantTicks--;
						}
						
						if ( Vegetation.enableGrass && ( grassTicks > 0 ) )
						{
							if( CB.getRelative(BlockFace.DOWN).getType() == Material.GRASS )
							{
								Grass.growGrass( CB.getRelative(BlockFace.DOWN) );
								grassTicks--;
							}
						}
						break;
					
					case RED_ROSE:
						if ( Vegetation.enablePlants && Vegetation.enableFlowers && ( plantTicks > 0 ) )
						{
							Plants.growPlant( CB , Material.RED_ROSE );
							plantTicks--;
						}
						
						if ( Vegetation.enableGrass && ( grassTicks > 0 ) )
						{
							if( CB.getRelative(BlockFace.DOWN).getType() == Material.GRASS )
							{
								Grass.growGrass( CB.getRelative(BlockFace.DOWN) );
								grassTicks--;
							}
						}
						break;
					
					case BROWN_MUSHROOM:
						if ( Vegetation.enablePlants && Vegetation.enableFungi && ( plantTicks > 0 ) )
						{
							Plants.growPlant( CB , Material.BROWN_MUSHROOM );
							plantTicks--;
						}
						
						if ( Vegetation.enableGrass && ( grassTicks > 0 ) )
						{
							if( CB.getRelative(BlockFace.DOWN).getType() == Material.GRASS )
							{
								Grass.growGrass( CB.getRelative(BlockFace.DOWN) );
								grassTicks--;
							}
						}
						break;
						
					case RED_MUSHROOM:
						if ( Vegetation.enablePlants && Vegetation.enableFungi && ( plantTicks > 0 ) )
						{
							Plants.growPlant( CB , Material.RED_MUSHROOM );
							plantTicks--;
						}
						
						if ( Vegetation.enableGrass && ( grassTicks > 0 ) )
						{
							if( CB.getRelative(BlockFace.DOWN).getType() == Material.GRASS )
							{
								Grass.growGrass( CB.getRelative(BlockFace.DOWN) );
								grassTicks--;
							}
						}
						break;
					
					case PUMPKIN:
						if ( Vegetation.enablePlants && Vegetation.enablePumpkins && ( plantTicks > 0 ) )
						{
							Plants.growPlant( CB , Material.PUMPKIN );
							plantTicks--;
						}
						break;
						
					case COBBLESTONE:
						if ( Vegetation.enableMoss && Vegetation.waterGrowsMoss && ( mossTicks > 0 ) )
						{
							Moss.growMoss( CB );
							mossTicks--;
						}
						break;
						
					case MOSSY_COBBLESTONE:
						if ( Vegetation.enableMoss  && ( mossTicks > 0 ) )
						{
							Moss.growMoss( CB );
							mossTicks--;
						}
						break;
						
					default:
						break;
					}
				}
				
				CB = Blocks.getRandomBlock( currentPlayer.getLocation(), Material.LOG );
				if( CB != null )
				{
					if ( Vegetation.enableVines && ( vineTicks > 0 ) )
					{
						Vines.growVines( CB );
						vineTicks--;
					}
				}
				
				if( Vegetation.enableGrazers && grazeTicks > 0 )
				{
					Grazers.grazeAnimals();
					grazeTicks--;
				}
			}
			LastTick = Tick;
			ActiveTickCount++;
		}
	}
}
