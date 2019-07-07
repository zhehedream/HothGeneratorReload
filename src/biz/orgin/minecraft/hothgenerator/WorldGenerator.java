package biz.orgin.minecraft.hothgenerator;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.world.block.BlockState;
import com.sk89q.worldedit.world.registry.LegacyMapper;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.block.data.BlockData;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.ChunkGenerator;

public class WorldGenerator extends ChunkGenerator
{
	private static HothGeneratorPlugin plugin; 
	
	private NoiseGenerator noiseGenerator;
	
	private WorldType worldType; // Don't access this directly! Use getWorldType
	private String worldName;
	
	public static void setPlugin(HothGeneratorPlugin plugin)
	{
		WorldGenerator.plugin = plugin;
	}
	
	public static HothGeneratorPlugin getPlugin()
	{
		return WorldGenerator.plugin;
	}
	
	/**
	 * Create a world with an optional hardcoded world type
	 * @param worldName Name of the world
	 * @param worldType Type of the world. Set to null for a non hardcoded worldType.
	 */
	public WorldGenerator(String worldName, WorldType worldType)
	{
		this.worldType = null;
		this.worldName = worldName;
		this.noiseGenerator = null;
		if(worldType!=null)
		{
			this.forceWorldType(worldName, worldType);
		}
	}
	
	/**
	 * Create a world without a hardcoded world type
	 * @param worldName Name of the world
	 */
	public WorldGenerator(String worldName)
	{
		this(worldName, null);
	}
	
	public void forceWorldType(World world, WorldType worldType)
	{
		this.forceWorldType(world.getName(), worldType);
	}

	public void forceWorldType(String worldName, WorldType worldType)
	{
		this.worldType = worldType;
		
		if(!plugin.isHothWorld(worldName)) // Force this world into a hothgenerator world with given type
		{
			ConfigManager.addWorld(WorldGenerator.plugin, worldName, worldType);
		}
		
		if(plugin.getWorldType(worldName) != worldType) // Force this world into the given type
		{
			ConfigManager.setWorldType(WorldGenerator.plugin, worldName, worldType);
		}
	}
	
	private WorldType getWorldType()
	{
		if(this.worldType==null)
		{
			return WorldGenerator.plugin.getWorldType(this.worldName);
		}
		else
		{
			return this.worldType;
		}
	}
	
        @Override
        public ChunkData generateChunkData(World world, Random random, int chunkx, int chunkz, BiomeGrid biomes) {
                WorldType t_worldType = this.getWorldType();
		
		switch(t_worldType)
		{
		case TATOOINE:
			return this.generateExtBlockSectionsTatooine(world, random, chunkx, chunkz, biomes);
		case DAGOBAH:
			return this.generateExtBlockSectionsDagobah(world, random, chunkx, chunkz, biomes);
		case MUSTAFAR:
			return this.generateExtBlockSectionsMustafar(world, random, chunkx, chunkz, biomes);
		case HOTH:
		default: // Default to hoth chunks
			return this.generateExtBlockSectionsHoth(world, random, chunkx, chunkz, biomes);
		}

        }
	/*@Override
	public short[][] generateExtBlockSections(World world, Random random, int chunkx, int chunkz, BiomeGrid biomes)
	{
		WorldType worldType = this.getWorldType();
		
		switch(worldType)
		{
		case TATOOINE:
			return this.generateExtBlockSectionsTatooine(world, random, chunkx, chunkz, biomes);
		case DAGOBAH:
			return this.generateExtBlockSectionsDagobah(world, random, chunkx, chunkz, biomes);
		case MUSTAFAR:
			return this.generateExtBlockSectionsMustafar(world, random, chunkx, chunkz, biomes);
		case HOTH:
		default: // Default to hoth chunks
			return this.generateExtBlockSectionsHoth(world, random, chunkx, chunkz, biomes);
		}
	}*/
		
	public ChunkData generateExtBlockSectionsHoth(World world, Random random, int chunkx, int chunkz, BiomeGrid biomes)
	{
            //plugin.getLogger().info("# " + Integer.toString(chunkx) + "," + Integer.toString(chunkz));
		if(this.noiseGenerator==null)
		{
			this.noiseGenerator = new NoiseGenerator(world);
		}
                boolean smooth_snow = false;
                if(ConfigManager.isSmoothSnow(plugin)) smooth_snow = true;
		LegacyMapper lm = LegacyMapper.getInstance();
                
		int surfaceOffset = ConfigManager.getWorldSurfaceoffset(WorldGenerator.plugin, world);
		
		Random localRand = new Random(chunkx*chunkz);
		Position[][] snowcover = new Position[16][16];
		
		int vsegs = WorldGenerator.plugin.getHeight() / 16;
		ChunkData chunk = createChunkData(world);
		
		for(int z=0;z<16;z++)
		{
			for(int x=0;x<16;x++)
			{
				int rx = chunkx*16 + x;
				int rz = chunkz*16 + z;
				
				Biome biome = biomes.getBiome(x, z);
				float factor = 1.0f;
                            switch (biome) {
                                case DESERT_HILLS:
                                    factor = 2.0f;
                                    break;
                                case MOUNTAINS:
                                    factor = 8.0f;
                                    break;
                                case WOODED_HILLS:
                                    factor = 3.0f;
                                    break;
                                case SNOWY_MOUNTAINS:
                                    factor = 5.0f;
                                    break;
                                case SNOWY_TUNDRA:
                                    factor = 0.5f;
                                    break;
                                case FROZEN_OCEAN:
                                    factor = 0.3f;
                                    break;
                                case FROZEN_RIVER:
                                    factor = 0.3f;
                                    break;
                                case JUNGLE_HILLS:
                                    factor = 3.0f;
                                    break;
                                case MUSHROOM_FIELDS:
                                    factor = 1.5f;
                                    break;
                                case PLAINS:
                                    factor = 0.5f;
                                    break;
                                case RIVER:
                                    factor = 0.3f;
                                    break;
                                case MOUNTAIN_EDGE:
                                    factor = 5.0f;
                                    break;
                                case TAIGA_HILLS:
                                    factor = 3.0f;
                                    break;
                                default:
                                    break;
                            }
				// BEDROCK Layer
				int y = 0;
				HothUtils.setPos(chunk, x,y,z,Material.BEDROCK);
				HothUtils.setPos(chunk, x,y+1,z, getBedrockMaterial(localRand, (int)(256*0.9f))); // 90%
				HothUtils.setPos(chunk, x,y+2,z, getBedrockMaterial(localRand, (int)(256*0.7f))); // 70%
				HothUtils.setPos(chunk, x,y+3,z, getBedrockMaterial(localRand, (int)(256*0.5f))); // 50%
				HothUtils.setPos(chunk, x,y+4,z, getBedrockMaterial(localRand, (int)(256*0.3f))); // 30%
				HothUtils.setPos(chunk, x,y+5,z, getBedrockMaterial(localRand, (int)(256*0.2f))); // 20%
				
				// STONE Layer, solid
				for(y=6	;y<27 + surfaceOffset ;y++)
				{
					HothUtils.setPos(chunk, x,y,z, Material.STONE);
				}
				
				// STONE Layer
				double stone = this.noiseGenerator.noise(rx, rz, 8, 16)*3;
				for(int i=0;i<(int)(stone);i++)
				{
					HothUtils.setPos(chunk, x,y,z, Material.STONE);
					y++;
				}
				
				// DIRT Layer
				double dirt = this.noiseGenerator.noise(rx, rz, 8, 11)*5;
				for(int i=2;i< (int)(dirt);i++)
				{
					HothUtils.setPos(chunk, x,y,z, Material.DIRT);
					y++;
				}

				// GRAVEL Layer
				double gravel = this.noiseGenerator.noise(rx, rz, 7, 16)*5;
				for(int i=2;i< (int)(gravel);i++)
				{
					HothUtils.setPos(chunk, x,y,z, Material.GRAVEL);
					y++;
				}

				// SANDSTONE Layer
				double sandstone = this.noiseGenerator.noise(rx, rz, 8, 23)*4;
				for(int i=1;i< (int)(sandstone);i++)
				{
					HothUtils.setPos(chunk, x,y,z, Material.SANDSTONE);
					y++;
				}

				// SAND Layer
				double sand = 1+this.noiseGenerator.noise(rx, rz, 8, 43)*4;
				for(int i=0;i< (int)(sand);i++)
				{
					HothUtils.setPos(chunk, x,y,z, Material.SAND);
					y++;
				}

				// CLAY Layer
				double clay = 1+this.noiseGenerator.noise(rx, rz, 3, 9)*5;
				for(int i=3;i< (int)(clay);i++)
				{
					if(i==3)
					{
						HothUtils.setPos(chunk, x,y,z, Material.TERRACOTTA);
					}
					else
					{
						HothUtils.setPos(chunk, x,y,z, Material.CLAY);
					}
					y++;
				}
				

				
				// ice Layer
				while(y<34+surfaceOffset)
				{
					HothUtils.setPos(chunk, x,y,z, Material.ICE);
					y++;
				}
				
				double icel = this.noiseGenerator.noise(rx, rz, 3, 68)*8;
				for(int i=3;i< (int)(icel);i++)
				{
					HothUtils.setPos(chunk, x,y,z, Material.ICE);
					y++;
				}
				
				double iceh = this.noiseGenerator.noise(rx, rz, 3, 7)*15;
				
				// ICE mountains and hills
				double ice = factor * (this.noiseGenerator.noise(rx, rz, 4, 63)*2 + 
						      this.noiseGenerator.noise(rx, rz, 10, 12)) * 2.5;
				
				int icey = surfaceOffset + 64+(int)(ice);
				double dicey = surfaceOffset + 64+ice;
				for(;y<(icey-iceh);y++)
				{
					HothUtils.setPos(chunk, x,y,z, Material.PACKED_ICE); // Replace with packed ice
				}

				for(;y<(icey);y++)
				{
					HothUtils.setPos(chunk, x,y,z, Material.ICE);
				}
				
				// Inject stone mountains
				double domountain = this.noiseGenerator.noise(rx, rz, 4, 236)*20;
				double mfactor = 0.0f;
				if(domountain>18)
				{
					mfactor = 1.0f;
				}
				else if (domountain>16)
				{
					mfactor = (domountain-16)*0.5;
				}
					
				if(mfactor>0)
				{
					double mountain = this.noiseGenerator.noise(rx, rz, 4, 27)*84; // bulk of the mountain
					mountain = mountain + this.noiseGenerator.noise(rx, rz, 8, 3)*5; // Add a bit more noise
					for(int i=0;i<(int)(mountain*mfactor);i++)
					{
						HothUtils.setPos(chunk, x,i+26 + surfaceOffset,z, Material.STONE);
						
						if(i+26+surfaceOffset>y)
						{
							y = i+26+surfaceOffset;
						}
					}
				}
				
				// snowblock cover
				double snowblocks = 1+this.noiseGenerator.noise(rx, rz, 8, 76)*2;

				for(int i = 0;i<(int)(snowblocks + (dicey - (int)dicey)); i++)
				{
					HothUtils.setPos(chunk, x,y,z, Material.SNOW_BLOCK);
					y++;
				}				
				
				// snow cover
                                double dval = snowblocks+dicey;
                                snowcover[z][x] = new Position(rx, y, rz, (int) (8.0*(dval-(int)(dval))));
                                HothUtils.setPos(chunk, x,y,z, Material.SNOW);
                                
                                if(smooth_snow) {
				    byte data = (byte)snowcover[z][x].type;
                                    BlockState state = lm.getBlockFromLegacy(78, data);
                                    BlockData sdata = BukkitAdapter.adapt(state);
                                    chunk.setBlock(x, y, z, sdata);
                                }
				
				// LAVA Layer
				double dolava = this.noiseGenerator.noise(rx, rz, 4, 71)*10;
				if(dolava>7)
				{
					double lava = this.noiseGenerator.noise(rx, rz, 4, 7)*21;
					int lavay = (int)lava-18;
					if(lavay>-2)
					{
						int start = 8-(2+lavay)/2;
						
						for(int i=-1;i<lavay;i++)
						{
							if(start+i>6)
							{
								HothUtils.setPos(chunk, x,start+i,z, Material.AIR);
							}
							else
							{
								HothUtils.setPos(chunk, x,start+i,z, Material.LAVA);
							}
						}
					}
				}

				
				// WATER Layer
				double dowater = this.noiseGenerator.noise(rx, rz, 4, 91)*10;
				if(dowater>7)
				{
					double water = this.noiseGenerator.noise(rx, rz, 4, 8)*21;
					int watery = (int)water-18;
					if(watery>-2)
					{
						int start = 23-(2+watery)/2;
						
						for(int i=-1;i<watery;i++)
						{
							if(start+i>21)
							{
								HothUtils.setPos(chunk, x,start+i,z, Material.AIR);
							}
							else
							{
								HothUtils.setPos(chunk, x,start+i,z, Material.WATER);
							}
							
						}
					}
				}
			}
		}
		
		// Add structures and such
                
		GardenGenerator.generateGarden(WorldGenerator.plugin, world, new Random(random.nextLong()), chunkx, chunkz);
		RoomGenerator.generateRooms(world, WorldGenerator.plugin, new Random(random.nextLong()), chunkx, chunkz);
		OreGenerator.generateOres(WorldGenerator.plugin, world, chunk, new Random(random.nextLong()) , chunkx, chunkz);
		DomeGenerator.generateDome(WorldGenerator.plugin, world, new Random(random.nextLong()), chunkx, chunkz);
		BaseGenerator.generateBase(WorldGenerator.plugin, world, new Random(random.nextLong()), chunkx, chunkz);
		SchematicsGenerator.generateSchematics(WorldGenerator.plugin, world, new Random(random.nextLong()), chunkx, chunkz);
		//CustomGenerator.generateCustom(WorldGenerator.plugin, world, new Random(random.nextLong()), chunkx, chunkz);
		CaveGenerator.generateCaves(WorldGenerator.plugin, world, new Random(random.nextLong()), chunkx, chunkz);
		SpikeGenerator.generateSpikes(WorldGenerator.plugin, world, new Random(random.nextLong()), chunkx, chunkz);
		//SnowGenerator.generateSnowCover(WorldGenerator.plugin, world, snowcover);

		return chunk;
	}
	
    public ChunkData generateExtBlockSectionsTatooine(World world, Random random, int chunkx, int chunkz, BiomeGrid biomes)
	{
		//int mSTONE_id = MaterialManager.toID(Material.STONE);
		//int mSANDSTONE_id = MaterialManager.toID(Material.SANDSTONE);
		//int mSAND_id = MaterialManager.toID(Material.SAND);
		Material mAIR = Material.AIR;
		Material mLAVA = Material.LAVA;
		
		if(this.noiseGenerator==null)
		{
			this.noiseGenerator = new NoiseGenerator(world);
		}
		
		int surfaceOffset = ConfigManager.getWorldSurfaceoffset(WorldGenerator.plugin, world);
		
		Random localRand = new Random(chunkx*chunkz);
		
		int vsegs = WorldGenerator.plugin.getHeight() / 16;
		ChunkData chunk = createChunkData(world);
		
		for(int z=0;z<16;z++)
		{
			for(int x=0;x<16;x++)
			{
				int rx = chunkx*16 + x;
				int rz = chunkz*16 + z;
				
				Biome biome = biomes.getBiome(x, z);
				float factor = 1.0f;
				if(biome.equals(Biome.DESERT_HILLS))
				{
					factor = 2.0f;
				}
				else if(biome.equals(Biome.MOUNTAINS))
				{
					factor = 8.0f;
				}
				else if(biome.equals(Biome.WOODED_HILLS))
				{
					factor = 3.0f;
				}
				else if(biome.equals(Biome.SNOWY_MOUNTAINS))
				{
					factor = 5.0f;
				}
				else if(biome.equals(Biome.SNOWY_TUNDRA))
				{
					factor = 0.5f;
				}
				else if(biome.equals(Biome.FROZEN_OCEAN))
				{
					factor = 0.3f;
				}
				else if(biome.equals(Biome.FROZEN_RIVER))
				{
					factor = 0.3f;
				}
				else if(biome.equals(Biome.JUNGLE_HILLS))
				{
					factor = 3.0f;
				}
				else if(biome.equals(Biome.MUSHROOM_FIELDS))
				{
					factor = 1.5f;
				}
				else if(biome.equals(Biome.PLAINS))
				{
					factor = 0.5f;
				}
				else if(biome.equals(Biome.RIVER))
				{
					factor = 0.3f;
				}
				else if(biome.equals(Biome.MOUNTAIN_EDGE))
				{
					factor = 5.0f;
				}
				else if(biome.equals(Biome.TAIGA_HILLS))
				{
					factor = 3.0f;
				}
				// BEDROCK Layer
				int y = 0;
				HothUtils.setPos(chunk, x,y,z,Material.BEDROCK);
				HothUtils.setPos(chunk, x,y+1,z, getBedrockMaterial(localRand, (int)(256*0.9f))); // 90%
				HothUtils.setPos(chunk, x,y+2,z, getBedrockMaterial(localRand, (int)(256*0.7f))); // 70%
				HothUtils.setPos(chunk, x,y+3,z, getBedrockMaterial(localRand, (int)(256*0.5f))); // 50%
				HothUtils.setPos(chunk, x,y+4,z, getBedrockMaterial(localRand, (int)(256*0.3f))); // 30%
				HothUtils.setPos(chunk, x,y+5,z, getBedrockMaterial(localRand, (int)(256*0.2f))); // 20%
				
				// STONE Layer, solid
				for(y=6	;y<27 + surfaceOffset ;y++)
				{
					HothUtils.setPos(chunk, x,y,z, Material.STONE);
				}
				
				// STONE Layer
				double stone = this.noiseGenerator.noise(rx, rz, 8, 16)*3;
				for(int i=0;i<(int)(stone);i++)
				{
					HothUtils.setPos(chunk, x,y,z, Material.STONE);
					y++;
				}
				
				// DIRT Layer
				double dirt = this.noiseGenerator.noise(rx, rz, 8, 11)*5;
				for(int i=2;i< (int)(dirt);i++)
				{
					HothUtils.setPos(chunk, x,y,z, Material.DIRT);
					y++;
				}

				// GRAVEL Layer
				double gravel = this.noiseGenerator.noise(rx, rz, 7, 16)*5;
				for(int i=2;i< (int)(gravel);i++)
				{
					HothUtils.setPos(chunk, x,y,z, Material.GRAVEL);
					y++;
				}

				/*
				// SANDSTONE Layer
				double sandstone = this.noiseGenerator.noise(rx, rz, 8, 23)*4;
				for(int i=1;i< (int)(sandstone);i++)
				{
					HothUtils.setPos(chunk, x,y,z, Material.SANDSTONE);
					y++;
				}

				// SAND Layer
				double sand = 1+this.noiseGenerator.noise(rx, rz, 8, 43)*4;
				for(int i=0;i< (int)(sand);i++)
				{
					HothUtils.setPos(chunk, x,y,z, Material.SAND);
					y++;
				}
				*/

				// CLAY Layer
				double clay = 1+this.noiseGenerator.noise(rx, rz, 3, 9)*5;
				for(int i=3;i< (int)(clay);i++)
				{
					if(i==3)
					{
						HothUtils.setPos(chunk, x,y,z, Material.TERRACOTTA);
					}
					else
					{
						HothUtils.setPos(chunk, x,y,z, Material.CLAY);
					}
					y++;
				}
				

				/*
				// ice Layer
				while(y<34+surfaceOffset)
				{
					HothUtils.setPos(chunk, x,y,z, Material.ICE);
					y++;
				}
				
				double icel = this.noiseGenerator.noise(rx, rz, 3, 68)*8;
				for(int i=3;i< (int)(icel);i++)
				{
					HothUtils.setPos(chunk, x,y,z, Material.ICE);
					y++;
				}
				*/
				
				double iceh = this.noiseGenerator.noise(rx, rz, 3, 7)*15;
				
				// ICE mountains and hills
				double ice = factor * (this.noiseGenerator.noise(rx, rz, 4, 63)*2 + 
						      this.noiseGenerator.noise(rx, rz, 10, 12)) * 2.5;
				
				int icey = surfaceOffset + 64+(int)(ice);
				double dicey = surfaceOffset + 64+ice;
				for(;y<(icey-iceh);y++)
				{
					HothUtils.setPos(chunk, x,y,z, Material.SANDSTONE);
				}

				for(;y<(icey);y++)
				{
					HothUtils.setPos(chunk, x,y,z, Material.SAND);
				}
				
				
				// Inject stone mountains
				double domountain = this.noiseGenerator.noise(rx, rz, 4, 236)*20;
				double mfactor = 0.0f;
				if(domountain>18)
				{
					mfactor = 1.0f;
				}
				else if (domountain>16)
				{
					mfactor = (domountain-16)*0.5;
				}
					
				if(mfactor>0)
				{
					double mountain = this.noiseGenerator.noise(rx, rz, 4, 27)*84; // bulk of the mountain
					mountain = mountain + this.noiseGenerator.noise(rx, rz, 8, 3)*5; // Add a bit more noise
					for(int i=0;i<(int)(mountain*mfactor);i++)
					{
						HothUtils.setPos(chunk, x,i+26 + surfaceOffset,z, Material.STONE);
						
						if(i+26+surfaceOffset>y)
						{
							y = i+26+surfaceOffset;
						}
					}
				}
				
				// Sand cover
				double snowblocks = 1+this.noiseGenerator.noise(rx, rz, 8, 76)*2;

				for(int i = 0;i<(int)(snowblocks + (dicey - (int)dicey)); i++)
				{
					HothUtils.setPos(chunk, x,y,z, Material.SAND);
					y++;
				}
				
				// Cave layer
				double a = this.noiseGenerator.noise(rx, rz, 2, 60)*8;
				for(int i=4;i<128+surfaceOffset;i++)
				{
					double d = this.noiseGenerator.noise(rx, i, rz, 4, 8)*16;
					
					if(i>(96+surfaceOffset))
					{
						a = a + 8  * (((i+surfaceOffset)-32.0)/32.0); // fade out the higher we go
					}
					if(d>(a+10))
					{
						Material old = chunk.getType(x, i, z);
						if( old == Material.STONE
						 || old == Material.SANDSTONE 
						 || old == Material.SAND
								)
						
						if(i<12)
						{
							HothUtils.setPos(chunk, x,i,z, mLAVA);
						}
						else
						{
							HothUtils.setPos(chunk, x,i,z, mAIR);
						}
					}
				}
				
				
				// LAVA Layer
				double dolava = this.noiseGenerator.noise(rx, rz, 4, 71)*10;
				if(dolava>7)
				{
					double lava = this.noiseGenerator.noise(rx, rz, 4, 7)*21;
					int lavay = (int)lava-18;
					if(lavay>-2)
					{
						int start = 8-(2+lavay)/2;
						
						for(int i=-1;i<lavay;i++)
						{
							if(start+i>6)
							{
								HothUtils.setPos(chunk, x,start+i,z, Material.AIR);
							}
							else
							{
								HothUtils.setPos(chunk, x,start+i,z, Material.LAVA);
							}
						}
					}
				}

				
				// WATER Layer
				double dowater = this.noiseGenerator.noise(rx, rz, 4, 91)*10;
				if(dowater>7)
				{
					double water = this.noiseGenerator.noise(rx, rz, 4, 8)*21;
					int watery = (int)water-18;
					if(watery>-2)
					{
						int start = 23-(2+watery)/2;
						
						for(int i=-1;i<watery;i++)
						{
							if(start+i>21)
							{
								HothUtils.setPos(chunk, x,start+i,z, Material.AIR);
							}
							else
							{
								HothUtils.setPos(chunk, x,start+i,z, Material.WATER);
							}
							
						}
					}
				}
				
			}
		}
		
		// Add structures and such
		GardenGenerator.generateGarden(WorldGenerator.plugin, world, new Random(random.nextLong()), chunkx, chunkz);
		RoomGenerator.generateRooms(world, WorldGenerator.plugin, new Random(random.nextLong()), chunkx, chunkz);
		OreGenerator.generateOres(WorldGenerator.plugin, world, chunk, new Random(random.nextLong()) , chunkx, chunkz);
		//DomeGenerator.generateDome(WorldGenerator.plugin, world, new Random(random.nextLong()), chunkx, chunkz);
		//BaseGenerator.generateBase(WorldGenerator.plugin, world, new Random(random.nextLong()), chunkx, chunkz);
		SchematicsGenerator.generateSchematics(WorldGenerator.plugin, world, new Random(random.nextLong()), chunkx, chunkz);
		//CustomGenerator.generateCustom(WorldGenerator.plugin, world, new Random(random.nextLong()), chunkx, chunkz);
		//CaveGenerator.generateCaves(WorldGenerator.plugin, world, new Random(random.nextLong()), chunkx, chunkz);
		//SpikeGenerator.generateSpikes(WorldGenerator.plugin, world, new Random(random.nextLong()), chunkx, chunkz);
		VillageGenerator.generateVillage(WorldGenerator.plugin, world, new Random(random.nextLong()), chunkx, chunkz);
		//SnowGenerator.generateSnowCover(WorldGenerator.plugin, world, snowcover);

		HothUtils.replaceTop(chunk, Material.SANDSTONE, Material.STONE, Material.SAND, WorldGenerator.plugin.getHeight());
		
		return chunk;
	}
	
	public ChunkData generateExtBlockSectionsDagobah(World world, Random random, int chunkx, int chunkz, BiomeGrid biomes)
	{
		DagobahOrePopulator orePopulator = new DagobahOrePopulator(WorldGenerator.plugin.getHeight());
		
		//int mSTONE_id = MaterialManager.toID(Material.STONE);
		//int mDIRT_id = MaterialManager.toID(Material.DIRT);
		//int mGRASS_id = MaterialManager.toID(Material.GRASS);
		//int mAIR_id = MaterialManager.toID(Material.AIR);
		Material mAIR = Material.AIR;
		Material mLAVA = Material.LAVA;
		
		if(this.noiseGenerator==null)
		{
			this.noiseGenerator = new NoiseGenerator(world);
		}
		
		int surfaceOffset = ConfigManager.getWorldSurfaceoffset(WorldGenerator.plugin, world);
		
		Random localRand = new Random(chunkx*chunkz);
		
		int vsegs = WorldGenerator.plugin.getHeight() / 16;
		ChunkData chunk = createChunkData(world);
		
		for(int z=0;z<16;z++)
		{
			for(int x=0;x<16;x++)
			{
				int rx = chunkx*16 + x;
				int rz = chunkz*16 + z;
				
				Biome biome = biomes.getBiome(x, z);
				float factor = 1.0f;
				if(biome.equals(Biome.DESERT_HILLS))
				{
					factor = 2.0f;
				}
				else if(biome.equals(Biome.MOUNTAINS))
				{
					factor = 3.0f;
				}
				else if(biome.equals(Biome.WOODED_HILLS))
				{
					factor = 2.5f;
				}
				else if(biome.equals(Biome.SNOWY_MOUNTAINS))
				{
					factor = 3.0f;
				}
				else if(biome.equals(Biome.SNOWY_TUNDRA))
				{
					factor = 0.5f;
				}
				else if(biome.equals(Biome.FROZEN_OCEAN))
				{
					factor = 1.3f;
				}
				else if(biome.equals(Biome.FROZEN_RIVER))
				{
					factor = 0.3f;
				}
				else if(biome.equals(Biome.JUNGLE_HILLS))
				{
					factor = 1.6f;
				}
				else if(biome.equals(Biome.MUSHROOM_FIELDS))
				{
					factor = 2.0f;
				}
				else if(biome.equals(Biome.MUSHROOM_FIELD_SHORE))
				{
					factor = 1.0f;
				}
				else if(biome.equals(Biome.PLAINS))
				{
					factor = 0.5f;
				}
				else if(biome.equals(Biome.RIVER))
				{
					factor = 0.1f;
				}
				else if(biome.equals(Biome.MOUNTAIN_EDGE ))
				{
					factor = 2.8f;
				}
				else if(biome.equals(Biome.TAIGA_HILLS))
				{
					factor = 2.0f;
				}
				// BEDROCK Layer
				int y = 0;
				HothUtils.setPos(chunk, x,y,z,Material.BEDROCK);
				HothUtils.setPos(chunk, x,y+1,z, getBedrockMaterial(localRand, (int)(256*0.9f))); // 90%
				HothUtils.setPos(chunk, x,y+2,z, getBedrockMaterial(localRand, (int)(256*0.7f))); // 70%
				HothUtils.setPos(chunk, x,y+3,z, getBedrockMaterial(localRand, (int)(256*0.5f))); // 50%
				HothUtils.setPos(chunk, x,y+4,z, getBedrockMaterial(localRand, (int)(256*0.3f))); // 30%
				HothUtils.setPos(chunk, x,y+5,z, getBedrockMaterial(localRand, (int)(256*0.2f))); // 20%
				
				// STONE Layer, solid
				for(y=6	;y<27 + surfaceOffset ;y++)
				{
					HothUtils.setPos(chunk, x,y,z, Material.STONE);
				}
				
				// STONE Layer
				double stone = this.noiseGenerator.noise(rx, rz, 8, 16)*3;
				for(int i=0;i<(int)(stone);i++)
				{
					HothUtils.setPos(chunk, x,y,z, Material.STONE);
					y++;
				}
				
				// DIRT Layer
				double dirt = this.noiseGenerator.noise(rx, rz, 8, 11)*5;
				for(int i=2;i< (int)(dirt);i++)
				{
					HothUtils.setPos(chunk, x,y,z, Material.DIRT);
					y++;
				}

				// GRAVEL Layer
				double gravel = this.noiseGenerator.noise(rx, rz, 7, 16)*5;
				for(int i=2;i< (int)(gravel);i++)
				{
					HothUtils.setPos(chunk, x,y,z, Material.GRAVEL);
					y++;
				}

				/*
				// SANDSTONE Layer
				double sandstone = this.noiseGenerator.noise(rx, rz, 8, 23)*4;
				for(int i=1;i< (int)(sandstone);i++)
				{
					HothUtils.setPos(chunk, x,y,z, Material.SANDSTONE);
					y++;
				}

				// SAND Layer
				double sand = 1+this.noiseGenerator.noise(rx, rz, 8, 43)*4;
				for(int i=0;i< (int)(sand);i++)
				{
					HothUtils.setPos(chunk, x,y,z, Material.SAND);
					y++;
				}
				*/

				// CLAY Layer
				double clay = 1+this.noiseGenerator.noise(rx, rz, 3, 9)*5;
				for(int i=3;i< (int)(clay);i++)
				{
					if(i==3)
					{
						HothUtils.setPos(chunk, x,y,z, Material.TERRACOTTA);
					}
					else
					{
						HothUtils.setPos(chunk, x,y,z, Material.CLAY);
					}
					y++;
				}
				

				/*
				// ice Layer
				while(y<34+surfaceOffset)
				{
					HothUtils.setPos(chunk, x,y,z, Material.ICE);
					y++;
				}
				
				double icel = this.noiseGenerator.noise(rx, rz, 3, 68)*8;
				for(int i=3;i< (int)(icel);i++)
				{
					HothUtils.setPos(chunk, x,y,z, Material.ICE);
					y++;
				}
				*/
				
				double iceh = this.noiseGenerator.noise(rx, rz, 3, 7)*15;
				
				// ICE mountains and hills
				double ice = factor * (this.noiseGenerator.noise(rx, rz, 4, 63)*2 + 
						      this.noiseGenerator.noise(rx, rz, 10, 12)) * 2.5;
				
				int icey = surfaceOffset + 64+(int)(ice);
				double dicey = surfaceOffset + 64+ice;
				for(;y<(icey-iceh);y++)
				{
					HothUtils.setPos(chunk, x,y,z, Material.DIRT);
				}

				for(;y<(icey);y++)
				{
					HothUtils.setPos(chunk, x,y,z, Material.DIRT);
				}
				
				
				// Inject stone mountains
				double domountain = this.noiseGenerator.noise(rx, rz, 4, 336)*20;
				double mfactor = 0.0f;

				if(domountain>10.0)
				{
					mfactor = (domountain-10.0)/10.0;
				}
					
				if(mfactor>0)
				{
					double mountain = this.noiseGenerator.noise(rx, rz, 4, 87)*84; // bulk of the mountain
					mountain = mountain + this.noiseGenerator.noise(rx, rz, 8, 30)*15; // Add a bit more noise
					for(int i=0;i<(int)(mountain*mfactor);i++)
					{
						HothUtils.setPos(chunk, x,i+26 + surfaceOffset,z, Material.STONE);
						
						if(i+26+surfaceOffset>y)
						{
							y = i+26+surfaceOffset;
						}
					}
				}
				
				// Dirt/Grass cover
				double snowblocks = 1+this.noiseGenerator.noise(rx, rz, 8, 76)*2;

				for(int i = 0;i<(int)(snowblocks + (dicey - (int)dicey)); i++)
				{
					if(i==(int)(snowblocks + (dicey - (int)dicey))-1)
					{
						if(biome.equals(Biome.MUSHROOM_FIELDS) || biome.equals(Biome.MUSHROOM_FIELD_SHORE))
						{
							HothUtils.setPos(chunk, x,y,z, Material.MYCELIUM);
						}
						else
						{
							HothUtils.setPos(chunk, x,y,z, Material.GRASS_BLOCK);
						}
					}
					else
					{
						HothUtils.setPos(chunk, x,y,z, Material.DIRT);
					}
					
					y++;
				}	

				// Cave layer
				double a = this.noiseGenerator.noise(rx, rz, 2, 150)*4;
				a = a + this.noiseGenerator.noise(rx, rz, 2, 43)*4;
				for(int i=4;i<128+surfaceOffset;i++)
				{
					//double a = this.noiseGenerator.noise(rx, i, rz, 4, 50)*8;
					double d = this.noiseGenerator.noise(rx, i, rz, 4, 10)*16;
					
					if(i>(96+surfaceOffset))
					{
						a = a + 8  * (((i+surfaceOffset)-32.0)/32.0); // fade out the higher we go
					}
					if(d>(a+8))
					{
						Material old = chunk.getType(x, i, z);
						if( old == Material.STONE
						 || old == Material.DIRT 
						 || old == Material.GRASS_BLOCK
								)
						
						if(i<12)
						{
							HothUtils.setPos(chunk, x,i,z, mLAVA);
						}
						else
						{
							HothUtils.setPos(chunk, x,i,z, mAIR);
						}
					}
				}

				// Sediment layer
				if(y<68)
				{
					double sediment = this.noiseGenerator.noise(rx, rz, 4, 23)*100;
					double sedimentD = this.noiseGenerator.noise(rx, rz, 4, 18)*2;
					for(int i=1;i<sedimentD+1;i++)
					if(sediment>90)
					{
						HothUtils.setPos(chunk, x,y-i,z, Material.SAND);
					}
					else if(sediment>80)
					{
						HothUtils.setPos(chunk, x,y-i,z, Material.CLAY);
					}
					else if(sediment>70)
					{
						HothUtils.setPos(chunk, x,y-i,z, Material.GRAVEL);
					}
					else if(sediment>60)
					{
						HothUtils.setPos(chunk, x,y-i,z, Material.TERRACOTTA);
					}
					else if(sediment<10)
					{
						HothUtils.setPos(chunk, x,y-i,z, Material.STONE);
					}
					else
					{
						HothUtils.setPos(chunk, x,y-i,z, Material.DIRT);
					}
				}
				
				// LAVA Layer
				double dolava = this.noiseGenerator.noise(rx, rz, 4, 71)*10;
				if(dolava>7)
				{
					double lava = this.noiseGenerator.noise(rx, rz, 4, 7)*21;
					int lavay = (int)lava-18;
					if(lavay>-2)
					{
						int start = 8-(2+lavay)/2;
						
						for(int i=-1;i<lavay;i++)
						{
							if(start+i>6)
							{
								HothUtils.setPos(chunk, x,start+i,z, Material.AIR);
							}
							else
							{
								HothUtils.setPos(chunk, x,start+i,z, Material.LAVA);
							}
						}
					}
				}

				
				// WATER Layer
				double dowater = this.noiseGenerator.noise(rx, rz, 4, 91)*10;
				if(dowater>7)
				{
					double water = this.noiseGenerator.noise(rx, rz, 4, 8)*21;
					int watery = (int)water-18;
					if(watery>-2)
					{
						int start = 23-(2+watery)/2;
						
						for(int i=-1;i<watery;i++)
						{
							if(start+i>21)
							{
								HothUtils.setPos(chunk, x,start+i,z, Material.AIR);
							}
							else
							{
								HothUtils.setPos(chunk, x,start+i,z, Material.WATER);
							}
							
						}
					}
				}
				
				// Global water level
				int wy = 67;
				boolean doLily = false;
				while(chunk.getType(x,wy,z)== Material.AIR)
				{
					doLily = true;
					//HothUtils.setPos(chunk, x,wy,z, Material.STATIONARY_WATER);
                                        chunk.setBlock(x, wy, z, Material.WATER);
					wy--;
				}

				if(doLily)
				{
					double lily = 1+this.noiseGenerator.noise(rx, rz, 4, 17)*100;
					if(random.nextInt((int)lily) == 1)
					{
						//HothUtils.setPos(chunk, x,68,z, Material.WATER_LILY);
                                            chunk.setBlock(x, 68, z, Material.LILY_PAD);
					}
				}

			}
		}
		
		//orePopulator.populateWater(WorldGenerator.plugin, world, new Random(random.nextLong()), chunk, surfaceOffset);

		// Add structures and such
		GardenGenerator.generateGarden(WorldGenerator.plugin, world, new Random(random.nextLong()), chunkx, chunkz);
		RoomGenerator.generateRooms(world, WorldGenerator.plugin, new Random(random.nextLong()), chunkx, chunkz);
		OreGenerator.generateOres(WorldGenerator.plugin, world, chunk, new Random(random.nextLong()) , chunkx, chunkz);
                //DagobahHugeTreeGenerator.generateDagobahHugeTree(WorldGenerator.plugin, world, new Random(random.nextLong()), chunkx, chunkz);
                SchematicsGenerator.generateSchematics(WorldGenerator.plugin, world, new Random(random.nextLong()), chunkx, chunkz);
		//CustomGenerator.generateCustom(WorldGenerator.plugin, world, new Random(random.nextLong()), chunkx, chunkz);
		
		HothUtils.replaceTop(chunk, Material.DIRT, Material.STONE, Material.GRASS_BLOCK, WorldGenerator.plugin.getHeight());

		return chunk;
	}
	
	public ChunkData generateExtBlockSectionsMustafar(World world, Random random, int chunkx, int chunkz, BiomeGrid biomes)
	{
		boolean smoothlava = ConfigManager.isSmoothLava(WorldGenerator.plugin);

		//int mSTONE_id = MaterialManager.toID(Material.STONE);
		//int mLAVA_id = MaterialManager.toID(Material.LAVA);
		//int mAIR_id = MaterialManager.toID(Material.AIR);
		boolean smooth_lava = false;
                if(ConfigManager.isSmoothLava(plugin)) smooth_lava = true;
		LegacyMapper lm = LegacyMapper.getInstance();
                
		Position[][] lavacover = new Position[16][16];

		
		if(this.noiseGenerator==null)
		{
			this.noiseGenerator = LavaLevelGenerator.getNoiseGenerator(world);
		}
		
		int surfaceOffset = ConfigManager.getWorldSurfaceoffset(WorldGenerator.plugin, world);
		
		Random localRand = new Random(chunkx*chunkz);
		
		int vsegs = WorldGenerator.plugin.getHeight() / 16;
		ChunkData chunk = createChunkData(world);
		
		for(int z=0;z<16;z++)
		{
			for(int x=0;x<16;x++)
			{
				int rx = chunkx*16 + x;
				int rz = chunkz*16 + z;
				
				// Lava level calculation
				double dLavaLevel = LavaLevelGenerator.getLavaLevelAt(noiseGenerator, rx, rz, surfaceOffset);
				int lavaLevel = (int)dLavaLevel;
				
				lavacover[z][x] = new Position(rx, lavaLevel, rz, (int) (8.0*((1 - (dLavaLevel - lavaLevel)))));
                                if(smooth_lava) {
                                    byte data = (byte)lavacover[z][x].type;
                                    BlockState state = lm.getBlockFromLegacy(10, data);
                                    BlockData sdata = BukkitAdapter.adapt(state);
                                    chunk.setBlock(x, lavaLevel, z, sdata);
                                }
                                
                                
				// BEDROCK Layer
				int y = 0;
				HothUtils.setPos(chunk, x,y,z,Material.BEDROCK);
				HothUtils.setPos(chunk, x,y+1,z, getBedrockMaterial(localRand, (int)(256*0.9f), Material.LAVA)); // 90%
				HothUtils.setPos(chunk, x,y+2,z, getBedrockMaterial(localRand, (int)(256*0.7f), Material.LAVA)); // 70%
				HothUtils.setPos(chunk, x,y+3,z, getBedrockMaterial(localRand, (int)(256*0.5f), Material.LAVA)); // 50%
				HothUtils.setPos(chunk, x,y+4,z, getBedrockMaterial(localRand, (int)(256*0.3f), Material.LAVA)); // 30%
				HothUtils.setPos(chunk, x,y+5,z, getBedrockMaterial(localRand, (int)(256*0.2f), Material.LAVA)); // 20%
				
				// Solid lava layer
				for(y=6;y<10;y++)
				{
					HothUtils.setPos(chunk, x,y,z,Material.LAVA);
				}
				
				// Smooth lava layer
				for(y=10;y<16;y++)
				{
					// DIRT Layer
					double h = this.noiseGenerator.noise(rx, rz, 8, 11)*6;
					if(y-10>h)
					{
						HothUtils.setPos(chunk,  x, y, z,  Material.STONE);
					}
					else
					{
						HothUtils.setPos(chunk,  x, y, z,  Material.LAVA);
					}
				}
				
				// Bulk layer
				for(y=16;y<64+surfaceOffset-20;y++)
				{
					HothUtils.setPos(chunk,  x, y, z,  Material.STONE);
				}
				
				// Surface level
				double ml1 = this.noiseGenerator.noise(rx, rz, 2, 236)*16;
				double ml2 = this.noiseGenerator.noise(rx, rz, 2, 33)*8;
				double ml3 = this.noiseGenerator.noise(rx, rz, 2, 22)*12;
				double ml4 = this.noiseGenerator.noise(rx, rz, 2, 7)*16 * this.noiseGenerator.noise(rx, rz, 2, 125);
				// - add some noise
				double fn1 = this.noiseGenerator.noise(rx, rz, 2, 235);
				double fn2 = fn1 * this.noiseGenerator.noise(rx, rz, 2, 3) * 4;
				
				int ml = (int)(ml1+ml2+ml3+ml4+fn2);
				for(int j=0;j<ml;j++)
				{
					HothUtils.setPos(chunk,  x, y+j, z,  Material.STONE);
				}
				
				// Inject stone mountains at lava level summits
				double domountain = this.noiseGenerator.noise(rx, rz, 4, 635)*20;
				double mfactor = 0.0f;

				if(domountain>10.0)
				{
					mfactor = (domountain-10.0)/10.0;
				}
					
				if(mfactor>0)
				{
					double mountain = this.noiseGenerator.noise(rx, rz, 4, 87)*104; // bulk of the mountain
					mountain = mountain + this.noiseGenerator.noise(rx, rz, 8, 50)*49; // Add a bit more noise
					mountain = mountain + this.noiseGenerator.noise(rx, rz, 8, 4)*7; // Add a bit more noise
					for(int i=0;i<(int)(mountain*mfactor);i++)
					{
						HothUtils.setPos(chunk, x,i+26 + surfaceOffset,z, Material.STONE);
						
						if(i+26+surfaceOffset>y)
						{
							y = i+26+surfaceOffset;
						}
					}
				}
				
				// River layer
				double r1 = this.noiseGenerator.noise(rx, rz, 2, 350)*(32+64)-16;
				double r2 = this.noiseGenerator.noise(rx, rz, 2, 830)*(32+64)-16;
				
				double r = r1+r2;
				
				if(r>60 && r<76)  // 66 - 70 = lavariver
				{
					//double height = (3-Math.abs(68-r))*3;
					double height = (3-Math.abs(68-r)*Math.abs(68-r))/1.5;
					int rivy = lavaLevel - (int)height;
					while(chunk.getType(x, rivy, z)!=Material.AIR)
					{
						HothUtils.setPos(chunk,  x, rivy, z,  Material.AIR);
						rivy++;
					}
				}
					

				// Global lava level
				int wy = lavaLevel;
				boolean added = false;
				while(chunk.getType(x,wy,z)==Material.AIR || chunk.getType(x,wy,z) == Material.LAVA)
				{
					added = true;
					HothUtils.setPos(chunk, x,wy,z, Material.LAVA);
					wy--;
				}
				// If we have smooth lava then we need an extra layer of lava or else the top layer may vanish
				if(smoothlava && added && wy>0)
				{
					HothUtils.setPos(chunk, x,wy,z, Material.LAVA);
				}

				// Lava tubes layer
				double a = this.noiseGenerator.noise(rx, rz, 2, 150)*4;
				a = a + this.noiseGenerator.noise(rx, rz, 2, 43)*4;
				for(int i=10;i<128+surfaceOffset+16;i++)
				{
					double d = this.noiseGenerator.noise(rx, i, rz, 4, 10)*16;
					
					if(i>(96+surfaceOffset+16))
					{
						a = a + 8  * (((i+surfaceOffset)-32.0)/32.0); // fade out the higher we go
					}
					if(d>(a+8))
					{
						Material old = chunk.getType(x,i,z);
						if( old == Material.STONE
								)
						{
							HothUtils.setPos(chunk, x,i,z, Material.LAVA);
						}
					}
				}
			
				// Cave layer
				a = this.noiseGenerator.noise(rx, rz, 2, 160)*4;
				a = a + this.noiseGenerator.noise(rx, rz, 2, 42)*4;
				for(int i=10;i<128+surfaceOffset+16;i++)
				{
					double d = this.noiseGenerator.noise(rx, i, rz, 4, 11)*16;
					
					if(i>(96+surfaceOffset+16))
					{
						a = a + 8  * (((i+surfaceOffset)-32.0)/32.0); // fade out the higher we go
					}
					if(d>(a+8))
					{
						Material old = chunk.getType(x,i,z);
						if( old == Material.STONE
								)
						{
							HothUtils.setPos(chunk, x,i,z, Material.AIR);
						}
					}
				}
			
			}
		}
		
		// Add structures and such
		MustafarBaseGenerator.generateBase(world, WorldGenerator.plugin, new Random(random.nextLong()), chunkx, chunkz);
		// GardenGenerator.generateGarden(HothGenerator.plugin, world, new Random(random.nextLong()), chunkx, chunkz);
		// RoomGenerator.generateRooms(world, HothGenerator.plugin, new Random(random.nextLong()), chunkx, chunkz);
		// OreGenerator.generateOres(HothGenerator.plugin, world, chunk, new Random(random.nextLong()) , chunkx, chunkz);
		// SchematicsGenerator.generateSchematics(HothGenerator.plugin, world, new Random(random.nextLong()), chunkx, chunkz);
		// CustomGenerator.generateCustom(HothGenerator.plugin, world, new Random(random.nextLong()), chunkx, chunkz);
		//SnowGenerator.generateLavaCover(WorldGenerator.plugin, world, lavacover);

		
		return chunk;
	}
	
	public ChunkData generateExtBlockSectionsKashyyyk(World world, Random random, int chunkx, int chunkz, BiomeGrid biomes)
	{
		DagobahOrePopulator orePopulator = new DagobahOrePopulator(WorldGenerator.plugin.getHeight());
		
		/*
		int mSTONE_id = MaterialManager.toID(Material.STONE);
		int mDIRT_id = MaterialManager.toID(Material.DIRT);
		int mGRASS_id = MaterialManager.toID(Material.GRASS);
		int mAIR_id = MaterialManager.toID(Material.AIR);
		Material mAIR = Material.AIR;
		Material mLAVA = Material.LAVA;
		*/
		
		if(this.noiseGenerator==null)
		{
			this.noiseGenerator = new NoiseGenerator(world);
		}
		
		int surfaceOffset = ConfigManager.getWorldSurfaceoffset(WorldGenerator.plugin, world);
		
		Random localRand = new Random(chunkx*chunkz);
		
		int vsegs = WorldGenerator.plugin.getHeight() / 16;
		ChunkData chunk = createChunkData(world);
		
		for(int z=0;z<16;z++)
		{
			for(int x=0;x<16;x++)
			{
				int rx = chunkx*16 + x;
				int rz = chunkz*16 + z;
				
				Biome biome = biomes.getBiome(x, z);
				float factor = 1.0f;
				if(biome.equals(Biome.DESERT_HILLS))
				{
					factor = 2.0f;
				}
				else if(biome.equals(Biome.MOUNTAINS))
				{
					factor = 3.0f;
				}
				else if(biome.equals(Biome.WOODED_HILLS))
				{
					factor = 2.5f;
				}
				else if(biome.equals(Biome.SNOWY_MOUNTAINS))
				{
					factor = 3.0f;
				}
				else if(biome.equals(Biome.SNOWY_TUNDRA))
				{
					factor = 0.5f;
				}
				else if(biome.equals(Biome.FROZEN_OCEAN))
				{
					factor = 1.3f;
				}
				else if(biome.equals(Biome.FROZEN_RIVER))
				{
					factor = 0.3f;
				}
				else if(biome.equals(Biome.JUNGLE_HILLS))
				{
					factor = 1.6f;
				}
				else if(biome.equals(Biome.MUSHROOM_FIELDS))
				{
					factor = 2.0f;
				}
				else if(biome.equals(Biome.MUSHROOM_FIELD_SHORE))
				{
					factor = 1.0f;
				}
				else if(biome.equals(Biome.PLAINS))
				{
					factor = 0.5f;
				}
				else if(biome.equals(Biome.RIVER))
				{
					factor = 0.1f;
				}
				else if(biome.equals(Biome.MOUNTAIN_EDGE))
				{
					factor = 2.8f;
				}
				else if(biome.equals(Biome.TAIGA_HILLS))
				{
					factor = 2.0f;
				}
				// BEDROCK Layer
				int y = 0;
				HothUtils.setPos(chunk, x,y,z,Material.BEDROCK);
				HothUtils.setPos(chunk, x,y+1,z, getBedrockMaterial(localRand, (int)(256*0.9f))); // 90%
				HothUtils.setPos(chunk, x,y+2,z, getBedrockMaterial(localRand, (int)(256*0.7f))); // 70%
				HothUtils.setPos(chunk, x,y+3,z, getBedrockMaterial(localRand, (int)(256*0.5f))); // 50%
				HothUtils.setPos(chunk, x,y+4,z, getBedrockMaterial(localRand, (int)(256*0.3f))); // 30%
				HothUtils.setPos(chunk, x,y+5,z, getBedrockMaterial(localRand, (int)(256*0.2f))); // 20%
			}
		}
		
		orePopulator.populateWater(new Random(random.nextLong()), chunk, surfaceOffset);

		return chunk;
	}

	public ChunkData generateExtBlockSectionsKamino(World world, Random random, int chunkx, int chunkz, BiomeGrid biomes)
	{
		DagobahOrePopulator orePopulator = new DagobahOrePopulator(WorldGenerator.plugin.getHeight());
		
		/*
		int mSTONE_id = MaterialManager.toID(Material.STONE);
		int mDIRT_id = MaterialManager.toID(Material.DIRT);
		int mGRASS_id = MaterialManager.toID(Material.GRASS);
		int mAIR_id = MaterialManager.toID(Material.AIR);
		Material mAIR = Material.AIR;
		Material mLAVA = Material.LAVA;
		*/
		
		if(this.noiseGenerator==null)
		{
			this.noiseGenerator = new NoiseGenerator(world);
		}
		
		int surfaceOffset = ConfigManager.getWorldSurfaceoffset(WorldGenerator.plugin, world);
		
		Random localRand = new Random(chunkx*chunkz);
		
		int vsegs = WorldGenerator.plugin.getHeight() / 16;
		ChunkData chunk = createChunkData(world);
		
		for(int z=0;z<16;z++)
		{
			for(int x=0;x<16;x++)
			{
				int rx = chunkx*16 + x;
				int rz = chunkz*16 + z;
				
				Biome biome = biomes.getBiome(x, z);
				float factor = 1.0f;
				if(biome.equals(Biome.DESERT_HILLS))
				{
					factor = 2.0f;
				}
				else if(biome.equals(Biome.MOUNTAINS))
				{
					factor = 3.0f;
				}
				else if(biome.equals(Biome.WOODED_HILLS))
				{
					factor = 2.5f;
				}
				else if(biome.equals(Biome.SNOWY_MOUNTAINS))
				{
					factor = 3.0f;
				}
				else if(biome.equals(Biome.SNOWY_TUNDRA))
				{
					factor = 0.5f;
				}
				else if(biome.equals(Biome.FROZEN_OCEAN))
				{
					factor = 1.3f;
				}
				else if(biome.equals(Biome.FROZEN_RIVER))
				{
					factor = 0.3f;
				}
				else if(biome.equals(Biome.JUNGLE_HILLS))
				{
					factor = 1.6f;
				}
				else if(biome.equals(Biome.MUSHROOM_FIELDS))
				{
					factor = 2.0f;
				}
				else if(biome.equals(Biome.MUSHROOM_FIELD_SHORE))
				{
					factor = 1.0f;
				}
				else if(biome.equals(Biome.PLAINS))
				{
					factor = 0.5f;
				}
				else if(biome.equals(Biome.RIVER))
				{
					factor = 0.1f;
				}
				else if(biome.equals(Biome.MOUNTAIN_EDGE))
				{
					factor = 2.8f;
				}
				else if(biome.equals(Biome.TAIGA_HILLS))
				{
					factor = 2.0f;
				}
				// BEDROCK Layer
				int y = 0;
				HothUtils.setPos(chunk, x,y,z,Material.BEDROCK);
				HothUtils.setPos(chunk, x,y+1,z, getBedrockMaterial(localRand, (int)(256*0.9f))); // 90%
				HothUtils.setPos(chunk, x,y+2,z, getBedrockMaterial(localRand, (int)(256*0.7f))); // 70%
				HothUtils.setPos(chunk, x,y+3,z, getBedrockMaterial(localRand, (int)(256*0.5f))); // 50%
				HothUtils.setPos(chunk, x,y+4,z, getBedrockMaterial(localRand, (int)(256*0.3f))); // 30%
				HothUtils.setPos(chunk, x,y+5,z, getBedrockMaterial(localRand, (int)(256*0.2f))); // 20%
			}
		}
		
		orePopulator.populateWater(new Random(random.nextLong()), chunk, surfaceOffset);

		return chunk;
	}

	private Material getBedrockMaterial(Random localRand, int limit)
	{
		return this.getBedrockMaterial(localRand, limit, Material.STONE);
	}

	
	private Material getBedrockMaterial(Random localRand, int limit, Material material)
	{
		int ran = localRand.nextInt() & 0xff;
		if(ran>limit)
		{
			return material;
		}
		return Material.BEDROCK;
	}
	
	@Override
	public List<BlockPopulator> getDefaultPopulators(World world)
	{
	
			WorldType worldType = this.getWorldType(); 
			int height = WorldGenerator.plugin.getHeight();
			
			switch(worldType)
			{
			case TATOOINE:
			{
				List<BlockPopulator> list = new ArrayList<BlockPopulator>(1);
				list.add(new TatooineSarlaccPopulator(height));
				//list.add(new TatooineOrePopulator(height));
				list.add(new TatooinePopulator(height)); // Must be the last populator. Sets biome.
				return list;
			}
			case DAGOBAH:
			{
				List<BlockPopulator> list = new ArrayList<BlockPopulator>(1);
				list.add(new DagobahMushroomHutPopulator(height));
				list.add(new DagobahGrassPopulator(height));
				list.add(new DagobahTemplePopulator(height));
				list.add(new DagobahTreeHutPopulator(height));
				list.add(new DagobahRootPopulator(height));
				list.add(new DagobahSmallTreePopulator(height));
				list.add(new DagobahHugeTreePopulator(height));
				list.add(new DagobahSpiderForestPopulator(height));
				//list.add(new DagobahOrePopulator(height));
				list.add(new DagobahPopulator(height)); // Must be the last populator. Sets biome.
				return list;
			}
			case MUSTAFAR:
			{
				List<BlockPopulator> list = new ArrayList<BlockPopulator>(1);
				list.add(new MustafarTemplePopulator(height));
				list.add(new MustafarLavaFountainPopulator(height));
				return list;
			}
			case KASHYYYK:
			{
				List<BlockPopulator> list = new ArrayList<BlockPopulator>(1);
				return list;
			}
			case HOTH:
			default: // default to hoth
			{
				List<BlockPopulator> list = new ArrayList<BlockPopulator>(1);
				list.add(new HothPopulator(height)); // Must be the last populator. Sets biome.
				return list;
			}
		}
	}

	@Override
	public Location getFixedSpawnLocation(World world, Random random)
	{
		return new Location(world,8,world.getHighestBlockYAt(8, 8)+2,8);
	}
}
