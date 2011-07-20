package net.weasel.Vegetation;

import org.bukkit.block.Block;

public class VegetationPlayer {

	private String name;
	private Block lastBlock;

	public VegetationPlayer(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setLastBlockPosition(Block block) {
		lastBlock = block;
	}

	public Block getLastBlockPosition() {
		return lastBlock;
	}
}
