package biz.orgin.minecraft.hothgenerator;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Levelled;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.ChunkGenerator.ChunkData;

public class DagobahOrePopulator extends BlockPopulator
{
	private static int[] iterations = new int[] { 8,  8,  4,  4,  4,  8};
	private static int[] amount =     new int[] {32, 32,  8,  8, 18, 32};
	private static Material[] type =       new Material[] {
		Material.GRAVEL,       // 80
		Material.SAND,         // 80
		Material.CLAY,         // 64
		Material.TERRACOTTA,    // 64
		Material.STONE,        // 70
		Material.AIR           // 80
		};
	private static byte[] data =     new byte[] {0, 0, 0, 0, 0, 0};
	private static int[] maxHeight = new int[] {80, 80, 64, 64, 70, 80};
	//private static int DIRT_id = MaterialManager.toID(Material.DIRT);
	//private static int STONE_id = MaterialManager.toID(Material.STONE);
	//private static int GRASS_id = MaterialManager.toID(Material.GRASS);
	private static Material REPLACE = Material.DIRT;

	@SuppressWarnings("unused")
	private int height;
	@SuppressWarnings("unused")
	private HothGeneratorPlugin plugin;

	public DagobahOrePopulator(int height)
	{
		this.plugin = HothGenerator.getPlugin();
		this.height = height;
	}

	@Override
	public void populate(World world, Random random, Chunk chunk)
	{
            //try{
		/*for (int i = 0; i < type.length; i++)
		{
			for (int j = 0; j < iterations[i]; j++)
			{
				this.vein(chunk, random, random.nextInt(16),
						random.nextInt(maxHeight[i]), random.nextInt(16),
						amount[i], type[i], data[i]);
                                
			}
		}*/
            //Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
            //                @Override
            //                public void run() {
                                populateWater2(chunk, random, 0, Material.WATER);
            //                }
            //},1);
            /*} catch(Exception e) {
                StringWriter sw = new StringWriter();
                PrintWriter pw = new PrintWriter(sw);
                e.printStackTrace(pw);
                Bukkit.getLogger().log(Level.SEVERE, sw.toString());
            }*/
	}
        
        public void populateWater2(Chunk chunk, Random random, int surfaceOffset, Material type)
	{
		for (int j = 0; j < 8; j++)
		{
                        int originX = random.nextInt(16);
			int originY = 26 + random.nextInt(80 + surfaceOffset - 26);
                        int originZ = random.nextInt(16);
                        int amount = 16;
                    if(originY > 64)
                    {
                            amount = 4;
                    }

                    int dx = originX;
                    int dy = originY;
                    int dz = originZ;

                    for (int i = 0; i < amount; i++)
                    {
                        //Bukkit.getLogger().log(Level.INFO, "GW: " + i + " " + amount);
                            int dir = random.nextInt(6);
                            switch(dir)
                            {
                            case 0:
                                dx++;
                                    break;
                            case 1:
                            dx--;
                            break;
                            case 2:
                                dy++;
                                    break;
                            case 3:
                            dy--;
                            break;
                            case 4:
                                dz++;
                                    break;
                            case 5:
                            dz--;
                            break;
                            }

                            dx = dx & 0x0f;
                            dz = dz & 0x0f;

                            if (dy > 127 || dy < 0) {
                                    continue;
                            }

                            Block block = chunk.getBlock(dx, dy, dz);
                            Material oldType = block.getType();
                            if(oldType == Material.DIRT || oldType == Material.STONE || oldType == Material.GRASS_BLOCK)
                            {
                                    block.setType(type, true);
                            }

                    }
		}
	}
	
	private void vein(Chunk chunk, Random random, int originX,
			int originY, int originZ, int amount, Material type, int data)
	{

		int dx = originX;
		int dy = originY;
		int dz = originZ;
		
		for (int i = 0; i < amount; i++)
		{
			int dir = random.nextInt(6);
			switch(dir)
			{
			case 0:
			    dx++;
				break;
			case 1:
		        dx--;
		        break;
			case 2:
			    dy++;
				break;
			case 3:
		        dy--;
		        break;
			case 4:
			    dz++;
				break;
			case 5:
		        dz--;
		        break;
			}
			
			dx = dx & 0x0f;
			dz = dz & 0x0f;

			if (dy > 127 || dy < 0) {
				continue;
			}

			Block block = chunk.getBlock(dx, dy, dz);
			if(block.getType().equals(DagobahOrePopulator.REPLACE))
			{
				block.setType(type);
                                //block.setBlockData(data, false);
				//DataManager.setData(block, data, false);
			}
			
		}
	}
	
	public void populateWater(HothGeneratorPlugin plugin, World world, Random random, ChunkData chunk, int surfaceOffset)
	{
		for (int j = 0; j < 8; j++)
		{
                    List<int[]> water = new ArrayList<>();
			this.waterVein(plugin, world, chunk, random, random.nextInt(16),
					26 + random.nextInt(80 + surfaceOffset - 26), random.nextInt(16),
					Material.WATER, water);
		}
	}
        public void populateWater(Random random, ChunkData chunk, int surfaceOffset)
	{
		for (int j = 0; j < 8; j++)
		{
			this.waterVein(chunk, random, random.nextInt(16),
					26 + random.nextInt(80 + surfaceOffset - 26), random.nextInt(16),
					Material.WATER);
		}
	}

	static class WaterUpdateTask extends HothRunnable {
            private static final long serialVersionUID = 2297078006521966210L;
            private List<int[]> list;
            private Random random;
            private Material type;
            public WaterUpdateTask(World world, Random random, List<int[]> list, Material type)
            {
            	this.setName("WaterUpdate");
		this.setWorld(world);
		this.setPlugin(null);
                this.list = list;
                this.random = random;
                this.type = type;
            }
            public String getParameterString() {
		return list.toString();
            }
            @Override
            public void run(){
                int len = list.size();
                World world = this.getWorld();
                for(int i = 0; i < len; i++) {
                    int[] pos = list.get(i);
                    int x = pos[0], y = pos[1], z = pos[2];
                    //Bukkit.getLogger().log(Level.INFO, x + " " + y + " " + z);
                    //world.getBlockAt(x, y, z).getState().update(true, true);
                    Block block = world.getBlockAt(x, y, z);
                    block.setType(type, true);
                    /*Levelled levelled = (Levelled) block.getBlockData();
                    levelled.setLevel(0);
                    block.setBlockData(levelled);
                    Block tmp = world.getBlockAt(x + 1, y, z);
                    if(tmp.getType() == Material.AIR) {
                        tmp.setType(Material.VOID_AIR, true);
                        continue;
                    }
                    tmp = world.getBlockAt(x - 1, y, z);
                    if(tmp.getType() == Material.AIR) {
                        tmp.setType(Material.VOID_AIR, true);
                        continue;
                    }
                    tmp = world.getBlockAt(x, y + 1, z);
                    if(tmp.getType() == Material.AIR) {
                        tmp.setType(Material.VOID_AIR, true);
                        continue;
                    }
                    tmp = world.getBlockAt(x, y - 1, z);
                    if(tmp.getType() == Material.AIR) {
                        tmp.setType(Material.VOID_AIR, true);
                        continue;
                    }
                    tmp = world.getBlockAt(x, y, z + 1);
                    if(tmp.getType() == Material.AIR) {
                        tmp.setType(Material.VOID_AIR, true);
                        continue;
                    }
                    tmp = world.getBlockAt(x, y, z - 1);
                    if(tmp.getType() == Material.AIR) {
                        tmp.setType(Material.VOID_AIR, true);
                        continue;
                    }*/
                }
            }
        }

        
	private void waterVein(HothGeneratorPlugin plugin, World world, ChunkData chunk, Random random, int originX,
			int originY, int originZ, Material type, List<int[]> water)
	{
		int amount = 16;
		if(originY > 64)
		{
			amount = 4;
		}
		
		
		int dx = originX;
		int dy = originY;
		int dz = originZ;
		
		for (int i = 0; i < amount; i++)
		{
			int dir = random.nextInt(6);
			switch(dir)
			{
			case 0:
			    dx++;
				break;
			case 1:
		        dx--;
		        break;
			case 2:
			    dy++;
				break;
			case 3:
		        dy--;
		        break;
			case 4:
			    dz++;
				break;
			case 5:
		        dz--;
		        break;
			}
			
			dx = dx & 0x0f;
			dz = dz & 0x0f;

			if (dy > 127 || dy < 0) {
				continue;
			}

			//short oldType = HothUtils.getPos(chunk, dx, dy, dz);
                        Material oldType = chunk.getType(dx, dy, dz);
			if(oldType == Material.DIRT || oldType == Material.STONE || oldType == Material.GRASS_BLOCK)
			{
				//HothUtils.setPos(chunk, dx, dy, dz, type);
                                //chunk.setBlock(dx, dy, dz, Material.VOID_AIR);
                                water.add(new int[]{dx,dy,dz});
			}
			
		}
                plugin.addTask(new WaterUpdateTask(world, random, water, type));
	}
        
        private void waterVein(ChunkData chunk, Random random, int originX,
			int originY, int originZ, Material type)
	{
		int amount = 16;
		if(originY > 64)
		{
			amount = 4;
		}
		
		
		int dx = originX;
		int dy = originY;
		int dz = originZ;
		
		for (int i = 0; i < amount; i++)
		{
			int dir = random.nextInt(6);
			switch(dir)
			{
			case 0:
			    dx++;
				break;
			case 1:
		        dx--;
		        break;
			case 2:
			    dy++;
				break;
			case 3:
		        dy--;
		        break;
			case 4:
			    dz++;
				break;
			case 5:
		        dz--;
		        break;
			}
			
			dx = dx & 0x0f;
			dz = dz & 0x0f;

			if (dy > 127 || dy < 0) {
				continue;
			}

			//short oldType = HothUtils.getPos(chunk, dx, dy, dz);
                        Material oldType = chunk.getType(dx, dy, dz);
			if(oldType == Material.DIRT || oldType == Material.STONE || oldType == Material.GRASS_BLOCK)
			{
				//HothUtils.setPos(chunk, dx, dy, dz, type);
                                chunk.setBlock(dx, dy, dz, type);
			}
			
		}
	}
	
}
