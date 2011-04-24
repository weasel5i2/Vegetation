package net.weasel.Vegetation;

import org.bukkit.Material;
import org.bukkit.block.Block;

public final class Moss
{
	public static void logOutput( String text ) { Vegetation.logOutput( text ); }
    
    public static void growMoss(Block B)
    {
    	int MaxCycle = 150;
    	int MaxSpreadAmount = 3;
    	Block CurrentBlock = null;
    	
    	if( Vegetation.debugging ) logOutput( "Spreading moss.." );
    	
		for(int I = 0; I < MaxCycle; I++ )
		{
			if( MaxCycle%2 == 0 )
			{
				CurrentBlock = Blocks.getRandomBlock( B.getLocation() ,Material.COBBLESTONE, Material.STATIONARY_WATER );
				if( CurrentBlock != null )
				{
					CurrentBlock.setType(Material.MOSSY_COBBLESTONE);
					MaxSpreadAmount--;
				}
			}
			else
			{
				CurrentBlock = Blocks.getRandomBlock( B.getLocation() , Material.COBBLESTONE, Material.AIR, 5 );
				if( CurrentBlock != null )
				{
					if( Blocks.isAdjacentBlockofType2( CurrentBlock  , Material.STATIONARY_WATER )
							|| Blocks.isAdjacentBlockofType2( CurrentBlock  , Material.WATER ) )
					{
						CurrentBlock.setType(Material.MOSSY_COBBLESTONE);
						MaxSpreadAmount--;
					}
				}
			}
			
			if( MaxSpreadAmount <= 0 ) break;
		}
    }
    
    public static boolean growSingleMoss(Block B)
    {
    	B.setType(Material.MOSSY_COBBLESTONE);
    	return true;
    }
}
