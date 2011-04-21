package net.weasel.Vegetation;

import java.util.ArrayList;

import net.minecraft.server.EnumSkyBlock;

import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.entity.Player;

public class Plants 
{
	public static boolean withinEnabledBiome( Block whichBlock ) { return( Blocks.withinEnabledBiome( whichBlock ) ); }
	public static boolean isEnabledPlant( Block whichBlock ) { return Blocks.isEnabledPlant( whichBlock ); }
	public static boolean isLilyPad( Block whichBlock ) { return Blocks.isLilyPad( whichBlock ); }
	public static Block getRandomAdjacentEmptyBlock(Block targetBlock, Integer blockType ) { return( Blocks.getRandomAdjacentEmptyBlock( targetBlock, blockType ) ); }
	public static void logOutput( String text ) { Vegetation.logOutput( text ); }
	public static Block getTopBlock(World world, double X, double Z, int[] ignoreBlocks ) { return Blocks.getTopBlock(world, X, Z, ignoreBlocks ); }
	
	public static void growPlant(Player player)
	{
		if( player == null ) return;
		
		if( Vegetation.enablePlants == false ) return;
		
		if( Vegetation.debugging ) logOutput( "Spreading plants.." );

		Block currentBlock = null;

		currentBlock = Plants.getRandomPlantBlock( player );
				
		if( currentBlock != null )
		{
			if( Vegetation.debugging ) logOutput( "Found plant block: " + currentBlock.getTypeId() + " plant at " + currentBlock.getX() + "," + currentBlock.getY() + "," + currentBlock.getZ() + "." );

			Integer plantOnBlock = currentBlock.getTypeId();
					
			Block RB = Blocks.getAdjacentBlockForPlant( currentBlock, plantOnBlock );
	    	int[] ignoreBlocks = { 17, 18, 0 };
				
			if( RB != null )
			{
				Block blockAbove = getTopBlock(player.getWorld(), (double)RB.getX(), 
								   (double)RB.getZ(), ignoreBlocks ).getRelative(BlockFace.UP);
	
				if( blockAbove.getTypeId() == 0 )
				{
					if( Vegetation.debugging ) logOutput( "Spreading " + currentBlock.getTypeId() + " plant at " + RB.getX() + "," + RB.getY() + "," + RB.getZ() + "." );
					
					RB.setType(currentBlock.getType() );
				}
			}
		}
	}

	public static void growLilyPads(Player player)
	{
		if( player == null ) return;
		if( Vegetation.debugging ) logOutput( "Spreading lilypads.." );

		Block currentBlock = null;

		currentBlock = getRandomLilyPadBlock( player );
				
		if( currentBlock != null )
		{
			if( Vegetation.debugging ) logOutput( "Found lilypad block: " + currentBlock.getTypeId() + " plant at " + currentBlock.getX() + "," + currentBlock.getY() + "," + currentBlock.getZ() + "." );

			Integer plantOnBlock = currentBlock.getTypeId();
	    	int[] ignoreBlocks = { 17, 18, 0 };
					
			try
			{
				Block RB = Blocks.getRandomAdjacentEmptyBlock( currentBlock, plantOnBlock ).getRelative(BlockFace.UP);
				Block targetBlock = getTopBlock(player.getWorld(), (double)RB.getX(), 
				(double)RB.getZ(), ignoreBlocks ).getRelative(BlockFace.UP);
				
				if( targetBlock != null )
				{
					if( targetBlock.getTypeId() == 0 )
					{
						if( Vegetation.debugging ) logOutput( "Spreading lilypad " + currentBlock.getTypeId() + " plant at " + targetBlock.getX() + "," + targetBlock.getY() + "," + targetBlock.getZ() + "." );
					
						targetBlock.setType(currentBlock.getType() );
						int X = targetBlock.getRelative(BlockFace.UP).getX();
						int Y = targetBlock.getRelative(BlockFace.UP).getY();
						int Z = targetBlock.getRelative(BlockFace.UP).getZ();
						CraftWorld cWorld = (CraftWorld)player.getWorld();
						cWorld.getHandle().b(EnumSkyBlock.BLOCK, (int)X, (int)Y, (int)Z, 15);
					}
				}
			}
				
			catch( Exception e )
			{
				// Do nothing..
			}
		}
	}

	public static Block getRandomPlantBlock( Player player )
    {
    	Block currentBlock = null;
    	
		if( player == null ) return null;

		ArrayList<Block> plantBlocks = new ArrayList<Block>();
    	
    	double pX = player.getLocation().getX();
    	double pZ = player.getLocation().getZ();
    	
    	Integer minX = (int) (pX - (Vegetation.growthRange / 2));
    	Integer minZ = (int) (pZ - (Vegetation.growthRange / 2));
    	Integer maxX = (int) (pX + (Vegetation.growthRange / 2));
    	Integer maxZ = (int) (pZ + (Vegetation.growthRange / 2));

    	int[] ignoreBlocks = { 17, 18, 0 };

    	for( Integer X = minX; X <= maxX; X++ )
    	{
    		for( Integer Z = minZ; Z <= maxZ; Z++ )
    		{
    			currentBlock = getTopBlock(player.getWorld(), X, Z, ignoreBlocks );
    			    			
            	if( currentBlock != null )
            	{
        			if( isEnabledPlant(currentBlock) 
        			 && withinEnabledBiome(currentBlock) == true )
	    			{
            			plantBlocks.add(currentBlock);
	    			}
            	}
    		}
    	}

    	if( plantBlocks.size() > 0 )
    	{
    		Integer R = Vegetation.generator.nextInt( plantBlocks.size() );

    		return( plantBlocks.get(R) );
    	}
    	else
    		return( null );
    }

	public static Block getRandomLilyPadBlock( Player player )
    {
		if( player == null ) return null;
		
    	Block currentBlock = null;
    	
    	ArrayList<Block> lilyPadBlocks = new ArrayList<Block>();
    	
    	double pX = player.getLocation().getX();
    	double pZ = player.getLocation().getZ();
    	
    	Integer minX = (int) (pX - (Vegetation.growthRange / 2));
    	Integer minZ = (int) (pZ - (Vegetation.growthRange / 2));
    	Integer maxX = (int) (pX + (Vegetation.growthRange / 2));
    	Integer maxZ = (int) (pZ + (Vegetation.growthRange / 2));

    	int[] ignoreBlocks = { 17, 18, 0 };

    	for( Integer X = minX; X <= maxX; X++ )
    	{
    		for( Integer Z = minZ; Z <= maxZ; Z++ )
    		{
    			currentBlock = getTopBlock(player.getWorld(), X, Z, ignoreBlocks );
    			    			
            	if( currentBlock != null )
            	{
        			if( isLilyPad(currentBlock) && withinEnabledBiome(currentBlock) == true )
	    			{
            			lilyPadBlocks.add(currentBlock);
	    			}
            	}
    		}
    	}

    	if( lilyPadBlocks.size() > 0 )
    	{
    		Integer R = Vegetation.generator.nextInt( lilyPadBlocks.size() );

    		return( lilyPadBlocks.get(R) );
    	}
    	else
    		return( null );
    }
}
