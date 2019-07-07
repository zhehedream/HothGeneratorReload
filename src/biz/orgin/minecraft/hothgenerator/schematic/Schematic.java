package biz.orgin.minecraft.hothgenerator.schematic;

import java.io.Serializable;

public interface Schematic extends Serializable
{
	public int[][][] getMatrix();
	public int getWidth();
	public int getLength();
	public int getHeight();
	public String getName();
	public Schematic rotate(int direction);
}
