package net.weasel.Vegetation;

import java.util.ArrayList;

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
    public Block getTopBlock(Location baseBlock, int x, int z,  Material surface)
    {   	
    	Block currentBlock = null;
    	World world = baseBlock.getWorld();
    	int maxY = 127;
    	int y = (int)baseBlock.getY();
    	int currentMaterial;
    	int surfaceId = surface.getId();
    	
    	currentMaterial = world.getBlockTypeIdAt(x, y, z);
    	if( currentMaterial == surfaceId )
    	{
    		//Assume we're above the ground and decrease Y
    		for( int i = 1; i < settings.verticalRadius; i++ )
    		{
    			if( (y - i) < 0 )
    			{
    				currentBlock = null;
    				break;
    			}
    			currentMaterial = world.getBlockTypeIdAt(x, y - i, z);
    			if( currentMaterial != surfaceId )
    			{
    				currentBlock = world.getBlockAt(x, y - i, z);
    				if( withinEnabledBiome(currentBlock) ) break;
    				else currentBlock = null;
    			}
    		}
    	}
    	else if( currentMaterial != surfaceId )
    	{
    		//Assume we're Underground and increase Y
    		for( int i = 1; i < settings.verticalRadius; i++ )
    		{
    			if( (y + i) > maxY )
    			{
    				currentBlock = null;
    				break;
    			}
    			currentMaterial = world.getBlockTypeIdAt(x, y + i, z);
    			if( currentMaterial == surfaceId )
    			{
    				currentBlock = world.getBlockAt(x, y + i - 1, z);
    				if( withinEnabledBiome(currentBlock) ) break;
    				else currentBlock = null;
    			}
    		}
    	}
    	return currentBlock;
    }
    
	/*
	 * gets a block right under the specified surface material block
	 * @param baseBlock Location of the block the player is currently standing on
	 * @param x
	 * @param z
	 * @param ground
	 * @param surface
	 * @return Block
	 */
    public Block getTopBlock(Location baseBlock, int x, int z, Material ground, Material surface)
    {
    	//surface and ground can't be of the same material
    	if( ground == surface ) return null;
    	
    	Block currentBlock = null;
    	World world = baseBlock.getWorld();
    	int maxY = 127;
    	int y = (int)baseBlock.getY();
    	int currentMaterial;
    	int surfaceId = surface.getId();
    	int groundId = ground.getId();
    	
    	currentMaterial = world.getBlockTypeIdAt(x, y, z);
    	if( currentMaterial == surfaceId )
    	{
    		//Assume we're above the ground and decrease Y
    		for( int i = 1; i < settings.verticalRadius; i++ )
    		{
    			if( (y - i) < 0 )
    			{
    				currentBlock = null;
    				break;
    			}
    			currentMaterial = world.getBlockTypeIdAt(x, y - i, z);
    			if( currentMaterial == groundId )
    			{
    				currentBlock = world.getBlockAt(x, y - i, z);
    				if( withinEnabledBiome(currentBlock) ) break;
    				else
    				{
    					currentBlock = null;
    					break;
    				}
    			}
    			else if( currentMaterial != groundId && currentMaterial != surfaceId )
    			{
    				currentBlock = null;
    				break;
    			}
    		}
    	}
    	else if( currentMaterial == groundId )
    	{
    		//Assume we're Underground and increase Y
    		for( int i = 1; i < settings.verticalRadius; i++ )
    		{
    			if( (y + i) > maxY )
    			{
    				currentBlock = null;
    				break;
    			}
    			currentMaterial = world.getBlockTypeIdAt(x, y + i, z);
    			if( currentMaterial == surfaceId )
    			{
    				currentBlock = world.getBlockAt(x, y + i - 1, z);
    				if( withinEnabledBiome(currentBlock) ) break;
    				else
    				{
    					currentBlock = null;
    					break;
    				}
    			}
    			else if( currentMaterial != groundId && currentMaterial != surfaceId )
    			{
    				currentBlock = null;
    				break;
    			}
    		}
    	}
    	return currentBlock;
    }
    
    /*
     * gets a random block around the player from give material
     * @param baseBlock Location of the block the player is currently standing on
     * @param material
     * @return Block
     */
    public Block getRandomBlock(Location baseBlock, Material material)
    {
    	World world = baseBlock.getWorld();
    	int hRange = settings.growthRange;
    	int vRange = settings.verticalRadius;
    	
    	int pX = (int)baseBlock.getX();
    	int pZ = (int)baseBlock.getZ();
    	int pY = (int)baseBlock.getY();

    	Block currentBlock = null;
    	int materialId = material.getId();
    	int currentMaterial;
    	int tX, tY, tZ;

    	for( int i = 0; i < maxCycle; i++ )
    	{
       		tX = pX + getRandomRangeValue(hRange);
       		tY = pY + getRandomRangeValue(vRange);
    		tZ = pZ + getRandomRangeValue(hRange);

    		currentMaterial = world.getBlockTypeIdAt(tX, tY , tZ);
    		if( currentMaterial == materialId )
    		{
    			currentBlock = world.getBlockAt(tX, tY, tZ);
    			if( withinEnabledBiome(currentBlock) )
    			{
        			if( Vegetation.debugging ) logOutput("Found random block of material: " + currentBlock.getType().toString() + " " + currentBlock.getX() + "," + currentBlock.getY() + "," + currentBlock.getZ());
        			break;
    			}
    		}
    	}
    	return currentBlock;
    }
    
    /*
     * gets a random block around the player. Block is on the surface
     * @param baseBlock Location of the block the player is currently standing on
     * @param surface
     * @return Block
     */
	public Block getRandomTopBlock(Location baseBlock, Material surface)
    {
    	int range = settings.growthRange;
    	
    	int pX = (int)baseBlock.getX();
    	int pZ = (int)baseBlock.getZ();

    	Block currentBlock = null;
    	int tX, tZ;

    	for( int i = 0; i < maxCycle; i++ )
    	{
       		tX = pX + getRandomRangeValue(range);
    		tZ = pZ + getRandomRangeValue(range);

    		currentBlock = getTopBlock(baseBlock, tX, tZ, surface);
    		if( currentBlock != null )
    		{
    			if( Vegetation.debugging ) logOutput("Found random block of material: " + currentBlock.getType().toString() + " " + currentBlock.getX() + "," + currentBlock.getY() + "," + currentBlock.getZ());
    			break;
    		}
    	}
    	return currentBlock;
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
    	int range = settings.growthRange;
    	
    	int pX = (int)baseBlock.getX();
    	int pZ = (int)baseBlock.getZ();

    	Block currentBlock = null;
    	int tX, tZ;

    	for( int i = 0; i < maxCycle; i++ )
    	{
       		tX = pX + getRandomRangeValue(range);
    		tZ = pZ + getRandomRangeValue(range);

    		currentBlock = getTopBlock(baseBlock, tX, tZ, material, surface);
    		if( currentBlock != null )
    		{
    			if( Vegetation.debugging ) logOutput("Found random block of material: " + material.toString() + " " + currentBlock.getX() + "," + currentBlock.getY() + "," + currentBlock.getZ());
    			break;
    		}
    	}
    	return currentBlock;
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
    	int pX = (int)baseBlock.getX();
    	int pZ = (int)baseBlock.getZ();

    	Block currentBlock = null;
    	int tX, tZ;

    	for( int i = 0; i < maxCycle; i++ )
    	{
       		tX = pX + getRandomRangeValue(range);
    		tZ = pZ + getRandomRangeValue(range);

    		currentBlock = getTopBlock(baseBlock, tX, tZ, material, surface);
    		if( currentBlock != null )
    		{
    			if( Vegetation.debugging ) logOutput("Found random block of material: " + material.toString() + " " + currentBlock.getX() + "," + currentBlock.getY() + "," + currentBlock.getZ());
    			break;
    		}
    	}
    	return currentBlock;
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
    	int pX = (int)baseBlock.getX();
    	int pZ = (int)baseBlock.getZ();

    	Block currentBlock = null;
    	int tX, tZ;

    	for( int i = 0; i < maxCycle; i++ )
    	{
       		tX = pX + getRandomRangeValue(maxRange);
    		tZ = pZ + getRandomRangeValue(maxRange);

    		//Cacti destroy each other if they are too close together
    		if( (tX - pX >= minRange ) && (tZ - pZ) >= minRange )
    		{
    			currentBlock = getTopBlock(baseBlock, tX, tZ, material, surface);
    			if( currentBlock != null )
    			{
    				if( Vegetation.debugging ) logOutput("Found random block of material: " + material.toString() + " " + currentBlock.getX() + "," + currentBlock.getY() + "," + currentBlock.getZ());
    				break;
    			}
    		}
    	}
    	return currentBlock;
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
    	else if( block.getRelative(BlockFace.DOWN).getType() == material )
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
    public int getRandomRangeValue(int range)
    {
    	int retVal;
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
    	int pX = centerBlock.getX();
    	int pZ = centerBlock.getZ();
    	double blockCountInRange = Math.pow(2*range + 1, 2);
    	int populatedBlocks = 0;
    	float density = 0.0f;
    	Block currentBlock = null;
    	Material currentMaterial;
    	
       	for( int x = pX-range; x <= pX+range; x++ )
    	{
    		for( int z = pZ-range; z <= pZ+range; z++ )
    		{
    			currentBlock = getTopBlock(location, x, z, Material.AIR);
    			if( currentBlock != null )
    			{
    				currentMaterial = currentBlock.getType();
    				
    				for( int i = 0; i < material.length; i++ )
    				{
    					if( currentMaterial == material[i] )
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
    
    public ArrayList<Block> getBlocksInRange(Block centerBlock, Material material, int range)
    {
    	ArrayList<Block> blocks = new ArrayList<Block>();
    	World world = centerBlock.getWorld();
    	Location center = centerBlock.getLocation();
    	int materialId = material.getId();
    	int cX = (int)center.getX();
    	int cY = (int)center.getY();
    	int cZ = (int)center.getZ();
    	
    	for( int y = cY-range; y <= cY+range; y++ )
    	{
           	for( int x = cX-range; x <= cX+range; x++ )
        	{
        		for( int z = cZ-range; z <= cZ+range; z++ )
        		{
        			if( world.getBlockTypeIdAt(x, y, z) == materialId )
        			{
        				blocks.add(world.getBlockAt(x, y, z));
        			}
        		}
        	}
    	}
    	return blocks;
    }
}
