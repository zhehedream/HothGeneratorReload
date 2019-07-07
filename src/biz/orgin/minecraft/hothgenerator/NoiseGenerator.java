package biz.orgin.minecraft.hothgenerator;

import org.bukkit.World;
import org.bukkit.util.noise.PerlinNoiseGenerator;

/**
 * Used to generate perlin noise for the terrain generator
 * @author orgin
 *
 */
public class NoiseGenerator 
{
	private PerlinNoiseGenerator generator;
	
	public NoiseGenerator(int seed)
	{
		this.generator = new PerlinNoiseGenerator(seed);
	}

	public NoiseGenerator(World world)
	{
		this.generator = new PerlinNoiseGenerator(world);
	}

	public double noise(int x, int y, int z, int octaves, double zoom)
	{
		double noise;
		
		if(octaves<1)
		{
			octaves = 1;
		}
		
		if(octaves>10)
		{
			octaves = 10;
		}
		
		noise = this.generator.noise((double)x/zoom, (double)y/zoom, (double)z/zoom, (int)octaves, 1f, 1, true);
		
		noise = (noise+1.0)/2;
		
		if(noise<0)
		{
			return 0;
		}
		
		if(noise>=1)
		{
			return 0.9999999f;
		}
		
		return noise;
	}
	
	
	public double noise(int x, int z, int octaves, double zoom)
	{
		double noise, offset = 0, div = 1.0;
		
		if(octaves<1)
		{
			octaves = 1;
		}
		
		if(octaves>10)
		{
			octaves = 10;
		}
		
		switch(octaves)
		{ // Define values to adjust the perlins noise generator output to be more controllable.
		case 1: //min = -0.5517347088168423 max = 0.5373601776560322 diff 1.0890948864728744
			offset = 0.5517347088168423f;
			div = 1.0890948864728744f;
			break;
		case 2: //min = -0.6375992913346601 max = 0.6545163185689686 diff 1.2921156099036288
			offset = 0.6375992913346601f;
			div = 1.2921156099036288f;
			break;
		case 3: //min = -0.6195292448202216 max = 0.6133642574213009 diff 1.2328935022415224
			offset = 0.6195292448202216f;
			div = 1.2328935022415224f;
			break;
		case 4: //min = -0.5321022024733646 max = 0.5118030786697235 diff 1.043905281143088
			offset = 0.5321022024733646f;
			div = 1.043905281143088f;
			break;
		case 5: //min = -0.40902603444590824 max = 0.5078504882541681 diff 0.9168765227000764
			offset = 0.40902603444590824;
			div = 0.9168765227000764;
			break;
		case 6: //min = -0.3374881789256107 max = 0.43090844264169387 diff 0.7683966215673046
			offset = 0.3374881789256107;
			div = 0.7683966215673046;
			break;
		case 7: //min = -0.2879264178925265 max = 0.3713734317964098 diff 0.6592998496889363
			offset = 0.2879264178925265;
			div = 0.6592998496889363;
			break;
		case 8: //min = -0.2509012622339434 max = 0.32604772296088474 diff 0.5769489851948282
			offset = 0.2509012622339434;
			div = 0.5769489851948282;
			break;
		case 9: //min = -0.22211701434645162 max = 0.29073202831064304 diff 0.5128490426570946
			offset = 0.22211701434645162;
			div = 0.5128490426570946;
			break;
		case 10: //min = -0.19909079554807718 max = 0.2624738380757258 diff 0.46156463362380296
			offset = 0.19909079554807718;
			div = 0.46156463362380296;
			break;
		}
		
		noise = this.generator.noise((double)x/zoom, (double)z/zoom, (int)octaves, 1f, 1, true);

		noise = offset + noise;
		noise = noise/div;
		
		if(noise<0)
		{
			return 0;
		}
		
		if(noise>=1)
		{
			return 0.9999999f;
		}

		return noise;
		
	}
}
