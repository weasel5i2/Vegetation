package net.weasel.Vegetation;

import java.util.ArrayList;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.Material;

public class Plants 
{
	public static boolean isLilyPad( Block whichBlock ) { return Blocks.isLilyPad( whichBlock ); }
	public static void logOutput( String text ) { Vegetation.logOutput( text ); }
	
	public static void growPlant(Block B, Material PlantType)
	{
		Material[] M = { Material.YELLOW_FLOWER, Material.RED_ROSE, Material.BROWN_MUSHROOM, Material.RED_MUSHROOM, Material.PUMPKIN };
		if( Blocks.getFieldDensity( B, 3, M ) > 0.1 ) return;
		
		if( Vegetation.debugging ) logOutput( "Spreading plants.." );

		int MaxSpreadAmount = 2;
		
		if( Vegetation.debugging ) logOutput( "Spreading Type of Plant: " + PlantType.toString() );
		
		Block PlantBlock = null;
		//Get surrounding block and place new plant
		for( int I = 0; I < 150; I++ )
		{
			PlantBlock = Blocks.getRandomTopBlock( B.getLocation() , Material.GRASS, Material.AIR, 3);
			if( PlantBlock != null )
			{
				PlantBlock.getRelative(BlockFace.UP).setType(PlantType);
				if( Vegetation.debugging ) logOutput( "Planting at: " + PlantBlock.getX() + " " + PlantBlock.getY() + " " + PlantBlock.getZ() );
				MaxSpreadAmount--;
			}

			if( MaxSpreadAmount <= 0 ) break;
		}
	}
	
	public static boolean growSinglePlant(Block B, Material PlantType)
	{
		B.getRelative(BlockFace.UP).setType(PlantType);
		if( Vegetation.debugging ) logOutput( "Planting at: " + B.getX() + " " + B.getY() + " " + B.getZ() );
		return true;
	}

	/*public static void growLilyPads(Player player)
	{
		if( player == null ) return;
		if( Vegetation.debugging ) logOutput( "Spreading lilypads.." );

		Block currentBlock = null;

		currentBlock = getRandomLilyPadBlock( player );
				
		if( currentBlock != null )
		{
			if( Vegetation.debugging ) logOutput( "Found lilypad block: " + currentBlock.getTypeId() + " plant at " + currentBlock.getX() + "," + currentBlock.getY() + "," + currentBlock.getZ() + "." );

			Integer plantOnBlock = currentBlock.getTypeId();
	    	//int[] ignoreBlocks = { 17, 18, 0 };
					
			try
			{
				Block RB = Blocks.getRandomAdjacentEmptyBlock( currentBlock, plantOnBlock ).getRelative(BlockFace.UP);
				Block targetBlock = getTopBlock(player.getLocation(), (double)RB.getX(), 
				(double)RB.getZ() ).getRelative(BlockFace.UP);
				
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
	}*/

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

    	//int[] ignoreBlocks = { 17, 18, 0 };

    	for( Integer X = minX; X <= maxX; X++ )
    	{
    		for( Integer Z = minZ; Z <= maxZ; Z++ )
    		{
    			currentBlock = Blocks.getTopBlock(player.getLocation(), X, Z, null );
    			    			
            	if( currentBlock != null )
            	{
        			if( isLilyPad(currentBlock) && Blocks.withinEnabledBiome(currentBlock.getBiome()) == true )
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
