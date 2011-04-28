package net.weasel.Vegetation;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

public final class Vines 
{
	public static void logOutput( String text ) { Vegetation.logOutput( text ); }
	public static boolean withinEnabledBiome( Biome biome ) { return Blocks.withinEnabledBiome( biome ); }
	public static Block getTopTreeBlock(World world, double X, double Z ) { return Blocks.getTopTreeBlock(world, X, Z ); }

	public static void growVines(Block Wood)
	{
		ArrayList<Block> LowerLeaves = getLowerLeafBlocks( getTreeTrunk(Wood) );
		
		if( !LowerLeaves.isEmpty() )
		{
			int R = Vegetation.generator.nextInt( LowerLeaves.size() );
			
			if( R <= LowerLeaves.size() )
			{
				Block Leaf = LowerLeaves.get( R );
				int Height = Vegetation.generator.nextInt(5);
				
				for( int I = 0; I < Height; I++ )
				{
					Leaf = Leaf.getRelative( BlockFace.DOWN );
					if( Leaf.getType() == Material.AIR && !Blocks.isSurroundedByBlockType1( Leaf, Material.SUGAR_CANE_BLOCK ) )
					{
						Leaf.setTypeIdAndData( 83, (byte)15, true );
					}
				}
			}
		}
	}
	/*public static void growVines( Player player, int count )
	{
		int R = 0;
		
		for( int X = 1; X <= count; X++ )
		{
			R = Vegetation.generator.nextInt(5);

			if( R < 1 ) R = 1;
			if( R > 5 ) R = 5;
			
			growVine( player, R );
		}
	}
	
	public static void growVine( Player player, int height )
	{
		Block leafBlock = null;
		Block foundBlock = null;
		
		leafBlock = getRandomLeafBlock( player );
		
		if( leafBlock != null )
		{
			leafBlock = leafBlock.getRelative(BlockFace.DOWN);
			
			if( leafBlock.getTypeId() == 0 )
			{
				if( leafBlock.getRelative(BlockFace.NORTH).getTypeId() != 83
				&& leafBlock.getRelative(BlockFace.NORTH_EAST).getTypeId() != 83 
				&& leafBlock.getRelative(BlockFace.EAST).getTypeId() != 83 
				&& leafBlock.getRelative(BlockFace.SOUTH_EAST).getTypeId() != 83 
				&& leafBlock.getRelative(BlockFace.SOUTH).getTypeId() != 83 
				&& leafBlock.getRelative(BlockFace.SOUTH_WEST).getTypeId() != 83 
				&& leafBlock.getRelative(BlockFace.WEST).getTypeId() != 83 
				&& leafBlock.getRelative(BlockFace.NORTH_WEST).getTypeId() != 83 
				)
				{
					if( foundBlock == null ) foundBlock = leafBlock;
				}
			}
		}
		
		if( foundBlock != null )
		{
			foundBlock.setTypeIdAndData( 83, (byte)15, true );
			
			if( height > 1 )
			{
				for( int H = height; H > 1; H-- )
				{
					foundBlock = foundBlock.getRelative(BlockFace.DOWN);
					if( foundBlock.getTypeId() == 0 ) 
					{
						foundBlock.setTypeIdAndData( 83, (byte)15, true );
					}
				}
			}
		}
	}

	public static Block getRandomLeafBlock( Player player )
    {
		ArrayList<Block> leafBlocks = getLowerLeafBlocks(player);
		
		if( leafBlocks != null )
		{
	    	if( leafBlocks.size() > 0 )
	    	{
	    		Integer R = Vegetation.generator.nextInt( leafBlocks.size() );
	
	    		return leafBlocks.get(R);
	    	}
	    	else
	    		return null;
		}
		else
			return null;
    }
	
	public static Block getRandomVineBlock( Player player )
    {
		ArrayList<Block> vineBlocks = getVineBlocks(player);
		
		if( vineBlocks != null )
		{
	    	if( vineBlocks.size() > 0 )
	    	{
	    		Integer R = Vegetation.generator.nextInt( vineBlocks.size() );
	
	    		return vineBlocks.get(R);
	    	}
	    	else
	    		return null;
		}
		else
			return null;
    }
	
	public static ArrayList<Block> getLowerLeafBlocks( Player player )
	{
		if( player == null ) return null;
		
		double pX = player.getLocation().getX();
		double pY = player.getLocation().getY();
		double pZ = player.getLocation().getZ();

		double minX = pX - Vegetation.growthRange;
		double maxX = pX + Vegetation.growthRange;
		double minY = pY - Vegetation.verticalRadius;
		double maxY = pY + Vegetation.verticalRadius;
		double minZ = pZ - Vegetation.growthRange;
		double maxZ = pZ + Vegetation.growthRange;
		
		double X, Y, Z;
		
		ArrayList<Block> blockList = new ArrayList<Block>();
		
		Block targetBlock = null;
		
		for( X = minX; X <= maxX; X++ )
		{
			for( Y = minY; Y <= maxY; Y++ )
			{
				for( Z = minZ; Z <= maxZ; Z++ )
				{
					targetBlock = player.getWorld().getBlockAt((int)X,(int)Y,(int)Z);
					
					if( targetBlock.getTypeId() == 18 )
					{
						if( targetBlock.getRelative(BlockFace.DOWN).getTypeId() == 0 )
						{
							blockList.add( targetBlock );
						}
					}
				}
			}
		}
		
		if( blockList.size() > 0 )
		{
			if( Vegetation.debugging ) logOutput( "Found " + blockList.size() + " eligible leaf blocks." );
			return blockList;
		}
		else
		{
			if( Vegetation.debugging )logOutput( "Found no leaf blocks." );
			return null;
		}
	}

	public static ArrayList<Block> getVineBlocks( Player player )
	{
		double pX = player.getLocation().getX();
		double pY = player.getLocation().getY();
		double pZ = player.getLocation().getZ();

		double minX = pX - Vegetation.growthRange;
		double maxX = pX + Vegetation.growthRange;
		double minY = pY - Vegetation.verticalRadius;
		double maxY = pY + Vegetation.verticalRadius;
		double minZ = pZ - Vegetation.growthRange;
		double maxZ = pZ + Vegetation.growthRange;
		
		double X, Y, Z;
		
		ArrayList<Block> blockList = new ArrayList<Block>();
		
		Block targetBlock = null;
		
		for( X = minX; X <= maxX; X++ )
		{
			for( Y = minY; Y <= maxY; Y++ )
			{
				for( Z = minZ; Z <= maxZ; Z++ )
				{
					targetBlock = player.getWorld().getBlockAt((int)X,(int)Y,(int)Z);
					
					if( targetBlock.getTypeId() == 18 )
					{
						if( targetBlock.getRelative(BlockFace.DOWN).getTypeId() == 83 )
						{
							logOutput( "Found vine block at " + targetBlock.getX() + "," + ( targetBlock.getY()-1 ) + "," + targetBlock.getZ() + "." );
							blockList.add( targetBlock.getRelative(BlockFace.DOWN) );
							logOutput( "This vine is " + getVineHeight(targetBlock.getRelative(BlockFace.DOWN)) + " units tall." );
						}
					}
				}
			}
		}
		
		if( blockList.size() > 0 )
		{
			if( Vegetation.debugging ) logOutput( "Found " + blockList.size() + " eligible vine blocks." );
			return blockList;
		}
		else
		{
			if( Vegetation.debugging )logOutput( "Found no vine blocks." );
			return null;
		}
		
	}

	public static void growVineAt( Player player, Block targetBlock, int height )
	{
		for( int X = 0; X < 20; X++ )
		{
			if( targetBlock.getTypeId() != 0 ) targetBlock = targetBlock.getRelative(BlockFace.DOWN);
		}

		if( targetBlock.getTypeId() != 0 ) return;
		
		targetBlock.setTypeIdAndData( 83, (byte)15, true );

		for( int H = height; H > 1; H-- )
		{
			targetBlock = targetBlock.getRelative(BlockFace.DOWN);
			if( targetBlock.getTypeId() == 0 ) 
			{
				targetBlock.setTypeIdAndData( 83, (byte)15, true );
			}
		}
	}
	
	public static int getVineHeight( Block topVineBlock )
	{
		int retVal = 1;
		Block targetBlock = topVineBlock;
		
		while( targetBlock.getTypeId() == 83 )
		{
			targetBlock = targetBlock.getRelative(BlockFace.DOWN);
			retVal++;
		}
		
		return retVal;
	}*/
	
	public static ArrayList<Block> getTreeTrunk(Block BaseBlock)
	{
		if( BaseBlock.getType() != Material.WOOD ) return null;
		
		ArrayList<Block> TreeTrunk = new ArrayList<Block>();
		Block CurrentBlock = BaseBlock;
		
		TreeTrunk.add( BaseBlock );
		
		if( BaseBlock.getRelative(BlockFace.DOWN).getType() == Material.WOOD )
		{
			while(true)
			{
				CurrentBlock = CurrentBlock.getRelative(BlockFace.DOWN);
				if( CurrentBlock.getType() == Material.WOOD )
				{
					TreeTrunk.add( CurrentBlock );
				}
				else break;
			}
		}
		if( BaseBlock.getRelative(BlockFace.UP).getType() == Material.WOOD )
		{
			while(true)
			{
				CurrentBlock = CurrentBlock.getRelative(BlockFace.UP);
				if( CurrentBlock.getType() == Material.WOOD )
				{
					TreeTrunk.add( CurrentBlock );
				}
				else break;
			}
		}
		
		return TreeTrunk.size() > 1 ? TreeTrunk : null;
	}
	
	public static ArrayList<Block> getLowerLeafBlocks(ArrayList<Block> TreeTrunk)
	{
		ArrayList<Block> LowerLeaves = new ArrayList<Block>();
    	double pX, pY, pZ;
    	int Range = 4;
    	double blockCountInRange = Math.pow(2*Range + 1, 2);
    	int count = 0;
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
	    				if( CurrentBlock.getRelative(BlockFace.DOWN).getType() == Material.AIR )
	    					LowerLeaves.add( CurrentBlock );
	    			}
	    			count++;
	    		}
	    	}
			
		}
		logOutput("Tree Leaves scanned field of " + blockCountInRange + " " + count + " was: " + LowerLeaves.size() );
		
		return !LowerLeaves.isEmpty() ? LowerLeaves : null;
	}
}
