package net.weasel.Vegetation;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.Material;

public class Plants 
{
	public void logOutput(String text) { Vegetation.logOutput(text); }
	
	private BlockCrawler blockCrawler;
	
	public Plants(BlockCrawler b)
	{
		blockCrawler = b;
	}
	
	public void growPlant(Block plantBlock, Material plantType)
	{
		Material[] material = { Material.YELLOW_FLOWER, Material.RED_ROSE, Material.BROWN_MUSHROOM, Material.RED_MUSHROOM, Material.PUMPKIN };
		if( blockCrawler.getFieldDensity(plantBlock, 3, material) > 0.1 ) return;
		
		if( Vegetation.debugging ) logOutput("Spreading plants..");

		int maxSpreadAmount = 2;
		
		if( Vegetation.debugging ) logOutput("Spreading Type of Plant: " + plantType.toString());
		
		Block emptyBlock = null;
		Material plantBlockMaterial = plantBlock.getRelative(BlockFace.DOWN).getType();
		//Get surrounding block and place new plant
		for( int I = 0; I < 150; I++ )
		{
			emptyBlock = blockCrawler.getRandomTopBlock(plantBlock.getLocation() , plantBlockMaterial, Material.AIR, 3);
			if( emptyBlock != null )
			{
				emptyBlock.getRelative(BlockFace.UP).setType(plantType);
				if( Vegetation.debugging ) logOutput("Planting at: " + emptyBlock.getX() + " " + emptyBlock.getY() + " " + emptyBlock.getZ());
				maxSpreadAmount--;
			}

			if( maxSpreadAmount <= 0 ) break;
		}
	}
	
	public boolean growSinglePlant(Block block, Material plantType)
	{
		block.getRelative(BlockFace.UP).setType(plantType);
		if( Vegetation.debugging ) logOutput("Planting at: " + block.getX() + " " + block.getY() + " " + block.getZ());
		return true;
	}
}
