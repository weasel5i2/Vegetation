package net.weasel.Vegetation;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

public class TallGrass {
	
	public void logOutput(String text) { Vegetation.logOutput(text); }
	
	private BlockCrawler blockCrawler;
	
	public TallGrass(BlockCrawler b)
	{
		blockCrawler = b;
	}
	
	public void growTallGrass(Block block)
	{
		// 31 - Tall Grass Block
		// 0 = dead bush, 1 = tall grass, 2 = green bush
		switch( block.getBiome() )
		{
		case FOREST:
			block.getRelative(BlockFace.UP).setTypeIdAndData(31, (byte) 1, false);
			break;
			
		case RAINFOREST:
			block.getRelative(BlockFace.UP).setTypeIdAndData(31, (byte) 2, false);
			break;
			
		case PLAINS:
			block.getRelative(BlockFace.UP).setTypeIdAndData(31, (byte) 1, false);
			break;
			
		case SWAMPLAND:
			break;
			
		case TAIGA:
			break;
			
		case TUNDRA:
			break;
			
		case SAVANNA:
			//nothing
			break;
		
		case SHRUBLAND:
			block.getRelative(BlockFace.UP).setTypeIdAndData(31, (byte) 1, false);
			break;
			
		case SEASONAL_FOREST:
			block.getRelative(BlockFace.UP).setTypeIdAndData(31, (byte) 0, false);
			break;
			
		case DESERT:
			block.getRelative(BlockFace.UP).setTypeIdAndData(31, (byte) 1, false);
			break;
			
		default:
			break;
		}
	}
	
	public void spreadTallGrass(Block bushBlock)
	{
		if( blockCrawler.getFieldDensity(bushBlock, 3, material) > 0.05 ) return;
		
		Block emptyBlock = null;
		Material plantBlockMaterial = bushBlock.getRelative(BlockFace.DOWN).getType();
		//Get surrounding block and place new plant
		for( int i = 0; i < 150; i++ )
		{
			emptyBlock = blockCrawler.getRandomTopBlock(bushBlock.getLocation() , plantBlockMaterial, Material.AIR, 3);
			if( emptyBlock != null )
			{
				//emptyBlock.getRelative(BlockFace.UP).setType(plantType);
				if( growSinglePlant(emptyBlock, plantType) )
				{
					maxSpreadAmount--;
				}
				if( Vegetation.debugging ) logOutput("Planting at: " + emptyBlock.getX() + " " + emptyBlock.getY() + " " + emptyBlock.getZ());
				if( maxSpreadAmount <= 0 ) break;
			}

		}
	}

}
