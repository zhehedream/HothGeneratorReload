package biz.orgin.minecraft.hothgenerator;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Random;
import java.util.Vector;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

import biz.orgin.minecraft.hothgenerator.schematic.LoadedSchematic;
import biz.orgin.minecraft.hothgenerator.schematic.Schematic;

public class MustafarBaseGenerator implements Serializable
{
	private static final long serialVersionUID = 3089690545731812260L;

	private static LoadedSchematic landing = null;
	private static LoadedSchematic[] mains = null;
	private static LoadedSchematic[][] walkways = null;
	
	public enum PartType
	{
		LANDING, WALKWAY, MAIN
	}

	public static void generateBase(World world, HothGeneratorPlugin plugin, Random random, int chunkx, int chunkz)
	{
		
		int rarity = ConfigManager.getStructureMustafarBaseRarity(plugin, world);

		
		try
		{
			if(landing==null)
			{
				landing = new LoadedSchematic(plugin.getResource("schematics/mustafar_landing.sm"),"mustafar_landing");
			}
			
			if(mains==null)
			{
				mains = new LoadedSchematic[5];
				mains[0] = new LoadedSchematic(plugin.getResource("schematics/mustafar_main1.sm"),"mustafar_main1");
				mains[1] = new LoadedSchematic(plugin.getResource("schematics/mustafar_main2.sm"),"mustafar_main2");
				mains[2] = new LoadedSchematic(plugin.getResource("schematics/mustafar_main3.sm"),"mustafar_main3");
				mains[3] = new LoadedSchematic(plugin.getResource("schematics/mustafar_main4.sm"),"mustafar_main4");
				mains[4] = new LoadedSchematic(plugin.getResource("schematics/mustafar_main5.sm"),"mustafar_main5");
			}
			
			if(walkways==null) // All directions, walkways
			{
				walkways = new LoadedSchematic[16][];
				LoadedSchematic nesw1 = new LoadedSchematic(plugin.getResource("schematics/mustafar_nesw1.sm"),"mustafar_nesw1");
				LoadedSchematic nesw2 = new LoadedSchematic(plugin.getResource("schematics/mustafar_nesw2.sm"),"mustafar_nesw2");
				LoadedSchematic esw1 = new LoadedSchematic(plugin.getResource("schematics/mustafar_esw1.sm"),"mustafar_esw1");
				LoadedSchematic sw1 = new LoadedSchematic(plugin.getResource("schematics/mustafar_sw1.sm"),"mustafar_sw1");
				LoadedSchematic e1 = new LoadedSchematic(plugin.getResource("schematics/mustafar_e1.sm"),"mustafar_e1");
				LoadedSchematic e2 = new LoadedSchematic(plugin.getResource("schematics/mustafar_e2.sm"),"mustafar_e2");
				LoadedSchematic ew1 = new LoadedSchematic(plugin.getResource("schematics/mustafar_ew1.sm"),"mustafar_ew1");
				LoadedSchematic ew2 = new LoadedSchematic(plugin.getResource("schematics/mustafar_ew2.sm"),"mustafar_ew2");
				LoadedSchematic ew3 = new LoadedSchematic(plugin.getResource("schematics/mustafar_ew3.sm"),"mustafar_ew3");
				LoadedSchematic ew4 = new LoadedSchematic(plugin.getResource("schematics/mustafar_ew4.sm"),"mustafar_ew4");
				walkways[0x0]=null;
				walkways[0x1]=new LoadedSchematic[2];
				walkways[0x1][0] = e1.cloneRotate(0, "mustafar_w1");
				walkways[0x1][1] = e2.cloneRotate(0, "mustafar_w2");
				walkways[0x2]=new LoadedSchematic[2];
				walkways[0x2][0] = e1.cloneRotate(3, "mustafar_s1");
				walkways[0x2][1] = e2.cloneRotate(3, "mustafar_s2");
				walkways[0x3]=new LoadedSchematic[1];
				walkways[0x3][0] = sw1;
				walkways[0x4]=new LoadedSchematic[2];
				walkways[0x4][0] = e1;
				walkways[0x4][1] = e2;
				walkways[0x5]=new LoadedSchematic[4];
				walkways[0x5][0] = ew1;
				walkways[0x5][1] = ew2;
				walkways[0x5][2] = ew3;
				walkways[0x5][3] = ew4;
				walkways[0x6]=new LoadedSchematic[1];
				walkways[0x6][0] = sw1.cloneRotate(1, "mustafar_es1");
				walkways[0x7]=new LoadedSchematic[1];
				walkways[0x7][0] = esw1;
				walkways[0x8]=new LoadedSchematic[2];
				walkways[0x8][0] = e1.cloneRotate(1, "mustafar_n1");
				walkways[0x8][1] = e2.cloneRotate(1, "mustafar_n2");
				walkways[0x9]=new LoadedSchematic[1];
				walkways[0x9][0] = sw1.cloneRotate(3, "mustafar_wn1");
				walkways[0xa]=new LoadedSchematic[4];
				walkways[0xa][0] = ew1.cloneRotate(1, "mustafar_ns1");
				walkways[0xa][1] = ew2.cloneRotate(1, "mustafar_ns2");
				walkways[0xa][2] = ew3.cloneRotate(1, "mustafar_ns3");
				walkways[0xa][3] = ew4.cloneRotate(1, "mustafar_ns4");
				walkways[0xb]=new LoadedSchematic[1];
				walkways[0xb][0] = esw1.cloneRotate(3, "mustafar_swn1");
				walkways[0xc]=new LoadedSchematic[1];
				walkways[0xc][0] = sw1.cloneRotate(0, "mustafar_ne1");
				walkways[0xd]=new LoadedSchematic[1];
				walkways[0xd][0] = esw1.cloneRotate(0, "mustafar_ne1");
				walkways[0xe]=new LoadedSchematic[1];
				walkways[0xe][0] = esw1.cloneRotate(1, "mustafar_nes1");
				walkways[0xf]=new LoadedSchematic[2];
				walkways[0xf][0] = nesw1;
				walkways[0xf][1] = nesw2;
			}

			int doit = random.nextInt(1001*rarity);
			
			if(chunkx==0 && chunkz==0 ||
					(rarity!=0 && doit == 426)
					)
			{
				MustafarBaseGenerator generator = new MustafarBaseGenerator();
				Base base = generator.getBase(random, world, chunkx, chunkz);
				plugin.addTask(new PlaceBase(world, base));
			}
			
		}
		catch(IOException e)
		{
			plugin.logMessage("Error while loading internal schematics: " + e.getMessage());
		}
	}
	
	private Base getBase(Random random, World world, int chunkx, int chunkz)
	{
		return new Base(random, chunkx, chunkz);
	}
	
	public class Base implements Serializable
	{
		private static final long serialVersionUID = 356425047483008459L;
		
		private HashSet<Part> parts;
		private int chunkx;
		private int chunkz;
		private Random random;
		
		public Base(Random random, int chunkx, int chunkz)
		{
			this.parts = new HashSet<Part>();
			this.chunkx = chunkx;
			this.chunkz = chunkz;
			this.random = random;
			
			
			this.generate();
		}
		
		private void generate()
		{
			this.add(new Part(PartType.LANDING, 0xf, 0, 0));
			
			int size = 4+random.nextInt(3);
			int maincnt = 2+random.nextInt(size*2);
			
			for(int i=0;i<maincnt;i++)
			{
				boolean added = false;
				while(!added)
				{
					int x = random.nextInt(size*2)-size;
					int z = random.nextInt(size*2)-size;
					Part newPart = new Part(PartType.MAIN, 0xf, x, z);
					Part oldPart = this.getPart(x, z);
					if(oldPart==null)
					{
						this.add(newPart);
						added = true;
					}
				}
			}
			

			Part[] mainParts = this.getParts(PartType.MAIN);
			
			for(int i=0;i<mainParts.length;i++)
			{
				Part part = mainParts[i];
				
				int x = part.getX();
				int z = part.getZ();
				
				this.addWalkWay(x, z);
			}
			
			this.fixWalkWays();
			this.addEnds();
			this.fixMains();
			this.fixLandings();

		}
		
		
		private void addWalkWay(int x, int z)
		{
			int closest = this.getClosest(x, z);
			
			switch(closest)
			{
			case 0:
				Part pn = this.getPart(x, z-1);
				if(pn==null)
				{
					this.add(new Part(PartType.WALKWAY, 0xf, x, z-1));
					this.addWalkWay(x,  z-1);
				}
				break;
			case 1:
				Part pe = this.getPart(x+1, z);
				if(pe==null)
				{
					this.add(new Part(PartType.WALKWAY, 0xf, x+1, z));
					this.addWalkWay(x+1,  z);
				}
				break;
			case 2:
				Part ps = this.getPart(x, z+1);
				if(ps==null)
				{
					this.add(new Part(PartType.WALKWAY, 0xf, x, z+1));
					this.addWalkWay(x,  z+1);

				}
				break;
			case 3:
				Part pw = this.getPart(x-1, z);
				if(pw==null)
				{
					this.add(new Part(PartType.WALKWAY, 0xf, x-1, z));
					this.addWalkWay(x-1,  z);

				}
				break;
			}
		}

		private void fixWalkWays()
		{
			Part[] walkWays = this.getParts(PartType.WALKWAY);
			this.fixDirections(walkWays, false);
		}
		
		private void fixMains()
		{
			Part[] walkWays = this.getParts(PartType.MAIN);
			this.fixDirections(walkWays, true);
		}

		private void fixLandings()
		{
			Part[] walkWays = this.getParts(PartType.LANDING);
			this.fixDirections(walkWays, true);
		}
		
		private void fixDirections(Part[] parts, boolean verify)
		{
			
			for(int i=0;i<parts.length;i++)
			{
				int direction = 0;
				Part part = parts[i];
				int x = part.getX();
				int z = part.getZ();
				
				Part north = this.getPart(x, z-1);
				Part east  = this.getPart(x+1, z);
				Part south = this.getPart(x, z+1);
				Part west  = this.getPart(x-1, z);
				
				if(verify)
				{
					if(north!=null && (
							(north.getPartType()==PartType.WALKWAY && (north.getDirection()&0x2)!=0)
						||
							(north.getPartType()!=PartType.WALKWAY)))
						direction=direction+0x8;
					
					if(east!=null && (
							(east.getPartType()==PartType.WALKWAY && (east.getDirection()&0x1)!=0)
						||
							(east.getPartType()!=PartType.WALKWAY)))
						direction=direction+0x4;

					if(south!=null && (
							(south.getPartType()==PartType.WALKWAY && (south.getDirection()&0x8)!=0)
						||
							(south.getPartType()!=PartType.WALKWAY)))
						direction=direction+0x2;
					
					if(west!=null && (
							(west.getPartType()==PartType.WALKWAY && (west.getDirection()&0x4)!=0)
						||
							(west.getPartType()!=PartType.WALKWAY)))
						direction=direction+0x1;
				}
				else
				{
					if(north!=null) direction=direction+0x8;
					if(east!=null)  direction=direction+0x4;
					if(south!=null) direction=direction+0x2;
					if(west!=null)  direction=direction+0x1;
				}
				
				part.setDirection(direction);
			}
		}
		
		private void addEnds()
		{
			Part[] mains = this.getParts(PartType.MAIN);
			
			for(int i=0;i<mains.length;i++)
			{
				Part part = mains[i];
				int x = part.getX();
				int z = part.getZ();
				
				Part north = this.getPart(x, z-1);
				Part east  = this.getPart(x+1, z);
				Part south = this.getPart(x, z+1);
				Part west  = this.getPart(x-1, z);
				
				if(north==null)
				{
					this.add(new Part(PartType.WALKWAY, 0x2, x, z-1));
				}
				if(east==null)
				{
					this.add(new Part(PartType.WALKWAY, 0x1, x+1, z));
				}
				if(south==null)
				{
					this.add(new Part(PartType.WALKWAY, 0x8, x, z+1));
				}
				if(west==null)
				{
					this.add(new Part(PartType.WALKWAY, 0x4, x-1, z));
				}

			}

		}
		
		private int getClosest(int x, int z)
		{
			double north = Math.sqrt(x*x + (z-1)*(z-1));
			double east = Math.sqrt((x+1)*(x+1) + z*z);
			double south = Math.sqrt(x*x + (z+1)*(z+1));
			double west = Math.sqrt((x-1)*(x-1) + z*z);
			
			if(north<=east && north<=south && north<=west)
			{
				return 0;
			}

			if(east<=north && east<=south && east<=west)
			{
				return 1;
			}

			if(south<=east && south<=north && south<=west)
			{
				return 2;
			}

			if(west<=north && west<=south && west<=east)
			{
				return 3;
			}
			
			return 0;
		}

		public Part getPart(int x, int z)
		{
			Part[] parts = this.parts.toArray(new Part[this.parts.size()]);
			for(int i=0;i<parts.length;i++)
			{
				Part part = parts[i];
				if(part.getX()==x && part.getZ()==z)
				{
					return part;
				}
			}
			
			return null;
		}
		
		public Part[] getParts(PartType partType)
		{
			Vector<Part> newParts = new Vector<Part>();
			
			Part[] parts = this.parts.toArray(new Part[this.parts.size()]);
			
			for(int i=0;i<parts.length;i++)
			{
				Part part = parts[i];
				if(part.getPartType() == partType)
				{
					newParts.add(part);
				}
			}
			
			return newParts.toArray(new Part[newParts.size()]);
		}
		
		public void add(Part part)
		{
			this.parts.add(part);
		}
		
		public boolean hasPart(Part part)
		{
			return this.parts.contains(part);
		}
		
		public int getChunkX()
		{
			return this.chunkx;
		}

		public int getChunkZ()
		{
			return this.chunkz;
		}
		
		public Random getRandom()
		{
			return this.random;
		}
		
		public Part[] getParts()
		{
			return this.parts.toArray(new Part[this.parts.size()]);
		}
		
		public String toString()
		{
			String direction = "0╕╥╗╘═╔╦╨╝║╣╚╩╠╬";
			String mainc = "0123456789ABCDEF";

			char [][] array = new char[29][29];
			Part[] parts = this.getParts();
			
			StringBuffer buffer = new StringBuffer();
			StringBuffer postBuffer = new StringBuffer();
			for(int z=0;z<29;z++)
			{
				for(int x=0;x<29;x++)
				{
					array[z][x]='-';
				}
			}
			
			char mainCnt = 0;
			
			for(int i=0;i<parts.length;i++)
			{
				Part part = parts[i];
				int x = part.getX();
				int z = part.getZ();
				
				switch(part.getPartType())
				{
				case WALKWAY:
					array[z+14][x+14] = direction.charAt(part.direction);
					break;
				case LANDING:
					array[z+14][x+14] = 'L';
					break;
				case MAIN:
					array[z+14][x+14] = mainc.charAt(mainCnt&0xf);//'O';
					postBuffer.append(""+ mainc.charAt(mainCnt&0xf) + ": ");
					if((part.direction&0x8)>0)
					{
						postBuffer.append("N ");
					}
					else
					{
						postBuffer.append("  ");
					}
					if((part.direction&0x4)>0)
					{
						postBuffer.append("E ");
					}
					else
					{
						postBuffer.append("  ");
					}
					if((part.direction&0x2)>0)
					{
						postBuffer.append("S ");
					}
					else
					{
						postBuffer.append("  ");
					}
					if((part.direction&0x1)>0)
					{
						postBuffer.append("W ");
					}
					else
					{
						postBuffer.append("  ");
					}
					postBuffer.append("\n");
					mainCnt++;
					break;
				}
				
			}
			
			for(int z=0;z<29;z++)
			{
				for(int x=0;x<29;x++)
				{
					buffer.append(array[z][x]);
				}
				buffer.append('\n');
			}
			
			return buffer.toString() + postBuffer.toString();
		}
	}
	
	public class Part implements Serializable
	{
		private static final long serialVersionUID = 5368918232837794235L;

		private int direction;
		private int x;
		private int z;
		private PartType partType;
		
		public Part(PartType partType, int direction, int x, int z)
		{
			this.partType = partType;
			
			this.direction = direction & 0xf;
			
			this.x = x;
			this.z = z;
			
		}
		
		public int getDirection()
		{
			return this.direction;
		}
		
		public void setDirection(int direction)
		{
			this.direction = direction&0xf;
		}
		
		public boolean isNorth()
		{
			return (this.direction & 0x8) !=0;
		}

		public boolean isEast()
		{
			return (this.direction & 0x4) !=0;
		}

		public boolean isSouth()
		{
			return (this.direction & 0x2) !=0;
		}

		public boolean isWest()
		{
			return (this.direction & 0x1) !=0;
		}
		
		public boolean isPos(int x, int z)
		{
			return this.x == x && this.z == z;
		}
		
		public int getX()
		{
			return this.x;
		}
		
		public int getZ()
		{
			return this.z;
		}
		
		public PartType getPartType()
		{
			return this.partType;
		}

		public boolean equals(Part other)
		{
			return this.x == other.getX() && this.z == other.getZ();
		}

		@Override
		public int hashCode()
		{
			return this.x * 0x10000 + this.z;
		}
	}
	
	static class PlaceBase extends HothRunnable
	{
		private static final long serialVersionUID = 257999018934375166L;
		
		private Base base;

		public PlaceBase(World world, Base base)
		{
			this.setName("PlaceMustafarBase");
			this.setWorld(world);
			this.setPlugin(null);
			this.base = base;
		}
		
		@Override
		public String getParameterString() {
			return "";
		}

		@Override
		public void run()
		{
			HothGeneratorPlugin plugin = this.getPlugin();
			World world = this.getWorld();
			
			int chunkx = this.base.getChunkX();
			int chunkz = this.base.getChunkZ();
			Random random = this.base.getRandom();
			
			int sx = chunkx*16 - 8;
			int sz = chunkz*16 - 8;
			
			// calculate height based on average height of all mains and the landing spot
			Part[] mains = this.base.getParts(PartType.MAIN);
			
			int sy = world.getHighestBlockYAt(sx+8, sz+8);
			for(int i=0;i<mains.length;i++)
			{
				Part main = mains[i];
				int tx = main.getX()*17 + sx + 8;
				int tz = main.getZ()*17 + sz + 8;
				sy = sy + this.getHighestBlockYAt(world, tx, tz);
			}
			sy = sy / (mains.length+1);
			
			Part[] parts = this.base.getParts();
			
			for(int i=0;i<parts.length;i++)
			{
				Part part = parts[i];
				Schematic schematic = this.getSchematic(random, part);
				if(schematic!=null)
				{
					PartType partType = part.getPartType();
					int sxx = sx + part.getX()*17;
					int szz = sz + part.getZ()*17;
					HothUtils.placeSchematic(plugin, world, schematic, sxx, sy+21+6, szz, 2, 10);
					
					if(partType==PartType.LANDING || partType==PartType.MAIN)
					{
						// Add root
						this.addRoot(world, sxx, sy+6, szz);
						// Block unused pathways
						if((part.direction&0x8)==0)
						{
							world.getBlockAt(sxx+8, sy+17, szz).setType(Material.NETHER_BRICK_FENCE);
						}
						if((part.direction&0x4)==0)
						{
							world.getBlockAt(sxx+16, sy+17, szz+8).setType(Material.NETHER_BRICK_FENCE);
						}
						if((part.direction&0x2)==0)
						{
							world.getBlockAt(sxx+8, sy+17, szz+16).setType(Material.NETHER_BRICK_FENCE);
						}
						if((part.direction&0x1)==0)
						{
							world.getBlockAt(sxx, sy+17, szz+8).setType(Material.NETHER_BRICK_FENCE);
						}
					}
				}
			}
			
		}
		
		private int getHighestBlockYAt(World world, int x, int z)
		{
			Block block = world.getHighestBlockAt(x, z);
			while(!block.isEmpty())
			{
				block = block.getRelative(BlockFace.UP);
			}
			block = block.getRelative(BlockFace.DOWN);
			
			return block.getY();
		}
		
		private void addRoot(World world, int sx, int sy, int sz)
		{
			int i=0;
			while((sy-i)>10)
			{
				int cnt = 0;
				cnt = this.addRootBlock(cnt, world.getBlockAt(sx+8,   sy-i, sz+8-1));
				cnt = this.addRootBlock(cnt, world.getBlockAt(sx+8-1, sy-i, sz+8  ));
				cnt = this.addRootBlock(cnt, world.getBlockAt(sx+8+1, sy-i, sz+8  ));
				cnt = this.addRootBlock(cnt, world.getBlockAt(sx+8,   sy-i, sz+8+1));
				
				if((sy-i)%2==0)
				{
					cnt = this.addRootBlock(cnt, world.getBlockAt(sx+8-1, sy-i, sz+8-1));
					cnt = this.addRootBlock(cnt, world.getBlockAt(sx+8+1, sy-i, sz+8-1));
					cnt = this.addRootBlock(cnt, world.getBlockAt(sx+8-1, sy-i, sz+8+1));
					cnt = this.addRootBlock(cnt, world.getBlockAt(sx+8+1, sy-i, sz+8+1));
				}
				if(cnt==0)
				{
					break;
				}
				i++;
			}
		}
		
		private int addRootBlock(int cnt, Block block)
		{
			if(block.getType().equals(Material.STONE))
			{
				return cnt;
			}
			
			block.setType(Material.NETHER_BRICKS);
			cnt++;
			return cnt;
		}
		
		private Schematic getSchematic(Random random, Part part)
		{
			switch(part.getPartType())
			{
			case LANDING:
				return landing;
			case WALKWAY:
				int direction = part.getDirection();
				Schematic[] ways = walkways[part.getDirection()];
				if(direction==0x8 || direction==0x4 || direction==0x2 || direction==0x1)
				{
					if(random.nextInt(10)>3)
					{
						return ways[0];
					}
					else
					{
						return ways[1];
					}
				}
				else
				{
					return ways[random.nextInt(ways.length)];
				}
			case MAIN:
				return mains[random.nextInt(mains.length)];
			}
			
			return null;
		}

	}

}
