package net.weasel.Vegetation;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockListener;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.block.LeavesDecayEvent;

public class VegetationBlockListener extends BlockListener
{
	public void logOutput(String text) { Vegetation.logOutput(text); }

	private final Vegetation plugin;
	
	public VegetationBlockListener(final Vegetation instance) 
	{
		plugin = instance;
	}

	@Override
	public void onBlockBreak(BlockBreakEvent event)
	{
		Block block = event.getBlock();
		if( block.getType() == Material.SUGAR_CANE_BLOCK && block.getData() == 15 )
		{
			event.setCancelled(true);
			
			Location location = block.getLocation();
			World world = event.getPlayer().getWorld();
			
			int x = (int)location.getX();
			int y = (int)location.getY();
			int z = (int)location.getZ();
			
			for( int i = 0; i < 20; i++ )
			{
				if( world.getBlockTypeIdAt(x, y - i, z) == Material.SUGAR_CANE_BLOCK.getId() )
				{
					world.getBlockAt(x, y - i, z).setType(Material.AIR);
				}
				else break;
			}
		}
	}
	
	@Override
	public void onBlockPhysics(BlockPhysicsEvent event)
	{
		Block block = event.getBlock();
		if( block.getType() == Material.SUGAR_CANE_BLOCK && block.getData() == 15 )
		{
			event.setCancelled(true);
		}
	}
	
	@Override
	public void onLeavesDecay(LeavesDecayEvent event)
	{
		Block block = event.getBlock();
		if( block.getRelative(BlockFace.DOWN).getType() == Material.SUGAR_CANE_BLOCK && block.getRelative(BlockFace.DOWN).getData() == 15 )
		{
			for( int i = 0; i < 20; i++ )
			{
				block = block.getRelative(BlockFace.DOWN);
				if( block.getType() == Material.SUGAR_CANE_BLOCK && block.getData() == 15 )
				{
					block.setType(Material.AIR);
				}
			}
		}
	}
	
	@Override
	public void onBlockBurn(BlockBurnEvent event)
	{
		Block block = event.getBlock();
		if( block.getRelative(BlockFace.DOWN).getType() == Material.SUGAR_CANE_BLOCK && block.getRelative(BlockFace.DOWN).getData() == 15 )
		{
			for( int i = 0; i < 20; i++ )
			{
				block = block.getRelative(BlockFace.DOWN);
				if( block.getType() == Material.SUGAR_CANE_BLOCK && block.getData() == 15 )
				{
					block.setType(Material.AIR);
				}
			}
		}
	}
	
}
