package biz.orgin.minecraft.hothgenerator;

import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;

import biz.orgin.minecraft.hothgenerator.schematic.BaseRoom1;
import biz.orgin.minecraft.hothgenerator.schematic.BaseRoom2;
import biz.orgin.minecraft.hothgenerator.schematic.BaseRoom3;
import biz.orgin.minecraft.hothgenerator.schematic.BaseRoom4;
import biz.orgin.minecraft.hothgenerator.schematic.BaseRoom5;
import biz.orgin.minecraft.hothgenerator.schematic.BaseRoom6;
import biz.orgin.minecraft.hothgenerator.schematic.BaseSection;
import biz.orgin.minecraft.hothgenerator.schematic.BaseTop;
import biz.orgin.minecraft.hothgenerator.schematic.Schematic;

public class BaseGenerator {

	
	private static Schematic[][] rooms = new Schematic[][]
			{
		
				// South				        West							North							East
				{ BaseRoom1.instance.rotate(0), BaseRoom1.instance.rotate(1), BaseRoom1.instance.rotate(2),  BaseRoom1.instance.rotate(3)},  
				{ BaseRoom2.instance.rotate(0), BaseRoom2.instance.rotate(1), BaseRoom2.instance.rotate(2),  BaseRoom2.instance.rotate(3)},  
				{ BaseRoom3.instance.rotate(0), BaseRoom3.instance.rotate(1), BaseRoom3.instance.rotate(2),  BaseRoom3.instance.rotate(3)},  
				{ BaseRoom4.instance.rotate(0), BaseRoom4.instance.rotate(1), BaseRoom4.instance.rotate(2),  BaseRoom4.instance.rotate(3)}, 
				{ BaseRoom5.instance.rotate(0), BaseRoom5.instance.rotate(1), BaseRoom5.instance.rotate(2),  BaseRoom5.instance.rotate(3)},  
				{ BaseRoom6.instance.rotate(0), BaseRoom6.instance.rotate(1), BaseRoom6.instance.rotate(2),  BaseRoom6.instance.rotate(3)}  
			};
	
	private static int roomCnt = BaseGenerator.rooms.length;
	
	public static void generateBase(HothGeneratorPlugin plugin, World world, Random random, int chunkX, int chunkZ)
	{
		int rarity = ConfigManager.getStructureBasesRarity(plugin, world);

		if(rarity!=0)
		{
			int doit = random.nextInt(512*rarity);
			if(doit == 350)
			{
				plugin.addTask(new PlaceBase(world, random, chunkX, chunkZ));
			}	
		}
	}
	
	
	static class PlaceBase extends HothRunnable
	{
		private static final long serialVersionUID = -4298666022386809250L;
		private Random random;
		private int chunkx;
		private int chunkz;
		
		private int room5s;
		
		public String getParameterString()
		{
			return "chunkx=" + this.chunkx + " chunkz=" + this.chunkz;
		}

		public PlaceBase(World world, Random random, int chunkx, int chunkz)
		{
			this.setName("PlaceBase");
			this.setPlugin(null);
			this.setWorld(world);
			this.random = random;
			this.chunkx = chunkx;
			this.chunkz = chunkz;
			
			this.room5s = 0;
		}
		
		public int getRandomRoom(HothGeneratorPlugin plugin, World world)
		{
			boolean done= false;
			int room = 0;
			
			while(!done)
			{
				room = random.nextInt(BaseGenerator.roomCnt);
				if(room==4)
				{
					if(this.room5s<2 && ConfigManager.isStructureBasesSpawner(plugin, world))
					{
						this.room5s++;
						done = true;
					}
				}
				else
				{
					done = true;
				}
			}
			
			return room;
		}

		@Override
		public void run()
		{	
			HothGeneratorPlugin plugin = this.getPlugin();
			World world = this.getWorld();
			
			int sx = this.chunkx*16 + random.nextInt(16);
			int sz = this.chunkz*16 + random.nextInt(16);
			
			int sy = world.getHighestBlockYAt(sx, sz);
			
			boolean safePlace = true;
			
			// Check if there's stone around
			Block block = world.getBlockAt(sx,sy,sz); Material type = block.getType();
			if(safePlace && (type.equals(Material.STONE) || type.equals(Material.GLASS))) safePlace = false;
			block = world.getBlockAt(sx-5,sy+11,sz-5); type = block.getType();
			if(safePlace && !type.equals(Material.AIR)) safePlace = false;
			block = world.getBlockAt(sx+5,sy+11,sz-5); type = block.getType();
			if(safePlace && !type.equals(Material.AIR)) safePlace = false;
			block = world.getBlockAt(sx+5,sy+11,sz+5); type = block.getType();
			if(safePlace && !type.equals(Material.AIR)) safePlace = false;
			block = world.getBlockAt(sx-5,sy+11,sz+5); type = block.getType();
			if(safePlace && !type.equals(Material.AIR)) safePlace = false;
			block = world.getBlockAt(sx-5,sy-2,sz-5); type = block.getType();
			if(safePlace && (type.equals(Material.STONE) || type.equals(Material.GLASS) || type.equals(Material.AIR))) safePlace = false;
			block = world.getBlockAt(sx+5,sy-2,sz-5); type = block.getType();
			if(safePlace && (type.equals(Material.STONE) || type.equals(Material.GLASS) || type.equals(Material.AIR))) safePlace = false;
			block = world.getBlockAt(sx+5,sy-2,sz+5); type = block.getType();
			if(safePlace && (type.equals(Material.STONE) || type.equals(Material.GLASS) || type.equals(Material.AIR))) safePlace = false;
			block = world.getBlockAt(sx-5,sy-2,sz+5); type = block.getType();
			if(safePlace && (type.equals(Material.STONE) || type.equals(Material.GLASS) || type.equals(Material.AIR))) safePlace = false;
			
			if(safePlace)
			{
				plugin.logMessage("Placing base at " + world.getName() + "," + sx + "," + sy + "," + sz, true);
				
				HothUtils.placeSchematic(plugin, world, BaseTop.instance, sx-5, sy+11, sz-5, 2, 10);
				world.spawnEntity(new Location(world, sx+5, sy+5, sz+5), EntityType.VILLAGER);
				
				int sections = 3+random.nextInt(4);
				
				for(int i=0;i<sections;i++)
				{
					int px = sx-3;
					int py = sy-5-i*5;
					int pz = sz-3;
					
					HothUtils.placeSchematic(plugin, world, BaseSection.instance, px, py, pz, 2, 10);
					world.spawnEntity(new Location(world, px+3, py-2, pz+2), EntityType.VILLAGER);
					
					int rooms = random.nextInt(16);
					if((rooms&0x1)!=0) // North
					{
						Schematic roomN = BaseGenerator.rooms[this.getRandomRoom(plugin, world)][2];
						HothUtils.placeSchematic(plugin, world, roomN, px, py , pz-9, 2, 10);
					}
					else
					{
						Block door = world.getBlockAt(px+3, py-2, pz); door.setType(Material.COBBLESTONE);
						door = world.getBlockAt(px+3, py-3, pz); door.setType(Material.COBBLESTONE);
					}
					if((rooms&0x2)!=0) // South
					{
						Schematic roomS = BaseGenerator.rooms[this.getRandomRoom(plugin, world)][0];
						HothUtils.placeSchematic(plugin, world, roomS, px, py , pz+6, 2, 10);
					}
					else
					{
						Block door = world.getBlockAt(px+3, py-2, pz+6); door.setType(Material.COBBLESTONE);
						door = world.getBlockAt(px+3, py-3, pz+6); door.setType(Material.COBBLESTONE);
					}
					if((rooms&0x4)!=0) // West
					{
						Schematic roomW = BaseGenerator.rooms[this.getRandomRoom(plugin, world)][1];
						HothUtils.placeSchematic(plugin, world, roomW, px-9, py , pz, 2, 10);
					}
					else
					{
						Block door = world.getBlockAt(px, py-2, pz+3); door.setType(Material.COBBLESTONE);
						door = world.getBlockAt(px, py-3, pz+3); door.setType(Material.COBBLESTONE);
					}
					if((rooms&0x8)!=0) // East
					{
						Schematic roomE = BaseGenerator.rooms[this.getRandomRoom(plugin, world)][3];
						HothUtils.placeSchematic(plugin, world, roomE, px+6, py , pz, 2, 10);
					}
					else
					{
						Block door = world.getBlockAt(px+6, py-2, pz+3); door.setType(Material.COBBLESTONE);
						door = world.getBlockAt(px+6, py-3, pz+3); door.setType(Material.COBBLESTONE);
					}
				}
				
			}
			else
			{
				plugin.logMessage("Failed to place base at " + world.getName() + "," + sx + "," + sy + "," + sz, true);
			}
			
			
		}
	}
}
