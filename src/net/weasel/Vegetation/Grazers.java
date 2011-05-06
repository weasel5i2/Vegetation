package net.weasel.Vegetation;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.LivingEntity;

public class Grazers
{
	public static Vegetation plugin;
	
	public void logOutput( String text ) { Vegetation.logOutput( text ); }
	
	private Settings settings;
	private World world;
	
	public Grazers(World w, Settings s)
	{
		world = w;
		settings = s;
	}
	
	public boolean isEnabledGrazer( String which )
	{
		if( which.equals( "CraftSheep" ) && settings.grazingSheep ) return true;
		else if( which.equals( "CraftChicken" ) && settings.grazingChickens ) return true;
		else if( which.equals( "CraftCow" ) && settings.grazingCows ) return true;
		else if( which.equals( "CraftPig" ) && settings.grazingPigs ) return true;
		else return false;
	}
	
	public void grazeAnimals()
	{
		Block targetBlock = null;
		List<LivingEntity> entities;
		LivingEntity entity = null;
		
		if ( Vegetation.debugging ) logOutput("Grazing Animals...");
		
		entities = world.getLivingEntities();

		if( entities.size() > 0 )
		{
			for( int Y = 0; Y < settings.maxGrazingAnimalsCount; Y++ )
			{
				int R = Vegetation.generator.nextInt(entities.size());
				entity = entities.get(R);

				if( isEnabledGrazer( entity.toString() ) )
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
