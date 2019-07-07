package biz.orgin.minecraft.hothgenerator;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockGrowEvent;

/**
 * BlockGrowEvent listener. Cancels any growth events on a h/o/t/h and t/a/t/o/o/i/n/e world that is directly exposed to sunlight.
 * In t/a/t/o/o/i/n/e things may grow if there's water around the block beneath.
 * @author orgin
 *
 */
public class BlockGrowManager implements Listener
{
	private HothGeneratorPlugin plugin;

	public BlockGrowManager(HothGeneratorPlugin plugin)
	{
		this.plugin = plugin;
	}
        
        public static boolean isSmallFlower(Material m) {
            if(m == null) return false;
            if(m.equals(Material.DANDELION) || m.equals(Material.POPPY) || m.equals(Material.BLUE_ORCHID)
                    || m.equals(Material.ALLIUM) || m.equals(Material.AZURE_BLUET) || m.equals(Material.RED_TULIP)
                    || m.equals(Material.ORANGE_TULIP) || m.equals(Material.WHITE_TULIP) || m.equals(Material.PINK_TULIP)
                    || m.equals(Material.OXEYE_DAISY)) return true;
            return false;
        }
        
        public static boolean isDoublePlant(Material m) {
            if(m == null) return false;
            if(m.equals(Material.SUNFLOWER) || m.equals(Material.LILAC) || m.equals(Material.TALL_GRASS) || 
                    m.equals(Material.LARGE_FERN) || m.equals(Material.ROSE_BUSH) || m.equals(Material.PEONY)) return true;
            return false;
        }
        
        public static boolean isSapling(Material m) {
            if(m == null) return false;
            if(m.equals(Material.OAK_SAPLING) || m.equals(Material.DARK_OAK_SAPLING) || m.equals(Material.SPRUCE_SAPLING)
                    || m.equals(Material.BIRCH_SAPLING) || m.equals(Material.JUNGLE_SAPLING) || m.equals(Material.ACACIA_SAPLING)) return true;
            return false;
        }
        
        public static boolean isCrops(Material m) {
            if(m == null) return false;
            if(m.equals(Material.WHEAT_SEEDS) || m.equals(Material.BEETROOTS) || m.equals(Material.CARROTS) || m.equals(Material.POTATOES)) return true;
            return false;
        }

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onBlockGrow(BlockGrowEvent event)
	{
		if(!event.isCancelled())
		{
			Block block = event.getBlock();
			World world = block.getWorld();
			WorldType worldType = this.plugin.getWorldType(world);
			Location location = event.getBlock().getLocation();
			
			if(this.plugin.isHothWorld(world))
			{
				if(!ConfigManager.isRulesPlantsgrow(this.plugin, block.getLocation())
					&& (worldType == WorldType.HOTH || worldType == WorldType.TATOOINE))
				{
					int maxy = world.getHighestBlockYAt(location);
		
					if(maxy==block.getY())
					{
						if(worldType == WorldType.HOTH || (worldType == WorldType.TATOOINE && !HothUtils.isWatered(block.getRelative(BlockFace.DOWN))))
						{
							event.setCancelled(true);
							block.breakNaturally();
						}
					}
				}
				else if(worldType == WorldType.MUSTAFAR)
				{
					BlockState state = event.getNewState();
					Material material = state.getType();
					
					boolean toohot = HothUtils.isTooHot(state.getLocation(), 2);
					
					// Check if it's too hot to grow here
					if(toohot)
					{
						if(isSmallFlower(material))
						{	// Flowers to dead bush
							state.setType(Material.DEAD_BUSH);
							state.update(true, false);
							event.setCancelled(true);
						}
						else if(isDoublePlant(material))
						{	// Don't render long grass
							event.setCancelled(true);
						}
						else
						{	// Everything else just drops
							block.breakNaturally();
							event.setCancelled(true);
						}
					}
				}
				
			}
			
		}
	}
}
