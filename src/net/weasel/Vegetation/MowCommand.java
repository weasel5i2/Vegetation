package net.weasel.Vegetation;

import org.bukkit.World.Environment;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.CommandExecutor;
import org.bukkit.entity.Player;

public class MowCommand implements CommandExecutor {

	public static void logOutput(String text) {
		Vegetation.logOutput(text);
	}

	private Vegetation plugin;

	public MowCommand(Vegetation instance) {
		this.plugin = instance;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {
		if (sender instanceof Player) {
			Player P = (Player) sender;

			if (plugin.hasPermission(P, "vegetation.mow")) {
				VegetationWorld vWorld = plugin.vWorlds.get(P.getWorld().getName());
				Settings settings = vWorld.getSettings();
				if (settings == null) {
					if (P.getWorld().getEnvironment() == Environment.NETHER) {
						P.sendMessage("You can't grow anything in a nether world");
						return true;
					} else {
						//TODO: create settings for new World
					}
				}
				int maxActivePlayerCommands = settings.maxActivePlayerCommands;
				// Todo: implement command queue
				if (vWorld.getActivePlayerCommands() < maxActivePlayerCommands) {
					vWorld.increaseActivePlayerCommands();
					vWorld.grass.mowGrass(P.getLocation());
					P.sendMessage("You have cut down the grass.");
					vWorld.decreaseActivePlayerCommands();
					return true;
				}
			} else {
				P.sendMessage("You don't have permission to use this command!");
				return true;
			}
		}
		return false;
	}
}
