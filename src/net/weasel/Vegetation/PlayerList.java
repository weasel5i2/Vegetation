package net.weasel.Vegetation;

import java.util.ArrayList;

import org.bukkit.World;
import org.bukkit.entity.Player;

public class PlayerList {

	public void logOutput( String text ) { Vegetation.logOutput( text ); }
	
	private Vegetation plugin;
	private World world;
	private ArrayList<String> players;
	private int posIndex;
	
	public PlayerList(Vegetation p, World w)
	{
		plugin = p;
		world = w;
		players = new ArrayList<String>();
		posIndex = 0;
	}
	
	public synchronized void getActivePlayerList()
	{
		players.clear();
		
		for( Player p: world.getPlayers() )
		{
			players.add(p.getName());
		}
		
		posIndex = 0;
	}
	
	public synchronized Player getNextPlayer()
	{
		//logOutput("PlayerList: " + world.getName() + " - " + players.size());
		
		Player player = null;
		
		//get next position
		if( posIndex <= (players.size() - 1) )
		{
			player = plugin.getServer().getPlayer(players.get(posIndex));
			posIndex++;
		}
		// reset to first position and return that player
		else if( posIndex > (players.size() - 1))
		{
			posIndex = 0;
			player = plugin.getServer().getPlayer(players.get(posIndex));
			posIndex++;
		}
		// return nothing
		else if( players.size() == 0 )
		{
			posIndex = 0;
		}
		//default
		else
		{
			posIndex = 0;
		}
		return player;
	}
	
	public synchronized void addPlayer(Player player)
	{
		if( !(players.contains(player.getName())) )
		{
			players.add(player.getName());
		}
	}
	
	public synchronized void removePlayer(Player player)
	{
		if( players.contains(player.getName()) )
		{
			players.remove(player.getName());
			players.trimToSize();
		}
	}
}
