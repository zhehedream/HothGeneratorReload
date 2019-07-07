package biz.orgin.minecraft.hothgenerator;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

public class RegionSet
{
	private String flag;
	private HashMap<String, RegionFlag> regions;
	
	public RegionSet(String flag)
	{
		this.regions = new HashMap<String, RegionFlag>();
		this.flag = flag;
	}
	
	public String[] getList()
	{
		String[] list = new String[this.regions.size()];
		
		Set<String> keys = this.regions.keySet();
		
		Iterator<String> iterator = keys.iterator();
		
		int i = 0;
		while(iterator.hasNext())
		{
			String key = iterator.next();
			String value = this.regions.get(key).getString();
			
			list[i] = key + ":" + value;
			i++;
		}
		
		return list;
	}
	
	public void set(String region, String value)
	{
		this.regions.put(region, new RegionFlag(value));
	}
	
	public void remove(String region)
	{
		this.regions.remove(region);
	}
	
	public String get(String region, String def)
	{
		RegionFlag flag = this.regions.get(region);
		
		if(flag == null || !flag.isSet())
		{
			return def;
		}
		
		return flag.getString();
	}
	
	public boolean getBoolean(String region, boolean def)
	{
		RegionFlag flag = this.regions.get(region);
		
		if(flag == null || !flag.isSet())
		{
			return def;
		}

		return flag.getBoolean();
	}

	public int getInt(String region, int def)
	{
		RegionFlag flag = this.regions.get(region);
		
		if(flag == null || !flag.isSet())
		{
			return def;
		}

		return flag.getInt();
	}

	public boolean isSet(String region)
	{
		RegionFlag flag = this.regions.get(region);
		
		if(flag == null)
		{
			return false;
		}
		else
		{
			return flag.isSet();
		}
	}
	
	public String getFlag()
	{
		return this.flag;
	}
	
	private class RegionFlag
	{
		private String flag;
		private boolean booleanFlag;
		private int intFlag;
		private boolean set;
		
		public RegionFlag(String flag)
		{
			this.flag = flag.toLowerCase();
			
			this.booleanFlag = flag.equals("allow");
			
			try
			{
				this.intFlag = Integer.parseInt(flag);
			}
			catch(Exception e)
			{
				this.intFlag = 0;
			}
			
			this.set = !flag.equals("");
		}
		
		public boolean isSet()
		{
			return this.set;
		}
		
		public int getInt()
		{
			return this.intFlag;
		}
		
		public boolean getBoolean()
		{
			return this.booleanFlag;
		}
		
		public String getString()
		{
			return this.flag;
		}
	}
}
