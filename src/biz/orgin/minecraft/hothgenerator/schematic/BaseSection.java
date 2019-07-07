package biz.orgin.minecraft.hothgenerator.schematic;

import biz.orgin.minecraft.hothgenerator.HothUtils;

public class BaseSection implements Schematic
{
	private static final long serialVersionUID = 7567347748044582242L;
	public static Schematic instance = new BaseSection();
	private static int WIDTH = 7;
	private static int LENGTH = 7;
	private static int HEIGHT = 5;
	private static String name = "BaseSection";
	
	private final int[][][] matrix = new int[][][]
	{
		    //   TYPEID                       DATAID
		{   // Layer 0
			{ -1, -1,  4,  4,  4, -1, -1,   0, 0, 0, 0 ,0 ,0 ,0},
			{ -1,  4,  0,  0,  0,  4, -1,   0, 0, 0, 0 ,0 ,0 ,0},
			{  4,  0,  0,  0,  0,  0,  4,   0, 0, 0, 0 ,0 ,0 ,0},
			{  4,  0,  0,  4,  0,  0,  4,   0, 0, 0, 0 ,0 ,0 ,0},
			{  4,  0,  0, 65,  0,  0,  4,   0, 0, 0, 3 ,0 ,0 ,0},
			{ -1,  4,  0,  0,  0,  4, -1,   0, 0, 0, 0 ,0 ,0 ,0},
			{ -1, -1,  4,  4,  4, -1, -1,   0, 0, 0, 0 ,0 ,0 ,0},
		},
		{   // Layer 1
			{ -1, -1,  4,  4,  4, -1, -1,   0, 0, 0, 0 ,0 ,0 ,0},
			{ -1,  4,  0, 50,  0,  4, -1,   0, 0, 0, 3 ,0 ,0 ,0},
			{  4,  0,  0,  0,  0,  0,  4,   0, 0, 0, 0 ,0 ,0 ,0},
			{  4, 50,  0,  4,  0, 50,  4,   0, 1, 0, 0 ,0 ,2 ,0},
			{  4,  0,  0, 65,  0,  0,  4,   0, 0, 0, 3 ,0 ,0 ,0},
			{ -1,  4,  0, 50,  0,  4, -1,   0, 0, 0, 4 ,0 ,0 ,0},
			{ -1, -1,  4,  4,  4, -1, -1,   0, 0, 0, 0 ,0 ,0 ,0},
		},
		{   // Layer 2
			{ -1, -1,  4, -1,  4, -1, -1,   0, 0, 0, 0 ,0 ,0 ,0},
			{ -1,  4,  0,  0,  0,  4, -1,   0, 0, 0, 0 ,0 ,0 ,0},
			{  4,  0,  0,  0,  0,  0,  4,   0, 0, 0, 0 ,0 ,0 ,0},
			{ -1,  0,  0,  4,  0,  0, -1,   0, 0, 0, 0 ,0 ,0 ,0},
			{  4,  0,  0, 65,  0,  0,  4,   0, 0, 0, 3 ,0 ,0 ,0},
			{ -1,  4,  0,  0,  0,  4, -1,   0, 0, 0, 0 ,0 ,0 ,0},
			{ -1, -1,  4, -1,  4, -1, -1,   0, 0, 0, 0 ,0 ,0 ,0},
		},
		{   // Layer 3
			{ -1, -1,  4, -1,  4, -1, -1,   0, 0, 0, 0 ,0 ,0 ,0},
			{ -1,  4,  0,  0,  0,  4, -1,   0, 0, 0, 0 ,0 ,0 ,0},
			{  4,  0,  0,  0,  0,  0,  4,   0, 0, 0, 0 ,0 ,0 ,0},
			{ -1,  0,  0,  4,  0,  0, -1,   0, 0, 0, 0 ,0 ,0 ,0},
			{  4,  0,  0, 65,  0,  0,  4,   0, 0, 0, 3 ,0 ,0 ,0},
			{ -1,  4,  0,  0,  0,  4, -1,   0, 0, 0, 0 ,0 ,0 ,0},
			{ -1, -1,  4, -1,  4, -1, -1,   0, 0, 0, 0 ,0 ,0 ,0},
		},
		{   // Layer 4
			{ -1, -1,  4,  4,  4, -1, -1,   0, 0, 0, 0 ,0 ,0 ,0},
			{ -1,  4, 43, 43, 43,  4, -1,   0, 0, 0, 0 ,0 ,0 ,0},
			{  4, 43, 43, 43, 43, 43,  4,   0, 0, 0, 0 ,0 ,0 ,0},
			{  4, 43, 43,  4, 43, 43,  4,   0, 0, 0, 0 ,0 ,0 ,0},
			{  4, 43, 43, 65, 43, 43,  4,   0, 0, 0, 3 ,0 ,0 ,0},
			{ -1,  4, 43, 43, 43,  4, -1,   0, 0, 0, 0 ,0 ,0 ,0},
			{ -1, -1,  4,  4,  4, -1, -1,   0, 0, 0, 0 ,0 ,0 ,0},
		}
	};
	
	private BaseSection()
	{
	}
	
	public int getWidth() // Inner
	{
		return BaseSection.WIDTH;
	}
	public int getLength() // Middle
	{
		return BaseSection.LENGTH;
	}
	public int getHeight() // Outer
	{
		return BaseSection.HEIGHT;
	}
	
	public int[][][] getMatrix()
	{
		return this.matrix;
	}

	@Override
	public String getName()
	{
		return BaseSection.name;
	}
	
	@Override
	public Schematic rotate(int direction)
	{
		return HothUtils.rotateSchematic(direction, this);
	}
}