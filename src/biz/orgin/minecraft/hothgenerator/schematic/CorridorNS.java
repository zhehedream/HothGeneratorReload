package biz.orgin.minecraft.hothgenerator.schematic;

import biz.orgin.minecraft.hothgenerator.HothUtils;

public class CorridorNS implements Schematic
{
	private static final long serialVersionUID = 5131983245767933355L;
	public static Schematic instance = new CorridorNS();
	private static int WIDTH = 5;
	private static int LENGTH = 7;
	private static int HEIGHT = 5;
	private static String name = "CorridorNS";
	
	private final int[][][] matrix = new int[][][]
	{
		    //   TYPEID            DATAID
		{   // Layer 0
			{-1,-1,-1,-1,-1,   0, 0, 0, 0, 0},
			{-1,-1,98,-1,-1,   0, 0, 0, 0, 0},
			{-1,-1,98,-1,-1,   0, 0, 0, 0, 0},
			{-1,-1,98,-1,-1,   0, 0, 0, 0, 0},
			{-1,-1,98,-1,-1,   0, 0, 0, 0, 0},
			{-1,-1,98,-1,-1,   0, 0, 0, 0, 0},
			{-1,-1,-1,-1,-1,   0, 0, 0, 0, 0}
		},
		{   // Layer 1
			{-1,109, 0,109,-1,   0, 5, 0, 4, 0},
			{-1,109, 0,109,-1,   0, 5, 0, 4, 0},
			{-1,109, 0,109,-1,   0, 5, 0, 4, 0},
			{-1,109, 0,109,-1,   0, 5, 0, 4, 0},
			{-1,109, 0,109,-1,   0, 5, 0, 4, 0},
			{-1,109, 0,109,-1,   0, 5, 0, 4, 0},
			{-1,109, 0,109,-1,   0, 5, 0, 4, 0}
		},
		{   // Layer 2
			{-1, 0, 0, 0,-1,   0, 0, 0, 0, 0},
			{98, 0, 0, 0,98,   0, 0, 0, 0, 0},
			{98, 0, 0, 0,98,   3, 0, 0, 0, 3},
			{89, 0, 0, 0,89,   0, 0, 0, 0, 0},
			{98, 0, 0, 0,98,   3, 0, 0, 0, 3},
			{98, 0, 0, 0,98,   0, 0, 0, 0, 0},
			{-1, 0, 0, 0,-1,   0, 0, 0, 0, 0}
		},
		{   // Layer 3
			{-1, 0, 0, 0,-1,   0, 0, 0, 0, 0},
			{98, 0, 0, 0,98,   0, 0, 0, 0, 0},
			{98, 0, 0, 0,98,   0, 0, 0, 0, 0},
			{98, 0, 0, 0,98,   3, 0, 0, 0, 3},
			{98, 0, 0, 0,98,   0, 0, 0, 0, 0},
			{98, 0, 0, 0,98,   0, 0, 0, 0, 0},
			{-1, 0, 0, 0,-1,   0, 0, 0, 0, 0}
		},
		{   // Layer 4
			{-1,-1,-1,-1,-1,   0, 0, 0, 0, 0},
			{-1,98,43,98,-1,   0, 0, 0, 0, 0},
			{-1,43,43,43,-1,   0, 0, 0, 0, 0},
			{-1,43,43,43,-1,   0, 0, 0, 0, 0},
			{-1,43,43,43,-1,   0, 0, 0, 0, 0},
			{-1,98,43,98,-1,   0, 0, 0, 0, 0},
			{-1,-1,-1,-1,-1,   0, 0, 0, 0, 0}
		}
	};
	
	
	private CorridorNS()
	{
	}
	
	public int getWidth() // Inner
	{
		return CorridorNS.WIDTH;
	}
	public int getLength() // Middle
	{
		return CorridorNS.LENGTH;
	}
	public int getHeight() // Outer
	{
		return CorridorNS.HEIGHT;
	}
	
	public int[][][] getMatrix()
	{
		return this.matrix;
	}

	@Override
	public String getName()
	{
		return CorridorNS.name;
	}

	@Override
	public Schematic rotate(int direction)
	{
		return HothUtils.rotateSchematic(direction, this);
	}
}
