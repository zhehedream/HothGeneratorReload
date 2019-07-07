package biz.orgin.minecraft.hothgenerator;

import java.util.Random;
import java.util.logging.Level;
import org.bukkit.Bukkit;

import org.bukkit.Chunk;
import org.bukkit.GrassSpecies;
import org.bukkit.Material;
import org.bukkit.TreeType;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.Bisected;
import org.bukkit.block.data.BlockData;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.material.LongGrass;

public class DagobahGrassPopulator extends BlockPopulator
{
	@SuppressWarnings("unused")
	private int height;
	@SuppressWarnings("unused")
	private HothGeneratorPlugin plugin;
	private NoiseGenerator noiseGenerator;


	public DagobahGrassPopulator(int height)
	{
		this.plugin = HothGenerator.getPlugin();
		this.height = height;
	}

	@Override
	public void populate(World world, Random random, Chunk chunk)
	{
		if(this.noiseGenerator==null)
		{
			this.noiseGenerator = new NoiseGenerator(world);
		}
		
		// Add tall grass
		
		int xx = chunk.getX()*16;
		int zz = chunk.getZ()*16;

		for(int x=0;x<16;x++)
		{
			for(int z=0;z<16;z++)
			{
				int rx = xx+x;
				int rz = zz+z;
				
				Block block = world.getHighestBlockAt(rx, rz);
				Material downType = block.getRelative(BlockFace.DOWN).getType();
				if(downType.equals(Material.GRASS_BLOCK))
				{
					double dograss = this.noiseGenerator.noise(rx, rz, 4, 4)*10;
					double domushroom = this.noiseGenerator.noise(rx, rz, 4, 50)*10;
					if(dograss>3 && dograss<8)
					{
						int mushroom = random.nextInt(70);
						if(mushroom==5 && domushroom>7)
						{
							BlockState state = block.getState();
							if(random.nextInt(2)== 0)
							{
								state.setType(Material.BROWN_MUSHROOM);
							}
							else
							{
								state.setType(Material.RED_MUSHROOM);
							}
							state.update(true, false);
						}
						else
						{
							int type = random.nextInt(4);
                                                        if(type == 0) {
                                                            BlockState state = block.getState();
                                                            state.setType(Material.TALL_GRASS);
                                                            BlockData data = state.getBlockData();
                                                            ((Bisected)data).setHalf(Bisected.Half.BOTTOM);
                                                            state.setBlockData(data);
                                                            state.update(true, false);
                                                            
                                                            state = block.getRelative(BlockFace.UP).getState();
                                                            state.setType(Material.TALL_GRASS);
                                                            data = state.getBlockData();
                                                            ((Bisected)data).setHalf(Bisected.Half.TOP);
                                                            state.setBlockData(data);
                                                            state.update(true, false);
                                                        } else {
                                                            BlockState state = block.getState();
                                                            state.setType(Material.GRASS);
                                                            state.update(true, false);
                                                        }
						}
					}
						

				}
				else if(downType.equals(Material.MYCELIUM))
				{
					int mushroom = random.nextInt(20);
					if(mushroom==5)
					{
						int type = random.nextInt(40);
						
						BlockState state = block.getState();
						if(type%2 == 0)
						{
							if(type==18)
							{
								world.generateTree(block.getLocation(), TreeType.BROWN_MUSHROOM);
								
							}
							else
							{
								state.setType(Material.BROWN_MUSHROOM);
								state.update(true, false);
							}
						}
						else
						{
							if(type==17)
							{
								world.generateTree(block.getLocation(), TreeType.RED_MUSHROOM);
							}
							else
							{
								state.setType(Material.RED_MUSHROOM);
								state.update(true, false);
							}
						}
					}
				}
                                else if(downType.equals(Material.WATER))
                                {
                                    try{
                                    double dograss = this.noiseGenerator.noise(rx, rz, 4, 4)*10;
                                    if(dograss > 3 && random.nextInt(3) == 0) {
                                        Block nblock = block.getRelative(BlockFace.DOWN);
                                        while(nblock.getType().equals(Material.WATER)) {
                                            nblock = nblock.getRelative(BlockFace.DOWN);
                                        }
                                        Material ntype = nblock.getType();
                                        if(ntype.equals(Material.DIRT) || ntype.equals(Material.TERRACOTTA) 
                                                || ntype.equals(Material.SAND) || ntype.equals(Material.CLAY)
                                                || ntype.equals(Material.OAK_LOG) || ntype.equals(Material.OAK_LEAVES)
                                                || ntype.equals(Material.GRAVEL)) {
                                            nblock = nblock.getRelative(BlockFace.UP);
                                            if(nblock.getRelative(BlockFace.UP).getType() == Material.WATER) {
                                                int type = random.nextInt(4);
                                                if(type == 0) {
                                                    BlockState state = nblock.getState();
                                                    state.setType(Material.TALL_SEAGRASS);
                                                    BlockData data = state.getBlockData();
                                                    ((Bisected)data).setHalf(Bisected.Half.BOTTOM);
                                                    state.setBlockData(data);
                                                    state.update(true, false);
                                                            
                                                    state = nblock.getRelative(BlockFace.UP).getState();
                                                    state.setType(Material.TALL_SEAGRASS);
                                                    data = state.getBlockData();
                                                    ((Bisected)data).setHalf(Bisected.Half.TOP);
                                                    state.setBlockData(data);
                                                    state.update(true, false);
                                                    
                                                    continue;
                                                }
                                            } else {
                                                BlockState state = nblock.getState();
                                                state.setType(Material.SEAGRASS);
                                                state.update(true, false);
                                            }
                                        }
                                    }
                                    } catch(Exception ex) {
                                        Bukkit.getLogger().log(Level.SEVERE, ex.toString());
                                    }
                                }
			}
		}
	}

}
