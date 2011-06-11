package net.weasel.Vegetation.commands;

import net.weasel.Vegetation.Vegetation;
import net.weasel.Vegetation.VegetationWorld;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class OpPurgeCommand implements CommandExecutor {

	public static void logOutput( String text ) { Vegetation.logOutput( text ); }
	
	public OpPurgeCommand( Vegetation Plugin )
	{

	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args)
	{
		if(sender instanceof Player)
		{
			Player player = (Player)sender;
			
			if ( player.isOp() )
			{
				Chunk[] chunks = player.getWorld().getLoadedChunks();
				int count = 0;
				for(Chunk chunk: chunks)
				{
				    // x - 0-15; y - 0-127; z - 0-15
					for( int y = 0; y < 127; y++ )
						for( int x = 0; x < 15; x++ )
							for( int z = 0; z < 0; z++ )
							{
								Block current = chunk.getBlock(x, y, z);
								Material material = current.getType();
								if( material == Material.GRASS )
								{
									if( current.getData() > 0 )
									{
										current.setData((byte)0);
										count++;
									}
								}
								else if( material == Material.SUGAR_CANE_BLOCK )
								{
									if( current.getData() == 15 )
									{
										current.setType(Material.AIR);
										current.setData((byte)0);
										count++;
									}
								}
							}
				}
				player.sendMessage(count + " Blocks cleared.");
				return true;
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
