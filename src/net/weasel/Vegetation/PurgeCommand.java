package net.weasel.Vegetation;

import org.bukkit.Chunk;
import org.bukkit.ChunkSnapshot;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PurgeCommand implements CommandExecutor {

	public static void logOutput(String text) {
		Vegetation.logOutput(text);
	}

	private Vegetation plugin;

	public PurgeCommand(Vegetation instance) {
		this.plugin = instance;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;

			if (plugin.hasPermission(player, "vegetation.purge")) {
				World world = player.getWorld();
				VegetationWorld vWorld = plugin.vWorlds.get(world.getName());
				Settings settings = vWorld.getSettings();
				if (settings == null) {
					if (player.getWorld().getEnvironment() == Environment.NETHER) {
						player.sendMessage("Nothing to purge from a nether world");
						return true;
					} else {
						//TODO: create settings for new World
					}
				}
				if (vWorld.getActivePlayerCommands() < settings.maxActivePlayerCommands) {
					if (args.length == 1) {
						vWorld.increaseActivePlayerCommands();

						Chunk[] chunks = player.getWorld().getLoadedChunks();
						int count = 0;
						int blockCount = 0;
						float chunkCount = chunks.length;

						if (args[0].equals("wild_grass")) {
							for (int i = 0; i < chunkCount; i++) {
								ChunkSnapshot snapChunk = chunks[i].getChunkSnapshot();
								// x - 0-15; y - 0-127; z - 0-15
								for (int y = 0; y <= 127; y++) {
									for (int x = 0; x <= 15; x++) {
										for (int z = 0; z <= 15; z++) {
											int material = snapChunk.getBlockTypeId(x, y, z);

											if (material == Material.GRASS.getId()) {
												if (snapChunk.getBlockData(x, y, z) != 0) {
													chunks[i].getBlock(x, y, z).setData((byte) 0);
													blockCount++;
												}
											} else if (material == Material.SUGAR_CANE_BLOCK.getId()) {
												if (snapChunk.getBlockData(x, y, z) == 15) {
													chunks[i].getBlock(x, y, z).setType(Material.AIR);
													blockCount++;
												}
											}
										}
									}
								}
								count++;
								if (i % 30 == 0) {
									player.sendMessage("Purge Progress: " + Math.round((count / chunkCount) * 100) + "%");
								}
								world.refreshChunk(chunks[i].getX(), chunks[i].getZ());
							}
						} else if (args[0].equals("vines")) {
							for (int i = 0; i < chunkCount; i++) {
								ChunkSnapshot snapChunk = chunks[i].getChunkSnapshot();
								// x - 0-15; y - 0-127; z - 0-15
								for (int y = 0; y <= 127; y++) {
									for (int x = 0; x <= 15; x++) {
										for (int z = 0; z <= 15; z++) {
											int material = snapChunk.getBlockTypeId(x, y, z);

											if (material == Material.SUGAR_CANE_BLOCK.getId()) {
												if (snapChunk.getBlockData(x, y, z) == 15) {
													chunks[i].getBlock(x, y, z).setType(Material.AIR);
													blockCount++;
												}
											}
										}
									}
								}
								count++;
								if (i % 30 == 0) {
									player.sendMessage("Purge Progress: " + Math.round((count / chunkCount) * 100) + "%");
								}
							}
						} else if (args[0].equals("tall_grass")) {
							for (int i = 0; i < chunkCount; i++) {
								ChunkSnapshot snapChunk = chunks[i].getChunkSnapshot();
								// x - 0-15; y - 0-127; z - 0-15
								for (int y = 0; y <= 127; y++) {
									for (int x = 0; x <= 15; x++) {
										for (int z = 0; z <= 15; z++) {
											int material = snapChunk.getBlockTypeId(x, y, z);

											if (material == Material.DEAD_BUSH.getId()) {
												chunks[i].getBlock(x, y, z).setType(Material.AIR);
												blockCount++;
											} else if (material == Material.LONG_GRASS.getId()) {
												chunks[i].getBlock(x, y, z).setType(Material.AIR);
												blockCount++;
											}
										}
									}
								}
								count++;
								if (i % 30 == 0) {
									player.sendMessage("Purge Progress: " + Math.round((count / chunkCount) * 100) + "%");
								}
							}
						} else if (args[0].equals("wild_flowers")) {
							for (int i = 0; i < chunkCount; i++) {
								ChunkSnapshot snapChunk = chunks[i].getChunkSnapshot();
								// x - 0-15; y - 0-127; z - 0-15
								for (int y = 0; y <= 127; y++) {
									for (int x = 0; x <= 15; x++) {
										for (int z = 0; z <= 15; z++) {
											int material = snapChunk.getBlockTypeId(x, y, z);

											if (material == Material.YELLOW_FLOWER.getId() || material == Material.RED_ROSE.getId()) {
												if (snapChunk.getBlockTypeId(x, y - 1, z) == Material.STATIONARY_WATER.getId()
														|| snapChunk.getBlockTypeId(x, y - 1, z) == Material.SAND.getId()) {
													chunks[i].getBlock(x, y, z).setType(Material.AIR);
													blockCount++;
												}
											}
										}
									}
								}
								count++;
								if (i % 30 == 0) {
									player.sendMessage("Purge Progress: " + Math.round((count / chunkCount) * 100) + "%");
								}
							}
						} else {
							vWorld.decreaseActivePlayerCommands();
							return false;
						}

						vWorld.decreaseActivePlayerCommands();

						player.sendMessage("Purge Progress: " + Math.round((count / chunkCount) * 100) + "%");
						player.sendMessage(blockCount + " Blocks purged.");
						return true;
					} else {
						vWorld.decreaseActivePlayerCommands();
						return false;
					}
				}
			} else {
				player.sendMessage("You don't have permission to use this command!");
				return true;
			}
		}
		return false;
	}
}
