package net.weasel.Vegetation;

import org.bukkit.Material;
import org.bukkit.block.Block;

public class Moss
{
	public void logOutput( String text ) { Vegetation.logOutput( text ); }
	
	private Blocks blocks;
	
	public Moss(Blocks b)
	{
		blocks = b;
	}
    
    public void growMoss(Block B)
    {
    	int MaxCycle = 150;
    	int MaxSpreadAmount = 5;
    	Block CurrentBlock = null;
    	
    	if( Vegetation.debugging ) logOutput( "Spreading moss.." );
    	
		for(int I = 0; I < MaxCycle; I++ )
		{
			if( MaxCycle%2 == 0 )
			{
				CurrentBlock = blocks.getRandomTopBlock( B.getLocation() ,Material.COBBLESTONE, Material.STATIONARY_WATER, 25 );
				if( CurrentBlock != null )
				{
					CurrentBlock.setType(Material.MOSSY_COBBLESTONE);
					if( Vegetation.debugging ) logOutput( "Planting at: " + B.getX() + " " + B.getY() + " " + B.getZ() );
					MaxSpreadAmount--;
				}
			}
			else
			{
				CurrentBlock = blocks.getRandomTopBlock( B.getLocation() , Material.COBBLESTONE, Material.AIR, 25 );
				if( CurrentBlock != null )
				{
					if( blocks.isAdjacentBlockofType2( CurrentBlock  , Material.STATIONARY_WATER )
							|| blocks.isAdjacentBlockofType2( CurrentBlock  , Material.WATER ) )
					{
						CurrentBlock.setType(Material.MOSSY_COBBLESTONE);
						if( Vegetation.debugging ) logOutput( "Planting at: " + B.getX() + " " + B.getY() + " " + B.getZ() );
						MaxSpreadAmount--;
					}
				}
			}
			
			if( MaxSpreadAmount <= 0 ) break;
		}
    }
    
    public boolean growSingleMoss(Block B)
    {
    	B.setType(Material.MOSSY_COBBLESTONE);
    	if( Vegetation.debugging ) logOutput( "Planting at: " + B.getX() + " " + B.getY() + " " + B.getZ() );
    	return true;
    }
}
