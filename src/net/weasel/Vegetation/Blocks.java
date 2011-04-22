package net.weasel.Vegetation;

import java.util.ArrayList;

import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.Location;
import org.bukkit.Material;

public class Blocks 
{
	public static void logOutput( String text )
	{
		Vegetation.logOutput( text );
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
	
	public static boolean WithinEnabledBiome( Biome biome )
    {	
		if( biome == Biome.FOREST && Vegetation.growForestBiome == true ) return true;
		else if( biome == Biome.RAINFOREST && Vegetation.growRainforestBiome == true ) return true;
		else if( biome == Biome.PLAINS && Vegetation.growPlainsBiome == true ) return true;
		else if( biome == Biome.ICE_DESERT && Vegetation.growIceDesertBiome == true ) return true;
		else if( biome == Biome.DESERT && Vegetation.growDesertBiome == true ) return true;
		else if( biome == Biome.SWAMPLAND && Vegetation.growSwamplandBiome == true ) return true;
		else if( biome == Biome.HELL && Vegetation.growHellBiome == true ) return true;
		else if( biome == Biome.TAIGA && Vegetation.growTaigaBiome == true ) return true;
		else if( biome == Biome.TUNDRA && Vegetation.growTundraBiome == true ) return true;
		else if( biome == Biome.SAVANNA && Vegetation.growSavannahBiome == true ) return true;
		else if( biome == Biome.SHRUBLAND && Vegetation.growShrublandBiome == true ) return true;
		else if( biome == Biome.SEASONAL_FOREST && Vegetation.growSeasonalForestBiome == true ) return true;
		else return false;
    }
    
    public static ArrayList<Block> getAdjacentBlocks( Block targetBlock )
    { 	
    	ArrayList<Block> blockList = new ArrayList<Block>();
    	
    	int tX = targetBlock.getX();
    	int tY = targetBlock.getY();
    	int tZ = targetBlock.getZ();
    	
    	int minX = tX - 1, maxX = tX + 1;
    	int minY = tY - 1, maxY = tY + 1;
    	int minZ = tZ - 1, maxZ = tZ + 1;
    	
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
    
    public static Block getTopBlock( Location BaseBlock, double X, double Z )
    {
    	Block retVal = null, current = null;
    	World W = BaseBlock.getWorld();
    	int MaxY = 127;
    	double Y = BaseBlock.getY();
    	
    	current = W.getBlockAt((int)X, (int)Y, (int)Z);
    	if ( current.getType() == Material.AIR )
    	{
    		//Assume we're above the ground and decrease Y
    		for( int I = 1; I < Vegetation.verticalRadius; I++ )
    		{
    			if( ((int)Y - I) < 0 )
    			{
    				break;
    			}
    			current = W.getBlockAt((int)X, (int)Y - I,(int)Z);
    			if( current.getType() != Material.AIR )
    			{
    				retVal = current;
    				break;
    			}
    		}
    	}
    	else if ( current.getType() != Material.AIR )
    	{
    		//Assume we're already on top, thus check for air
    		if ( W.getBlockAt((int)X, (int)Y + 1, (int)Z).getType() == Material.AIR )
    		{
    			retVal = current;
    		}
    		else
    		{
    			//Assume we're Underground and increase Y
    			for( int I = 1; I < Vegetation.verticalRadius; I++ )
    			{
    				if( ((int)Y + I) >= MaxY )
    				{
    					break;
    				}
    				current = W.getBlockAt((int)X, (int)Y + I,(int)Z);
    				if( current.getType() == Material.AIR )
    				{
    					retVal = current.getRelative(BlockFace.DOWN);
    					break;
    				}
    			}
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
    
	public static Block getRandomBlock( Location BaseBlock )
    {
    	Block retVal = null;
    	int Range = Vegetation.growthRange;
    	int MaxCycle = 50;
    	
    	double pX = BaseBlock.getX();
    	double pZ = BaseBlock.getZ();

    	Block currentBlock;
    	double tX, tZ;

    	for( int I = 0; I < MaxCycle; I++ )
    	{
    		tX = pX + Vegetation.generator.nextInt(Range) - Vegetation.generator.nextInt(Range);
    		tZ = pZ + Vegetation.generator.nextInt(Range) - Vegetation.generator.nextInt(Range);

    		currentBlock = getTopBlock( BaseBlock, tX, tZ );
    		if( currentBlock != null && WithinEnabledBiome( currentBlock.getBiome() ) )
    		{
    			retVal = currentBlock;
    			if( Vegetation.debugging ) logOutput( "Found random block of material: " + currentBlock.getType().toString() + " " + retVal.getX() + "," + retVal.getY() + "," + retVal.getZ() );
    			break;
    		}
    	}
    	return retVal;
    }
    
	public static Block getRandomBlock( Location BaseBlock, Material material )
    {
    	Block retVal = null;
    	int Range = Vegetation.growthRange;
    	int MaxCycle = 150;
    	
    	double pX = BaseBlock.getX();
    	double pZ = BaseBlock.getZ();

    	Block currentBlock;
    	double tX, tZ;

    	for( int I = 0; I < MaxCycle; I++ )
    	{
       		tX = pX + GetRandomRangeValue( I, Range );
    		tZ = pZ + GetRandomRangeValue( I, Range );

    		currentBlock = getTopBlock( BaseBlock, tX, tZ );
    		if( ( currentBlock != null ) && ( currentBlock.getType() == material ) && ( WithinEnabledBiome( currentBlock.getBiome() ) ) )
    		{
    			retVal = currentBlock;
    			if( Vegetation.debugging ) logOutput( "Found random block of material: "+ material.toString() + " " + retVal.getX() + "," + retVal.getY() + "," + retVal.getZ() );
    			break;
    		}
    	}
    	return retVal;
    }
	
	public static Block getRandomBlock( Location BaseBlock, Material material, int Range )
    {
    	Block retVal = null;
    	int MaxCycle = 150;
    	
    	double pX = BaseBlock.getX();
    	double pZ = BaseBlock.getZ();

    	Block currentBlock;
    	double tX, tZ;

    	for( int I = 0; I < MaxCycle; I++ )
    	{
       		tX = pX + GetRandomRangeValue( I, Range );
    		tZ = pZ + GetRandomRangeValue( I, Range );

    		currentBlock = getTopBlock( BaseBlock, tX, tZ );
    		if( ( currentBlock != null ) && ( currentBlock.getType() == material ) && ( WithinEnabledBiome( currentBlock.getBiome() ) ) )
    		{
    			retVal = currentBlock;
    			if( Vegetation.debugging ) logOutput( "Found random block of material: "+ material.toString() + " " + retVal.getX() + "," + retVal.getY() + "," + retVal.getZ() );
    			break;
    		}
    	}
    	return retVal;
    }
	
	public static Block getRandomBlock( Location BaseBlock, Material material, int MinRange, int MaxRange )
    {
    	Block retVal = null;
    	int MaxCycle = 150;
    	
    	double pX = BaseBlock.getX();
    	double pZ = BaseBlock.getZ();

    	Block currentBlock;
    	double tX, tZ;

    	for( int I = 0; I < MaxCycle; I++ )
    	{
       		tX = pX + GetRandomRangeValue( I, MaxRange );
    		tZ = pZ + GetRandomRangeValue( I, MaxRange );

    		//Cacti destroy each other if they are too close togehter
    		if( (tX - pX >= MinRange ) && (tZ - pZ) >= MinRange )
    		{
    			currentBlock = getTopBlock( BaseBlock, tX, tZ );
    			if( ( currentBlock != null ) && ( currentBlock.getType() == material ) && ( WithinEnabledBiome( currentBlock.getBiome() ) ) )
    			{
    				retVal = currentBlock;
    				if( Vegetation.debugging ) logOutput( "Found random block of material: "+ material.toString() + " " + retVal.getX() + "," + retVal.getY() + "," + retVal.getZ() );
    				break;
    			}
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
	            		if( WithinEnabledBiome(currentBlock.getBiome()) == true )
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
    
    public static boolean IsAdjacentBlockofType( Block B, Material M )
    {
    	if( B.getRelative(BlockFace.NORTH).getType() == M )
    	{
    		return true;
    	}
    	else if( B.getRelative(BlockFace.EAST).getType() == M )
    	{
    		return true;
    	}
    	else if( B.getRelative(BlockFace.SOUTH).getType() == M )
    	{
    		return true;
    	}
    	else if( B.getRelative(BlockFace.WEST).getType() == M )
    	{
    		return true;
    	}
    	else
    	{
    		return false;
    	}
    }
    
    public static int GetRandomRangeValue(int V, int MaxRange)
    {
    	if( V%3 == 1 )
    	{
    		return ( Vegetation.generator.nextInt(MaxRange) - Vegetation.generator.nextInt(MaxRange) ) / 2;
    	}
    	else if( V%3 == 2 )
    	{
    		return ( Vegetation.generator.nextInt(MaxRange) - Vegetation.generator.nextInt(MaxRange) ) / 3;
    	}
    	else
    	{
    		return Vegetation.generator.nextInt(MaxRange) - Vegetation.generator.nextInt(MaxRange);
    	}
    }
}
