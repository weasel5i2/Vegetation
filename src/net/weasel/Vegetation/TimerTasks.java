package net.weasel.Vegetation;

import net.minecraft.server.EnumSkyBlock;
import net.weasel.Vegetation.Vegetation;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.CraftWorld;

public class TimerTasks implements Runnable
{
	public static void logOutput( String text ) { Vegetation.logOutput( text ); }
	public static boolean withinEnabledBiome( Block whichBlock ) { return Blocks.withinEnabledBiome( whichBlock ); }
	public static Block getTopBlock(World world, double X, double Z, int[] ignoreBlocks ) { return Blocks.getTopBlock(world, X, Z, ignoreBlocks ); }
	
	public static Vegetation plugin;
	public static Server server;

	private Block targetBlock = null;
	private int X, Y, Z;
	
	public TimerTasks(Vegetation instance)
	{
		plugin = instance;
		server = plugin.getServer();
	}

	@Override
	public void run() 
	{
		if( server.getOnlinePlayers().length == 0 ) return;
		
		if( Vegetation.overGrowTicks > 0 )
		{
			Grass.growGrass( Vegetation.overGrower );
			Grass.growGrass( Vegetation.overGrower );
			Grass.growGrass( Vegetation.overGrower );
			Grass.growGrass( Vegetation.overGrower );
			Grass.growGrass( Vegetation.overGrower );
			Grass.growGrass( Vegetation.overGrower );
			Grass.growGrass( Vegetation.overGrower );
			Grass.growGrass( Vegetation.overGrower );
			Grass.growGrass( Vegetation.overGrower );
			Grass.growGrass( Vegetation.overGrower );
			
			if( Vegetation.overGrowingPlants )
			{
				Plants.growPlant( Vegetation.overGrower );
				Plants.growLilyPads( Vegetation.overGrower );
				Cacti.growCacti( Vegetation.overGrower );
			}

			return;
		}
		
		Vegetation.getNextPlayer();
		Vegetation.timerTick += 0.5;
		
		if( ( Vegetation.grassTicks + Vegetation.mossTicks + Vegetation.plantTicks + Vegetation.grazeTicks + Vegetation.vineTicks ) >= 100 )
		{
			Vegetation.timerTick = 0;
			Vegetation.grassTicks = 0;
			Vegetation.mossTicks = 0;
			Vegetation.plantTicks = 0;
			Vegetation.grazeTicks = 0;
			Vegetation.vineTicks = 0;
		}
		
		if( Vegetation.lastCycle == 0 )
		{
			if( Vegetation.enableGrass && ( Vegetation.grassTicks < Vegetation.grassPercent ) )
			{
				Vegetation.grassTicks += 0.5;
				for( int GPG = 0; GPG < Vegetation.tempGrassPerGrow; GPG++ )
				{
					Grass.growGrass(Vegetation.currentPlayer);
				}
			}
			Vegetation.lastCycle = 1;
		}
		else if( Vegetation.lastCycle == 1 )
		{
			if( Vegetation.enablePlants && ( Vegetation.plantTicks < Vegetation.plantsPercent ) )
			{
				Vegetation.plantTicks += 0.5;
				Plants.growPlant( Vegetation.currentPlayer );
				
				if( Vegetation.enableCacti == true ) Cacti.growCacti( Vegetation.currentPlayer );
			}			
			Vegetation.lastCycle = 2;
		}
		
		else if( Vegetation.lastCycle == 2 )
		{
			if( Vegetation.enableMoss && ( Vegetation.mossTicks < Vegetation.mossPercent ) )
			{
				Vegetation.mossTicks += 0.5;
				Moss.growMoss( Vegetation.currentPlayer );
			}
			Vegetation.lastCycle = 3;
		}
		
		else if( Vegetation.lastCycle == 3 )
		{
			if( Vegetation.playerList.size() == 0 ) return;
			
			if( Vegetation.enableGrazers && ( Vegetation.grazeTicks < Vegetation.grazePercent ) )
			{
				Vegetation.grazeTicks += 0.5;
				Grazers.grazeAnimals();
			}
			Vegetation.lastCycle = 4;
		}
		else if( Vegetation.lastCycle == 4 )
		{
			if( Vegetation.playerList.size() == 0 ) return;

			if( Vegetation.enableLilyPads && ( Vegetation.lilyPadTicks < Vegetation.lilyPadPercent ) )
			{
				targetBlock = Plants.getRandomLilyPadBlock( Vegetation.currentPlayer );
				if( targetBlock != null )
				{
					Vegetation.lilyPadTicks += 0.5;
					X = targetBlock.getRelative(BlockFace.UP).getX();
					Y = targetBlock.getRelative(BlockFace.UP).getY();
					Z = targetBlock.getRelative(BlockFace.UP).getZ();
					CraftWorld cWorld = (CraftWorld)targetBlock.getWorld();
					cWorld.getHandle().b(EnumSkyBlock.BLOCK, (int)X, (int)Y, (int)Z, 15);
				}
			}
			
			Vegetation.lastCycle = 5;
		}
		
		else if( Vegetation.lastCycle == 5 )
		{
			if( Vegetation.enableVines && ( Vegetation.vineTicks < Vegetation.vinePercent ) )
			{
				int R = Vegetation.generator.nextInt( 10 );
				
				if( Math.round(R/3) == (R/3) )
				{
					targetBlock = Vines.getRandomLeafBlock( Vegetation.currentPlayer );
				
					if( targetBlock != null )
					{
						Vegetation.vineTicks += 0.5;
						Vines.growVineAt( Vegetation.currentPlayer, targetBlock, 1 );
					}
				}
				else
				{
					targetBlock = Vines.getRandomVineBlock( Vegetation.currentPlayer );
					
					if( targetBlock != null )
					{
						Vegetation.vineTicks++;
						Vines.growVineAt( Vegetation.currentPlayer, targetBlock, 1 );
					}
				}
			}
			
			Vegetation.lastCycle = 0;
		}
	}
}