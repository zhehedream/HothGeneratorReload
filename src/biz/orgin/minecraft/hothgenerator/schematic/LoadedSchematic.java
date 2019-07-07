package biz.orgin.minecraft.hothgenerator.schematic;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.bukkit.World;

import biz.orgin.minecraft.hothgenerator.HothUtils;
import biz.orgin.minecraft.hothgenerator.LootGenerator;


public class LoadedSchematic implements Schematic
{
	private static final long serialVersionUID = 399844744818570056L;
	private int width;
	private int length;
	private int height;
	private String name;
	
	private boolean enabled;
	private int type;
	private int yoffset;
	private int rarity;
	private int random;
	private String loot;
	private int lootMin;
	private int lootMax;
	private String[] worlds;

	private int[][][] matrix;
	

	public LoadedSchematic(File file) throws IOException
	{
		this.width = 0;
		this.length = 0;
		this.height = 0;
		this.name = "";
		this.enabled = false;
		this.type = -1;
		this.yoffset = 0;
		this.rarity = 0;
		this.random = -1;
		this.loot = "";
		this.lootMin = -1;
		this.lootMax = -1;
		this.worlds = null;
		this.matrix = null;
		
		this.name = file.getName();
		this.load(file);
	}
	
	private LoadedSchematic()
	{
		
	}
	
	public LoadedSchematic(InputStream stream, String name) throws IOException
	{
		this.width = 0;
		this.length = 0;
		this.height = 0;
		this.name = "";
		this.enabled = false;
		this.type = -1;
		this.yoffset = 0;
		this.rarity = 0;
		this.random = -1;
		this.loot = "";
		this.lootMin = -1;
		this.lootMax = -1;
		this.worlds = null;
		this.matrix = null;
		
		this.name = name;
		
		this.load(new BufferedReader(new InputStreamReader(stream)));
	}
	
	private void load(File file) throws IOException
	{
		BufferedReader reader = new BufferedReader(new FileReader(file));
		this.load(reader);
	}
	
	private void load(BufferedReader reader) throws IOException
	{
		int mode = 0; // 0 - reading tags, 1 - reading matrix
		String line = "";
		int _layer = 0;
		int _row = 0;
		
		while( (line=reader.readLine())!=null)
		{
			String row = line.trim();
			if(row.length()>0 &&  row.charAt(0)!=';' && row.charAt(0)!='#')
			{
				switch(mode)
				{
				case 0:
					String[] data = row.split(":",2);
					if(data.length==2)
					{
						String key = data[0].toUpperCase();
						String value = data[1].trim();
						
						if(key.equals("ENABLED"))
						{
							if(value.toLowerCase().equals("true"))
							{
								this.enabled = true;
							}
							else if(value.toLowerCase().equals("false"))
							{
								this.enabled = false;
							}
							else
							{
								throw new IOException("ENABLED must be true or false but was set to: " + value);
							}
						}
						else if(key.equals("WIDTH"))
						{
							try
							{
								this.width = Integer.parseInt(value);
							}
							catch(NumberFormatException e)
							{
								throw new IOException("WIDTH contains illegal characters: " + value);
							}
							
							if(this.width<1)
							{
								throw new IOException("WIDTH must be 1 or more, was: " + value);
							}
						}
						else if(key.equals("LENGTH"))
						{
							try
							{
								this.length = Integer.parseInt(value);
							}
							catch(NumberFormatException e)
							{
								throw new IOException("LENGTH contains illegal characters: " + value);
							}
							if(this.length<1)
							{
								throw new IOException("LENGTH must be 1 or more, was: " + value);
							}
						}
						else if(key.equals("HEIGHT"))
						{
							try
							{
								this.height = Integer.parseInt(value);
							}
							catch(NumberFormatException e)
							{
								throw new IOException("HEIGHT contains illegal characters: " + value);
							}
							if(this.height<1)
							{
								throw new IOException("HEIGHT must be 1 or more, was: " + value);
							}
						}
						else if(key.equals("TYPE"))
						{
							try
							{
								this.type = Integer.parseInt(value);
							}
							catch(NumberFormatException e)
							{
								throw new IOException("TYPE contains illegal characters: " + value);
							}

							if(this.type!=0 && this.type!=1 && type!=2)
							{
								throw new IOException("TYPE must be 0,1 or 2, was: " + value);
							}
						}
						else if(key.equals("YOFFSET"))
						{
							try
							{
								this.setYoffset(Integer.parseInt(value));
							}
							catch(NumberFormatException e)
							{
								throw new IOException("YOFFSET contains illegal characters: " + value);
							}
						}
						else if(key.equals("RARITY"))
						{
							try
							{
								this.rarity = Integer.parseInt(value);
							}
							catch(NumberFormatException e)
							{
								throw new IOException("RARITY contains illegal characters: " + value);
							}
							
							if(this.rarity<0)
							{
								throw new IOException("RARITY must be 0 or more, was: " + value);
							}
						}
						else if(key.equals("RANDOM"))
						{
							try
							{
								this.random = Integer.parseInt(value);
							}
							catch(NumberFormatException e)
							{
								throw new IOException("RANDOM contains illegal characters: " + value);
							}
							
							if(this.random<0)
							{
								throw new IOException("RANDOM must be 0 or more, was: " + value);
							}
						}
						else if(key.equals("LOOT"))
						{
							this.loot = value;
							if(!value.equals(""))
							{
								LootGenerator generator = LootGenerator.getLootGenerator(value);
								if(generator==null)
								{
									throw new IOException("Unknown Loot List: " + value);
								}
							}
						}
						else if(key.equals("LOOTMIN"))
						{
							try
							{
								this.lootMin = Integer.parseInt(value);
							}
							catch(NumberFormatException e)
							{
								throw new IOException("LOOTMIN contains illegal characters: " + value);
							}
							
							if(this.lootMin<0)
							{
								throw new IOException("LOOTMIN must be 0 or more, was: " + value);
							}
						}
						else if(key.equals("LOOTMAX"))
						{
							try
							{
								this.lootMax = Integer.parseInt(value);
							}
							catch(NumberFormatException e)
							{
								throw new IOException("LOOTMAX contains illegal characters: " + value);
							}
							
							if(this.lootMax<1)
							{
								throw new IOException("LOOTMAX must be 1 or more, was: " + value);
							}
						}
						else if(key.equals("WORLDS"))
						{
							this.worlds = null;
							if(!value.equals(""))
							{
								this.worlds = value.split(",");
							}
						}
						else if(key.equals("MATRIX"))
						{
							if(this.width==0)
							{
								throw new IOException("WIDTH was not defined properly");
							}
							if(this.length==0)
							{
								throw new IOException("LENGTH was not defined properly");
							}
							if(this.height==0)
							{
								throw new IOException("HEIGHT was not defined properly");
							}
							
							this.matrix = new int[this.height][this.length][this.width*2];
							_layer = 0;
							_row = 0;
							mode = 1;
						}
					}
					break;
				case 1:
					String[] vals = line.split(",");
					if(vals.length!=this.width*2)
					{
						throw new IOException("MATRIX row contained incorrect number of values: " + line);
					}
					
					if(_layer==this.height)
					{
						throw new IOException("MATRIX contains too many rows");
					}
					
					for(int i=0;i<this.width*2;i++)
					{
						try
						{
							int val = Integer.parseInt(vals[i].trim());
							this.matrix[_layer][_row][i] = val;
						}
						catch(Exception e)
						{
							throw new IOException("MATRIX row contained illegal characters: " + line);
						}
					}
					
					_row++;
					if(_row==this.length)
					{
						_layer++;
						_row = 0;
					}
					

					break;
				}
			}
		}
		

		if(this.type<0)
		{
			throw new IOException("TYPE was not defined properly");
		}
		if(this.rarity<0)
		{
			throw new IOException("RARITY was not defined properly");
		}
		if(this.random==-1 || random>=this.rarity && this.rarity>0)
		{
			throw new IOException("RANDOM was not defined properly. Must be below RARITY.");
		}
		if(this.matrix==null)
		{
			throw new IOException("Could not read the MATRIX data.");
		}
		
		if(!(_layer==this.height && _row==0))
		{
			throw new IOException("MATRIX does not contain enough rows.");
		}
		
		if(this.lootMin==-1)
		{
			throw new IOException("LOOTMIN was not set.");
		}

		if(this.lootMax==-1)
		{
			throw new IOException("LOOTMAX was not set.");
		}
		
		if(this.lootMin > this.lootMax)
		{
			throw new IOException("LOOTMIN is higher that LOOTMAX.");
		}
		
		if(this.lootMax<1)
		{
			throw new IOException("LOOTMAX must be 1 or more.");
		}
	}
	

	@Override
	public int[][][] getMatrix() {
		return this.matrix;
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
	
	public LoadedSchematic cloneRotate(int direction, String name)
	{
		
		LoadedSchematic schem = this.cloneRotate(direction);
		schem.name = name;
		return schem;
	}
	public LoadedSchematic cloneRotate(int direction)
	{
		LoadedSchematic newSchematic = new LoadedSchematic();
		
		Schematic schematic = HothUtils.rotateSchematic(direction, this);
		
		newSchematic.width = schematic.getWidth();
		newSchematic.length = schematic.getLength();
		newSchematic.height = schematic.getHeight();
		newSchematic.name = schematic.getName();
		
		newSchematic.enabled = this.enabled;
		newSchematic.type = this.type;
		newSchematic.yoffset = this.yoffset;
		newSchematic.rarity = this.rarity;
		newSchematic.random = this.random;
		newSchematic.loot = this.loot;
		newSchematic.lootMin = this.lootMin;
		newSchematic.lootMax = this.lootMax;
		newSchematic.matrix = schematic.getMatrix();
		
		return newSchematic;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getRarity() {
		return rarity;
	}

	public void setRarity(int rarity) {
		this.rarity = rarity;
	}

	public int getRandom() {
		return random;
	}

	public void setRandom(int random) {
		this.random = random;
	}

	public String getLoot() {
		return loot;
	}
	
	@Override
	public String toString()
	{
		StringBuffer mySB = new StringBuffer();
		
		mySB.append("WIDTH=").append(this.width).append("\n");
		mySB.append("LENGTH=").append(this.length).append("\n");
		mySB.append("HEIGHT=").append(this.height).append("\n");
		mySB.append("NAME=").append(this.name).append("\n");
		mySB.append("TYPE=").append(this.type).append("\n");
		mySB.append("YOFFSET=").append(this.yoffset).append("\n");
		mySB.append("RARITY=").append(this.rarity).append("\n");
		mySB.append("RANDOM=").append(this.random).append("\n");
		mySB.append("LOOT=").append(this.loot).append("\n");
		
		for(int y=0;y<this.height;y++)
		{
			mySB.append("Layer ").append(y).append(":\n");
			for(int z=0;z<this.length;z++)
			{
				for(int x=0;x<this.width;x++)
				{
					mySB.append(this.matrix[y][z][x]).append(" ");
				}
				
				mySB.append("- ");

				for(int x=this.width;x<this.width*2;x++)
				{
					mySB.append(this.matrix[y][z][x]).append(" ");
				}
				mySB.append("\n");
			}
		}
		
		return mySB.toString();
	}
	
	public boolean hasWorld(World world)
	{
		if(this.worlds == null || this.worlds.length == 0)
		{
			return true;
		}
		
		String name = world.getName();
		for(int i=0;i<this.worlds.length;i++)
		{
			if(name.equals(this.worlds[i]))
			{
				return true;
			}
		}
		
		return false;
	}


	public boolean isEnabled() {
		return enabled;
	}

	public int getLootMin() {
		return lootMin;
	}

	public int getLootMax() {
		return lootMax;
	}
	
	public String[] getWorlds()
	{
		return this.worlds;
	}

	public int getYoffset() {
		return yoffset;
	}

	public void setYoffset(int yoffset) {
		this.yoffset = yoffset;
	}
}
