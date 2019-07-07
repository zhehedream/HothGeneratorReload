package biz.orgin.minecraft.hothgenerator;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import biz.orgin.minecraft.hothgenerator.SuperChunkBlob.ChunkKey;

/**
 * Holds all SuperChunks for a world.
 * @author orgin
 *
 */
public class SuperChunkStorage implements Serializable
{
	private static final long serialVersionUID = -3344195481722168269L;
	
	public static String BASEPATH = "plugins/HothGenerator";
	
	private String worldName;

	private Map<SuperChunkKey, SuperChunk> superChunkMap;
	
	public SuperChunkStorage(String worldName)
	{
		this.worldName = worldName;
		this.superChunkMap = new HashMap<SuperChunkKey, SuperChunk>();
	}
	
	public SuperChunk getSuperChunk(int x, int z)
	{
		SuperChunkKey superChunkKey = new SuperChunkKey(x, z);
		return this.superChunkMap.get(superChunkKey);
	}

	public SuperChunk getSuperChunk(SuperChunkKey superChunkKey)
	{
		return this.superChunkMap.get(superChunkKey);
	}
	
	public void addSuperChunkBlob(ChunkKey chunkKey, SuperChunkBlob superChunkBlob)
	{
		SuperChunkKey superChunkKey = new SuperChunkKey(chunkKey);
		
		SuperChunk superChunk = this.superChunkMap.get(superChunkKey);
		if(superChunk==null)
		{
			superChunk = this.load(superChunkKey);
			if(superChunk==null)
			{
				superChunk = new SuperChunk();
			}
			this.superChunkMap.put(superChunkKey, superChunk);
		}
		List<SuperChunkBlob> list = superChunk.get(chunkKey);
		
		if(list==null) // No list? create one
		{
			list = new ArrayList<SuperChunkBlob>();
			superChunk.put(chunkKey, list);
		}

		list.add(superChunkBlob);
	}
	
	public void save()
	{
		// Make sure that the sub directories exist
		if(this.verifyCachePath() && this.verifyWorldPath())
		{
			// loop through all maps and save them
			Set<SuperChunkKey> keys = this.superChunkMap.keySet();
			Iterator<SuperChunkKey> iter = keys.iterator();
			
			while(iter.hasNext())
			{
				SuperChunkKey superChunkKey = iter.next();
				String filename = this.getFilename(superChunkKey);
				
				SuperChunk superChunk = this.superChunkMap.get(superChunkKey);
				
				try
				{
					FileOutputStream fout = new FileOutputStream(filename);
					ObjectOutputStream oos = new ObjectOutputStream(fout);
					oos.writeObject(superChunk);
					oos.close();
					fout.close();
				}
				catch(Exception e)
				{
					e.printStackTrace(); // @ToDo, remove this or log it
				}
			}
		}
		else
		{
			// @ToDo: was unable to create the files
		}
	}

	public boolean verifyWorldPath()
	{
		return this.verifyPath(this.getWorldPath());
	}

	public boolean verifyCachePath()
	{
		return this.verifyPath(this.getCachePath());
	}

	public boolean verifyPath(String fileName)
	{
		File file = new File(fileName);
		if(!file.isDirectory() && !file.isFile())
		{
			return file.mkdir();
		}
		return true;
	}
	
	public SuperChunk load(SuperChunkKey superChunkKey)
	{
		// Try to load the SuperChunk for this.worlds superChunkKey
		String filename = this.getFilename(superChunkKey);
		File file = new File(filename);
		SuperChunk superChunk = null;
		if(file.isFile())
		{
			try
			{
				FileInputStream fin = new FileInputStream(file);
				ObjectInputStream ois = new ObjectInputStream(fin);
				superChunk  = (SuperChunk) ois.readObject();
				ois.close();
				fin.close();
			}
			catch(Exception e)
			{
				e.printStackTrace(); //@ToDo: remove this, or log it
			}
		}

		return superChunk;
	}
	
	private String getFilename(SuperChunkKey superChunkKey)
	{
		return this.getWorldPath() + "/" + superChunkKey.getX() + "_" + superChunkKey.getZ() + ".ss";
	}
	
	private String getCachePath()
	{
		return SuperChunkStorage.BASEPATH + "/cache";
	}

	private String getWorldPath()
	{
		return this.getCachePath() + "/" + this.worldName;
	}

	
	public static class SuperChunkKey implements Serializable
	{
		private static final long serialVersionUID = 8278786864820312976L;

		private int x;
		private int z;
		
		public SuperChunkKey(ChunkKey chunkKey)
		{
			this(chunkKey.getX(),chunkKey.getZ());
		}

		public SuperChunkKey(int x, int z)
		{
			this.x = x&~0x0ff;
			this.z = z&~0x0ff;
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
					&& other instanceof SuperChunkKey 
					&& this.x == ((SuperChunkKey)other).getX() 
					&& this.z == ((SuperChunkKey)other).getZ();
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

	}

	/** test accessor methods **/
	public Map<SuperChunkKey, SuperChunk> getSuperChunkMap()
	{
		return this.superChunkMap;
	}
}
