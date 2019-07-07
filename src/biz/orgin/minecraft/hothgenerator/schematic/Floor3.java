package biz.orgin.minecraft.hothgenerator.schematic;

import biz.orgin.minecraft.hothgenerator.HothUtils;

public class Floor3 implements Schematic
{
	private static final long serialVersionUID = 1921530602710847072L;
	public static Schematic instance = new Floor3();
	private static int WIDTH = 7;
	private static int LENGTH = 7;
	private static int HEIGHT = 1;
	private static String name = "Floor3";
	
	private final int[][][] matrix = new int[][][]
	{
		    //   TYPEID                       DATAID
		{   // Layer 0
			{ -1, -1, -1, -1, -1, -1, -1,   0, 0, 0, 0, 0, 0, 0},
			{ -1, -1,109,109,109, -1, -1,   0, 0, 2, 2, 2, 0, 0},
			{ -1,109, 43, 43, 43,109, -1,   0, 0, 0, 0, 0, 1, 0},
			{ -1,109, 43, 89, 43,109, -1,   0, 0, 0, 0, 0, 1, 0},
			{ -1,109, 43, 43, 43,109, -1,   0, 0, 0, 0, 0, 1, 0},
			{ -1, -1,109,109,109, -1, -1,   0, 0, 3, 3, 3, 0, 0},
			{ -1, -1, -1, -1, -1, -1, -1,   0, 0, 0, 0, 0, 0, 0}
		},
	};
	
	private Floor3()
	{
	}
	
	public int getWidth() // Inner
	{
		return Floor3.WIDTH;
	}
	public int getLength() // Middle
	{
		return Floor3.LENGTH;
	}
	public int getHeight() // Outer
	{
		return Floor3.HEIGHT;
	}
	
	public int[][][] getMatrix()
	{
		return this.matrix;
	}

	@Override
	public String getName()
	{
		return Floor3.name;
	}

	@Override
	public Schematic rotate(int direction)
	{
		return HothUtils.rotateSchematic(direction, this);
	}
}