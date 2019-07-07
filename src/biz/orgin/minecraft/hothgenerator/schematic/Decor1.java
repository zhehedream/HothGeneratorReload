package biz.orgin.minecraft.hothgenerator.schematic;

import biz.orgin.minecraft.hothgenerator.HothUtils;

public class Decor1 implements Schematic
{
	private static final long serialVersionUID = -6280819354950592146L;
	public static Schematic instance = new Decor1();
	private static int WIDTH = 3;
	private static int LENGTH = 3;
	private static int HEIGHT = 3;
	private static String name = "Decor1";
	
	private final int[][][] matrix = new int[][][]
	{
		    //   TYPEID                       DATAID
		{   // Layer 0
			{ -1, -1, -1,   0, 0, 0},
			{ -1, 43, -1,   0, 0, 0},
			{ -1, -1, -1,   0, 0, 0}
		},
		{   // Layer 1
			{ -1, -1, -1,   0, 0, 0},
			{ -1, 43, -1,   0, 0, 0},
			{ -1, -1, -1,   0, 0, 0}
		},
		{   // Layer 2
			{ -1, 44, -1,   0, 0, 0},
			{ 44, 43, 44,   0, 0, 0},
			{ -1, 44, -1,   0, 0, 0}
		}
	};
	
	private Decor1()
	{
	}
	
	public int getWidth() // Inner
	{
		return Decor1.WIDTH;
	}
	public int getLength() // Middle
	{
		return Decor1.LENGTH;
	}
	public int getHeight() // Outer
	{
		return Decor1.HEIGHT;
	}
	
	public int[][][] getMatrix()
	{
		return this.matrix;
	}

	@Override
	public String getName()
	{
		return Decor1.name;
	}

	@Override
	public Schematic rotate(int direction)
	{
		return HothUtils.rotateSchematic(direction, this);
	}
}
