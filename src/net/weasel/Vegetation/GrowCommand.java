package net.weasel.Vegetation;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class GrowCommand implements CommandExecutor{

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

			}
		}
		return false;
	}
}
