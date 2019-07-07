package biz.orgin.minecraft.hothgenerator;

import java.io.Serializable;
import java.util.Random;

/**
 * Holds metadata for a maze room 
 * @author orgin
 *
 */
public class Room implements Serializable
{

	private static final long serialVersionUID = -3608697687316409041L;

	/**
	 * Used to define which exit that points to the parent room. Exits must
	 * always be rendered from the originating room. An exit with the DUMMY
	 * room won't be rendered back to the parent room.
	 */
	public static Room DUMMY = new Room(0,0,0,0);
	
	public Room[] children;
	// 0 - up
	// 1 - down
	// 2 - north
	// 3 - south
	// 4 - west
	// 5 - east
	
	public int floor;
	public boolean spawner;
	public int decoration;
	
	public int x;
	public int y;
	public int z;
	
	public int id;
	
	public Room parent;
	
	public boolean isPopulated;
	
	public Room(int id, int x, int y, int z)
	{
		this.id = id;

		this.children = new Room[6];
		
		for(int i=0;i<6;i++)
		{
			this.children[i]=null;
		}
		
		this.x = x;
		this.y = y;
		this.z = z;
		
		this.floor = 0;
		this.decoration = 0;
		this.spawner = false;
		
		if(id==0)
		{
			this.isPopulated = false;
		}
		else
		{
			this.isPopulated = false;
		}
	}
	
	/**
	 * Generates metadata for room decoration. Do not call this method before
	 * adding children to the room or else it will not be rendered properly.
	 * @param random
	 */
	public void decorate(Random random)
	{
		// Decorate room here, with all special rules and what not. But after the children has been set.
		
		this.floor = 0;
		this.decoration = 0;
		
		// Floor
		if(this.children[1]==null)
		{
			this.floor = random.nextInt(4);
		}

		// Decoration
		if(this.children[0]==null && this.children[1]==null) // Don't decorate rooms with up or down stairs.
		{
			this.decoration = random.nextInt(9);
		}
		
		if(this.decoration==8) // This is a spawner room.
		{
			if(RoomGenerator.SPAWNER)
			{
				this.spawner = true;
			}
			else
			{
				this.decoration = 0;
			}
		}
	}
	
	public int getChildCount()
	{
		int result = 0;
		for(int i=0;i<6;i++)
		{
			if(this.children[i]!=null && this.children[i]!=Room.DUMMY)
			{
				result++;
			}
		}
		return result;
	}
	
	public boolean Equals(Room other)
	{
		return this.id == other.id;
	}
	
	public String toString()
	{
		StringBuffer mySB = new StringBuffer();
		
		mySB.append("Room ID=" + this.id);
		if(this.children[0]!=null)	mySB.append("[UP: " + this.children[0].id + "] ");
		else mySB.append("[UP: null]");
		if(this.children[1]!=null)	mySB.append("[DOWN: " + this.children[1].id + "] ");
		else mySB.append("[DOWN: null]");
		if(this.children[2]!=null)	mySB.append("[NORTH: " + this.children[2].id + "] ");
		else mySB.append("[NORTH: null]");
		if(this.children[3]!=null)	mySB.append("[SOUTH: " + this.children[3].id + "] ");
		else mySB.append("[SOUTH: null]");
		if(this.children[4]!=null)	mySB.append("[WEST: " + this.children[4].id + "] ");
		else mySB.append("[WEST: null]");
		if(this.children[5]!=null)	mySB.append("[EAST: " + this.children[5].id + "] ");
		else mySB.append("[EAST: null]");
		
		
		return mySB.toString();
	}
}
