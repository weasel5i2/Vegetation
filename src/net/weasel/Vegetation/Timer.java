package net.weasel.Vegetation;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;

public class Timer implements Runnable {
	
	public void logOutput( String text ) { Vegetation.logOutput( text ); }
	
	private VegetationWorld vWorld;
	private PlayerList pList;
	private long LastTick = System.currentTimeMillis();
	private long UpdateTicks = 200;
	private int ActiveTickCount = 100;
	
	private int plantTicks = 0;
	private int grassTicks = 0;
	//private int lilyPadTicks = 0;
	private int mossTicks = 0;
	private int vineTicks = 0;
	private int grazeTicks = 0;
	
	public Timer(VegetationWorld w)
	{
		vWorld = w;
		pList = w.getPlayerList();
	}

	@Override
	public void run()
	{
		long Tick = System.currentTimeMillis();
		
		if ( ActiveTickCount >= 100 )
		{
			plantTicks = vWorld.plantsPercent;
			grassTicks = vWorld.grassPercent;
			//lilyPadTicks = 0;
			mossTicks = vWorld.mossPercent;
			grazeTicks = vWorld.grazePercent;
			vineTicks = vWorld.vinePercent;
			ActiveTickCount = 0;
		}
		
		if ( ( Tick - LastTick >= UpdateTicks ) )
		{	
			Player currentPlayer = pList.getNextPlayer();
			
			if( currentPlayer != null )
			{
				Block CB = Blocks.getRandomTopBlock( currentPlayer.getLocation(), Material.AIR );
				
				//Todo: Implement delegates instead of switch case
				if ( CB != null )
				{
					switch( CB.getType() )
					{
					case GRASS:
						if ( vWorld.enableGrass && ( grassTicks > 0 ) )
						{
							Grass.growGrass( CB );
							grassTicks--;
						}
						break;
					
					case CACTUS:
						if ( vWorld.enablePlants && vWorld.enableCacti && ( plantTicks > 0 ) )
						{
							Cacti.growCacti( CB );
							plantTicks--;
						}
						break;
						
					case SUGAR_CANE_BLOCK:
						if ( vWorld.enablePlants && vWorld.enableCanes && ( plantTicks > 0 ) )
						{
							Canes.growCanes( CB );
							plantTicks--;
						}
						break;
						
					case YELLOW_FLOWER:
						if ( vWorld.enablePlants && vWorld.enableFlowers && ( plantTicks > 0 ) )
						{
							Plants.growPlant( CB , Material.YELLOW_FLOWER );
							plantTicks--;
						}
						
						if ( vWorld.enableGrass && ( grassTicks > 0 ) )
						{
							if( CB.getRelative(BlockFace.DOWN).getType() == Material.GRASS )
							{
								Grass.growGrass( CB.getRelative(BlockFace.DOWN) );
								grassTicks--;
							}
						}
						break;
					
					case RED_ROSE:
						if ( vWorld.enablePlants && vWorld.enableFlowers && ( plantTicks > 0 ) )
						{
							Plants.growPlant( CB , Material.RED_ROSE );
							plantTicks--;
						}
						
						if ( vWorld.enableGrass && ( grassTicks > 0 ) )
						{
							if( CB.getRelative(BlockFace.DOWN).getType() == Material.GRASS )
							{
								Grass.growGrass( CB.getRelative(BlockFace.DOWN) );
								grassTicks--;
							}
						}
						break;
					
					case BROWN_MUSHROOM:
						if ( vWorld.enablePlants && vWorld.enableFungi && ( plantTicks > 0 ) )
						{
							Plants.growPlant( CB , Material.BROWN_MUSHROOM );
							plantTicks--;
						}
						
						if ( vWorld.enableGrass && ( grassTicks > 0 ) )
						{
							if( CB.getRelative(BlockFace.DOWN).getType() == Material.GRASS )
							{
								Grass.growGrass( CB.getRelative(BlockFace.DOWN) );
								grassTicks--;
							}
						}
						break;
						
					case RED_MUSHROOM:
						if ( vWorld.enablePlants && vWorld.enableFungi && ( plantTicks > 0 ) )
						{
							Plants.growPlant( CB , Material.RED_MUSHROOM );
							plantTicks--;
						}
						
						if ( vWorld.enableGrass && ( grassTicks > 0 ) )
						{
							if( CB.getRelative(BlockFace.DOWN).getType() == Material.GRASS )
							{
								Grass.growGrass( CB.getRelative(BlockFace.DOWN) );
								grassTicks--;
							}
						}
						break;
					
					case PUMPKIN:
						if ( vWorld.enablePlants && vWorld.enablePumpkins && ( plantTicks > 0 ) )
						{
							Plants.growPlant( CB , Material.PUMPKIN );
							plantTicks--;
						}
						break;
						
					case COBBLESTONE:
						if ( vWorld.enableMoss && vWorld.waterGrowsMoss && ( mossTicks > 0 ) )
						{
							Moss.growMoss( CB );
							mossTicks--;
						}
						break;
						
					case MOSSY_COBBLESTONE:
						if ( vWorld.enableMoss  && ( mossTicks > 0 ) )
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
					if ( vWorld.enableVines && ( vineTicks > 0 ) )
					{
						Vines.growVines( CB );
						vineTicks--;
					}
				}
				
				if( vWorld.enableGrazers && grazeTicks > 0 )
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
