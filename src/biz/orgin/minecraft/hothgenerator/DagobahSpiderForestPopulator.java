package biz.orgin.minecraft.hothgenerator;

import java.util.Random;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.inventory.Inventory;

public class DagobahSpiderForestPopulator extends BlockPopulator
{
	@SuppressWarnings("unused")
	private int height;
	private HothGeneratorPlugin plugin;
	private NoiseGenerator noiseGenerator;
	private LootGenerator lootGenerator;

	public DagobahSpiderForestPopulator(int height)
	{
		this.plugin = HothGenerator.getPlugin();
		this.height = height;
		this.noiseGenerator = null;
		this.lootGenerator = null;
	}
	
	@Override
	public void populate(World world, Random random, Chunk chunk)
	{
		if(this.noiseGenerator==null)
		{
			this.noiseGenerator = new NoiseGenerator(world);
		}
		
		if(this.lootGenerator==null)
		{
			this.lootGenerator = LootGenerator.getLootGenerator();
		}
		
		int rand = 223;
		int rarity = ConfigManager.getStructureSpiderForestRarity(this.plugin, world); 

		if(rarity!=0)
		{
			if(rand == random.nextInt((rarity*1132/2)))
			{
				this.placeSpiderForest(world, random, chunk);
			}	
		}			
	}
	
	private void placeSpiderForest(World world, Random random, Chunk chunk)
	{
		int x = 16*chunk.getX() + 8+random.nextInt(16);
		int z = 16*chunk.getZ() + 8+random.nextInt(16);
		
		plugin.logMessage("Placing spider forest at " + world.getName() + ","+ x + "," + z, true);

		
		int size = 40 + random.nextInt(10);
		
		this.placeWeb(world, random, x, z, size);
	}
        
        public static boolean isLog(Material m) {
            if(m == null) return false;
            if(m.equals(Material.OAK_LOG) || m.equals(Material.SPRUCE_LOG) || m.equals(Material.DARK_OAK_LOG) 
                    || m.equals(Material.BIRCH_LOG) || m.equals(Material.JUNGLE_LOG) || m.equals(Material.ACACIA_LOG)) return true;
            return false;
        }
	
	private void placeWeb(World world, Random random, int x, int z, int size)
	{
		int y = this.getSurfaceLevel(world.getHighestBlockAt(x,  z)); 
		y = y -2;
		int height = y + 3 + size/2;
		
		this.plugin.addSpiderForest(world,x,y,z,size);

		// Place chests
		int chestSize = (int)(size * 0.2);
		for(int xx=x-chestSize;xx<x+chestSize;xx++)
		{
			int rx = xx-x;
			for(int zz=z-chestSize;zz<z+chestSize;zz++)
			{
				int doChest = random.nextInt(25);
				if(doChest==7)
				{
					int rz = zz-z;
					if(Math.sqrt(rx*rx + rz*rz)<chestSize)
					{
						int yy = 1+this.getSurfaceLevel(world.getHighestBlockAt(xx, zz));
						Block block = world.getBlockAt(xx, yy, zz);
						Material mat = block.getType();
						if(!mat.equals(Material.OAK_LOG))
						{
							block.setType(Material.CHEST);
							Chest chest = (Chest)block.getState();
							org.bukkit.material.Chest data = (org.bukkit.material.Chest)chest.getData();
							int dir = random.nextInt(4);
							switch(dir)
							{
							case 0: data.setFacingDirection(BlockFace.NORTH);break;
							case 1: data.setFacingDirection(BlockFace.EAST);break;
							case 2: data.setFacingDirection(BlockFace.SOUTH);break;
							case 3: data.setFacingDirection(BlockFace.WEST);break;
							}
							
							Inventory inv = chest.getInventory();
							lootGenerator.getLootInventory(inv, 1, 10);
							chest.update(true, false);
							
						}
					}
				}
			}
		}
		
		// Place web
		for (int yy=y;yy<height;yy++)
		{
			for(int xx=x-size;xx<x+size;xx++)
			{
				int rx = xx-x;
				for(int zz=z-size;zz<z+size;zz++)
				{
					int rz = zz-z;
					if(Math.sqrt(rx*rx + rz*rz)<(size-(yy-y)))
					{
						double d = this.noiseGenerator.noise(xx, yy, zz, 4, 27)*(height+1-y);
						
						if(d>yy-y)
						{
							Block block = world.getBlockAt(xx, yy,zz);
							if(DagobahSpiderForestPopulator.isAttachable(block))
							{
								BlockState state = block.getState();
								state.setType(Material.COBWEB);
								state.update(true, false);
							}
						}
					}
				}
			}
		}
	}
	
	public static boolean isAttachable(Block block)
	{
		if(!block.isEmpty())
		{
			return false;
		}
		
		Block blk = block.getRelative(BlockFace.UP);
		if(!DagobahSpiderForestPopulator.isKindaEmpty(blk))
		{
			return true;
		}
		blk = block.getRelative(BlockFace.DOWN);
		if(!DagobahSpiderForestPopulator.isKindaEmpty(blk))
		{
			return true;
		}
		blk = block.getRelative(BlockFace.EAST);
		if(!DagobahSpiderForestPopulator.isKindaEmpty(blk))
		{
			return true;
		}
		blk = block.getRelative(BlockFace.WEST);
		if(!DagobahSpiderForestPopulator.isKindaEmpty(blk))
		{
			return true;
		}
		blk = block.getRelative(BlockFace.NORTH);
		if(!DagobahSpiderForestPopulator.isKindaEmpty(blk))
		{
			return true;
		}
		blk = block.getRelative(BlockFace.SOUTH);
		if(!DagobahSpiderForestPopulator.isKindaEmpty(blk))
		{
			return true;
		}
		
		return false;
	}
	
	private static boolean isKindaEmpty(Block block)
	{
		if(block.isEmpty())
		{
			return true;
		}
		
		Material mat = block.getType();
		
		if(mat.equals(Material.VINE)
				|| mat.equals(Material.COBWEB)
				|| mat.equals(Material.GRASS)
				|| mat.equals(Material.MYCELIUM)
				|| mat.equals(Material.WATER)
				|| mat.equals(Material.WATER)
				|| mat.equals(Material.LILY_PAD)
				|| mat.equals(Material.RED_MUSHROOM)
				|| mat.equals(Material.BROWN_MUSHROOM)
				|| mat.equals(Material.TALL_GRASS)
				)
		{
			return true;
		}

		return false;
	}
	
	private int getSurfaceLevel(Block block)
	{
		Material type = block.getType();
		while(block.getY()>64 && !type.equals(Material.GRASS) && !type.equals(Material.DIRT)) //  && !type.equals(Material.STATIONARY_WATER))
		{
			block = block.getRelative(BlockFace.DOWN);
			type = block.getType();
		}
		
		return block.getY();
	}
}
