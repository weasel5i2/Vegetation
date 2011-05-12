package net.weasel.Vegetation;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;

/*
 * This class controls entities, which
 * graze grass.
 */
public class Grazers
{
	public void logOutput(String text) { Vegetation.logOutput(text); }
	
	private Settings settings;
	private World world;
	
	public Grazers(World w, Settings s)
	{
		world = w;
		settings = s;
	}
	
	/*
	 * Determines whether a type of animals
	 * grazes grass.
	 * 
	 * @param type
	 * @return true if the entity is of specified type and allowed to graze
	 */
	public boolean isEnabledGrazer(String type)
	{
		if( type.equals("CraftSheep") && settings.grazingSheep ) return true;
		else if( type.equals("CraftChicken") && settings.grazingChickens ) return true;
		else if( type.equals("CraftCow") && settings.grazingCows ) return true;
		else if( type.equals("CraftPig") && settings.grazingPigs ) return true;
		else return false;
	}
	
	/*
	 * Fetches entity list and let's animal graze
	 */
	public void grazeAnimals()
	{
		List<LivingEntity> entities;
		
		if ( Vegetation.debugging ) logOutput("Grazing Animals...");
		
		entities = world.getLivingEntities();

		if( entities.size() > 0 )
		{
			Block targetBlock;
			LivingEntity entity;
			Location locationOfEntity;
			int r;
			int grass = Material.GRASS.getId();
			
			for( int i = 0; i < settings.maxGrazingAnimalsCount; i++ )
			{
				r = Vegetation.generator.nextInt(entities.size());
				entity = entities.get(r);

				if( isEnabledGrazer( entity.toString() ) )
				{
					locationOfEntity = entity.getLocation();

					if( world.getBlockTypeIdAt((int)locationOfEntity.getX(), (int)locationOfEntity.getY() - 1, (int)locationOfEntity.getZ()) == grass )
					{
						targetBlock = world.getBlockAt((int)locationOfEntity.getX(), (int)locationOfEntity.getY() - 1, (int)locationOfEntity.getZ());
						if( targetBlock.getData() >= 2 )
						{
							targetBlock.setData( (byte)(targetBlock.getData()-1) );
							if ( Vegetation.debugging ) logOutput("Entity " + entity.toString() + " just ate some grass.");
						}
					}

				}

			}
		}
	}
}
