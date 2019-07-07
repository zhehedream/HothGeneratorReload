package biz.orgin.minecraft.hothgenerator;

import java.util.Random;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;

public class SpikeGenerator {


	public static void generateSpikes(HothGeneratorPlugin plugin, World world, Random random, int chunkX, int chunkZ)
	{
		int rarity = ConfigManager.getStructureSpikesRarity(plugin, world);

		if(rarity!=0)
		{
			int place = random.nextInt(500*rarity);

			if(place==45)
			{
				plugin.addTask(new PlaceSpikes(world, random, chunkX, chunkZ));
			}	
		}
	}


	static class PlaceSpikes extends HothRunnable
	{
		private static final long serialVersionUID = -5714001078356727719L;
		private Random random;
		private int chunkx;
		private int chunkz;
		
		public String getParameterString()
		{
			return "chunkx=" + chunkx + " chunkz=" + chunkz;
		}
		
		private double getMin(double n1, double n2, double n3)
		{
			double min = n1;
			if(n2<min) min = n2;
			if(n3<min) min = n3;
			return min;
		}

		private double getMax(double n1, double n2, double n3)
		{
			double max = n1;
			if(n2>max) max = n2;
			if(n3>max) max = n3;
			return max;
		}

		public PlaceSpikes(World world, Random random, int chunkx, int chunkz)
		{
			this.setName("PlaceSpikes");
			this.setPlugin(null);
			this.setWorld(world);
			this.random = random;
			this.chunkx = chunkx;
			this.chunkz = chunkz;
		}

		private int getMethod(double minx, double maxx, double miny, double maxy, double minz, double maxz)
		{
			if(maxx-minx > maxy-miny && maxx-minx > maxz-minz)
			{
				return 0; // x needs most steps
			}

			if(maxy-miny > maxx-minx && maxy-miny > maxz-minz)
			{
				return 1; // y needs most steps
			}

			if(maxz-minz > maxx-minx && maxz-minz > maxy-miny)
			{
				return 2; // z needs most steps
			}

			return 0; // Default to x
		}
		
		private void addPos(World world, Blob blob, int x, int y, int z, int type)
		{
			Block block = world.getBlockAt(x, y, z);
			if(!block.getType().equals(Material.STONE))
			{
				blob.addPosition(new Position(x,y,z,type));
			}
			block = world.getBlockAt(x, y+1, z);
			if(!block.getType().equals(Material.STONE))
			{
				blob.addPosition(new Position(x,y+1,z,type));
			}
			block = world.getBlockAt(x, y-1, z);
			if(!block.getType().equals(Material.STONE))
			{
				blob.addPosition(new Position(x,y-1,z,type));
			}
		}

		@Override
		public void run()
		{
			World world = this.getWorld();
			HothGeneratorPlugin plugin = this.getPlugin();
			
			Blob blob = new Blob(plugin, world, this.getName());
			int iceID = MaterialManager.toID(Material.PACKED_ICE);

			int surfaceOffset = ConfigManager.getWorldSurfaceoffset(plugin, world);

			double basex1 = random.nextInt(24) + this.chunkx * 16;
			double basez1 = random.nextInt(24) + this.chunkz * 16;
			double basey1 = 64 + surfaceOffset - 10;

			double basex2 = random.nextInt(24) + this.chunkx * 16;
			double basez2 = random.nextInt(24) + this.chunkz * 16;
			//float basey2 = 64 + surfaceOffset - 10;

			double basex3 = random.nextInt(48) + this.chunkx * 16;
			double basez3 = random.nextInt(48) + this.chunkz * 16;
			double basey3 = 64 + surfaceOffset + 30 + random.nextInt(14);

			int count = random.nextInt(10)+6;

			for(int i=0;i<count;i++)
			{
				double posx = random.nextInt(48)-24;
				double posz = random.nextInt(48)-24;

				double x1=basex1 + posx + random.nextInt(4) - 2;
				double y1=basey1;
				double z1=basez1 + posz + random.nextInt(4) - 2;
				double x2=basex2 + posx + random.nextInt(4) - 2;
				double y2=basey1;
				double z2=basez2 + posz + random.nextInt(4) - 2;
				double x3=basex3 + posx + random.nextInt(4) - 2;
				double y3=basey3 + random.nextInt(8) - 4;
				double z3=basez3 + posz + random.nextInt(4) - 2;

				// Calculate method to use
				double minx = this.getMin(x1,x2,x3);
				double maxx = this.getMax(x1,x2,x3);
				double miny = this.getMin(y1,y2,y3);
				double maxy = this.getMax(y1,y2,y3);
				double minz = this.getMin(z1,z2,z3);
				double maxz = this.getMax(z1,z2,z3);

				int method = this.getMethod(minx, maxx, miny, maxy, minz, maxz);

				if(method==0) //x
				{

				}
				else if(method==1) //y
				{

					double steps = y3-y1;
					double dx1 = (x3-x1)/steps;
					double dz1 = (z3-z1)/steps;
					double dx2 = (x3-x2)/steps;
					double dz2 = (z3-z2)/steps;
					for(double curry=0;curry<(y3-y1);curry++)
					{
						double currx1 = x1 + dx1 * curry;
						double currz1 = z1 + dz1 * curry;
						double currx2 = x2 + dx2 * curry;
						double currz2 = z2 + dz2 * curry;

						// Time to draw a line
						if(Math.abs(currx1-currx2) > Math.abs(currz1-currz2))
						{
							double startx = currx1;
							double startz = currz1;
							double stopx = currx2;
							double stopz = currz2;

							if(startx>stopx)
							{
								startx = currx2;
								startz = currz2;
								stopx = currx1;
								stopz = currz1;
							}

							double diffz = (stopz-startz) / (stopx-startx);

							for(double currx=0;currx<(stopx-startx);currx++)
							{
								double currz = startz + diffz * currx;
								if(Math.abs(((stopx-startx)/2)-currx)> ((stopx-startx)*0.2))
								{
									this.addPos(world, blob, (int)Math.round(startx+currx), (int)Math.round(y1+curry), (int)Math.round(currz), iceID);
								}
								if(Math.abs(((stopx-startx)/2)-currx)< ((stopx-startx)*0.3))
								{
									this.addPos(world, blob, (int)Math.round(startx+currx), (int)Math.round(y1+curry)+1, (int)Math.round(currz), iceID);
								}
								if(Math.abs(((stopx-startx)/2)-currx)< ((stopx-startx)*0.1))
								{
									this.addPos(world, blob, (int)Math.round(startx+currx), (int)Math.round(y1+curry)+2, (int)Math.round(currz), iceID);
								}
							}
						}
						else
						{
							double startx = currx1;
							double startz = currz1;
							double stopx = currx2;
							double stopz = currz2;

							if(startz>stopz)
							{
								startx = currx2;
								startz = currz2;
								stopx = currx1;
								stopz = currz1;
							}

							double diffx = (stopx-startx) / (stopz-startz);

							for(double currz=0;currz<(stopz-startz);currz++)
							{
								double currx = startx + diffx * currz;
								if(Math.abs(((stopx-startx)/2)-currx)> ((stopx-startx)*0.2))
								{
									this.addPos(world, blob, (int)Math.round(currx), (int)Math.round(y1+curry), (int)Math.round(startz+currz), iceID);
								}
								if(Math.abs(((stopx-startx)/2)-currx)< ((stopx-startx)*0.3))
								{
									this.addPos(world, blob, (int)Math.round(currx), (int)Math.round(y1+curry)+1, (int)Math.round(startz+currz), iceID);
								}
								if(Math.abs(((stopx-startx)/2)-currx)< ((stopx-startx)*0.1))
								{
									this.addPos(world, blob, (int)Math.round(currx), (int)Math.round(y1+curry)+2, (int)Math.round(startz+currz), iceID);
								}
							}
						}
					}

					plugin.logMessage("Placing spike at " + world.getName() + "," + x3 + "," + y3 + "," + z3, true);

				}
				else if(method == 2) // z
				{
					
				}
			}

			blob.instantiate();
		}
	}
}
