package biz.orgin.minecraft.hothgenerator;

import java.util.Random;

import org.bukkit.World;

import biz.orgin.minecraft.hothgenerator.schematic.GreenGarden;
import biz.orgin.minecraft.hothgenerator.schematic.GreyGarden;
import biz.orgin.minecraft.hothgenerator.schematic.Schematic;

// @ToDo: replace this with schematics

/**
 * A generator that places underground gardens into the world.
 * @author orgin
 *
 */
public class GardenGenerator
{
	
	public static void generateGarden(HothGeneratorPlugin plugin, World world, Random random, int chunkX, int chunkZ)
	{
		int rarity = ConfigManager.getStructureGardensRarity(plugin, world);
		
		if(rarity!=0)
		{
			int place = random.nextInt(50*rarity);
		
			if(place==37)
			{
				//Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new PlaceGarden(plugin, world, random, chunkX, chunkZ));
				plugin.addTask(new PlaceGarden(world, random, chunkX, chunkZ));
			}	
		}
	}

	static class PlaceGarden extends HothRunnable
	{
		private static final long serialVersionUID = -1968688856754270141L;
		private Random random;
		private int chunkx;
		private int chunkz;
		
		public PlaceGarden(World world, Random random, int chunkx, int chunkz)
		{
			this.setName("PlaceGarden");
			this.setWorld(world);
			this.setPlugin(null);
			this.random = random;
			this.chunkx = chunkx;
			this.chunkz = chunkz;
		}

		public String getParameterString() {
			return "chunkx=" + chunkx + " chunkz=" + chunkz;
		}

		@Override
		public void run()
		{
			World world = this.getWorld();
			HothGeneratorPlugin plugin = this.getPlugin();

			int surfaceOffset = ConfigManager.getWorldSurfaceoffset(plugin, world);

			int x = random.nextInt(16) + this.chunkx * 16;
			int z = random.nextInt(16) + this.chunkz * 16;
			int y = 9 + random.nextInt(15 + surfaceOffset);
			
			Schematic garden;
			switch(random.nextInt(4))
			{
			case 0:
				garden = GreyGarden.instance; // Mushroom garden
				break;
			case 1:
			case 2:
			case 3:
			default:
				garden = GreenGarden.instance; // Green garden
				break;
			}
			
			HothUtils.placeSchematic(plugin, world, garden, x, y, z, 2, 10);

			plugin.logMessage("Placing garden at " + world.getName() + "," + x + "," + y + "," + z, true);
		}

	}

}
