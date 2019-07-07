package biz.orgin.minecraft.hothgenerator;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.HashMap;

import biz.orgin.minecraft.hothgenerator.SuperChunkBlob.ChunkKey;

public class SuperBlob implements Serializable
{
	private static final long serialVersionUID = 6391401373951456689L;


	public static void main(String[] args)
	{
		SuperBlob superBlob = new SuperBlob(SuperBlobType.GENERIC);
		
		superBlob.addPosition(new Position(0,0,0,10));
		superBlob.addPosition(new Position(1,0,1,10));
		superBlob.addPosition(new Position(0,0,2,10));
		superBlob.addPosition(new Position(3,0,0,10));
		superBlob.addPosition(new Position(19,0,3,10));
		
		superBlob.addPosition(new Position(5,0,-4,40));
		superBlob.addPosition(new Position(6,0,-1,40));
		superBlob.addPosition(new Position(3,0,-2,40));
		superBlob.addPosition(new Position(8,0,-6,40));
		superBlob.addPosition(new Position(9,0,-3,40));

		superBlob.addPosition(new Position(-5,0,-16,10));
		superBlob.addPosition(new Position(-6,0,-18,10));
		superBlob.addPosition(new Position(-3,0,-20,40));
		superBlob.addPosition(new Position(-8,0,-26,40));
		superBlob.addPosition(new Position(-9,0,-23,40));
		System.out.println(superBlob);
		
	}
	
	private SuperBlobType superBlobType;
	private Map<ChunkKey,SuperChunkBlob> blobs;

	public SuperBlob(SuperBlobType superBlobType)
	{
		this.superBlobType = superBlobType;
		this.blobs = new HashMap<ChunkKey, SuperChunkBlob>();
	}
	
	public SuperBlob()
	{
		this(SuperBlobType.GENERIC);
	}
	
	public void removeSuperChunkBlob(ChunkKey chunkKey)
	{
		this.blobs.remove(chunkKey);
	}
	
	public SuperChunkBlob getSuperChunkBlob(ChunkKey chunkKey)
	{
		return this.blobs.get(chunkKey);
	}
	
	public Set<ChunkKey> getChunkKeys()
	{
		return this.blobs.keySet();
	}
	
	public Set<Position> getPrimaryForChunk(int x, int z)
	{
		ChunkKey chunkKey = new ChunkKey(x, z);
		SuperChunkBlob ss = this.blobs.get(chunkKey);
		if(ss==null)
		{
			return null;
		}
		return ss.getPrimary();
	}
	
	public Set<Position> getSecondaryForChunk(int x, int z)
	{
		ChunkKey chunkKey = new ChunkKey(x, z);
		SuperChunkBlob ss = this.blobs.get(chunkKey);
		if(ss==null)
		{
			return null;
		}
		return ss.getSecondary();
	}

	public void addPosition(Position position)
	{
		ChunkKey chunkKey = new ChunkKey(position.x, position.z);
		SuperChunkBlob ss = this.blobs.get(chunkKey);
		if(ss==null)
		{
			ss = new SuperChunkBlob(chunkKey, this.superBlobType);
			this.blobs.put(chunkKey,  ss);
		}
		ss.addPosition(position);
	}
	
	public String toString()
	{
		StringBuffer mySB = new StringBuffer();
		
		Set<ChunkKey> chunks = this.getChunkKeys();
		
		Iterator<ChunkKey> iter = chunks.iterator();
		
		while(iter.hasNext())
		{
			ChunkKey key = iter.next();
			int x = key.getX();
			int z = key.getZ();
			mySB.append("Chunk: ").append(key.getX()).append(',').append(key.getZ()).append('\n');
			
			Set<Position> primary = this.getPrimaryForChunk(x, z);
			Set<Position> secondary = this.getSecondaryForChunk(x, z);
			
			mySB.append(" Primary blocks:\n");
			if(primary!=null)
			{
				Iterator<Position> pi = primary.iterator();
				while(pi.hasNext())
				{
					Position pos = pi.next();
					mySB.append("  ").append(pos.toString()).append('\n');
				}
			}
			mySB.append(" Secondary blocks:\n");
			if(secondary!=null)
			{
				Iterator<Position> si = secondary.iterator();
				while(si.hasNext())
				{
					Position pos = si.next();
					mySB.append("  ").append(pos.toString()).append('\n');
				}
			}
		}
		
		return mySB.toString();
	}
}
