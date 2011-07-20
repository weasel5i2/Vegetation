package net.weasel.Vegetation;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

public class Cacti {
	public void logOutput(String text) {
		Vegetation.logOutput(text);
	}

	private BlockCrawler blocks;

	/*
	 * generates a new cacti object
	 * 
	 * @param b
	 */
	public Cacti(BlockCrawler b) {
		blocks = b;
	}

	/*
	 * spreads cacti from give block
	 * 
	 * @param blocks Cactus
	 */
	public void growCacti(Block block) {
		Material[] material = { Material.CACTUS };
		if (blocks.getFieldDensity(block, 5, material) > 0.03)
			return;

		if (Vegetation.debugging)
			logOutput("Spreading cacti..");

		int maxSpreadAmount = 1;
		Block plantBlock = null;

		// Get surrounding block and place new plant
		for (int i = 0; i < 150; i++) {
			plantBlock = blocks.getRandomTopBlock(block.getLocation(),
					Material.SAND, Material.AIR, 5);
			if (plantBlock != null
					&& blocks.isSurroundedByBlockType1(
							plantBlock.getRelative(BlockFace.UP), Material.AIR)) {
				plantBlock.getRelative(BlockFace.UP).setType(Material.CACTUS);
				if (Vegetation.debugging)
					logOutput("Planting at: " + plantBlock.getX() + " "
							+ plantBlock.getY() + " " + plantBlock.getZ());
				maxSpreadAmount--;
			}

			if (maxSpreadAmount <= 0)
				break;
		}
	}

	/*
	 * grows just a single cactus on give block
	 * 
	 * @param block Sand
	 */
	public boolean growSingleCacti(Block block) {
		if (blocks.isSurroundedByBlockType1(block.getRelative(BlockFace.UP),
				Material.AIR)) {
			block.getRelative(BlockFace.UP).setType(Material.CACTUS);
			if (Vegetation.debugging)
				logOutput("Planting at: " + block.getX() + " " + block.getY()
						+ " " + block.getZ());
			return true;
		}
		return false;
	}
}
