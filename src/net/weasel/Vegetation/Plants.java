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
	
	public void growPlant(Block block, Material plantType)
	{
		Material[] M = { Material.YELLOW_FLOWER, Material.RED_ROSE, Material.BROWN_MUSHROOM, Material.RED_MUSHROOM, Material.PUMPKIN };
		if( blockCrawler.getFieldDensity(block, 3, M) > 0.1 ) return;
		
		if( Vegetation.debugging ) logOutput("Spreading plants..");

		int MaxSpreadAmount = 2;
		
		if( Vegetation.debugging ) logOutput("Spreading Type of Plant: " + plantType.toString());
		
		Block PlantBlock = null;
		//Get surrounding block and place new plant
		for( int I = 0; I < 150; I++ )
		{
			PlantBlock = blockCrawler.getRandomTopBlock(block.getLocation() , Material.GRASS, Material.AIR, 3);
			if( PlantBlock != null )
			{
				PlantBlock.getRelative(BlockFace.UP).setType(plantType);
				if( Vegetation.debugging ) logOutput("Planting at: " + PlantBlock.getX() + " " + PlantBlock.getY() + " " + PlantBlock.getZ());
				MaxSpreadAmount--;
			}

			if( MaxSpreadAmount <= 0 ) break;
		}
	}
	
	public boolean growSinglePlant(Block block, Material plantType)
	{
		block.getRelative(BlockFace.UP).setType(plantType);
		if( Vegetation.debugging ) logOutput("Planting at: " + block.getX() + " " + block.getY() + " " + block.getZ());
		return true;
	}
}
