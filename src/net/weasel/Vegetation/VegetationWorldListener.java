package net.weasel.Vegetation;

import org.bukkit.event.world.ChunkPopulateEvent;
import org.bukkit.event.world.WorldListener;

public class VegetationWorldListener extends WorldListener {
	
	private final Vegetation plugin;

	public VegetationWorldListener(final Vegetation instance) {
		plugin = instance;
	}

	public void onChunkPopulate(ChunkPopulateEvent event) {
		plugin.getExecutor().addJob(new ChunkTask(event.getChunk()));
	}
}
