package biz.orgin.minecraft.hothgenerator;

import static biz.orgin.minecraft.hothgenerator.BlockGrowManager.isCrops;
import static biz.orgin.minecraft.hothgenerator.BlockGrowManager.isDoublePlant;
import static biz.orgin.minecraft.hothgenerator.BlockGrowManager.isSapling;
import static biz.orgin.minecraft.hothgenerator.BlockGrowManager.isSmallFlower;
import java.util.logging.Level;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;

/**
 * Listens for BlockPlaceEvents. Makes sure that water and lava is placed as ice and stone.
 * @author orgin
 *
 */
public class BlockPlaceManager implements Listener
{
	private HothGeneratorPlugin plugin;
	
	public BlockPlaceManager(HothGeneratorPlugin plugin)
        {
		this.plugin = plugin;
	}
	@EventHandler(priority = EventPriority.HIGHEST)
        public void onBucketEmpty(PlayerBucketEmptyEvent event) {
            if(!event.isCancelled()) {
                Block block = event.getBlockClicked().getRelative(event.getBlockFace());
                World world = block.getWorld();
                if(this.plugin.isHothWorld(world)) {
                    WorldType worldType = this.plugin.getWorldType(world);
                    
                    if(worldType == WorldType.HOTH && !this.plugin.canPlaceLiquid(world, block)) {
                        Material bucket = event.getBucket();
                        if(bucket.equals(Material.WATER_BUCKET) || bucket.equals(Material.COD_BUCKET)
                                || bucket.equals(Material.PUFFERFISH_BUCKET) || bucket.equals(Material.SALMON_BUCKET)
                                || bucket.equals(Material.TROPICAL_FISH_BUCKET)) {
                            if(ConfigManager.isRulesFreezewater(this.plugin, block.getLocation()))
                            {
				BlockPlacerThread th = new BlockPlacerThread(world, block.getX(), block.getY(), block.getZ(), Material.WATER, Material.ICE);
				plugin.addTask(th);
                            }
                        } else if(bucket.equals(Material.LAVA_BUCKET)) {
                            if(ConfigManager.isRulesFreezelava(this.plugin, block.getLocation()))
                            {
                                BlockPlacerThread th = new BlockPlacerThread(world, block.getX(), block.getY(), block.getZ(), Material.LAVA, Material.STONE);
				plugin.addTask(th);
                            }
                        }
                    }
                }
            }
        }
	@EventHandler(priority = EventPriority.HIGHEST)
	public void placeBlock(BlockPlaceEvent event)
	{
		if(!event.isCancelled())
		{
			Block block = event.getBlock();
			
			World world = block.getWorld();
			
	  	  	if(this.plugin.isHothWorld(world))
  	  		{
	  	  		WorldType worldType = this.plugin.getWorldType(world);
	  	  		/*
	  	  		if(worldType == WorldType.HOTH)
	  	  		{
					if(block.isLiquid() && !this.plugin.canPlaceLiquid(world, block))
					{
						Material type = block.getType();
					
						if(type.equals(Material.WATER) ||
							type.equals(Material.WATER_BUCKET))
						{
							if(ConfigManager.isRulesFreezewater(this.plugin, block.getLocation()))
							{
								BlockPlacerThread th = new BlockPlacerThread(world, block.getX(), block.getY(), block.getZ(), Material.WATER, Material.ICE);
								plugin.addTask(th);
							}
						}
						else if(type.equals(Material.LAVA) ||
							type.equals(Material.LAVA_BUCKET))
						{
							if(ConfigManager.isRulesFreezelava(this.plugin, block.getLocation()))
							{
								BlockPlacerThread th = new BlockPlacerThread(world, block.getX(), block.getY(), block.getZ(), Material.LAVA, Material.STONE);
								plugin.addTask(th);
							}
						}
					}
	  	  			
	  	  		}*/
	  	  		if(worldType == WorldType.MUSTAFAR)
	  	  		{
					Material type = block.getType();
					if(HothUtils.isTooHot(block.getLocation(), 2))
					{
						if(type.equals(Material.GRASS))
						{
							BlockPlacerThread th = new BlockPlacerThread(world, block.getX(), block.getY(), block.getZ(), Material.GRASS_BLOCK, Material.DIRT);
							plugin.addTask(th);
						}
						else if(type.equals(Material.MYCELIUM))
						{
							BlockPlacerThread th = new BlockPlacerThread(world, block.getX(), block.getY(), block.getZ(), Material.MYCELIUM, Material.DIRT);
							plugin.addTask(th);
						}
						else if(isDoublePlant(type)
								|| type.equals(Material.RED_MUSHROOM)
                                                                || isSmallFlower(type)
								|| type.equals(Material.BROWN_MUSHROOM)
								|| type.equals(Material.TALL_GRASS)
								|| type.equals(Material.CACTUS)
								|| type.equals(Material.PUMPKIN_STEM)
								|| type.equals(Material.MELON_STEM)
								|| isSapling(type)
								|| type.equals(Material.VINE)
								|| type.equals(Material.SUGAR_CANE)
								|| isCrops(type)
								)
						{
							event.setCancelled(true);
						}
					}

	  	  		}
			}
		}
	}
}


