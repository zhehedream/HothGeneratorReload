package biz.orgin.minecraft.hothgenerator;

import java.util.Random;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.TreeSpecies;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.type.Leaves;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.material.Tree;
import org.bukkit.material.Vine;

public class DagobahHugeTreePopulator  extends BlockPopulator
{
	private int height;
	private HothGeneratorPlugin plugin;

	public DagobahHugeTreePopulator(int height)
	{
		this.plugin = HothGenerator.getPlugin();
		this.height = height;
	}

	@Override
	public void populate(World world, Random random, Chunk chunk)
	{
		int rand = 1;
		int rarity = ConfigManager.getStructureHugeTreeRarity(this.plugin, world); 

		if(rarity!=0)
		{
			if(rand == random.nextInt((rarity * 40)))
			{
				this.placeHugeTree(world, random, chunk);
			}
		}			
	}
	
	private void placeHugeTree(World world, Random random, Chunk chunk)
	{
		int x = 16*chunk.getX() + 8 + random.nextInt(16);
		int z = 16*chunk.getZ() + 8 + random.nextInt(16);
		
		Block block = world.getBlockAt(x, 64, z);
		
		for(int y=this.height;y>64;y--)
		{
			block = world.getBlockAt(x, y, z);
			if(block.getType().equals(Material.GRASS_BLOCK) || block.getType().equals(Material.DIRT))
			{
				break;
			}
		}
		
		int y = block.getY();
		this.renderHugeTreeAt(world, random, x, y, z);
		this.plugin.logMessage("Placing huge tree at " + world.getName() + "," + x + "," + y + "," + z, true);

	}

	private void renderHugeTreeAt(World world, Random random, int x, int y, int z)
	{
		this.placeRootsAt(world, random, x, y, z);
		this.placeStemAt(world, random, x, y, z);
	}
	
	private void placeStemAt(World world, Random random, int x, int y, int z)
	{
		for(int i=0;i<5;i++)
		{
			this.addStemRing(world, random, x, y+i, z, 2);
		}
		for(int i=5;i<10;i++)
		{
			this.addStemRing(world, random, x, y+i, z, 1);
		}
		double radians = 2*Math.PI*random.nextDouble();
		int height = 20 + random.nextInt(5);
		
		double dx = Math.cos(radians)/20;
		double dz = Math.sin(radians)/20;

		int xx;
		int zz;
		for(int i=0;i<height;i++)
		{
			xx = (int) (x + dx*i);
			zz = (int) (z + dz*i);
			this.addStemRing(world, random, xx, y+i+10, zz, 1);
			dx=dx*1.1;
			dz=dz*1.1;
			
			if(i!=0 && i%5==0)
			{
				this.addBranchesAt(world, random, xx, y+i+10, zz, i);
			}
		}
	}
	
	private void addBranchesAt(World world, Random random, int x, int y, int z, int lengthMod)
	{
		int count = 4+random.nextInt(4);
		
		double rotate = (random.nextDouble()*2*Math.PI);
		
		for(int cnt = 0;cnt<count;cnt++)
		{		
			int length = 16 + random.nextInt(2) - lengthMod/2;
			
			double radians = (2*Math.PI * cnt / count) + rotate;  // 2*Math.PI*random.nextDouble();
			
			double dx = Math.cos(radians);
			double dz = Math.sin(radians);
			
			double dy = 0.6;
			
			int oldyy = (int) (y - (dy-1.4));
			
			BlockFace direction;
			if(Math.abs(dx) > Math.abs(dz))
			{
				direction = BlockFace.EAST;
			}
			else
			{
				direction = BlockFace.NORTH;
			}
			
			for(int i=0;i<length;i++)
			{
				int xx = (int) (x + dx*i);
				int zz = (int) (z + dz*i);
				int yy = (int) (y - (dy-1.4)*i);

				Block block= null;
				
				int k=0;
				do
				{
					block = world.getBlockAt(xx, yy-k, zz);
					if(block.getType().equals(Material.DIRT) || block.getType().equals(Material.GRASS_BLOCK) || block.getType().equals(Material.STONE))
					{
						break; // stop rendering this branch
					}
                                        block.setType(Material.OAK_LOG);
					BlockState state = block.getState();
					Tree tree = (Tree)state.getData();
					tree.setDirection(direction);
					tree.setSpecies(TreeSpecies.GENERIC);
					state.update(true, false);
					k++;
				}
				while(k<Math.abs(oldyy-yy));
				
				if(block!=null && i==length-1)
				{
					Block curr = block;
					HothSet leaves = new HothSet();
					this.addCrown(world, random, leaves, curr.getRelative(BlockFace.DOWN));
					int height = (int)((10-lengthMod/10)*0.75); 
					int size = height;
					this.addLeafRing(world, random, leaves, curr, size, 0, size*0.3);
					curr = curr.getRelative(BlockFace.DOWN);
					this.addLeafRing(world, random, leaves, curr, size, 0, size*0.7);
					curr = curr.getRelative(BlockFace.DOWN);
					this.addLeafRing(world, random, leaves, curr, size, size-size*0.5, size*0.9);
					curr = curr.getRelative(BlockFace.DOWN);
					this.addLeafRing(world, random, leaves, curr, size, size-size*0.2, size);
					//this.addLeafRingVines(world, random, leaves, curr, size, size-size*0.2, size, 4);
					
					this.renderBlob(world, leaves.toArray(), y);
				}
				dy = (dy * (1.065));
				
				oldyy = yy;
			}
		}
	}
	
	private void addCrown(World world, Random random, HothSet leaves, Block block)
	{
		Block[] blocks = new Block[]{
				block.getRelative(BlockFace.NORTH),
				block.getRelative(BlockFace.EAST),
				block.getRelative(BlockFace.SOUTH),
				block.getRelative(BlockFace.WEST),
				block.getRelative(BlockFace.DOWN)
				};
		
		BlockFace[] faces = new BlockFace[]{
				BlockFace.NORTH,
				BlockFace.EAST,
				BlockFace.SOUTH,
				BlockFace.WEST,
				BlockFace.DOWN
		};
		
		Material[] types = new Material[]{
				Material.OAK_LOG,
				Material.OAK_LOG,
				Material.OAK_LOG,
				Material.OAK_LOG,
				Material.GLOWSTONE
		};
	
		for(int i=0;i<blocks.length;i++)
		{
			Block logBlock = blocks[i];
			BlockState state = logBlock.getState();
			state.setType(types[i]);
			if(types[i].equals(Material.OAK_LOG))
			{
				Tree dirlog = (Tree)state.getData();
				dirlog.setDirection(faces[i]);
				dirlog.setSpecies(TreeSpecies.GENERIC);
			}
			leaves.add(new Position(state));
		}
	}
	
	private void addLeafRing(World world, Random random, HothSet leaves, Block block, int size, double inner, double outer)
	{
		int x = block.getX();
		int y = block.getY();
		int z = block.getZ();
		
		for(int i=x-size;i<x+size+1;i++)
		{
			for(int j=z-size;j<z+size+1;j++)
			{
				double dist = Math.sqrt( (double)( (i-x)*(i-x)+(j-z)*(j-z) ) );
				
				if(dist>=inner && dist<=outer)
				{
					Block newBlock = world.getBlockAt(i, y, j);
                                        newBlock.setType(Material.OAK_LEAVES);
                                        Leaves leavesData = (Leaves) newBlock.getBlockData();
                                        leavesData.setPersistent(true);
                                        newBlock.setBlockData(leavesData);
                                        BlockState state = newBlock.getState();

					Position newPos = new Position(state);
					if(!leaves.contains(newPos))
					{
						leaves.add(newPos);
					}
				}
			}
		}
		
	}

	private void addLeafRingVines(World world, Random random, HothSet tree, Block block, int size, double inner, double outer, int vineProbability)
	{
		// Add vines
		int x = block.getX();
		int y = block.getY();
		int z = block.getZ();
		
		Position test = new Position();
		test.y = y;
		for(int i=x-size;i<x+size+1;i++)
		{
			for(int j=z-size;j<z+size+1;j++)
			{
				int dx = x-i;
				int dz = z-j;
				
				test.x = i;
				test.z = j;
				
				Position pos = tree.get(test);
				
				if(pos!=null && pos.blockState.getType().equals(Material.OAK_LEAVES))
				{
				
				if(dx<0)
				{
					// EAST
					test.x = i+1;
					test.z = j;
					if(!tree.contains(test))
					{
						Block vBlock = world.getBlockAt(test.x, test.y, test.z);
						this.addVinesToLeaf(world, random, tree, vBlock, BlockFace.EAST, 3);
					}
				}
				else if(dx>0)
				{
					// WEST
					test.x = i-1;
					test.z = j;
					if(!tree.contains(test))
					{
						Block vBlock = world.getBlockAt(test.x, test.y, test.z);
						this.addVinesToLeaf(world, random, tree, vBlock, BlockFace.WEST, 3);
					}
				}
				
				if(dz<0)
				{
					// SOUTH
					test.x = i;
					test.z = j+1;
					if(!tree.contains(test))
					{
						Block vBlock = world.getBlockAt(test.x, test.y, test.z);
						this.addVinesToLeaf(world, random, tree, vBlock, BlockFace.SOUTH, 3);
					}
				}
				else if(dz>0)
				{
					// NORTH
					test.x = i;
					test.z = j-1;
					if(!tree.contains(test))
					{
						Block vBlock = world.getBlockAt(test.x, test.y, test.z);
						this.addVinesToLeaf(world, random, tree, vBlock, BlockFace.NORTH, 3);
					}
				}
				}
			}
		}
	}
	
	private void addVinesToLeaf(World world, Random random, HothSet tree, Block block, BlockFace face, int vineProbability)
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
		
		while(curr.isEmpty())
		{
			BlockState state = curr.getState();
			state.setType(Material.VINE);
			Vine vine = (Vine)state.getData();
			vine.putOnFace(reverse);
			tree.add(new Position(state));
			curr = curr.getRelative(BlockFace.DOWN);
		}
	}

	private void addStemRing(World world, Random random, int x, int y, int z, double size)
	{
		
		for(int i=(int) (x-size);i<x+size+1;i++)
		{
			for(int j=(int) (z-size);j<z+size+1;j++)
			{
				double dist = Math.sqrt( (double)( (i-x)*(i-x)+(j-z)*(j-z) ) );
				
				if(dist<=size)
				{
					Block newBlock = world.getBlockAt(i, y, j);
                                        newBlock.setType(Material.OAK_LOG);
					BlockState state = newBlock.getState();
					Tree tree = (Tree)state.getData();
					tree.setSpecies(TreeSpecies.GENERIC);
					state.update(true, false);
				}
			}
		}
	}		
	
	private void placeRootsAt(World world, Random random, int x, int y, int z)
	{
		int roots = 8 + random.nextInt(8);
		
		for(int i=0;i<roots;i++)
		{
			double radians = 2*Math.PI*random.nextDouble();
			
			double dy = (2+random.nextInt(2)) * 0.6;
			int height = 9 + random.nextInt(4);
			
			int j=2;
			while(height>1 || j<18)
			{
				double newRadians = radians + Math.cos((4*j+i*36)*Math.PI / 180);

				double dx = Math.cos(newRadians);
				double dz = Math.sin(newRadians);

				int currx = (int)Math.floor(x + dx*j*0.7);
				int currz = (int)Math.floor(z + dz*j*0.7);
				
				
				Block block = world.getBlockAt(currx, y+height-3, currz);
				Material type=block.getType();
				while(!type.equals(Material.DIRT) && !type.equals(Material.STONE) && block.getY()>64)
				{
                                        block.setType(Material.OAK_LOG);
					BlockState state = block.getState();
					Tree tree = (Tree)state.getData();
					tree.setSpecies(TreeSpecies.GENERIC);
					tree.setDirection(BlockFace.UP);
					state.update(true, false);
					block = block.getRelative(BlockFace.DOWN);
					type = block.getType();
				}
				
				dy = dy * 0.45;
				if(dy<0.5)
				{
					dy = 0.75;
				}
				height = (int)(height - dy);
				
				j++;
				
			}
		}
	}
	
	private void renderBlob(World world, Position[] blob, int y)
	{
		// Check for free space
		for(int i=0;i<blob.length;i++)
		{
			BlockState state = blob[i].blockState;
			int yy = state.getY();
			if(yy-y > 2)
			{
				Material newType = state.getType();
				if(newType.equals(Material.OAK_LOG))
				{
					Block block = state.getBlock();
					Material oldType = block.getType();
					if(oldType.equals(Material.DIRT)
							|| oldType.equals(Material.STONE)
							|| oldType.equals(Material.GRASS_BLOCK)
							)
					{
						return; // Can't render this tree.
					}
				}
			}
		}
		
		// Insert it
		// Logs replace everything. Leafs only replace transparent blocks.
		for(int i=0;i<blob.length;i++)
		{
			BlockState state = blob[i].blockState;
			Block block = state.getBlock();
			Material newType = state.getType();
			
			if(newType.equals(Material.OAK_LOG))
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
