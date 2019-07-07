package biz.orgin.minecraft.hothgenerator;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Endermite;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

import biz.orgin.minecraft.hothgenerator.FireBeetle.FireBeetleType;

public class EntityDeathManager implements Listener
{
	private HothGeneratorPlugin plugin;

	public EntityDeathManager(HothGeneratorPlugin plugin)
	{
		this.plugin = plugin;
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onEntitydeath(EntityDeathEvent event)
	{
		Entity entity = event.getEntity();
		Location location = entity.getLocation();
		World world = location.getWorld();
		
		if(this.plugin.isHothWorld(world) && this.plugin.getWorldType(world)== WorldType.MUSTAFAR)
		{
			if(event.getEntityType() == EntityType.ENDERMITE)
			{
				Endermite em = (Endermite)entity;
				String name = em.getCustomName();
				FireBeetleType type = FireBeetle.getFireBeetleType(name);
				
				if(type!=null)
				{
					FireBeetle.handleDeath(event, type, location);
				}
			}
		}
	}
}
