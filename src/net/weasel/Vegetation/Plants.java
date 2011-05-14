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
		
		Block emptyBlock;
		Material plantBlockMaterial = plantBlock.getRelative(BlockFace.DOWN).getType();
		//Get surrounding block and place new plant
		for( int i = 0; i < 150; i++ )
		{
			emptyBlock = blockCrawler.getRandomTopBlock(plantBlock.getLocation() , plantBlockMaterial, Material.AIR, 3);
			if( emptyBlock != null )
			{
				emptyBlock.getRelative(BlockFace.UP).setType(plantType);
				if( growSinglePlant(emptyBlock, plantType) )
				{
					maxSpreadAmount--;
				}
				if( Vegetation.debugging ) logOutput("Planting at: " + emptyBlock.getX() + " " + emptyBlock.getY() + " " + emptyBlock.getZ());
				if( maxSpreadAmount <= 0 ) break;
			}

		}
	}
	
	public boolean growSinglePlant(Block block, Material plantType)
	{
		int lightLevel = block.getRelative(BlockFace.UP).getLightLevel();
		switch( plantType )
		{
		case YELLOW_FLOWER:
			if( lightLevel > 7 )
			{
				block.getRelative(BlockFace.UP).setType(plantType);
				return true;
			}
			break;
			
		case RED_ROSE:
			if( lightLevel > 7 )
			{
				block.getRelative(BlockFace.UP).setType(plantType);
				return true;
			}
			break;
			
		case RED_MUSHROOM:
			if( lightLevel < 2 )
			{
				block.getRelative(BlockFace.UP).setType(plantType);
				return true;
			}
			break;
			
		case BROWN_MUSHROOM:
			if( lightLevel < 2 )
			{
				block.getRelative(BlockFace.UP).setType(plantType);
				return true;
			}
			break;
		
		default:
			block.getRelative(BlockFace.UP).setType(plantType);
			return true;
		}
		return false;
	}
}
