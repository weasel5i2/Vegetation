package net.weasel.Vegetation;

import java.util.HashSet;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
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

	public void onPlayerInteract( PlayerInteractEvent event )
	{
		if( !(event.getAction() == Action.RIGHT_CLICK_BLOCK) ) return;
		
		Player player = event.getPlayer();
		ItemStack heldItems = player.getItemInHand();
		HashSet<Byte> transparentBlocks = new HashSet<Byte>();
		transparentBlocks.add( (byte)9 ); //stationary water 
		Block targetBlock = player.getTargetBlock(transparentBlocks, 100);
		
		logOutput(""+targetBlock.getType());
		
		if( targetBlock.getType() == Material.DIRT && targetBlock.getRelative(BlockFace.UP).getType() == Material.STATIONARY_WATER )
		{
			if( heldItems.getType() == Material.YELLOW_FLOWER || heldItems.getType() == Material.RED_ROSE )
			{
				event.setCancelled(true);
				for( int I = 0; I < 5; I++ )
				{
					targetBlock = targetBlock.getRelative(BlockFace.UP);
					if( targetBlock.getRelative(BlockFace.UP).getType() == Material.AIR )
					{
						targetBlock.getRelative(BlockFace.UP).setType( heldItems.getType() );
						heldItems.setAmount( heldItems.getAmount() - 1 );
						break;
					}
				}
			}
		}
	}
	
	public void onPlayerQuit( PlayerQuitEvent Event)
	{
		Vegetation.pList.removePlayer( Event.getPlayer() );
	}
	
	public void onPlayerLogin ( PlayerLoginEvent Event )
	{
		Vegetation.pList.addPlayer( Event.getPlayer() );
	}
	
	public void onPlayerMove( PlayerMoveEvent Event )
	{
		if( !Event.getPlayer().isSneaking() )
		{
			try
			{
				Block B = Event.getPlayer().getLocation().getBlock().getRelative(BlockFace.DOWN);
				if( B.getType() == Material.GRASS )
				{
					byte data = B.getData();
					if( data >= 2 ) B.setData( (byte)(data - 2) );
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
}
