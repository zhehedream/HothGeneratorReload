package biz.orgin.minecraft.hothgenerator;

import java.util.HashMap;

import org.bukkit.World;

public class LavaLevelGenerator 
{
	private static HashMap<World, NoiseGenerator> generators = new HashMap<World, NoiseGenerator>();
	
	public static double getLavaLevelAt(World world, int x, int z, int surfaceOffset)
	{
		NoiseGenerator noiseGenerator = generators.get(world);
		if(noiseGenerator==null)
		{
			noiseGenerator = new NoiseGenerator(world);
			generators.put(world, noiseGenerator);
		}
		
		double ll = noiseGenerator.noise(x, z, 2, 635)*36;
		return 64 + surfaceOffset + ll - 18;
	}

	public static double getLavaLevelFractionAt(World world, int x, int z)
	{
		NoiseGenerator noiseGenerator = generators.get(world);
		if(noiseGenerator==null)
		{
			noiseGenerator = new NoiseGenerator(world);
			generators.put(world, noiseGenerator);
		}
		
		double ll = noiseGenerator.noise(x, z, 2, 635);
		return ll;
	}

	public static double getLavaLevelAt(NoiseGenerator noiseGenerator, int x, int z, int surfaceOffset)
	{
		double ll = noiseGenerator.noise(x, z, 2, 635)*36;
		return 64 + surfaceOffset + ll - 18;
	}
	
	public static NoiseGenerator getNoiseGenerator(World world)
	{
		NoiseGenerator noiseGenerator = generators.get(world);
		if(noiseGenerator==null)
		{
			noiseGenerator = new NoiseGenerator(world);
			generators.put(world, noiseGenerator);
		}
		return noiseGenerator;
	}

}
