package biz.orgin.minecraft.hothgenerator;

import java.util.HashSet;

public class IntSet extends HashSet<Integer>
{
	private static final long serialVersionUID = -5198684673071092609L;

	public IntSet(int[] values)
	{
		for(int i=0;i<values.length;i++)
		{
			this.add(new Integer(values[i]));
		}
	}
	
	public boolean contains(int value)
	{
		return this.contains(new Integer(value));
	}
}
