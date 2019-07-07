package biz.orgin.minecraft.hothgenerator;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockSpreadEvent;

/**
 * Prevents grass and mycelium that is exposed to the sky from spreading in hoth worlds.
 * Source block will "die" when trying to spread.
 * Grass and mycelium won't grow into too hot blocks in mustafar worlds.
 * @author orgin
 *
 */
public class BlockSpreadManager implements Listener
{
	private HothGeneratorPlugin plugin;

	public BlockSpreadManager(HothGeneratorPlugin plugin)
	{
		this.plugin = plugin;
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onBlockSpread(BlockSpreadEvent event)
	{
		if(!event.isCancelled())
		{
			Block block = event.getSource();
			World world = block.getWorld();
			
			if(this.plugin.isHothWorld(world))
			{
				WorldType worldType = this.plugin.getWorldType(world);
				
				if(!ConfigManager.isRulesGrassspread(this.plugin, block.getLocation()) && worldType == WorldType.HOTH)
				{
					Block block2 = event.getBlock(); // destination.
					int maxy = world.getHighestBlockYAt(block.getLocation());
					int maxy2 = world.getHighestBlockYAt(block2.getLocation());
					Material type = block.getType();
		
					if(type.equals(Material.GRASS) || type.equals(Material.MYCELIUM))
					{
						// Check both source block and destination
						boolean exposed = Math.abs(maxy-block.getY())<2;
						boolean exposed2 = Math.abs(maxy2-block2.getY())<2;
						if(exposed2) // The destination is exposed. Cancel spreading
						{
							event.setCancelled(true);
						}
		
						if(exposed) // The source was exposed. Kill it. But allow spreading if not already canceled.
						{
							block.setType(Material.DIRT);
						}
					}
				}
				else if(worldType == WorldType.MUSTAFAR)
				{
					Block block2 = event.getBlock();
					Material type = block.getType();
					
					if(type.equals(Material.GRASS) || type.equals(Material.MYCELIUM) || type.equals(Material.VINE))
					{
						if(HothUtils.isTooHot(block2.getLocation(), 2) || HothUtils.isTooHot(block.getLocation(), 2))
						{	// Don't spread into too hot blocks
							event.setCancelled(true);
						}
					}

				}
			}
		}
	}
}
