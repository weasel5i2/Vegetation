package net.weasel.Vegetation;

import org.bukkit.Chunk;
import org.bukkit.ChunkSnapshot;
import org.bukkit.Material;
import org.bukkit.World;

public class ChunkTask implements Runnable {

	private Chunk chunk;
	
	public ChunkTask(Chunk chunk)
	{
		this.chunk = chunk;
	}
	
	@Override
	public void run() {
		ChunkSnapshot snapChunk = chunk.getChunkSnapshot();
		// x - 0-15; y - 0-127; z - 0-15
		for (int y = 0; y <= 127; y++) {
			for (int x = 0; x <= 15; x++) {
				for (int z = 0; z <= 15; z++) {
					int material = snapChunk.getBlockTypeId(x, y, z);

					if (material == Material.LONG_GRASS.getId()) {
						if (snapChunk.getBlockTypeId(x, y - 1, z) == Material.STATIONARY_WATER.getId()
								|| snapChunk.getBlockTypeId(x, y - 1, z) == Material.SAND.getId()) {
							chunk.getBlock(x, y, z).setType(Material.AIR);
						}
					}
				}
			}
		}
	}

}
