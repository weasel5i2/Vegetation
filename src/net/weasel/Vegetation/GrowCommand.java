package net.weasel.Vegetation;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.CommandExecutor;
import org.bukkit.block.Block;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class GrowCommand implements CommandExecutor {
	
	
	public static void logOutput( String text ) { Vegetation.logOutput( text ); }
	
	
	public GrowCommand( Vegetation Plugin )
	{

	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args)
	{
		if( sender instanceof Player )
		{
			Player P = (Player)sender;

			if ( Vegetation.Permissions.has(P, "vegetation.grow") )
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
								if (Vegetation.debugging) logOutput( "Found Block of Type: " + Material.GRASS );
								Grass.growGrass( CB );
								break;

							case CACTUS:
								if( Vegetation.debugging ) logOutput( "Found Block of Type: " + Material.CACTUS );
								Cacti.growCacti( CB );
								break;

							case SUGAR_CANE_BLOCK:
								if( Vegetation.debugging ) logOutput( "Found Block of Type: " + Material.SUGAR_CANE_BLOCK );
								Cranes.GrowCranes( CB );
								break;

							case YELLOW_FLOWER:
								if( Vegetation.debugging ) logOutput( "Found Block of Type: " + Material.YELLOW_FLOWER );
								Plants.growPlant( CB, Material.YELLOW_FLOWER );
								break;

							case RED_ROSE:
								if( Vegetation.debugging ) logOutput( "Found Block of Type: " + Material.RED_ROSE );
								Plants.growPlant( CB, Material.RED_ROSE );
								break;

							case BROWN_MUSHROOM:
								if ( Vegetation.debugging ) logOutput( "Found Block of Type: " + Material.BROWN_MUSHROOM );
								Plants.growPlant( CB, Material.BROWN_MUSHROOM );
								break;

							case RED_MUSHROOM:
								if ( Vegetation.debugging ) logOutput( "Found Block of Type: " + Material.RED_MUSHROOM );
								Plants.growPlant( CB, Material.RED_MUSHROOM );
								break;

							case PUMPKIN:
								if (Vegetation.debugging) logOutput( "Found Block of Type: " + Material.PUMPKIN );
								Plants.growPlant( CB, Material.PUMPKIN );
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
