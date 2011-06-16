package net.weasel.Vegetation;

import java.util.ArrayList;

import org.bukkit.World;
import org.bukkit.entity.Player;

public class PlayerList {

	public void logOutput( String text ) { Vegetation.logOutput( text ); }
	
	private Vegetation plugin;
	private World world;
	private ArrayList<VegetationPlayer> players;
	private int posIndex;
	private Mutex mutex;
	
	public PlayerList(Vegetation p, World w)
	{
		plugin = p;
		world = w;
		players = new ArrayList<VegetationPlayer>();
		posIndex = 0;
		mutex = new Mutex();
	}
	
	public void getActivePlayerList()
	{
		mutex.aquire();
		
		players.clear();
		
		for( Player player: world.getPlayers() )
		{
			players.add(new VegetationPlayer(player.getName()));
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
			player = plugin.getServer().getPlayer(players.get(posIndex).getName());
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
	
	public VegetationPlayer getVegetationplayer(String name)
	{
		for(VegetationPlayer player: players)
		{
			if( name.equals(player.getName()) )
			{
				return player;
			}
		}
		return null;
	}
	
	public void addPlayer(Player player)
	{
		mutex.aquire();
		
		if( !(contains(player.getName())) )
		{
			players.add(new VegetationPlayer(player.getName()));
		}
		
		mutex.release();
	}
	
	public void removePlayer(Player player)
	{
		mutex.aquire();
		
		if( contains(player.getName()) )
		{
			remove(player.getName());
			players.trimToSize();
		}
		
		mutex.release();
	}
	
	private void remove(String name)
	{
		for( int i = 0; i < players.size(); i++ )
		{
			if( name.equals(players.get(i).getName()) )
			{
				players.remove(i);
			}
		}
	}
	
	private boolean contains(String name)
	{
		for( VegetationPlayer player: players )
		{
			if( name.equals(player.getName()) )
			{
				return true;
			}
		}
		return false;
	}
	
	public int getCountofPlayer()
	{
		return players.size();
	}
}
