package biz.orgin.minecraft.hothgenerator;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SuperChunkBlob implements Serializable, Comparable<SuperChunkBlob>
{
	private static final long serialVersionUID = -1529791443846054494L;
	
	private static IntSet delays = new IntSet(new int[] {  // Block types to defer until infrastructure is made.
			50,75,76,6,32,37,38,39,40,51,55,26,
			59,31,63,65,66,96,69,77,78,106,83,115,
			93,94,111,127,131,132,140,141,142,143,171,78,64});

	private Set<Position> primary;
	private Set<Position> secondary;
	private ChunkKey chunkKey;
	private SuperBlobType superBlobType;
	
	public SuperChunkBlob(ChunkKey chunkKey, SuperBlobType superBlobType)
	{
		this.superBlobType = superBlobType;
		this.chunkKey = chunkKey;
		this.primary = new HashSet<Position>();
		this.secondary = new HashSet<Position>();
	}
	
	public void addPosition(Position position)
	{
		int type = position.type;
		if(SuperChunkBlob.delays.contains(type))
		{
			this.secondary.add(position);
		}
		else
		{
			this.primary.add(position);
		}
	}
	
	public Set<Position> getPrimary()
	{
		return this.primary;
	}
	
	public Set<Position> getSecondary()
	{
		return this.secondary;
	}
	
	public SuperBlobType getSuperBlobType()
	{
		return this.superBlobType;
	}
	
	public ChunkKey getChunkKey()
	{
		return this.chunkKey;
	}
	
	public String toString()
	{
		StringBuffer mySB = new StringBuffer();
		
		mySB.append("    SCB: ").append(this.chunkKey).append('\n');
		
		mySB.append("     Primary:\n");
		List<Position> plist = HothUtils.toSortedList(this.primary);
		for(int i=0;i<plist.size();i++)
		{
			Position pos = plist.get(i);
			mySB.append("      POS:").append(pos.toString()).append('\n');
		}
		
		mySB.append("     Secondary:\n");
		List<Position> slist = HothUtils.toSortedList(this.secondary);
		for(int i=0;i<slist.size();i++)
		{
			Position pos = slist.get(i);
			mySB.append("      POS:").append(pos.toString()).append('\n');
		}

		return mySB.toString();
	}
	
	@Override
	public int compareTo(SuperChunkBlob other)
	{
		String ck1 = this.chunkKey.toString();
		String ck2 = other.getChunkKey().toString();
		return ck1.compareTo(ck2);
		
	}
	
	public static class ChunkKey implements Serializable, Comparable<ChunkKey>
	{
		private static final long serialVersionUID = 7161146253017780563L;

		private int x;
		private int z;
		
		public ChunkKey(int x, int z)
		{
			this.x = x&~0xf;
			this.z = z&~0xf;
		}
		
		public int getX()
		{
			return this.x;
		}

		public int getZ()
		{
			return this.z;
		}
		
		@Override
		public boolean equals(Object other)
		{
			return other!=null
					&& other instanceof ChunkKey 
					&& this.x == ((ChunkKey)other).getX() 
					&& this.z == ((ChunkKey)other).getZ();
		}

		@Override
		public int hashCode()
		{
			return this.z*999999 + this.x;
		}
		
		@Override
		public String toString()
		{
			return this.x + "," + this.z;
		}

		@Override
		public int compareTo(ChunkKey other)
		{
			return this.toString().compareTo(other.toString());
		}

	}

}
