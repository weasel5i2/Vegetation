package net.weasel.Vegetation;

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
    	int maxCycle = 150;
    	int maxSpreadAmount = 5;
    	Block currentBlock = null;
    	
    	if( Vegetation.debugging ) logOutput("Spreading moss..");
    	
		for(int i = 0; i < maxCycle; i++ )
		{
			if( maxCycle%2 == 0 )
			{
				currentBlock = blockCrawler.getRandomTopBlock(block.getLocation() ,Material.COBBLESTONE, Material.STATIONARY_WATER, 25);
				if( currentBlock != null )
				{
					currentBlock.setType(Material.MOSSY_COBBLESTONE);
					if( Vegetation.debugging ) logOutput("Planting at: " + block.getX() + " " + block.getY() + " " + block.getZ());
					maxSpreadAmount--;
				}
			}
			else
			{
				currentBlock = blockCrawler.getRandomTopBlock(block.getLocation() , Material.COBBLESTONE, Material.AIR, 25);
				if( currentBlock != null )
				{
					if( blockCrawler.isAdjacentofBlockType2(currentBlock  , Material.STATIONARY_WATER)
							|| blockCrawler.isAdjacentofBlockType2(currentBlock  , Material.WATER) )
					{
						currentBlock.setType(Material.MOSSY_COBBLESTONE);
						if( Vegetation.debugging ) logOutput("Planting at: " + block.getX() + " " + block.getY() + " " + block.getZ());
						maxSpreadAmount--;
					}
				}
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
