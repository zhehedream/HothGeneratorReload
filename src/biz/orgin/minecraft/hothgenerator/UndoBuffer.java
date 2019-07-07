package biz.orgin.minecraft.hothgenerator;

import java.util.EmptyStackException;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import java.util.UUID;

public class UndoBuffer
{
	private Map<UUID, Buffer> buffers;
	
	public UndoBuffer()
	{
		this.buffers = new HashMap<UUID, Buffer>();
	}

	public void pushBlob(UUID uuid, Blob blob)
	{
		Buffer buffer;
		if(this.buffers.containsKey(uuid))
		{
			buffer = this.buffers.get(uuid);
		}
		else
		{
			buffer = new Buffer();
			this.buffers.put(uuid, buffer);
		}
		
		buffer.pushBlob(blob);
	}
	
	/**
	 * Get the latest undo entry. Will return null if there is no undo set left
	 * @param player Name of player
	 * @return A Blob from the stack associated with the player name
	 */
	public Blob popBlob(UUID uuid)
	{
		Buffer buffer;
		if(this.buffers.containsKey(uuid))
		{
			buffer = this.buffers.get(uuid);
		}
		else
		{
			buffer = new Buffer();
			this.buffers.put(uuid, buffer);
		}
		
		return buffer.popBlob();
	}
	
	
	
	private class Buffer
	{
		private Stack<Blob> stack;
		
		public Buffer()
		{
			this.stack = new Stack<Blob>();
		}
		
		public void pushBlob(Blob blob)
		{
			this.stack.push(blob);
		}
		
		public Blob popBlob()
		{
			try
			{
				return this.stack.pop();
			}
			catch(EmptyStackException e)
			{
				return null;
			}
		}
	}
}
