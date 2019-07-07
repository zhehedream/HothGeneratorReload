package biz.orgin.minecraft.hothgenerator;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFadeEvent;

/**
 * BlockFadeEvent listener. Makes sure that ice and snow does not melt.
 * @author orgin
 *
 */
public class BlockMeltManager implements Listener
{
	HothGeneratorPlugin plugin;
	
	public BlockMeltManager(HothGeneratorPlugin plugin)
	{
		this.plugin = plugin;
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onBlockFade(BlockFadeEvent event)
	{
		if(!event.isCancelled())
		{
			Block block = event.getBlock();
			World world = block.getWorld();
			
			if(this.plugin.isHothWorld(world) && ConfigManager.isRulesStopmelt(this.plugin, block.getLocation()) && this.plugin.getWorldType(world) == WorldType.HOTH)
			{
				int y = block.getY();
				
				int surfaceOffset = ConfigManager.getWorldSurfaceoffset(this.plugin, world);
				
				if(y>(26 + surfaceOffset))
				{
					Material type = block.getType();
					
					if(type.equals(Material.ICE) ||
					   type.equals(Material.SNOW) ||
					   type.equals(Material.SNOW_BLOCK))
					{
						event.setCancelled(true);
					}
				}
			}
		}
	}
}
