package biz.orgin.minecraft.hothgenerator;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * Handles giving players damage when exposed to the cold.
 * @author orgin
 *
 */
public class PlayerEnvironmentManager
{
	private HothGeneratorPlugin plugin;
	private int taskId;
	
	public PlayerEnvironmentManager(HothGeneratorPlugin plugin)
	{
		this.plugin = plugin;
		
		this.plugin.debugMessage("Initializing PlayerEnvironmentManager. Starting repeating task.");
		
		int period = ConfigManager.getRulesEnvironmentPeriod(this.plugin);
		
		this.taskId = -1;
		if(period>0)
		{
			this.taskId = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new DamagePlayers(this.plugin), 10, period * 20);
		}

	}
	
	/**
	 * Stop the scheduled repeating task.
	 * A new instance of the PlayerFreezeManager must be made to restart a new task.
	 * Use this to shut down the currently running freeze task before a reload.
	 */
	public void stop()
	{
		if(this.taskId!=-1)
		{
			Bukkit.getServer().getScheduler().cancelTask(this.taskId);
		}
	}
	
	private class PlayerData
	{
		public Location location;  // Location of a mosquito attack
		public int stage;  // 0=Ok, 1=hear, 2=see, 3=attack
		public int ctr;
		
		public PlayerData(Location location)
		{
			this.location = location;
			this.stage = 0;
			this.ctr = 0;
		}
	}
	
	private class DamagePlayers implements Runnable
	{
		HothGeneratorPlugin plugin;
		
		private Map<UUID, Integer> thirsts;
		private Map<UUID, PlayerData> mosquitos;
		private Map<UUID, PlayerData> leeches;
		
		private Random random;
		
		
		public DamagePlayers(HothGeneratorPlugin plugin)
		{
			this.plugin = plugin;
			this.thirsts = new HashMap<UUID, Integer>();
			this.mosquitos = new HashMap<UUID, PlayerData>();
			this.leeches = new HashMap<UUID, PlayerData>();
			
			this.random = new Random(System.currentTimeMillis());
		}
		
		@Override
		public void run()
		{
			Server server = this.plugin.getServer();
			
			List<World> worlds = server.getWorlds();
			
			// Find all worlds configured as hoth worlds
			for(int i=0;i<worlds.size();i++)
			{
				World world = worlds.get(i);
				if(this.plugin.isHothWorld(world))
				{
					if(this.plugin.getWorldType(world) == WorldType.HOTH)
					{
						this.freeze(world);
					}
					else if(this.plugin.getWorldType(world) == WorldType.TATOOINE)
					{
						this.heat(world);
					}
					else if(this.plugin.getWorldType(world) == WorldType.DAGOBAH)
					{
						this.mosquito(world);
						this.leech(world);
					}
					else if(this.plugin.getWorldType(world) == WorldType.MUSTAFAR)
					{
						this.lavaBurn(world);
					}
				}
			}
		}
		
		/**
		 * Finds all players on the specified world and applies mosquito damage.
		 * @param world
		 */
		private void mosquito(World world)
		{
			
			List<Player> players = world.getPlayers();
			Iterator<Player> iterator = players.iterator();
			
			while(iterator.hasNext())
			{
				Player player = iterator.next();
				UUID uuid = player.getUniqueId();
				
				PlayerData mosquito;
				
				
				if(this.mosquitos.containsKey(uuid))
				{
					mosquito = this.mosquitos.get(uuid);
				}
				else
				{
					mosquito = new PlayerData(player.getLocation());
					this.mosquitos.put(uuid, mosquito);
				}
				
				GameMode gm = player.getGameMode();
				if(!gm.equals(GameMode.CREATIVE) && !gm.equals(GameMode.SPECTATOR) && PermissionManager.hasMosquitoPermission(player) && !this.hasRepellentSuit(player))
				{
					Location location = player.getLocation();
					int damage = ConfigManager.getRulesMosquitoDamage(this.plugin, location);
					
					if(damage>0)
					{
						String message1 = ConfigManager.getRulesMosquitoMessage1(this.plugin, location);
						String message2 = ConfigManager.getRulesMosquitoMessage2(this.plugin, location);
						String message3 = ConfigManager.getRulesMosquitoMessage3(this.plugin, location);
						String message4 = ConfigManager.getRulesMosquitoMessage4(this.plugin, location);
						int rarity = ConfigManager.getRulesMosquitoRarity(this.plugin, location);
						int runfree = ConfigManager.getRulesMosquitoRunFree(this.plugin, location);
	
						Block block = world.getBlockAt(location.getBlockX(), location.getBlockY()+1, location.getBlockZ());
						Block block2 = world.getBlockAt(location.getBlockX(), location.getBlockY(), location.getBlockZ());
						
						Double distance = this.getDistance(location, mosquito.location);
						
						if(block.getType().equals(Material.WATER) || (mosquito.stage!=0 && distance>runfree))
						{
							if(mosquito.stage!=0)
							{
								mosquito.stage = 0;
								plugin.sendMessage(player, message1); // You seem to have lost the mosquito swarm.
							}
						}
						else
						{
							switch(mosquito.stage)
							{
							case 0: // ok
								Block block3 = block2.getRelative(BlockFace.DOWN);
								if(block3.getType().equals(Material.GRASS))
								{
									int rand = this.random.nextInt(20*rarity); // attacks are completely random
									
									if(rand==1)
									{
										mosquito.stage = 1;
										mosquito.ctr = 0;
										mosquito.location = player.getLocation();
										plugin.sendMessage(player, message2); // You hear some buzzing nearby.
									}
								}
								
								break;
							case 1: // hear
								mosquito.ctr++;
								mosquito.location = this.moveCloser(player.getLocation(), mosquito.location, 0.7);
								if(mosquito.ctr>5 || this.random.nextInt(20) == 1)
								{
									mosquito.stage = 2;
									mosquito.ctr = 0;
									plugin.sendMessage(player, message3); // You can see a mosquito swarm nearby.
								}
								break;
							case 2: // see
								mosquito.ctr++;
								mosquito.location = this.moveCloser(player.getLocation(), mosquito.location, 0.7);
								if(mosquito.ctr>3 || this.random.nextInt(10) == 1)
								{
									mosquito.stage = 3;
									mosquito.ctr = 0;
								}
								break;
							case 3: // attack
								mosquito.location = this.moveCloser(player.getLocation(), mosquito.location, 0.9);
	
								double oldDamage = player.getHealth();
								if(oldDamage - damage <= 0)
								{
									mosquito.stage = 0; // Player is going to die, reset stage 
								}
	
								plugin.sendMessage(player, message4); // Mosquitos are attacking you! Run!!!
								player.damage(damage);
								break;
							}
						}
					}
				}
				else
				{
					// reset mosquitos
					mosquito = new PlayerData(player.getLocation());
					this.mosquitos.put(uuid, mosquito);
				}
			}
		}
		
		private Location moveCloser(Location player, Location swarm, double factor)
		{
			double dx = player.getX() - swarm.getX();
			double dy = player.getY() - swarm.getY();
			double dz = player.getZ() - swarm.getZ();
			
			return new Location(swarm.getWorld(), player.getX() + dx*factor, player.getY() + dy*factor, player.getZ() + dz*factor);
		}
		
		private double getDistance(Location player, Location swarm)
		{
			if(player.getWorld().equals(swarm.getWorld()))
			{
				double dx = player.getX()-swarm.getX();
				double dy = player.getY()-swarm.getY();
				double dz = player.getZ()-swarm.getZ();
				return Math.sqrt(dx*dx + dy*dy + dz*dz);
			}
			else
			{
				return 9999;
			}
		}

		/**
		 * Finds all players on the specified world and applies leech damage.
		 * @param world
		 */
		private void leech(World world)
		{
			List<Player> players = world.getPlayers();
			Iterator<Player> iterator = players.iterator();
			
			while(iterator.hasNext())
			{
				Player player = iterator.next();
				UUID uuid = player.getUniqueId();
				
				PlayerData leech;
				
				
				if(this.leeches.containsKey(uuid))
				{
					leech = this.leeches.get(uuid);
				}
				else
				{
					leech = new PlayerData(player.getLocation());
					this.leeches.put(uuid, leech);
				}
				
				GameMode gm = player.getGameMode();
				if(!gm.equals(GameMode.CREATIVE) && !gm.equals(GameMode.SPECTATOR) && PermissionManager.hasLeechPermission(player) && !this.hasRepellentSuit(player))
				{
					Location location = player.getLocation();
					int damage = ConfigManager.getRulesLeechDamage(this.plugin, location);
					
					if(damage>0)
					{
						String message1 = ConfigManager.getRulesLeechMessage1(this.plugin, location);
						String message2 = ConfigManager.getRulesLeechMessage2(this.plugin, location);
						String message3 = ConfigManager.getRulesLeechMessage3(this.plugin, location);
						String message4 = ConfigManager.getRulesLeechMessage4(this.plugin, location);
						String message5 = ConfigManager.getRulesLeechMessage5(this.plugin, location);
						int rarity = ConfigManager.getRulesMosquitoRarity(this.plugin, location);
	
						Block block = world.getBlockAt(location.getBlockX(), location.getBlockY()+1, location.getBlockZ());
						Block block2 = world.getBlockAt(location.getBlockX(), location.getBlockY(), location.getBlockZ());
						
						boolean isInWater = HothUtils.isWater(block) || HothUtils.isWater(block2);
						
						if(leech.stage!=0 && !leech.location.getWorld().equals(world))
						{
							leech.stage = 0;
							plugin.sendMessage(player, message1);
						}
						
						switch(leech.stage)
						{
						case 0: // ok
							if(isInWater)
							{
								int rand = this.random.nextInt(20*rarity); // attacks are completely random
								if(rand==1)
								{
									leech.stage = 1;
									leech.ctr = 0;
									leech.location = player.getLocation();
									plugin.sendMessage(player, message2);
								}
							}
							
							break;
						case 1: // something moving
							if(isInWater)
							{
								leech.ctr++;
								if(leech.ctr>4 || this.random.nextInt(8) == 1)
								{
									leech.stage = 2;
									leech.ctr = 0;
									plugin.sendMessage(player, message3);
								}
							}
							else
							{
								leech.stage = 0;
							}
							break;
						case 2: // see leeches in water
							if(isInWater)
							{
								leech.ctr++;
								if(leech.ctr>4 || this.random.nextInt(4) == 1)
								{
									leech.stage = 3;
									leech.ctr = 0;
									leech.ctr = 50;
								}
							}
							else
							{
								leech.stage = 0;
							}
							break;
						case 3: // attacked by leeches
							if(leech.ctr<0)
							{
								leech.stage = 0;
								plugin.sendMessage(player, message1); // Shaked off leeches
							}
							else
							{
								if(isInWater)
								{
									leech.ctr = leech.ctr + 5;
									if(leech.ctr>100)
									{
										leech.ctr = 100;
									}
								
									double oldDamage = player.getHealth();
									if(oldDamage - damage <= 0)
									{
										leech.stage = 0; // Player is going to die, reset stage 
									}
		
									plugin.sendMessage(player, message4); // Under attack, damage and food drop
									player.damage(damage);
								}
								else
								{
									if(player.isSprinting())
									{
										leech.ctr = leech.ctr - 20;
									}
									else
									{
										leech.ctr = leech.ctr - 5;
									}
									plugin.sendMessage(player, message5); // Under attack, food drop
								}

								int food = player.getFoodLevel();
								food = food - damage;
								if(food<0)
								{
									food = 0;
								}
								player.setFoodLevel(food);
							}
							break;
						}
					}
				}
				else
				{
					// reset leeches
					leech = new PlayerData(player.getLocation());
					this.leeches.put(uuid, leech);
				}
			}
		}

		/**
		 * Finds all players on the specified world and applies heat damage.
		 * @param world
		 */
		private void heat(World world)
		{
			
			List<Player> players = world.getPlayers();
			Iterator<Player> iterator = players.iterator();
			
			while(iterator.hasNext())
			{
				Player player = iterator.next();
				UUID uuid = player.getUniqueId();
				
				int thirst = 100;
				
				if(this.thirsts.containsKey(uuid))
				{
					thirst = this.thirsts.get(uuid);
				}
				
				GameMode gm = player.getGameMode();
				if(!gm.equals(GameMode.CREATIVE) && PermissionManager.hasHeatPermission(player) && !this.hasCoolingSuit(player))
				{
					Location location = player.getLocation();
					int damage = ConfigManager.getRulesHeatDamage(this.plugin, location);
					
					if(damage>0)
					{
						String message1 = ConfigManager.getRulesHeatMessage1(this.plugin, location);
						String message2 = ConfigManager.getRulesHeatMessage2(this.plugin, location);
						String message3 = ConfigManager.getRulesHeatMessage3(this.plugin, location);
						String message4 = ConfigManager.getRulesHeatMessage4(this.plugin, location);
						
						Block block = world.getBlockAt(location.getBlockX(), location.getBlockY()+1, location.getBlockZ());
						Block block2 = world.getBlockAt(location.getBlockX(), location.getBlockY(), location.getBlockZ());
						
						if(block2.getType().equals(Material.WATER))
						{
							if(thirst!=100)
							{
								thirst = 100;
								plugin.sendMessage(player, message1); // The water removes your thirst.
							}
						}
						else
						{
							// Dry damage happens during the day if exposed to the sky
							if(block.getLightFromSky() > 10 && block.getLightLevel() > 10)
							{
								thirst = thirst - 2;
	
								if(thirst == 50)
								{
									plugin.sendMessage(player, message2); // Your are starting to feel thirsty.
								}
	
								if(thirst == 25)
								{
									plugin.sendMessage(player, message3); // Your feel very thirsty.
								}
							}
							else
							{
								thirst = thirst + 10;
							}
	
	
							// Apply damage to player
							if(thirst<=0)
							{
								double oldDamage = player.getHealth();
								if(oldDamage - damage <= 0)
								{
									thirst = 100; // Player is going to die, reset thirst 
								}
	
								plugin.sendMessage(player, message4); // You are exhausted from the heat. Find water or shelter!
								player.damage(damage);
							}
						}

						if(thirst>100)
						{
							thirst = 100;
						}
						else if(thirst<0)
						{
							thirst = 0;
						}
	
						this.thirsts.put(uuid, new Integer(thirst));
					}
				}
				else
				{
					this.thirsts.put(uuid, 100); // reset thirst
				}
			}
		}
		
		/**
		 * Finds all players on the specified world and applies freeze damage.
		 * @param world
		 */
		private void freeze(World world)
		{
			boolean storm = world.hasStorm();

			List<Player> players = world.getPlayers();
			Iterator<Player> iterator = players.iterator();
			
			while(iterator.hasNext())
			{
				int realDamage = 0;
				Player player = iterator.next();
				GameMode gm = player.getGameMode();
				if(!gm.equals(GameMode.CREATIVE) && !gm.equals(GameMode.SPECTATOR) && PermissionManager.hasFreezePermission(player) && !this.hasWarmSuit(player))
				{
					Location location = player.getLocation();
					int damage = ConfigManager.getRulesFreezeDamage(this.plugin, location);
					int stormdamage = ConfigManager.getRulesFreezeStormdamage(this.plugin, location);
					String message = ConfigManager.getRulesFreezeMessage(this.plugin, location);
					
					Block block = world.getBlockAt(location.getBlockX(), location.getBlockY()+1, location.getBlockZ());
					
					// Storm damage happens even during the day if exposed to the sky
					if(storm && stormdamage > 0 && block.getLightFromSky() > 8 && block.getLightFromBlocks()<10)
					{
						realDamage = realDamage + stormdamage;
					}

					// Freeze damage if exposed to the sky
					if(damage > 0 && block.getLightLevel()<10 && block.getLightFromSky() > 8)
					{
						realDamage = realDamage + damage;
					}
					
					// Apply damage to player
					if(realDamage>0)
					{
						plugin.sendMessage(player, message);
						player.damage(realDamage);
					}
				}
			}
		}
		
		/**
		 * Finds all players on the specified world and applies lava burn damage.
		 * @param world
		 */
		private void lavaBurn(World world)
		{
			List<Player> players = world.getPlayers();
			Iterator<Player> iterator = players.iterator();
			
			while(iterator.hasNext())
			{
				Player player = iterator.next();
				
				GameMode gm = player.getGameMode();
				if(!gm.equals(GameMode.CREATIVE) && PermissionManager.hasHeatPermission(player))
				{
					Location location = player.getLocation();
					if(ConfigManager.isRulesLavaBurn(this.plugin, player.getLocation()))
					{
						int x = location.getBlockX();
						int y = location.getBlockY();
						int z = location.getBlockZ();
						
						int cnt = 0;
						
						for(int xx=x-3;xx<=x+3;xx++)
						{
							for(int zz=z-3;zz<=z+3;zz++)
							{
								for(int yy=y-3;yy<=y+3;yy++)
								{
									Block block = world.getBlockAt(xx, yy, zz);
									Material mat = block.getType();
									
									
									if(mat.equals(Material.LAVA))
									{
										cnt ++;
									}
								}
							}
						}

						if(cnt>0)
						{
							int curr = player.getFireTicks();
							int newTick = 15 + cnt/2 + curr;
							if(this.hasGlassSuit(player)) // randomly mini burn the player
							{
								if(random.nextInt(10)==1)
								{
									newTick = cnt/10 + curr + 15;
									player.setFireTicks(newTick);
								}
							}
							else
							{
								player.setFireTicks(newTick);
							}
						}
					}
				}
			}
		}

                private boolean hasEnvironmentSuit(Player player) {
                    return false;
                }
		/**
		 * Check if a player is wearing a full set of armour with displaynames starting with "Environment"
		 * @param player
		 * @return
		 */
		private boolean hasWarmSuit(Player player)
		{
			if(ConfigManager.getRulesEnvironmentSuit(this.plugin, player.getLocation()))
			{
				PlayerInventory inv = player.getInventory();
				ItemStack helmet = inv.getHelmet();
				ItemStack chestplate = inv.getChestplate();
				ItemStack leggings = inv.getLeggings();
				ItemStack boots = inv.getBoots();
				String helmetS = "";
				String chestplateS = "";
				String leggingsS = "";
				String bootsS = "";
				
				if(helmet!=null && helmet.hasItemMeta())
				{
					ItemMeta meta = helmet.getItemMeta();
					/*if(meta.hasDisplayName())
					{
						helmetS = meta.getDisplayName();
					}*/
                                        if(meta.hasLore()) {
                                                List<String> sub = meta.getLore();
                                                if(sub.size() > 0) {
                                                    helmetS = sub.get(0);
                                                }
                                        }
				}
				if(chestplate!=null && chestplate.hasItemMeta())
				{
					ItemMeta meta = chestplate.getItemMeta();
					/*if(meta.hasDisplayName())
					{
						chestplateS = meta.getDisplayName();
					}*/
                                        if(meta.hasLore()) {
                                                List<String> sub = meta.getLore();
                                                if(sub.size() > 0) {
                                                    chestplateS = sub.get(0);
                                                }
                                        }
				}
				if(leggings!=null && leggings.hasItemMeta())
				{
					ItemMeta meta = leggings.getItemMeta();
					/*if(meta.hasDisplayName())
					{
						leggingsS = meta.getDisplayName();
					}*/
                                        if(meta.hasLore()) {
                                                List<String> sub = meta.getLore();
                                                if(sub.size() > 0) {
                                                    leggingsS = sub.get(0);
                                                }
                                        }
				}
				if(boots!=null && boots.hasItemMeta())
				{
					ItemMeta meta = boots.getItemMeta();
					/*if(meta.hasDisplayName())
					{
						bootsS = meta.getDisplayName();
					}*/
                                        if(meta.hasLore()) {
                                                List<String> sub = meta.getLore();
                                                if(sub.size() > 0) {
                                                    bootsS = sub.get(0);
                                                }
                                        }
				}
				
				/*return helmetS.startsWith("Environment") &&
						chestplateS.startsWith("Environment") &&
						leggingsS.startsWith("Environment") &&
						bootsS.startsWith("Environment");*/
                                return helmetS.equals(RecipeManager.WarmSuitTag)
                                        && chestplateS.equals(RecipeManager.WarmSuitTag)
                                        && leggingsS.equals(RecipeManager.WarmSuitTag)
                                        && bootsS.equals(RecipeManager.WarmSuitTag);
			}
			else
			{
				return false; // pretend that the player does not have the suit by default
			}
		}
                
                private boolean hasRepellentSuit(Player player)
		{
			if(ConfigManager.getRulesEnvironmentSuit(this.plugin, player.getLocation()))
			{
				PlayerInventory inv = player.getInventory();
				ItemStack helmet = inv.getHelmet();
				ItemStack chestplate = inv.getChestplate();
				ItemStack leggings = inv.getLeggings();
				ItemStack boots = inv.getBoots();
				String helmetS = "";
				String chestplateS = "";
				String leggingsS = "";
				String bootsS = "";
				
				if(helmet!=null && helmet.hasItemMeta())
				{
					ItemMeta meta = helmet.getItemMeta();
					/*if(meta.hasDisplayName())
					{
						helmetS = meta.getDisplayName();
					}*/
                                        if(meta.hasLore()) {
                                                List<String> sub = meta.getLore();
                                                if(sub.size() > 0) {
                                                    helmetS = sub.get(0);
                                                }
                                        }
				}
				if(chestplate!=null && chestplate.hasItemMeta())
				{
					ItemMeta meta = chestplate.getItemMeta();
					/*if(meta.hasDisplayName())
					{
						chestplateS = meta.getDisplayName();
					}*/
                                        if(meta.hasLore()) {
                                                List<String> sub = meta.getLore();
                                                if(sub.size() > 0) {
                                                    chestplateS = sub.get(0);
                                                }
                                        }
				}
				if(leggings!=null && leggings.hasItemMeta())
				{
					ItemMeta meta = leggings.getItemMeta();
					/*if(meta.hasDisplayName())
					{
						leggingsS = meta.getDisplayName();
					}*/
                                        if(meta.hasLore()) {
                                                List<String> sub = meta.getLore();
                                                if(sub.size() > 0) {
                                                    leggingsS = sub.get(0);
                                                }
                                        }
				}
				if(boots!=null && boots.hasItemMeta())
				{
					ItemMeta meta = boots.getItemMeta();
					/*if(meta.hasDisplayName())
					{
						bootsS = meta.getDisplayName();
					}*/
                                        if(meta.hasLore()) {
                                                List<String> sub = meta.getLore();
                                                if(sub.size() > 0) {
                                                    bootsS = sub.get(0);
                                                }
                                        }
				}
				
				/*return helmetS.startsWith("Environment") &&
						chestplateS.startsWith("Environment") &&
						leggingsS.startsWith("Environment") &&
						bootsS.startsWith("Environment");*/
                                return helmetS.equals(RecipeManager.RepellentSuitTag)
                                        && chestplateS.equals(RecipeManager.RepellentSuitTag)
                                        && leggingsS.equals(RecipeManager.RepellentSuitTag)
                                        && bootsS.equals(RecipeManager.RepellentSuitTag);
			}
			else
			{
				return false; // pretend that the player does not have the suit by default
			}
		}
                private boolean hasCoolingSuit(Player player)
		{
			if(ConfigManager.getRulesEnvironmentSuit(this.plugin, player.getLocation()))
			{
				PlayerInventory inv = player.getInventory();
				ItemStack helmet = inv.getHelmet();
				ItemStack chestplate = inv.getChestplate();
				ItemStack leggings = inv.getLeggings();
				ItemStack boots = inv.getBoots();
				String helmetS = "";
				String chestplateS = "";
				String leggingsS = "";
				String bootsS = "";
				
				if(helmet!=null && helmet.hasItemMeta())
				{
					ItemMeta meta = helmet.getItemMeta();
					/*if(meta.hasDisplayName())
					{
						helmetS = meta.getDisplayName();
					}*/
                                        if(meta.hasLore()) {
                                                List<String> sub = meta.getLore();
                                                if(sub.size() > 0) {
                                                    helmetS = sub.get(0);
                                                }
                                        }
				}
				if(chestplate!=null && chestplate.hasItemMeta())
				{
					ItemMeta meta = chestplate.getItemMeta();
					/*if(meta.hasDisplayName())
					{
						chestplateS = meta.getDisplayName();
					}*/
                                        if(meta.hasLore()) {
                                                List<String> sub = meta.getLore();
                                                if(sub.size() > 0) {
                                                    chestplateS = sub.get(0);
                                                }
                                        }
				}
				if(leggings!=null && leggings.hasItemMeta())
				{
					ItemMeta meta = leggings.getItemMeta();
					/*if(meta.hasDisplayName())
					{
						leggingsS = meta.getDisplayName();
					}*/
                                        if(meta.hasLore()) {
                                                List<String> sub = meta.getLore();
                                                if(sub.size() > 0) {
                                                    leggingsS = sub.get(0);
                                                }
                                        }
				}
				if(boots!=null && boots.hasItemMeta())
				{
					ItemMeta meta = boots.getItemMeta();
					/*if(meta.hasDisplayName())
					{
						bootsS = meta.getDisplayName();
					}*/
                                        if(meta.hasLore()) {
                                                List<String> sub = meta.getLore();
                                                if(sub.size() > 0) {
                                                    bootsS = sub.get(0);
                                                }
                                        }
				}
				
				/*return helmetS.startsWith("Environment") &&
						chestplateS.startsWith("Environment") &&
						leggingsS.startsWith("Environment") &&
						bootsS.startsWith("Environment");*/
                                return helmetS.equals(RecipeManager.CoolingSuitTag)
                                        && chestplateS.equals(RecipeManager.CoolingSuitTag)
                                        && leggingsS.equals(RecipeManager.CoolingSuitTag)
                                        && bootsS.equals(RecipeManager.CoolingSuitTag);
			}
			else
			{
				return false; // pretend that the player does not have the suit by default
			}
		}
                
                private boolean hasGlassSuit(Player player)
		{
			if(ConfigManager.getRulesEnvironmentSuit(this.plugin, player.getLocation()))
			{
				PlayerInventory inv = player.getInventory();
				ItemStack helmet = inv.getHelmet();
				ItemStack chestplate = inv.getChestplate();
				ItemStack leggings = inv.getLeggings();
				ItemStack boots = inv.getBoots();
				String helmetS = "";
				String chestplateS = "";
				String leggingsS = "";
				String bootsS = "";
				
				if(helmet!=null && helmet.hasItemMeta())
				{
					ItemMeta meta = helmet.getItemMeta();
					/*if(meta.hasDisplayName())
					{
						helmetS = meta.getDisplayName();
					}*/
                                        if(meta.hasLore()) {
                                                List<String> sub = meta.getLore();
                                                if(sub.size() > 0) {
                                                    helmetS = sub.get(0);
                                                }
                                        }
				}
				if(chestplate!=null && chestplate.hasItemMeta())
				{
					ItemMeta meta = chestplate.getItemMeta();
					/*if(meta.hasDisplayName())
					{
						chestplateS = meta.getDisplayName();
					}*/
                                        if(meta.hasLore()) {
                                                List<String> sub = meta.getLore();
                                                if(sub.size() > 0) {
                                                    chestplateS = sub.get(0);
                                                }
                                        }
				}
				if(leggings!=null && leggings.hasItemMeta())
				{
					ItemMeta meta = leggings.getItemMeta();
					/*if(meta.hasDisplayName())
					{
						leggingsS = meta.getDisplayName();
					}*/
                                        if(meta.hasLore()) {
                                                List<String> sub = meta.getLore();
                                                if(sub.size() > 0) {
                                                    leggingsS = sub.get(0);
                                                }
                                        }
				}
				if(boots!=null && boots.hasItemMeta())
				{
					ItemMeta meta = boots.getItemMeta();
					/*if(meta.hasDisplayName())
					{
						bootsS = meta.getDisplayName();
					}*/
                                        if(meta.hasLore()) {
                                                List<String> sub = meta.getLore();
                                                if(sub.size() > 0) {
                                                    bootsS = sub.get(0);
                                                }
                                        }
				}
				
				/*return helmetS.startsWith("Environment") &&
						chestplateS.startsWith("Environment") &&
						leggingsS.startsWith("Environment") &&
						bootsS.startsWith("Environment");*/
                                return helmetS.equals(RecipeManager.GlassSuitTag)
                                        && chestplateS.equals(RecipeManager.GlassSuitTag)
                                        && leggingsS.equals(RecipeManager.GlassSuitTag)
                                        && bootsS.equals(RecipeManager.GlassSuitTag);
			}
			else
			{
				return false; // pretend that the player does not have the suit by default
			}
		}
	}
}
