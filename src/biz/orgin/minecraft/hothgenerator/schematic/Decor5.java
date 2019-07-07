package biz.orgin.minecraft.hothgenerator.schematic;

import biz.orgin.minecraft.hothgenerator.HothUtils;

public class Decor5 implements Schematic
{
	private static final long serialVersionUID = -570188186942233215L;
	public static Schematic instance = new Decor5();
	private static int WIDTH = 3;
	private static int LENGTH = 3;
	private static int HEIGHT = 3;
	private static String name = "Decor5";
	
	private final int[][][] matrix = new int[][][]
	{
		    //   TYPEID                       DATAID
		{   // Layer 0
			{ 44, 44, 44,   0, 0, 0},
			{ 44, 44, 44,   0, 0, 0},
			{ 44, 44, 44,   0, 0, 0}
		},
		{   // Layer 1
			{ 43, 89, 43,   0, 0, 0},
			{ 89, 98, 89,   0, 0, 0},
			{ 43, 89, 43,   0, 0, 0}
		},
		{   // Layer 2
			{ 43, 43, 43,   0, 0, 0},
			{ 43, 98, 43,   0, 0, 0},
			{ 43, 43, 43,   0, 0, 0}
		}
	};
	
	private Decor5()
	{
	}
	
	public int getWidth() // Inner
	{
		return Decor5.WIDTH;
	}
	public int getLength() // Middle
	{
		return Decor5.LENGTH;
	}
	public int getHeight() // Outer
	{
		return Decor5.HEIGHT;
	}
	
	public int[][][] getMatrix()
	{
		return this.matrix;
	}

	@Override
	public String getName()
	{
		return Decor5.name;
	}

	@Override
	public Schematic rotate(int direction)
	{
		return HothUtils.rotateSchematic(direction, this);
	}
}