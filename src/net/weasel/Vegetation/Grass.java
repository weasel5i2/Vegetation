package net.weasel.Vegetation;

import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;

public final class Grass 
{
	public static void logOutput( String text ) { Vegetation.logOutput( text ); }
	public static boolean withinEnabledBiome( Block whichBlock ) { return Blocks.withinEnabledBiome( whichBlock ); }
	public static Block getTopBlock(World world, double X, double Z, int[] ignoreBlocks ) { return Blocks.getTopBlock(world, X, Z, ignoreBlocks ); }

	public static void growGrass( Player player )
	{
		if( player == null ) return;
		
		if( Vegetation.enableGrass == false ) return;
		
		if( player.getWorld().getTime() > 12000 && Vegetation.overGrowTicks > 0 ) 
		{
			if( Vegetation.overGrower != null ) Vegetation.overGrower.sendMessage( "It is too dark for plants to grow." );
			Vegetation.overGrowTicks = 0;
		}
		
		if( Vegetation.debugging ) logOutput( "Growing grass.." );

		Integer V;
		Block currentBlock = null;
		
		currentBlock = getRandomGrassBlock( player );

		if( currentBlock != null )
		{
			V = currentBlock.getData() + 1;
				
			if( V < 2 ) V = 2;
			if( V > 10 ) V = 10;
    
			if( Vegetation.debugging ) logOutput( "Adjusting block " + currentBlock.getX() + "," + currentBlock.getY() + "," + currentBlock.getZ() );
			currentBlock.setData( (byte)(int)V );
			
			if( Vegetation.overGrowTicks > 0 )
			{
				Vegetation.overGrowTicks--;
				
				if( Vegetation.overGrowTicks == 0 )
				{
					Vegetation.overGrower.sendMessage( "Overgrowth completed." );
					Vegetation.overGrowTicks = 0;
					Vegetation.overGrower = null;
					Vegetation.overGrowingPlants = false;
					Vegetation.tempGrassPerGrow = Vegetation.grassPerGrow;
				}
				if( Vegetation.overGrowTicks == 250 )
				{
					Vegetation.overGrower.sendMessage( "Overgrowth 75% done." );
				}
				if( Vegetation.overGrowTicks == 500 )
				{
					Vegetation.overGrower.sendMessage( "Overgrowth 50% done." );
				}
				if( Vegetation.overGrowTicks == 750 )
				{
					Vegetation.overGrower.sendMessage( "Overgrowth 25% done." );
				}
			}
		}
	}

	public static Block getRandomGrassBlock( Player player )
    {
    	Block retVal = null;
    	Integer range = Vegetation.growthRange;
    	
    	double pX = player.getLocation().getX();
    	double pZ = player.getLocation().getZ();
    	
    	double minX = (pX - (range / 2));
    	double minZ = (pZ - (range / 2));

    	Block currentBlock;
    	int[] ignoreBlocks = { 17, 18, 0 };
    	double tX, tZ;

    	while( retVal == null )
    	{
    		tX = minX + Vegetation.generator.nextInt(range);
    		tZ = minZ + Vegetation.generator.nextInt(range);

        	currentBlock = getTopBlock( player.getWorld(), tX, tZ, ignoreBlocks );
			
        	if( currentBlock != null )
        	{
	        	if( ( currentBlock.getTypeId() == 2 
	        	   && withinEnabledBiome( currentBlock ) == true
	        	   && ( currentBlock.getRelative(BlockFace.UP).getTypeId() == 0
	        	   || currentBlock.getRelative(BlockFace.UP).getTypeId() == 6
	        	   || currentBlock.getRelative(BlockFace.UP).getTypeId() == 37 
	        	   || currentBlock.getRelative(BlockFace.UP).getTypeId() == 38 
	        	   || currentBlock.getRelative(BlockFace.UP).getTypeId() == 39
	        	   || currentBlock.getRelative(BlockFace.UP).getTypeId() == 78
	        	   || currentBlock.getRelative(BlockFace.UP).getTypeId() == 40 ) )
	        	||
	        	( currentBlock.getTypeId() == 3 
	        	  || currentBlock.getRelative(BlockFace.UP).getTypeId() == 78 ) )
	        		retVal = currentBlock;
	        	else
	        		retVal = null;
        	}
    	}

    	if( retVal.getTypeId() != 2 ) retVal = null;  
    	
    	if( Vegetation.debugging && retVal != null ) logOutput( "Found grass block: " + retVal.getX() + "," + retVal.getY() + "," + retVal.getZ() );
    	
    	return( retVal );
    }
    
    public static void mowTheGrass( Player player, int height, boolean add )
    {
    	Integer R = Math.round( Vegetation.growthRange / 2 );
    	double pX = player.getLocation().getX();
    	double pZ = player.getLocation().getZ();
    	int[] ignoreBlocks = { 17, 18, 0 };
    	    	
    	Block currentBlock = null;
    	
    	for( double X = (pX-R); X <= (pX+R); X++ )
    	{
    		for( double Z = (pZ-R); Z <= (pZ+R); Z++ )
    		{
    			currentBlock = getTopBlock(player.getWorld(), X, Z, ignoreBlocks );
    			
    			if( currentBlock != null )
    			{
	    			if( currentBlock.getTypeId() == 2 ) 
	    			{
	    				if( add == true )
	    				{
	    					int nHeight = height + currentBlock.getData();
	    					if( nHeight > 10 ) nHeight = 10;
	    					currentBlock.setData( (byte) nHeight );
	    				}
	    				else
	    					currentBlock.setData( (byte) height );
	    			}
    			}
    		}
    	}
    }
}
