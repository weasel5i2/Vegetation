package net.weasel.Vegetation.commands;

import net.weasel.Vegetation.Settings;
import net.weasel.Vegetation.Vegetation;
import net.weasel.Vegetation.VegetationWorld;

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
						Location playerLocation = ((Player) sender).getLocation();
						int maxGrowAmount = 0;
						String Arg = args[0];

						if( Arg.equals("flower") )
						{
							sender.sendMessage("Growing flowers..");
							maxGrowAmount = settings.spreadAmountFlowers;
							Material[] plantableBlocks = { Material.GRASS, Material.SAND, Material.STATIONARY_WATER };
							int r = 0;
							
							for (int I = 0; I < MaxCycle; I++)
							{
								r = Vegetation.generator.nextInt(plantableBlocks.length);
								Block currentBlock = vWorld.blocks.getRandomTopBlock(playerLocation, plantableBlocks[r], Material.AIR);
								if( currentBlock != null )
								{
									if( I%2 == 0 )
									{
										if( vWorld.plants.growSinglePlant(currentBlock, Material.YELLOW_FLOWER) )
											maxGrowAmount--;
									}
									else
									{
										if( vWorld.plants.growSinglePlant(currentBlock, Material.RED_ROSE) )
											maxGrowAmount--;
									}

									if( maxGrowAmount <= 0 ) break;
								}
							}
							vWorld.decreaseActivePlayerCommands();
							return true;
						}
						else if( Arg.equals("mushroom") )
						{
							sender.sendMessage( "Growing mushrooms.." );
							maxGrowAmount = settings.spreadAmountFungi;
							for (int I = 0; I < MaxCycle; I++)
							{
								Block CB = vWorld.blocks.getRandomTopBlock( playerLocation, Material.GRASS, Material.AIR );
								if( CB != null )
								{
									if( I%2 == 0 )
									{
										if( vWorld.plants.growSinglePlant( CB, Material.BROWN_MUSHROOM ) )
											maxGrowAmount--;
									}
									else
									{
										if( vWorld.plants.growSinglePlant( CB, Material.RED_MUSHROOM ) )
											maxGrowAmount--;
									}
								}

								if( maxGrowAmount <= 0 ) break;
							}
							vWorld.decreaseActivePlayerCommands();
							return true;
						}
						else if( Arg.equals("cactus") )
						{
							sender.sendMessage( "Growing cacti.." );
							maxGrowAmount = settings.spreadAmountCacti;
							for (int I = 0; I < MaxCycle; I++)
							{
								Block CB = vWorld.blocks.getRandomTopBlock( playerLocation, Material.SAND, Material.AIR );
								if( CB != null )
								{
									if( vWorld.cacti.growSingleCacti( CB ) )
										maxGrowAmount--;
								}

								if( maxGrowAmount <= 0 ) break;
							}
							vWorld.decreaseActivePlayerCommands();
							return true;
						}
						else if( Arg.equals("sugar_cane") )
						{
							sender.sendMessage( "Growing sugar canes.." );
							maxGrowAmount = settings.spreadAmountSugarCane;
							for (int I = 0; I < MaxCycle; I++)
							{
								Block CB = vWorld.blocks.getRandomTopBlock( playerLocation, Material.GRASS, Material.AIR );
								if( CB != null )
								{
									if( vWorld.canes.growSingleCane( CB ) )
										maxGrowAmount--;
								}

								if( maxGrowAmount <= 0 ) break;
							}
							vWorld.decreaseActivePlayerCommands();
							return true;
						}
						else if( Arg.equals("moss") )
						{
							sender.sendMessage( "Growing moss.." );
							maxGrowAmount = settings.spreadAmountMoss;
							for (int I = 0; I < MaxCycle; I++)
							{
								Block CB = vWorld.blocks.getRandomTopBlock( playerLocation, Material.COBBLESTONE, Material.STATIONARY_WATER );
								if( CB != null )
								{
									if( vWorld.moss.growSingleMoss( CB ))
										maxGrowAmount--;
								}
								else
								{
									CB = vWorld.blocks.getRandomTopBlock( playerLocation, Material.COBBLESTONE, Material.WATER );
									if( CB != null )
									{
										if( vWorld.moss.growSingleMoss( CB ))
											maxGrowAmount--;
									}
									else
									{
										CB = vWorld.blocks.getRandomTopBlock( playerLocation, Material.COBBLESTONE, Material.AIR );
										if( CB != null )
										{
											if( vWorld.blocks.isAdjacentofBlockType2( CB  , Material.STATIONARY_WATER )
													|| vWorld.blocks.isAdjacentofBlockType2( CB  , Material.WATER ) )
											{
												if( vWorld.moss.growSingleMoss( CB ))
													maxGrowAmount--;
											}
										}
									}
								}

								if( maxGrowAmount <= 0 ) break;
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
