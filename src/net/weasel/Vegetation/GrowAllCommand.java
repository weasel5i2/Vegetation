package net.weasel.Vegetation;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.CommandExecutor;
import org.bukkit.block.Block;
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

			if ( Vegetation.Permissions.has(P, "vegetation.growall") )
			{
				// Todo: implement command queue
				if ( Vegetation.ActivePlayerCommands < 20 )
				{
					Vegetation.ActivePlayerCommands++;
					sender.sendMessage( "Growing everything.." );
					for (int I = 0; I < 100; I++)
					{
						Block CB = Blocks.getRandomBlock(((Player) sender).getLocation());

						if ( CB != null )
						{
							switch ( CB.getType() )
							{
							case GRASS:
								Grass.growGrass( CB );
								break;

							case CACTUS:
								Cacti.growCacti( CB );
								break;

							case SUGAR_CANE_BLOCK:
								Canes.GrowCanes( CB );
								break;

							case YELLOW_FLOWER:
								Plants.growPlant( CB, Material.YELLOW_FLOWER );
								break;

							case RED_ROSE:
								Plants.growPlant( CB, Material.RED_ROSE );
								break;

							case BROWN_MUSHROOM:
								Plants.growPlant( CB, Material.BROWN_MUSHROOM );
								break;

							case RED_MUSHROOM:
								Plants.growPlant( CB, Material.RED_MUSHROOM );
								break;

							case PUMPKIN:
								Plants.growPlant( CB, Material.PUMPKIN );
								break;
								
							case COBBLESTONE:
								Moss.GrowMoss( CB );
								break;
								
							case MOSSY_COBBLESTONE:
								Moss.GrowMoss( CB );
								break;

							default:
								return false;
							}
						}
					}
					return true;
				}
			}
		}
		return false;
	}
}
