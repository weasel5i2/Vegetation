package net.weasel.Vegetation;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

public class Vines 
{
	public void logOutput( String text ) { Vegetation.logOutput( text ); }

	private int MaxVinesOnTree = 5;
	private BlockCrawler blocks;
	
	public Vines(BlockCrawler b)
	{
		blocks = b;
	}
	
	public void growVines(Block Log)
	{
		ArrayList<Block> LowerLeaves = getLowerLeafBlocks( getTreeTrunk(Log) );
		
		if( LowerLeaves != null )
		{
			int R = Vegetation.generator.nextInt( LowerLeaves.size() );
			
			if( R <= LowerLeaves.size() )
			{
				if ( Vegetation.debugging ) logOutput("Growing Vines...");
				Block Leaf = LowerLeaves.get( R );
				int Height = Vegetation.generator.nextInt(7) + 2;
				
				for( int I = 0; I < Height; I++ )
				{
					Leaf = Leaf.getRelative( BlockFace.DOWN );
					if( Leaf.getType() == Material.AIR )
					{
						if( Leaf.getRelative(BlockFace.DOWN).getType() == Material.AIR && blocks.isSurroundedByBlockType1( Leaf, Material.AIR ) )
							Leaf.setTypeIdAndData( 83, (byte)15, true );
					}
					else break;
				}
			}
		}
	}
	
	public ArrayList<Block> getTreeTrunk(Block BaseBlock)
	{
		if( BaseBlock.getType() != Material.LOG ) return null;
		
		ArrayList<Block> TreeTrunk = new ArrayList<Block>();
		TreeTrunk.add(BaseBlock);
		
		Block CurrentBlock = BaseBlock;
		
		if( BaseBlock.getRelative(BlockFace.DOWN).getType() == Material.LOG )
		{
			while(true)
			{
				CurrentBlock = CurrentBlock.getRelative(BlockFace.DOWN);
				if( CurrentBlock.getType() == Material.LOG )
				{
					TreeTrunk.add( CurrentBlock );
				}
				else if( CurrentBlock.getType() == Material.AIR )
				{
					//treetrunk is floating
					return null;
				}
				else break;
			}
		}
		
		CurrentBlock = BaseBlock;
		
		if( BaseBlock.getRelative(BlockFace.UP).getType() == Material.LOG )
		{
			while(true)
			{
				CurrentBlock = CurrentBlock.getRelative(BlockFace.UP);
				if( CurrentBlock.getType() == Material.LOG )
				{
					TreeTrunk.add( CurrentBlock );
				}
				else break;
			}
		}
		
		return TreeTrunk.size() > 1 ? TreeTrunk : null;
	}
	
	public ArrayList<Block> getLowerLeafBlocks(ArrayList<Block> treeTrunk)
	{
		if( treeTrunk == null ) return null;
		
		ArrayList<Block> lowerLeaves = new ArrayList<Block>();
    	int pX, pY, pZ;
    	int range = 0;
    	
    	if( treeTrunk.size() > 6 )
    	{
    		//big tree
    		range = 5;
    		MaxVinesOnTree = 6;
    	}
    	else if( treeTrunk.size() < 5 )
    	{
    		//tree is too small to grow vines (only one block between leaves and ground
    		return null;
    	}
    	else
    	{
    		//normal sized tree
    		range = 2;
    		MaxVinesOnTree = 3;
    	}

    	//double blockCountInRange = Math.pow(2*Range + 1, 2);
    	int vineCount = 0;
    	//int count = 0;
    	Block currentBlock;
    	Block first = treeTrunk.get(0);
    	World W = first.getWorld();
    	
		pX = (int)first.getLocation().getX();
		pZ = (int)first.getLocation().getZ();
    	
		for( Block B: treeTrunk )
		{
			pY = (int)B.getLocation().getY();
			
	       	for( int x = pX-range; x <= pX+range; x++ )
	    	{
	    		for( int z = pZ-range; z <= pZ+range; z++ )
	    		{
	    			if( W.getBlockTypeIdAt(x, pY, z) == Material.LEAVES.getId() )
	    			{
	    				currentBlock = W.getBlockAt(x , pY , z );
	    				if( W.getBlockTypeIdAt(x, pY - 1, z) == Material.AIR.getId() 
	    						&& blocks.isSurroundedByBlockType1( currentBlock.getRelative(BlockFace.DOWN), Material.AIR) )
	    				{
	    					lowerLeaves.add(currentBlock);
	    				}
	    				else if( W.getBlockTypeIdAt(x, pY - 1, z) == Material.SUGAR_CANE_BLOCK.getId() )
	    				{
	    					vineCount++;
	    					if( vineCount >= MaxVinesOnTree ) return null;
	    				}
	    			}
	    			//count++;
	    		}
	    	}
			
		}
		//logOutput("Tree Leaves scanned field of " + blockCountInRange + " " + count + " was: " + LowerLeaves.size() );
		
		return !lowerLeaves.isEmpty() ? lowerLeaves : null;
	}
}
