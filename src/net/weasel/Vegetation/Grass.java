package net.weasel.Vegetation;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Biome;
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


		// clear meta data of grass blocks on the fly
		int grassMeta = block.getData();
		if( grassMeta > 0 ) block.setData((byte)0);
		
		// 31 - Tall Grass Block
		// 0 = dead bush, 1 = tall grass, 2 = bush
		switch( block.getBiome() )
		{
		case FOREST:
			block.getRelative(BlockFace.UP).setTypeIdAndData(31, (byte) 1, false);
			break;
			
		case RAINFOREST:
			block.getRelative(BlockFace.UP).setTypeIdAndData(31, (byte) 1, false);
			break;
			
		case PLAINS:
			break;
			
		case SWAMPLAND:
			break;
			
		case TAIGA:
			break;
			
		case TUNDRA:
			break;
			
		case SAVANNA:
			block.getRelative(BlockFace.UP).setTypeIdAndData(31, (byte) 2, false);
			break;
		
		case SHRUBLAND:
			block.getRelative(BlockFace.UP).setTypeIdAndData(31, (byte) 1, false);
			break;
			
		case SEASONAL_FOREST:
			break;
			
		default:
			break;
		}
		
		/*if( V > maxGrassHeight + 1 )
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
    	int pX = (int)baseBlock.getX();
    	int pZ = (int)baseBlock.getZ();
    	    	
    	Block currentBlock = null;
    	
    	for( int x = pX-r; x <= pX+r; x++ )
    	{
    		for( int z = pZ-r; z <= pZ+r; z++ )
    		{
    			currentBlock = blockCrawler.getTopBlock(baseBlock, x, z, Material.AIR);
    			
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
