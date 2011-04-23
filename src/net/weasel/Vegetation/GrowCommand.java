package net.weasel.Vegetation;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class GrowCommand implements CommandExecutor{

	public static void logOutput( String text ) { Vegetation.logOutput( text ); }
	
	public static final int MaxCycle = 100;
	
	
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
				if( args.length == 1)
				{
					String Arg = args[0];
					Location PL = ((Player) sender).getLocation();
					
					if( Arg.equals("grass") )
					{
						sender.sendMessage( "Growing grass.." );
						for (int I = 0; I < MaxCycle; I++)
						{
							Block CB = Blocks.getRandomBlock( PL );
							if(  CB != null && CB.getType() == Material.GRASS )
								Grass.growGrass( CB );
						}
						return true;
					}
					else if( Arg.equals("flower") )
					{
						sender.sendMessage( "Growing flowers.." );
						for (int I = 0; I < MaxCycle; I++)
						{
							Block CB = Blocks.getRandomBlock( PL );
							if(  CB != null && CB.getType() == Material.GRASS )
							{
								if( I%2 == 0 )
									Plants.spreadPlant( CB, Material.YELLOW_FLOWER );
								else
									Plants.spreadPlant( CB, Material.RED_ROSE );
							}
						}
						return true;
					}
					else if( Arg.equals("mushroom") )
					{
						sender.sendMessage( "Growing mushrooms.." );
						for (int I = 0; I < MaxCycle; I++)
						{
							Block CB = Blocks.getRandomBlock( PL );
							if(  CB != null && CB.getType() == Material.GRASS )
							{
								if( I%2 == 0 )
									Plants.spreadPlant( CB, Material.RED_MUSHROOM);
								else
									Plants.spreadPlant( CB, Material.BROWN_MUSHROOM );
							}
						}
						return true;
					}
					else if( Arg.equals("cactus") )
					{
						sender.sendMessage( "Growing cacti.." );
						for (int I = 0; I < MaxCycle; I++)
						{
							Block CB = Blocks.getRandomBlock( PL );
							if(  CB != null && CB.getType() == Material.GRASS )
								Plants.spreadPlant( CB, Material.CACTUS );
						}
						return true;
					}
					else if( Arg.equals("sugar_cane") )
					{
						sender.sendMessage( "Growing sugar canes.." );
						for (int I = 0; I < MaxCycle; I++)
						{
							Block CB = Blocks.getRandomBlock( PL );
							if(  CB != null && CB.getType() == Material.GRASS )
								Canes.spreadCanes( CB );
						}
						return true;
					}
				}
			}
		}
		return false;
	}
}
