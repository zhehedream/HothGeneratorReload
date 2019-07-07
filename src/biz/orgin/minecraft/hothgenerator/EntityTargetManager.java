package biz.orgin.minecraft.hothgenerator;

import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.EntityTargetEvent.TargetReason;

public class EntityTargetManager  implements Listener
{
	private HothGeneratorPlugin plugin;

	public EntityTargetManager(HothGeneratorPlugin plugin)
	{
		this.plugin = plugin;
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onEntityTarget(EntityTargetEvent event)
	{
		World world = event.getEntity().getWorld();
		
		if(this.plugin.isHothWorld(world))
		{
			WorldType worldType = this.plugin.getWorldType(world);
			
			if(worldType == WorldType.MUSTAFAR)
			{
				// Make sure that endermites are passive in mustafar
				
				EntityType type = event.getEntityType();
				if(type.equals(EntityType.ENDERMITE))
				{
					Entity target = event.getTarget();
					if(target!=null)
					{
						EntityType ttype = target.getType();
						if(ttype.equals(EntityType.PLAYER))
						{
							if(event.getReason().equals(TargetReason.RANDOM_TARGET)
									|| event.getReason().equals(TargetReason.CLOSEST_PLAYER)
									)
							{
								// make endermite passive
								event.setCancelled(true);
							}
						}
					}
				}
			}
		}
	}
}