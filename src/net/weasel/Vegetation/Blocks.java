package net.weasel.Vegetation;

import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.Location;
import org.bukkit.Material;

public class Blocks 
{
	public void logOutput( String text ) { Vegetation.logOutput( text ); }
	
	private final int maxCycle = 150;
	private Settings settings;
	
	public Blocks(Settings s)
	{
		settings = s;
	}
	
	public boolean isLilyPad( Block whichBlock )
	{
		boolean retVal = false;
		
		if( whichBlock.getTypeId() == 37 && settings.enableLilyPads == true 
		|| whichBlock.getTypeId() == 38 && settings.enableLilyPads == true )
		{
			if( whichBlock.getRelative(BlockFace.DOWN).getTypeId() == 9 ) retVal = true;
		}
 	    
 	    return( retVal );
	}
	
	public boolean withinEnabledBiome( Biome biome )
    {	
		if( biome == Biome.FOREST && settings.growForestBiome == true ) return true;
		else if( biome == Biome.RAINFOREST && settings.growRainforestBiome == true ) return true;
		else if( biome == Biome.PLAINS && settings.growPlainsBiome == true ) return true;
		else if( biome == Biome.ICE_DESERT && settings.growIceDesertBiome == true ) return true;
		else if( biome == Biome.DESERT && settings.growDesertBiome == true ) return true;
		else if( biome == Biome.SWAMPLAND && settings.growSwamplandBiome == true ) return true;
		else if( biome == Biome.HELL && settings.growHellBiome == true ) return true;
		else if( biome == Biome.TAIGA && settings.growTaigaBiome == true ) return true;
		else if( biome == Biome.TUNDRA && settings.growTundraBiome == true ) return true;
		else if( biome == Biome.SAVANNA && settings.growSavannahBiome == true ) return true;
		else if( biome == Biome.SHRUBLAND && settings.growShrublandBiome == true ) return true;
		else if( biome == Biome.SEASONAL_FOREST && settings.growSeasonalForestBiome == true ) return true;
		else return false;
    }
    
    public Block getTopBlock( Location BaseBlock, double X, double Z, Material Surface )
    {
    	Block retVal = null, current = null;
    	World W = BaseBlock.getWorld();
    	int MaxY = 127;
    	double Y = BaseBlock.getY();
    	
    	current = W.getBlockAt((int)X, (int)Y, (int)Z);
    	if ( current.getType() == Surface )
    	{
    		//Assume we're above the ground and decrease Y
    		for( int I = 1; I < settings.verticalRadius; I++ )
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
    			for( int I = 1; I < settings.verticalRadius; I++ )
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
    
    public Block getRandomBlock(Location BaseBlock, Material M)
    {
    	Block retVal = null;
    	World W = BaseBlock.getWorld();
    	int hRange = settings.growthRange;
    	int vRange = settings.verticalRadius;
    	
    	double pX = BaseBlock.getX();
    	double pZ = BaseBlock.getZ();
    	double pY = BaseBlock.getY();

    	Block currentBlock;
    	double tX, tY, tZ;

    	for( int I = 0; I < maxCycle; I++ )
    	{
       		tX = pX + getRandomRangeValue( hRange );
       		tY = pY + getRandomRangeValue( vRange );
    		tZ = pZ + getRandomRangeValue( hRange );

    		currentBlock = W.getBlockAt( (int)tX, (int)tY , (int)tZ );
    		if( currentBlock != null && withinEnabledBiome( currentBlock.getBiome() ) && currentBlock.getType() == M )
    		{
    			retVal = currentBlock;
    			if( Vegetation.debugging ) logOutput( "Found random block of material: " + currentBlock.getType().toString() + " " + retVal.getX() + "," + retVal.getY() + "," + retVal.getZ() );
    			break;
    		}
    	}
    	return retVal;
    }
    
	public Block getRandomTopBlock( Location BaseBlock, Material Surface )
    {
    	Block retVal = null;
    	int Range = settings.growthRange;
    	
    	double pX = BaseBlock.getX();
    	double pZ = BaseBlock.getZ();

    	Block currentBlock;
    	double tX, tZ;

    	for( int I = 0; I < maxCycle; I++ )
    	{
       		tX = pX + getRandomRangeValue( Range );
    		tZ = pZ + getRandomRangeValue( Range );

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
    
	public Block getRandomTopBlock( Location BaseBlock, Material M, Material Surface )
    {
    	Block retVal = null;
    	int Range = settings.growthRange;
    	
    	double pX = BaseBlock.getX();
    	double pZ = BaseBlock.getZ();

    	Block currentBlock;
    	double tX, tZ;

    	for( int I = 0; I < maxCycle; I++ )
    	{
       		tX = pX + getRandomRangeValue( Range );
    		tZ = pZ + getRandomRangeValue( Range );

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
	
	public Block getRandomTopBlock( Location BaseBlock, Material M, Material Surface, int Range )
    {
    	Block retVal = null;
    	
    	double pX = BaseBlock.getX();
    	double pZ = BaseBlock.getZ();

    	Block currentBlock;
    	double tX, tZ;

    	for( int I = 0; I < maxCycle; I++ )
    	{
       		tX = pX + getRandomRangeValue( Range );
    		tZ = pZ + getRandomRangeValue( Range );

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
	
	public Block getRandomTopBlock( Location BaseBlock, Material M, Material Surface, int MinRange, int MaxRange )
    {
    	Block retVal = null;
    	
    	double pX = BaseBlock.getX();
    	double pZ = BaseBlock.getZ();

    	Block currentBlock;
    	double tX, tZ;

    	for( int I = 0; I < maxCycle; I++ )
    	{
       		tX = pX + getRandomRangeValue( MaxRange );
    		tZ = pZ + getRandomRangeValue( MaxRange );

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

    public boolean isAdjacentBlockofType1( Block B, Material M )
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
    
    public boolean isAdjacentBlockofType2( Block B, Material M )
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
    
    public boolean isSurroundedByBlockType1( Block B, Material M )
    {
    	if( ( B.getRelative(BlockFace.NORTH).getType() == M )
    			&& ( B.getRelative(BlockFace.EAST).getType() == M )
    			&& ( B.getRelative(BlockFace.SOUTH).getType() == M )
    			&& ( B.getRelative(BlockFace.WEST).getType() == M ) )
    		return true;
    			
    	return false;
    }
    
    public boolean isSurroundedByBlockType2( Block B, Material M )
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
    
    public double getRandomRangeValue(int Range)
    {
    	int retVal = 0;
    	int V = Vegetation.generator.nextInt(6);
    	
    	switch(V)
    	{
    	case 0:
    		retVal = ( Vegetation.generator.nextInt(Range + 1) - Vegetation.generator.nextInt(Range + 1) ) / 2;
    		break;
    		
    	case 1:
    		retVal = ( Vegetation.generator.nextInt(Range + 1) - Vegetation.generator.nextInt(Range + 1) ) / 3;
    		break;
    		
    	case 2:
    		retVal = Vegetation.generator.nextInt(Range + 1) - Vegetation.generator.nextInt(Range + 1);
    		break;
    		
    	case 4:
    		retVal = Vegetation.generator.nextInt(Range + 1);
    		break;
    		
    	case 5:
    		retVal = -Vegetation.generator.nextInt(Range + 1);
    		break;
    		
    	default:
    		retVal = 1;
    		break;
    	}

    	if( ( retVal > 0 ) && ( retVal > Range ) ) retVal = Range;
    	else if( ( retVal < 0 ) && ( retVal < Range*-1 ) ) retVal = Range*-1;
    	
    	return retVal;
    }
    
    //returns density value from 0.0 - 1.0
    //range is (2*Range+1)^2 blocks
    public float getFieldDensity(Block CenterBlock, int Range, Material[] M)
    {
    	Location L = CenterBlock.getLocation();
    	double pX = CenterBlock.getX();
    	double pZ = CenterBlock.getZ();
    	double blockCountInRange = Math.pow(2*Range + 1, 2);
    	int populatedBlocks = 0;
    	float density = 0.0f;
    	Block CurrentBlock = null;
    	Material CurrentM;
    	//int count = 0;
    	
       	for( double X = pX-Range; X <= pX+Range; X++ )
    	{
    		for( double Z = pZ-Range; Z <= pZ+Range; Z++ )
    		{
    			CurrentBlock = getTopBlock( L, X, Z, Material.AIR );
    			if( CurrentBlock != null )
    			{
    				CurrentM = CurrentBlock.getType();
    				
    				for( int I = 0; I < M.length; I++ )
    				{
    					if( CurrentM == M[I] )
    					{
    						populatedBlocks++;
    						break;
    					}
    				}
    			}
    			//count++;
    		}
    	}
       	if( populatedBlocks > 0 )
       	{
       		density = (float)populatedBlocks / (float)blockCountInRange;
       	}
       	//logOutput("Densitiy for a field of " + populatedBlocks + "/" + blockCountInRange + " " + count + " was: " + density );
    	return density;
    }
}
