package biz.orgin.minecraft.hothgenerator;

import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Endermite;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

public class FireBeetle
{
	private static Random random = new Random(System.currentTimeMillis());
	
	public static void handleDeath(EntityDeathEvent event, FireBeetleType fireBeetleType, Location location)
	{
		World world = location.getWorld();
		
		// Spawn some more beetles
		int prob = 2;
		switch(fireBeetleType)
		{
		case PRIME:
			prob = 0;
			for(int i=0;i<FireBeetle.random.nextInt(2);i++)
			{
				Location pl = location.clone();
				pl.add(FireBeetle.random.nextDouble(), 0, FireBeetle.random.nextDouble());
				FireBeetle.spawn(pl, FireBeetleType.REGULAR);
			}
			for(int i=0;i<FireBeetle.random.nextInt(2);i++)
			{
				Location pl = location.clone();
				pl.add(FireBeetle.random.nextDouble(), 0, FireBeetle.random.nextDouble());
				FireBeetle.spawn(pl, FireBeetleType.HATCHLING);
			}
			break;
		case REGULAR:
			prob = 1;
			for(int i=0;i<FireBeetle.random.nextInt(3);i++)
			{
				Location pl = location.clone();
				pl.add(FireBeetle.random.nextDouble(), 0, FireBeetle.random.nextDouble());
				FireBeetle.spawn(pl, FireBeetleType.HATCHLING);
			}
			break;
		case HATCHLING:
			prob = 2;
			break;
		default:
			break;
		}
		
		
		// Drop some loot
		if(prob==0 || FireBeetle.random.nextInt(prob)==0)
		{
			ItemStack stack = null;
			switch(FireBeetle.random.nextInt(10))
			{
			case 1:
				stack = new ItemStack(Material.GLOWSTONE_DUST,1);
				break;
			case 2:
				stack = new ItemStack(Material.FLINT,1);
				break;
			case 3:
				int rnd = FireBeetle.random.nextInt(10);
				if(rnd==0)
				{
					stack = new ItemStack(Material.PRISMARINE_SHARD,1);
				}
				else if(rnd<4)
				{
					stack = new ItemStack(Material.PRISMARINE_CRYSTALS,1);
				}
				break;
			case 4:
				stack = new ItemStack(Material.QUARTZ,1);
				break;
			case 5:
				stack = new ItemStack(Material.SPIDER_EYE,1);
				break;

			default:
				break;
			}
			if(stack!=null)
			{
				world.dropItem(location, stack);
			}
		}

	}
	
	public static void handleDamage(EntityDamageEvent event, Endermite fireBeetle, FireBeetleType fireBeetleType)
	{
		DamageCause cause = event.getCause();
		
		if(cause==DamageCause.ENTITY_ATTACK || cause==DamageCause.PROJECTILE)
		{

			double factor = 1.0;
			
			// Get damage factor for each beetle type
			switch(fireBeetleType)
			{
			case PRIME:
				factor = 0.2;
				break;
			case REGULAR:
				factor = 0.4;
				break;
			case HATCHLING:
				factor = 0.8;
				break;
			}
			
			// Reduce damage using the factor
			if(factor!=1.0)
			{
				double damage = event.getDamage();
				event.setDamage(damage * factor);
			}
		}
		else if(cause==DamageCause.LAVA || cause==DamageCause.FIRE || cause==DamageCause.FIRE_TICK)
		{	// Fire beetles can't be damaged by lava and fire
			event.setCancelled(true);
		}
	}
	
	/**
	 * Spawn a random FireBeetle at the given location if the block below is STONE
	 * @param location Location to spawn at
	 */
	public static void spawn(Location location)
	{
		Block block = location.getBlock().getRelative(BlockFace.DOWN);
		
		if(HothUtils.isTooHot(location, 2) && block.getType().equals(Material.STONE))
		{
			int rnd = FireBeetle.random.nextInt(10);
			if(rnd==1)
			{
				FireBeetle.spawn(location, FireBeetleType.PRIME);
			}
			else if(rnd<4)
			{
				FireBeetle.spawn(location, FireBeetleType.REGULAR);
			}
			else
			{
				FireBeetle.spawn(location, FireBeetleType.HATCHLING);
			}
		}
	}
	
	/**
	 * Spawn a fire beetle with the correct type, name, and health at the given location
	 * @param location Where to spawn the Fire Beetle
	 * @param fireBeetleType The type of Fire Beetle to spawn
	 * @return The spawned entity
	 */
	
	public static Endermite spawn(Location location, FireBeetleType fireBeetleType)
	{
		World world = location.getWorld();
		Entity mite = world.spawnEntity(location, EntityType.ENDERMITE);
		Endermite m = (Endermite)mite;
		
		switch(fireBeetleType)
		{
		case PRIME:
			m.setCustomName(FireBeetleType.PRIME.getName());
			m.setMaxHealth(20.0);
			m.setHealth(20.0);
			break;
		case REGULAR:
			m.setCustomName(FireBeetleType.REGULAR.getName());
			m.setMaxHealth(16.0);
			m.setHealth(16);
			break;
		case HATCHLING:
			m.setCustomName(FireBeetleType.HATCHLING.getName());
			m.setMaxHealth(8.0);
			m.setHealth(8);
			break;
		}

		m.setMaxHealth(20.0);
		m.setHealth(20.0);
		m.setCustomNameVisible(true);
		
		return m;
	}
	
	/**
	 * Convert a string into a FireBeetleType
	 * @param type The string to convert
	 * @return The FireBeetleType or null for an unknown String
	 */
	public static FireBeetleType getFireBeetleType(String type)
	{
		 for(FireBeetleType t : FireBeetleType.values())
		 {
			 if(t.name.equals(type))
			 {
				 return t;
			 }
		 }
		 
		 return null;
	}
	
	public enum FireBeetleType {
		 PRIME ("Prime fire beetle"),
		 REGULAR ("Fire beetle"),
		 HATCHLING ("Fire beetle hatchling");
		 
		 private final String name;
		 
		 private FireBeetleType(String name)
		 {
			 this.name = name;
		 }
		 
		 public String getName()
		 {
			 return this.name;
		 }
		 
		 public String toString()
		 {
			 return this.name;
		 }
	}

}
