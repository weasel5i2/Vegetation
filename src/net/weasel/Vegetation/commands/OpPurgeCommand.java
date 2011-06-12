package net.weasel.Vegetation.commands;

import net.weasel.Vegetation.Settings;
import net.weasel.Vegetation.Vegetation;
import net.weasel.Vegetation.VegetationWorld;

import org.bukkit.Chunk;
import org.bukkit.ChunkSnapshot;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class OpPurgeCommand implements CommandExecutor {

	public static void logOutput( String text ) { Vegetation.logOutput( text ); }
	
	private Vegetation plugin;
	
	public OpPurgeCommand( Vegetation instance )
	{
		this.plugin = instance;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args)
	{
		if(sender instanceof Player)
		{
			Player player = (Player)sender;
			VegetationWorld vWorld = Vegetation.vWorlds.get(player.getWorld().getName());
			Settings settings = vWorld.getSettings();
			
			if ( player.isOp() )
			{
				if ( vWorld.getActivePlayerCommands() < settings.maxActivePlayerCommands )
				{
					vWorld.increaseActivePlayerCommands();
					
					player.sendMessage("Starting purge");
					Chunk[] chunks = player.getWorld().getLoadedChunks();
					int count = 0;
					int blockCount = 0;
					float chunkCount = chunks.length;
					
					for( int i = 0; i < chunkCount; i++ )
					{
						ChunkSnapshot snapChunk = chunks[i].getChunkSnapshot();
					    // x - 0-15; y - 0-127; z - 0-15
						for( int y = 0; y <= 127; y++ )
						{
							for( int x = 0; x <= 15; x++ )
							{
								for( int z = 0; z <= 15; z++ )
								{											
									int material = snapChunk.getBlockTypeId(x, y, z);
									
									if( material == Material.GRASS.getId() )
									{
										if( snapChunk.getBlockData(x, y, z) > 0 )
										{
											chunks[i].getBlock(x, y, z).setData((byte)0);
											blockCount++;
										}
									}
									else if( material == Material.SUGAR_CANE_BLOCK.getId() )
									{
										if( snapChunk.getBlockData(x, y, z) == 15 )
										{
											chunks[i].getBlock(x, y, z).setType(Material.AIR);
											chunks[i].getBlock(x, y, z).setData((byte)0);
											blockCount++;
										}
									}
								}
							}
						}
						count++;
						if( i % 30 == 0 )
						{
							player.sendMessage("Purge Progress: " + Math.round((count/chunkCount) * 100) + "%");
						}
					}
					
					vWorld.decreaseActivePlayerCommands();
					
					player.sendMessage("Purge Progress: " + Math.round((count/chunkCount) * 100) + "%");
					player.sendMessage(blockCount + " Blocks purged.");
					return true;
				}
			}
			else
			{
				player.sendMessage("You don't have permission to use this command!");
				return true;
			}
		}
		return false;
	}
}