package biz.orgin.minecraft.hothgenerator;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.world.block.BlockState;
import com.sk89q.worldedit.world.registry.LegacyMapper;
import java.util.HashSet;
import java.util.Set;

import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.block.data.BlockData;
import org.bukkit.inventory.Inventory;

public class Blob
{
	private Set<Position>primary;
	private Set<Position>secondary;
	private World world;
	private HothGeneratorPlugin plugin;
	private String parentName;
	
	private static IntSet delays = new IntSet(new int[] {  // Block types to defer until infrastructure is made.
			50,75,76,6,32,37,38,39,40,50,51,55,26,
			59,31,63,65,66,96,69,77,78,89,106,83,115,
			93,94,111,127,131,132,140,141,142,143,171,78,64});
	
	private static int blocksPerInteration = 2000;
	
	public Blob(HothGeneratorPlugin plugin, World world, String parentName)
	{
		this.plugin = plugin;
		this.world = world;
		this.primary = new HashSet<Position>();
		this.secondary = new HashSet<Position>();
		this.parentName = parentName;
	}
	
	public String toString()
	{
		StringBuffer mySB = new StringBuffer();
		
		mySB.append("primary:\n");
		Position[] pos1 = this.primary.toArray(new Position[0]);
		for(int i=0;i<pos1.length;i++)
		{
			Position pos = pos1[i];
			mySB.append("type="+pos.type+" data="+pos.data+" " + pos.x + "," + pos.y + "," + pos.z + "\n");
		}
		Position[] pos2 = this.primary.toArray(new Position[0]);
		mySB.append("secondary:\n");
		for(int i=0;i<pos2.length;i++)
		{
			Position pos = pos2[i];
			mySB.append("type="+pos.type+" data="+pos.data+" " + pos.x + "," + pos.y + "," + pos.z + "\n");
		}
		
		return mySB.toString();
	}
	
	public void addPosition(Position position)
	{
		int type = position.data;
		if(Blob.delays.contains(type))
		{
			this.secondary.add(position);
		}
		else
		{
			this.primary.add(position);
		}
	}
	
	public void instantiate()
	{
		Position[] blocks = this.primary.toArray(new Position[0]);
		if(blocks.length>0)
		{
			this.finishBlob(blocks);
		}
		blocks = this.secondary.toArray(new Position[0]);
		if(blocks.length>0)
		{
			this.finishBlob(blocks);
		}
	}
	
	private void finishBlob(Position[] blocks)
	{
		plugin.addTask(new PlaceBlob(this.world, this.parentName, blocks, 0, Blob.blocksPerInteration));
	}
	
	static class PlaceBlob extends HothRunnable
	{
		private static final long serialVersionUID = 9054230080505873655L;
		private Position[] blocks;
		private int start;
		private int count;
		private String parentName;

		public String getParameterString()
		{
			return "parentName = " + this.parentName + " start=" + this.start + " count=" + this.count;
		}
		
		public PlaceBlob(World world, String parentName, Position[] blocks, int start, int count)
		{
			this.setName(parentName + ".PlaceBlob");
			this.setWorld(world);
			this.setPlugin(null);
			this.blocks = blocks;
			this.start = start;
			this.count = count;
			this.parentName = parentName;
		}

		@Override
		public void run()
		{
			HothGeneratorPlugin plugin = this.getPlugin();
			World world = this.getWorld();
			
			Position[] blocks = this.blocks;
			int start = this.start;
			int count = this.count;
                        LegacyMapper lm = LegacyMapper.getInstance();
			
			for(int i=start;i<start+count && i<blocks.length;i++)
			{
				Position pos = blocks[i];
				
				if(pos.blockState!=null)
				{
					Block block = world.getBlockAt(pos.x, pos.y, pos.z);
					int type = pos.type;
					block.setType(MaterialManager.toMaterial(type));
					pos.blockState.update(true, false);
				}
				else
				{
					Block block = world.getBlockAt(pos.x, pos.y, pos.z);
					// Check if chest, rotation and what not
					int type = pos.type;
					byte data = pos.data;
					if(type==52) // Spawner
					{
						block.setType(MaterialManager.toMaterial(type));
						CreatureSpawner spawner = (CreatureSpawner)block.getState();
						spawner.setSpawnedType(EntityTypeManager.toEntityType(data));
						spawner.update(true);
					}
					else if(type==54) // Chest, set correct rotation and add some random loot
					{
						block.setType(MaterialManager.toMaterial(type));
						Chest chest = (Chest)block.getState();
						org.bukkit.material.Chest cst = null;
						switch(data)
						{
						default:
						case 0:
							cst = new org.bukkit.material.Chest(BlockFace.EAST);
							break;
						case 1:
							cst = new org.bukkit.material.Chest(BlockFace.WEST);
							break;
						case 2:
							cst = new org.bukkit.material.Chest(BlockFace.NORTH);
							break;
						case 3:
							cst = new org.bukkit.material.Chest(BlockFace.SOUTH);
							break;
						}
						chest.setData(cst);
						Inventory inv = chest.getInventory();
						int lootMin = pos.lootMin;
						int lootMax = pos.lootMax;
						LootGenerator lootGenerator = pos.lootGenerator;
						if(lootGenerator!=null)
						{
							lootGenerator.getLootInventory(inv, lootMin, lootMax);
						}
                                                //block.setBlockData(chest.getBlockData(), true);
						//chest.update(true);
					}
					else
					{
						try
						{
							block.setType(MaterialManager.toMaterial(type));
						}
						catch(Exception e)
						{
						}
                                                BlockState bs = lm.getBlockFromLegacy(type, data);
                                                BlockData bd = BukkitAdapter.adapt(bs);
                                                block.setBlockData(bd, false);
						//DataManager.setData(block, data, false);
					}
				}
			}
			
			if(start+count < blocks.length)
			{
				plugin.addTask(new PlaceBlob(world, this.parentName, blocks, start+count, count));
			}

		}
	}

}
