package biz.orgin.minecraft.hothgenerator;

import java.io.Serializable;

import org.bukkit.Bukkit;
import org.bukkit.World;

public abstract class HothRunnable implements Runnable, Serializable
{
	private static final long serialVersionUID = -7394100364757655266L;
	private String name;
	private String worldName;
	private World world;
	private HothGeneratorPlugin plugin;
	private int staleCtr = 0;
	private boolean prioritized = false;
	
	public String getName()
	{
		return this.name;
	}
	
	public void setName(String name)
	{
		this.name = name;
	}
	
	public World getWorld()
	{
		return this.world;
	}
	
	public void setWorld(World world)
	{
		this.world = world;
	}
	
	public String getWorldName()
	{
		return this.worldName;
	}

	public void setPlugin(HothGeneratorPlugin plugin)
	{
		this.plugin = plugin;
	}
	
	public HothGeneratorPlugin getPlugin()
	{
		return this.plugin;
	}
	
	public boolean isPrioritized()
	{
		return this.prioritized;
	}

	public void setPrioritized(boolean prioritized)
	{
		this.prioritized = prioritized;
	}

	public void serialize()
	{
		if((this.worldName==null || this.worldName.isEmpty()) && this.world!=null)
		{
			this.worldName = this.world.getName();
		}
		this.world = null;
		this.plugin = null;
		this.staleCtr = 0;
	}
	
	public void deserialize()
	{
		this.world = Bukkit.getServer().getWorld(this.worldName);
		this.staleCtr++;
	}
	
	public boolean isStale()
	{
		return this.staleCtr>10;
	}
	
	public abstract void run();
	public abstract String getParameterString();
}
