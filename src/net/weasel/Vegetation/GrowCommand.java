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
			VegetationWorld vWorld = Vegetation.vWorlds.get(P.getWorld().getName());
			Settings settings = vWorld.getSettings();

			if ( Vegetation.Permissions.has(P, "vegetation.grow") )
			{
				// Todo: implement command queue
				if ( vWorld.getActivePlayerCommands() < settings.maxActivePlayerCommands )
				{
					vWorld.increaseActivePlayerCommands();
					
					if( args.length == 1 )
					{
						String Arg = args[0];
						int MaxGrowAmount = 0;
						Location PL = ((Player) sender).getLocation();

						if( Arg.equals("flower") )
						{
							sender.sendMessage( "Growing flowers.." );
							MaxGrowAmount = settings.spreadAmountFlowers;
							for (int I = 0; I < MaxCycle; I++)
							{
								Block CB = vWorld.blocks.getRandomTopBlock( PL, Material.GRASS, Material.AIR );
								if( CB != null )
								{
									if( I%2 == 0 )
									{
										if( vWorld.plants.growSinglePlant( CB, Material.YELLOW_FLOWER ) )
											MaxGrowAmount--;
									}
									else
									{
										if( vWorld.plants.growSinglePlant( CB, Material.RED_ROSE ) )
											MaxGrowAmount--;
									}

									if( MaxGrowAmount <= 0 ) break;
								}
							}
							vWorld.decreaseActivePlayerCommands();
							return true;
						}
						else if( Arg.equals("mushroom") )
						{
							sender.sendMessage( "Growing mushrooms.." );
							MaxGrowAmount = settings.spreadAmountFungi;
							for (int I = 0; I < MaxCycle; I++)
							{
								Block CB = vWorld.blocks.getRandomTopBlock( PL, Material.GRASS, Material.AIR );
								if( CB != null )
								{
									if( I%2 == 0 )
									{
										if( vWorld.plants.growSinglePlant( CB, Material.BROWN_MUSHROOM ) )
											MaxGrowAmount--;
									}
									else
									{
										if( vWorld.plants.growSinglePlant( CB, Material.RED_MUSHROOM ) )
											MaxGrowAmount--;
									}
								}

								if( MaxGrowAmount <= 0 ) break;
							}
							vWorld.decreaseActivePlayerCommands();
							return true;
						}
						else if( Arg.equals("cactus") )
						{
							sender.sendMessage( "Growing cacti.." );
							MaxGrowAmount = settings.spreadAmountCacti;
							for (int I = 0; I < MaxCycle; I++)
							{
								Block CB = vWorld.blocks.getRandomTopBlock( PL, Material.SAND, Material.AIR );
								if( CB != null )
								{
									if( vWorld.cacti.growSingleCacti( CB ) )
										MaxGrowAmount--;
								}

								if( MaxGrowAmount <= 0 ) break;
							}
							vWorld.decreaseActivePlayerCommands();
							return true;
						}
						else if( Arg.equals("sugar_cane") )
						{
							sender.sendMessage( "Growing sugar canes.." );
							MaxGrowAmount = settings.spreadAmountSugarCane;
							for (int I = 0; I < MaxCycle; I++)
							{
								Block CB = vWorld.blocks.getRandomTopBlock( PL, Material.GRASS, Material.AIR );
								if( CB != null )
								{
									if( vWorld.canes.growSingleCane( CB ) )
										MaxGrowAmount--;
								}

								if( MaxGrowAmount <= 0 ) break;
							}
							vWorld.decreaseActivePlayerCommands();
							return true;
						}
						else if( Arg.equals("moss") )
						{
							sender.sendMessage( "Growing moss.." );
							MaxGrowAmount = settings.spreadAmountMoss;
							for (int I = 0; I < MaxCycle; I++)
							{
								Block CB = vWorld.blocks.getRandomTopBlock( PL, Material.COBBLESTONE, Material.STATIONARY_WATER );
								if( CB != null )
								{
									if( vWorld.moss.growSingleMoss( CB ))
										MaxGrowAmount--;
								}
								else
								{
									CB = vWorld.blocks.getRandomTopBlock( PL, Material.COBBLESTONE, Material.WATER );
									if( CB != null )
									{
										if( vWorld.moss.growSingleMoss( CB ))
											MaxGrowAmount--;
									}
									else
									{
										CB = vWorld.blocks.getRandomTopBlock( PL, Material.COBBLESTONE, Material.AIR );
										if( CB != null )
										{
											if( vWorld.blocks.isAdjacentofBlockType2( CB  , Material.STATIONARY_WATER )
													|| vWorld.blocks.isAdjacentofBlockType2( CB  , Material.WATER ) )
											{
												if( vWorld.moss.growSingleMoss( CB ))
													MaxGrowAmount--;
											}
										}
									}
								}

								if( MaxGrowAmount <= 0 ) break;
							}
							vWorld.decreaseActivePlayerCommands();
							return true;
						}
						else
						{
							vWorld.decreaseActivePlayerCommands();
							return false;
						}
					}
				}
			}
		}
		return false;
	}
}
