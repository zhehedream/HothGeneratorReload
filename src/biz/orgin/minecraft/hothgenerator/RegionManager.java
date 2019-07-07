package biz.orgin.minecraft.hothgenerator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

import org.bukkit.Location;

/**
 * Handles region flags.
 * @author orgin
 *
 */
public abstract class RegionManager
{
		protected HothGeneratorPlugin plugin;
		
		private HashMap<String, RegionSet> regionSets;
		
		private FlagValue[] validFlags = new FlagValue[] {
			new FlagValue("dropice",              new String[]{"allow","deny", "[blank]"}),	
			new FlagValue("droppackedice",        new String[]{"allow","deny", "[blank]"}),	
			new FlagValue("dropsnow",             new String[]{"allow","deny", "[blank]"}),	
			new FlagValue("freezewater",          new String[]{"allow","deny", "[blank]"}),	
			new FlagValue("freezelava",           new String[]{"allow","deny", "[blank]"}),	
			new FlagValue("plantsgrow",           new String[]{"allow","deny", "[blank]"}),	
			new FlagValue("grassspread",          new String[]{"allow","deny", "[blank]"}),	
			new FlagValue("stopmelt",             new String[]{"allow","deny", "[blank]"}),	
			new FlagValue("limitslime",           new String[]{"allow","deny", "[blank]"}),	
			new FlagValue("snowgravity",          new String[]{"allow","deny", "[blank]"}),	
			new FlagValue("volcanoes",            new String[]{"allow","deny", "[blank]"}),	
			new FlagValue("rfg.enable",           new String[]{"allow","deny", "[blank]"}),	
			new FlagValue("rfg.coal",             new String[]{"[integer]", "[blank]"}),	
			new FlagValue("rfg.redstone",         new String[]{"[integer]", "[blank]"}),	
			new FlagValue("rfg.message1",         new String[]{"[text]", "[blank]"}),	
			new FlagValue("rfg.message2",         new String[]{"[text]", "[blank]"}),	
			new FlagValue("rfg.message3",         new String[]{"[text]", "[blank]"}),	
			new FlagValue("rfg.message4",         new String[]{"[text]", "[blank]"}),	
			new FlagValue("rfg.name",             new String[]{"[text]", "[blank]"}),	
			new FlagValue("lavaburn",             new String[]{"allow","deny", "[blank]"}),	
			new FlagValue("placewater",           new String[]{"allow","deny", "[blank]"}),	
			new FlagValue("lessstone",            new String[]{"allow","deny", "[blank]"}),	
			//new FlagValue("environment.period",      new String[]{"[integer]"}), // This is global only
			new FlagValue("environment.suit",     new String[]{"allow","deny", "[blank]"}),
			new FlagValue("freeze.damage",        new String[]{"[integer]", "[blank]"}),
			new FlagValue("freeze.stormdamage",   new String[]{"[integer]", "[blank]"}),
			new FlagValue("freeze.message",       new String[]{"[text]", "[blank]"}),
			new FlagValue("heat.damage",          new String[]{"[integer]", "[blank]"}),
			new FlagValue("heat.message1",        new String[]{"[text]", "[blank]"}),
			new FlagValue("heat.message2",        new String[]{"[text]", "[blank]"}),
			new FlagValue("heat.message3",        new String[]{"[text]", "[blank]"}),
			new FlagValue("heat.message4",        new String[]{"[text]", "[blank]"}),
			new FlagValue("mosquito.damage",      new String[]{"[integer]", "[blank]"}),
			new FlagValue("mosquito.rarity",      new String[]{"[integer]", "[blank]"}),
			new FlagValue("mosquito.runfree",     new String[]{"[integer]", "[blank]"}),
			new FlagValue("mosquito.message1",    new String[]{"[text]", "[blank]"}),
			new FlagValue("mosquito.message2",    new String[]{"[text]", "[blank]"}),
			new FlagValue("mosquito.message3",    new String[]{"[text]", "[blank]"}),
			new FlagValue("mosquito.message4",    new String[]{"[text]", "[blank]"}),
			new FlagValue("leech.damage",         new String[]{"[integer]", "[blank]"}),
			new FlagValue("leech.rarity",         new String[]{"[integer]", "[blank]"}),
			new FlagValue("leech.message1",       new String[]{"[text]", "[blank]"}),
			new FlagValue("leech.message2",       new String[]{"[text]", "[blank]"}),
			new FlagValue("leech.message3",       new String[]{"[text]", "[blank]"}),
			new FlagValue("leech.message4",       new String[]{"[text]", "[blank]"}),
			new FlagValue("leech.message5",       new String[]{"[text]", "[blank]"}),
			// new FlagValue("spawn.neutral.on",     new String[]{"allow","deny", "[blank]"}), // This is global only
			new FlagValue("spawn.neutral.rarity", new String[]{"[integer]", "[blank]"}),
			new FlagValue("spawn.neutral.mobs",   new String[]{"none", "[text]", "[blank]"})
		};

		
		public RegionManager(HothGeneratorPlugin plugin)
		{
			this.plugin = plugin;
			
			this.regionSets = new HashMap<String, RegionSet>();
		}
		
		/**
		 * Set a flag value for a specified region
		 * @param region
		 * @param flag
		 * @param value
		 * @return
		 */
		public boolean set(String region, String flag, String value)
		{
			// Check if there is such a flag
			RegionSet regionSet = this.regionSets.get(flag);
			if(regionSet==null)
			{
				return false;
			}
			
			if(value.equals(""))
			{
				regionSet.remove(region);
			}
			else
			{
				regionSet.set(region, value);
			}
			this.save(regionSet);
			return true;
		}
		
		/**
		 * remove a region from all region files
		 * @param region
		 */
		public void remove(String region)
		{
			for(int i=0;i<this.validFlags.length;i++)
			{
				String flag = this.validFlags[i].flag;
				
				RegionSet regionSet = this.regionSets.get(flag);
				
				if(regionSet!=null)
				{
					regionSet.remove(region);
					this.save(regionSet);
				}
			}
		}
		
		/**
		 * Check if a flag is set at the given location.
		 * @param flag
		 * @param location
		 * @return
		 */
		public boolean isSet(String flag, Location location)
		{
			// Check if there is a region at the location
			String region = this.getRegion(location);
			if(region==null)
			{
				return false;
			}
			
			// Check if there is such a flag
			RegionSet regionSet = this.regionSets.get(flag);
			if(regionSet==null)
			{
				return false;
			}
			
			// Check if the flag is set
			return regionSet.isSet(region);
		}
		
		/**
		 * Get the flag string value at the given location.
		 * @param flag
		 * @param location
		 * @param def Default value in case the flag is not set here.
		 * @return
		 */
		public String get(String flag, Location location, String def)
		{
			if(location==null)
			{
				return def;
			}
			
			// Check if there is a region at the location
			String region = this.getRegion(location);
			if(region==null)
			{
				return def;
			}
			
			// Check if there is such a flag
			RegionSet regionSet = this.regionSets.get(flag);
			if(regionSet==null)
			{
				return def;
			}
			
			// Get the flag value
			return regionSet.get(region, def);
		}
		
		/**
		 * Get the flag boolean value at the given location.
		 * @param flag
		 * @param location
		 * @param def Default value in case the flag is not set here.
		 * @return
		 */
		public boolean getBoolean(String flag, Location location, boolean def)
		{
			if(location==null)
			{
				return def;
			}
			
			// Check if there is a region at the location
			String region = this.getRegion(location);
			if(region==null)
			{
				return def;
			}
			
			// Check if there is such a flag
			RegionSet regionSet = this.regionSets.get(flag);
			if(regionSet==null)
			{
				return def;
			}
			
			// Get the flag value
			return regionSet.getBoolean(region, def);
		}

		/**
		 * Get the flag integer value at the given location.
		 * @param flag
		 * @param location
		 * @param def Default value in case the flag is not set here.
		 * @return
		 */	
		public int getInt(String flag, Location location, int def)
		{
			if(location==null)
			{
				return def;
			}
			
			// Check if there is a region at the location
			String region = this.getRegion(location);
			if(region==null)
			{
				return def;
			}
			
			// Check if there is such a flag
			RegionSet regionSet = this.regionSets.get(flag);
			if(regionSet==null)
			{
				return def;
			}
			
			// Get the flag value
			return regionSet.getInt(region, def);
		}

		/**
		 * Get an applicable region name at the current position.
		 * regions with passthrough set to allow are ignored.
		 * @param location
		 * @return
		 */
		public abstract String getRegion(Location location);

		/**
		 * Get a string with all flag values for the specified region
		 * @param region
		 * @return
		 */
		
		public String getInfo(String region)
		{
			StringBuffer mySB = new StringBuffer();
			
			for(int i=0;i<this.validFlags.length;i++)
			{
				FlagValue flagValue = this.validFlags[i];

				RegionSet regionSet = this.regionSets.get(flagValue.flag);
				
				if(regionSet!=null)
				{
					if(regionSet.isSet(region))
					{
						if(mySB.length()!=0)
						{
							mySB.append("&f, ");
						}
						mySB.append("&f" + flagValue.flag + ": &9" + regionSet.get(region,""));
					}
				}
			}
			
			return mySB.toString();
		}
		
		/**
		 * Check if the specified region is actually a region
		 * @param region
		 * @return
		 */
		public abstract boolean isValidRegion(String region);
		
		/**
		 * Check if the specified flag is a valid flag
		 * @param flag
		 * @return
		 */
		public boolean isValidFlag(String flag)
		{
			for(int i=0;i<this.validFlags.length;i++)
			{
				if(flag.equals(validFlags[i].flag))
				{
					return true;
				}
			}
			
			return false;
		}
		
		/**
		 * Check if the specified value is valid for this flag
		 * @param flag
		 * @param value
		 * @return
		 */
		public boolean isValidFlagValue(String flag, String value)
		{
			for(int i=0;i<this.validFlags.length;i++)
			{
				if(flag.equals(this.validFlags[i].flag))
				{
					for(int j=0;j<this.validFlags[i].values.length;j++)
					{
						String _value = this.validFlags[i].values[j];
						if(_value.equals("[blank]"))
						{
							if(value.equals(""))
							{
								return true;
							}
						}
						else if(_value.equals("[integer]"))
						{
							try
							{
								Integer.parseInt(value);
								return true;
							}
							catch(NumberFormatException e)
							{
								return false;
							}
						}
						else if(_value.equals("[text]"))
						{
							return true;
						}
						else
						{
							if(_value.equals(value.toLowerCase()))
							{
								return true;
							}
						}
					}
				}
			}
			
			return false;
		}
		
		/**
		 * Get a string with all valid flags
		 * @return
		 */
		public String getValidFlags()
		{
			StringBuffer mySB = new StringBuffer();
			
			for(int i=0;i<this.validFlags.length;i++)
			{
				mySB.append(validFlags[i].flag);
				mySB.append(" ");
			}
			
			return mySB.toString();
		}
		
		/**
		 * Get a string with all the valid values for the given flag
		 * @param flag
		 * @return
		 */
		public String getValidFlagValues(String flag)
		{
			StringBuffer mySB = new StringBuffer();
			
			for(int i=0;i<this.validFlags.length;i++)
			{
				if(flag.equals(validFlags[i].flag))
				{
					for(int j=0;j<this.validFlags[i].values.length;j++)
					{
						mySB.append(this.validFlags[i].values[j]);
						mySB.append(" ");
					}
				}
			}
			
			return mySB.toString();
			
		}
		
		/**
		 * Load all flag files into memory
		 */
		public void load()
		{
			// First make sure that we have a directory
			File dataFolder = plugin.getDataFolder();
			String path = dataFolder.getAbsolutePath() + "/region";
			File regionFolder = new File(path);
			if(!regionFolder.exists())
			{
				regionFolder.mkdir();
			}
			
			for(int i=0;i<this.validFlags.length;i++)
			{
				String flag = validFlags[i].flag;

				RegionSet regionSet = new RegionSet(flag);
				
				File regionFile = new File(path + "/" + flag + ".cfg");
				
				try
				{
					if(regionFile.isFile())
					{
						BufferedReader reader = new BufferedReader(new FileReader(regionFile));
						
						String line;
						
						while( (line=reader.readLine())!=null)
						{
							String row = line.trim();
							if(row.length()>0 &&  row.charAt(0)!=';' && row.charAt(0)!='#')
							{
								// region:value
								String[] data = row.split(":",2);
								
								if(data.length==2 && data[1].length()>0)
								{
									regionSet.set(data[0].trim(), data[1].trim());
								}
							}
						}
						
						reader.close();
		
					}
				}
				catch(IOException e)
				{
					this.plugin.logMessage("Failed to load region file " + flag + ".cfg", true);
				}
				
				// open file
				// if file found read values into regionSet
				this.regionSets.put(flag, regionSet);
			}
		}
		
		/**
		 * Save a specific flag file
		 * @param regionSet
		 */
		public void save(RegionSet regionSet)
		{
			File dataFolder = plugin.getDataFolder();
			String path = dataFolder.getAbsolutePath() + "/region";

			try
			{
				String flag = regionSet.getFlag();
				
				File file = new File(path + "/" + flag + ".cfg");

				if(file.exists())
				{
					file.delete();
				}
				
				file.createNewFile();

				FileWriter writer = new FileWriter(file);
				
				writer.write("# Region flag file for " + flag + "\n");
				writer.write("#\n");
				
				String[] list = regionSet.getList();
				
				for(int j=0;j<list.length;j++)
				{
					writer.write(list[j] + "\n");
				}
				
				writer.flush();
				writer.close();
			}
			catch(IOException e)
			{
				this.plugin.logMessage("Failed to store regionSet + " + regionSet.getFlag(), true);
			}

		}
		
		private class FlagValue
		{
			public String flag;
			public String[] values;
			
			public FlagValue(String flag, String[] values)
			{
				this.flag = flag;
				this.values = values;
			}
		}

	}

