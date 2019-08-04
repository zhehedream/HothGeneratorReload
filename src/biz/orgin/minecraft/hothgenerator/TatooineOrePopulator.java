package biz.orgin.minecraft.hothgenerator;

import java.util.Random;
import me.zhehe.MagicIdHandler;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.generator.BlockPopulator;

public class TatooineOrePopulator extends BlockPopulator
{
	private static int[] iterations = new int[] { 8,  8, 10,  4,  2, 8};
	private static int[] amount =     new int[] {32, 32, 32,  8,  8, 32};
	private static Material[] type =       new Material[] {
		Material.DIRT,         // 80
		Material.GRAVEL,       // 80
		Material.SAND,         // 80
		Material.CLAY,         // 80
		Material.TERRACOTTA,    // 80
		Material.STONE};       // 64
	private static byte[] data =     new byte[] {0, 0, 0, 0, 0, 0};
	private static int[] maxHeight = new int[] {80, 80, 80, 80, 80, 64};
	private static Material REPLACE = Material.SANDSTONE;

	@SuppressWarnings("unused")
	private int height;
	@SuppressWarnings("unused")
	private HothGeneratorPlugin plugin;

	public TatooineOrePopulator(int height)
	{
		this.plugin = HothGenerator.getPlugin();
		this.height = height;
	}

	@Override
	public void populate(World world, Random random, Chunk chunk)
	{
		for (int i = 0; i < type.length; i++)
		{
			for (int j = 0; j < iterations[i]; j++)
			{
				this.vein(chunk, random, random.nextInt(16),
						random.nextInt(maxHeight[i]), random.nextInt(16),
						amount[i], type[i], data[i]);
			}
		}

	}
	
	private void vein(Chunk chunk, Random random, int originX,
			int originY, int originZ, int amount, Material type, byte data)
	{

		int dx = originX;
		int dy = originY;
		int dz = originZ;
		for (int i = 0; i < amount; i++)
		{
			int dir = random.nextInt(6);
			switch(dir)
			{
			case 0:
			    dx++;
				break;
			case 1:
		        dx--;
		        break;
			case 2:
			    dy++;
				break;
			case 3:
		        dy--;
		        break;
			case 4:
			    dz++;
				break;
			case 5:
		        dz--;
		        break;
			}
			
			dx = dx & 0x0f;
			dz = dz & 0x0f;

			if (dy > 127 || dy < 0) {
				continue;
			}

			Block block = chunk.getBlock(dx, dy, dz);
			if(block.getType().equals(TatooineOrePopulator.REPLACE))
			{
				block.setType(type);
                                int id = MaterialManager.toID(type);
                                BlockData sdata = MagicIdHandler.fromId(id, data);
                                if(sdata != null) {
                                    block.setBlockData(sdata, false);
                                }
				//DataManager.setData(block, data, false);
			}
			
		}
	}

}
