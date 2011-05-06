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
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;

public class VegetationPlayerListener extends PlayerListener
{
	public static void logOutput( String text ) { Vegetation.logOutput( text ); }

	public static Vegetation plugin;
	
	public VegetationPlayerListener( Vegetation instance ) 
	{
		plugin = instance;
	}

	public void onPlayerInteract(PlayerInteractEvent event)
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
	
	public void onPlayerQuit(PlayerQuitEvent event)
	{
		Player p = event.getPlayer();
		VegetationWorld vWorld = Vegetation.vWorlds.get(p.getWorld().getName());
		if( vWorld != null ) vWorld.playerList.removePlayer(p);
	}
	
	public void onPlayerLogin (PlayerLoginEvent event)
	{
		Player p = event.getPlayer();
		VegetationWorld vWorld = Vegetation.vWorlds.get(p.getWorld().getName());
		if( vWorld != null ) vWorld.playerList.addPlayer(p);
	}
	
	public void onPlayerMove(PlayerMoveEvent event)
	{
		Player player = event.getPlayer();
		VegetationWorld vWorld = Vegetation.vWorlds.get(player.getWorld().getName());
		if( vWorld != null )
		{
			boolean trampleGrass = vWorld.getSettings().trampleGrass;
			if( !trampleGrass )
			{
				if( !player.isSneaking() )
				{
					Block B = player.getLocation().getBlock().getRelative(BlockFace.DOWN);
					if( B.getType() == Material.GRASS )
					{
						byte data = B.getData();
						if( data >= 2 ) B.setData( (byte)(data - 2) );
					}
				}
			}
		}
	}
	
	public void onPlayerTeleport(PlayerTeleportEvent event)
	{
		String oldWorld = event.getFrom().getWorld().getName();
		String currentWorld = event.getTo().getWorld().getName();
		
		if( !oldWorld.equals(currentWorld) )
		{
			Player player = event.getPlayer();
			VegetationWorld old = Vegetation.vWorlds.get(oldWorld);
			VegetationWorld current = Vegetation.vWorlds.get(currentWorld);
			
			if( old != null && current != null )
			{
				old.playerList.removePlayer(player);
				current.playerList.addPlayer(player);
			}
		}
	}
}
