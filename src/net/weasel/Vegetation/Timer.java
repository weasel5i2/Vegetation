package net.weasel.Vegetation;

import org.bukkit.Location;
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
	
	private int plantTicks;
	private int grassTicks;
	private int lilyPadTicks;
	private int mossTicks;
	private int vineTicks;
	private int grazeTicks;
	private int tallGrassTicks;
	
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
			lilyPadTicks = settings.lilyPadPercent;
			mossTicks = settings.mossPercent;
			grazeTicks = settings.grazePercent;
			vineTicks = settings.vinePercent;
			tallGrassTicks = settings.tallGrassPercent;
			ActiveTickCount = 0;
		}
		
		if ( ( Tick - LastTick >= UpdateTicks ) )
		{	
			Player currentPlayer = vWorld.playerList.getNextPlayer();
			
			if( currentPlayer != null )
			{
				Location playerLocation = currentPlayer.getLocation();
				Block currentBlock = vWorld.blockCrawler.getRandomTopBlock( playerLocation, Material.AIR );
				
				//Todo: Implement delegates instead of switch case
				if ( currentBlock != null )
				{
					switch( currentBlock.getType() )
					{
					case GRASS:
						if ( settings.enableGrass && ( grassTicks > 0 ) )
						{
							vWorld.grass.growGrass( currentBlock );
							grassTicks--;
						}
						break;
					
					case CACTUS:
						if ( settings.enablePlants && settings.enableCacti && ( plantTicks > 0 ) )
						{
							vWorld.cacti.growCacti( currentBlock );
							plantTicks--;
						}
						break;
						
					case SUGAR_CANE_BLOCK:
						if ( settings.enablePlants && settings.enableCanes && ( plantTicks > 0 ) )
						{
							vWorld.canes.growCanes( currentBlock );
							plantTicks--;
						}
						break;
						
					case YELLOW_FLOWER:
						int blockTypeYellow = vWorld.world.getBlockTypeIdAt(currentBlock.getX(), currentBlock.getY() - 1, currentBlock.getZ());
						
						if( blockTypeYellow == Material.GRASS.getId() )
						{
							if ( settings.enablePlants && settings.enableFlowers && ( plantTicks > 0 ) )
							{
								vWorld.plants.growPlant( currentBlock , Material.YELLOW_FLOWER );
								plantTicks--;
							}
							
							if ( settings.enableGrass && ( grassTicks > 0 ) )
							{
								if( currentBlock.getRelative(BlockFace.DOWN).getType() == Material.GRASS )
								{
									vWorld.grass.growGrass( currentBlock.getRelative(BlockFace.DOWN) );
									grassTicks--;
								}
							}
							break;
						}
						else if ( blockTypeYellow == Material.SAND.getId() )
						{
							if ( settings.enablePlants && settings.enableFlowers && ( plantTicks > 0 ) )
							{
								vWorld.plants.growPlant( currentBlock , Material.YELLOW_FLOWER );
								plantTicks--;
							}
							break;
						}
						else if ( blockTypeYellow == Material.STATIONARY_WATER.getId() )
						{
							if ( settings.enablePlants && settings.enableLilyPads && ( lilyPadTicks > 0 ) )
							{
								vWorld.plants.growPlant( currentBlock , Material.YELLOW_FLOWER );
								lilyPadTicks--;
							}
							break;
						}
					
					case RED_ROSE:
						int blockTypeRed = vWorld.world.getBlockTypeIdAt(currentBlock.getX(), currentBlock.getY() - 1, currentBlock.getZ());
						
						if( blockTypeRed == Material.GRASS.getId() )
						{
							if ( settings.enablePlants && settings.enableFlowers && ( plantTicks > 0 ) )
							{
								vWorld.plants.growPlant( currentBlock , Material.RED_ROSE );
								plantTicks--;
							}
							
							if ( settings.enableGrass && ( grassTicks > 0 ) )
							{
								if( currentBlock.getRelative(BlockFace.DOWN).getType() == Material.GRASS )
								{
									vWorld.grass.growGrass( currentBlock.getRelative(BlockFace.DOWN) );
									grassTicks--;
								}
							}
							break;
						}
						else if ( blockTypeRed == Material.SAND.getId() )
						{
							if ( settings.enablePlants && settings.enableFlowers && ( plantTicks > 0 ) )
							{
								vWorld.plants.growPlant( currentBlock , Material.RED_ROSE );
								plantTicks--;
							}
							break;
						}
						else if ( blockTypeRed == Material.STATIONARY_WATER.getId() )
						{
							if ( settings.enablePlants && settings.enableLilyPads && ( lilyPadTicks > 0 ) )
							{
								vWorld.plants.growPlant( currentBlock , Material.RED_ROSE );
								lilyPadTicks--;
							}
							break;
						}
					
					case BROWN_MUSHROOM:
						if ( settings.enablePlants && settings.enableFungi && ( plantTicks > 0 ) )
						{
							vWorld.plants.growPlant( currentBlock , Material.BROWN_MUSHROOM );
							plantTicks--;
						}
						
						if ( settings.enableGrass && ( grassTicks > 0 ) )
						{
							if( currentBlock.getRelative(BlockFace.DOWN).getType() == Material.GRASS )
							{
								vWorld.grass.growGrass( currentBlock.getRelative(BlockFace.DOWN) );
								grassTicks--;
							}
						}
						break;
						
					case RED_MUSHROOM:
						if ( settings.enablePlants && settings.enableFungi && ( plantTicks > 0 ) )
						{
							vWorld.plants.growPlant( currentBlock , Material.RED_MUSHROOM );
							plantTicks--;
						}
						
						if ( settings.enableGrass && ( grassTicks > 0 ) )
						{
							if( currentBlock.getRelative(BlockFace.DOWN).getType() == Material.GRASS )
							{
								vWorld.grass.growGrass( currentBlock.getRelative(BlockFace.DOWN) );
								grassTicks--;
							}
						}
						break;
					
					case PUMPKIN:
						if ( settings.enablePlants && settings.enablePumpkins && ( plantTicks > 0 ) )
						{
							vWorld.plants.growPlant( currentBlock , Material.PUMPKIN );
							plantTicks--;
						}
						break;
						
					case COBBLESTONE:
						if ( settings.enableMoss && settings.waterGrowsMoss && ( mossTicks > 0 ) )
						{
							vWorld.moss.growMoss( currentBlock );
							mossTicks--;
						}
						break;
						
					case MOSSY_COBBLESTONE:
						if ( settings.enableMoss  && ( mossTicks > 0 ) )
						{
							vWorld.moss.growMoss( currentBlock );
							mossTicks--;
						}
						break;
						
					case LONG_GRASS:
						if( settings.enableTallGrass && ( tallGrassTicks > 0 ) )
						{
							vWorld.tGrass.spreadTallGrass(currentBlock);
							tallGrassTicks--;
						}
						break;
						
					case DEAD_BUSH:
						if( settings.enableTallGrass && ( tallGrassTicks > 0 ) )
						{
							vWorld.tGrass.spreadTallGrass(currentBlock);
							tallGrassTicks--;
						}
						break;
						
					default:
						break;
					}
				}
				currentBlock = null;
				
				// Vines
				currentBlock = vWorld.blockCrawler.getRandomBlock( playerLocation, Material.LOG );
				if( currentBlock != null )
				{
					if ( settings.enableVines && ( vineTicks > 0 ) )
					{
						// only grow vines on normal trees
						if( currentBlock.getData() == 0 )
						{
							vWorld.vines.growVines( currentBlock );
						}
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
