package biz.orgin.minecraft.hothgenerator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.Hashtable;
import java.util.Random;
import java.util.Vector;
import java.util.logging.Level;
import org.bukkit.Bukkit;

import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class LootGenerator implements Serializable
{
	private static final long serialVersionUID = 4855454685158085649L;
	private String name;
	private Loot[] loot;
	
	private static Random random = new Random();
	
	private static Hashtable<String, LootGenerator> generators = new Hashtable<String, LootGenerator>();
	
	private static LootGenerator defLoot = new LootGenerator(new Loot[]
		{
			// Material, data, min, max %
			new Loot(Material.BIRCH_SAPLING,   (byte)0x00, 1, 4, 10), //Birch sapling
			new Loot(Material.BOOK,            (byte)0x00, 1, 2,  5), //Books
			new Loot(Material.BREAD,           (byte)0x00, 1, 3, 75), //Bread
			new Loot(Material.BUCKET,          (byte)0x00, 1, 1, 30), //Bucket
			new Loot(Material.COAL,            (byte)0x00, 1, 1, 20), //Coal
			new Loot(Material.COCOA_BEANS,     (byte)0x00, 1, 2,  5), //Cocoa Beans
			new Loot(Material.COMPASS,         (byte)0x00, 1, 1,  5), //Compasses
			new Loot(Material.DIAMOND,         (byte)0x00, 1, 1,  5), //Diamond
			new Loot(Material.ENDER_PEARL,     (byte)0x00, 1, 1,  5), //Ender Pearls
			new Loot(Material.GOLD_INGOT,      (byte)0x00, 1, 1,  5), //Gold Ingot
			new Loot(Material.GOLDEN_APPLE,    (byte)0x00, 1, 1,  5), //Golden Apple (Normal)
			new Loot(Material.GUNPOWDER,       (byte)0x00, 1, 4,  5), //Gunpowder
			new Loot(Material.IRON_BOOTS,      (byte)0x00, 1, 1, 25), //Iron Boots
			new Loot(Material.IRON_CHESTPLATE, (byte)0x00, 1, 1, 25), //Iron Chestplates
			new Loot(Material.IRON_HELMET,     (byte)0x00, 1, 1, 25), //Iron Helmets
			new Loot(Material.IRON_INGOT,      (byte)0x00, 1, 2, 10), //Iron Ingot
			new Loot(Material.IRON_PICKAXE,    (byte)0x00, 1, 1, 25), //Iron Pickaxe
			new Loot(Material.IRON_SWORD,      (byte)0x00, 1, 1, 25), //Iron Swords
			new Loot(Material.JUNGLE_SAPLING,  (byte)0x00, 1, 4,  5), //Jungle sapling
			new Loot(Material.LAPIS_LAZULI,    (byte)0x00, 1, 8, 15), //Lapis Lazuli
			new Loot(Material.MELON_SEEDS,     (byte)0x00, 1, 2,  5), //Melon Seeds
			new Loot(Material.MUSIC_DISC_11,   (byte)0x00, 1, 1,  1), //Melon Seeds
			new Loot(Material.MUSIC_DISC_BLOCKS,(byte)0x00, 1, 1,  1), //Music Discs
			new Loot(Material.MUSIC_DISC_CHIRP,(byte)0x00, 1, 1,  1), //Music Discs
			new Loot(Material.MUSIC_DISC_FAR,        (byte)0x00, 1, 1,  1), //Music Discs
			new Loot(Material.MUSIC_DISC_MALL,        (byte)0x00, 1, 1,  1), //Music Discs
			new Loot(Material.MUSIC_DISC_MELLOHI,        (byte)0x00, 1, 1,  1), //Music Discs
			new Loot(Material.MUSIC_DISC_STAL,        (byte)0x00, 1, 1,  1), //Music Discs
			new Loot(Material.MUSIC_DISC_STRAD,        (byte)0x00, 1, 1,  1), //Music Discs
			new Loot(Material.MUSIC_DISC_WAIT,       (byte)0x00, 1, 1,  1), //Music Discs
			new Loot(Material.MUSIC_DISC_WARD,       (byte)0x00, 1, 1,  1), //Music Discs
			new Loot(Material.OAK_SAPLING,         (byte)0x00, 1, 4,  5), //Oak sapling
			new Loot(Material.OBSIDIAN,        (byte)0x00, 1, 2,  4), //Obsidian
			new Loot(Material.PAPER,           (byte)0x00, 1, 3, 10), //Paper
			new Loot(Material.SPRUCE_SAPLING,         (byte)0x00, 1, 4,  5), //Pine sapling
			new Loot(Material.PUMPKIN_SEEDS,   (byte)0x00, 1, 2,  5), //Pumpkin Seeds
			new Loot(Material.RAIL,           (byte)0x00, 1,10, 25), //Rails
			new Loot(Material.APPLE,           (byte)0x00, 1, 5, 50), //Red Apples
			new Loot(Material.REDSTONE,        (byte)0x00, 1, 4, 15), //Redstone
			new Loot(Material.WHEAT_SEEDS,           (byte)0x00, 1, 2, 25), //Seeds
			new Loot(Material.SADDLE,          (byte)0x00, 1, 1,  5), //Saddle
			new Loot(Material.STRING,          (byte)0x00, 1, 3, 50), //String
			new Loot(Material.SUGAR_CANE,      (byte)0x00, 1, 3,  5), //Sugar cane
			new Loot(Material.LILY_PAD,      (byte)0x00, 1, 5,  5), //Water lily
			new Loot(Material.WHEAT,           (byte)0x00, 1, 4,  5), //Wheat
			new Loot(Material.POTATO,     (byte)0x00, 1, 2,  5), //Potato
			new Loot(Material.CARROT,     (byte)0x00, 1, 2,  5), //Carrot
			new Loot(Material.ACACIA_SAPLING,         (byte)0x00, 1, 4,  5), //Acacia sapling
			new Loot(Material.DARK_OAK_SAPLING,         (byte)0x00, 4, 8,  5), //Dark oak sapling
			new Loot(Material.PUMPKIN_PIE,     (byte)0x00, 1, 5, 50), //Pumpkin pie
			new Loot(Material.POPPY,        (byte)0x00, 1,25,  5), //Red rose
			new Loot(Material.BLUE_ORCHID,        (byte)0x00, 1,25,  5), //Blue orchid
			new Loot(Material.ALLIUM,        (byte)0x00, 1,25,  5), //Allium
			new Loot(Material.AZURE_BLUET,        (byte)0x00, 1,25,  5), //Azure Bluet
			new Loot(Material.RED_TULIP,        (byte)0x00, 1,25,  5), //Red tulip
			new Loot(Material.ORANGE_TULIP,        (byte)0x00, 1,25,  5), //Orange tulip
			new Loot(Material.WHITE_TULIP,        (byte)0x00, 1,25,  5), //White tulip
			new Loot(Material.PINK_TULIP,        (byte)0x00, 1,25,  5), //Pink tulip
			new Loot(Material.OXEYE_DAISY,        (byte)0x00, 1,25,  5), //Oxeye daisy
			new Loot(Material.SUNFLOWER,    (byte)0x00, 1,25,  5), //Sun flower
			new Loot(Material.LILAC,    (byte)0x00, 1,25,  5), //Lilac
			new Loot(Material.TALL_GRASS,    (byte)0x00, 1,25,  5), //Double tallgrass
			new Loot(Material.LARGE_FERN,    (byte)0x00, 1,25,  5), //Large fern
			new Loot(Material.ROSE_BUSH,    (byte)0x00, 1,25,  5), //Rose bush
			new Loot(Material.PEONY,    (byte)0x00, 1,25,  5), //Peony
			
			new Loot(Material.PIG_SPAWN_EGG,     (byte) 0, 1, 4,  3),  //Pig egg
			new Loot(Material.SHEEP_SPAWN_EGG,     (byte) 0, 1, 4,  3),  //Sheep
			new Loot(Material.COW_SPAWN_EGG,     (byte) 0, 1, 4,  3),  //Cow
			new Loot(Material.CHICKEN_SPAWN_EGG,     (byte) 0, 1, 4,  3),  //Chicken
			new Loot(Material.WOLF_SPAWN_EGG,     (byte) 0, 1, 4,  3),  //Wolf
			new Loot(Material.OCELOT_SPAWN_EGG,     (byte) 0, 1, 4,  3),  //Ocelot
			new Loot(Material.HORSE_SPAWN_EGG,     (byte)0, 1, 4,  3)   //Horse
		}
	);
	
	private LootGenerator()
	{
		
	}
	public void save(HothGeneratorPlugin plugin) throws IOException
	{
		File dataFolder = plugin.getDataFolder();
		String path = dataFolder.getAbsolutePath() + "/custom/" + this.name;
		
		File file = new File(path);
		
		if(file.exists())
		{
			file.delete();
		}
		
		file.createNewFile();
		
		FileWriter writer = new FileWriter(file);
		
		writer.write("# Generated loot list\n");
		writer.write("#\n");
		writer.write("# Name,MaterialID,Data,Min,Max,Probability\n");
		
		for(int i=0;i<this.loot.length;i++)
		{
			Loot loot = this.loot[i];
			writer.write(String.format("%-16s", loot.material.name()));
			writer.write(",");
			writer.write(String.format("%4d", MaterialManager.toID(loot.material)));
			writer.write(",");
			writer.write(String.format("%3d", loot.data));
			writer.write(",");
			writer.write(String.format("%3d", loot.min));
			writer.write(",");
			writer.write(String.format("%3d", loot.max));
			writer.write(",");
			writer.write(String.format("%3d", loot.chance));
			writer.write("\n");
		}
		writer.flush();
		
		writer.close();
	}
	
	
	private LootGenerator(Loot[] loot)
	{
		this.loot = loot;
		this.name = "default.ll";
	}
	
	public static void load(HothGeneratorPlugin plugin)
	{
		LootGenerator.generators = new Hashtable<String, LootGenerator>();
		
		File dataFolder = plugin.getDataFolder();
		String path = dataFolder.getAbsolutePath() + "/custom";
		File customFolder = new File(path);
		if(!customFolder.exists())
		{
			customFolder.mkdir();
		}
		else
		{
			File[] files = customFolder.listFiles();
			if(files!=null)
			{
				for(int i=0;i<files.length;i++)
				{
					File file = files[i];
					if(file.isFile() && file.getName().endsWith(".ll")  && !file.getName().equals("example.ll"))
					{
						try
						{
							LootGenerator generator = LootGenerator.loadLootGenerator(file);
							LootGenerator.generators.put(file.getName(), generator);
							plugin.getLogger().info("Loaded custom loot list: " + file.getName());

						}
						catch(Exception e)
						{
							plugin.getLogger().info("ERROR: Failed to load " + file.getName() + " " + e.getMessage());
						}
					}
				}
			}
		}
	}

	/**
	 * Get a named custom loot generator.
	 * Returns null if the generator can't be found.
	 * @param name Name of the loot list
	 * @return The specified LootGenerator
	 */
	public static LootGenerator getLootGenerator(String name)
	{
		return LootGenerator.generators.get(name);
		
	}

	private static LootGenerator loadLootGenerator(File lootFile) throws IOException
	{
		LootGenerator generator = new LootGenerator();
		generator.name = lootFile.getName();
		
		
		BufferedReader reader = new BufferedReader(new FileReader(lootFile));
		
		String line;
		
		Vector<Loot> lootVector = new Vector<Loot>();
		
		while( (line=reader.readLine())!=null)
		{
			String row = line.trim();
			if(row.length()>0 &&  row.charAt(0)!=';' && row.charAt(0)!='#')
			{
				// Name, Material, data, min, max, %
				String[] data = row.split(",");
				if(data.length!=6)
				{
					reader.close();
					throw new IOException("Error reading from " + generator.name + " wrong number of parameters: " + row);
				}
				
				int materialID,dataVal,min,max,probability;
				
				try
				{
					materialID = Integer.parseInt(data[1].trim());
					dataVal = Integer.parseInt(data[2].trim());
					min = Integer.parseInt(data[3].trim());
					max = Integer.parseInt(data[4].trim());
					probability = Integer.parseInt(data[5].trim());
				}
				catch(Exception e)
				{
					reader.close();
					throw new IOException("Error reading from " + generator.name + " invalid parameters: " + row);
				}
				
				Material material = MaterialManager.toMaterial(materialID);
				if(material==null)
				{
					reader.close();
					throw new IOException("Error reading from " + generator.name + " Unknow materialID: " + row);
				}
				
				if(dataVal<0)
				{
					reader.close();
					throw new IOException("Error reading from " + generator.name + " data value below 0: " + row);
				}
				
				if(min<0)
				{
					reader.close();
					throw new IOException("Error reading from " + generator.name + " min value below 0: " + row);
				}
				
				if(max<min)
				{
					reader.close();
					throw new IOException("Error reading from " + generator.name + " max value below min: " + row);
				}
				
				if(probability>100 || probability<1)
				{
					reader.close();
					throw new IOException("Error reading from " + generator.name + " invalid probability: " + row);
				}
				
				Loot newLoot = new Loot(material, (byte)dataVal, min, max, probability);
				lootVector.add(newLoot);
			}
		}
		
		reader.close();
		
		generator.loot = lootVector.toArray(new Loot[lootVector.size()]);
		return generator;
	}
	
	

	/**
	 * Get the default loot generator that is used for internally generated loot chests.
	 * The default loot list can be replaced by adding a custom loot list called "default.ll"
	 * @return The default loot generator.
	 */
	public static LootGenerator getLootGenerator()
	{
		LootGenerator generator = LootGenerator.generators.get("default.ll");
		if(generator==null)
		{
			generator = LootGenerator.defLoot;
		}
		return generator;
	}
	
	public Inventory getLootInventory(Inventory inv, int min, int max)
	{
		Random random = LootGenerator.random;
		
		int items = min + random.nextInt(max-min);
                //Bukkit.getLogger().log(Level.INFO, Integer.toString(items));
		//LegacyMapper lm = LegacyMapper.getInstance();
                
		for(int i=0;i<items;i++)
		{
			boolean found = false;
			while(!found)
			{
				Loot randLoot = this.loot[random.nextInt(this.loot.length)];
				
				int prob = random.nextInt(100);
				if(randLoot.chance >= prob)
				{
					int cnt = randLoot.min, rand = randLoot.max - randLoot.min;
					if(rand>0)
					{
						cnt = cnt + random.nextInt(rand);
					}
					
                                        /*ItemType it = lm.getItemFromLegacy(MaterialManager.toID(randLoot.material), randLoot.data);
                                        if(it == null) {
                                            //Bukkit.getLogger().log(Level.SEVERE, Integer.toString(MaterialManager.toID(randLoot.material)) + "#" + Integer.toString(randLoot.data));
                                            //Bukkit.getLogger().log(Level.SEVERE, "ItemType null");
                                            continue;
                                        }
                                        Material m = BukkitAdapter.adapt(it);
                                        if(m == null) {
                                            //Bukkit.getLogger().log(Level.SEVERE, "Material null");
                                            continue;
                                        }
                                        ItemStack stack = new ItemStack(m);*/
                                        ItemStack stack = new ItemStack(randLoot.material);
                                        //Bukkit.getLogger().log(Level.FINE, stack.toString());
					//MaterialData mdata = new MaterialData(randLoot.material);
					//DataManager.setData(mdata, randLoot.data);
					//ItemStack stack = mdata.toItemStack(cnt);
					
					// @ToDo: Spawn eggs don't store the type via the data value any more
					// currently there's no bukkit/spigot method for handling this
					// therefore spawn eggs are no longer added to loot chests
					
					inv.addItem(stack);
                                        //Bukkit.getLogger().log(Level.INFO, stack.toString());
					found = true;
				}
			}
		}
		
		return inv;
	}

	public String getName() {
		return name;
	}
}
