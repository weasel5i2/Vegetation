package net.weasel.Vegetation;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

public class Canes {

	public void logOutput(String text) {
		Vegetation.logOutput(text);
	}

	private BlockCrawler blocks;

	/*
	 * generates a new Canes object
	 * 
	 * @param b
	 */
	public Canes(BlockCrawler b) {
		blocks = b;
	}

	/*
	 * spread sugar canes from give block
	 * 
	 * @param block
	 */
	public void growCanes(Block block) {
		if (Vegetation.debugging)
			logOutput("Spreading plants..");

		int maxSpreadAmount = 2;

		if (Vegetation.debugging)
			logOutput("Spreading Type of Plant: Sugar Crane ");

		Block plantBlock = null;
		// Get surrounding block and place new plant
		for (int i = 0; i < 150; i++) {
			plantBlock = blocks.getRandomTopBlock(block.getLocation(), Material.GRASS, Material.AIR, 5);
			if (plantBlock != null) {
				// sugar cranes can only grow near water :O
				if (blocks.isAdjacentofBlockType1(plantBlock, Material.STATIONARY_WATER) || blocks.isAdjacentofBlockType1(plantBlock, Material.WATER)) {
					plantBlock.getRelative(BlockFace.UP).setType(Material.SUGAR_CANE_BLOCK);
					if (Vegetation.debugging)
						logOutput("Planting at: " + plantBlock.getX() + " " + plantBlock.getY() + " " + plantBlock.getZ());
					maxSpreadAmount--;
				}
			}

			if (maxSpreadAmount <= 0)
				break;
		}
	}

	/*
	 * grows a single sugar cane on given block
	 * 
	 * @param block
	 */
	public boolean growSingleCane(Block block) {
		if (blocks.isAdjacentofBlockType1(block, Material.STATIONARY_WATER) || blocks.isAdjacentofBlockType1(block, Material.WATER)) {
			block.getRelative(BlockFace.UP).setType(Material.SUGAR_CANE_BLOCK);
			if (Vegetation.debugging)
				logOutput("Planting at: " + block.getX() + " " + block.getY() + " " + block.getZ());
			return true;
		}
		return false;
	}
}
