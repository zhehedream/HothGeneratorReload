package biz.orgin.minecraft.hothgenerator;

import java.io.IOException;
import java.util.Random;

import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.generator.BlockPopulator;

import biz.orgin.minecraft.hothgenerator.schematic.LoadedSchematic;

public class MustafarTemplePopulator extends BlockPopulator
{
	@SuppressWarnings("unused")
	private int height;
	private HothGeneratorPlugin plugin;
	
	private LoadedSchematic[] schematics;

	public MustafarTemplePopulator(int height)
	{
		this.plugin = HothGenerator.getPlugin();
		this.height = height;
		try
		{
			this.schematics = new LoadedSchematic[4];
			LoadedSchematic schematic = new LoadedSchematic(plugin.getResource("schematics/mustafartemple.sm"),"mustafartemple");
			this.schematics[0] = schematic.cloneRotate(0);
			this.schematics[1] = schematic.cloneRotate(1);
			this.schematics[2] = schematic.cloneRotate(2);
			this.schematics[3] = schematic.cloneRotate(3);
		}
		catch (IOException e)
		{
			this.plugin.logMessage("Error while loading mustafartemple.sm: " + e.getMessage());
			this.schematics = null;
		}
	}

	@Override
	public void populate(World world, Random random, Chunk chunk)
	{
		if(this.schematics!=null)
		{
			int rand = this.schematics[0].getRandom();
			int rarity = ConfigManager.getStructureMustafarTempleRarity(this.plugin, world);

			if(rarity!=0)
			{
				if(rand == random.nextInt((rarity*this.schematics[0].getRarity()/2)))
				{
					this.placeTemple(world, random, chunk);
				}	
			}			
		}	
	}
	
	private void placeTemple(World world, Random random, Chunk chunk)
	{
		LoadedSchematic schematic = this.schematics[random.nextInt(4)];
		
		int xx = chunk.getX()*16;
		int zz = chunk.getZ()*16;
		
		// Don't generate temples too close to the spawn.
		if(Math.sqrt((xx*xx)+(zz*zz))<500)
		{
			return;
		}
		
		int x = xx + 8 + random.nextInt(16);
		int z = zz + 8 + random.nextInt(16);
		
		int width = schematic.getWidth();
		int length = schematic.getLength();
		int height = schematic.getHeight();
		
		// Crudely calculate the lowest height of the lava level
		int surfaceOffset = ConfigManager.getWorldSurfaceoffset(this.plugin, world);
		
		int center = (int)LavaLevelGenerator.getLavaLevelAt(world, x, z, surfaceOffset);
		int c1 = (int)LavaLevelGenerator.getLavaLevelAt(world, x-width/2, z-length/2, surfaceOffset);
		int c2 = (int)LavaLevelGenerator.getLavaLevelAt(world, x+width/2, z-length/2, surfaceOffset);
		int c3 = (int)LavaLevelGenerator.getLavaLevelAt(world, x-width/2, z+length/2, surfaceOffset);
		int c4 = (int)LavaLevelGenerator.getLavaLevelAt(world, x+width/2, z+length/2, surfaceOffset);
		int c5 = (int)LavaLevelGenerator.getLavaLevelAt(world, x-width/2, z, surfaceOffset);
		int c6 = (int)LavaLevelGenerator.getLavaLevelAt(world, x+width/2, z, surfaceOffset);
		int c7 = (int)LavaLevelGenerator.getLavaLevelAt(world, x, z+length/2, surfaceOffset);
		int c8 = (int)LavaLevelGenerator.getLavaLevelAt(world, x, z+length/2, surfaceOffset);
		
		int min = center;
		
		if(c1<min) min = c1;
		if(c2<min) min = c2;
		if(c3<min) min = c3;
		if(c4<min) min = c4;
		if(c5<min) min = c5;
		if(c6<min) min = c6;
		if(c7<min) min = c7;
		if(c8<min) min = c8;
		
		// Place The Schematic
		int y = min + height + schematic.getYoffset();
		int lootMin = schematic.getLootMin();
		int lootMax = schematic.getLootMax();
		HothUtils.placeSchematic(plugin, world, schematic, x-width/2, y, z-length/2, lootMin, lootMax);
		this.plugin.logMessage("Placing " + schematic.getName() + " at " + world.getName() + "," + x + "," + y + "," + z, true);
	}
}
