package net.weasel.Vegetation;

import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.Location;
import org.bukkit.Material;

public class Blocks 
{
	public static void logOutput( String text ) { Vegetation.logOutput( text ); }
	
	public static final int MaxCycle = 150;
	
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
	
	public static boolean withinEnabledBiome( Biome biome )
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
    
    public static Block getTopBlock( Location BaseBlock, double X, double Z, Material Surface )
    {
    	Block retVal = null, current = null;
    	World W = BaseBlock.getWorld();
    	int MaxY = 127;
    	double Y = BaseBlock.getY();
    	
    	current = W.getBlockAt((int)X, (int)Y, (int)Z);
    	if ( current.getType() == Surface )
    	{
    		//Assume we're above the ground and decrease Y
    		for( int I = 1; I < Vegetation.verticalRadius; I++ )
    		{
    			if( ((int)Y - I) < 0 )
    			{
    				break;
    			}
    			current = W.getBlockAt((int)X, (int)Y - I,(int)Z);
    			//we need at least two blocks of air on top of a block
    			if( current.getType() != Surface && I <= 2 )
    			{
    				retVal = current;
    				break;
    			}
    		}
    	}
    	else if ( current.getType() != Surface )
    	{
    		//Assume we're already on top, thus check for air
    		if ( W.getBlockAt((int)X, (int)Y + 1, (int)Z).getType() == Surface )
    		{
    			//we need at least two blocks of air on top of a block
    			if ( W.getBlockAt((int)X, (int)Y + 2, (int)Z).getType() == Surface )
    			{
    				retVal = current;
    			}
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
    				if( current.getType() == Surface )
    				{
    					//we need at least two blocks of air on top of a block
    					if( current.getRelative(BlockFace.UP).getType() == Surface )
    					{
        					retVal = current.getRelative(BlockFace.DOWN);
        					break;
    					}
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
    
	public static Block getRandomBlock( Location BaseBlock, Material Surface )
    {
    	Block retVal = null;
    	int Range = Vegetation.growthRange;
    	
    	double pX = BaseBlock.getX();
    	double pZ = BaseBlock.getZ();

    	Block currentBlock;
    	double tX, tZ;

    	for( int I = 0; I < MaxCycle; I++ )
    	{
    		tX = pX + Vegetation.generator.nextInt(Range) - Vegetation.generator.nextInt(Range);
    		tZ = pZ + Vegetation.generator.nextInt(Range) - Vegetation.generator.nextInt(Range);

    		currentBlock = getTopBlock( BaseBlock, tX, tZ, Surface );
    		if( currentBlock != null && withinEnabledBiome( currentBlock.getBiome() ) )
    		{
    			retVal = currentBlock;
    			if( Vegetation.debugging ) logOutput( "Found random block of material: " + currentBlock.getType().toString() + " " + retVal.getX() + "," + retVal.getY() + "," + retVal.getZ() );
    			break;
    		}
    	}
    	return retVal;
    }
    
	public static Block getRandomBlock( Location BaseBlock, Material M, Material Surface )
    {
    	Block retVal = null;
    	int Range = Vegetation.growthRange;
    	
    	double pX = BaseBlock.getX();
    	double pZ = BaseBlock.getZ();

    	Block currentBlock;
    	double tX, tZ;

    	for( int I = 0; I < MaxCycle; I++ )
    	{
       		tX = pX + getRandomRangeValue( I, Range );
    		tZ = pZ + getRandomRangeValue( I, Range );

    		currentBlock = getTopBlock( BaseBlock, tX, tZ, Surface );
    		if( ( currentBlock != null ) && ( currentBlock.getType() == M ) && ( withinEnabledBiome( currentBlock.getBiome() ) ) )
    		{
    			retVal = currentBlock;
    			if( Vegetation.debugging ) logOutput( "Found random block of material: "+ M.toString() + " " + retVal.getX() + "," + retVal.getY() + "," + retVal.getZ() );
    			break;
    		}
    	}
    	return retVal;
    }
	
	public static Block getRandomBlock( Location BaseBlock, Material M, Material Surface, int Range )
    {
    	Block retVal = null;
    	
    	double pX = BaseBlock.getX();
    	double pZ = BaseBlock.getZ();

    	Block currentBlock;
    	double tX, tZ;

    	for( int I = 0; I < MaxCycle; I++ )
    	{
       		tX = pX + getRandomRangeValue( I, Range );
    		tZ = pZ + getRandomRangeValue( I, Range );

    		currentBlock = getTopBlock( BaseBlock, tX, tZ, Surface );
    		if( ( currentBlock != null ) && ( currentBlock.getType() == M ) && ( withinEnabledBiome( currentBlock.getBiome() ) ) )
    		{
    			retVal = currentBlock;
    			if( Vegetation.debugging ) logOutput( "Found random block of material: "+ M.toString() + " " + retVal.getX() + "," + retVal.getY() + "," + retVal.getZ() );
    			break;
    		}
    	}
    	return retVal;
    }
	
	public static Block getRandomBlock( Location BaseBlock, Material M, Material Surface, int MinRange, int MaxRange )
    {
    	Block retVal = null;
    	
    	double pX = BaseBlock.getX();
    	double pZ = BaseBlock.getZ();

    	Block currentBlock;
    	double tX, tZ;

    	for( int I = 0; I < MaxCycle; I++ )
    	{
       		tX = pX + getRandomRangeValue( I, MaxRange );
    		tZ = pZ + getRandomRangeValue( I, MaxRange );

    		//Cacti destroy each other if they are too close togehter
    		if( (tX - pX >= MinRange ) && (tZ - pZ) >= MinRange )
    		{
    			currentBlock = getTopBlock( BaseBlock, tX, tZ, Surface );
    			if( ( currentBlock != null ) && ( currentBlock.getType() == M ) && ( withinEnabledBiome( currentBlock.getBiome() ) ) )
    			{
    				retVal = currentBlock;
    				if( Vegetation.debugging ) logOutput( "Found random block of material: "+ M.toString() + " " + retVal.getX() + "," + retVal.getY() + "," + retVal.getZ() );
    				break;
    			}
    		}
    	}
    	return retVal;
    }

    public static boolean isAdjacentBlockofType1( Block B, Material M )
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
    
    public static boolean isAdjacentBlockofType2( Block B, Material M )
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
    	else if( B.getRelative(BlockFace.UP).getType() == M )
    	{
    		return true;
    	}
    	else
    	{
    		return false;
    	}
    }
    
    public static boolean isSurroundedByBlockType1( Block B, Material M )
    {
    	if( ( B.getRelative(BlockFace.NORTH).getType() == M )
    			&& ( B.getRelative(BlockFace.EAST).getType() == M )
    			&& ( B.getRelative(BlockFace.SOUTH).getType() == M )
    			&& ( B.getRelative(BlockFace.WEST).getType() == M ) )
    		return true;
    			
    	return false;
    }
    
    public static boolean isSurroundedByBlockType2( Block B, Material M )
    {
    	if( ( B.getRelative(BlockFace.NORTH).getType() == M )
    			&& ( B.getRelative(BlockFace.NORTH_EAST).getType() == M )
    			&& ( B.getRelative(BlockFace.EAST).getType() == M )
    			&& ( B.getRelative(BlockFace.SOUTH_EAST).getType() == M )
    			&& ( B.getRelative(BlockFace.SOUTH).getType() == M )
    			&& ( B.getRelative(BlockFace.SOUTH_WEST).getType() == M )
    			&& ( B.getRelative(BlockFace.WEST).getType() == M )
    			&& ( B.getRelative(BlockFace.NORTH_WEST).getType() == M ) )
    		return true;
    			
    	return false;
    }
    
    public static int getRandomRangeValue(int V, int MaxRange)
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
    
    //returns density value from 0.00 - 1.00
    public static float getFieldDensity(Block CenterBlock, int Range)
    {
    	Location L = CenterBlock.getLocation();
    	double pX = CenterBlock.getX();
    	double pZ = CenterBlock.getZ();
    	int R = Math.round( Range / 2 );
    	Material M = CenterBlock.getType();
    	int blockCountInRange = Range * Range;
    	int populatedBlocks = 0;
    	float density = 0.0f;
    	Block CurrentBlock = null;
    	
       	for( double X = pX-R; X <= pX+R; X++ )
    	{
    		for( double Z = pZ-R; Z <= pZ+R; Z++ )
    		{
    			CurrentBlock = Blocks.getTopBlock( L, X, Z, Material.AIR );
    			if( CurrentBlock != null )
    			{
    				if( CurrentBlock.getType() == M ) populatedBlocks++;
    			}
    		}
    	}
       	if( populatedBlocks > 0 )
       	{
       		density = (float)populatedBlocks / (float)blockCountInRange;
       	}
       	logOutput("Densitiy for a field of " + blockCountInRange + "/" + populatedBlocks + " was: " + densitiy );
    	return density;
    }
}
