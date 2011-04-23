package net.weasel.Vegetation;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.CommandExecutor;
import org.bukkit.entity.Player;

public class MowCommand implements CommandExecutor{

	public static void logOutput( String text ) { Vegetation.logOutput( text ); }
	
	public MowCommand( Vegetation Plugin )
	{

	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args)
	{
		if(sender instanceof Player)
		{
			Player P = (Player)sender;
			if ( Vegetation.Permissions.has(P, "vegetation.mow") )
			{
				// Todo: implement command queue
				if( Vegetation.ActivePlayerCommands < 20 )
				{
					Grass.MowGrass( P.getLocation() , Vegetation.generator.nextInt(10) );
					sender.sendMessage( "You have cut down the grass." );
					return true;
				}
			}
		}
		return false;
	}
}