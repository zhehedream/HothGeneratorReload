package biz.orgin.minecraft.hothgenerator;

import biz.orgin.minecraft.hothgenerator.schematic.Schematic;

/**
 * Holder class used by the RoomGenerator to store data about
 * which exits a room can have.
 * @author orgin
 *
 */
public class Exit
{
	public int x;
	public int y;
	public int z;
	public Schematic schematic;
	
	public Exit(int x, int y, int z, Schematic schematic)
	{
		this.x = x;
		this.y = y;
		this.z = z;
		this.schematic = schematic;
	}
}
