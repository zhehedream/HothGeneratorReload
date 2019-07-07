package biz.orgin.minecraft.hothgenerator.schematic;

import biz.orgin.minecraft.hothgenerator.HothUtils;

public class Decor3 implements Schematic
{
	private static final long serialVersionUID = -9184125780585214294L;
	public static Schematic instance = new Decor3();
	private static int WIDTH = 3;
	private static int LENGTH = 3;
	private static int HEIGHT = 3;
	private static String name = "Decor3";
	
	private final int[][][] matrix = new int[][][]
	{
		    //   TYPEID                       DATAID
		{   // Layer 0
			{ -1, 44, -1,   0, 0, 0},
			{ 44,  8, 44,   0, 0, 0},
			{ -1, 44, -1,   0, 0, 0}
		},
		{   // Layer 1
			{ -1, 44, -1,   0, 0, 0},
			{ 44,  8, 44,   0, 0, 0},
			{ -1, 44, -1,   0, 0, 0}
		},
		{   // Layer 2
			{ -1, 44, -1,   0, 0, 0},
			{ 44,  8, 44,   0, 0, 0},
			{ -1, 44, -1,   0, 0, 0}
		}
	};
	
	private Decor3()
	{

	}
	
	public int getWidth() // Inner
	{
		return Decor3.WIDTH;
	}
	public int getLength() // Middle
	{
		return Decor3.LENGTH;
	}
	public int getHeight() // Outer
	{
		return Decor3.HEIGHT;
	}
	
	public int[][][] getMatrix()
	{
		return this.matrix;
	}

	@Override
	public String getName()
	{
		return Decor3.name;
	}

	@Override
	public Schematic rotate(int direction)
	{
		return HothUtils.rotateSchematic(direction, this);
	}
}