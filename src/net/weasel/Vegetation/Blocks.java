package net.weasel.Vegetation;

import java.util.ArrayList;

import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

public class Blocks 
{
	public static void logOutput( String text )
	{
		Vegetation.logOutput( text );
	}

	public static boolean isEnabledPlant( Block whichBlock )
	{
		boolean retVal = false;
		
		if( whichBlock.getTypeId() == 37 && Vegetation.enableFlowers == true ) retVal = true;
		if( whichBlock.getTypeId() == 38 && Vegetation.enableFlowers == true ) retVal = true;
		if( whichBlock.getTypeId() == 39 && Vegetation.enableFungi == true ) retVal = true;
 	    if( whichBlock.getTypeId() == 40 && Vegetation.enableFungi == true ) retVal = true;
 	    if( whichBlock.getTypeId() == 86 && Vegetation.enablePumpkins == true ) retVal = true;
 	    if( whichBlock.getTypeId() == 81 && Vegetation.enableCacti == true ) retVal = true;
 	    if( whichBlock.getTypeId() == 83 && Vegetation.enableCanes == true ) retVal = true;
 	    
 	    return( retVal );
	}
	
	public static boolean isLilyPad( Block whichBlock )
	{
		boolean retVal = false;
		
		if( whichBlock.getTypeId() == 37 && Vegetation.enableLilyPads == true 
		|| whichBlock.getTypeId() == 38 && Vegetation.enableLilyPads == true )
		{
			if( whichBlock.getRelative(BlockFace.DOWN).getTypeId() == 9 ) retVal = true;
		}
 	    
 	    return( retVal );
	}
	
	public static boolean withinEnabledBiome( Block whichBlock )
    {
    	boolean retVal = false;
    	Biome biome = whichBlock.getBiome();
    	
		if( biome == Biome.FOREST && Vegetation.growForestBiome == true ) retVal = true;
		if( biome == Biome.RAINFOREST && Vegetation.growRainforestBiome == true ) retVal = true;
	    if( biome == Biome.PLAINS && Vegetation.growPlainsBiome == true ) retVal = true;
	    if( biome == Biome.ICE_DESERT && Vegetation.growIceDesertBiome == true ) retVal = true;
	    if( biome == Biome.DESERT && Vegetation.growDesertBiome == true ) retVal = true;
	    if( biome == Biome.SWAMPLAND && Vegetation.growSwamplandBiome == true ) retVal = true;
	    if( biome == Biome.HELL && Vegetation.growHellBiome == true ) retVal = true;
	    if( biome == Biome.TAIGA && Vegetation.growTaigaBiome == true ) retVal = true;
	    if( biome == Biome.TUNDRA && Vegetation.growTundraBiome == true ) retVal = true;
	    if( biome == Biome.SAVANNA && Vegetation.growSavannahBiome == true ) retVal = true;
	    if( biome == Biome.SHRUBLAND && Vegetation.growShrublandBiome == true ) retVal = true;
	    if( biome == Biome.SEASONAL_FOREST && Vegetation.growSeasonalForestBiome == true ) retVal = true;

	    return retVal;
    }

    public static ArrayList<Block> getAdjacentBlocks( Block targetBlock )
    { 	
    	ArrayList<Block> blockList = new ArrayList<Block>();
    	
    	Integer tX = targetBlock.getX();
    	Integer tY = targetBlock.getY();
    	Integer tZ = targetBlock.getZ();
    	
    	Integer minX = tX - 1, maxX = tX + 1;
    	Integer minY = tY - 1, maxY = tY + 1;
    	Integer minZ = tZ - 1, maxZ = tZ + 1;
    	
    	for( int X = minX; X <= maxX; X++ )
    	{
    		for( int Y = minY; Y <= maxY; Y++ )
    		{
    			for( int Z = minZ; Z <= maxZ; Z++ )
    			{
    				if( X != tX && Y != tY && Z != tZ )
    					blockList.add( targetBlock.getWorld().getBlockAt(X,Y,Z) );
    			}
    		}
    	}
    	
    	return blockList;
    }

    public static Block getTopBlock( World world, double X, double Z, int[] ignoreBlocks )
    {
    	Block retVal = null;
    	double Y = 127;
    	boolean exitLoop = false;
    	Block currentBlock = null;
    	
    	while( Y > 0 && exitLoop == false )
    	{
    		try
    		{
    			currentBlock = world.getBlockAt((int)X,(int)Y,(int)Z);
    		}
    		catch( Exception e )
    		{
    			retVal = null;
    			exitLoop = true;
    			break;
    		}
    		
    		if( currentBlock.getTypeId() != 0 
    		&& currentBlock.getTypeId() != 17 
    		&& currentBlock.getTypeId() != 18 
    		&& currentBlock.getTypeId() != 78 )
    		{
    			retVal = currentBlock;
    			exitLoop = true;
    		}
    		else
    		{
    			Y--;
    		}
    	}
    	
    	return retVal;
    }

    public static Block getTopTreeBlock( World world, double X, double Z )
    {
    	Block retVal = null;
    	double Y = 127;
    	boolean exitLoop = false;
    	Block currentBlock = null;
    	
    	while( Y > 0 && exitLoop == false )
    	{
    		try
    		{
    			currentBlock = world.getBlockAt((int)X,(int)Y,(int)Z);
    		}
    		catch( Exception e )
    		{
    			retVal = null;
    			exitLoop = true;
    			break;
    		}
    		
    		if( currentBlock.getTypeId() == 18 )
    		{
    			retVal = currentBlock;
    			exitLoop = true;
    		}
    		else if( currentBlock.getTypeId() == 0 )
    		{
    			Y--;
    		}
    		else
    		{
    			Y = 0;
    		}
    	}
    	
    	return retVal;
    }

    public static ArrayList<Block> getBlockRange(World world, Integer X, Integer Y, Integer Z, Integer Xsize, Integer Ysize )
    {
    	Block currentBlock = null;
    	ArrayList<Block> range = new ArrayList<Block>();
    	
    	double minX = X - (Xsize / 2);
    	double minY = Y - (Ysize / 2);
    	double minZ = Z - (Xsize / 2);
    	double maxX = X + (Xsize / 2);
    	double maxY = Y + (Ysize / 2);
    	double maxZ = Z + (Xsize / 2);
    	
    	int chkX = (int)minX;
    	int chkY = (int)minY;
    	int chkZ = (int)minZ;
    	
    	while( chkX <= maxX )
    	{
    		while( chkY <= maxY )
    		{
    	    	while( chkZ <= maxZ )
    	    	{
    				currentBlock = world.getBlockAt(chkX,chkY,chkZ);

	            	if( currentBlock != null )
	            	{
	            		if( withinEnabledBiome(currentBlock) == true )
		    			{
	            			if( currentBlock.getTypeId() != 0 ) 
	            			{
	            				range.add(currentBlock);
	            			}
		    			}
	            	}
	            	
	            	chkZ++;
    	    	}
    	    	chkZ = (int)minZ;
    	    	chkY++;
    		}
    		chkY = (int)minY;
    		chkX++;
    	}

		return range;
    }

    public static Block getAdjacentBlockForPlant( Block targetBlock, Integer blockType )
    {
    	ArrayList<Block> validBlocks = new ArrayList<Block>();
    	Block retVal = null;
    	Block chkBlock = null;
    	World world = targetBlock.getWorld();
    	int X, Z;
    	
    	X = targetBlock.getX();
    	Z = targetBlock.getZ();
    	
    	int[] ignoreBlocks = { 17, 18, 0 };
    	
    	chkBlock = getTopBlock( world, X-1, Z-1, ignoreBlocks );
    	if( chkBlock.getTypeId() == 2 && chkBlock.getRelative(BlockFace.UP).getTypeId() == 0 ) validBlocks.add(chkBlock); 

    	chkBlock = getTopBlock( world, X-1, Z, ignoreBlocks );
    	if( chkBlock.getTypeId() == 2 && chkBlock.getRelative(BlockFace.UP).getTypeId() == 0 ) validBlocks.add(chkBlock); 

    	chkBlock = getTopBlock( world, X-1, Z+1, ignoreBlocks );
    	if( chkBlock.getTypeId() == 2 && chkBlock.getRelative(BlockFace.UP).getTypeId() == 0 ) validBlocks.add(chkBlock); 

    	chkBlock = getTopBlock( world, X, Z+1, ignoreBlocks );
    	if( chkBlock.getTypeId() == 2 && chkBlock.getRelative(BlockFace.UP).getTypeId() == 0 ) validBlocks.add(chkBlock); 

    	chkBlock = getTopBlock( world, X, Z-1, ignoreBlocks );
    	if( chkBlock.getTypeId() == 2 && chkBlock.getRelative(BlockFace.UP).getTypeId() == 0 ) validBlocks.add(chkBlock); 

    	chkBlock = getTopBlock( world, X+1, Z-1, ignoreBlocks );
    	if( chkBlock.getTypeId() == 2 && chkBlock.getRelative(BlockFace.UP).getTypeId() == 0 ) validBlocks.add(chkBlock); 

    	chkBlock = getTopBlock( world, X+1, Z, ignoreBlocks );
    	if( chkBlock.getTypeId() == 2 && chkBlock.getRelative(BlockFace.UP).getTypeId() == 0 ) validBlocks.add(chkBlock); 
    	
    	chkBlock = getTopBlock( world, X+1, Z+1, ignoreBlocks );
    	if( chkBlock.getTypeId() == 2 && chkBlock.getRelative(BlockFace.UP).getTypeId() == 0 ) validBlocks.add(chkBlock); 

    	if( validBlocks.size() > 0 )
    	{
    		int R = Vegetation.generator.nextInt( validBlocks.size() );
    		retVal = validBlocks.get(R).getRelative(BlockFace.UP);
    	}
    	else
    		retVal = null;
    	
    	return retVal;
    }
    
    public static Block getRandomAdjacentEmptyBlock(Block targetBlock, Integer blockType )
    {
    	Block retVal = null;
    	Block block = null;
    	ArrayList<Block> tBlocks = new ArrayList<Block>();
    	ArrayList<Block> vBlocks = new ArrayList<Block>();

    	tBlocks = Blocks.getAdjacentBlocks(targetBlock);
    	
    	for( int X = 0; X < tBlocks.size(); X++ )
    	{
    		block = tBlocks.get(X);
    		
    		if( block.getTypeId() == blockType && block.getRelative(BlockFace.UP).getTypeId() == 0 )
    		{
    			vBlocks.add( block );
    		}
    	}
    	
    	if( vBlocks.size() > 0 )
    	{
			Integer R = Vegetation.generator.nextInt(vBlocks.size());
			retVal = vBlocks.get(R);
    	}

    	return retVal;
    }
}
