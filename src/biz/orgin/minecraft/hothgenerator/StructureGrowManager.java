package biz.orgin.minecraft.hothgenerator;

import static biz.orgin.minecraft.hothgenerator.BlockGrowManager.isSapling;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.TreeSpecies;
import org.bukkit.TreeType;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.world.StructureGrowEvent;
import org.bukkit.material.Tree;

/**
 * Prevent trees and mushrooms from growing under open sky in hoth and on unwatered blocks in tatooine.
 * Makes regular trees spawn dagobah trees in dagobah.
 * Makes regular trees spawn stunted trees in mustafar and prevents all other trees from growing.
 * @author orgin
 *
 */
public class StructureGrowManager implements Listener
{
	private HothGeneratorPlugin plugin;
	private Random random;

	public StructureGrowManager(HothGeneratorPlugin plugin)
	{
		this.plugin = plugin;
		this.random = new Random(System.currentTimeMillis());
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onStructureGrow(StructureGrowEvent event)
	{
		if(!event.isCancelled())
		{
			World world = event.getWorld();
			
			Location location = event.getLocation();
			
			
			if(this.plugin.isHothWorld(world))
			{
				WorldType worldtype = this.plugin.getWorldType(world);

				if(worldtype == WorldType.HOTH || worldtype == WorldType.TATOOINE)
				{
					if(!ConfigManager.isRulesPlantsgrow(this.plugin, location))
					{
						int maxy = world.getHighestBlockYAt(location);
						
						if(Math.abs(maxy-location.getBlockY())<2)
						{
							if(worldtype == WorldType.HOTH || (worldtype == WorldType.TATOOINE && !HothUtils.isWatered(location.getBlock().getRelative(BlockFace.DOWN))))
							{
								event.setCancelled(true);
							}
						}
					}
				}
				
				else if(worldtype == WorldType.DAGOBAH)
				{
					TreeType type = event.getSpecies();
					
					// Prevent regular trees from generating
					if(type.equals(TreeType.TREE) || type.equals(TreeType.BIG_TREE))
					{
						int x = location.getBlockX();
						int y = location.getBlockY();
						int z = location.getBlockZ();

						plugin.addTask(new PlaceDagobahTree(world, x, y, z), true);
	
						// Always cancel the event
						event.setCancelled(true);
					}
				}
				
				else if(worldtype == WorldType.MUSTAFAR)
				{
					try
					{
						MustafarSaplingThread th = new MustafarSaplingThread(location.getBlock(), event.getSpecies());
						Bukkit.getScheduler().scheduleSyncDelayedTask(this.plugin, th);
						event.setCancelled(true);
					}
					catch(Exception e)
					{
						this.plugin.logMessage("Exception while trying to register MustafarSaplingThread. You probably need to restart yoru server.", true);
					}

					
				}

			}
		}
	}
	

	private class MustafarSaplingThread implements Runnable
	{
		private Block block;
		private TreeType type;
		
		private MustafarSaplingThread(Block block, TreeType type)
		{
			this.block = block;
			this.type = type;
		}
		
		@Override
		public void run()
		{
			Location location = block.getLocation();
			World world = block.getWorld();
			
			if(HothUtils.isTooHot(location,2))
			{
				if(type.equals(TreeType.BROWN_MUSHROOM) || type.equals(TreeType.RED_MUSHROOM))
				{
					block.breakNaturally();
					world.playEffect(location, Effect.SMOKE, BlockFace.UP);
				}
				else
				{
					block.setType(Material.DEAD_BUSH);
					world.playEffect(location, Effect.SMOKE, BlockFace.UP);
				}
			}
			else
			{
				if(type.equals(TreeType.TREE) || type.equals(TreeType.BIG_TREE))
				{
					int height = 3+random.nextInt(4);
					Block curr = this.block;
					
					HothSet tree = new HothSet();
					
					for(int i=0;i<height;i++)
					{
						BlockState state;
						
						if(i>0 && (i&1)!=0)
						{
							Block leaf = curr;

							int dir = random.nextInt(16);
							
							if((dir&1) != 0)
							{
								leaf = curr.getRelative(BlockFace.NORTH);
                                                                leaf.setType(Material.OAK_LEAVES);
								state = leaf.getState();
								Tree leavesD = (Tree)state.getData();
								leavesD.setSpecies(TreeSpecies.GENERIC);
								state.setData(leavesD);
								tree.add(new Position(state));
							}

							if((dir&2) != 0)
							{
								leaf = curr.getRelative(BlockFace.EAST);
                                                                leaf.setType(Material.OAK_LEAVES);
								state = leaf.getState();
								Tree leavesD = (Tree)state.getData();
								leavesD.setSpecies(TreeSpecies.GENERIC);
								state.setData(leavesD);
								tree.add(new Position(state));
							}

							if((dir&4) != 0)
							{
								leaf = curr.getRelative(BlockFace.SOUTH);
                                                                leaf.setType(Material.OAK_LEAVES);
								state = leaf.getState();
								Tree leavesD = (Tree)state.getData();
								leavesD.setSpecies(TreeSpecies.GENERIC);
								state.setData(leavesD);
								tree.add(new Position(state));
							}

							if((dir&8) != 0)
							{
								leaf = curr.getRelative(BlockFace.WEST);
                                                                leaf.setType(Material.OAK_LEAVES);
								state = leaf.getState();
								Tree leavesD = (Tree)state.getData();
								leavesD.setSpecies(TreeSpecies.GENERIC);
								state.setData(leavesD);
								tree.add(new Position(state));
							}
                                                        leaf.setType(Material.OAK_LEAVES);
							state = leaf.getState();
							Tree leavesD = (Tree)state.getData();
							leavesD.setSpecies(TreeSpecies.GENERIC);
							state.setData(leavesD);
							tree.add(new Position(state));
						}
						
						// Add stem
                                                curr.setType(Material.OAK_LOG);
						state = curr.getState();
						Tree treeD = (Tree)state.getData();
						treeD.setSpecies(TreeSpecies.GENERIC);
						state.setData(treeD);
						tree.add(new Position(state));
						
						curr = curr.getRelative(BlockFace.UP);
						if(random.nextInt(100)>70)
						{
							int dir = random.nextInt(4);
							switch(dir)
							{
							case 0:	curr = curr.getRelative(BlockFace.NORTH); break;
							case 2:	curr = curr.getRelative(BlockFace.EAST); break;
							case 3:	curr = curr.getRelative(BlockFace.SOUTH); break;
							case 4:	curr = curr.getRelative(BlockFace.WEST); break;
							}
						}
					}
					
					// Add top leaves
					if(random.nextInt(4)>0)
					{
                                                curr.setType(Material.OAK_LOG);
						BlockState state = curr.getState();
						Tree treeD = (Tree)state.getData();
						treeD.setSpecies(TreeSpecies.GENERIC);
						state.setData(treeD);
						tree.add(new Position(state));
					}

					// Check if there's room for the tree
					boolean empty = true;
					Position[] states = tree.toArray();
					for(int i=0;i<states.length;i++)
					{
						Position state = states[i];
						Block block = world.getBlockAt(state.x, state.y, state.z);
						if(!block.getType().equals(Material.AIR) && !isSapling(block.getType()))
						{
							empty = false;
							break;
						}
						
					}
					
					// Instantiate the tree
					if(empty)
					{
						for(int i=0;i<states.length;i++)
						{
							states[i].blockState.update(true, false);
						}
					}

				}
				else // Other Tree's and huge mushrooms can't grow
				{
					block.breakNaturally();
					world.playEffect(location, Effect.SMOKE, BlockFace.UP);
				}
			}

		}
	}
	
	private class PlaceDagobahTree extends HothRunnable
	{
		private static final long serialVersionUID = 2967615175022427407L;
		private int x;
		private int y;
		private int z;
		
		@Override
		public String getParameterString() {
			return "x="+x+",y="+y+"z="+z;
		}

		public PlaceDagobahTree(World world, int x, int y, int z)
		{
			this.setName("PlaceDagobahTree");
			this.setWorld(world);
			this.setPlugin(null);
			this.x = x;
			this.y = y;
			this.z = z;
		}

		@Override
		public void run()
		{
			World world = this.getWorld();
			HothGeneratorPlugin plugin = this.getPlugin();

			DagobahSmallTreePopulator dagobahSmallTreePopulator = new DagobahSmallTreePopulator(plugin.getHeight());

			Block block = world.getBlockAt(this.x, this.y, this.z);
			block = block.getRelative(BlockFace.DOWN);
			int j=0;
			while(block.isEmpty() && j<3)
			{
				block = block.getRelative(BlockFace.DOWN);
				j++;
			}
			
			if(j<3)
			{
				// Attempt to place dagobah tree
				if(dagobahSmallTreePopulator.renderTreeAt(world, random, x, block.getY(), z, 0, false))
				{
					// Make sure that we don't leave the sapling around
					block = world.getBlockAt(this.x, this.y, this.z);
					if(isSapling(block.getType()))
					{
						block.setType(Material.AIR);
					}
				}
			}

		}

	}
}
