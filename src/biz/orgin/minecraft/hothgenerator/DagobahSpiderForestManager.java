package biz.orgin.minecraft.hothgenerator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.entity.CaveSpider;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

public class DagobahSpiderForestManager
{
	HothGeneratorPlugin plugin;
	private static String listFile = "plugins/HothGenerator/spiderforests.lis";
	private Set<SpiderCoord> coords;
	private boolean loaded;

	private int taskId;

	public DagobahSpiderForestManager(HothGeneratorPlugin plugin)
	{
		this.plugin = plugin;
		this.coords = new HashSet<SpiderCoord>();
		this.loaded = false;
		this.taskId = -1;

		int period = 40; // Get this from config ?

		this.taskId = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new SpiderThread(new Random(System.currentTimeMillis())), 10, period);

	}

	private void load()
	{
		Server server = Bukkit.getServer();

		// Load coordinates from file
		try
		{
			File file = new File(DagobahSpiderForestManager.listFile);
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String read;
			while( (read=reader.readLine())!=null)
			{
				String[] parts = read.split(",");
				if(parts.length==5)
				{
					try
					{
						World world = server.getWorld(parts[0]);
						if(world!=null)
						{
							int x = Integer.parseInt(parts[1]);
							int y = Integer.parseInt(parts[2]);
							int z = Integer.parseInt(parts[3]);
							int size = Integer.parseInt(parts[4]);
							if(world.loadChunk(x/16, z/16, false))  // Is the chunk generated? In case someone decides to remove the chunk/world data
							{
								this.add(world, x, y, z, size);
								world.unloadChunkRequest(x/16, z/16);  // Make sure we don't have chunks loaded for no good reason.
							}
						}
					}
					catch(NumberFormatException e)
					{
						// Malformed, someone has edited the file manually? Ignore the row
					}
				}
			}
			reader.close();
		}
		catch(IOException e)
		{
			// Could not read the file. ohh well.
		}


		this.save(); // Make sure we keep the data consistent
	}

	private void save()
	{
		try
		{
			File file = new File(DagobahSpiderForestManager.listFile);

			if(file.exists())
			{
				file.delete();
			}
			file.createNewFile();

			FileWriter writer = new FileWriter(file);

			SpiderCoord[] array = this.coords.toArray(new SpiderCoord[0]);

			for(int i=0;i<array.length;i++)
			{
				String row = array[i].toString();
				writer.write(row);
				writer.write("\n");
			}
			writer.close();
		}
		catch(IOException e)
		{
			this.plugin.logMessage("Failed to save spider forest coordinates. Previously generated spider forests won't work properly!");
		}
	}

	/* called by chunk populator each time a spider forest is made */
	public void add(World world, int x, int y, int z, int size)
	{
		if(this.coords==null)
		{
			this.coords = new HashSet<SpiderCoord>();
		}
		
		this.coords.add(new SpiderCoord(world.getName(), x, y, z, size));
		this.save(); // Immediately store the list to keep it consistent
	}


	public void stop()
	{
		if(this.taskId!=-1)
		{
			Bukkit.getServer().getScheduler().cancelTask(this.taskId);
		}
	}

	private void remove(SpiderCoord coord)
	{
		this.coords.remove(coords);
	}

	private class SpiderThread implements Runnable
	{
		private Random random;
		
		public SpiderThread(Random random)
		{
			this.random = random;
		}

		public void run()
		{
			if(!loaded)
			{
				load();
				loaded = true;
			}

			boolean save = false;

			SpiderCoord[] array = coords.toArray(new SpiderCoord[0]);

			Server server = Bukkit.getServer();

			for(int i=0;i<array.length;i++)
			{
				World world = server.getWorld(array[i].world);
				if(world!=null)
				{
					List<Player> players = world.getPlayers();
					Player found = null;
					for(int p=0;p<players.size() && found==null;p++) // Check if any player is near the spider forest
					{
						Player player = players.get(p); 
						if(this.isPlayerClose(player, array[i]))
						{
							found = player;
						}
					}
					
					if(found!=null)
					{
						int cnt = this.getNearbyCaveSpiderCount(found);
						if(cnt<20)
						{
							int xx = array[i].x + this.random.nextInt(20)-10;
							int zz = array[i].z + this.random.nextInt(20)-10;
							int yy = this.getSurfaceLevel(world.getHighestBlockAt(xx, zz));
							
							world.spawnEntity(new Location(world,xx,yy,zz), EntityType.CAVE_SPIDER);
						}
						
					this.placeWeb(world, array[i]);
					}
				}
				else
				{
					remove(array[i]);
					save = true;
				}
			}

			if(save)
			{
				save();
			}
		}
		
		private void placeWeb(World world, SpiderCoord coord)
		{
			int x = coord.x;
			int y = coord.y;
			int z = coord.z;
			int size = coord.size;
			
			y = y -2;
			int height = y + 3 + size/2;
			int failed = 0;
			
			int i=0;
			while(i<20) // Try to place 20 webs
			{
				int rx = random.nextInt(size*2)-size;
				int rz = random.nextInt(size*2)-size;
				int yy = y + random.nextInt(height-y);
				
				if(Math.sqrt(rx*rx + rz*rz) < (size-(yy-y)))
				{
					int xx = x + rx;
					int zz = z + rz;
					
					Block block = world.getBlockAt(xx, yy, zz);
					if(block.isEmpty() && DagobahSpiderForestPopulator.isAttachable(block))
					{
						BlockState state = block.getState();
						state.setType(Material.COBWEB);
						state.update(true, false);
						failed = 0;
					}
					else
					{
						if(block.getType().equals(Material.COBWEB))
						{
							failed++; // Web hits counts as two hits. Just to reduce the growth a bit the more web there is
						}
						failed++;
					}
				}
				
				if(failed==0 || failed>60) // Max 60 attempts to place a single web
				{
					i++;
				}
			}

		}
		
		private int getSurfaceLevel(Block block)
		{
			Material type = block.getType();
			while(block.getY()>64 && !type.equals(Material.GRASS_BLOCK) && !type.equals(Material.DIRT) && !type.equals(Material.WATER))
			{
				block = block.getRelative(BlockFace.DOWN);
				type = block.getType();
			}
			
			return block.getY();
		}
		
		private int getNearbyCaveSpiderCount(Player player)
		{
			int cnt = 0;
			List<Entity> list = player.getNearbyEntities(50, 50, 50);
			for(int i=0;i<list.size();i++)
			{
				if(list.get(i) instanceof CaveSpider)
				{
					cnt++;
				}
			}
			
			return cnt;
		}

		private boolean isPlayerClose(Player player, SpiderCoord coord)
		{
			Location loc = player.getLocation();
			
			if(!loc.getWorld().getName().equals(coord.world))
			{
				return false;
			}

			int x1=loc.getBlockX();
			int z1=loc.getBlockZ();
			int x2 = coord.x;
			int z2 = coord.z;

			int dx = x1-x2;
			int dz = z1-z2;

			if(Math.sqrt(dx*dx + dz*dz) < coord.size)
			{
				return true;
			}
			return false;
		}
	}


	private class SpiderCoord
	{
		public int x;
		public int y;
		public int z;
		public String world;
		public int size;

		public SpiderCoord(String world, int x, int y, int z, int size)
		{
			this.world = world;
			this.x = x;
			this.y = y;
			this.z = z;
			this.size = size;
		}

		public boolean equals(Object o)
		{
			if(o instanceof SpiderCoord)
			{
				SpiderCoord other = (SpiderCoord)o;
				if(other.world.equals(other.world) && other.x == this.x && other.z == this.z)
				{
					return true;
				}
			}
			return false;
		}

		public int hashCode()
		{
			return this.x*32767 + this.z + this.world.hashCode();
		}

		public String toString()
		{
			return this.world+","+this.x+","+this.y+","+this.z+","+this.size;
		}
	}
}

