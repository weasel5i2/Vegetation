package net.weasel.Vegetation;

import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.Material;

public class Plants {
	private BlockCrawler blockCrawler;

	public Plants(BlockCrawler b) {
		blockCrawler = b;
	}

	public void growPlant(Block plantBlock, Material plantType) {
		Material[] material = { Material.YELLOW_FLOWER, Material.RED_ROSE, Material.BROWN_MUSHROOM, Material.RED_MUSHROOM, Material.PUMPKIN };
		if (plantType == Material.RED_ROSE || plantType == Material.YELLOW_FLOWER) {
			if (blockCrawler.getFieldDensity(plantBlock, 3, material) > 0.1)
				return;
		} else if (plantType == Material.RED_MUSHROOM || plantType == Material.BROWN_MUSHROOM) {
			if (blockCrawler.getFieldDensity(plantBlock, 3, material) > 0.05)
				return;
		}

		int maxSpreadAmount = 2;
		Biome biome = plantBlock.getBiome();
		Block emptyBlock = null;
		Material plantBlockMaterial = plantBlock.getRelative(BlockFace.DOWN).getType();
		// Get surrounding block and place new plant
		for (int i = 0; i < 150; i++) {
			emptyBlock = blockCrawler.getRandomTopBlock(plantBlock.getLocation(), plantBlockMaterial, Material.AIR, 3);
			if (emptyBlock != null && emptyBlock.getBiome() == biome) {
				if (growSinglePlant(emptyBlock, plantType)) {
					maxSpreadAmount--;
				}
				if (maxSpreadAmount <= 0)
					break;
			}

		}
	}

	public boolean growSinglePlant(Block block, Material plantType) {
		int lightLevel = block.getRelative(BlockFace.UP).getLightLevel();
		switch (plantType) {
		case YELLOW_FLOWER:
			if (lightLevel >= 8) {
				block.getRelative(BlockFace.UP).setType(plantType);
				return true;
			}
			break;

		case RED_ROSE:
			if (lightLevel >= 8) {
				block.getRelative(BlockFace.UP).setType(plantType);
				return true;
			}
			break;

		case RED_MUSHROOM:
			if (lightLevel <= 13) {
				block.getRelative(BlockFace.UP).setType(plantType);
				return true;
			}
			break;

		case BROWN_MUSHROOM:
			if (lightLevel <= 13) {
				block.getRelative(BlockFace.UP).setType(plantType);
				return true;
			}
			break;

		default:
			block.getRelative(BlockFace.UP).setType(plantType);
			return true;
		}
		return false;
	}
}
