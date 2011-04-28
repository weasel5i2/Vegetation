package net.weasel.Vegetation;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

public final class Grass 
{
	public static void logOutput( String text ) { Vegetation.logOutput( text ); }
	
	public static void growGrass( Block B )
	{	
		if( Vegetation.debugging ) logOutput( "Growing grass.." );


		int V = B.getData();
		if( V > Vegetation.maxGrassHeight + 1 )
		{
			B.setData( (byte)(Vegetation.maxGrassHeight + 1) );
			if( Vegetation.debugging ) logOutput( "Adjusting block " + B.getX() + "," + B.getY() + "," + B.getZ() + "V:" + V );
		}
		else
		{
			V++;
			if( V <= Vegetation.maxGrassHeight + 1 )
			{
				if( V < 2 ) V = 2;
				if( V > 10 ) V = 10;
				B.setData( (byte)V );
				if( Vegetation.debugging ) logOutput( "Adjusting block " + B.getX() + "," + B.getY() + "," + B.getZ() + "V:" + V );
			}
		}

		/*if( (int)B.getData() == 0 )
		{
			int V = Vegetation.generator.nextInt(10);
			if( V < 2 ) V = 2;
			if( Vegetation.debugging ) logOutput( "Adjusting block " + B.getX() + "," + B.getY() + "," + B.getZ() + "V:" + V );
			B.setData( (byte)V );
		}*/
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
	    			else if( CurrentBlock.getType() == Material.RED_ROSE
	    					|| CurrentBlock.getType() == Material.YELLOW_FLOWER 
	    					|| CurrentBlock.getType() == Material.RED_MUSHROOM
	    					|| CurrentBlock.getType() == Material.BROWN_MUSHROOM )
	    			{
	    				if( CurrentBlock.getRelative(BlockFace.DOWN).getType() == Material.GRASS )
	    					CurrentBlock.getRelative(BlockFace.DOWN).setData( (byte)0 );
	    			}
    			}
    		}
    	}
    }
}
