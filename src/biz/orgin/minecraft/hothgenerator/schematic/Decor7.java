package biz.orgin.minecraft.hothgenerator.schematic;

import biz.orgin.minecraft.hothgenerator.HothUtils;

public class Decor7 implements Schematic
{
	private static final long serialVersionUID = -6628207824728294916L;
	public static Schematic instance = new Decor7();
	private static int WIDTH = 3;
	private static int LENGTH = 3;
	private static int HEIGHT = 3;
	private static String name = "Decor7";
	
	private final int[][][] matrix = new int[][][]
	{
		    //   TYPEID                       DATAID
		{   // Layer 0
			{ -1, -1, -1,   0, 0, 0},
			{ -1, -1, -1,   0, 0, 0},
			{ -1, -1, -1,   0, 0, 0}
		},
		{   // Layer 1
			{ -1, -1, -1,   0, 0, 0},
			{ -1, 54, -1,   0, 3, 0},
			{ -1, -1, -1,   0, 0, 0}
		},
		{   // Layer 2
			{ -1,109, -1,   0, 6, 0},
			{109, 98,109,   4, 3, 5},
			{ -1,109, -1,   0, 7, 0}
		}
	};
	
	private Decor7()
	{
	}
	
	public int getWidth() // Inner
	{
		return Decor7.WIDTH;
	}
	public int getLength() // Middle
	{
		return Decor7.LENGTH;
	}
	public int getHeight() // Outer
	{
		return Decor7.HEIGHT;
	}
	
	public int[][][] getMatrix()
	{
		return this.matrix;
	}

	@Override
	public String getName()
	{
		return Decor7.name;
	}

	@Override
	public Schematic rotate(int direction)
	{
		return HothUtils.rotateSchematic(direction, this);
	}
}