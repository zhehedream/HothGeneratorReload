
package biz.orgin.minecraft.hothgenerator;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

//import com.sk89q.worldedit.bukkit.selections.CuboidSelection;
//import com.sk89q.worldedit.bukkit.selections.Selection;

import biz.orgin.minecraft.hothgenerator.WorldType.InvalidWorldTypeException;
import biz.orgin.minecraft.hothgenerator.schematic.LoadedSchematic;
import biz.orgin.minecraft.hothgenerator.schematic.Schematic;
import biz.orgin.minecraft.hothgenerator.HothGenerator;
import me.zhehe.MagicIdHandler;
import org.bukkit.Bukkit;

/**
 * Main plugin class
 * @author orgin
 *
 */
public class HothGeneratorPlugin extends JavaPlugin
{
	public static final String LOGFILE = "plugins/HothGenerator/hoth.log";
	
	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	private BlockPlaceManager blockPlaceManager;
	private BlockBreakManager blockBreakManager;
	private ToolUseManager toolUseManager;
	private BuckitUseManager buckitUseManager;
	private BlockMeltManager blockMeltManager;
	private BlockGrowManager blockGrowManager;
	private BlockFormManager blockFormManager;
	private BlockFromToManager blockFromToManager;
	private StructureGrowManager structureGrowManager;
	private BlockSpreadManager blockSpreadManager;
	private CreatureSpawnManager creatureSpawnManager;
	private PlayerEnvironmentManager playerFreezeManager;
	private VolcanoManager volcanoManager;
	private BlockGravityManager blockGravityManager;
	private RegionManager regionManager;
	private MobSpawnManager mobSpawnManager;
	private DagobahSpiderForestManager spiderForestManager = null;
	private HothTaskManager taskManager;
	private EntityTargetManager entityTargetManager;
	private EntityDeathManager entityDeathManager;
	private EntityDamageManager entityDamageManager;
	
	private FileConfiguration config;
	private FileConfiguration worldConfig;
	
	private UndoBuffer undoBuffer;
        
        private MagicIdHandler mih;
	
	private long id = System.currentTimeMillis();
	
	public long getID()
	{
		return this.id;
	}
	
    public void onEnable()
    { 
    	HothGenerator.setPlugin(this);
        this.mih = new MagicIdHandler();
        mih.init(this);
    	
    	this.blockPlaceManager = new BlockPlaceManager(this);
    	this.blockBreakManager = new BlockBreakManager(this);
    	//this.toolUseManager = new ToolUseManager(this);
    	this.buckitUseManager = new BuckitUseManager(this);
    	this.blockMeltManager = new BlockMeltManager(this);
    	this.blockGrowManager = new BlockGrowManager(this);
    	this.blockFormManager = new BlockFormManager(this);
    	this.blockFromToManager = new BlockFromToManager(this);
    	this.structureGrowManager = new StructureGrowManager(this);
    	this.blockSpreadManager = new BlockSpreadManager(this);
    	this.creatureSpawnManager = new CreatureSpawnManager(this);
    	this.blockGravityManager = new BlockGravityManager(this);
    	this.regionManager = RegionManagerFactory.getRegionmanager(this);
    	this.entityTargetManager = new EntityTargetManager(this);
    	this.entityDeathManager = new EntityDeathManager(this);
    	this.entityDamageManager = new EntityDamageManager(this);
    	
    	this.getServer().getPluginManager().registerEvents(this.blockPlaceManager, this);
    	this.getServer().getPluginManager().registerEvents(this.blockBreakManager, this);
    	//this.getServer().getPluginManager().registerEvents(this.toolUseManager, this);
    	this.getServer().getPluginManager().registerEvents(this.buckitUseManager, this);
    	this.getServer().getPluginManager().registerEvents(this.blockMeltManager, this);
    	this.getServer().getPluginManager().registerEvents(this.blockGrowManager, this);
    	this.getServer().getPluginManager().registerEvents(this.blockFormManager, this);
    	this.getServer().getPluginManager().registerEvents(this.blockFromToManager, this);
    	this.getServer().getPluginManager().registerEvents(this.structureGrowManager, this);
    	this.getServer().getPluginManager().registerEvents(this.blockSpreadManager, this);
    	this.getServer().getPluginManager().registerEvents(this.creatureSpawnManager, this);
    	this.getServer().getPluginManager().registerEvents(this.blockGravityManager, this);
    	this.getServer().getPluginManager().registerEvents(this.entityTargetManager, this);
    	this.getServer().getPluginManager().registerEvents(this.entityDeathManager, this);
    	this.getServer().getPluginManager().registerEvents(this.entityDamageManager, this);
    	
	this.saveDefaultConfig();
    	this.config = this.getConfig();
    	
    	this.loadWorldConfig();

    	LootGenerator.load(this);
    	CustomGenerator.load(this);
    	OreGenerator.load(this);
    	this.saveResource("custom/example.sm", true);
    	this.saveResource("custom/example.ll", true);
    	this.saveResource("custom/example_ores.ol", true);
    	
    	this.regionManager.load();

    	this.playerFreezeManager = new PlayerEnvironmentManager(this);
    	this.volcanoManager = new VolcanoManager(this);
    	this.mobSpawnManager = new MobSpawnManager(this);
    	if(this.spiderForestManager==null)
    	{
    		this.spiderForestManager = new DagobahSpiderForestManager(this);
    	}
    	if(this.taskManager==null)
    	{
    		this.taskManager = new HothTaskManager(this);
    	}
    	else
    	{
    		this.taskManager.resume();
    	}
    	
    	this.undoBuffer = new UndoBuffer();
        
        //RecipeManager.registerRecipe(this);
        HothGeneratorPlugin ins = this;
        Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
			@Override
			public void run()
			{
				RecipeManager.registerRecipe(ins);
			}
	}, 1L);
    }
    
    public void onDisable()
    {
    	if(this.playerFreezeManager!=null)
    	{
    		this.playerFreezeManager.stop();
    	}
    	if(this.volcanoManager!=null)
    	{
    		this.volcanoManager.stop();
    	}
    	if(this.mobSpawnManager!=null)
    	{
    		this.mobSpawnManager.stop();
    	}
    	if(this.spiderForestManager!=null)
    	{
    		this.mobSpawnManager.stop();
    	}
    	if(this.taskManager!=null)
    	{
    		this.taskManager.pause();
    	}

    	// Unload worlds to make sure the ChunkGenerator is reloaded properly
    	List<World> worlds = this.getServer().getWorlds();
    	for(int i=0;i<worlds.size();i++)
    	{
			World world = worlds.get(i);
    		if(this.isHothWorld(world))
    		{
    			List<Player> players = world.getPlayers();
    			for(int j=0;j<players.size();j++)
    			{
    				Player player = players.get(j);
    				player.kickPlayer("Server reloading");
    			}
    			this.getServer().unloadWorld(world, true);
    		}
    	}

    }
    
    public void addTask(HothRunnable task)
    {
    	this.taskManager.addTask(task, false);
    }

    public void addTask(HothRunnable task, boolean prioritized)
    {
    	this.taskManager.addTask(task, prioritized);
    }
    
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args)
    {
    	// Log all actions
    	StringBuffer full = new StringBuffer();
    	for(int i=0;i<args.length;i++)
    	{
    		if(full.length()>0)
    		{
        		full.append(" ");
    		}
    		full.append(args[i]);
    	}
    	this.getLogger().info("[PLAYER COMMAND] " + sender.getName() + ": /" + cmd.getName() + " " + full.toString());

    	
    	if(cmd.getName().equalsIgnoreCase("hothreload"))
    	{
    		this.sendMessage(sender, "&bReloading HothGenerator config...");
    		
    		this.saveDefaultConfig(); // In case the file has been deleted
    		this.reloadConfig();
    		this.config = this.getConfig();
    		this.loadWorldConfig();
    		
    		LootGenerator.load(this);
    		CustomGenerator.load(this);
        	OreGenerator.load(this);
        	this.saveResource("custom/example.sm", true);
        	this.saveResource("custom/example.ll", true);
        	this.saveResource("custom/example_ores.ol", true);
        	
        	this.regionManager.load();

        	if(this.playerFreezeManager!=null)
        	{
        		this.playerFreezeManager.stop();
        	}
    		this.playerFreezeManager = new PlayerEnvironmentManager(this);

    		if(this.mobSpawnManager!=null)
        	{
        		this.mobSpawnManager.stop();
        	}
        	this.mobSpawnManager = new MobSpawnManager(this);

    		this.sendMessage(sender, "&b... reloading done.");

    		return true;
    	}
    	else if(cmd.getName().equalsIgnoreCase("hothexport"))
       	{
            this.sendMessage(sender, "&cERROR: hothexport is not supported at this time");
            return true;
    		/*if(args.length>0)
    		{
    			int maskid = -1;
    			
    			if(args.length==2)
    			{
    				try
    				{
    					maskid = Integer.parseInt(args[1]);
    					if(maskid<0)
    					{
    						this.sendMessage(sender, "&cERROR: Invalid mask: " + args[1]);
    						return false;
    					}
    				}
    				catch(NumberFormatException e)
    				{
    					this.sendMessage(sender, "&cERROR: Invalid mask: " + args[1]);
    					return false;
    				}
    			}
    			
	       		if(sender instanceof Player)
	       		{
	       			Player player = (Player)sender;
	       			World world = player.getWorld();
	       			
	       			if(ConfigManager.isWorldEditSelection(this))  // use worldedit cuboid region selection to export
	       			{
		       			Plugin plugin = getServer().getPluginManager().getPlugin("WorldEdit");
		       			if(plugin!=null && plugin instanceof WorldEditPlugin)
		       			{
		       				WorldEditPlugin wePlugin = (WorldEditPlugin)plugin;
		       				Selection selection = wePlugin.getSession(player).getSelection(world);
		       				if(selection!=null && selection instanceof CuboidRegion)
		       				{
		       					CuboidRegion cSelection = (CuboidRegion)selection;
		        				
	       						//ExportManager.export(this, world, cSelection, sender, args[0], maskid);
	       						CustomGenerator.load(this);
							}
							else
							{
								this.sendMessage(sender, "&cERROR: Selected region is not cuboid");
							}
		       			}
		       			else
		       			{
		       				this.sendMessage(sender, "&cERROR: WorldEdit plugin not installed");
		       			}
	       			}
	       			else // Use hothgenerator cuboid region selection to export
	       			{
		       			Location pos1 = this.toolUseManager.getPrimaryPosition(player);
		       			Location pos2 = this.toolUseManager.getSecondaryPosition(player);
		       			
		       			if(pos1!=null && pos2!=null)
		       			{
			       			if(pos1.getWorld().equals(world) && pos2.getWorld().equals(world))
			       			{
				       			ExportManager.export(this, world, pos1, pos2, sender, args[0], maskid);
								CustomGenerator.load(this);
			       			}
			       			else
			       			{
			       				this.sendMessage(sender, "&bThere's no valid selection. Try selecting again.");
			       			}
		       				
		       			}
		       			else
		       			{
		       				this.sendMessage(sender, "&bThere's no valid selection. Try selecting again.");
		       			}
	       			}
	       			
		       	}
				return true;
    		}*/
       	}
    	else if(cmd.getName().equalsIgnoreCase("hothsavell"))
       	{
    		if(args.length>0)
    		{
    			LootGenerator generator = null;
    			String name = args[0].toLowerCase();
    			
    			if(name.equals("default.ll"))
    			{
    				generator = LootGenerator.getLootGenerator();
    			}
    			else
    			{
    				generator = LootGenerator.getLootGenerator(name);
    			}
    			
    			if(generator!=null)
    			{
    				try
    				{
    					generator.save(this);
    				}
    				catch(Exception e)
    				{
        				this.sendMessage(sender, "&cFailed to save loot list: " + name);
    				}
    			}
    			else
    			{
    				this.sendMessage(sender, "&cCould not find any loot list with name: " + name);
    			}
    			return true;
    		}
       	}
    	else if(cmd.getName().equalsIgnoreCase("hothregion"))
    	{
    		if(args.length>0)
    		{
    			String command = args[0].toLowerCase();
    			if(command.equals("info"))
    			{
    				if(args.length>1)
    				{
    					String region = args[1].toLowerCase();
    					if(this.regionManager.isValidRegion(region))
    					{
    						this.sendMessage(sender, "&9Region: " + region + ": " + this.regionManager.getInfo(region));
    					}
    					else
    					{
        					this.sendMessage(sender, "&cERROR: " + region + " is not a valid region");
    					}
    				}
    				else
    				{
    					this.sendMessage(sender, "Usage: /hothregion info [region]");
    				}
    				return true;
    			}
    			if(command.equals("remove"))
    			{
    				if(args.length>1)
    				{
    					String region = args[1].toLowerCase();
    					if(this.regionManager.isValidRegion(region))
    					{
    						this.regionManager.remove(region);
        					this.sendMessage(sender, "&bRegion removed");
    					}
    					else
    					{
        					this.sendMessage(sender, "&cERROR: " + region + " is not a valid region");
    					}
    				}
    				else
    				{
    					this.sendMessage(sender, "Usage: /hothregion info [region]");
    				}
    				return true;
    			}
    			else if(command.equals("flag"))
    			{
    				if(args.length>2)
    				{
    					String region = args[1].toLowerCase();
    					if(this.regionManager.isValidRegion(region))
    					{
    						String flag = args[2].toLowerCase();
	    					if(this.regionManager.isValidFlag(flag))
	    					{
	    						String value = "";
	    						for(int i=3;i<args.length;i++)
	    						{
	    							value = value + args[i] + " ";
	    						}
	    						value = value.trim();
		    					if(this.regionManager.isValidFlagValue(flag, value))
		    					{
		    						regionManager.set(region, flag, value);
		    						if(value.equals(""))
		    						{
		    							this.sendMessage(sender, "&bRegion flag &9" + flag + "&b cleared");
		    						}
		    						else
		    						{
		    							this.sendMessage(sender, "&bRegion flag &9" + flag + "&b set to &f" + value);
		    						}
		    					}
		    					else
		    					{
		    						this.sendMessage(sender, "&cERROR: Valid values for " + flag + " are: " + this.regionManager.getValidFlagValues(flag));
		    					}
	    					}
	    					else
	    					{
	    						this.sendMessage(sender, "&cERROR: Valid flags are: " + this.regionManager.getValidFlags() );
	    					}
    					}
    					else
    					{
        					this.sendMessage(sender, "&cERROR: " + region + " is not a valid region");
    					}
    				}
    				else
    				{
    					this.sendMessage(sender, "Usage: /hothregion flag [region] [flag] <value>");
    				}
    				return true;
    			}
    		}
    	}
    	else if(cmd.getName().equalsIgnoreCase("hothpaste"))
    	{
    		if(args.length>1 && sender instanceof Player && CustomGenerator.schematics!=null)
    		{
    			Player player = (Player)sender;
    			Location loc = player.getLocation();
    			World world = player.getWorld();
    			int x = loc.getBlockX();
    			int y = loc.getBlockY();
    			int z = loc.getBlockZ();

    			String method = args[0].toLowerCase();

				int direction = -1;
				String directionStr = "south";
				if(args.length>2)
				{
					directionStr = args[2].toLowerCase();
					if(directionStr.equals("south"))
					{
						direction = 0;
					}
					else if(directionStr.equals("west"))
					{
						direction = 1;
					}
					else if(directionStr.equals("north"))
					{
						direction = 2;
					}
					else if(directionStr.equals("east"))
					{
						direction = 3;
					}
				}
				else
				{
					direction = 0;
				}
				
				if(direction!=-1)
				{

					if(method.equals("ext"))
					{

						String schematic = args[1].toLowerCase();

						boolean found = false;

						for(int i=0;i<CustomGenerator.schematics.size() && found == false;i++)
						{
							LoadedSchematic lschem = CustomGenerator.schematics.elementAt(i);
							if(lschem.getName().toLowerCase().equals(schematic))
							{
								LoadedSchematic schem = lschem.cloneRotate(direction);
								this.sendMessage(sender, "&bPlacing " + schematic + " at " + x + "," + y + "," + z + " direction: " + directionStr);
								this.undoBuffer.pushBlob(player.getUniqueId(), HothUtils.getUndoBlob(this, world, schem, x, y, z));
								HothUtils.placeSchematic(this, world, schem, x, y, z, lschem.getLootMin(), lschem.getLootMax());

								found = true;
							}
						}

						if(!found)
						{
							this.sendMessage(sender, "&cCould not find schematic: " + schematic);
						}

						return true;
					}
					else if(method.equals("int"))
					{
						String schematic = args[1].toLowerCase();

						boolean found = false;
						
						List<Schematic> schematics = InternalSchematics.getSchematics(this);

						for(int i=0;i<schematics.size() && found == false;i++)
						{
							Schematic lschem = schematics.get(i);
							if(lschem.getName().toLowerCase().equals(schematic))
							{
								Schematic schem = HothUtils.rotateSchematic(direction, lschem);
								this.sendMessage(sender, "&bPlacing " + schematic + " at " + x + "," + y + "," + z + " direction = " + directionStr);
								this.undoBuffer.pushBlob(player.getUniqueId(), HothUtils.getUndoBlob(this, world, schem, x, y, z));
								HothUtils.placeSchematic(this, world, schem, x, y, z, 2, 10);

								found = true;
							}
						}

						if(!found)
						{
							this.sendMessage(sender, "&cCould not find schematic: " + schematic);
						}


						return true;
					}
				}
    		}
    	}
    	else if(cmd.getName().equalsIgnoreCase("hothundo"))
    	{
    		if(sender instanceof Player)
    		{
    			Player player = (Player)sender;
    			
    			Blob blob = this.undoBuffer.popBlob(player.getUniqueId());
    			if(blob!=null)
    			{
    				this.sendMessage(player, "&bUndo schedueled");
    				blob.instantiate();
    			}
    			else
    			{
    				this.sendMessage(player, "&bUndo buffer is empty");
    			}
    			
    			return true;
    		}

    	}
    	else if(cmd.getName().equalsIgnoreCase("hothlist"))
    	{
    		if(args.length>0 && sender instanceof Player && CustomGenerator.schematics!=null)
    		{
    			String mode = args[0];
    			if(mode.equals("int"))
    			{
    				this.sendMessage(sender, "&bInternal scematics:");
					List<Schematic> schematics = InternalSchematics.getSchematics(this);
    				for(int i=0;i<schematics.size();i++)
    				{
    					Schematic schematic = schematics.get(i);
    					
    					this.sendMessage(sender, "&b " + schematic.getName());
    				}
        			return true;
    			}
    			else if(mode.equals("ext"))
    			{
    				this.sendMessage(sender, "&bExternal scematics:");
    				for(int i=0;i<CustomGenerator.schematics.size();i++)
    				{
    					Schematic schematic = CustomGenerator.schematics.elementAt(i);
    					
    					this.sendMessage(sender, "&b " + schematic.getName());
    				}
        			return true;
    			}
    		}
    	}
    	else if(cmd.getName().equalsIgnoreCase("hothinfo"))
    	{
    		PluginDescriptionFile descriptionFile = this.getDescription();
    		String version = descriptionFile.getVersion();
    		String name = descriptionFile.getName();
    		String website = descriptionFile.getWebsite();
    		List<String> authors = descriptionFile.getAuthors();
    		String description = descriptionFile.getDescription();
    		
    		this.sendMessage(sender, "&b" + name + " " + version);
    		this.sendMessage(sender, "&b" + description);
    		this.sendMessage(sender, "&b" + website);
    		
    		if(sender instanceof Player)
    		{
    			Player player = (Player)sender;
    			World world = player.getWorld();
    			if(this.isHothWorld(world))
    			{
        			this.sendMessage(sender, "&bWorld: " + world.getName() + " type: " + this.getWorldType(world));
    			}
    			else
    			{
        			this.sendMessage(sender, "&bWorld: " + world.getName() + " type: &cUNKNOWN");
    			}
    		}

    		String author = "";
    		
    		for(String authr : authors)
    		{
    			if(author.length()!=0)
    			{
    				author = author + ", ";
    			}
    			author = author + authr;
    		}

    		this.sendMessage(sender, "&bCreated by: " + author);
    	}
    	else if(cmd.getName().equalsIgnoreCase("hothaddworld"))
    	{
    		if(args.length==2)
    		{
    			String worldName = args[0];
    			String type = args[1];
    			return ConfigManager.addWorld(this, sender, worldName.toLowerCase(), type.toLowerCase());
    		}
    	}
    	else if(cmd.getName().equalsIgnoreCase("hothdelworld"))
    	{
    		if(args.length==1)
    		{
    			String worldName = args[0];
    			return ConfigManager.delWorld(this, sender, worldName.toLowerCase());
    		}
    	}
    	else if(cmd.getName().equalsIgnoreCase("hothsetworldtype"))
    	{
			if(args.length==2)
			{
				String world = args[0].toLowerCase();
				String type = args[1].toLowerCase();
				
				return ConfigManager.setWorldType(this, sender, world, type);
			}
    		
    	}
    	else if(cmd.getName().equalsIgnoreCase("hothsetworldflag"))
    	{
			if(args.length>1)
			{
				String world = args[0].toLowerCase();
				String flag = args[1].toLowerCase();

				String value = "";
				for(int i=2;i<args.length;i++)
				{
					value = value + args[i] + " ";
				}
				value = value.trim();
				
				return ConfigManager.setWorldFlag(this, sender, world, flag, value);
			}
    	}
    	else if(cmd.getName().equalsIgnoreCase("hothworldinfo"))
    	{
    		if(args.length==0)
    		{
				return ConfigManager.printWorldList(this, sender);
    		}
    		else if(args.length==1)
			{
				String world = args[0].toLowerCase();
				return ConfigManager.printWorldInfo(this, sender, world);
			}
    	}
    	return false;
    }
    
    public void sendMessage(Player sender, String message)
    {
    	sender.sendMessage(MessageFormatter.format(message));
    }

    public void sendMessage(CommandSender sender, String message)
    {
    	sender.sendMessage(MessageFormatter.format(message));
    }

    public void sendMessage(Server sender, String message)
    {
    	sender.broadcastMessage(MessageFormatter.format(message));
    }
 	
	public ChunkGenerator getDefaultWorldGenerator(String worldName, String id)
	{
		if(id==null)
		{
			return new WorldGenerator(worldName); // Non hard coded world type
		}
		
		try
		{
			// See if we can make a world with a hardcoded type
			WorldType worldType = WorldType.getType(id.toLowerCase());
			switch(worldType)
			{
				case TATOOINE:
					return new TatooineGenerator(worldName);
				case DAGOBAH:
					return new DagobahGenerator(worldName);
				case MUSTAFAR:
					return new MustafarGenerator(worldName);
				case HOTH:
					return new HothGenerator(worldName);
				case KASHYYYK:
					return new KashyyykGenerator(worldName);
				case KAMINO:
					return new KaminoGenerator(worldName);
				default:
					return new WorldGenerator(worldName); // Non hard coded world type
			}
		}
		catch (InvalidWorldTypeException e)
		{
			this.debugMessage("WARNING! You have specified an unknown world type (" + id + ") for " + worldName + ". Defaulting to Hoth type world");
			return new WorldGenerator(worldName); // Non hard coded world type
		}
	}
	
	public int getHeight()
	{
		return 256;
	}
	
	/**
	 * Check if the current world is a hothworld. Hoth worlds are defined in the config.
	 * The comparison is case insensitive.
	 * @param world The world to check
	 * @return True if the world is in the hoth world list
	 */
	public boolean isHothWorld(World world)
	{
            if(this.worldConfig == null) return false;
		List<String> list = this.worldConfig.getStringList("hothworlds");
		
		if(list!=null)
		{
			String current = world.getName().toLowerCase();
			
			for(int i=0;i<list.size();i++)
			{
				String item = list.get(i).toLowerCase();
				
				if(item.equals(current))
				{
					return true;
				}
			}
		}
		return false;
	}

	public boolean isHothWorld(String world)
	{
		List<String> list = this.worldConfig.getStringList("hothworlds");
		
		if(list!=null)
		{
			String current = world.toLowerCase();
			
			for(int i=0;i<list.size();i++)
			{
				String item = list.get(i).toLowerCase();
				
				if(item.equals(current))
				{
					return true;
				}
			}
		}
		return false;
	}

	public WorldType getWorldType(World world)
	{
		return this.getWorldType(world.getName());
	}

	public WorldType getWorldType(String worldName)
	{
		List<String> list = this.worldConfig.getStringList("hothworlds");
		String name = worldName.toLowerCase();
		
		for(int i=0;i<list.size();i++)
		{
			String _worldName = list.get(i);
			if(_worldName.equals(name))
			{
				String type = this.worldConfig.getString("hothworldsdata." + _worldName + ".type", "hoth").toLowerCase();
				try
				{
					WorldType worldType = WorldType.getType(type);
					return worldType;
				}
				catch(InvalidWorldTypeException e)
				{
					return WorldType.HOTH;
				}
			}
		}
		
		return WorldType.HOTH;
	}

	/**
	 * Check if the given block is the highest at that x,z position.
	 * Only air blocks are treated as empty.
	 * @param world The world to check
	 * @param block The block to check
	 * @return true if it's the highest block
	 */
	public boolean blockIsHighest(World world, Block block)
	{
		int x = block.getX();
		int y = block.getY();
		int z = block.getZ();
		
		if(y<255)
		{
			y++;
			while(y<256)
			{
				if(!world.getBlockAt(x, y, z).equals(Material.AIR))
				{
					return false;
				}
				y++;
			}
		}
		
		return true;
	}
	
	public void addSpiderForest(World world, int x, int y, int z, int size)
	{
    	if(this.spiderForestManager==null)
    	{
    		this.spiderForestManager = new DagobahSpiderForestManager(this);
    	}

		this.spiderForestManager.add(world, x, y, z, size);
	}
	
	/**
	 * Returns true if the given block is allowed to be liquid
	 * @param world The world to check
	 * @param block The block to check
	 * @return True if allowed
	 */
	public boolean canPlaceLiquid(World world, Block block)
	{
		int y = block.getY();
		int surfaceOffset = ConfigManager.getWorldSurfaceoffset(this, world);
		
		return !(y>(63 + surfaceOffset) || (y>(26 + surfaceOffset) && this.blockIsHighest(world, block)));
	}

	/**
	 * Logs the given message on the console as an info message.
	 * Messages are only logged if the hoth.debug flag is set to true in the config file.
	 * @param message The message to log.
	 */
	public void debugMessage(String message)
	{
		if(ConfigManager.isDebug(this))
		{
			this.getLogger().info(message);
		}
	}
	
	/**
	 * Sends a message to the log file.
	 * @param message The message to send.
	 */
	public void logMessage(String message)
	{
		this.logMessage(message, false);
	}

	/**
	 * Sends a message to the log file and optionally to the console.
	 * The console message is sent by the debugMessage() method.
	 * @param message The message to send.
	 * @param onConsole Whether it should be sent to teh console or not.
	 */
	public void logMessage(String message, boolean onConsole)
	{
		if(onConsole)
		{
			this.debugMessage(message);
		}
		
		try
		{
			FileWriter writer = new FileWriter(HothGeneratorPlugin.LOGFILE, true);
			writer.write(HothGeneratorPlugin.dateFormat.format(new Date()) + " " + message);
			writer.write("\n");
			writer.close();
		}
		catch(IOException e)
		{
			this.getLogger().info("Failed to write to log file " + HothGeneratorPlugin.LOGFILE);
		}
		
	}
	
	public void loadWorldConfig()
	{
		this.worldConfig = new YamlConfiguration();

		File configFile = this.getWorldConfigFile();
		
		if(configFile.exists())
		{
			try
			{
				this.worldConfig.load(configFile);
			}
			catch (Exception e)
			{
				this.debugMessage("Could not open worldConfig file! : " );
				e.printStackTrace();
			}
		}
		else
		{
			// See if there is a section in the normal config that we can read. This makes sure that
			// we copy the old world list to the new config file instead of messign up everyone's server.
			Object section = this.config.get("hothworlds");
			if(section!=null)
			{
				this.worldConfig.set("hothworlds", section);
			}
			
			section = this.config.get("hothworldsdata");
			if(section!=null)
			{
				this.worldConfig.set("hothworldsdata", section);
			}
			
			this.saveWorldConfig();
		}
		
	}
	
	public void saveWorldConfig()
	{
		File configFile = this.getWorldConfigFile();

		try
		{
			this.worldConfig.save(configFile);
		}
		catch (IOException e)
		{
			this.debugMessage("Failed to save worldConfig file! : " );
			e.printStackTrace();
		}
	}
	
	private File getWorldConfigFile()
	{
		return new File(this.getDataFolder().getAbsolutePath() + "/" + "worldConfig.yml");
	}

	public FileConfiguration getWorldConfig()
	{
		return this.worldConfig;
	}
	
	public RegionManager getRegionManager()
	{
		return this.regionManager;
	}
}