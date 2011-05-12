package net.weasel.Vegetation;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.block.Block;

public class Moss
{
	public void logOutput(String text) { Vegetation.logOutput(text); }
	
	private BlockCrawler blockCrawler;
	
	/*
	 * generates a new Moss object
	 * 
	 * @param b
	 */
	public Moss(BlockCrawler b)
	{
		blockCrawler = b;
	}
    
	/*
	 * spreads moss from given block
	 * 
	 * @param block
	 */
    public void growMoss(Block block)
    {
    	int maxSpreadAmount = 2;
    	
    	if( Vegetation.debugging ) logOutput("Spreading moss..");
    	
    	ArrayList<Block> blocks = blockCrawler.getBlocksInRange(block, Material.COBBLESTONE, 1);
    	
    	for( int i = 0; i < blocks.size(); i++ )
    	{
    		if( blockCrawler.isAdjacentofBlockType2(blocks.get(i), Material.STATIONARY_WATER) )
    		{
    			blocks.get(i).setType(Material.MOSSY_COBBLESTONE);
    			maxSpreadAmount--;
    		}
    		
    		if( maxSpreadAmount <= 0 ) break;
    	}
    }
    
    /*
     * grows moss on given block
     * 
     * @param block
     */
    public boolean growSingleMoss(Block block)
    {
    	block.setType(Material.MOSSY_COBBLESTONE);
    	if( Vegetation.debugging ) logOutput("Planting at: " + block.getX() + " " + block.getY() + " " + block.getZ());
    	return true;
    }
}
