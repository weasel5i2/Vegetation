package net.weasel.Vegetation;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

public class Canes {

	public void logOutput( String text ) { Vegetation.logOutput( text ); }
	
	private Blocks blocks;
	
	public Canes(Blocks b)
	{
		blocks = b;
	}
	
	public void growCanes( Block B )
	{
		if( Vegetation.debugging ) logOutput( "Spreading plants.." );

		int MaxSpreadAmount = 2;

		if( Vegetation.debugging ) logOutput( "Spreading Type of Plant: Sugar Crane " );

		Block PlantBlock = null;
		//Get surrounding block and place new plant
		for( int I = 0; I < 150; I++ )
		{
			PlantBlock = blocks.getRandomTopBlock( B.getLocation() , Material.GRASS, Material.AIR, 5);
			if( PlantBlock != null )
			{
				//sugar cranes can only grow near water :O
				if( blocks.isAdjacentBlockofType1(PlantBlock, Material.STATIONARY_WATER)
						|| blocks.isAdjacentBlockofType1(PlantBlock, Material.WATER) )
				{
					PlantBlock.getRelative(BlockFace.UP).setType(Material.SUGAR_CANE_BLOCK);
					if( Vegetation.debugging ) logOutput( "Planting at: " + PlantBlock.getX() + " " + PlantBlock.getY() + " " + PlantBlock.getZ() );
					MaxSpreadAmount--;
				}
			}

			if( MaxSpreadAmount <= 0 ) break;
		}
	}
	
	public boolean growSingleCane(Block B)
	{
		if( blocks.isAdjacentBlockofType1(B, Material.STATIONARY_WATER)
				|| blocks.isAdjacentBlockofType1(B, Material.WATER) )
		{
			B.getRelative(BlockFace.UP).setType(Material.SUGAR_CANE_BLOCK);
			if( Vegetation.debugging ) logOutput( "Planting at: " + B.getX() + " " + B.getY() + " " + B.getZ() );
			return true;
		}
		return false;
	}
}
