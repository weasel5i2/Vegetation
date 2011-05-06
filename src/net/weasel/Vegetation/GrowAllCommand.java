package net.weasel.Vegetation;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.CommandExecutor;
import org.bukkit.block.Block;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class GrowAllCommand implements CommandExecutor {
	
	
	public static void logOutput( String text ) { Vegetation.logOutput( text ); }
	
	
	public GrowAllCommand( Vegetation Plugin )
	{

	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args)
	{
		if( sender instanceof Player )
		{
			Player P = (Player)sender;
			VegetationWorld vWorld = Vegetation.vWorlds.get(P.getWorld().getName());
			int maxActivePlayerCommands = vWorld.getSettings().maxActivePlayerCommands;
			
			if ( Vegetation.Permissions.has(P, "vegetation.growall") )
			{
				// Todo: implement command queue
				if ( vWorld.getActivePlayerCommands() < maxActivePlayerCommands )
				{
					vWorld.increaseActivePlayerCommands();
					Location L = P.getLocation();
					sender.sendMessage( "Growing everything.." );
					for (int I = 0; I < 500; I++)
					{
						Block CB = vWorld.blocks.getRandomTopBlock( L , Material.AIR );

						if ( CB != null )
						{
							switch ( CB.getType() )
							{
							case GRASS:
								vWorld.grass.growGrass( CB );
								break;

							case CACTUS:
								vWorld.cacti.growCacti( CB );
								break;

							case SUGAR_CANE_BLOCK:
								vWorld.canes.growCanes( CB );
								break;

							case YELLOW_FLOWER:
								vWorld.plants.growPlant( CB, Material.YELLOW_FLOWER );
								break;

							case RED_ROSE:
								vWorld.plants.growPlant( CB, Material.RED_ROSE );
								break;

							case BROWN_MUSHROOM:
								vWorld.plants.growPlant( CB, Material.BROWN_MUSHROOM );
								break;

							case RED_MUSHROOM:
								vWorld.plants.growPlant( CB, Material.RED_MUSHROOM );
								break;

							case PUMPKIN:
								vWorld.plants.growPlant( CB, Material.PUMPKIN );
								break;
								
							case COBBLESTONE:
								vWorld.moss.growMoss( CB );
								break;
								
							case MOSSY_COBBLESTONE:
								vWorld.moss.growMoss( CB );
								break;

							default:
								vWorld.decreaseActivePlayerCommands();
								return false;
							}
						}
					}
					vWorld.decreaseActivePlayerCommands();
					return true;
				}
			}
		}
		return false;
	}
}
