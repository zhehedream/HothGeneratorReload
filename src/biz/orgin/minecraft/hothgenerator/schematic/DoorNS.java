package biz.orgin.minecraft.hothgenerator.schematic;

import biz.orgin.minecraft.hothgenerator.HothUtils;

public class DoorNS implements Schematic
{
	private static final long serialVersionUID = 299532124330599474L;
	public static Schematic instance = new DoorNS();
	private static int WIDTH = 3;
	private static int LENGTH = 1;
	private static int HEIGHT = 3;
	private static String name = "DoorNS";
	
	private final int[][][] matrix = new int[][][]
	{
		    //   TYPEID            DATAID
		{   // Layer 0
			{ 98, 98, 98,   0, 3, 0}
		},
		{   // Layer 1
			{ 98, 89, 98,   3, 0, 3}
		},
		{   // Layer 2
			{ 98, 98, 98,   0, 3, 0}
		},
	};
	
	private DoorNS()
	{
	}
	
	public int getWidth() // Inner
	{
		return DoorNS.WIDTH;
	}
	public int getLength() // Middle
	{
		return DoorNS.LENGTH;
	}
	public int getHeight() // Outer
	{
		return DoorNS.HEIGHT;
	}
	
	public int[][][] getMatrix()
	{
		return this.matrix;
	}

	@Override
	public String getName()
	{
		return DoorNS.name;
	}

	@Override
	public Schematic rotate(int direction)
	{
		return HothUtils.rotateSchematic(direction, this);
	}
}