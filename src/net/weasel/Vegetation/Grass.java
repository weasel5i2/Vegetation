package net.weasel.Vegetation;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

public final class Grass 
{
	public static void logOutput( String text ) { Vegetation.logOutput( text ); }
	
	public static void growGrass( Block B )
	{	
		if( Vegetation.debugging ) logOutput( "Growing grass.." );

		Integer V;

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
    
    public static void MowGrass( Location BaseBlock , int V )
    {
    	//max grass height
    	if( V > 10 ) V = 10;
    	//no grass
    	if( V < 1 ) V = 1;
    	
    	int R = Math.round( Vegetation.growthRange / 2 );
    	double pX = BaseBlock.getX();
    	double pZ = BaseBlock.getZ();
    	    	
    	Block CurrentBlock = null;
    	
    	for( double X = (pX-R); X <= (pX+R); X++ )
    	{
    		for( double Z = (pZ-R); Z <= (pZ+R); Z++ )
    		{
    			CurrentBlock = Blocks.getTopBlock(BaseBlock, X, Z );
    			
    			if( CurrentBlock != null )
    			{
	    			if( CurrentBlock.getType() == Material.GRASS ) 
	    			{
	    				CurrentBlock.setData( (byte)V );
	    			}
    			}
    		}
    	}
    }
}
