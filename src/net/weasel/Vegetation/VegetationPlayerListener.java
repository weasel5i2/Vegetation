package net.weasel.Vegetation;

import java.util.HashSet;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;

public class VegetationPlayerListener extends PlayerListener {
	private final Vegetation plugin;

	public VegetationPlayerListener(final Vegetation instance) {
		plugin = instance;
	}

	@Override
	public void onPlayerInteract(PlayerInteractEvent event) {
		if (!(event.getAction() == Action.RIGHT_CLICK_BLOCK))
			return;

		Player player = event.getPlayer();
		World world = player.getWorld();
		ItemStack heldItem = player.getItemInHand();
		Material heldItemType = heldItem.getType();
		if (heldItemType == Material.WOOD_HOE || heldItemType == Material.STONE_HOE || heldItemType == Material.IRON_HOE
				|| heldItemType == Material.GOLD_HOE || heldItemType == Material.DIAMOND_HOE) {
			Block targetBlock = player.getTargetBlock(null, 100);
			byte data = targetBlock.getData();
			if (targetBlock.getType() == Material.GRASS && data > 1) {
				event.setCancelled(true);
				if (data - 3 < 0) {
					targetBlock.setData((byte) 0);
				} else {
					targetBlock.setData((byte) (data - 3));
				}
			}
		} else if (heldItemType == Material.YELLOW_FLOWER || heldItemType == Material.RED_ROSE) {
			HashSet<Byte> transparentBlocks = new HashSet<Byte>();
			transparentBlocks.add((byte) Material.AIR.getId());
			transparentBlocks.add((byte) Material.STATIONARY_WATER.getId());
			transparentBlocks.add((byte) Material.YELLOW_FLOWER.getId());
			transparentBlocks.add((byte) Material.RED_ROSE.getId());

			Block targetBlock = player.getTargetBlock(transparentBlocks, 100);
			Location location = targetBlock.getLocation();

			int x = (int) location.getX();
			int y = (int) location.getY();
			int z = (int) location.getZ();

			if (world.getBlockTypeIdAt(x, y + 1, z) == Material.STATIONARY_WATER.getId()) {
				event.setCancelled(true);
				int currentBlockType;
				for (int i = 2; i < 5; i++) {
					currentBlockType = world.getBlockTypeIdAt(x, y + i, z);
					if (currentBlockType == Material.AIR.getId()) {
						targetBlock = world.getBlockAt(x, y + i, z);
						targetBlock.setType(heldItemType);
						heldItem.setAmount(heldItem.getAmount() - 1);
						if (heldItem.getAmount() == 0)
							player.setItemInHand(null);
						break;
					} else if (currentBlockType != Material.STATIONARY_WATER.getId()) {
						break;
					}
				}
			}
		}
	}

	@Override
	public void onPlayerQuit(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		Vegetation.lf.write("[Event] Player [" + player.getName() + "] quit - World [" + player.getWorld().getName() + "]");
		VegetationWorld vWorld = plugin.vWorlds.get(player.getWorld().getName());
		if (vWorld != null)
			vWorld.playerList.removePlayer(player);
	}

	@Override
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		Vegetation.lf.write("[Event] Player [" + player.getName() + "] logged in - World [" + player.getWorld().getName() + "]");
		VegetationWorld vWorld = plugin.vWorlds.get(player.getWorld().getName());
		if (vWorld != null) {
			// vWorld.playerList.addPlayer(player);
			// vWorld.playerList.getVegetationplayer(player.getName()).setLastBlockPosition(player.getLocation().getBlock());
			VegetationPlayer vPlayer = new VegetationPlayer(player.getName());
			vPlayer.setLastBlockPosition(player.getLocation().getBlock());
			vWorld.playerList.addPlayer(vPlayer);
		}
	}

	@Override
	public void onPlayerMove(PlayerMoveEvent event) {
		Player player = event.getPlayer();
		VegetationWorld vWorld = plugin.vWorlds.get(player.getWorld().getName());
		if (vWorld != null) {
			if (vWorld.getSettings() != null) {
				boolean trampleGrass = vWorld.getSettings().trampleGrass;
				if (trampleGrass) {
					if (!player.isSneaking()) {
						Block block = player.getLocation().getBlock().getRelative(BlockFace.DOWN);
						VegetationPlayer vPlayer = vWorld.playerList.getVegetationplayer(player.getName());
						if (vPlayer != null) {
							if (block != vPlayer.getLastBlockPosition()) {
								vPlayer.setLastBlockPosition(block);
								if (block.getType() == Material.GRASS) {
									byte data = block.getData();
									// if( data > 0 && data - 2 > 0 )
									// block.setData((byte)(data - 2));
									// else block.setData((byte)0);
									// if( data > 0 ) block.setData((byte)(data -
									// 1));
									if (data > 0)
										block.setTypeIdAndData(Material.GRASS.getId(), (byte) (data - 1), true);
								}
							}
						} else {
							vPlayer.setLastBlockPosition(block);
						}
					}
				}
			}
		}
	}

	@Override
	public void onPlayerTeleport(PlayerTeleportEvent event) {
		String oldWorld = event.getFrom().getWorld().getName();
		String currentWorld = event.getTo().getWorld().getName();

		if (!oldWorld.equals(currentWorld)) {
			Player player = event.getPlayer();
			Vegetation.lf.write("[Event] Player [" + player.getName() + "] teleported - World [" + event.getFrom().getWorld().getName() + "] -> ["
					+ event.getTo().getWorld().getName() + "]");
			VegetationWorld old = plugin.vWorlds.get(oldWorld);
			VegetationWorld current = plugin.vWorlds.get(currentWorld);

			if (old != null && current != null) {
				VegetationPlayer vPlayer = old.playerList.getVegetationplayer(player.getName());
				vPlayer.setLastBlockPosition(player.getLocation().getBlock());
				PlayerList.transferPlayer(vPlayer, old, current);
			}
		}
	}
}
