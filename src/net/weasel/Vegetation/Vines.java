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
	
	public void growVines(Block log)
	{
		ArrayList<Block> lowerLeaves = getLowerLeafBlocks(getTreeTrunk(log));
		
		if( lowerLeaves != null )
		{
			int r = Vegetation.generator.nextInt(lowerLeaves.size());
			
			if( r <= lowerLeaves.size() )
			{
				if ( Vegetation.debugging ) logOutput("Growing Vines...");
				Block leaf = lowerLeaves.get(r);
				int height = Vegetation.generator.nextInt(7) + 2;
				
				for( int I = 0; I < height; I++ )
				{
					leaf = leaf.getRelative( BlockFace.DOWN );
					if( leaf.getType() == Material.AIR )
					{
						if( leaf.getRelative(BlockFace.DOWN).getType() == Material.AIR && blocks.isSurroundedByBlockType1(leaf, Material.AIR) )
							leaf.setTypeIdAndData(83, (byte)15, true);
					}
					else break;
				}
			}
		}
	}
	
	public ArrayList<Block> getTreeTrunk(Block baseBlock)
	{
		if(baseBlock.getData() != 0) return null;
		
		ArrayList<Block> treeTrunk = new ArrayList<Block>();
		treeTrunk.add(baseBlock);
		
		Block currentBlock = baseBlock;
		
		if( baseBlock.getRelative(BlockFace.DOWN).getType() == Material.LOG )
		{
			while(true)
			{
				currentBlock = currentBlock.getRelative(BlockFace.DOWN);
				if( currentBlock.getType() == Material.LOG )
				{
					treeTrunk.add( currentBlock );
				}
				else if( currentBlock.getType() == Material.AIR )
				{
					//treetrunk is floating
					return null;
				}
				else break;
			}
		}
		
		currentBlock = baseBlock;
		
		if( baseBlock.getRelative(BlockFace.UP).getType() == Material.LOG )
		{
			while(true)
			{
				currentBlock = currentBlock.getRelative(BlockFace.UP);
				if( currentBlock.getType() == Material.LOG )
				{
					treeTrunk.add( currentBlock );
				}
				else break;
			}
		}
		
		return treeTrunk.size() > 1 ? treeTrunk : null;
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
