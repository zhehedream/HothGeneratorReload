package biz.orgin.minecraft.hothgenerator;

import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFromToEvent;

public class BlockFromToManager implements Listener
{
	private HothGeneratorPlugin plugin;

	public BlockFromToManager(HothGeneratorPlugin plugin)
	{
		this.plugin = plugin;
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onBlockFromTo(BlockFromToEvent event)
	{
		if(!event.isCancelled())
		{
			Block block = event.getBlock();
			World world = block.getWorld();
			
			if(this.plugin.isHothWorld(world) && this.plugin.getWorldType(world) == WorldType.MUSTAFAR)
			{
				Material material = block.getType();
				
				if(material.equals(Material.WATER))
				{
					Block to = event.getToBlock();
					if(HothUtils.isTooHot(to.getLocation(),2))
					{
						world.playEffect(to.getLocation(), Effect.SMOKE, BlockFace.UP);
						event.setCancelled(true);
					}
				}
			}
		}
	}

}