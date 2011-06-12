package net.weasel.Vegetation.commands;

import net.weasel.Vegetation.Vegetation;
import net.weasel.Vegetation.VegetationWorld;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.CommandExecutor;
import org.bukkit.entity.Player;

public class OpMowCommand implements CommandExecutor {

	public static void logOutput( String text ) { Vegetation.logOutput( text ); }
	
	private Vegetation plugin;
	
	public OpMowCommand(Vegetation instance)
	{
		this.plugin = instance;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args)
	{
		if(sender instanceof Player)
		{
			Player P = (Player)sender;
			VegetationWorld vWorld = Vegetation.vWorlds.get(P.getWorld().getName());
			int maxActivePlayerCommands = vWorld.getSettings().maxActivePlayerCommands;
			
			if ( P.isOp() )
			{
				// Todo: implement command queue
				if( vWorld.getActivePlayerCommands() < maxActivePlayerCommands )
				{
					vWorld.increaseActivePlayerCommands();
					vWorld.grass.mowGrass( P.getLocation() );
					P.sendMessage( "You have cut down the grass." );
					vWorld.decreaseActivePlayerCommands();
					return true;
				}
			}
			else
			{
				P.sendMessage("You don't have permission to use this command!");
				return true;
			}
		}
		return false;
	}
}
