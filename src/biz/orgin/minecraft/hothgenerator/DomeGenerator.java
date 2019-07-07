package biz.orgin.minecraft.hothgenerator;

import static biz.orgin.minecraft.hothgenerator.DagobahSpiderForestPopulator.isLog;
import java.util.Random;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;

import biz.orgin.minecraft.hothgenerator.schematic.MiniDome;

public class DomeGenerator
{
	private static int PLANTSTEMID = 19;   // Sponge
	private static int PLANTTOPID = 89;    // Glowstone
	private static int FLOORID = 3;        // Dirt
	private static int FLOORRANDOMID = 89; // Glowstone
	

	public static double distance(Position pos1, Position pos2)
	{
		double xd = (pos1.x-pos2.x)*(pos1.x-pos2.x);
		double yd = (pos1.y-pos2.y)*(pos1.y-pos2.y);
		double zd = (pos1.z-pos2.z)*(pos1.z-pos2.z);
		
		double result = Math.sqrt(xd + yd + zd);
		
		return result;
	}

	
	public static void generateDome(HothGeneratorPlugin plugin, World world, Random random, int chunkX, int chunkZ)
	{
		int rarity = ConfigManager.getStructureDomesRarity(plugin, world);
		DomeGenerator.PLANTSTEMID = ConfigManager.getStructureDomesPlantstem(plugin, world);
		DomeGenerator.PLANTTOPID = ConfigManager.getStructureDomesPlanttop(plugin, world);
		DomeGenerator.FLOORID = ConfigManager.getStructureDomesFloor(plugin, world);
		DomeGenerator.FLOORRANDOMID = ConfigManager.getStructureDomesFloorrandom(plugin, world);
		
		if(rarity!=0)
		{
			int doit = random.nextInt(512*rarity);
			if(doit == 336)
			{
				plugin.addTask(new PlaceDome(world, random, chunkX, chunkZ));
			}	
		}
	}
	
	static class PlaceMiniDome extends HothRunnable
	{

		private static final long serialVersionUID = 1500323696570562431L;

		private int x;
		private int y;
		private int z;
		

		@Override
		public String getParameterString() {
			return "x=" + this.x + " y= " + this.y + " z=" + this.z;
		}

		public PlaceMiniDome(World world, int x, int y, int z)
		{
			this.setName("PlaceMiniDome");
			this.setWorld(world);
			this.setPlugin(null);
			this.x = x;
			this.y = y;
			this.z = z;

		}
		
		@Override
		public void run() {
			HothGeneratorPlugin plugin = this.getPlugin();
			World world = this.getWorld();
			HothUtils.placeSchematic(plugin, world, MiniDome.instance, this.x, this.y, this.z, 2, 10);
		}
	}

	static class PlaceDome extends HothRunnable
	{
		private static final long serialVersionUID = -9172096637393548731L;
		private Random random;
		private int chunkx;
		private int chunkz;
		
		
		public String getParameterString() {
			return "chunkx=" + this.chunkx + " chunkz=" + this.chunkz;
		}

		public PlaceDome(World world, Random random, int chunkx, int chunkz)
		{
			this.setName("PlaceDome");
			this.setWorld(world);
			this.setPlugin(null);
			this.random = random;
			this.chunkx = chunkx;
			this.chunkz = chunkz;
		}
		
		private void placeTop(World world, int sx, int sy, int sz)
		{
			Position centerPos = new Position(sx, sy, sz);
			
			int radius = 1+this.random.nextInt(3);

			for(int y=sy-radius;y<=sy+radius;y++)
			{
				for(int z=sz-radius;z<=sz+radius;z++)
				{
					for(int x=sx-radius;x<=sx+radius;x++)
					{
						Position currPos = new Position(x,y,z);
						int dist = (int)Math.ceil(DomeGenerator.distance(centerPos, currPos));
						if(dist==radius)
						{
							Block block = world.getBlockAt(x, y, z);
							Material type = block.getType();
							if(type.equals(Material.AIR))
							{
								BlockState blockState = block.getState();
								blockState.setType(MaterialManager.toMaterial(DomeGenerator.PLANTTOPID)); // Glowstone
								blockState.update(true, false);
							}
						}

					}
				}
			}
		}
		

		@Override
		public void run()
		{	
			HothGeneratorPlugin plugin = this.getPlugin();
			World world = this.getWorld();
			
			// Make sure that domes aren't generated in a non hoth type world.
			if(plugin.getWorldType(world) != WorldType.HOTH)
			{
				return;
			}

			int surfaceOffset = ConfigManager.getWorldSurfaceoffset(plugin, world);
			
			int sx = this.chunkx*16 + random.nextInt(16);
			int sz = this.chunkz*16 + random.nextInt(16);
			int sy = 26 + surfaceOffset;
			
			plugin.logMessage("Placing Dome at " + world.getName() + "," + sx + "," + sy + "," + sz,true);

			int radius = 46;

			Position centerPos = new Position(sx, sy, sz);

			// First generate a tiny .. well .. not so tiny dome

			for(int y = centerPos.y;y<=centerPos.y+radius;y++)
			{
				for(int z =-radius + centerPos.z;z<=radius + centerPos.z;z++)
				{
					for(int x =-radius + centerPos.x;x<=radius + centerPos.x;x++)
					{
						Position currPos = new Position(x,y,z);

						int dist = (int)Math.ceil(DomeGenerator.distance(centerPos, currPos));

						if(dist==radius) // Dome
						{
							Block block = world.getBlockAt(x, y, z);
							Material type = block.getType();
							if(type.equals(Material.ICE)
									|| type.equals(Material.PACKED_ICE)
									|| type.equals(Material.SNOW_BLOCK)
									|| type.equals(Material.SNOW)
									|| type.equals(Material.AIR)
									|| isLog(type)
									|| type.equals(Material.SAND)
									|| type.equals(Material.SANDSTONE)
									|| type.equals(Material.CLAY)
									|| type.equals(Material.GRAVEL))
							{
								BlockState blockState = block.getState();
								blockState.setType(Material.GLASS);
								blockState.update(true, false);
							}
						}
						else if(dist<radius) // Inside dome
						{
							Block block = world.getBlockAt(x, y, z);
							Material type = block.getType();
							if(type.equals(Material.ICE) // Make air
									|| type.equals(Material.PACKED_ICE)
									|| type.equals(Material.SNOW_BLOCK)
									|| type.equals(Material.SNOW)
									|| isLog(type))
							{
								BlockState blockState = block.getState();
								blockState.setType(Material.AIR);
								blockState.update(true, false);
							}
							else if(type.equals(Material.SAND) // Make dirty floor
									|| type.equals(Material.SANDSTONE)
									|| type.equals(Material.TERRACOTTA)
									|| type.equals(Material.CLAY)
									|| type.equals(Material.GRAVEL))
							{
								BlockState blockState = block.getState();
								int glow = this.random.nextInt(40);
								if(glow==5)
								{
									blockState.setType(MaterialManager.toMaterial(DomeGenerator.FLOORRANDOMID)); // Glowstone
								}
								else
								{
									blockState.setType(MaterialManager.toMaterial(DomeGenerator.FLOORID)); // Dirt
								}
								blockState.update(true, false);
							}
						}
						else
						{
							// Ignore, outside dome.
						}
					}
				}
			}
			// Next place the internal dome
			if(ConfigManager.isStructureDomesPlaceminidome(plugin, world))
			{
				plugin.addTask(new PlaceMiniDome(world, sx-8, sy+8, sz-8));
			}
			
			// Next grow some alien plants
			int cnt = this.random.nextInt(20);
			for(int i=0;i<30+cnt;i++)
			{
				int ix = sx+random.nextInt(40*2)-40;
				int iy = 26 + surfaceOffset;
				int iz = sz+random.nextInt(40*2)-40;
				
				Position currPos = new Position(ix,iy,iz);
				double dist = DomeGenerator.distance(centerPos, currPos);
				if(dist<40) // Only grow inside the dome (duh!)
				{
					int len = 16 + random.nextInt(16); // Length of plant
					
					boolean done = false;
					int j=0;
					
					double currx = 0;
					double growx1 = 0.1f;
					double growx2 = (5+random.nextInt(15))/100.0f; // Slope factor
					
					int currix = ix;
					int curriy = iy;
					int curriz = iz;
					
					double angle = Math.atan2(iz-sz, ix-sx);    // Angle towards center
					
					double fx = Math.cos(angle) * (dist/40.0f); // for sloping towards center. Less so the closer to the center
					double fz = Math.sin(angle) * (dist/40.0f); // -||-
					
					while(j<len && !done) // Grow the plant
					{
						currx = currx-growx1;
						
						currix = (int)(currx*fx) + ix;
						curriy = j + iy;
						curriz = (int)(currx*fz) + iz;
						
						Block block = world.getBlockAt(currix, curriy, curriz);
						Material type = block.getType();
						if(type.equals(Material.GLASS) || type.equals(Material.STONE_BRICKS)) // Stop growing if we hit the dome(s)
						{
							done = true;
						}
						else if(type.equals(Material.AIR)) // Only place blocks in the air
						{
							BlockState blockState = block.getState();
							blockState.setType(MaterialManager.toMaterial(DomeGenerator.PLANTSTEMID)); // Default is sponge
							blockState.update(true, false);
						}
						
						growx1 = growx1 + growx1*growx2; // Slope magic
						
						if(growx1>1)
						{
							growx1 = 1;
						}
						
						j++;
					}
					
					if(!done) // Place top piece/flower
					{
						this.placeTop(world, currix, curriy, curriz);
					}
				}
			}
		}
	}
}
