package biz.orgin.minecraft.hothgenerator.schematic;

import biz.orgin.minecraft.hothgenerator.HothUtils;

public class DoorEW implements Schematic
{
	private static final long serialVersionUID = 3102728557886905484L;
	public static Schematic instance = new DoorEW();
	private static int WIDTH = 1;
	private static int LENGTH = 3;
	private static int HEIGHT = 3;
	private static String name = "DoorEW";
	
	private final int[][][] matrix = new int[][][]
	{
		    //   TYPEID            DATAID
		{   // Layer 0
			{ 98,    0, },
			{ 98,    3, },
			{ 98,    0, }
		},
		{   // Layer 1
			{ 98,    3, },
			{ 89,    0, },
			{ 98,    3, }
		},
		{   // Layer 2
			{ 98,    0, },
			{ 98,    3, },
			{ 98,    0, }
		},
	};
	
	private DoorEW()
	{
	}
	
	public int getWidth() // Inner
	{
		return DoorEW.WIDTH;
	}
	public int getLength() // Middle
	{
		return DoorEW.LENGTH;
	}
	public int getHeight() // Outer
	{
		return DoorEW.HEIGHT;
	}
	
	public int[][][] getMatrix()
	{
		return this.matrix;
	}

	@Override
	public String getName()
	{
		return DoorEW.name;
	}

	@Override
	public Schematic rotate(int direction)
	{
		return HothUtils.rotateSchematic(direction, this);
	}
}