package biz.orgin.minecraft.hothgenerator;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Endermite;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

import biz.orgin.minecraft.hothgenerator.FireBeetle.FireBeetleType;

public class EntityDamageManager implements Listener
{
	private HothGeneratorPlugin plugin;

	public EntityDamageManager(HothGeneratorPlugin plugin)
	{
		this.plugin = plugin;
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onEntityDamage(EntityDamageEvent event)
	{
		Entity entity = event.getEntity();
		Location location = entity.getLocation();
		World world = location.getWorld();
		
		if(this.plugin.isHothWorld(world) && this.plugin.getWorldType(world) == WorldType.MUSTAFAR)
		{
			if(event.getEntityType() == EntityType.ENDERMITE)
			{
				Endermite m = (Endermite)event.getEntity();
				String name = m.getCustomName();
				FireBeetleType type = FireBeetle.getFireBeetleType(name);
				
				if(type!=null)
				{
					FireBeetle.handleDamage(event, m, type);
				}
			}
		}
	}
}
