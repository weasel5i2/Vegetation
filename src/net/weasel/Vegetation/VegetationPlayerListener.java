package net.weasel.Vegetation;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.inventory.ItemStack;

public class VegetationPlayerListener extends PlayerListener
{
	public static void logOutput( String text ) { Vegetation.logOutput( text ); }

	public static Vegetation plugin;
	
	public VegetationPlayerListener( Vegetation instance ) 
	{
		plugin = instance;
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

		}
		catch( Exception e )
		{
			Vegetation.getActivePlayerList();
		}
	}
	
	public void onPlayerLogin ( PlayerLoginEvent Event )
	{
		try
		{
			if( !Vegetation.playerList.contains( Event.getPlayer().getName() ) )
				Vegetation.playerList.add( Event.getPlayer().getName() );
		}
		catch( Exception e )
		{
			logOutput( "Something went wrong during PlayerList Login Event" );
		}
	}
	
	public void onPlayerMove ( PlayerMoveEvent Event )
	{
		try
		{
			Block B = Event.getPlayer().getLocation().getBlock().getRelative(BlockFace.DOWN);
			if( B.getType() == Material.GRASS )
			{
				byte data = B.getData();
				if( data >= 2 ) B.setData( (byte)(data - 1) );
			}
		}
		catch( Exception e )
		{
			logOutput( "OnPlayerMove Exception: " + e );
			logOutput( "Disabling trampleGrass");
			Vegetation.trampleGrass = false;
			Vegetation.plugin.getPluginLoader().disablePlugin(Vegetation.plugin);
			Vegetation.plugin.getPluginLoader().enablePlugin(Vegetation.plugin);
		}
	}
}
