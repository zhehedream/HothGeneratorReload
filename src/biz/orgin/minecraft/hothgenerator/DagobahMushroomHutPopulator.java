package biz.orgin.minecraft.hothgenerator;

import java.io.IOException;
import java.util.Random;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.generator.BlockPopulator;

import biz.orgin.minecraft.hothgenerator.schematic.LoadedSchematic;
import java.util.logging.Level;
import org.bukkit.Bukkit;

public class DagobahMushroomHutPopulator extends BlockPopulator
{
	@SuppressWarnings("unused")
	private int height;
	private HothGeneratorPlugin plugin;

	private LoadedSchematic[][] schematics;

	public DagobahMushroomHutPopulator(int height)
	{
		this.plugin = HothGenerator.getPlugin();
		this.height = height;
		try
		{
			this.schematics = new LoadedSchematic[6][4];
			LoadedSchematic schematic = new LoadedSchematic(plugin.getResource("schematics/mushroomhut1.sm"),"mushroomhut1");
			this.schematics[0][0] = schematic.cloneRotate(0);
			this.schematics[0][1] = schematic.cloneRotate(1);
			this.schematics[0][2] = schematic.cloneRotate(2);
			this.schematics[0][3] = schematic.cloneRotate(3);
			schematic = new LoadedSchematic(plugin.getResource("schematics/mushroomhut2.sm"),"mushroomhut2");
			this.schematics[1][0] = schematic.cloneRotate(0);
			this.schematics[1][1] = schematic.cloneRotate(1);
			this.schematics[1][2] = schematic.cloneRotate(2);
			this.schematics[1][3] = schematic.cloneRotate(3);
			schematic = new LoadedSchematic(plugin.getResource("schematics/mushroomhut3.sm"),"mushroomhut3");
			this.schematics[2][0] = schematic.cloneRotate(0);
			this.schematics[2][1] = schematic.cloneRotate(1);
			this.schematics[2][2] = schematic.cloneRotate(2);
			this.schematics[2][3] = schematic.cloneRotate(3);
			schematic = new LoadedSchematic(plugin.getResource("schematics/mushroomhut4.sm"),"mushroomhut4");
			this.schematics[3][0] = schematic.cloneRotate(0);
			this.schematics[3][1] = schematic.cloneRotate(1);
			this.schematics[3][2] = schematic.cloneRotate(2);
			this.schematics[3][3] = schematic.cloneRotate(3);
			schematic = new LoadedSchematic(plugin.getResource("schematics/mushroomhut5.sm"),"mushroomhut5");
			this.schematics[4][0] = schematic.cloneRotate(0);
			this.schematics[4][1] = schematic.cloneRotate(1);
			this.schematics[4][2] = schematic.cloneRotate(2);
			this.schematics[4][3] = schematic.cloneRotate(3);
			schematic = new LoadedSchematic(plugin.getResource("schematics/mushroomhut6.sm"),"mushroomhut6");
			this.schematics[5][0] = schematic.cloneRotate(0);
			this.schematics[5][1] = schematic.cloneRotate(1);
			this.schematics[5][2] = schematic.cloneRotate(2);
			this.schematics[5][3] = schematic.cloneRotate(3);
		}
		catch (IOException e)
		{
			this.plugin.logMessage("Error while loading treehut.sm: " + e.getMessage());
			this.schematics = null;
		}
	}

        
        static class PlaceMushroomHutTask extends HothRunnable {
            private static final long serialVersionUID = 2297078006521966210L;
            private int chunkX;
            private int chunkZ;
            private Random random;
            private DagobahMushroomHutPopulator pop;
            public PlaceMushroomHutTask(World world, Random random, int chunkX, int chunkZ, DagobahMushroomHutPopulator pop)
            {
            	this.setName("PlaceMushroomHut");
		this.setWorld(world);
		this.setPlugin(null);
                this.chunkX = chunkX;
		this.chunkZ = chunkZ;
                this.random = random;
                this.pop = pop;
            }
            public String getParameterString() {
		return "chunkx=" + chunkX + " chunkz=" + chunkZ;
            }
            @Override
            public void run(){
                //try {
                World world = this.getWorld();
                this.pop.placeMushroomHut(world, random, world.getChunkAt(chunkX, chunkZ));
                /*} catch(Exception ex) {
                    Bukkit.getLogger().log(Level.SEVERE, ex.toString());
                }*/
            }
        }
        
	@Override
	public void populate(World world, Random random, Chunk chunk)
	{
		if(this.schematics!=null && ConfigManager.isGenerateMushroomHuts(this.plugin, world))
		{
			int rand = 1;
			int rarity = 3; 

			if(rarity!=0)
			{
				//if(rand == random.nextInt(rarity))
                                if(true)
				{
                                    //Bukkit.getLogger().log(Level.INFO, "Mushroom");
					//this.placeMushroomHut(world, random, chunk);
                                        plugin.addTask(new PlaceMushroomHutTask(world, random, chunk.getX(), chunk.getZ(), this));
				}	
			}			
		}
	}

	private void placeMushroomHut(World world, Random random, Chunk chunk)
	{
		LoadedSchematic schematic = this.schematics[random.nextInt(6)][random.nextInt(4)];

		int xx = chunk.getX()*16;
		int zz = chunk.getZ()*16;

		int x = xx + 8 + (4+random.nextInt(12));
		int z = zz + 8 + (4+random.nextInt(12));

		Biome biome = world.getBiome(x, z);

		if(biome.equals(Biome.MUSHROOM_FIELDS) || biome.equals(Biome.MUSHROOM_FIELD_SHORE))  // Only in the correct biomes
		{

			// Some kind of bug makes the server throw an exception if you spawn an entity (creature) during world load in a
			// populator in a world where a plugin has registered a creaturespawn listener. To circumvent this I had
			// to prevent spawning mushroom huts too close to the centre of the world.
			if(Math.sqrt((xx*xx)+(zz*zz))<400)
			{
				return;
			}

			int width = schematic.getWidth();
			int length = schematic.getLength();
			int height = schematic.getHeight();

			// Crudely calculate the height difference of the surface

			Block block = world.getHighestBlockAt(x, z);

			Block block1 = block.getRelative(BlockFace.DOWN);
			if(!block1.getType().equals(Material.MYCELIUM))
			{
				Block block2 = block1.getRelative(BlockFace.DOWN);
				if(block2.getType().equals(Material.MYCELIUM))
				{
					block = block1;
				}
				else
				{
					return; // Unable to place hut here
				}
			}


			int center = this.getSurfaceLevel(block);
			block = world.getHighestBlockAt(x-2, z-2);
			int c1 = this.getSurfaceLevel(block);
			block = world.getHighestBlockAt(x+2, z-2);
			int c2 = this.getSurfaceLevel(block);
			block = world.getHighestBlockAt(x-2, z+2);
			int c3 = this.getSurfaceLevel(block);
			block = world.getHighestBlockAt(x+2, z+2);
			int c4 = this.getSurfaceLevel(block);
			block = world.getHighestBlockAt(x-2, z);
			int c5 = this.getSurfaceLevel(block);
			block = world.getHighestBlockAt(x+2, z);
			int c6 = this.getSurfaceLevel(block);
			block = world.getHighestBlockAt(x, z+2);
			int c7 = this.getSurfaceLevel(block);
			block = world.getHighestBlockAt(x, z+2);
			int c8 = this.getSurfaceLevel(block);

			int min = center;
			int max = center;

			if(c1<min) min = c1;
			if(c2<min) min = c2;
			if(c3<min) min = c3;
			if(c4<min) min = c4;
			if(c5<min) min = c5;
			if(c6<min) min = c6;
			if(c7<min) min = c7;
			if(c8<min) min = c8;

			if(c1>max) max = c1;
			if(c2>max) max = c2;
			if(c3>max) max = c3;
			if(c4>max) max = c4;
			if(c5>max) max = c5;
			if(c6>max) max = c6;
			if(c7>max) max = c7;
			if(c8>max) max = c8;

			if(max-min<5)
			{
				// Place The Schematic
				int y = min + height + schematic.getYoffset();
				int lootMin = schematic.getLootMin();
				int lootMax = schematic.getLootMax();
				HothUtils.placeSchematic(plugin, world, schematic, x-width/2, y, z-length/2, lootMin, lootMax);
			}
		}
	}

	private int getSurfaceLevel(Block block)
	{
		Material type = block.getType();
		while(block.getY()>5 && !type.equals(Material.GRASS_BLOCK) && !type.equals(Material.DIRT) && !type.equals(Material.WATER))
		{
			block = block.getRelative(BlockFace.DOWN);
			type = block.getType();
		}

		return block.getY();
	}
}
