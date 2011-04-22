package net.weasel.Vegetation;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

public class Cacti 
{
	public static boolean withinEnabledBiome( Block whichBlock ) { return( Blocks.withinEnabledBiome( whichBlock ) ); }
	public static void logOutput( String text ) { Vegetation.logOutput( text ); }
	public static Block getTopBlock(Location BaseBlock, double X, double Z ) { return Blocks.getTopBlock(BaseBlock, X, Z ); }
	
	public static void growCacti(Block B)
	{
		if( Vegetation.debugging ) logOutput( "Spreading cacti.." );
		
		int MaxSpreadAmount = 1;

		if( Vegetation.debugging ) logOutput( "Found cactus block: " + B.getTypeId() + " plant at " + B.getX() + "," + B.getY() + "," + B.getZ() + "." );

		Block PlantBlock = null;
		//Get surrounding block and place new plant
		for( int I = 0; I < 150; I++ )
		{
			PlantBlock = Blocks.getRandomBlock( B.getLocation() , Material.SAND, 5, 15);
			if( PlantBlock != null )
			{
				PlantBlock.getRelative(BlockFace.UP).setType(Material.CACTUS);
				if( Vegetation.debugging ) logOutput( "Planting at: " + PlantBlock.getX() + " " + PlantBlock.getY() + " " + PlantBlock.getZ() );
				MaxSpreadAmount--;
			}

			if( MaxSpreadAmount <= 0 ) break;
		}
	}
}
