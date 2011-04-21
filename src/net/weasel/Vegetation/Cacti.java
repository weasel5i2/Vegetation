package net.weasel.Vegetation;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;

public class Cacti 
{
	public static boolean withinEnabledBiome( Block whichBlock ) { return( Blocks.withinEnabledBiome( whichBlock ) ); }
	public static boolean isEnabledPlant( Block whichBlock ) { return Blocks.isEnabledPlant( whichBlock ); }
	public static Block getRandomAdjacentEmptyBlock(Block targetBlock, Integer blockType ) { return( Blocks.getRandomAdjacentEmptyBlock( targetBlock, blockType ) ); }
	public static void logOutput( String text ) { Vegetation.logOutput( text ); }
	public static Block getTopBlock(World world, double X, double Z, int[] ignoreBlocks ) { return Blocks.getTopBlock(world, X, Z, ignoreBlocks ); }

	public static void growCacti(Player player)
	{
		if( player == null ) return;
		if( Vegetation.debugging ) logOutput( "Spreading cacti.." );

		Block currentBlock = null;

		currentBlock = getRandomCactusBlock( player );
				
		if( currentBlock != null )
		{
			if( Vegetation.debugging ) logOutput( "Found cactus block: " + currentBlock.getTypeId() + " plant at " + currentBlock.getX() + "," + currentBlock.getY() + "," + currentBlock.getZ() + "." );

			Integer plantOnBlock = currentBlock.getTypeId();
					
			try
			{
				Block RB = Blocks.getRandomAdjacentEmptyBlock( currentBlock, plantOnBlock ).getRelative(BlockFace.UP);
				Block targetBlock = RB.getRelative(BlockFace.UP);
				
				if( targetBlock != null )
				{
					if( targetBlock.getTypeId() == 0 )
					{
						if( Vegetation.debugging ) logOutput( "Spreading cactus " + currentBlock.getTypeId() + " plant at " + targetBlock.getX() + "," + targetBlock.getY() + "," + targetBlock.getZ() + "." );
					
						targetBlock.setType( Material.CACTUS );
					}
				}
			}
				
			catch( Exception e )
			{
				// Do nothing..
			}
		}
	}
	
	public static Block getRandomCactusBlock( Player player )
    {
    	Block currentBlock = null;
    	
		if( player == null ) return null;

		ArrayList<Block> cactusBlocks = new ArrayList<Block>();
    	
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
            		if( currentBlock.getType() == Material.CACTUS )
            		{
	        			if( isEnabledPlant(currentBlock) 
	        			 && withinEnabledBiome(currentBlock) == true )
		    			{
	            			cactusBlocks.add(currentBlock);
		    			}
            		}
            	}
    		}
    	}

    	if( cactusBlocks.size() > 0 )
    	{
    		Integer R = Vegetation.generator.nextInt( cactusBlocks.size() );

    		return( cactusBlocks.get(R) );
    	}
    	else
    		return( null );
    }
}
