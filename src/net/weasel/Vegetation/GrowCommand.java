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
	
	public static final int MaxCycle = 150;
	
	
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
				if ( Vegetation.ActivePlayerCommands < Vegetation.maxActivePlayerCommands )
				{
					Vegetation.ActivePlayerCommands++;
					
					if( args.length == 1 )
					{
						String Arg = args[0];
						int MaxGrowAmount = 0;
						Location PL = ((Player) sender).getLocation();

						if( Arg.equals("flower") )
						{
							sender.sendMessage( "Growing flowers.." );
							MaxGrowAmount = Vegetation.spreadAmountFlowers;
							for (int I = 0; I < MaxCycle; I++)
							{
								Block CB = Blocks.getRandomTopBlock( PL, Material.GRASS, Material.AIR );
								if( CB != null )
								{
									if( I%2 == 0 )
									{
										if( Plants.growSinglePlant( CB, Material.YELLOW_FLOWER ) )
											MaxGrowAmount--;
									}
									else
									{
										if( Plants.growSinglePlant( CB, Material.RED_ROSE ) )
											MaxGrowAmount--;
									}

									if( MaxGrowAmount <= 0 ) break;
								}
							}
							Vegetation.ActivePlayerCommands--;
							return true;
						}
						else if( Arg.equals("mushroom") )
						{
							sender.sendMessage( "Growing mushrooms.." );
							MaxGrowAmount = Vegetation.spreadAmountFungi;
							for (int I = 0; I < MaxCycle; I++)
							{
								Block CB = Blocks.getRandomTopBlock( PL, Material.GRASS, Material.AIR );
								if( CB != null )
								{
									if( I%2 == 0 )
									{
										if( Plants.growSinglePlant( CB, Material.BROWN_MUSHROOM ) )
											MaxGrowAmount--;
									}
									else
									{
										if( Plants.growSinglePlant( CB, Material.RED_MUSHROOM ) )
											MaxGrowAmount--;
									}
								}

								if( MaxGrowAmount <= 0 ) break;
							}
							Vegetation.ActivePlayerCommands--;
							return true;
						}
						else if( Arg.equals("cactus") )
						{
							sender.sendMessage( "Growing cacti.." );
							MaxGrowAmount = Vegetation.spreadAmountCacti;
							for (int I = 0; I < MaxCycle; I++)
							{
								Block CB = Blocks.getRandomTopBlock( PL, Material.SAND, Material.AIR );
								if( CB != null )
								{
									if( Cacti.growSingleCacti( CB ) )
										MaxGrowAmount--;
								}

								if( MaxGrowAmount <= 0 ) break;
							}
							Vegetation.ActivePlayerCommands--;
							return true;
						}
						else if( Arg.equals("sugar_cane") )
						{
							sender.sendMessage( "Growing sugar canes.." );
							MaxGrowAmount = Vegetation.spreadAmountSugarCane;
							for (int I = 0; I < MaxCycle; I++)
							{
								Block CB = Blocks.getRandomTopBlock( PL, Material.GRASS, Material.AIR );
								if( CB != null )
								{
									if( Canes.growSingleCane( CB ) )
										MaxGrowAmount--;
								}

								if( MaxGrowAmount <= 0 ) break;
							}
							Vegetation.ActivePlayerCommands--;
							return true;
						}
						else if( Arg.equals("moss") )
						{
							sender.sendMessage( "Growing moss.." );
							MaxGrowAmount = Vegetation.spreadAmountMoss;
							for (int I = 0; I < MaxCycle; I++)
							{
								Block CB = Blocks.getRandomTopBlock( PL, Material.COBBLESTONE, Material.STATIONARY_WATER );
								if( CB != null )
								{
									if( Moss.growSingleMoss( CB ))
										MaxGrowAmount--;
								}
								else
								{
									CB = Blocks.getRandomTopBlock( PL, Material.COBBLESTONE, Material.WATER );
									if( CB != null )
									{
										if( Moss.growSingleMoss( CB ))
											MaxGrowAmount--;
									}
									else
									{
										CB = Blocks.getRandomTopBlock( PL, Material.COBBLESTONE, Material.AIR );
										if( CB != null )
										{
											if( Blocks.isAdjacentBlockofType2( CB  , Material.STATIONARY_WATER )
													|| Blocks.isAdjacentBlockofType2( CB  , Material.WATER ) )
											{
												if( Moss.growSingleMoss( CB ))
													MaxGrowAmount--;
											}
										}
									}
								}

								if( MaxGrowAmount <= 0 ) break;
							}
							Vegetation.ActivePlayerCommands--;
							return true;
						}
						else
						{
							Vegetation.ActivePlayerCommands--;
							return false;
						}
					}
				}
			}
		}
		return false;
	}
}
