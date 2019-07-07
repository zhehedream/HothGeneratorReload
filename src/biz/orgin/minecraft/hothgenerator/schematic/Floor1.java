package biz.orgin.minecraft.hothgenerator.schematic;

import biz.orgin.minecraft.hothgenerator.HothUtils;

public class Floor1 implements Schematic
{
	private static final long serialVersionUID = -8542959724188969729L;
	public static Schematic instance = new Floor1();
	private static int WIDTH = 7;
	private static int LENGTH = 7;
	private static int HEIGHT = 1;
	private static String name = "Floor1";
	
	private final int[][][] matrix = new int[][][]
	{
		    //   TYPEID                       DATAID
		{   // Layer 0
			{ -1, -1, -1, -1, -1, -1, -1,   0, 0, 0, 0, 0, 0, 0},
			{ -1, -1, 98, 98, 98, -1, -1,   0, 0, 3, 3, 3, 0, 0},
			{ -1, 98, 43, 43, 43, 98, -1,   0, 3, 0, 0, 0, 3, 0},
			{ -1, 98, 43, 43, 43, 98, -1,   0, 3, 0, 0, 0, 3, 0},
			{ -1, 98, 43, 43, 43, 98, -1,   0, 3, 0, 0, 0, 3, 0},
			{ -1, -1, 98, 98, 98, -1, -1,   0, 0, 3, 3, 3, 0, 0},
			{ -1, -1, -1, -1, -1, -1, -1,   0, 0, 0, 0, 0, 0, 0}
		},
	};
	
	private Floor1()
	{
	}
	
	public int getWidth() // Inner
	{
		return Floor1.WIDTH;
	}
	public int getLength() // Middle
	{
		return Floor1.LENGTH;
	}
	public int getHeight() // Outer
	{
		return Floor1.HEIGHT;
	}
	
	public int[][][] getMatrix()
	{
		return this.matrix;
	}

	@Override
	public String getName()
	{
		return Floor1.name;
	}

	@Override
	public Schematic rotate(int direction)
	{
		return HothUtils.rotateSchematic(direction, this);
	}
}
