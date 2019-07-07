package biz.orgin.minecraft.hothgenerator;

import java.io.Serializable;

import org.bukkit.block.BlockState;

/**
 * Simple class for holding coordinates. Uses a custom hasCode for
 * quick lookup in a set. Used mainly by the CavePopulator.
 * @author orgin
 *
 */
public class Position implements Serializable, Comparable<Position>
{
	private static final long serialVersionUID = 8446813743059526736L;
	public int x,y,z;
	public int type;
	public byte data;
	public int lootMin;
	public int lootMax;
	public LootGenerator lootGenerator;
	public BlockState blockState;
	
	public Position(int x, int y, int z, int type)
	{
		this.x = x;
		this.y = y;
		this.z = z;
		this.type = type;
		this.data = 0;
		this.lootMin = 0;
		this.lootMax = 0;
		this.lootGenerator = null;
		this.blockState = null;

	}
	
	public Position(BlockState state)
	{
		this.x = state.getX();
		this.y = state.getY();
		this.z = state.getZ();
		this.type = 0;
		this.data = 0;
		this.lootMin = 0;
		this.lootMax = 0;
		this.lootGenerator = null;
		this.blockState = state;
	}

	public Position(int x, int y, int z)
	{
		this.x = x;
		this.y = y;
		this.z = z;
		this.type = 0;
		this.data = 0;
		this.lootMin = 0;
		this.lootMax = 0;
		this.lootGenerator = null;
		this.blockState = null;
	}

	public Position()
	{
		this.x = 0;
		this.y = 0;
		this.z = 0;
		this.type = 0;
		this.data = 0;
		this.lootMin = 0;
		this.lootMax = 0;
		this.lootGenerator = null;
		this.blockState = null;
	}

	@Override
	public int hashCode()
	{
		int result = 1;
		result = 43 * result + x;
		result = 43 * result + y;
		result = 43 * result + z;
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
		{
			return true;
		}
		if (obj == null || !(obj instanceof Position))
		{
			return false;
		}
		Position other = (Position) obj;
		if (x != other.x || y != other.y || z != other.z)
		{
			return false;
		}
		return true;
	}
	
	public String toString()
	{
		StringBuffer mySB = new StringBuffer();
		
		if(this.blockState==null)
		{
			mySB.append(this.x).append(',').append(this.y).append(',').append(this.z).append(" - ").append(this.type).append(',').append(this.data);
		}
		else
		{
			mySB.append(this.x).append(',').append(this.y).append(',').append(this.z).append(" - ").append(this.blockState.getType());
		}
		
		return mySB.toString();
	}

	@Override
	public int compareTo(Position other)
	{
		return this.toString().compareTo(other.toString());
	}
}
