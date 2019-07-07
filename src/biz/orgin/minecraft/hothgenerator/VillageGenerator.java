package biz.orgin.minecraft.hothgenerator;

import java.io.IOException;
import java.util.Random;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;

import biz.orgin.minecraft.hothgenerator.schematic.LoadedSchematic;

public class VillageGenerator {

	private static LoadedSchematic villagecenter = null;
	private static LoadedSchematic[] villagehuts = null;
	
	private static Position[] hutPositions = new Position[]
			{
				new Position(-11, 0,   5),  // left
				new Position(  2, 0, -11), // top 1
				new Position( 13, 0, -11), // top 2
				new Position( 24, 0, -11), // top 3
				new Position( 37, 0,   5), // right
				new Position(  2, 0,  21), // bottom 1
				new Position( 13, 0,  21), // bottom 2
				new Position( 24, 0,  21)  // bottom 3
			};
	
	private static int[] hutRotations = new int[]
			{
				1,2,2,2,3,0,0,0
			};

	
	public static void generateVillage(HothGeneratorPlugin plugin, World world, Random random, int chunkX, int chunkZ)
	{
		try
		{
			if(VillageGenerator.villagecenter==null)
			{
				VillageGenerator.villagecenter = new LoadedSchematic(plugin.getResource("schematics/villagecenter.sm"),"villagecenter");
				VillageGenerator.villagehuts = new LoadedSchematic[8];
				VillageGenerator.villagehuts[0] = new LoadedSchematic(plugin.getResource("schematics/villagehut1.sm"),"villagehut1");
				VillageGenerator.villagehuts[1] = new LoadedSchematic(plugin.getResource("schematics/villagehut2.sm"),"villagehut2");
				VillageGenerator.villagehuts[2] = new LoadedSchematic(plugin.getResource("schematics/villagehut3.sm"),"villagehut3");
				VillageGenerator.villagehuts[3] = new LoadedSchematic(plugin.getResource("schematics/villagehut4.sm"),"villagehut4");
				VillageGenerator.villagehuts[4] = new LoadedSchematic(plugin.getResource("schematics/villagehut5.sm"),"villagehut5");
				VillageGenerator.villagehuts[5] = new LoadedSchematic(plugin.getResource("schematics/villagehut6.sm"),"villagehut6");
				VillageGenerator.villagehuts[6] = new LoadedSchematic(plugin.getResource("schematics/villagehut7.sm"),"villagehut7");
				VillageGenerator.villagehuts[7] = new LoadedSchematic(plugin.getResource("schematics/villagehut8.sm"),"villagehut8");
			}
			
			int rarity = ConfigManager.getStructureVillageRarity(plugin, world);
	
			if(rarity!=0)
			{
				int doit = random.nextInt(VillageGenerator.villagecenter.getRarity()*rarity);
				if(doit == VillageGenerator.villagecenter.getRandom())
				{
					plugin.addTask(new PlaceVillage(world, random, chunkX, chunkZ));
				}	
			}
		}
		catch(IOException e)
		{
			plugin.logMessage("Error while loading internal schematics: " + e.getMessage(), true);
			VillageGenerator.villagecenter = null;
		}
	}
	
	static class PlaceVillage extends HothRunnable
	{
		private static final long serialVersionUID = 2479291832071117631L;
		private Random random;
		private int chunkx;
		private int chunkz;
		
		public String getParameterString()
		{
			return "chunkx=" + this.chunkx + " chunkz=" + this.chunkz;
		}

		public PlaceVillage(World world, Random random, int chunkx, int chunkz)
		{
			this.setName("PlaceVillage");
			this.setPlugin(null);
			this.setWorld(world);
			this.random = random;
			this.chunkx = chunkx;
			this.chunkz = chunkz;
		}

		@Override
		public void run()
		{
			HothGeneratorPlugin plugin = this.getPlugin();
			World world = this.getWorld();
			
			if(plugin.getWorldType(world) == WorldType.TATOOINE)
			{
				LoadedSchematic schematic = VillageGenerator.villagecenter;
				int x = this.chunkx * 16 + this.random.nextInt(15);
				int z = this.chunkz * 16 + this.random.nextInt(15);
				int y = getSafePosition(world, schematic, x, z);
				
				if(y > 0)
				{
					
					// Begin with village center
					int w = schematic.getWidth();
					int l = schematic.getLength();
					
					int hw = w/2;
					int hl = l/2;
					
					LootGenerator generator = LootGenerator.getLootGenerator(schematic.getLoot());
					if(generator==null)
					{
						HothUtils.placeSchematic(plugin, world, schematic, x-hw, y, z-hl, schematic.getLootMin(), schematic.getLootMax());
					}
					else
					{
						HothUtils.placeSchematic(plugin, world, schematic, x-hw, y, z-hl, schematic.getLootMin(), schematic.getLootMax(), generator);
					}
					
					plugin.logMessage("Placing village at " + world.getName() + "," + x + "," + y + "," + z, true);
					
					// Then huts
					for(int i=0;i<VillageGenerator.hutPositions.length;i++)
					{
						Position pos = VillageGenerator.hutPositions[i];
						int dx = pos.x;
						int dz = pos.z;
						
						int xx = x-hw+dx;
						int zz = z-hl+dz;
						
						LoadedSchematic hut = VillageGenerator.villagehuts[this.random.nextInt(VillageGenerator.villagehuts.length)].cloneRotate(VillageGenerator.hutRotations[i]);
						
						int yy = this.getSafePosition(world, hut, xx, zz);
						
						if(yy>0)
						{
							generator = LootGenerator.getLootGenerator(hut.getLoot());
							
							if(generator==null)
							{
								HothUtils.placeSchematic(plugin, world, hut, xx, yy, zz, hut.getLootMin(), hut.getLootMax());
							}
							else
							{
								HothUtils.placeSchematic(plugin, world, hut, xx, yy, zz, hut.getLootMin(), hut.getLootMax(), generator);
							}
						}
					}
				}
			}
			
		}
		
		private int getSafePosition(World world, LoadedSchematic schematic, int x, int z)
		{
			int y = 128;

			int w = schematic.getWidth();
			int l = schematic.getLength();
			int h = schematic.getHeight();
			
			int hw = w/2;
			int hl = l/2;

			boolean safe = true;
			
			int yoffset = schematic.getYoffset();
			
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

			if(safe)
			{
				return y;
			}
			else
			{
				return -1;
			}
		}
	}
}
