package biz.orgin.minecraft.hothgenerator;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFormEvent;

public class BlockFormManager implements Listener
{
	private HothGeneratorPlugin plugin;

	public BlockFormManager(HothGeneratorPlugin plugin)
	{
		this.plugin = plugin;
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onBlockForm(BlockFormEvent event)
	{
		if(!event.isCancelled())
		{
			Block block = event.getBlock();
			World world = block.getWorld();
			WorldType worldType = this.plugin.getWorldType(world);
			Material newType = event.getNewState().getType();
			
			// Prevent snow from forming in tatooine and dagobah worlds
			
			if(newType.equals(Material.SNOW) && (worldType == WorldType.TATOOINE || worldType == WorldType.DAGOBAH))
			{
				event.setCancelled(true);
			}
		}
	}
}
