package net.weasel.Vegetation;

import java.util.ArrayList;

import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class PlayerList {

	public void logOutput( String text ) { Vegetation.logOutput( text ); }
	
	private Vegetation plugin;
	private World world;
	private ArrayList<String> players;
	private boolean mutex;
	private int posIndex;
	
	public PlayerList(Vegetation p, World w)
	{
		plugin = p;
		world = w;
		players = new ArrayList<String>();
		mutex = false;
		posIndex = 0;
	}
	
	public void getActivePlayerList()
	{
		lock();
		
		players.clear();
		
		for( Player p: world.getPlayers() )
		{
			players.add(p.getName());
		}
		
		posIndex = 0;
		
		unlock();
	}
	
	public Player getNextPlayer()
	{
		lock();
		
		/*for( int i = 0; i < players.size(); i++ )
		{
			logOutput(""+plugin.getServer().getPlayer(players.get( posIndex )));
		}
		logOutput("PlayerIndex at: " + posIndex);
		logOutput("PlayerList size: " + players.size());*/
		
		Player player = null;
		
		if ( posIndex < (players.size() - 1) )
		{
			player = plugin.getServer().getPlayer(players.get(posIndex));
			posIndex++;
		}
		else if ( players.size() > 0 )
		{
			posIndex = 0;
			player = plugin.getServer().getPlayer(players.get(posIndex));
		}
		else
		{
			posIndex = 0;
		}
		
		unlock();
		return player;
	}
	
	public void addPlayer(Player player)
	{
		lock();
		
		if( !(players.contains(player.getName())) )
		{
			players.add(player.getName());
		}
		
		unlock();
	}
	
	public void removePlayer(Player player)
	{
		lock();
		
		if( players.contains(player.getName()) )
		{
			players.remove(player.getName());
			players.trimToSize();
		}
		
		unlock();
	}
	
	public boolean isLocked()
	{
		return mutex;
	}
	
	private void lock()
	{
		while( isLocked() ) {}
		mutex = true;
	}
	
	private void unlock()
	{
		mutex = false;
	}
}
