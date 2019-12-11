package biz.orgin.minecraft.hothgenerator;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.FallingBlock;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

/**
 * Applies gravity to snow blocks.
 * @author orgin
 *
 */
public class BlockGravityManager  implements Listener
{
	private HothGeneratorPlugin plugin;

	public BlockGravityManager(HothGeneratorPlugin plugin)
	{
		this.plugin = plugin;
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void placeBlock(BlockPlaceEvent event)
	{
		if(!event.isCancelled())
		{
			Block block = event.getBlock();
			
			World world = block.getWorld();
			if(plugin.isHothWorld(world) && ConfigManager.isRulesSnowgravity(this.plugin, block.getLocation()))
			{
				this.applyGravityToBlock(world, block, false);
			}
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void breakBlock(BlockBreakEvent event)
	{
		if(!event.isCancelled())
		{
			Block block = event.getBlock();

			int x = block.getX();
			int y = block.getY();
			int z = block.getZ();
			
			World world = block.getWorld();
			if(plugin.isHothWorld(world) && ConfigManager.isRulesSnowgravity(this.plugin, block.getLocation()))
			{
				this.applyGravityToBlock(world, world.getBlockAt(x, y+1, z), true);
			}
		}
	}

	private void applyGravityToBlock(World world, Block block, boolean breakBlock)
	{
		Material material = block.getType(); 
		
		if(material.equals(Material.SNOW_BLOCK))
		{
			int x = block.getX();
			int y = block.getY();
			int z = block.getZ();

			int below = MaterialManager.toID(world.getBlockAt(x, y-1, z).getType());
			if(below==0 || breakBlock)
			{
				BlockData data = block.getBlockData();
				
				try
				{
					Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new DropBlock(world, block, material, data));
				}
				catch(Exception e)
				{
					plugin.logMessage("Exception when trying to register DropBlock task. You probably need to restart your server.", true);
				}
			}
		}
	}

	private class DropBlock implements Runnable
	{
		private World world;
		private Block block;
		private Material material;
		private BlockData data;
		
		public DropBlock(World world, Block block, Material material, BlockData data)
		{
			this.world = world;
			this.block = block;
			this.material = material;
			this.data = data;
		}
		
		public void run()
		{
			int x = this.block.getX();
			int y = this.block.getY();
			int z = this.block.getZ();

			this.block.setType(Material.AIR, false);

			FallingBlock falling = this.world.spawnFallingBlock(block.getLocation(), this.data);
			falling.setDropItem(true);
			
			Block up = this.world.getBlockAt(x,y+1,z);
			applyGravityToBlock(world, up, false);

			applyGravityToBlock(world, world.getBlockAt(x+1, y, z), false);
			applyGravityToBlock(world, world.getBlockAt(x-1, y, z), false);
			applyGravityToBlock(world, world.getBlockAt(x, y, z+1), false);
			applyGravityToBlock(world, world.getBlockAt(x, y, z-1), false);
		}
	}
}
