package biz.orgin.minecraft.hothgenerator;

import java.util.Iterator;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.plugin.Plugin;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.flags.StateFlag.State;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

/**
 * Worldguard implementation of region manager.
 * @author orgin
 *
 */

public class WorldGuardRegionManager extends RegionManager
{

	
	public WorldGuardRegionManager(HothGeneratorPlugin plugin)
	{
		super(plugin);
		plugin.getLogger().info("Using WorldGuard region manager");
	}

	/**
	 * Get an applicable region name at the current position.
	 * regions with passthrough set to allow are ignored.
	 * @param location
	 * @return
	 */
	@Override
	public String getRegion(Location location)
	{
		// Use worldguard to get region. If worldguard isn't installed. Return null;
		
		Server server = this.plugin.getServer();

		Plugin tempPlugin = server.getPluginManager().getPlugin("WorldGuard");
		
		if(tempPlugin==null || !(tempPlugin instanceof WorldGuardPlugin))
		{
			return null;
		}
		
		WorldGuardPlugin worldGuard = (WorldGuardPlugin)tempPlugin;
		
		World world = location.getWorld();
		/*
		//com.sk89q.worldguard.protection.managers.RegionManager manager = worldGuard.getRegionManager(world);
                com.sk89q.worldguard.protection.managers.RegionManager manager = createRegionManager();
		
		if(manager!=null)
		{
			// find highest priority region that isn't set to passthrough at the location
		
			ApplicableRegionSet set = manager.getApplicableRegions(location);
			Iterator<ProtectedRegion> iter = set.iterator();
			ProtectedRegion highest = null;
			
			while(iter.hasNext())
			{
				ProtectedRegion region = iter.next();
				
				if(highest==null || region.getPriority() > highest.getPriority())
				{
					// Ignore regions with passthrough flag set to allow
					State passthrough = region.getFlag(Flags.PASSTHROUGH);
					if(passthrough == null || passthrough == State.DENY)
					{
						highest = region;
					}
				}
			}
			
			if(highest==null)
			{
				return null;
			}
			else
			{
				return highest.getId();
			}
		}
		*/
		return null;
	}

	/**
	 * Check if the specified region is actually a region
	 * @param region
	 * @return
	 */
	@Override
	public boolean isValidRegion(String region)
	{
            /*
		Server server = this.plugin.getServer();

		Plugin tempPlugin = server.getPluginManager().getPlugin("WorldGuard");
		
		if(tempPlugin==null || !(tempPlugin instanceof WorldGuardPlugin))
		{
			return false;
		}
		
		WorldGuardPlugin worldGuard = (WorldGuardPlugin)tempPlugin;

		List<World> worlds = server.getWorlds();
		
		for(int i=0;i<worlds.size();i++)
		{
			World world = worlds.get(i);
			com.sk89q.worldguard.protection.managers.RegionManager manager = worldGuard.getRegionManager(world);
			if(manager!=null)
			{
				ProtectedRegion pregion = manager.getRegion(region);
				if(pregion!=null)
				{
					return true;
				}
			}

		}
		*/
		return false;
		
	}
	

}
