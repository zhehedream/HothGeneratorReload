package biz.orgin.minecraft.hothgenerator.schematic;

import biz.orgin.minecraft.hothgenerator.HothUtils;

public class RotatedSchematic implements Schematic
{
	private static final long serialVersionUID = 2511207124250593853L;
	private int width;
	private int length;
	private int height;
	private String name;
	
	private int[][][] matrix;
	
	public RotatedSchematic(String name, int width, int length, int height, int[][][]matrix)
	{
		this.width = width;
		this.length = length;
		this.height = height;
		this.name = name;
		this.matrix = matrix;
	}

	@Override
	public int[][][] getMatrix() {
		return matrix;
	}

	@Override
	public int getWidth() {
		return this.width;
	}

	@Override
	public int getLength() {
		return this.length;
	}

	@Override
	public int getHeight() {
		return this.height;
	}

	@Override
	public String getName() {
		return this.name;
	}

	public Schematic rotate(int direction)
	{
		return HothUtils.rotateSchematic(direction, this);
	}
}
