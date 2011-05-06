package net.weasel.Vegetation;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

public class Grass 
{
	public void logOutput(String text) { Vegetation.logOutput(text); }
	
	private BlockCrawler blockCrawler;
	private int maxGrassHeight;
	
	/*
	 * generates a new Grass object
	 * 
	 * @param b
	 * @param maxHeight
	 */
	public Grass(BlockCrawler b, int maxHeight)
	{
		blockCrawler = b;
		maxGrassHeight = maxHeight;
	}
	
	/*
	 * grows grass on give block
	 * 
	 * @param block
	 */
	public void growGrass(Block block)
	{	
		if( Vegetation.debugging ) logOutput( "Growing grass.." );


		int V = block.getData();
		if( V > maxGrassHeight + 1 )
		{
			block.setData((byte)(maxGrassHeight + 1));
			if( Vegetation.debugging ) logOutput("Adjusting block " + block.getX() + "," + block.getY() + "," + block.getZ() + "V:" + V);
		}
		else
		{
			V++;
			if( V <= maxGrassHeight + 1 )
			{
				if( V < 2 ) V = 2;
				if( V > 10 ) V = 10;
				block.setData((byte)V);
				if( Vegetation.debugging ) logOutput("Adjusting block " + block.getX() + "," + block.getY() + "," + block.getZ() + "V:" + V);
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
    
	/*
	 * cuts down grass in an area around give location
	 * 
	 * @param baseBlock
	 */
    public void mowGrass(Location baseBlock)
    {    	
    	int r = Math.round( 50 / 2 );
    	double pX = baseBlock.getX();
    	double pZ = baseBlock.getZ();
    	    	
    	Block currentBlock = null;
    	
    	for( double x = pX-r; x <= pX+r; x++ )
    	{
    		for( double Z = pZ-r; Z <= pZ+r; Z++ )
    		{
    			currentBlock = blockCrawler.getTopBlock(baseBlock, x, Z, Material.AIR);
    			
    			if( currentBlock != null )
    			{
	    			if( currentBlock.getType() == Material.GRASS ) 
	    			{
	    				currentBlock.setData((byte)0);
	    			}
	    			else if( currentBlock.getType() == Material.RED_ROSE
	    					|| currentBlock.getType() == Material.YELLOW_FLOWER 
	    					|| currentBlock.getType() == Material.RED_MUSHROOM
	    					|| currentBlock.getType() == Material.BROWN_MUSHROOM )
	    			{
	    				if( currentBlock.getRelative(BlockFace.DOWN).getType() == Material.GRASS )
	    					currentBlock.getRelative(BlockFace.DOWN).setData((byte)0);
	    			}
    			}
    		}
    	}
    }
}
