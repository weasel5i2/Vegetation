package net.weasel.Vegetation;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockListener;
import org.bukkit.event.block.BlockPhysicsEvent;

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
			
			// Get the sugar cane block nearest to ground
			for( int i = 0; i < 3; i++ )
			{
				if( world.getBlockTypeIdAt(x, y - i, z) == Material.SUGAR_CANE_BLOCK.getId() )
				{
					world.getBlockAt(x, y - i, z).setType(Material.AIR);
				}
				else break;
			}
			
			for( int i = 0; i < 3; i++ )
			{
				if( world.getBlockTypeIdAt(x, y + i, z) == Material.SUGAR_CANE_BLOCK.getId() )
				{
					world.getBlockAt(x, y + i, z).setType(Material.AIR);
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
	
}
