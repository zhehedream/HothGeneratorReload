package biz.orgin.minecraft.hothgenerator.schematic;

import biz.orgin.minecraft.hothgenerator.HothUtils;

public class CorridorEW  implements Schematic
{
	private static final long serialVersionUID = -3723609036328259577L;
	public static Schematic instance = new CorridorEW();
	private static int WIDTH = 7;
	private static int LENGTH = 5;
	private static int HEIGHT = 5;
	private static String name = "CorridorEW";
	
	private final int[][][] matrix = new int[][][]
	{
		    //   TYPEID                 DATAID
		{   // Layer 0
			{-1,-1,-1,-1,-1,-1,-1,   0, 0, 0, 0, 0, 0, 0},
			{-1,-1,-1,-1,-1,-1,-1,   0, 0, 0, 0, 0, 0, 0},
			{-1,98,98,98,98,98,-1,   0, 0, 0, 0, 0, 0, 0},
			{-1,-1,-1,-1,-1,-1,-1,   0, 0, 0, 0, 0, 0, 0},
			{-1,-1,-1,-1,-1,-1,-1,   0, 0, 0, 0, 0, 0, 0}
		},
		{   // Layer 1
			{ -1, -1, -1, -1, -1, -1, -1,   0, 0, 0, 0, 0, 0, 0},
			{109,109,109,109,109,109,109,   7, 7, 7, 7, 7, 7, 7},
			{  0,  0,  0,  0,  0,  0,  0,   0, 0, 0, 0, 0, 0, 0},
			{109,109,109,109,109,109,109,   6, 6, 6, 6, 6, 6, 6},
			{ -1, -1, -1, -1, -1, -1, -1,   0, 0, 0, 0, 0, 0, 0}

		},
		{   // Layer 2
			{-1,98,98,89,98,98,-1,   0, 0, 3, 0, 3, 0, 0},
			{ 0, 0, 0, 0, 0, 0, 0,   0, 0, 0, 0, 0, 0, 0},
			{ 0, 0, 0, 0, 0, 0, 0,   0, 0, 0, 0, 0, 0, 0},
			{ 0, 0, 0, 0, 0, 0, 0,   0, 0, 0, 0, 0, 0, 0},
			{-1,98,98,89,98,98,-1,   0, 0, 3, 0, 3, 0, 0}
		},
		{   // Layer 3
			{-1,98,98,98,98,98,-1,   0, 0, 0, 3, 0, 0, 0},
			{ 0, 0, 0, 0, 0, 0, 0,   0, 0, 0, 0, 0, 0, 0},
			{ 0, 0, 0, 0, 0, 0, 0,   0, 0, 0, 0, 0, 0, 0},
			{ 0, 0, 0, 0, 0, 0, 0,   0, 0, 0, 0, 0, 0, 0},
			{-1,98,98,98,98,98,-1,   0, 0, 0, 3, 0, 0, 0}
		},
		{   // Layer 4
			{-1,-1,-1,-1,-1,-1,-1,   0, 0, 0, 0, 0, 0, 0},
			{-1,98,43,43,43,98,-1,   0, 0, 0, 0, 0, 0, 0},
			{-1,43,43,43,43,43,-1,   0, 0, 0, 0, 0, 0, 0},
			{-1,98,43,43,43,98,-1,   0, 0, 0, 0, 0, 0, 0},
			{-1,-1,-1,-1,-1,-1,-1,   0, 0, 0, 0, 0, 0, 0}
		}
	};
	
	
	private CorridorEW()
	{
	}
	
	public int getWidth() // Inner
	{
		return CorridorEW.WIDTH;
	}
	public int getLength() // Middle
	{
		return CorridorEW.LENGTH;
	}
	public int getHeight() // Outer
	{
		return CorridorEW.HEIGHT;
	}
	
	public int[][][] getMatrix()
	{
		return this.matrix;
	}

	@Override
	public String getName()
	{
		return CorridorEW.name;
	}

	@Override
	public Schematic rotate(int direction)
	{
		return HothUtils.rotateSchematic(direction, this);
	}
}