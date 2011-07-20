package net.weasel.Vegetation;

import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantLock;

import org.bukkit.World;
import org.bukkit.entity.Player;

public class PlayerList {

	private Vegetation plugin;
	private World world;
	private ArrayList<VegetationPlayer> players;
	private int posIndex;
	private ReentrantLock lock;

	public PlayerList(Vegetation p, World w) {
		plugin = p;
		world = w;
		players = new ArrayList<VegetationPlayer>();
		posIndex = 0;
		// mutex = new Mutex();
		lock = new ReentrantLock();
	}

	public void getActivePlayerList() {
		lock.lock();

		players.clear();
		Vegetation.lf.write("Clearing PlayerList - World [" + world.getName()
				+ "]");

		for (Player player : world.getPlayers()) {
			VegetationPlayer vPlayer = new VegetationPlayer(player.getName());
			vPlayer.setLastBlockPosition(player.getLocation().getBlock());
			players.add(vPlayer);
			Vegetation.lf.write("Adding Player [" + vPlayer.getName()
					+ "] - World [" + world.getName() + "]");
		}

		posIndex = 0;

		lock.unlock();
	}

	public Player getNextPlayer() {
		lock.lock();

		if (posIndex > (players.size() - 1))
			posIndex = 0;

		Player player = null;

		// get next position
		if (posIndex <= (players.size() - 1)) {
			player = plugin.getServer().getPlayer(
					players.get(posIndex).getName());
			posIndex++;
		}
		// return nothing
		else if (players.size() == 0) {
			posIndex = 0;
		}

		// log.write("Timer grabs Player [" + player.getName() + "]");
		lock.unlock();
		return player;
	}

	public VegetationPlayer getVegetationplayer(String name) {
		for (VegetationPlayer player : players) {
			if (name.equals(player.getName())) {
				return player;
			}
		}
		return null;
	}

	public void addPlayer(Player player) {
		lock.lock();

		if (!(contains(player.getName()))) {
			players.add(new VegetationPlayer(player.getName()));
		}

		lock.unlock();
	}

	public void addPlayer(VegetationPlayer player) {
		lock.lock();

		if (!(contains(player.getName()))) {
			players.add(player);
			Vegetation.lf.write("Adding Player [" + player.getName()
					+ "]- World [" + world.getName() + "]");
		}

		lock.unlock();
	}

	public void removePlayer(Player player) {
		lock.lock();

		if (contains(player.getName())) {
			remove(player.getName());
		}

		lock.unlock();
	}

	public void removePlayer(VegetationPlayer player) {
		lock.lock();

		if (contains(player.getName())) {
			remove(player.getName());
		}

		lock.unlock();
	}

	private void remove(String name) {
		for (int i = 0; i < players.size(); i++) {
			if (name.equals(players.get(i).getName())) {
				Vegetation.lf.write("Removing Player ["
						+ players.get(i).getName() + "] - World ["
						+ world.getName() + "]");
				players.remove(i);
				players.trimToSize();
			}
		}
	}

	private boolean contains(String name) {
		for (VegetationPlayer player : players) {
			if (name.equals(player.getName())) {
				return true;
			}
		}
		return false;
	}

	public int getCountofPlayer() {
		return players.size();
	}
}
