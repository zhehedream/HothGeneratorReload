package biz.orgin.minecraft.hothgenerator;

import java.util.Iterator;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.*;

/**
 * Spawns neutral mobs around the player.
 * @ToDo: This might be performance draining. Speed it up. 
 * @author orgin
 *
 */
public class MobSpawnManager
{
	private HothGeneratorPlugin plugin;
	private int taskId;
	
	private static Material[] spawnBlocks = new Material[]{
		Material.GRASS,
		Material.GRASS,
		Material.MYCELIUM,
		Material.GRASS,
		Material.GRASS,
		Material.GRASS,
		Material.GRASS,
                Material.GRASS,
                Material.GRASS,
		Material.GRASS
        };
	
	private static EntityType[] mobTypes = new EntityType[]{
		EntityType.CHICKEN,
		EntityType.COW,
		EntityType.MUSHROOM_COW,
		EntityType.OCELOT,
		EntityType.PIG,
		EntityType.SHEEP,
		EntityType.WOLF,
		EntityType.HORSE,
                EntityType.DONKEY,
                EntityType.MULE,
                
        };
        private static EntityType[] aquaMobTypes = new EntityType[]{
                EntityType.SQUID,
                EntityType.COD,
                EntityType.SALMON,
                EntityType.TROPICAL_FISH,
                EntityType.PUFFERFISH
        };
	
	private static int[] maxExisting = new int[] {4, 2, 2, 1, 2, 2, 1, 2 , 2, 2};
	private static double[] probability = new double[] { 5.0, 5.0, 3.0, 1.0, 5.0, 5.0, 1.0, 1.0, 1.0, 1.0 };
	
	private static String[] mobNames = new String[]
			{
				"chicken",
				"cow",
				"mushroom_cow",
				"ocelot",
				"pig",
				"sheep",
				"wolf",
				"horse",
                                "donkey",
                                "mule",
			};
        private static String[] aquaMobNames = new String[]
                        {
                                "squid",
                                "cod",
                                "salmon",
                                "tropical_fish",
                                "pufferfish"
                        };
	
	public MobSpawnManager(HothGeneratorPlugin plugin)
	{
		this.plugin = plugin;
		
		int period = 20*5;
		
		this.taskId = -1;
		if(period>0)
		{
			this.taskId = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new SpawnMobs(this.plugin), 10, period);
		}

	}
	
	/**
	 * Stop the scheduled repeating task.
	 * A new instance of the MobSpawnManager must be made to restart a new task.
	 * Use this to shut down the currently running freeze task before a reload.
	 */
	public void stop()
	{
		if(this.taskId!=-1)
		{
			Bukkit.getServer().getScheduler().cancelTask(this.taskId);
		}
	}
	
	private class SpawnMobs implements Runnable
	{
		HothGeneratorPlugin plugin;
		Random random;
		
		public SpawnMobs(HothGeneratorPlugin plugin)
		{
			this.plugin = plugin;
			this.random = new Random();
		}
		
		@Override
		public void run()
		{
			Server server = this.plugin.getServer();
			
			List<World> worlds = server.getWorlds();
			
			// Find all worlds configured as hoth worlds and check if each world has spawn neutral rule
			for(int i=0;i<worlds.size();i++)
			{
				World world = worlds.get(i);
				if(this.plugin.isHothWorld(world) && this.plugin.getWorldType(world) == WorldType.HOTH && ConfigManager.isRulesSpawnNeutralOn(this.plugin, world))
				{
					this.spawnMobs(world);
				}
			}
		}
		
		private void spawnMobs(World world)
		{
			List<Player> players = world.getPlayers();
			
			Iterator<Player> pi = players.iterator();
			
			// Check the area around each player on this world
			while(pi.hasNext())
			{
				Player player = pi.next();
				
				for(int i=0;i<MobSpawnManager.mobTypes.length;i++)
				{
					
					EntityType mobType = MobSpawnManager.mobTypes[i];
					Material material = MobSpawnManager.spawnBlocks[i];
					int typeId = MaterialManager.toID(material);
					
					int x = 25-this.random.nextInt(50) + player.getLocation().getBlockX();
					int z = 25-this.random.nextInt(50) + player.getLocation().getBlockZ();
					
					List<Entity> entities = player.getNearbyEntities(50, world.getMaxHeight(), 50);
					
					int cnt = 0;
					int maxCnt = MobSpawnManager.maxExisting[i];
					double mindis = 999.0;
					
					// Check how many mobs of this type that already exists close by
					Iterator<Entity> iterator = entities.iterator();
					while(cnt<=maxCnt && iterator.hasNext())
					{
						Entity entity = iterator.next();
						Location location = entity.getLocation();
						
						if(entity.getType().equals(mobType))
						{
							double dis = Math.sqrt(
									(x-location.getX())*(x-location.getX())
								  + (z-location.getZ())*(z-location.getZ())
										);
							
							if(mindis>dis)
							{
								mindis = dis;
							}
							
							cnt++;
						}
					}
					
					if(cnt<maxCnt && mindis>15) // allowed to spawn
					{
						// Loop through the block column for places where we can spawn
						for(int y=0;y<world.getMaxHeight()-1;y++)
						{
							int id = MaterialManager.toID(world.getBlockAt(x, y, z).getType());
							if(id == typeId && world.getBlockAt(x,  y+1, z).equals(Material.AIR))
							{
								Location location = new Location(world, x, y+1, z);
								
								// Check if mobs of this type is allowed at this location
								String allowedMobs = ConfigManager.getRulesSpawnNeutralMobs(this.plugin, location);
								String[] mobs = allowedMobs.split(",");
								
								for(int m=0;m<mobs.length;m++)
								{
									if(mobs[m].equals(MobSpawnManager.mobNames[i]))
									{
										int test = this.random.nextInt(1000 * ConfigManager.getRulesSpawnNeutralRarity(this.plugin, location));
										int prob = (int)(MobSpawnManager.probability[i]*10);
										
										if( test < prob)
										{
											Block block = world.getBlockAt(x,  y+1, z);
											Byte light = block.getLightLevel();
											Byte skyLight = block.getLightFromSky();
											
											if(light > 9 && skyLight < 9)
											{
												world.spawnEntity(location, mobType);
											}
										}

										break;
									}
								}
							}
						}
					}
				}
			}
		}
	}
}
