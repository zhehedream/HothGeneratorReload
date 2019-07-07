package biz.orgin.minecraft.hothgenerator.schematic;

import biz.orgin.minecraft.hothgenerator.HothUtils;

public class StairsUp implements Schematic
{
	private static final long serialVersionUID = -4879133278272178437L;
	public static Schematic instance = new StairsUp();
	private static int WIDTH = 5;
	private static int LENGTH = 5;
	private static int HEIGHT = 4;
	private static String name = "StairsUp";
	
	private final int[][][] matrix = new int[][][]
	{
		    //   TYPEID            DATAID
		{   // Layer 0
			{ -1, -1, -1, -1, -1,   0, 0, 0, 0, 0},
			{ -1,109,  0,  0, -1,   0, 1, 0, 0, 0},
			{ -1, 98, 98,  0, -1,   0, 0, 0, 0, 0},
			{ -1, 98, 98, 98, -1,   0, 0, 0, 0, 0},
			{ -1, -1, -1, -1, -1,   0, 0, 0, 0, 0}
		},
		{   // Layer 1
			{ -1, -1, -1, -1, -1,   0, 0, 0, 0, 0},
			{ -1,  0,109,  0, -1,   0, 0, 1, 0, 0},
			{ -1,  0, 98,  0, -1,   0, 0, 0, 0, 0},
			{ -1,  0,  0,  0, -1,   0, 0, 0, 0, 0},
			{ -1, -1, -1, -1, -1,   0, 0, 0, 0, 0}
		},
		{   // Layer 2
			{ -1, -1, -1, -1, -1,   0, 0, 0, 0, 0},
			{ -1,  0,109,109, -1,   0, 0, 4, 3, 0},
			{ -1,  0, 98,  0, -1,   0, 0, 0, 0, 0},
			{ -1,  0,  0,  0, -1,   0, 0, 0, 0, 0},
			{ -1, -1, -1, -1, -1,   0, 0, 0, 0, 0}
		},
		{   // Layer 3
			{ -1, -1, -1, -1, -1,   0, 0, 0, 0, 0},
			{ -1,  0,  0,109, -1,   0, 0, 0, 6, 0},
			{ -1,  0, 98,109, -1,   0, 0, 0, 3, 0},
			{ -1,  0,  0,  0, -1,   0, 0, 0, 0, 0},
			{ -1, -1, -1, -1, -1,   0, 0, 0, 0, 0}
		}
	};
	
	
	private StairsUp()
	{
	}
	
	public int getWidth() // Inner
	{
		return StairsUp.WIDTH;
	}
	public int getLength() // Middle
	{
		return StairsUp.LENGTH;
	}
	public int getHeight() // Outer
	{
		return StairsUp.HEIGHT;
	}
	
	public int[][][] getMatrix()
	{
		return this.matrix;
	}

	@Override
	public String getName()
	{
		return StairsUp.name;
	}

	@Override
	public Schematic rotate(int direction)
	{
		return HothUtils.rotateSchematic(direction, this);
	}
}