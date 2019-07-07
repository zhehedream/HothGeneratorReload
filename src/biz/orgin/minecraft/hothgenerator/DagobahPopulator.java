package biz.orgin.minecraft.hothgenerator;

import java.util.Random;

import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.generator.BlockPopulator;

public class DagobahPopulator extends BlockPopulator
{
	@SuppressWarnings("unused")
	private int height;
	@SuppressWarnings("unused")
	private HothGeneratorPlugin plugin;

	public DagobahPopulator(int height)
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
		
		// Add various swamp stuff here


		
		// Set biome for proper weather effect
		for(int i=0;i<16;i++)
		{
			for(int j=0;j<16;j++)
			{
				Biome biome = world.getBiome(rx+j, rz+i);
				if(//!biome.equals(Biome.RIVER)
						//&& !biome.equals(Biome.FROZEN_RIVER)
						!biome.equals(Biome.MUSHROOM_FIELDS)
						&& !biome.equals(Biome.MUSHROOM_FIELD_SHORE))
				{
					world.setBiome(rx+j, rz+i, Biome.SWAMP);
				}
			}
		}
	}
}
