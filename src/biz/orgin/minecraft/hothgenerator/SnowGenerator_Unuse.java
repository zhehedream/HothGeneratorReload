package biz.orgin.minecraft.hothgenerator;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.world.block.BlockState;
import com.sk89q.worldedit.world.registry.LegacyMapper;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;

/*
 * Makes the snow cover made by HothGenerator into a smooth one.
 */
public class SnowGenerator_Unuse
{

	public static void generateSnowCover(HothGeneratorPlugin plugin, World world, Position[][] snowcover)
	{
		if(ConfigManager.isSmoothSnow(plugin))
		{
			plugin.addTask(new PlaceSnowCover(world, snowcover), true);
		}
	}

	static class PlaceSnowCover extends HothRunnable
	{
		private static final long serialVersionUID = -8662915904475816785L;
		private Position[][] snowcover;
		
		public PlaceSnowCover(World world, Position[][] snowcover)
		{
			this.setName("PlaceSnowCover");
			this.setWorld(world);
			this.snowcover = snowcover;
			this.setPlugin(null);
		}
		
		public String getParameterString() {
			return "";
		}

		@Override
		public void run()
		{	
			World world = this.getWorld();
			LegacyMapper lm = LegacyMapper.getInstance();
                        
			for(int z=0;z<16;z++)
			{
				for(int x=0;x<16;x++)
				{
					int rx = this.snowcover[z][x].x;
					int ry = this.snowcover[z][x].y;
					int rz = this.snowcover[z][x].z;

					Block block = world.getBlockAt(rx, ry, rz);
					
					if(block.getType().equals(Material.SNOW))
					{
						byte data = (byte)this.snowcover[z][x].type;
                                                BlockState state = lm.getBlockFromLegacy(78, data);
                                                BlockData sdata = BukkitAdapter.adapt(state);
                                                block.setBlockData(sdata, false);
						//DataManager.setData(block, data, false);
					}
				}
			}
		}
	}

	public static void generateLavaCover(HothGeneratorPlugin plugin, World world, Position[][] lavacover)
	{
		if(ConfigManager.isSmoothLava(plugin))
		{
			plugin.addTask(new PlaceLavaCover(world, lavacover), true);
		}
	}

	static class PlaceLavaCover extends HothRunnable
	{
		private static final long serialVersionUID = 880193028174384835L;
		private Position[][] lavacover;
		
		public PlaceLavaCover(World world, Position[][] lavacover)
		{
			this.setName("PlaceLavaCover");
			this.setWorld(world);
			this.lavacover = lavacover;
			this.setPlugin(null);
		}
		
		public String getParameterString() {
			return "";
		}

		@Override
		public void run()
		{	
			World world = this.getWorld();
			LegacyMapper lm = LegacyMapper.getInstance();
			for(int z=0;z<16;z++)
			{
				for(int x=0;x<16;x++)
				{
					int rx = this.lavacover[z][x].x;
					int ry = this.lavacover[z][x].y;
					int rz = this.lavacover[z][x].z;

					Block block = world.getBlockAt(rx, ry, rz);
					
					if(block.getType().equals(Material.LAVA))
					{
						byte data = (byte)this.lavacover[z][x].type;
                                                BlockState state = lm.getBlockFromLegacy(10, data);
                                                BlockData sdata = BukkitAdapter.adapt(state);
                                                block.setBlockData(sdata, false);
						//DataManager.setData(block, data, false);
					}
				}
			}
		}
	}

}
