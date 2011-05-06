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
	
	public ArrayList<Block> getLowerLeafBlocks(ArrayList<Block> TreeTrunk)
	{
		if( TreeTrunk == null ) return null;
		
		ArrayList<Block> LowerLeaves = new ArrayList<Block>();
    	double pX, pY, pZ;
    	int Range = 0;
    	
    	if( TreeTrunk.size() > 6 )
    	{
    		//big tree
    		Range = 5;
    		MaxVinesOnTree = 6;
    	}
    	else if( TreeTrunk.size() < 5 )
    	{
    		//tree is too small to grow vines (only one block between leaves and ground
    		return null;
    	}
    	else
    	{
    		//normal sized tree
    		Range = 2;
    		MaxVinesOnTree = 3;
    	}

    	//double blockCountInRange = Math.pow(2*Range + 1, 2);
    	int VineCount = 0;
    	//int count = 0;
    	Block CurrentBlock = null;
    	World W = TreeTrunk.get(0).getWorld();
    	
		for( Block B: TreeTrunk )
		{
			pX = B.getLocation().getX();
			pY = B.getLocation().getY();
			pZ = B.getLocation().getZ();
			
	       	for( double X = pX-Range; X <= pX+Range; X++ )
	    	{
	    		for( double Z = pZ-Range; Z <= pZ+Range; Z++ )
	    		{
	    			CurrentBlock = W.getBlockAt((int)X , (int)pY , (int)Z );
	    			if( CurrentBlock != null && CurrentBlock.getType() == Material.LEAVES )
	    			{
	    				if( CurrentBlock.getRelative(BlockFace.DOWN).getType() == Material.AIR 
	    						&& blocks.isSurroundedByBlockType1( CurrentBlock.getRelative(BlockFace.DOWN), Material.AIR) )
	    				{
	    					LowerLeaves.add( CurrentBlock );
	    				}
	    				else if( CurrentBlock.getRelative(BlockFace.DOWN).getType() == Material.SUGAR_CANE_BLOCK )
	    				{
	    					VineCount++;
	    					if( VineCount >= MaxVinesOnTree ) return null;
	    				}
	    			}
	    			//count++;
	    		}
	    	}
			
		}
		//logOutput("Tree Leaves scanned field of " + blockCountInRange + " " + count + " was: " + LowerLeaves.size() );
		
		return !LowerLeaves.isEmpty() ? LowerLeaves : null;
	}
}
