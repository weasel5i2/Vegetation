package net.weasel.Vegetation;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.Location;

public final class Grass 
{
	public static void logOutput( String text ) { Vegetation.logOutput( text ); }
	public static boolean withinEnabledBiome( Block whichBlock ) { return Blocks.withinEnabledBiome( whichBlock ); }
	public static Block getTopBlock(Location BaseBlock, double X, double Z ) { return Blocks.getTopBlock(BaseBlock, X, Z ); }

	public static void growGrass( Player player )
	{
		if( Vegetation.enableGrass == false ) return;
		
		if( player == null ) return;
		
		if( player.getWorld().getTime() > 12000 && Vegetation.overGrowTicks > 0 ) 
		{
			if( Vegetation.overGrower != null ) Vegetation.overGrower.sendMessage( "It is too dark for plants to grow." );
			Vegetation.overGrowTicks = 0;
		}
		
		if( Vegetation.debugging ) logOutput( "Growing grass.." );

		Integer V;
		Block currentBlock = null;
		
		currentBlock = Blocks.getRandomBlock( player.getLocation(), Material.GRASS );

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
	
	public static void growGrass( Block B )
	{	
		if( Vegetation.debugging ) logOutput( "Growing grass.." );

		int V;


		V = B.getData() + 1;

		if( V < 2 ) V = 2;
		if( V > 10 ) V = 10;

		if( Vegetation.debugging ) logOutput( "Adjusting block " + B.getX() + "," + B.getY() + "," + B.getZ() );
		B.setData( (byte)(int)V );

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
	
	/*public static Block getRandomGrassBlock( Player player )
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

        	//currentBlock = getTopBlock( player.getWorld(), tX, tZ, ignoreBlocks );
    		currentBlock = getTopBlock( player.getLocation(), tX, tZ, ignoreBlocks );
			
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
        	if ( retVal != null ) break;
    	}

    	if( retVal.getTypeId() != 2 ) retVal = null;  
        	
        if( Vegetation.debugging && retVal != null ) logOutput( "Found grass block: " + retVal.getX() + "," + retVal.getY() + "," + retVal.getZ() );

    	
    	return( retVal );
    }*/
	
	/*public static Block getRandomGrassBlock( Player player )
    {
    	Block retVal = null;
    	int range = Vegetation.growthRange;
    	int MaxCycle = 50;
    	
    	double pX = player.getLocation().getX();
    	double pZ = player.getLocation().getZ();
    	
    	double minX = (pX - (range / 2));
    	double minZ = (pZ - (range / 2));

    	Block currentBlock;
    	double tX, tZ;

    	for ( int I = 0; I < MaxCycle; I++ )
    	{
    		tX = minX + Vegetation.generator.nextInt(range);
    		tZ = minZ + Vegetation.generator.nextInt(range);

    		currentBlock = getTopBlock( player.getLocation(), tX, tZ );
    		if ( currentBlock != null && currentBlock.getType() == Material.GRASS )
    		{
    			retVal = currentBlock;
    			if( Vegetation.debugging ) logOutput( "Found grass block: " + retVal.getX() + "," + retVal.getY() + "," + retVal.getZ() );
    			break;
    		}
    	}
    	return retVal;
    }*/
    
    public static void mowTheGrass( Player player, int height, boolean add )
    {
    	Integer R = Math.round( Vegetation.growthRange / 2 );
    	double pX = player.getLocation().getX();
    	double pZ = player.getLocation().getZ();
    	//int[] ignoreBlocks = { 17, 18, 0 };
    	    	
    	Block currentBlock = null;
    	
    	for( double X = (pX-R); X <= (pX+R); X++ )
    	{
    		for( double Z = (pZ-R); Z <= (pZ+R); Z++ )
    		{
    			currentBlock = getTopBlock(player.getLocation(), X, Z );
    			
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
