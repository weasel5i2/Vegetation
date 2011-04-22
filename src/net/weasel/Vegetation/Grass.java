package net.weasel.Vegetation;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.Location;

public final class Grass 
{
	public static void logOutput( String text ) { Vegetation.logOutput( text ); }
	
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
    			currentBlock = Blocks.getTopBlock(player.getLocation(), X, Z );
    			
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
