package biz.orgin.minecraft.hothgenerator;

import java.io.IOException;
import java.util.Random;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.generator.BlockPopulator;

import biz.orgin.minecraft.hothgenerator.schematic.LoadedSchematic;
import java.util.logging.Level;
import org.bukkit.Bukkit;

public class DagobahTreeHutPopulator extends BlockPopulator
{
	@SuppressWarnings("unused")
	private int height;
	private HothGeneratorPlugin plugin;
	
	private LoadedSchematic[] schematics;

	public DagobahTreeHutPopulator(int height)
	{
		this.plugin = HothGenerator.getPlugin();
		this.height = height;
		try
		{
			this.schematics = new LoadedSchematic[4];
			LoadedSchematic schematic = new LoadedSchematic(plugin.getResource("schematics/treehut.sm"),"treehut");
			this.schematics[0] = schematic.cloneRotate(0);
			this.schematics[1] = schematic.cloneRotate(1);
			this.schematics[2] = schematic.cloneRotate(2);
			this.schematics[3] = schematic.cloneRotate(3);
		}
		catch (IOException e)
		{
			this.plugin.logMessage("Error while loading treehut.sm: " + e.getMessage());
			this.schematics = null;
		}
	}
        
        static class PlaceTreeHutTask extends HothRunnable {
            private static final long serialVersionUID = 2297078006521966210L;
            private int chunkX;
            private int chunkZ;
            private Random random;
            private DagobahTreeHutPopulator pop;
            public PlaceTreeHutTask(World world, Random random, int chunkX, int chunkZ, DagobahTreeHutPopulator pop)
            {
            	this.setName("PlaceTreeHut");
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
                this.pop.placeTreeHut(world, random, world.getChunkAt(chunkX, chunkZ));
                /*} catch(Exception ex) {
                    Bukkit.getLogger().log(Level.SEVERE, ex.toString());
                }*/
            }
        }

	@Override
	public void populate(World world, Random random, Chunk chunk)
	{
		if(this.schematics!=null)
		{
			int rand = this.schematics[0].getRandom();
			int rarity = ConfigManager.getStructureTreeHutRarity(this.plugin, world); 

			if(rarity!=0)
			{
				//if(rand == random.nextInt((rarity*this.schematics[0].getRarity()/2)))
                                if(0 == random.nextInt(100))
				{
					this.placeTreeHut(world, random, chunk);
                                        //plugin.addTask(new PlaceTreeHutTask(world, random, chunk.getX(), chunk.getZ(), this));
				}	
			}			
		}
	}
	
	private void placeTreeHut(World world, Random random, Chunk chunk)
	{//Bukkit.getLogger().log(Level.INFO, "IN");
		LoadedSchematic schematic = this.schematics[random.nextInt(4)];
		
		int xx = chunk.getX()*16;
		int zz = chunk.getZ()*16;
		
		// Some kind of bug makes the server throw an exception if you spawn an entity (creature) during world load in a
		// populator in a world where a plugin has registered a creaturespawn listener. To circumvent this I had
		// to prevent spawning tree huts too close to the centre of the world.
		/*if(Math.sqrt((xx*xx)+(zz*zz))<400)
		{
			return;
		}*/
		
		int x = xx + 8 + random.nextInt(16);
		int z = zz + 8 + random.nextInt(16);
		
		int width = schematic.getWidth();
		int length = schematic.getLength();
		int height = schematic.getHeight();
		
		// Crudely calculate the height difference of the surface
		
		Block block = world.getHighestBlockAt(x, z);
		int center = this.getSurfaceLevel(block);
		block = world.getHighestBlockAt(x-width/2, z-length/2);
		int c1 = this.getSurfaceLevel(block);
		block = world.getHighestBlockAt(x+width/2, z-length/2);
		int c2 = this.getSurfaceLevel(block);
		block = world.getHighestBlockAt(x-width/2, z+length/2);
		int c3 = this.getSurfaceLevel(block);
		block = world.getHighestBlockAt(x+width/2, z+length/2);
		int c4 = this.getSurfaceLevel(block);
		block = world.getHighestBlockAt(x-width/2, z);
		int c5 = this.getSurfaceLevel(block);
		block = world.getHighestBlockAt(x+width/2, z);
		int c6 = this.getSurfaceLevel(block);
		block = world.getHighestBlockAt(x, z+length/2);
		int c7 = this.getSurfaceLevel(block);
		block = world.getHighestBlockAt(x, z+length/2);
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
		//Bukkit.getLogger().log(Level.INFO, "CC");
		if(max-min<5)
                //if(true)
		{
			// Place The Schematic
			int y = min + height + schematic.getYoffset();
			int lootMin = schematic.getLootMin();
			int lootMax = schematic.getLootMax();
			HothUtils.placeSchematic(plugin, world, schematic, x-width/2, y, z-length/2, lootMin, lootMax);
			this.plugin.logMessage("Placing " + schematic.getName() + " at " + world.getName() + "," + x + "," + y + "," + z, true);
		}
	}
	
	private int getSurfaceLevel(Block block)
	{
		Material type = block.getType();
		while(block.getY()>5 && !type.equals(Material.GRASS) && !type.equals(Material.DIRT) && !type.equals(Material.WATER))
		{
			block = block.getRelative(BlockFace.DOWN);
			type = block.getType();
		}
		
		return block.getY();
	}
}
