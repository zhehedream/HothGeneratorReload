package biz.orgin.minecraft.hothgenerator;

import static biz.orgin.minecraft.hothgenerator.BlockGrowManager.isDoublePlant;
import static biz.orgin.minecraft.hothgenerator.BlockGrowManager.isSapling;
import static biz.orgin.minecraft.hothgenerator.BlockGrowManager.isSmallFlower;
import static biz.orgin.minecraft.hothgenerator.DagobahSmallTreePopulator.isLeaves;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.Plugin;

import biz.orgin.minecraft.hothgenerator.schematic.RotatedSchematic;
import biz.orgin.minecraft.hothgenerator.schematic.Schematic;
import java.util.logging.Level;
import me.zhehe.MagicIdHandler;
import org.bukkit.Bukkit;
import org.bukkit.block.data.BlockData;
import org.bukkit.generator.ChunkGenerator.ChunkData;

/**
 * Generic utility functionality
 * @author orgin
 *
 */
public class HothUtils
{
	
	private static IntSet delays = new IntSet(new int[] {  // Block types to defer until infrastructure is made.
			50,75,76,6,32,37,38,39,40,51,55,26,
			59,31,63,65,66,96,69,77,78,104,105,106,83,89,115,
			93,94,111,127,131,132,140,141,142,143,171,175,78,64});

	public static void placeSchematic(Plugin plugin, World world, Schematic schematic, int x, int y, int z, int lootMin, int lootMax)
	{
		HothUtils.placeSchematic(plugin, world, schematic, x, y, z, lootMin, lootMax, LootGenerator.getLootGenerator(), false);

	}
	public static void placeSchematic(Plugin plugin, World world, Schematic schematic, int x, int y, int z, int lootMin, int lootMax, boolean darken)
	{
		HothUtils.placeSchematic(plugin, world, schematic, x, y, z, lootMin, lootMax, LootGenerator.getLootGenerator(), darken);
	}

	public static void placeSchematic(Plugin plugin, World world, Schematic schematic, int x, int y, int z, int lootMin, int lootMax, LootGenerator lootGenerator)
	{
		HothUtils.placeSchematic(plugin, world, schematic, x, y, z, lootMin, lootMax, lootGenerator, false);
	}
	
	public static void placeSchematic(Plugin plugin, World world, Schematic schematic, int x, int y, int z, int lootMin, int lootMax, LootGenerator lootGenerator, boolean darken)
	{
		int height = schematic.getHeight();
		int length = schematic.getLength();
		int width = schematic.getWidth();
		int[][][] matrix = schematic.getMatrix();
		
		Set<Position>delays = new HashSet<Position>();
		for(int yy=0;yy<height;yy++)
		{
			for(int zz=0;zz<length;zz++)
			{
				for(int xx=0;xx<width;xx++)
				{
					int type = matrix[yy][zz][xx];
					
					if(darken) // Replace lightsources with cobble
					{
						// Glowstone, torch, redstone torch (lit/unlit), redstone lamp (lit/unlit)
						if(type==89 || type==50 || type==75 || type==76 || type==123 || type==124 || type==10)
						{
							type=4; // Turn to cobble
						}
					}
					
					if(type>-1)
					{
						byte data = (byte)matrix[yy][zz][xx+width];
						Block block = world.getBlockAt(x+xx, y-yy, z+zz);
						
						if(type==52) // Spawner, Set some spawner data
						{
							block.setType(Material.SPAWNER);
							CreatureSpawner spawner = (CreatureSpawner)block.getState();
							if(data==0)
							{
								int creature = x%8;
								switch(creature)
								{
								case 0:
								case 1:
									spawner.setSpawnedType(EntityType.SKELETON); //25
									break;
								case 2:
								case 3:
									spawner.setSpawnedType(EntityType.SPIDER); // 25
									break;
								case 4:
								case 5:
								case 6:
								case 7:
								default:
									spawner.setSpawnedType(EntityType.ZOMBIE); // 50
									break;
								}
							}
							else // Schematic wants to set spawner type
							{
								spawner.setSpawnedType(EntityTypeManager.toEntityType(data));
							}
							
							spawner.update(true, false);
						}
						else if(type==54) // Chest, set correct rotation and add some random loot
						{
							block.setType(Material.CHEST);
                                                        
                                                        BlockData data2 = MagicIdHandler.fromId(type, data);
                                                        if(data2 != null) {
                                                            block.setBlockData(data2, false);
                                                        }
							Chest chest = (Chest)block.getState();

							Inventory inv = chest.getInventory();
							lootGenerator.getLootInventory(inv, lootMin, lootMax);
						}
						else if(HothUtils.delays.contains(type)) // Torches and such, delay for later
						{
							Position pos = new Position(block.getX(), block.getY(), block.getZ());
							pos.type = type;
							pos.data = (byte)data;
							delays.add(pos);
						}
						else
						{
							if(type==0)
							{
								data = 0;
							}
							
							//if(data==0)
                                                        if(false)
							{
								//BlockState state = block.getState();
								//state.setType(MaterialManager.toMaterial(type));
                                                                BlockData bd = MagicIdHandler.fromId(type, data);
                                                                block.setBlockData(bd);
								//state.update(true, false);
                                                                //block.setType(MaterialManager.toMaterial(type));
							}
							else
							{
								block.setType(MaterialManager.toMaterial(type));
                                                                if(type == 85 || type == 188 || type == 189 || type == 190 || type == 191 || type == 192 || type == 101) continue; //for fence
                                                                BlockData data2 = MagicIdHandler.fromId(type, data);
                                                                if(data2 != null) {
                                                                    block.setBlockData(data2, false);
                                                                }
								//DataManager.setData(block, (byte)data, false);
							}
						}
					}
					else if(type==-2) // Spawn entity
					{
						Block block = world.getBlockAt(x+xx, y-yy, z+zz);
						block.setType(Material.AIR);
						byte data = (byte)matrix[yy][zz][xx+width];
						
						EntityType entityType = EntityTypeManager.toEntityType(data);
						try
						{
							world.spawnEntity(new Location(world, x+xx, y-yy, z+zz), entityType);
						}
						catch(Exception e)
						{
							// Failed to spawn entity. Generator not ready yet?
						}
					}
				}
			}
		}
		
		Position[] posArray = delays.toArray(new Position[0]);
		
		for(int i=0;i<posArray.length;i++)
		{
			Block block = world.getBlockAt(posArray[i].x, posArray[i].y, posArray[i].z);
			//block.setType(MaterialManager.toMaterial(posArray[i].type));
                        BlockData data2 = MagicIdHandler.fromId(posArray[i].type, posArray[i].data);
                        if(data2 != null) {
                            block.setBlockData(data2, false);
                        }

			//DataManager.setData(block, posArray[i].data);
		}
	}
	
	public static void setPos(ChunkData chunk, int x, int y, int z, Material material)
	{
		//int type = MaterialManager.toID(material);
		//HothUtils.setPos(chunk, x, y, z, material);
            chunk.setBlock(x, y, z, material);
	}
	
	/*public static void setBlock(short[] subchunk, int x, int y, int z, short blkid)
	{
		subchunk[((y) << 8) | (z << 4) | x] = blkid;
	}*/

	/*public static short getPos(short[][] chunk, int x, int y, int z)
	{
		int sub = y/16;
		int rely = y-(sub*16);
		
		if(chunk[sub]==null)
		{
			chunk[sub] = new short[16*16*16];
		}
		
		return HothUtils.getBlock(chunk[sub], x,rely,z);

	}*/
	
	public static void replaceTop(ChunkData chunk, Material from1, Material from2, Material to, int maxy)
	{
		for(int x=0;x<16;x++)
		{
			for(int z=0;z<16;z++)
			{
				int y = HothUtils.getMaxY(chunk, x, z, maxy);
				if(y>0)
				{
					Material old = chunk.getType(x, y, z);
					if(old==from1 || old==from2)
					{
						HothUtils.setPos(chunk, x, y, z, to);
					}
				}
			}
		}
	}
	
	private static int getMaxY(ChunkData chunk, int x, int z, int maxy)
	{
		for(int i=(maxy-1);i>0;i--)
		{
			Material type = HothUtils.getRawPos(chunk, x, i, z); 
			if(type!= Material.AIR)
			{
				return i;
			}
		}
		
		return 0;
	}
	
	private static Material getRawPos(ChunkData chunk, int x, int y, int z)
	{
		//int sub = y/16;
		//int rely = y-(sub*16);
		
		//if(chunk[sub]==null)
		//{
		//	return -1;
		//}
		
		//return HothUtils.getBlock(chunk[sub], x,rely,z);
            return chunk.getType(x, y, z);
	}

	public static short getBlock(short[] subchunk, int x, int y, int z)
	{
		return subchunk[((y) << 8) | (z << 4) | x];
	}

	public static Schematic rotateSchematic(int direction, Schematic schematic)
	{
		Schematic result = null;
		
		if(direction==2) // North
		{
			result = schematic;
		}
		else if(direction==0) // South
		{
			int[][][] source = schematic.getMatrix();
			
			String name = schematic.getName()+"_SOUTH";
			int width = schematic.getWidth();
			int length = schematic.getLength();
			int height = schematic.getHeight();
			int[][][] matrix = new int[height][length][width*2];
			
			for(int y=0;y<height;y++)
			{
				for(int z=0;z<length;z++)
				{
					for(int x=0;x<width;x++)
					{
						int type = source[y][length-z-1][width-x-1];
						int data = source[y][length-z-1][width-x-1+width];
						
						matrix[y][z][x] = type;
						matrix[y][z][x+width] = HothUtils.rotateData(type, data, direction);
								
					}
				}
			}
			
			result = new RotatedSchematic(name, width, length, height, matrix);
		}
		else if(direction==1) // West
		{
			int[][][] source = schematic.getMatrix();
			
			String name = schematic.getName()+"_WEST";
			int width = schematic.getLength();
			int length = schematic.getWidth();
			int height = schematic.getHeight();
			int[][][] matrix = new int[height][length][width*2];
			
			for(int y=0;y<height;y++)
			{
				for(int z=0;z<length;z++)
				{
					for(int x=0;x<width;x++)
					{
						int type = source[y][x][length-z-1]; // source[y][length-z-1][x];
						int data = source[y][x][length-z-1 + length];// source[y][length-z-1][x+width];
						
						matrix[y][z][x] = type;
						matrix[y][z][x+width] = HothUtils.rotateData(type, data, direction);
								
					}
				}
			}
			
			result = new RotatedSchematic(name, width, length, height, matrix);
		}
		else if(direction==3) // East
		{
			int[][][] source = schematic.getMatrix();
			
			String name = schematic.getName()+"_EAST";
			int width = schematic.getLength();
			int length = schematic.getWidth();
			int height = schematic.getHeight();
			int[][][] matrix = new int[height][length][width*2];
			
			for(int y=0;y<height;y++)
			{
				for(int z=0;z<length;z++)
				{
					for(int x=0;x<width;x++)
					{
						int type = source[y][width-x-1][z]; // source[y][length-z-1][x];
						int data = source[y][width-x-1][z+length];// source[y][length-z-1][x+width];
						
						matrix[y][z][x] = type;
						matrix[y][z][x+width] = HothUtils.rotateData(type, data, direction);
								
					}
				}
			}
			
			result = new RotatedSchematic(name, width, length, height, matrix);
		}

		return result;
		
	}
	
	/**
	 * Rotates the data value for the specified type to a new direction.
	 * Something pointing east rotated to the east will point south and so forth.
	 * @param type The type to rotate
	 * @param data The data value to rotate
	 * @param rot The direction to rotate teh data
	 * @return The new direction
	 */
	public static int rotateData(int type, int data, int rot)
	{
		if(type==53 || type==67 || type==108 || type==109 || type==114 || type==128 || type==134 || type==135 || type==136
				 || type==156 || type==163 || type==164) // Stairs
		{
			int upside = data & 0x04;
			int direction = data & 0x03;
			switch(rot)
			{
			case 0: // south
				switch(direction)
				{
				case 0: return 1 | upside; // E -> W 
				case 1: return 0 | upside; // W -> E
				case 2: return 3 | upside; // S -> N
				case 3: return 2 | upside; // N -> S
				}
			case 2: // north
				switch(direction)
				{
				case 0: return 0 | upside; // E -> E 
				case 1: return 1 | upside; // W -> W
				case 2: return 2 | upside; // S -> S
				case 3: return 3 | upside; // N -> N
				}
			case 1: // west
				switch(direction)
				{
				case 0: return 3 | upside; // E -> N
				case 1: return 2 | upside; // W -> S
				case 2: return 0 | upside; // S -> E
				case 3: return 1 | upside; // N -> W
				}
			case 3: // east
				switch(direction)
				{
				case 0: return 2 | upside; // E -> S
				case 1: return 3 | upside; // W -> N
				case 2: return 1 | upside; // S -> W
				case 3: return 0 | upside; // N -> E
				}
			}
		}
		else if(type==86 || type==91) // pumpkins
		{
			int direction = data & 0x03;
			switch(rot)
			{
			case 0: // south
				switch(direction)
				{
				case 0: return 2; // N -> S 
				case 1: return 3; // E -> W
				case 2: return 0; // S -> N
				case 3: return 1; // W -> E
				}
			case 2: // north
				switch(direction)
				{
				case 0: return 0; // N -> N 
				case 1: return 1; // E -> E
				case 2: return 2; // S -> S
				case 3: return 3; // W -> W
				}
			case 1: // west
				switch(direction)
				{
				case 0: return 3; // N -> W
				case 1: return 0; // E -> N
				case 2: return 1; // S -> E
				case 3: return 2; // W -> S
				}
			case 3: // east
				switch(direction)
				{
				case 0: return 1; // N -> E
				case 1: return 2; // E -> S
				case 2: return 3; // S -> W
				case 3: return 0; // W -> N
				}
			}
		}
		else if(type==50)  // torch
		{
			switch(rot)
			{
			case 2: // North
				switch(data)
				{
				case 1: return 1; // W -> W
				case 2: return 2; // E -> E
				case 3: return 3; // N -> N
				case 4: return 4; // S -> S
				}
			case 0: // South
				switch(data)
				{
				case 1: return 2; // W -> E
				case 2: return 1; // E -> W
				case 3: return 4; // N -> S
				case 4: return 3; // S -> N
				}
			case 1: // West
				switch(data)
				{
				case 1: return 4; // W -> S
				case 2: return 3; // E -> N
				case 3: return 1; // N -> W
				case 4: return 2; // S -> E
				}
			case 3: // East
				switch(data)
				{
				case 1: return 3; // W -> N
				case 2: return 4; // E -> S
				case 3: return 2; // N -> E
				case 4: return 1; // S -> W
				}
			}
		}
		else if(type==54 || type==23 || type==158 || type==61 || type==130 || type==62 || type==65 || type==33 || type==29) // chest, enderchest, furnace, dropper etc
		{
			// 33 - Piston base
			// 29 - Sticky piston base
			switch(rot)
			{
			case 2: // North
				switch(data)
				{
				case 2: return 2; // N
				case 3: return 3; // S
				case 4: return 4; // W
				case 5: return 5; // E
				}
			case 0: // South
				switch(data)
				{
				case 2: return 3; // N -> S
				case 3: return 2; // S -> N
				case 4: return 5; // W -> E
				case 5: return 4; // E -> W
				}
			case 1: // West
				switch(data)
				{
				case 2: return 4; // N -> W
				case 3: return 5; // S -> E
				case 4: return 3; // W -> S
				case 5: return 2; // E -> N
				}
			case 3: // East
				switch(data)
				{
				case 2: return 5; // N -> E
				case 3: return 4; // S -> W
				case 4: return 2; // W -> N
				case 5: return 3; // E -> S
				}
			}
		}
		else if(type==106) // Vines
		{
			switch(rot)
			{
			case 2: // North
				switch(data)
				{
				case 1: return 1; // N
				case 2: return 2; // E
				case 4: return 4; // S
				case 8: return 8; // W
				}
			case 0: // South
				switch(data)
				{
				case 1: return 4; // N -> S
				case 2: return 8; // E -> W
				case 4: return 1; // S -> N
				case 8: return 2; // W -> E
				}
			case 1: // West
				switch(data)
				{
				case 1: return 8; // N -> W
				case 2: return 1; // E -> N
				case 4: return 2; // S -> E
				case 8: return 4; // W -> S
				}
			case 3: // East
				switch(data)
				{
				case 1: return 2; // N -> E
				case 2: return 4; // E -> S
				case 4: return 8; // S -> W
				case 8: return 1; // W -> N
				}
			}
		}
		else if(type==26) // Bed
		{
			int part = data & 0x08;
			int dir = data &0x07;
			
			switch(rot)
			{
			case 2: // North
				switch(dir)
				{
				case 0: return 0 | part; // N
				case 1: return 1 | part; // E
				case 2: return 2 | part; // S
				case 3: return 3 | part; // W
				}
			case 0: // South
				switch(dir)
				{
				case 0: return 2 | part; // N -> S
				case 1: return 3 | part; // E -> W
				case 2: return 0 | part; // S -> N
				case 3: return 1 | part; // W -> E
				}
			case 1: // West
				switch(dir)
				{
				case 0: return 3 | part; // N -> W
				case 1: return 0 | part; // E -> N
				case 2: return 1 | part; // S -> E
				case 3: return 2 | part; // W -> S
				}
			case 3: // East
				switch(dir)
				{
				case 0: return 1 | part; // N -> E
				case 1: return 2 | part; // E -> S
				case 2: return 3 | part; // S -> W
				case 3: return 0 | part; // W -> N
				}
			}
		}
		else if(type==17 || type==162) // Logs
		{
			int typ = data & 0x03;
			int dir = data &0x0c;
			
			switch(rot)
			{
			case 2: // North
				switch(dir)
				{
				case  0: return  0 | typ; // UD -> UD
				case  4: return  4 | typ; // EW -> EW
				case  8: return  8 | typ; // NS -> NS
				case 12: return 12 | typ; // A -> A
				}
			case 0: // South
				switch(dir)
				{
				case  0: return  0 | typ; // UD -> UD
				case  4: return  4 | typ; // EW -> EW
				case  8: return  8 | typ; // NS -> NS
				case 12: return 12 | typ; // A -> A
				}
			case 1: // West
				switch(dir)
				{
				case  0: return  0 | typ; // UD -> UD
				case  4: return  8 | typ; // EW -> NS
				case  8: return  4 | typ; // NS -> EW
				case 12: return 12 | typ; // A -> A
				}
			case 3: // East
				switch(dir)
				{
				case  0: return  0 | typ; // UD -> UD
				case  4: return  8 | typ; // EW -> NS
				case  8: return  4 | typ; // NS -> EW
				case 12: return 12 | typ; // A -> A
				}
			}
		}
		else if(type==99 || type==100) // Huge mushrooms
		{
			switch(rot)
			{
			case 2: // North
				switch(data)
				{
				case  0: return  0;
				case  1: return  1;
				case  2: return  2;
				case  3: return  3;
				case  4: return  4;
				case  5: return  5;
				case  6: return  6;
				case  7: return  7;
				case  8: return  8;
				case  9: return  9;
				case 10: return 10;
				case 14: return 14;
				case 15: return 15;
				}
			case 0: // South
				switch(data)
				{
				case  0: return  0;
				case  1: return  9;
				case  2: return  8;
				case  3: return  7;
				case  4: return  6;
				case  5: return  5;
				case  6: return  4;
				case  7: return  3;
				case  8: return  2;
				case  9: return  1;
				case 10: return 10;
				case 14: return 14;
				case 15: return 15;
				}
			case 1: // West
				switch(data)
				{
				case  0: return  0;
				case  1: return  7;
				case  2: return  4;
				case  3: return  1;
				case  4: return  8;
				case  5: return  5;
				case  6: return  2;
				case  7: return  9;
				case  8: return  6;
				case  9: return  3;
				case 10: return 10;
				case 14: return 14;
				case 15: return 15;
				}
			case 3: // East
				switch(data)
				{
				case  0: return  0;
				case  1: return  3;
				case  2: return  6;
				case  3: return  9;
				case  4: return  2;
				case  5: return  5;
				case  6: return  8;
				case  7: return  1;
				case  8: return  4;
				case  9: return  7;
				case 10: return 10;
				case 14: return 14;
				case 15: return 15;
				}
			}
		}
		else if(type==93 || type==94) // Repeater
		{
			int delay = data & 0x0c;
			int dir = data &0x03;
			
			switch(rot)
			{
			case 2: // North
				switch(dir)
				{
				case 0: return 0 | delay; // N
				case 1: return 1 | delay; // E
				case 2: return 2 | delay; // S
				case 3: return 3 | delay; // W
				}
			case 0: // South
				switch(dir)
				{
				case 0: return 2 | delay; // N -> S
				case 1: return 3 | delay; // E -> W
				case 2: return 0 | delay; // S -> N
				case 3: return 1 | delay; // W -> E
				}
			case 1: // West
				switch(dir)
				{
				case 0: return 3 | delay; // N -> W
				case 1: return 0 | delay; // E -> N
				case 2: return 1 | delay; // S -> E
				case 3: return 2 | delay; // W -> S
				}
			case 3: // East
				switch(dir)
				{
				case 0: return 1 | delay; // N -> E
				case 1: return 2 | delay; // E -> S
				case 2: return 3 | delay; // S -> W
				case 3: return 0 | delay; // W -> N
				}
			}

		}


			
		
		
		return data;
	}
	
	public static Blob getUndoBlob(HothGeneratorPlugin plugin, World world, Schematic schematic, int x, int y, int z)
	{
		Blob blob = new Blob(plugin, world, "Undo");
		int height = schematic.getHeight();
		int length = schematic.getLength();
		int width = schematic.getWidth();
		
		for(int yy=0;yy<height;yy++)
		{
			for(int zz=0;zz<length;zz++)
			{
				for(int xx=0;xx<width;xx++)
				{
					Block block = world.getBlockAt(x+xx, y-yy, z+zz);
					int type = MaterialManager.toID(block.getType());
					Position position = new Position(x+xx, y-yy, z+zz, type);
					position.blockState = block.getState();
					blob.addPosition(position);
				}
			}
		}
		return blob;
	}
	
	public static String schematicToString(Schematic schematic)
	{
		int[][][] source = schematic.getMatrix();
		StringBuffer mySB = new StringBuffer();
		StringBuffer mySB2 = new StringBuffer();
		StringBuffer result = new StringBuffer();
		
		int width = schematic.getWidth();
		int length = schematic.getLength();
		int height = schematic.getHeight();
		
		for(int y=0;y<height;y++)
		{
			result.append("\t{\t// Layer " + y + "\n");
			for(int z=0;z<length;z++)
			{
				for(int x=0;x<width;x++)
				{
					int type = source[y][z][x];
					int data = source[y][z][x+width];
					
					String stype = String.format("%4d", type);
					String sdata;
					if(x==0)
					{
						sdata = String.format("%5d", data);
					}
					else
					{
						sdata = String.format("%3d", data);
					}
					
					if(x>0)
					{
						mySB.append(",");
					}
					mySB.append(stype);
					mySB2.append("," + sdata);
				}
				
				result.append("\t\t{" + mySB.toString() + mySB2.toString() + "}");
				if(z!=length-1)
				{
					result.append(",\n");
				}
				else
				{
					result.append("\n");
				}
				mySB.setLength(0);
				mySB2.setLength(0);
			}
			if(y!=height-1)
			{
			result.append("\t},\n");
			}
			else
			{
				result.append("\t}\n");
			
			}
		}
		
		return result.toString();
	}
	
	public static boolean isTransparent(Block block)
	{
		if(block.isEmpty())
		{
			return true;
		}
		
		Material type = block.getType();
		return 
				type.equals(Material.TALL_GRASS) ||
                                type.equals(Material.GRASS) ||
				type.equals(Material.RED_MUSHROOM) ||
				type.equals(Material.BROWN_MUSHROOM) ||
				isSmallFlower(type) ||
				isSapling(type) ||
				type.equals(Material.COBWEB) ||
				type.equals(Material.TORCH) ||
				type.equals(Material.REDSTONE_TORCH) ||
				type.equals(Material.REDSTONE_WALL_TORCH) ||
				type.equals(Material.GLASS) ||
				type.equals(Material.VINE) ||
				type.equals(Material.LILY_PAD) ||
				isLeaves(type) ||
				isDoublePlant(type)
				;
	}
	
	public static boolean isWatered(Block block)
	{
		return	HothUtils.isWater(block.getRelative(BlockFace.NORTH))
				|| HothUtils.isWater(block.getRelative(BlockFace.EAST))
				|| HothUtils.isWater(block.getRelative(BlockFace.SOUTH))
				|| HothUtils.isWater(block.getRelative(BlockFace.WEST))
				|| HothUtils.isWater(block.getRelative(BlockFace.NORTH_EAST))
				|| HothUtils.isWater(block.getRelative(BlockFace.NORTH_WEST))
				|| HothUtils.isWater(block.getRelative(BlockFace.SOUTH_EAST))
				|| HothUtils.isWater(block.getRelative(BlockFace.SOUTH_WEST))
				;
	}
	
	public static boolean isWater(Block block)
	{
		Material type = block.getType();
		return type.equals(Material.WATER);
	}

	/**
	 * Check if there's nearby lava. Within a (size+1)^3 cube
	 * @param location
	 * @return true if there's lava inside the cube
	 */
	public static boolean isTooHot(Location location, int size)
	{
		World world = location.getWorld();
		int x = location.getBlockX();
		int y = location.getBlockY();
		int z = location.getBlockZ();
		
		for(int xx=x-size;xx<=x+size;xx++)
		{
			for(int zz=z-size;zz<=z+size;zz++)
			{
				for(int yy=y-size;yy<=y+size;yy++)
				{
					Block block = world.getBlockAt(xx, yy, zz);
					Material mat = block.getType();
					
					
					if(mat.equals(Material.LAVA))
					{
						return true;
					}
				}
			}
		}

		return false;
	}
	
	public static <T extends Comparable<? super T>> List<T> toSortedList(Collection<T> c)
	{
	  List<T> list = new ArrayList<T>(c);
	  Collections.sort(list);
	  return list;
	}

}
