package net.weasel.Vegetation;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;

public class Grazers
{
	public static Vegetation plugin;
	
	public static void logOutput( String text ) { Vegetation.logOutput( text ); }
	
	public static boolean isEnabledGrazer( String which )
	{
		boolean retVal = false;

		if( Vegetation.enableGrazers == false ) return false;
			
		if( which.equals( "CraftSheep" ) && Vegetation.grazingSheep == true ) retVal = true;
		if( which.equals( "CraftChicken" ) && Vegetation.grazingChickens == true ) retVal = true;
		if( which.equals( "CraftCow" ) && Vegetation.grazingCows == true ) retVal = true;
		if( which.equals( "CraftPig" ) && Vegetation.grazingPigs == true ) retVal = true;

	 	return( retVal );
	}
	
	public static void grazeAnimals()
	{
		if( Vegetation.enableGrazers == false ) return;
		
		plugin = Vegetation.plugin;
		World world = null;
		Block targetBlock = null;
		List<Entity> entities;
		Entity entity = null;
		
		if ( Vegetation.debugging ) logOutput("Grazing Animals...");
		
		for( int X = 0; X < Vegetation.plugin.getServer().getWorlds().size(); X++ )
		{
			world = plugin.getServer().getWorlds().get(X);
			entities = world.getEntities();
			
			if( entities.size() > 0 )
			{
				for( int Y = 0; Y < Vegetation.maxGrazingAnimalsCount; Y++ )
				{
					int R = Vegetation.generator.nextInt(entities.size());
					entity = entities.get(R);

					if( Grazers.isEnabledGrazer( entity.toString() ) )
					{
						targetBlock = entity.getLocation().getBlock().getRelative(BlockFace.DOWN);
						
						if( targetBlock.getType() == Material.GRASS )
						{
							if( targetBlock.getData() >= 2 )
							{
								targetBlock.setData( (byte)(targetBlock.getData()-1) );
								if ( Vegetation.debugging ) logOutput( "Entity " + entity.toString() + " just ate some grass." );
							}
						}
						
					}

				}
			}
		}
	}
}
