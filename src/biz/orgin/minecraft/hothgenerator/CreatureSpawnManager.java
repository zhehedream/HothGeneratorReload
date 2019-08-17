package biz.orgin.minecraft.hothgenerator;


import java.util.logging.Level;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Slime;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;

/**
 * Prevents slime from spawning above the stone layer
 * Spawns Fire Beetles in Mustafar
 * @author orgin
 *
 */

public class CreatureSpawnManager implements Listener
{
	private HothGeneratorPlugin plugin;

	public CreatureSpawnManager(HothGeneratorPlugin plugin)
	{
		this.plugin = plugin;
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onCreatureSpawn(CreatureSpawnEvent event)
	{
		if(!event.isCancelled())
		{
			Location location = event.getLocation();
			World world = location.getWorld();
			
			if(this.plugin.isHothWorld(world))
			{
				if(ConfigManager.isRulesLimitslime(this.plugin, location))
				{
					int surfaceOffset = ConfigManager.getWorldSurfaceoffset(this.plugin, world);
					
					LivingEntity entity = event.getEntity();
					
					if(entity instanceof Slime && location.getBlockY()>(27 + surfaceOffset) &&
							event.getSpawnReason().equals(CreatureSpawnEvent.SpawnReason.NATURAL))
					{
						event.setCancelled(true);
					}
				}
				
				// Don't allow naturally spawned mobs in mustafar worlds
				if(this.plugin.getWorldType(world)==WorldType.MUSTAFAR && ConfigManager.getMustafarSpawn(plugin, world) == false)
				{
					SpawnReason reason = event.getSpawnReason();
					
					if(reason.equals(CreatureSpawnEvent.SpawnReason.NATURAL) || 
							reason.equals(CreatureSpawnEvent.SpawnReason.MOUNT))
					{
						event.setCancelled(true);
						
						// Spawn some fire beetles instead
						FireBeetle.spawn(event.getLocation());
						
					}
				}
			}
		}
	}
	

	

}
