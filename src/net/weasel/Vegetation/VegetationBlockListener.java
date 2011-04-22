package net.weasel.Vegetation;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockListener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

public class VegetationBlockListener extends BlockListener
{
	public static void logOutput( String text ) { Vegetation.logOutput( text ); }

	public static Vegetation plugin;
	
	public VegetationBlockListener( Vegetation instance ) 
	{
		plugin = instance;
	}
	public void OnBlockPlaced( BlockPlaceEvent e )
	{
		if( e.getBlockPlaced().getTypeId() == 37 || e.getBlockPlaced().getTypeId() == 38 )
		{
			// logOutput( "Block placed(): " + e.getPlayer().getName() );
			Player player = e.getPlayer();
			ItemStack heldItems = player.getItemInHand();

			if( heldItems.getTypeId() == 37 || heldItems.getTypeId() == 38 )
			{
				Block targetBlock = player.getTargetBlock(null, 100);

				if( targetBlock.getRelative(BlockFace.UP).getTypeId() == 0 )
				{
					targetBlock.getRelative(BlockFace.UP).setTypeIdAndData(heldItems.getTypeId(), (byte)1, true );
					heldItems.setAmount(heldItems.getAmount() - 1 );
					
					e.setCancelled( true );
				}
			}
		}
	}
	
}
