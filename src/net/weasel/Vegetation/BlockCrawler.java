package net.weasel.Vegetation;

import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.Location;
import org.bukkit.Material;

public class BlockCrawler 
{
	public void logOutput(String text) { Vegetation.logOutput(text); }
	
	private int maxCycle = 150;
	private Settings settings;
	
	/*
	 * Constructs a new object
	 * @param s a set of settings i required
	 */
	public BlockCrawler(Settings s)
	{
		settings = s;
	}
	
	/*
	 * Determines whether it's allowed to grow on the given block
	 * @param b
	 */
	public boolean withinEnabledBiome(Block b)
    {	
		Biome biome = b.getBiome();
		
		if( biome == Biome.FOREST && settings.growForestBiome ) return true;
		else if( biome == Biome.RAINFOREST && settings.growRainforestBiome ) return true;
		else if( biome == Biome.PLAINS && settings.growPlainsBiome ) return true;
		else if( biome == Biome.ICE_DESERT && settings.growIceDesertBiome ) return true;
		else if( biome == Biome.DESERT && settings.growDesertBiome ) return true;
		else if( biome == Biome.SWAMPLAND && settings.growSwamplandBiome ) return true;
		else if( biome == Biome.HELL && settings.growHellBiome ) return true;
		else if( biome == Biome.TAIGA && settings.growTaigaBiome ) return true;
		else if( biome == Biome.TUNDRA && settings.growTundraBiome ) return true;
		else if( biome == Biome.SAVANNA && settings.growSavannahBiome ) return true;
		else if( biome == Biome.SHRUBLAND && settings.growShrublandBiome ) return true;
		else if( biome == Biome.SEASONAL_FOREST && settings.growSeasonalForestBiome ) return true;
		else return false;
    }
    
	/*
	 * gets a block right under the specified surface material block
	 * @param baseBlock Location of the block the player is currently standing on
	 * @param x
	 * @param z
	 * @param surface
	 * @return Block
	 */
    public Block getTopBlock(Location baseBlock, double x, double z, Material surface)
    {
    	Block retVal = null, current = null;
    	World world = baseBlock.getWorld();
    	int maxY = 127;
    	double y = baseBlock.getY();
    	
    	current = world.getBlockAt((int)x, (int)y, (int)z);
    	if ( current.getType() == surface )
    	{
    		//Assume we're above the ground and decrease Y
    		for( int i = 1; i < settings.verticalRadius; i++ )
    		{
    			if( ((int)y - i) < 0 )
    			{
    				break;
    			}
    			current = world.getBlockAt((int)x, (int)y - i,(int)z);
    			//we need at least two blocks of air on top of a block
    			if( current.getType() != surface && i <= 2 )
    			{
    				retVal = current;
    				break;
    			}
    		}
    	}
    	else if ( current.getType() != surface )
    	{
    		//Assume we're already on top, thus check for air
    		if ( world.getBlockAt((int)x, (int)y + 1, (int)z).getType() == surface )
    		{
    			//we need at least two blocks of air on top of a block
    			if ( world.getBlockAt((int)x, (int)y + 2, (int)z).getType() == surface )
    			{
    				retVal = current;
    			}
    		}
    		else
    		{
    			//Assume we're Underground and increase Y
    			for( int i = 1; i < settings.verticalRadius; i++ )
    			{
    				if( ((int)y + i) >= maxY )
    				{
    					break;
    				}
    				current = world.getBlockAt((int)x, (int)y + i,(int)z);
    				if( current.getType() == surface )
    				{
    					//we need at least two blocks of air on top of a block
    					if( current.getRelative(BlockFace.UP).getType() == surface )
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
    
    /*
     * gets a random block around the player from give material
     * @param baseBlock Location of the block the player is currently standing on
     * @param material
     * @return Block
     */
    public Block getRandomBlock(Location baseBlock, Material material)
    {
    	Block retVal = null;
    	World world = baseBlock.getWorld();
    	int hRange = settings.growthRange;
    	int vRange = settings.verticalRadius;
    	
    	double pX = baseBlock.getX();
    	double pZ = baseBlock.getZ();
    	double pY = baseBlock.getY();

    	Block currentBlock;
    	double tX, tY, tZ;

    	for( int i = 0; i < maxCycle; i++ )
    	{
       		tX = pX + getRandomRangeValue(hRange);
       		tY = pY + getRandomRangeValue(vRange);
    		tZ = pZ + getRandomRangeValue(hRange);

    		currentBlock = world.getBlockAt((int)tX, (int)tY , (int)tZ);
    		if( currentBlock != null && withinEnabledBiome(currentBlock) && currentBlock.getType() == material )
    		{
    			retVal = currentBlock;
    			if( Vegetation.debugging ) logOutput("Found random block of material: " + currentBlock.getType().toString() + " " + retVal.getX() + "," + retVal.getY() + "," + retVal.getZ());
    			break;
    		}
    	}
    	return retVal;
    }
    
    /*
     * gets a random block around the player. Block is on the surface
     * @param baseBlock Location of the block the player is currently standing on
     * @param surface
     * @return Block
     */
	public Block getRandomTopBlock(Location baseBlock, Material surface)
    {
    	Block retVal = null;
    	int range = settings.growthRange;
    	
    	double pX = baseBlock.getX();
    	double pZ = baseBlock.getZ();

    	Block currentBlock;
    	double tX, tZ;

    	for( int i = 0; i < maxCycle; i++ )
    	{
       		tX = pX + getRandomRangeValue(range);
    		tZ = pZ + getRandomRangeValue(range);

    		currentBlock = getTopBlock(baseBlock, tX, tZ, surface);
    		if( currentBlock != null && withinEnabledBiome(currentBlock) )
    		{
    			retVal = currentBlock;
    			if( Vegetation.debugging ) logOutput("Found random block of material: " + currentBlock.getType().toString() + " " + retVal.getX() + "," + retVal.getY() + "," + retVal.getZ());
    			break;
    		}
    	}
    	return retVal;
    }
    
    /*
     * gets a random block of specified material around the player. Block is on the surface
     * @param baseBlock Location of the block the player is currently standing on
     * @param material
     * @param surface
     * @return Block
     */
	public Block getRandomTopBlock(Location baseBlock, Material material, Material surface)
    {
    	Block retVal = null;
    	int range = settings.growthRange;
    	
    	double pX = baseBlock.getX();
    	double pZ = baseBlock.getZ();

    	Block currentBlock;
    	double tX, tZ;

    	for( int i = 0; i < maxCycle; i++ )
    	{
       		tX = pX + getRandomRangeValue(range);
    		tZ = pZ + getRandomRangeValue(range);

    		currentBlock = getTopBlock(baseBlock, tX, tZ, surface);
    		if( ( currentBlock != null ) && ( currentBlock.getType() == material ) && ( withinEnabledBiome(currentBlock) ) )
    		{
    			retVal = currentBlock;
    			if( Vegetation.debugging ) logOutput("Found random block of material: " + material.toString() + " " + retVal.getX() + "," + retVal.getY() + "," + retVal.getZ());
    			break;
    		}
    	}
    	return retVal;
    }
	
    /*
     * gets a random block of specified material in give range around the player. Block is on the surface
     * @param baseBlock Location of the block the player is currently standing on
     * @param material
     * @param surface
     * @param range
     * @return Block
     */
	public Block getRandomTopBlock(Location baseBlock, Material material, Material surface, int range)
    {
    	Block retVal = null;
    	
    	double pX = baseBlock.getX();
    	double pZ = baseBlock.getZ();

    	Block currentBlock;
    	double tX, tZ;

    	for( int i = 0; i < maxCycle; i++ )
    	{
       		tX = pX + getRandomRangeValue(range);
    		tZ = pZ + getRandomRangeValue(range);

    		currentBlock = getTopBlock(baseBlock, tX, tZ, surface);
    		if( ( currentBlock != null ) && ( currentBlock.getType() == material ) && ( withinEnabledBiome(currentBlock) ) )
    		{
    			retVal = currentBlock;
    			if( Vegetation.debugging ) logOutput("Found random block of material: " + material.toString() + " " + retVal.getX() + "," + retVal.getY() + "," + retVal.getZ());
    			break;
    		}
    	}
    	return retVal;
    }
	
    /*
     * gets a random block of specified material in give range around the player. Block is on the surface
     * @param baseBlock Location of the block the player is currently standing on
     * @param material
     * @param surface
     * @param minrange
     * @param maxrange
     * @return Block
     */
	public Block getRandomTopBlock(Location baseBlock, Material material, Material surface, int minRange, int maxRange)
    {
    	Block retVal = null;
    	
    	double pX = baseBlock.getX();
    	double pZ = baseBlock.getZ();

    	Block currentBlock;
    	double tX, tZ;

    	for( int i = 0; i < maxCycle; i++ )
    	{
       		tX = pX + getRandomRangeValue(maxRange);
    		tZ = pZ + getRandomRangeValue(maxRange);

    		//Cacti destroy each other if they are too close togehter
    		if( (tX - pX >= minRange ) && (tZ - pZ) >= minRange )
    		{
    			currentBlock = getTopBlock(baseBlock, tX, tZ, surface);
    			if( ( currentBlock != null ) && ( currentBlock.getType() == material ) && ( withinEnabledBiome(currentBlock) ) )
    			{
    				retVal = currentBlock;
    				if( Vegetation.debugging ) logOutput("Found random block of material: " + material.toString() + " " + retVal.getX() + "," + retVal.getY() + "," + retVal.getZ());
    				break;
    			}
    		}
    	}
    	return retVal;
    }

	/*
	 * checks whether give block (x) is surrounded by any block (+) of given material
	 * 
	 *  @param block
	 *  @param material
	 *  @return
	 */
    public boolean isAdjacentofBlockType1(Block block, Material material)
    {
    	if( block.getRelative(BlockFace.NORTH).getType() == material )
    	{
    		return true;
    	}
    	else if( block.getRelative(BlockFace.EAST).getType() == material )
    	{
    		return true;
    	}
    	else if( block.getRelative(BlockFace.SOUTH).getType() == material )
    	{
    		return true;
    	}
    	else if( block.getRelative(BlockFace.WEST).getType() == material )
    	{
    		return true;
    	}
    	else
    	{
    		return false;
    	}
    }
    
	/*
	 * checks whether give block (x) is surrounded by any block (+) of give material
	 *
	 *  @param block
	 *  @param material
	 *  @return
	 */
    public boolean isAdjacentofBlockType2(Block block, Material material)
    {
    	if( block.getRelative(BlockFace.NORTH).getType() == material )
    	{
    		return true;
    	}
    	else if( block.getRelative(BlockFace.EAST).getType() == material )
    	{
    		return true;
    	}
    	else if( block.getRelative(BlockFace.SOUTH).getType() == material )
    	{
    		return true;
    	}
    	else if( block.getRelative(BlockFace.WEST).getType() == material )
    	{
    		return true;
    	}
    	else if( block.getRelative(BlockFace.UP).getType() == material )
    	{
    		return true;
    	}
    	else
    	{
    		return false;
    	}
    }
    
    /*
     * checks whether a block is completely surrounded by blocks of give material
     * @param blocks
     * @param material
     * @return
     */
    public boolean isSurroundedByBlockType1(Block block, Material material)
    {
    	if( ( block.getRelative(BlockFace.NORTH).getType() == material )
    			&& ( block.getRelative(BlockFace.EAST).getType() == material )
    			&& ( block.getRelative(BlockFace.SOUTH).getType() == material )
    			&& ( block.getRelative(BlockFace.WEST).getType() == material ) )
    		return true;
    			
    	return false;
    }
    
    /*
     * checks whether a block is completely surrounded by blocks of give material
     * @param blocks
     * @param material
     * @return
     */
    public boolean isSurroundedByBlockType2(Block block, Material material)
    {
    	if( ( block.getRelative(BlockFace.NORTH).getType() == material )
    			&& ( block.getRelative(BlockFace.NORTH_EAST).getType() == material )
    			&& ( block.getRelative(BlockFace.EAST).getType() == material )
    			&& ( block.getRelative(BlockFace.SOUTH_EAST).getType() == material )
    			&& ( block.getRelative(BlockFace.SOUTH).getType() == material )
    			&& ( block.getRelative(BlockFace.SOUTH_WEST).getType() == material )
    			&& ( block.getRelative(BlockFace.WEST).getType() == material )
    			&& ( block.getRelative(BlockFace.NORTH_WEST).getType() == material ) )
    		return true;
    			
    	return false;
    }
    
    /*
     * randomizes given range value from -range to range
     * @param range
     * @return
     */
    public double getRandomRangeValue(int range)
    {
    	int retVal = 0;
    	int V = Vegetation.generator.nextInt(6);
    	
    	switch(V)
    	{
    	case 0:
    		retVal = ( Vegetation.generator.nextInt(range + 1) - Vegetation.generator.nextInt(range + 1) ) / 2;
    		break;
    		
    	case 1:
    		retVal = ( Vegetation.generator.nextInt(range + 1) - Vegetation.generator.nextInt(range + 1) ) / 3;
    		break;
    		
    	case 2:
    		retVal = Vegetation.generator.nextInt(range + 1) - Vegetation.generator.nextInt(range + 1);
    		break;
    		
    	case 4:
    		retVal = Vegetation.generator.nextInt(range + 1);
    		break;
    		
    	case 5:
    		retVal = -Vegetation.generator.nextInt(range + 1);
    		break;
    		
    	default:
    		retVal = 1;
    		break;
    	}

    	if( ( retVal > 0 ) && ( retVal > range ) ) retVal = range;
    	else if( ( retVal < 0 ) && ( retVal < range*-1 ) ) retVal = range*-1;
    	
    	return retVal;
    }
    
    /*
     * generates a density variable for give field of blocks
     * formula is: (2*Range+1)^2 blocks
     * @param centerBlock
     * @param material
     * @return float value between 0.0 and 1.0
     */
    public float getFieldDensity(Block centerBlock, int range, Material[] material)
    {
    	Location location = centerBlock.getLocation();
    	double pX = centerBlock.getX();
    	double pZ = centerBlock.getZ();
    	double blockCountInRange = Math.pow(2*range + 1, 2);
    	int populatedBlocks = 0;
    	float density = 0.0f;
    	Block currentBlock = null;
    	Material CurrentM;
    	
       	for( double x = pX-range; x <= pX+range; x++ )
    	{
    		for( double z = pZ-range; z <= pZ+range; z++ )
    		{
    			currentBlock = getTopBlock(location, x, z, Material.AIR);
    			if( currentBlock != null )
    			{
    				CurrentM = currentBlock.getType();
    				
    				for( int i = 0; i < material.length; i++ )
    				{
    					if( CurrentM == material[i] )
    					{
    						populatedBlocks++;
    						break;
    					}
    				}
    			}
    		}
    	}
       	if( populatedBlocks > 0 )
       	{
       		density = (float)populatedBlocks / (float)blockCountInRange;
       	}
    	return density;
    }
}
