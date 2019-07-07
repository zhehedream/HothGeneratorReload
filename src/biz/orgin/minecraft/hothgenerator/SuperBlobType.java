package biz.orgin.minecraft.hothgenerator;

import java.io.Serializable;

public enum SuperBlobType implements Serializable
{
	GENERIC ("generic"),
	KASHYYYK_TREE ("kashyyyk_tree");
	
	private String name;
	
	private SuperBlobType(String name)
	{
		this.name = name;
	}
	
	public String getName()
	{
		return this.name;
	}

	public String toString()
	{
		return this.name;
	}
}
