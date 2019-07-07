package biz.orgin.minecraft.hothgenerator;

import java.util.Random;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.generator.BlockPopulator;

/**
 * Generates small lava fountains
 * @author orgin
 *
 */
public class MustafarLavaFountainPopulator extends BlockPopulator
{
	@SuppressWarnings("unused")
	private int height;
	@SuppressWarnings("unused")
	private HothGeneratorPlugin plugin;

	public MustafarLavaFountainPopulator(int height)
	{
		this.plugin = HothGenerator.getPlugin();
		this.height = height;
	}

	
	@Override
	public void populate(World world, Random random, Chunk chunk)
	{
		int x = chunk.getX()*16;
		int z = chunk.getZ()*16;

		
		// Lava level calculation
		double ll = LavaLevelGenerator.getLavaLevelFractionAt(world, x, z);

		int ctr = 0;
		if(ll>0.9)
		{
			ctr = random.nextInt(5);
		}
		else if(ll>0.7)
		{
			ctr = random.nextInt(4);
		}
		else if(ll>0.5)
		{
			ctr = random.nextInt(2);
		}
		else if(ll>0.3)
		{
			ctr = random.nextInt(1);
		}
		
		for(int i=0;i<ctr;i++)
		{
			int rx = x + random.nextInt(16) - 8;
			int rz = z + random.nextInt(16) - 8;
			
			// Find highest non empty block. For some reason getHighestBlockAt treats lava as AIR
			Block block = world.getHighestBlockAt(rx, rz);
			while(!block.isEmpty())
			{
				block = block.getRelative(BlockFace.UP);
			}
			block = block.getRelative(BlockFace.DOWN);
			
			// Inject the fountain. But only on LAVA.
			if(block.getType().equals(Material.LAVA))
			{
				int height = (int)((double)(3 + random.nextInt(8)) * ll);
				
				for(int j=0;j<height;j++)
				{
					block = block.getRelative(BlockFace.UP);
					block.setType(Material.LAVA);
					
					if(height>5)
					{
						if(j<(height/2))
						{
							block.getRelative(BlockFace.EAST).setType(Material.LAVA);
							block.getRelative(BlockFace.WEST).setType(Material.LAVA);
							block.getRelative(BlockFace.NORTH).setType(Material.LAVA);
							block.getRelative(BlockFace.SOUTH).setType(Material.LAVA);
						}
					}
				}
			}
		}
	}

}
