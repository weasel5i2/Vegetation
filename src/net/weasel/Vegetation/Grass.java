package net.weasel.Vegetation;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

public class Grass {
	public void logOutput(String text) {
		Vegetation.logOutput(text);
	}

	private World world;
	private BlockCrawler blockCrawler;
	private int maxGrassHeight;

	/*
	 * generates a new Grass object
	 * 
	 * @param b
	 * 
	 * @param maxHeight
	 */
	public Grass(World world, BlockCrawler blockCrawler, int maxGrassHeight) {
		this.world = world;
		this.blockCrawler = blockCrawler;
		this.maxGrassHeight = maxGrassHeight;
	}

	/*
	 * grows grass on give block
	 * 
	 * @param block
	 */
	public void growGrass(Block block) {
		// for safety...don't want any other blocks turn into Grass blocks
		// suddenly
		if (block.getType() != Material.GRASS)
			return;

		int data = block.getData();

		if (data < 2)
			data = 2;

		if (data > maxGrassHeight + 1) {
			block.setData((byte) (maxGrassHeight + 1));
		} else {
			data++;
			if (data < maxGrassHeight + 1) {
				// block.setData((byte)data, true);
				block.setTypeIdAndData(Material.GRASS.getId(), (byte) data, true);
				// we only need to refresh the chunk if the grass is growing
				// world.refreshChunk(block.getChunk().getX(),
				// block.getChunk().getZ());
			}
		}
	}

	/*
	 * cuts down grass in an area around give location
	 * 
	 * @param baseBlock
	 */
	public void mowGrass(Location baseBlock) {
		int r = Math.round(50 / 2);
		int pX = (int) baseBlock.getX();
		int pZ = (int) baseBlock.getZ();

		Block currentBlock = null;

		for (int x = pX - r; x <= pX + r; x++) {
			for (int z = pZ - r; z <= pZ + r; z++) {
				currentBlock = blockCrawler.getTopBlock(baseBlock, x, z, Material.AIR);

				if (currentBlock != null) {
					if (currentBlock.getType() == Material.GRASS) {
						// currentBlock.setData((byte)0);
						currentBlock.setTypeIdAndData(Material.GRASS.getId(), (byte) 0, true);
					} else if (currentBlock.getType() == Material.RED_ROSE || currentBlock.getType() == Material.YELLOW_FLOWER
							|| currentBlock.getType() == Material.RED_MUSHROOM || currentBlock.getType() == Material.BROWN_MUSHROOM) {
						if (currentBlock.getRelative(BlockFace.DOWN).getType() == Material.GRASS)
							// currentBlock.getRelative(BlockFace.DOWN).setData((byte)0);
							currentBlock.getRelative(BlockFace.DOWN).setTypeIdAndData(Material.GRASS.getId(), (byte) 0, true);
						// world.refreshChunk(currentBlock.getChunk().getX(),
						// currentBlock.getChunk().getZ());
					}
				}
			}
		}
	}
}
