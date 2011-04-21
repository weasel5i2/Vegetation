package net.weasel.Vegetation;

import org.bukkit.Server;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.inventory.ItemStack;

public class PlayerListeners extends PlayerListener
{
	public static void logOutput( String text ) { Vegetation.logOutput( text ); }

	public Vegetation plugin;
	public Server server;
	
	public PlayerListeners( Vegetation instance ) 
	{
		plugin = instance;
		server = Vegetation.server;
	}

	public void onPlayerItem( PlayerInteractEvent e )
	{
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
		else
			e.setCancelled( false );
		
		return;
	}
	
	public void onPlayerQuit( PlayerQuitEvent event)
	{
		try
		{
			if( Vegetation.playerList.contains( event.getPlayer() ) )
				Vegetation.playerList.remove( event.getPlayer() );

			if( Vegetation.overGrower != null )
			{
				if( event.getPlayer() == Vegetation.overGrower )
				{
					Vegetation.overGrowTicks = 0;
					Vegetation.overGrower = null;
					Vegetation.logOutput( "Overgrower has quit the game. Cancelling overgrowth loop." );
				}
			}
		}
		catch( Exception e )
		{
			// Do nothing..
		}
	}
}
