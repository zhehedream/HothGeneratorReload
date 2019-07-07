package biz.orgin.minecraft.hothgenerator;

import java.util.HashMap;
import java.util.Random;
import java.util.Vector;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

public class BuckitUseManager implements Listener
{
	public static void main(String[] args)
	{
		int len = materialArray.length;
		
		int coalTot = len/10;
		int redStoneTot = len/5;
		
		System.out.println("For " + coalTot + " coal and " + redStoneTot + " redstone you will get:");
		
		int sum = 0;
		
		for(int i=0;i<materials.length;i++)
		{
			Material mat = materials[i];
			int amount = probability[i];
			
			sum = sum + amount;
			
			System.out.println(mat + " - " + amount);
		}
		
		System.out.println(Material.COBBLESTONE + " - " + (len-sum)*0.75);
		System.out.println(Material.STONE + " - " + (len-sum)*0.25);
	}
	
	private static Random random = new Random(System.currentTimeMillis());
	
	private static Material[] materials = new Material[]
	{
			Material.COAL_ORE,     // 128
			Material.IRON_ORE,     // 128
			Material.GOLD_ORE,     // 26
			Material.REDSTONE_ORE, // 16
			Material.DIAMOND_ORE,  // 16
			Material.LAPIS_ORE,    // 26
			Material.EMERALD_ORE,  // 128
			Material.OBSIDIAN,     // 64
			Material.GLOWSTONE,    // 128
			Material.NETHER_QUARTZ_ORE    // 32
	};

	private static int[] maxHeights = new int[]
	{
		128, // Coal
		128, // Iron
		26,  // Gold
		16,  // Redstone
		16,  // diamond
		26,  // lapis
		128, // emerald
		64,  // obsidian
		128, // Glowstone
		32   // quartz
	};
	
	private static int[] probability = new int[]
	{
		16,
		16,
		8,
		16,
		6,
		6,
		4,
		2,
		4,
		4
	};
	
	private static int[] experienceArray = new int[]
	{
		2, // Coal, 0-2
		0, // Iron, 0
		0, // Gold, 0
		3, // Redstone, 1-5
		7, // Diamond, 3-7
		4, // Lapis, 2-5
		5, // Emerald, 3-7
		0, // Obsidian
		0, // Glowstone
		3  // Quartz
	};

			private static int[] materialArray = new int[320];

	private static void generateMaterialArray()
	{
		// Initiate array with default values
		for(int i=0;i<materialArray.length;i++)
		{
			materialArray[i] = -1;
		}
		
		// Construct the probability array
		int pos = 0;
		for(int i=0;i<probability.length;i++)
		{
			int prob = probability[i];
			for(int j=0;j<prob;j++)
			{
				if(pos<materialArray.length)
				{
					materialArray[pos] = i;
				}
				pos++;
			}
		}
	}
	
	HothGeneratorPlugin plugin;
	
	public BuckitUseManager(HothGeneratorPlugin plugin)
	{
		this.plugin = plugin;
		BuckitUseManager.generateMaterialArray();
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerBucketFill(PlayerBucketFillEvent event)
	{
		if(!event.isCancelled())
		{
			Block block = event.getBlockClicked();
			World world = block.getWorld();
			WorldType worldType = this.plugin.getWorldType(world);
			Material bucket = event.getBucket();

			// Repulsor Field Generator handling
			
			if(ConfigManager.isRulesRFGEnable(this.plugin, block.getLocation())
					&& bucket.equals(Material.BUCKET)
					&& (block.getType().equals(Material.LAVA))) // Did player try to get some lava?
			{


				if(this.plugin.isHothWorld(world) && worldType == WorldType.MUSTAFAR) // And if in mustafar
				{
					int coalProb = ConfigManager.getRulesRFGCoal(this.plugin, block.getLocation());
					int redstoneProb = ConfigManager.getRulesRFGCoal(this.plugin, block.getLocation());
					String rfgName = ConfigManager.getRulesRFGName(this.plugin, block.getLocation());
					
					System.out.println("rfgName = " + rfgName);

					Player player = event.getPlayer();

					if(player.getGameMode().equals(GameMode.SURVIVAL)) // Only bother if player is in survival
					{
						PlayerInventory inv = player.getInventory();

						// Check player inventory if there is a regular piston named "Repulsor Field Generator"
						ItemStack items[] = inv.getContents();
						boolean found = false;
						for(int i=0;i<items.length;i++)
						{
							ItemStack item = items[i];
							if(item!=null)
							{
								if(item.getType().equals(Material.PISTON))
								{
									if(item.hasItemMeta())
									{
										ItemMeta meta = items[i].getItemMeta();
										if(meta.hasDisplayName())
										{
											String displayName = meta.getDisplayName();
											if(displayName.equals(rfgName)) // "Repulsor Field Generator"
											{
												found = true;
												break;
											}
										}
									}
								}
							}

						}

						if(found)
						{
							if(inv.contains(Material.REDSTONE) || coalProb == 0)
							{
								if(inv.contains(Material.COAL) || redstoneProb == 0)
								{
									// Create thread to consume the collected lava
									Location loc = block.getLocation();
									int y = loc.getBlockY();
									LavaProcessorThread th = new LavaProcessorThread(player, y, coalProb, redstoneProb);
									Bukkit.getScheduler().scheduleSyncDelayedTask(this.plugin, th);
								}
								else
								{
									// "You need Coal to power the Repulsor Field Generator"
									this.plugin.sendMessage(player,ConfigManager.getRulesRFGMessage3(this.plugin, block.getLocation()));
								}
							}
							else
							{
								// "You need Redstone to power the Repulsor Field Generator"
								this.plugin.sendMessage(player,ConfigManager.getRulesRFGMessage2(this.plugin, block.getLocation()));
							}
						}
						else
						{
							// "You need a Repulsor Field Generator piston to extract ore from lava"
							this.plugin.sendMessage(player,ConfigManager.getRulesRFGMessage1(this.plugin, block.getLocation()));
						}
					}

				}
			}
		}
	}
	
	private class LavaProcessorThread implements Runnable
	{
		private Player player;
		private int y;
		private int coalProb;
		private int redstoneProb;
		
		private LavaProcessorThread(Player player, int y, int coalProb, int redstoneProb)
		{
			this.player = player;
			this.y = y;
			this.coalProb = coalProb;
			this.redstoneProb = redstoneProb;
		}
		
		@Override
		public void run()
		{
			PlayerInventory inv = this.player.getInventory();

			// Default material is stone
			Material material = Material.STONE;
			
			// Get random material
			int rand = random.nextInt(materialArray.length);
			int index = materialArray[rand];
			
			int experience = 0;
			
			if(index>=0)
			{
				int maxheight = maxHeights[index];
				
				if(this.y < maxheight) // Height must be correct
				{
					material = materials[index];
					experience = experienceArray[index];
				}
			}
			else
			{
				if(random.nextInt(4)>0) // Default to 75% cobblestone
				{
					material = Material.COBBLESTONE;
				}
			}
			
			// Add a material to player inventory
			ItemStack stack = new ItemStack(material, 1);
			HashMap<Integer,ItemStack> result = inv.addItem(stack);
			if(result.isEmpty())
			{
				int slot = inv.getHeldItemSlot();
				inv.setItem(slot,  new ItemStack(Material.BUCKET, 1));
				
				// remove redstone and coal
				ItemStack oldStack[] = inv.getContents();
				Vector<ItemStack> newStack = new Vector<ItemStack>();
				
				boolean coalDone = false;
				boolean redStoneDone = false;
				
				for(int i=0;i<oldStack.length;i++)
				{
					ItemStack stk = oldStack[i];
					
					Material type = Material.AIR;
					
					if(stk!=null)
					{
						type = stk.getType();
					}
					
					if(!coalDone && type.equals(Material.COAL))
					{ // Remove a coal from inventory
						if(this.coalProb!=0 && random.nextInt(this.coalProb) == 1)
						{
							int amt = stk.getAmount();
							if(amt>1)
							{
								stk.setAmount(amt-1);
								newStack.add(stk);
							}
						}
						else
						{
							newStack.add(stk);
						}
						coalDone = true;
					}
					else if(!redStoneDone && type.equals(Material.REDSTONE))
					{ // Remove a redstone from inventory
						if(this.redstoneProb!=0 && random.nextInt(this.redstoneProb) == 1)
						{
							int amt = stk.getAmount();
							if(amt>1)
							{
								stk.setAmount(amt-1);
								newStack.add(stk);
							}
						}
						else
						{
							newStack.add(stk);
						}
						redStoneDone = true;
					}
					else
					{
						newStack.add(stk);
					}
				}
				
				// Update inventory
				inv.setContents(newStack.toArray(new ItemStack[newStack.size()]));
				
				// Spawn some experience
				World world = player.getWorld();
				world.spawn(player.getLocation(), ExperienceOrb.class).setExperience(experience);
			}
			else // If inventory is full then send message and do nothing.
			{
				// "Inventory full"
				plugin.sendMessage(player,ConfigManager.getRulesRFGMessage4(plugin, player.getLocation()));
			}
		}
		
	}

}
