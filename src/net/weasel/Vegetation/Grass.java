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

		/*Integer V;

		V = B.getData() + 1;

		if( V < 2 ) V = 2;
		if( V > 10 ) V = 10;*/

		if( (int)B.getData() == 0 )
		{
			int V = Vegetation.generator.nextInt(10);
			if( V < 2 ) V = 2;
			if( Vegetation.debugging ) logOutput( "Adjusting block " + B.getX() + "," + B.getY() + "," + B.getZ() + "V:" + V );
			B.setData( (byte)V );
		}
	}
    
    public static void mowGrass( Location BaseBlock )
    {    	
    	int R = Math.round( 50 / 2 );
    	double pX = BaseBlock.getX();
    	double pZ = BaseBlock.getZ();
    	    	
    	Block CurrentBlock = null;
    	
    	for( double X = pX-R; X <= pX+R; X++ )
    	{
    		for( double Z = pZ-R; Z <= pZ+R; Z++ )
    		{
    			CurrentBlock = Blocks.getTopBlock(BaseBlock, X, Z, Material.AIR );
    			
    			if( CurrentBlock != null )
    			{
	    			if( CurrentBlock.getType() == Material.GRASS ) 
	    			{
	    				CurrentBlock.setData( (byte)0 );
	    			}
    			}
    		}
    	}
    }
}
