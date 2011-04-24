package net.weasel.Vegetation;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

public class Cacti 
{
	public static void logOutput( String text ) { Vegetation.logOutput( text ); }
	
	public static void spreadCacti(Block B)
	{
		if( Vegetation.debugging ) logOutput( "Spreading cacti.." );
		
		int MaxSpreadAmount = 1;

		Block PlantBlock = null;
		//Get surrounding block and place new plant
		for( int I = 0; I < 150; I++ )
		{
			PlantBlock = Blocks.getRandomBlock( B.getLocation() , Material.SAND, Material.AIR, 5, 15);
			if( PlantBlock != null && Blocks.isSurroundedByBlockType( PlantBlock, Material.AIR ) )
			{
				PlantBlock.getRelative(BlockFace.UP).setType(Material.CACTUS);
				if( Vegetation.debugging ) logOutput( "Planting at: " + PlantBlock.getX() + " " + PlantBlock.getY() + " " + PlantBlock.getZ() );
				MaxSpreadAmount--;
			}

			if( MaxSpreadAmount <= 0 ) break;
		}
	}
}
