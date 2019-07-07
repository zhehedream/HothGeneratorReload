package biz.orgin.minecraft.hothgenerator.schematic;

import biz.orgin.minecraft.hothgenerator.HothUtils;

public class Decor4 implements Schematic
{
	private static final long serialVersionUID = 3643166865894119466L;
	public static Schematic instance = new Decor4();
	private static int WIDTH = 3;
	private static int LENGTH = 3;
	private static int HEIGHT = 3;
	private static String name = "Decor4";
	
	private final int[][][] matrix = new int[][][]
	{
		    //   TYPEID                       DATAID
		{   // Layer 0
			{ -1, -1, -1,   0, 0, 0},
			{ -1,124, -1,   0, 3, 0},
			{ -1, -1, -1,   0, 0, 0}
		},
		{   // Layer 1
			{ -1, -1, -1,   0, 0, 0},
			{ -1,124, -1,   0, 0, 0},
			{ -1, -1, -1,   0, 0, 0}
		},
		{   // Layer 2
			{ -1,109, -1,   0, 6, 0},
			{109, 76,109,   4, 0, 5},
			{ -1,109, -1,   0, 7, 0}
		}
	};
	
	private Decor4()
	{
	}
	
	public int getWidth() // Inner
	{
		return Decor4.WIDTH;
	}
	public int getLength() // Middle
	{
		return Decor4.LENGTH;
	}
	public int getHeight() // Outer
	{
		return Decor4.HEIGHT;
	}
	
	public int[][][] getMatrix()
	{
		return this.matrix;
	}

	@Override
	public String getName()
	{
		return Decor4.name;
	}

	@Override
	public Schematic rotate(int direction)
	{
		return HothUtils.rotateSchematic(direction, this);
	}
}