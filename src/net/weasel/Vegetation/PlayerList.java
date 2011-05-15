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
	private Mutex mutex;
	
	public PlayerList(Vegetation p, World w)
	{
		plugin = p;
		world = w;
		players = new ArrayList<String>();
		posIndex = 0;
		mutex = new Mutex();
	}
	
	public void getActivePlayerList()
	{
		mutex.aquire();
		
		players.clear();
		
		for( Player p: world.getPlayers() )
		{
			players.add(p.getName());
		}
		
		posIndex = 0;
		
		mutex.release();
	}
	
	public Player getNextPlayer()
	{
		mutex.aquire();
		
		if( posIndex > (players.size() - 1) ) posIndex = 0;
		
		Player player = null;
		
		//get next position
		if( posIndex <= (players.size() - 1) )
		{
			player = plugin.getServer().getPlayer(players.get(posIndex));
			posIndex++;
		}
		// return nothing
		else if( players.size() == 0 )
		{
			posIndex = 0;
		}
		
		mutex.release();
		return player;
	}
	
	public void addPlayer(Player player)
	{
		mutex.aquire();
		
		if( !(players.contains(player.getName())) )
		{
			players.add(player.getName());
		}
		
		mutex.release();
	}
	
	public void removePlayer(Player player)
	{
		mutex.aquire();
		
		if( players.contains(player.getName()) )
		{
			players.remove(player.getName());
			players.trimToSize();
		}
		
		mutex.release();
	}
}
