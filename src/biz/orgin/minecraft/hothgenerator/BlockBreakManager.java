package biz.orgin.minecraft.hothgenerator;

import java.util.Random;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.Plugin;

/**
 * BlockBreakEvent listener. Makes sure that ice and snow blocks breaks into blocks that the player can pick up. 
 * @author orgin
 *
 */
public class BlockBreakManager implements Listener
{
	private HothGeneratorPlugin plugin;
	private Random random;
	
	private static Permission EssentialsBuildAll = new Permission("essentials.build.*","",PermissionDefault.TRUE);
	private static Permission EssentialsBuildBreakAll = new Permission("essentials.build.break.*","",PermissionDefault.TRUE);
	private static Permission EssentialsBuildBreakICE = new Permission("essentials.build.break." + MaterialManager.toID(Material.ICE),"",PermissionDefault.TRUE);
	private static Permission EssentialsBuildBreakSNOW_BLOCK = new Permission("essentials.build.break." + MaterialManager.toID(Material.SNOW_BLOCK),"",PermissionDefault.TRUE);
	private static Permission EssentialsBuildBreakPACKED_ICE = new Permission("essentials.build.break." + MaterialManager.toID(Material.PACKED_ICE),"",PermissionDefault.TRUE);
	private static Permission EssentialsBuildBreakSTONE = new Permission("essentials.build.break." + MaterialManager.toID(Material.STONE),"",PermissionDefault.TRUE);

	public BlockBreakManager(HothGeneratorPlugin plugin)
	{
		this.plugin = plugin;
		this.random = new Random(System.currentTimeMillis());
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onBlockBreak(BlockBreakEvent event)
	{
		if(!event.isCancelled())
		{
			Block block = event.getBlock();
			World world = block.getWorld();
			Player player = event.getPlayer(); 
	
			GameMode gamemode = player.getGameMode();
			
			// Don't do anything in creative mode
			if (!gamemode.equals(GameMode.CREATIVE))
			{
				boolean gotPermsICE = true;
				boolean gotPermsSNOWBLOCK = true;
				boolean gotPermsPACKEDICE = true;
				boolean gotPermsSTONE = true;
				
				// Special Essentials permissions check. For some reason Essentials executes its block break event handling
				// after HothGenerator so the event isn't cancelled properly before executing this method.
				// Thus a special essentials only check had to be added here.
				Plugin tempPlugin = this.plugin.getServer().getPluginManager().getPlugin("Essentials");
				if(tempPlugin!=null)
				{
					if(!player.hasPermission(BlockBreakManager.EssentialsBuildBreakICE)
						|| !player.hasPermission(BlockBreakManager.EssentialsBuildBreakAll)
						|| !player.hasPermission(BlockBreakManager.EssentialsBuildAll))
					{
						gotPermsICE = false;
					}
					if(!player.hasPermission(BlockBreakManager.EssentialsBuildBreakSNOW_BLOCK)
						|| !player.hasPermission(BlockBreakManager.EssentialsBuildBreakAll)
						|| !player.hasPermission(BlockBreakManager.EssentialsBuildAll))
					{
						gotPermsSNOWBLOCK = false;
					}
					if(!player.hasPermission(BlockBreakManager.EssentialsBuildBreakPACKED_ICE)
							|| !player.hasPermission(BlockBreakManager.EssentialsBuildBreakAll)
							|| !player.hasPermission(BlockBreakManager.EssentialsBuildAll))
					{
						gotPermsPACKEDICE = false;
					}
					if(!player.hasPermission(BlockBreakManager.EssentialsBuildBreakSTONE)
							|| !player.hasPermission(BlockBreakManager.EssentialsBuildBreakAll)
							|| !player.hasPermission(BlockBreakManager.EssentialsBuildAll))
					{
						gotPermsSTONE = false;
					}
				}				
				
				// Ice breaking
				if (gotPermsICE && this.plugin.isHothWorld(world) && block.getType().equals(Material.ICE) && this.plugin.getWorldType(world) == WorldType.HOTH)
				{
					if(ConfigManager.isRulesDropice(this.plugin, block.getLocation()))
					{
						block.setType(Material.AIR);
						ItemStack iceStack = new ItemStack(Material.ICE);
						iceStack.setAmount(1);
						block.getWorld().dropItemNaturally(block.getLocation(), iceStack);
					}
				}
				// Snow block breaking
				else if (gotPermsSNOWBLOCK && this.plugin.isHothWorld(world) && block.getType().equals(Material.SNOW_BLOCK) && this.plugin.getWorldType(world) == WorldType.HOTH)
				{
					if(ConfigManager.isRulesDropsnow(this.plugin, block.getLocation()))
					{
						block.setType(Material.AIR);
						ItemStack snowStack = new ItemStack(Material.SNOW_BLOCK);
						snowStack.setAmount(1);
						block.getWorld().dropItemNaturally(block.getLocation(), snowStack);
					}
				}
				// Packed ice breaking
				if (gotPermsPACKEDICE && this.plugin.isHothWorld(world) && block.getType().equals(Material.PACKED_ICE) && this.plugin.getWorldType(world) == WorldType.HOTH)
				{
					if(ConfigManager.isRulesDroppackedice(this.plugin, block.getLocation()))
					{
						block.setType(Material.AIR);
						ItemStack iceStack = new ItemStack(Material.PACKED_ICE);
						iceStack.setAmount(1);
						block.getWorld().dropItemNaturally(block.getLocation(), iceStack);
					}
				}
				// Stone breaking
				if (gotPermsSTONE && this.plugin.isHothWorld(world) && block.getType().equals(Material.STONE) && this.plugin.getWorldType(world) == WorldType.MUSTAFAR)
				{
					if(ConfigManager.isRulesLessStone(this.plugin, block.getLocation()))
					{
						block.setType(Material.AIR);
						if(this.random.nextInt(8) == 1)
						{
							Material material = Material.STONE;
							if(this.random.nextInt(4) > 0)
							{
								material = Material.COBBLESTONE;
							}
							ItemStack iceStack = new ItemStack(material);
							iceStack.setAmount(1);
							block.getWorld().dropItemNaturally(block.getLocation(), iceStack);
						}
					}
				}
			}
		}
	}

}