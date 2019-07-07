package biz.orgin.minecraft.hothgenerator;

import java.io.IOException;
import java.util.Random;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;

import biz.orgin.minecraft.hothgenerator.schematic.LoadedSchematic;

/**
 * Used to generate structures that are defined in the form of schematics files.
 * It basically does the same job as the CustomGenerator but it loads its schematics from
 * internal resource files instead of external ones.
 * Which schematic files to use are hard coded. Which means that new schematics cannot be
 * added without changing the code in this class.
 * @author orgin
 *
 */
public class SchematicsGenerator
{
	private static LoadedSchematic skeleton = null;
	private static LoadedSchematic skeleton2 = null;
	private static LoadedSchematic oasis = null;
	private static LoadedSchematic sandcastle = null;
	private static LoadedSchematic supergarden = null;
	private static LoadedSchematic[][] schematicsHoth = new LoadedSchematic[2][4];
	private static LoadedSchematic[][] schematicsTatooine = new LoadedSchematic[5][4];
	
	public static void generateSchematics(HothGeneratorPlugin plugin, World world, Random random, int chunkX, int chunkZ)
	{
		try
		{
			WorldType worldType = plugin.getWorldType(world);
			
			if(SchematicsGenerator.skeleton==null)
			{
				SchematicsGenerator.skeleton = new LoadedSchematic(plugin.getResource("schematics/skeleton.sm"),"skeleton");
				SchematicsGenerator.oasis = new LoadedSchematic(plugin.getResource("schematics/oasis.sm"),"oasis");
				SchematicsGenerator.sandcastle = new LoadedSchematic(plugin.getResource("schematics/sandcastle.sm"),"sandcastle");
				SchematicsGenerator.supergarden = new LoadedSchematic(plugin.getResource("schematics/supergarden.sm"),"supergarden");

				SchematicsGenerator.schematicsHoth[0][0] = SchematicsGenerator.skeleton.cloneRotate(0);
				SchematicsGenerator.schematicsHoth[0][1] = SchematicsGenerator.skeleton.cloneRotate(1);
				SchematicsGenerator.schematicsHoth[0][2] = SchematicsGenerator.skeleton.cloneRotate(2);
				SchematicsGenerator.schematicsHoth[0][3] = SchematicsGenerator.skeleton.cloneRotate(3);
				
				SchematicsGenerator.skeleton2 = SchematicsGenerator.skeleton.cloneRotate(0);
				SchematicsGenerator.skeleton2.setType(1);
				SchematicsGenerator.schematicsHoth[1][0] = SchematicsGenerator.skeleton2.cloneRotate(0);
				SchematicsGenerator.schematicsHoth[1][1] = SchematicsGenerator.skeleton2.cloneRotate(1);
				SchematicsGenerator.schematicsHoth[1][2] = SchematicsGenerator.skeleton2.cloneRotate(2);
				SchematicsGenerator.schematicsHoth[1][3] = SchematicsGenerator.skeleton2.cloneRotate(3);
				
				SchematicsGenerator.schematicsTatooine[0][0] = SchematicsGenerator.schematicsHoth[0][0];
				SchematicsGenerator.schematicsTatooine[0][1] = SchematicsGenerator.schematicsHoth[0][1];
				SchematicsGenerator.schematicsTatooine[0][2] = SchematicsGenerator.schematicsHoth[0][2];
				SchematicsGenerator.schematicsTatooine[0][3] = SchematicsGenerator.schematicsHoth[0][3];

				SchematicsGenerator.schematicsTatooine[1][0] = SchematicsGenerator.schematicsHoth[1][0];
				SchematicsGenerator.schematicsTatooine[1][1] = SchematicsGenerator.schematicsHoth[1][1];
				SchematicsGenerator.schematicsTatooine[1][2] = SchematicsGenerator.schematicsHoth[1][2];
				SchematicsGenerator.schematicsTatooine[1][3] = SchematicsGenerator.schematicsHoth[1][3];

				SchematicsGenerator.schematicsTatooine[2][0] = SchematicsGenerator.oasis.cloneRotate(0);
				SchematicsGenerator.schematicsTatooine[2][1] = SchematicsGenerator.oasis.cloneRotate(1);
				SchematicsGenerator.schematicsTatooine[2][2] = SchematicsGenerator.oasis.cloneRotate(2);
				SchematicsGenerator.schematicsTatooine[2][3] = SchematicsGenerator.oasis.cloneRotate(3);
				
				SchematicsGenerator.schematicsTatooine[3][0] = SchematicsGenerator.sandcastle.cloneRotate(0);
				SchematicsGenerator.schematicsTatooine[3][1] = SchematicsGenerator.sandcastle.cloneRotate(1);
				SchematicsGenerator.schematicsTatooine[3][2] = SchematicsGenerator.sandcastle.cloneRotate(2);
				SchematicsGenerator.schematicsTatooine[3][3] = SchematicsGenerator.sandcastle.cloneRotate(3);

				SchematicsGenerator.schematicsTatooine[4][0] = SchematicsGenerator.supergarden;
				SchematicsGenerator.schematicsTatooine[4][1] = SchematicsGenerator.supergarden;
				SchematicsGenerator.schematicsTatooine[4][2] = SchematicsGenerator.supergarden;
				SchematicsGenerator.schematicsTatooine[4][3] = SchematicsGenerator.supergarden;
			}
			
			if(worldType == WorldType.HOTH)
			{
				for(int i=0;i<schematicsHoth.length;i++)
				{
					Random newRandom = new Random(random.nextLong());
					
					LoadedSchematic schematic = schematicsHoth[i][newRandom.nextInt(4)];
					
					int rarity = 0;
					int rnd = 0;
					String name = schematic.getName();
					if(name.equals("skeleton"))
					{
						rarity = ConfigManager.getStructureSkeletonsRarity(plugin, world)*SchematicsGenerator.skeleton.getRarity()/2;
						rnd = ConfigManager.getStructureSkeletonsRarity(plugin, world)*SchematicsGenerator.skeleton.getRandom()/2;
					}
					
					if(rarity!=0)
					{
						if(rnd==newRandom.nextInt(rarity))
						{
							plugin.addTask(new PlaceSchematic(world, newRandom, chunkX, chunkZ, schematic));
						}
					}
				}
			}
			else if(worldType == WorldType.TATOOINE)
			{
				for(int i=0;i<schematicsTatooine.length;i++)
				{
					Random newRandom = new Random(random.nextLong());
					
					LoadedSchematic schematic = schematicsTatooine[i][newRandom.nextInt(4)];
					if(schematic==null)
					{
						break;
					}

					int rarity = 0;
					int rnd = 0;
					String name = schematic.getName();
					if(name.equals("oasis"))
					{
						rarity = ConfigManager.getStructureOasisRarity(plugin, world)*SchematicsGenerator.oasis.getRarity()/2;
						rnd = ConfigManager.getStructureOasisRarity(plugin, world)*SchematicsGenerator.oasis.getRandom()/2;
					}
					else if(name.equals("sandcastle"))
					{
						rarity = ConfigManager.getStructureSandCastleRarity(plugin, world)*SchematicsGenerator.sandcastle.getRarity()/2;
						rnd = ConfigManager.getStructureSandCastleRarity(plugin, world)*SchematicsGenerator.sandcastle.getRandom()/2;
					}
					else if(name.equals("supergarden"))
					{
						rarity = ConfigManager.getStructureSuperGardenRarity(plugin, world)*SchematicsGenerator.supergarden.getRarity()/2;
						rnd = ConfigManager.getStructureSuperGardenRarity(plugin, world)*SchematicsGenerator.supergarden.getRandom()/2;
					}
					
					if(rarity!=0)
					{
						
						if(rnd==newRandom.nextInt(rarity))
						{
							plugin.addTask(new PlaceSchematic(world, newRandom, chunkX, chunkZ, schematic));
						}
					}
				}
			}
		}
		catch(IOException e)
		{
			
		}
	}

	static class PlaceSchematic extends HothRunnable
	{
		private static final long serialVersionUID = -441975683950603467L;
		private Random random;
		private int chunkx;
		private int chunkz;
		private LoadedSchematic schematic;
		
		public PlaceSchematic(World world, Random random, int chunkx, int chunkz, LoadedSchematic schematic)
		{
			this.setName("PlaceSchematic");
			this.setWorld(world);
			this.setPlugin(null);
			this.random = random;
			this.chunkx = chunkx;
			this.chunkz = chunkz;
			this.schematic = schematic;
		}
		
		public String getParameterString()
		{
			return "schematic=" + schematic.getName() + " chunkx=" + this.chunkx + " chunkz=" + this.chunkz;
		}

		@Override
		public void run()
		{
			World world = this.getWorld();
			HothGeneratorPlugin plugin = this.getPlugin();
			
			int surfaceOffset = ConfigManager.getWorldSurfaceoffset(plugin, world);
			
			int x = this.random.nextInt(16) + this.chunkx * 16 - this.schematic.getWidth()/2;
			int z = this.random.nextInt(16) + this.chunkz * 16 - this.schematic.getLength()/2;
			int y = 128;
			
			int miny, maxy;
			
			int w = this.schematic.getWidth();
			int l = this.schematic.getLength();
			int h = this.schematic.getHeight();
			
			int hw = w/2;
			int hl = l/2;

			boolean safe = true;

			switch(this.schematic.getType())
			{
			case 0: // On surface
				
				int yoffset = this.schematic.getYoffset();
				
				for(int zz=z-hl;zz<z+hl;zz++)
				{
					for(int xx=x-hw;xx<x+hw;xx++)
					{
						int ty = world.getHighestBlockYAt(xx,zz);
						if(ty<y)
						{
							y=ty;
						}
					}
				}
				
				y = y + h + yoffset;
				
				Block block = world.getBlockAt(x,y - h,z); Material type = block.getType();
				if(safe && (type.equals(Material.STONE) || type.equals(Material.GLASS))) safe = false;

				if(yoffset==0) // Only do air safety checks if the offset value is 0
				{
					block = world.getBlockAt(x-hw,y,z-hl); type = block.getType();
					if(safe && !type.equals(Material.AIR)) safe = false;
					block = world.getBlockAt(x+hw,y,z-hl); type = block.getType();
					if(safe && !type.equals(Material.AIR)) safe = false;
					block = world.getBlockAt(x+hw,y,z+hl); type = block.getType();
					if(safe && !type.equals(Material.AIR)) safe = false;
					block = world.getBlockAt(x-hw,y,z+hl); type = block.getType();
					if(safe && !type.equals(Material.AIR)) safe = false;

					block = world.getBlockAt(x-hw,y-h,z-hl); type = block.getType();
					if(safe && type.equals(Material.AIR)) safe = false;
					block = world.getBlockAt(x+hw,y-h,z-hl); type = block.getType();
					if(safe && type.equals(Material.AIR)) safe = false;
					block = world.getBlockAt(x+hw,y-h,z+hl); type = block.getType();
					if(safe && type.equals(Material.AIR)) safe = false;
					block = world.getBlockAt(x-hw,y-h,z+hl); type = block.getType();
					if(safe && type.equals(Material.AIR)) safe = false;
				
				}
				block = world.getBlockAt(x-hw,y-h,z-hl); type = block.getType();
				if(safe && (type.equals(Material.STONE) || type.equals(Material.GLASS))) safe = false;
				block = world.getBlockAt(x+hw,y-h,z-hl); type = block.getType();
				if(safe && (type.equals(Material.STONE) || type.equals(Material.GLASS))) safe = false;
				block = world.getBlockAt(x+hw,y-h,z+hl); type = block.getType();
				if(safe && (type.equals(Material.STONE) || type.equals(Material.GLASS))) safe = false;
				block = world.getBlockAt(x-hw,y-h,z+hl); type = block.getType();
				if(safe && (type.equals(Material.STONE) || type.equals(Material.GLASS))) safe = false;
				break;
			case 1: // In ice layer
				miny = surfaceOffset + 30 + this.schematic.getHeight();
				maxy = surfaceOffset + 65;
				y = miny + this.random.nextInt(maxy-miny);
				break;
			case 2: // In stone layer
				miny = 6 + this.schematic.getHeight();
				maxy = surfaceOffset + 26;
				y = miny + this.random.nextInt(maxy-miny);
				break;
			}
			
			if(safe)
			{
				LootGenerator generator = LootGenerator.getLootGenerator(schematic.getLoot());
				if(generator==null)
				{
					HothUtils.placeSchematic(plugin, world, schematic, x-hw, y, z-hl, schematic.getLootMin(), schematic.getLootMax());
				}
				else
				{
					HothUtils.placeSchematic(plugin, world, schematic, x-hw, y, z-hl, schematic.getLootMin(), schematic.getLootMax(), generator);
				}
	
				plugin.logMessage("Placing " + schematic.getName() + " at " + world.getName() + "," + x + "," + y + "," + z, true);
			}
			else
			{
				plugin.logMessage("Failed to place " + this.schematic.getName() + " at " + world.getName() + "," + x + "," + y + "," + z, true);
			}
		}
	}

}
