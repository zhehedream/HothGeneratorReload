package biz.orgin.minecraft.hothgenerator;

import java.util.Random;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.generator.BlockPopulator;

public class TatooinePopulator extends BlockPopulator
{
	@SuppressWarnings("unused")
	private int height;
	private HothGeneratorPlugin plugin;

	public TatooinePopulator(int height)
	{
		this.plugin = HothGenerator.getPlugin();
		this.height = height;
	}

	@Override
	public void populate(World world, Random random, Chunk chunk)
	{
		int x = chunk.getX();
		int z = chunk.getZ();
		
		int rx = x * 16;
		int rz = z * 16;
		
		// Cactuses
		if(ConfigManager.isGenerateCactuses(plugin, world))
		{
			this.placeCactuses(world, rx, rz, random);
		}
		
		// Shrubs
		if(ConfigManager.isGenerateShrubs(plugin, world))
		{
			this.placeShrubs(world, rx, rz, random);
		}
		
		// Set biome for proper weather effect
		for(int i=0;i<16;i++)
		{
			for(int j=0;j<16;j++)
			{
				world.setBiome(rx+j, rz+i, Biome.DESERT);
			}
		}
	}
	
	private void placeCactuses(World world, int rx, int rz, Random random)
	{
		for(int i=0;i<random.nextInt(32);i++)
		{
			int x = random.nextInt(15);
			int z = random.nextInt(15);

			int ran = 0;
			
			Biome biome = world.getBiome(rx+x, rz+z);
			
			if(biome.equals(Biome.MOUNTAINS))
			{
				ran = random.nextInt(16);
			}
			else if(biome.equals(Biome.FOREST) || biome.equals(Biome.WOODED_HILLS))
			{
				ran = random.nextInt(3);
			}
			else if(biome.equals(Biome.PLAINS))
			{
				ran = random.nextInt(10);
			}
			else if(biome.equals(Biome.JUNGLE) || biome.equals(Biome.JUNGLE_HILLS))
			{
				ran = random.nextInt(2);
			}
			else if(biome.equals(Biome.SWAMP))
			{
				ran = random.nextInt(8);
			}
			else if(biome.equals(Biome.TAIGA) || biome.equals(Biome.TAIGA_HILLS))
			{
				ran = random.nextInt(20);
			}
			else if(biome.equals(Biome.DARK_FOREST) || biome.equals(Biome.DARK_FOREST_HILLS))
			{
				ran = random.nextInt(6);
			}			
			else if(biome.equals(Biome.DESERT))
			{
				ran = random.nextInt(100);
			}			
			
			if(ran == 1)
			{
				Block block = world.getHighestBlockAt(rx+x, rz+z);
				block = world.getBlockAt(rx+x, block.getY()-1, rz+z);
				if(block.getType().equals(Material.SAND))
				{
					int y = block.getY();
					for(int j=1;j<2+random.nextInt(4);j++)
					{
						block = world.getBlockAt(rx+x, y+j, rz+z);
						block.setType(Material.CACTUS);
					}
				}
			}
		}
	}
	
	private void placeShrubs(World world, int rx, int rz, Random random)
	{
		for(int i=0;i<random.nextInt(32);i++)
		{
			int x = random.nextInt(15);
			int z = random.nextInt(15);

			int ran = 0;
			
			Biome biome = world.getBiome(rx+x, rz+z);
			
			if(biome.equals(Biome.MOUNTAINS))
			{
				ran = random.nextInt(16);
			}
			else if(biome.equals(Biome.FOREST) || biome.equals(Biome.WOODED_HILLS))
			{
				ran = random.nextInt(6);
			}
			else if(biome.equals(Biome.PLAINS))
			{
				ran = random.nextInt(20);
			}
			else if(biome.equals(Biome.JUNGLE) || biome.equals(Biome.JUNGLE_HILLS))
			{
				ran = random.nextInt(3);
			}
			else if(biome.equals(Biome.SWAMP))
			{
				ran = random.nextInt(14);
			}
			else if(biome.equals(Biome.TAIGA) || biome.equals(Biome.TAIGA_HILLS))
			{
				ran = random.nextInt(12);
			}
			else if(biome.equals(Biome.DARK_FOREST) || biome.equals(Biome.DARK_FOREST_HILLS))
			{
				ran = random.nextInt(7);
			}			
			else if(biome.equals(Biome.DESERT))
			{
				ran = random.nextInt(3);
			}			
			else
			{
				ran = random.nextInt(5);
			}
			
			if(ran == 1)
			{
				Block block = world.getHighestBlockAt(rx+x, rz+z);
				block.setType(Material.DEAD_BUSH);
			}
		}
	}

}
