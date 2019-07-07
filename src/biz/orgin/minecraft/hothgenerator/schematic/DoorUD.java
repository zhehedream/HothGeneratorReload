package biz.orgin.minecraft.hothgenerator.schematic;

import biz.orgin.minecraft.hothgenerator.HothUtils;

public class DoorUD  implements Schematic
{
	private static final long serialVersionUID = 845482045029535701L;
	public static Schematic instance = new DoorUD();
	private static int WIDTH = 3;
	private static int LENGTH = 3;
	private static int HEIGHT = 1;
	private static String name = "DoorUD";
	
	private final int[][][] matrix = new int[][][]
	{
		    //   TYPEID            DATAID
		{   // Layer 0
			{ 98, 98, 98,   0, 0, 0},
			{ 98, 98, 98,   0, 0, 0},
			{ 98, 98, 98,   0, 0, 0},
		},
	};
	
	private DoorUD()
	{
	}
	
	public int getWidth() // Inner
	{
		return DoorUD.WIDTH;
	}
	public int getLength() // Middle
	{
		return DoorUD.LENGTH;
	}
	public int getHeight() // Outer
	{
		return DoorUD.HEIGHT;
	}
	
	public int[][][] getMatrix()
	{
		return this.matrix;
	}

	@Override
	public String getName()
	{
		return DoorUD.name;
	}

	@Override
	public Schematic rotate(int direction)
	{
		return HothUtils.rotateSchematic(direction, this);
	}
}