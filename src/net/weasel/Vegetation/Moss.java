package net.weasel.Vegetation;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;

public final class Moss
{
	public static void logOutput( String text ) { Vegetation.logOutput( text ); }
    public static ArrayList<Block> getBlockRange(World world, Integer X, Integer Y, Integer Z, Integer Xsize, Integer Ysize ) { return Blocks.getBlockRange( world,X, Y, Z, Xsize, Ysize ); }
    
    public static void spreadMoss(Block B)
    {
    	int MaxCycle = 150;
    	int MaxSpreadAmount = 3;
    	Block CurrentBlock = null;
    	
    	if( Vegetation.debugging ) logOutput( "Spreading moss.." );
    	
		for(int I = 0; I < MaxCycle; I++ )
		{
			CurrentBlock = Blocks.getRandomBlock( B.getLocation() , Material.COBBLESTONE, null, 5 );
			if( CurrentBlock != null )
			{
				if( Blocks.isAdjacentBlockofType2( CurrentBlock  , Material.STATIONARY_WATER )
						|| Blocks.isAdjacentBlockofType2( CurrentBlock  , Material.WATER ) )
				{
					CurrentBlock.setType(Material.MOSSY_COBBLESTONE);
					MaxSpreadAmount--;
				}
			}
			
			if( MaxSpreadAmount <= 0 ) break;
		}
    }
    
	/*public static void growMoss( Player player )
	{
		if( player == null ) return;
	
		if( Vegetation.enableMoss == false ) return;
		
		Block currentBlock = null;
		Block spreadBlock = null;
		
		if( Vegetation.debugging ) logOutput( "Spreading moss.." );

		if( Vegetation.waterGrowsMoss )
		{
			currentBlock = getWetCobblestoneBlock( player ); 

			if( currentBlock != null )
			{
				currentBlock.setType( Material.MOSSY_COBBLESTONE );
			}
		}
		
    	spreadBlock = null;
		currentBlock = getMossyStoneBlock( player );

		if( currentBlock != null )
		{
			spreadBlock = getAdjacentCobblestone(currentBlock);
					
			if( spreadBlock != null )
			{
				if( Vegetation.debugging ) logOutput( "Spreading moss to block " + spreadBlock.getTypeId() + " at " + spreadBlock.getX() + "," + spreadBlock.getY() + "," + spreadBlock.getZ() + "." );
				
				spreadBlock.setType( Material.MOSSY_COBBLESTONE );
			}
		}
	}*/
    
    public static void growMoss(Block B)
    {
    	int MaxCycle = 150;
    	int MaxSpreadAmount = 3;
    	Block CurrentBlock = null;
    	
		for(int I = 0; I < MaxCycle; I++ )
		{
			CurrentBlock = Blocks.getRandomBlock( B.getLocation() , Material.STATIONARY_WATER );
			if( CurrentBlock != null )
			{
				CurrentBlock.setType(Material.MOSSY_COBBLESTONE);
				MaxSpreadAmount--;
			}
			
			if( MaxSpreadAmount <= 0 ) break;
		}
    }

    public static Block getMossyStoneBlock( Player player )
    {
    	double pX = player.getLocation().getX();
    	double pY = player.getLocation().getY();
    	double pZ = player.getLocation().getZ();

    	Block currentBlock = null;
    	Integer X;
    	
    	ArrayList<Block> blockRange = getBlockRange( player.getWorld(), (int)pX, (int)pY, (int)pZ, Vegetation.growthRange, Vegetation.verticalRadius );
    	ArrayList<Block> validBlocks = new ArrayList<Block>();
    	
    	for( X = 0; X < blockRange.size(); X++ )
    	{
    		currentBlock = blockRange.get(X);
    	
    		if( currentBlock != null )
    		{
        		if( currentBlock.getTypeId() == 48 ) validBlocks.add( currentBlock );
    		}
    		
    	}
    	
    	if( validBlocks.size() > 0 )
    	{
    		Integer R = Vegetation.generator.nextInt( validBlocks.size() );
    		return( validBlocks.get(R) );
    	}
    	else
    		return( null );
    }
    
    public static Block getWetCobblestoneBlock( Player player )
    {
    	double pX = player.getLocation().getX();
    	double pY = player.getLocation().getY();
    	double pZ = player.getLocation().getZ();

    	Block currentBlock = null;
    	Integer X;
    	
    	ArrayList<Block> blockRange = getBlockRange( player.getWorld(), (int)pX, (int)pY, (int)pZ, Vegetation.growthRange, Vegetation.verticalRadius );
    	ArrayList<Block> validBlocks = new ArrayList<Block>();
    	
    	for( X = 0; X < blockRange.size(); X++ )
    	{
    		currentBlock = blockRange.get(X);
    	
    		if( currentBlock != null )
    		{
        		if( currentBlock.getTypeId() == 4 && isTouchingWater(currentBlock) ) 
        		{
        			validBlocks.add( currentBlock );
        		}
    		}
    		
    	}
    	
    	if( validBlocks.size() > 0 )
    	{
    		Integer R = Vegetation.generator.nextInt( validBlocks.size() );
    		return( validBlocks.get(R) );
    	}
    	else
    		return( null );
    	
    }

    public static Block getAdjacentCobblestone( Block whichBlock )
    {
		boolean blockFound = false;
		Block cobble = null;
		Block retVal = null;
		
		try
		{
			if( !blockFound )
			{
				cobble = whichBlock.getRelative(BlockFace.NORTH);

				if( cobble.getTypeId() == 4 )
				{
					retVal = cobble;
					blockFound = true;
				}
			}
			
			if( !blockFound )
			{
				cobble = whichBlock.getRelative(BlockFace.EAST);
				
				if( cobble != null )
				{
					if( cobble.getTypeId() == 4 )
					{
						retVal = cobble;
						blockFound = true;
					}
				}
			}

			if( !blockFound )
			{
				cobble = whichBlock.getRelative(BlockFace.SOUTH);
				
				if( cobble != null )
				{
					if( cobble.getTypeId() == 4 )
					{
						retVal = cobble;
						blockFound = true;
					}
				}
			}

			if( !blockFound )
			{
				cobble = whichBlock.getRelative(BlockFace.WEST);
				
				if( cobble != null )
				{
					if( cobble.getTypeId() == 4 )
					{
						retVal = cobble;
						blockFound = true;
					}
				}
			}

			if( !blockFound )
			{
				cobble = whichBlock.getRelative(BlockFace.UP);
				
				if( cobble != null )
				{
					if( cobble.getTypeId() == 4 )
					{
						retVal = cobble;
						blockFound = true;
					}
				}
			}

			if( !blockFound )
			{
				cobble = whichBlock.getRelative(BlockFace.DOWN);
				
				if( cobble != null )
				{
					if( cobble.getTypeId() == 4 )
					{
						retVal = cobble;
						blockFound = true;
					}
				}
			}
		}
		catch( Exception e )
		{
			retVal = null;
		}

		return retVal;
    }
    
    public static boolean isTouchingWater( Block block )
    {
    	boolean retVal = false;
    	
    	if( block.getRelative(BlockFace.UP).getTypeId() == 8 
    	|| block.getRelative(BlockFace.UP).getTypeId() == 9 ) retVal = true;
    	
    	if( block.getRelative(BlockFace.DOWN).getTypeId() == 8 
    	    	|| block.getRelative(BlockFace.DOWN).getTypeId() == 9 ) retVal = true;
    	    	
    	if( block.getRelative(BlockFace.NORTH).getTypeId() == 8 
    	    	|| block.getRelative(BlockFace.NORTH).getTypeId() == 9 ) retVal = true;
    	    	
    	if( block.getRelative(BlockFace.EAST).getTypeId() == 8 
    	    	|| block.getRelative(BlockFace.EAST).getTypeId() == 9 ) retVal = true;
    	    	
    	if( block.getRelative(BlockFace.SOUTH).getTypeId() == 8 
    	    	|| block.getRelative(BlockFace.SOUTH).getTypeId() == 9 ) retVal = true;
    	    	
    	if( block.getRelative(BlockFace.WEST).getTypeId() == 8 
    	    	|| block.getRelative(BlockFace.WEST).getTypeId() == 9 ) retVal = true;
    	    	
    	return retVal;
    }
}
