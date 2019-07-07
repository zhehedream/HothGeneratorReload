package biz.orgin.minecraft.hothgenerator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bukkit.World;

import biz.orgin.minecraft.hothgenerator.SuperChunkBlob.ChunkKey;

/**
 * External accessor for using the super chunk cache.
 * It hold a list of SuperChunkStorage for each world.
 * The intent is to enable delaying rendering into non generated chunks until the chunk is generated.
 * The caller goes through the following schemas:
 *  When generating a new structure/schematics that is to be inserted into the world:
 *   - Generate a SuperBlob
 *   - Inject SuperBlob using addSuperBlob()
 *   - render the returned SuperChunkBlobs into the world.
 *  When generating a new chunk
 *   - getSuperChunkBlobsForChunk()
 *   - render the contents into the chunk
 *   When onDisable() is running
 *   - save()
 * @author orgin
 *
 */
public class SuperChunkManager
{
	private Map<String, SuperChunkStorage> superChunkStorageList;
	
	public SuperChunkManager()
	{
		this.superChunkStorageList = new HashMap<String, SuperChunkStorage>();
	}

	/**
	 * Inject a SuperBlob into a worlds cache storage. Any parts that are inside chunks that are already loaded
	 * are not injected into the cache.
	 * @param world
	 * @param superBlob
	 * @return A list with the SuperChunkBlobs that must be rendered in the world.
	 */
	public List<SuperChunkBlob> addSuperBlob(World world, SuperBlob superBlob)
	{
		List<SuperChunkBlob> list = new ArrayList<SuperChunkBlob>();
		
		Set<ChunkKey> keys = superBlob.getChunkKeys();
		Iterator<ChunkKey> iter = keys.iterator();
		
		
		while(iter.hasNext())
		{
			ChunkKey key = iter.next();
			SuperChunkBlob scb = superBlob.getSuperChunkBlob(key);
			if(world.loadChunk(key.getX(), key.getZ(), false)) // Check if the chunk is rendered
			{
				list.add(scb);
				superBlob.removeSuperChunkBlob(key);
			}
		}
		
		for(int i=0;i<list.size();i++)
		{
			SuperChunkBlob sc = list.get(i);
			superBlob.removeSuperChunkBlob(sc.getChunkKey());
		}
		
		this.addSuperBlob(world.getName(), superBlob);
		
		return list;
	}

	/**
	 * Add a SuperBlob to the worlds cache storage.
	 * This method will add the full superBlob and any parts that are in a chunk that is already rendered will
	 * not be rendered.
	 * The method is intended for internal and test case use only.
	 * @param worldName
	 * @param superBlob
	 */
	public void addSuperBlob(String worldName, SuperBlob superBlob)
	{
		Set<ChunkKey> keys = superBlob.getChunkKeys();
		SuperChunkStorage superChunkStorage = this.superChunkStorageList.get(worldName);
		if(superChunkStorage==null)
		{
			superChunkStorage = new SuperChunkStorage(worldName);
			this.superChunkStorageList.put(worldName, superChunkStorage);
		}
		
		Iterator<ChunkKey> ick = keys.iterator();
		while(ick.hasNext())
		{
			ChunkKey key = ick.next();
			
			superChunkStorage.addSuperChunkBlob(key, superBlob.getSuperChunkBlob(key));
		}
	}
	
	/**
	 * Get a List of SuperChunkBlob to render into the world.
	 * The List will be removed from the worlds cache storage! If you don't render the list into the world 
	 * then it will be lost forever!
	 * @param world
	 * @param x
	 * @param z
	 * @return
	 */
	public List<SuperChunkBlob> getSuperChunkBlobListForChunk(World world, int x, int z)
	{
		List<SuperChunkBlob> list = this.getSuperChunkBlobListForChunk(world.getName(), x, z);
		this.removeSuperChunkBlobListForChunk(world.getName(), x, z);
		return list;
	}

	/**
	 * Get a List of SuperChunkBlob for a given chunk.
	 * This is a peek method, nothing will be removed from the worlds cache storage.
	 * @param world
	 * @param x
	 * @param z
	 * @return	 */
	public List<SuperChunkBlob> getSuperChunkBlobListForChunk(String worldName, int x, int z)
	{
		SuperChunkStorage superChunkStorage = this.superChunkStorageList.get(worldName);
		if(superChunkStorage==null)
		{
			return null;
		}
		
		SuperChunk superChunk = superChunkStorage.getSuperChunk(x, z);
		if(superChunk==null)
		{
			return null;
		}
		
		return superChunk.getSuperChunkBlobList(x, z);
	}
	
	private void removeSuperChunkBlobListForChunk(String worldName, int x, int z)
	{
		SuperChunkStorage superChunkStorage = this.superChunkStorageList.get(worldName);
		if(superChunkStorage==null)
		{
			return;
		}
		
		SuperChunk superChunk = superChunkStorage.getSuperChunk(x, z);
		if(superChunk==null)
		{
			return;
		}
		
		superChunk.remove(new ChunkKey(x, z));
	}
	
	public void save()
	{
		Set<String> worlds = this.superChunkStorageList.keySet();
		Iterator<String> iter = worlds.iterator();
		
		while(iter.hasNext())
		{
			SuperChunkStorage sc = this.superChunkStorageList.get(iter.next());
			sc.save();
		}
	}
	
	/** test accessor methods **/
	public Map<String, SuperChunkStorage> getSuperChunkStorageList()
	{
		return this.superChunkStorageList;
	}
}
