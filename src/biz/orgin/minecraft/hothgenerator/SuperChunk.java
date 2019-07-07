package biz.orgin.minecraft.hothgenerator;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import biz.orgin.minecraft.hothgenerator.SuperChunkBlob.ChunkKey;

public class SuperChunk extends HashMap<ChunkKey, List<SuperChunkBlob>>
{
	private static final long serialVersionUID = 7289198175326240950L;
	
	public List<SuperChunkBlob> getSuperChunkBlobList(int x, int z)
	{
		return this.getSuperChunkBlobList(new ChunkKey(x,z));
	}

	public List<SuperChunkBlob> getSuperChunkBlobList(ChunkKey chunkKey)
	{
		return this.get(chunkKey);
	}
	
	public String toString()
	{
		StringBuffer mySB = new StringBuffer();
		
		Set<ChunkKey> ckset = this.keySet();
		List<ChunkKey> cklist = HothUtils.toSortedList(ckset);
		for(int i=0;i<cklist.size();i++)
		{
			ChunkKey key = cklist.get(i);
			List<SuperChunkBlob> list = this.get(key);
			Collections.sort(list);
			for(int j=0;j<list.size();j++)
			{
				SuperChunkBlob scb = list.get(j);
				mySB.append("  ").append(j).append(" - ").append(scb.toString());
			}
		}
		
		return mySB.toString();
	}
}
