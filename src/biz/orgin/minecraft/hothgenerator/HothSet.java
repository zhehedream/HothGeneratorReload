package biz.orgin.minecraft.hothgenerator;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class HothSet implements Serializable
{
	private static final long serialVersionUID = 8865094688368113908L;

	private Map<Position, Position>map;

	public HothSet()
	{
		this.map = new HashMap<Position, Position>();
	}
	
	
	public Position[] toArray()
	{
		return this.map.keySet().toArray(new Position[0]);
	}
	
	public void add(Position position)
	{
		this.map.remove(position);
		this.map.put(position, position);
	}
	
	public void remove(Position position)
	{
		this.map.remove(position);
	}
	
	public int size()
	{
		return this.map.size();
	}
	
	public boolean isEmpty()
	{
		return this.map.isEmpty();
	}
	
	public void clear()
	{
		this.map.clear();
	}
	
	public Position get(Position position)
	{
		return this.map.get(position);
	}
	
	public boolean contains(Position position)
	{
		return this.map.containsKey(position);
	}
	
	public int hashCode()
	{
		return map.hashCode();
	}
	
	
}
