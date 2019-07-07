package biz.orgin.minecraft.hothgenerator;

import org.bukkit.Server;
import org.bukkit.plugin.Plugin;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;

public class RegionManagerFactory 
{
	
	public static RegionManager getRegionmanager(HothGeneratorPlugin plugin)
	{
		Server server = plugin.getServer();
		
		// Check if WorldGuard is installed
		Plugin tempPlugin = server.getPluginManager().getPlugin("WorldGuard");
		if(tempPlugin!=null && (tempPlugin instanceof WorldGuardPlugin))
		{
			return new WorldGuardRegionManager(plugin);
		}
		
		// return default region manager
		return new DefaultRegionManager(plugin);
	}
}
