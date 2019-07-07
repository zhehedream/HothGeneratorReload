package biz.orgin.minecraft.hothgenerator.schematic;

import biz.orgin.minecraft.hothgenerator.HothUtils;

public class Decor8 implements Schematic
{
	private static final long serialVersionUID = 4551137110622919531L;
	public static Schematic instance = new Decor8();
	private static int WIDTH = 3;
	private static int LENGTH = 3;
	private static int HEIGHT = 3;
	private static String name = "Decor8";
	
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
			{ -1, 52, -1,   0, 0, 0},
			{ -1, -1, -1,   0, 0, 0}
		},
		{   // Layer 2
			{ -1, -1, -1,   0, 0, 0},
			{ -1, 98, -1,   0, 3, 5},
			{ -1, -1, -1,   0, 7, 0}
		}
	};
	
	private Decor8()
	{
	}
	
	public int getWidth() // Inner
	{
		return Decor8.WIDTH;
	}
	public int getLength() // Middle
	{
		return Decor8.LENGTH;
	}
	public int getHeight() // Outer
	{
		return Decor8.HEIGHT;
	}
	
	public int[][][] getMatrix()
	{
		return this.matrix;
	}

	@Override
	public String getName()
	{
		return Decor8.name;
	}

	@Override
	public Schematic rotate(int direction)
	{
		return HothUtils.rotateSchematic(direction, this);
	}
}