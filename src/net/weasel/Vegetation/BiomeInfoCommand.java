package net.weasel.Vegetation;

import org.bukkit.block.Biome;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BiomeInfoCommand implements CommandExecutor {

	private Vegetation plugin;

	public BiomeInfoCommand(Vegetation instance) {
		plugin = instance;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			Biome biome = player.getLocation().getBlock().getBiome();
			player.sendMessage("You are currently standing on " + biome);
			return true;
		}
		return false;
	}
}
