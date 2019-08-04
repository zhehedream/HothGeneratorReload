package biz.orgin.minecraft.hothgenerator;

import static biz.orgin.minecraft.hothgenerator.DagobahSpiderForestPopulator.isLog;
import java.util.Random;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.TreeSpecies;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.type.Leaves;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.material.Tree;
import org.bukkit.material.Vine;

public class DagobahRootPopulator  extends BlockPopulator
{
	@SuppressWarnings("unused")
	private int height;
	@SuppressWarnings("unused")
	private HothGeneratorPlugin plugin;

	public DagobahRootPopulator(int height)
	{
		this.plugin = HothGenerator.getPlugin();
		this.height = height;
	}

	@Override
        public void populate(World world, Random random, Chunk chunk) {
            ;
        }
	public void otherPopulate(World world, Random random, Chunk chunk)
	{
		int rand = random.nextInt(10);
		if(rand>5)
		{
			for(int i=6;i<rand;i++)
			{
				this.placeRoot(world, random, chunk);
			}
		}
	}
	
	private void placeRoot(World world, Random random, Chunk chunk)
	{
		int x = 16*chunk.getX() + 3 + random.nextInt(10);
		int z = 16*chunk.getZ() + 3 + random.nextInt(10);
		
		Biome biome = world.getBiome(x, z);
		
		if(!biome.equals(Biome.MUSHROOM_FIELDS) && !biome.equals(Biome.MUSHROOM_FIELD_SHORE))
		{

		
			Block block = world.getHighestBlockAt(x, z);
			block = world.getBlockAt(x, block.getY()-1, z);
			while(HothUtils.isTransparent(block.getRelative(BlockFace.DOWN)))
			{
				block = block.getRelative(BlockFace.DOWN);
			}
			Material type = block.getType();
			if(type.equals(Material.DIRT) || type.equals(Material.GRASS_BLOCK))
			{
				this.renderRootAt(world, random, x, block.getY(), z);
			}
			else if(type.equals(Material.WATER)) // Depth 1 water
			{
				block = world.getBlockAt(x, block.getY()-1, z);
				type = block.getType();
				if(type.equals(Material.DIRT))
				{
					this.renderRootAt(world, random, x, block.getY(), z);
				}
				else if(type.equals(Material.WATER)) // Depth 2 water
				{
					block = world.getBlockAt(x, block.getY()-1, z);
					type = block.getType();
					if(type.equals(Material.DIRT))
					{
						this.renderRootAt(world, random, x, block.getY(), z);
					}
				}
			}
		}
	}
	
	private void renderRootAt(World world, Random random, int x, int y, int z)
	{
		HothSet root = new HothSet();
		
		int maxy = 1 + random.nextInt(3);
		int miny = random.nextInt(3);
		
		int sections = 5 + random.nextInt(10);

		Block block = world.getBlockAt(x,y,z);
		BlockFace direction = this.getRandomDirection(random);
		
		boolean split = false;
		
		for(int i=0;i<sections;i++)
		{
			int len = 2+random.nextInt(7);
			
			direction = this.getNewRandomDirection(random, direction);
			if(direction.equals(BlockFace.UP) || direction.equals(BlockFace.DOWN))
			{
				len = 1+random.nextInt(2);
			}

			for(int j=1;j<len;j++)
			{
				int q=0;
				while(this.getFreefall(block)>maxy && q<5)
				{
                                        block.setType(Material.OAK_LOG);
					BlockState state = block.getState();
					Tree dirlog = (Tree)state.getData();
					dirlog.setDirection(BlockFace.UP);
					dirlog.setSpecies(TreeSpecies.GENERIC);
					root.add(new Position(state));
					block = block.getRelative(BlockFace.DOWN);
					q++;
				}
				if(q!=0)
				{
					break; //next section
				}

				if(q==0)
				{
					while(this.getDepth(block)>miny && q<5)
					{
                                                block.setType(Material.OAK_LOG);
						BlockState state = block.getState();
						Tree dirlog = (Tree)state.getData();
						dirlog.setDirection(BlockFace.DOWN);
						dirlog.setSpecies(TreeSpecies.GENERIC);
						root.add(new Position(state));
						block = block.getRelative(BlockFace.UP);
						q++;
					}

					if(q!=0)
					{
						break; // next section
					}
				}
                                block.setType(Material.OAK_LOG);
				BlockState state = block.getState();
				Tree dirlog = (Tree)state.getData();
				dirlog.setDirection(direction);
				dirlog.setSpecies(TreeSpecies.GENERIC);
				root.add(new Position(state));
				
				block = block.getRelative(direction);
			}
			
			if(random.nextInt(320)==1 && !split) // Split the log, rarely
			{
				split = true;
				this.renderRootAt(world, random, block.getX(), block.getY(), block.getZ());
			}
			
			// add some foliage
			this.addtinyTop(world, random, root, block.getRelative(BlockFace.UP));
		}
		
		this.renderBlob(world, root.toArray());
	}
	
	private void addtinyTop(World world, Random random, HothSet root, Block block)
	{
		int x = block.getX();
		int y = block.getY();
		int z = block.getZ();
		
		if(block.isEmpty())
		{
                        block.setType(Material.OAK_LOG);
			BlockState state = block.getState();
			Tree tree = (Tree)state.getData();
			tree.setDirection(BlockFace.UP);
			tree.setSpecies(TreeSpecies.GENERIC);
			root.add(new Position(state));
		}

		Block t = world.getBlockAt(x, y+1, z);
		if(HothUtils.isTransparent(t))
		{
			this.addLeaf(world, random, root, t, null, null, 0);
		}
		t = world.getBlockAt(x+1, y, z);
		if(HothUtils.isTransparent(t))
		{
			this.addLeaf(world, random, root, t, BlockFace.EAST, null, 8);
		}
		t = world.getBlockAt(x-1, y, z);
		if(HothUtils.isTransparent(t))
		{
			this.addLeaf(world, random, root, t, BlockFace.WEST, null, 8);
		}
		t = world.getBlockAt(x, y, z+1);
		if(HothUtils.isTransparent(t))
		{
			this.addLeaf(world, random, root, t, BlockFace.SOUTH, null, 8);
		}
		t = world.getBlockAt(x, y, z-1);
		if(HothUtils.isTransparent(t))
		{
			this.addLeaf(world, random, root, t, BlockFace.NORTH, null, 8);
		}
		
		if((x+z)%2>0)
		{
			t = world.getBlockAt(x+1, y, z+1);
			if(HothUtils.isTransparent(t))
			{
				this.addLeaf(world, random, root, t, BlockFace.EAST, BlockFace.SOUTH, 5);
			}
			t = world.getBlockAt(x+1, y, z-1);
			if(HothUtils.isTransparent(t))
			{
				this.addLeaf(world, random, root, t, BlockFace.EAST, BlockFace.NORTH, 5);
			}
			t = world.getBlockAt(x-1, y, z+1);
			if(HothUtils.isTransparent(t))
			{
				this.addLeaf(world, random, root, t, BlockFace.WEST, BlockFace.SOUTH, 5);
			}
			t = world.getBlockAt(x-1, y, z-1);
			if(HothUtils.isTransparent(t))
			{
				this.addLeaf(world, random, root, t, BlockFace.WEST, BlockFace.NORTH, 5);
			}
		}

	}
	
	private void addLeaf(World world, Random random, HothSet root, Block block, BlockFace face1, BlockFace face2, int vineProbability)
	{
                block.setType(Material.OAK_LEAVES);
		BlockData data = block.getBlockData();
                Leaves leavesData = (Leaves) data;
                leavesData.setPersistent(true);
                block.setBlockData(leavesData);
		
		Position position = new Position(block.getState());
		if(!root.contains(position)) // Only add if it doesn't exist
		{
			root.add(position);
			if(face1!=null)
			{
				this.addVinesToLeaf(world, random, root, block.getRelative(face1), face1, vineProbability);
			}
			if(face2!=null)
			{
				this.addVinesToLeaf(world, random, root, block.getRelative(face2), face2, vineProbability);
			}
		}
	}
	
	private void addVinesToLeaf(World world, Random random, HothSet root, Block block, BlockFace face, int vineProbability)
	{
		int addVine = random.nextInt(10);
		if(addVine>vineProbability)
		{
			return;
		}
		
		BlockFace reverse = face;
		if(face.equals(BlockFace.NORTH))
		{
			reverse = BlockFace.SOUTH;
		}
		else if(face.equals(BlockFace.EAST))
		{
			reverse = BlockFace.WEST;
		}
		else if(face.equals(BlockFace.SOUTH))
		{
			reverse = BlockFace.NORTH;
		}
		else if(face.equals(BlockFace.WEST))
		{
			reverse = BlockFace.EAST;
		}
		
		Block curr = block;
		
		BlockState state = curr.getState();
		Position newPos = new Position(state);

		while(curr.isEmpty() && !root.contains(newPos))
		{
			state.setType(Material.VINE);
			Vine vine = (Vine)state.getData();
			vine.putOnFace(reverse);
			root.add(newPos);
			curr = curr.getRelative(BlockFace.DOWN);
			
			state = curr.getState();
			newPos = new Position(state);
		}
	}


	
	private int getFreefall(Block block)
	{
		
		int i = 0;
		Block runner = block.getRelative(BlockFace.DOWN);
		Material type = runner.getType();
		while(HothUtils.isTransparent(runner) || type.equals(Material.OAK_LOG) && i<10)
		{
			i++;
			runner = runner.getRelative(BlockFace.DOWN);
			type = runner.getType();
		}
		return i;
	}
	
	private int getDepth(Block block)
	{
		int i = 0;
		Block runner = block.getRelative(BlockFace.UP);
		Material type = runner.getType();
		while(!HothUtils.isTransparent(runner) && !type.equals(Material.OAK_LOG) && i<10)
		{
			i++;
			runner = runner.getRelative(BlockFace.UP);
			type = runner.getType();
		}
		return i;
	}

	private BlockFace getNewRandomDirection(Random random, BlockFace old)
	{
		BlockFace newDirection = this.getRandomDirection(random);
		while(old.equals(newDirection))
		{
			newDirection = this.getRandomDirection(random);
		}
		return newDirection;
	}

	private BlockFace getRandomDirection(Random random)
	{
		int rand = random.nextInt(6);
		switch(rand)
		{
		case 0: return BlockFace.NORTH;
		case 1: return BlockFace.EAST;
		case 2: return BlockFace.SOUTH;
		case 3: return BlockFace.WEST;
		case 4: return BlockFace.UP;
		case 5: return BlockFace.DOWN;
		}
		
		return BlockFace.SOUTH;
	}
	
	private void renderBlob(World world, Position[] blob)
	{
		// Insert it
		// Logs replace everything. Leafs only replace transparent blocks.
		for(int i=0;i<blob.length;i++)
		{
			BlockState state = blob[i].blockState;
			Block block = state.getBlock();
			Material newType = state.getType();
			
			if(isLog(newType))
			{
				state.update(true, false);
			}
			else if(newType.equals(Material.GLOWSTONE))
			{
				state.update(true, false);
			}
			else
			{
				if(HothUtils.isTransparent(block))
				{
					state.update(true, false);
				}
			}
		}
	}
}
