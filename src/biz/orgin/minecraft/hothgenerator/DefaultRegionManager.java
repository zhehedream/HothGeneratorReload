package biz.orgin.minecraft.hothgenerator;

import org.bukkit.Location;

/**
 * Default implementation of a region manager.
 * Essentially does nothing. There's no default region handling.
 * @author orgin
 *
 */
public class DefaultRegionManager extends RegionManager
{

	public DefaultRegionManager(HothGeneratorPlugin plugin)
	{
		super(plugin);
		plugin.getLogger().info("Using Default region manager");
	}

	@Override
	public String getRegion(Location location)
	{
		return null;
	}

	@Override
	public boolean isValidRegion(String region)
	{
		return false;
	}

}
