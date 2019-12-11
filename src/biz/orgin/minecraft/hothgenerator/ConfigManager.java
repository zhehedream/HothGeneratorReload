package biz.orgin.minecraft.hothgenerator;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;

import biz.orgin.minecraft.hothgenerator.WorldType.InvalidWorldTypeException;

public class ConfigManager
{
	
	private static FlagValue[] validWorldFlags = new FlagValue[] {
		new FlagValue("world.surfaceoffset",          new String[]{"[integer]", "[blank]"}, 0, 127, Integer.class),
		new FlagValue("structure.spikes.rarity",      new String[]{"[integer]", "[blank]"}, 0, 10, Integer.class),
		new FlagValue("structure.gardens.rarity",     new String[]{"[integer]", "[blank]"}, 0, 10, Integer.class),
		new FlagValue("structure.domes.rarity",       new String[]{"[integer]", "[blank]"}, 0, 10, Integer.class),
		new FlagValue("structure.domes.plantstem",    new String[]{"[integer]", "[blank]"}, 0, 255, Integer.class),
		new FlagValue("structure.domes.planttop",     new String[]{"[integer]", "[blank]"}, 0, 255, Integer.class),
		new FlagValue("structure.domes.floor",        new String[]{"[integer]", "[blank]"}, 0, 255, Integer.class),
		new FlagValue("structure.domes.floorrandom",  new String[]{"[integer]", "[blank]"}, 0, 255, Integer.class),
		new FlagValue("structure.domes.placeminidome",new String[]{"allow","deny", "[blank]"}, Boolean.class),
		new FlagValue("structure.bases.rarity",       new String[]{"[integer]", "[blank]"}, 0, 10, Integer.class),
		new FlagValue("structure.bases.spawner",      new String[]{"allow","deny", "[blank]"}, Boolean.class),
		new FlagValue("structure.mazes.rarity",       new String[]{"[integer]", "[blank]"}, 0, 10, Integer.class),
		new FlagValue("structure.mazes.minrooms",     new String[]{"[integer]", "[blank]"}, 1, 100, Integer.class),
		new FlagValue("structure.mazes.maxrooms",     new String[]{"[integer]", "[blank]"}, 1, 100, Integer.class),
		new FlagValue("structure.mazes.spawner",      new String[]{"allow","deny", "[blank]"}, Boolean.class),
		new FlagValue("structure.skeletons.rarity",   new String[]{"[integer]", "[blank]"}, 0, 10, Integer.class),
		new FlagValue("structure.oasis.rarity",       new String[]{"[integer]", "[blank]"}, 0, 10, Integer.class),
		new FlagValue("structure.sandcastle.rarity",  new String[]{"[integer]", "[blank]"}, 0, 10, Integer.class),
		new FlagValue("structure.village.rarity",     new String[]{"[integer]", "[blank]"}, 0, 10, Integer.class),
		new FlagValue("structure.supergarden.rarity", new String[]{"[integer]", "[blank]"}, 0, 10, Integer.class),
		new FlagValue("structure.sarlacc.rarity",     new String[]{"[integer]", "[blank]"}, 0, 10, Integer.class),
		new FlagValue("structure.hugetree.rarity",    new String[]{"[integer]", "[blank]"}, 0, 10, Integer.class),
		new FlagValue("structure.spiderforest.rarity",new String[]{"[integer]", "[blank]"}, 0, 10, Integer.class),
		new FlagValue("structure.swamptemple.rarity", new String[]{"[integer]", "[blank]"}, 0, 10, Integer.class),
		new FlagValue("structure.treehut.rarity",     new String[]{"[integer]", "[blank]"}, 0, 10, Integer.class),
		new FlagValue("generate.logs",                new String[]{"allow","deny", "[blank]"}, Boolean.class),
		new FlagValue("generate.caves.rarity",        new String[]{"[integer]", "[blank]"}, 0, 10, Integer.class),
		new FlagValue("generate.ores",                new String[]{"allow","deny", "[blank]"}, Boolean.class),
		new FlagValue("hoth.generate.extendedore",    new String[]{"allow","deny", "[blank]"}, Boolean.class),
		new FlagValue("generate.cactuses",            new String[]{"allow","deny", "[blank]"}, Boolean.class),
		new FlagValue("generate.shrubs",              new String[]{"allow","deny", "[blank]"}, Boolean.class),
		new FlagValue("generate.mushroomhuts",        new String[]{"allow","deny", "[blank]"}, Boolean.class),
		new FlagValue("rules.dropice",                new String[]{"allow","deny", "[blank]"}, Boolean.class),
		new FlagValue("rules.droppackedice",          new String[]{"allow","deny", "[blank]"}, Boolean.class),
		new FlagValue("rules.dropsnow",               new String[]{"allow","deny", "[blank]"}, Boolean.class),
		new FlagValue("rules.freezewater",            new String[]{"allow","deny", "[blank]"}, Boolean.class),
		new FlagValue("rules.freezelava",             new String[]{"allow","deny", "[blank]"}, Boolean.class),
		new FlagValue("rules.plantsgrow",             new String[]{"allow","deny", "[blank]"}, Boolean.class),
		new FlagValue("rules.grassspread",            new String[]{"allow","deny", "[blank]"}, Boolean.class),
		new FlagValue("rules.stopmelt",               new String[]{"allow","deny", "[blank]"}, Boolean.class),
		new FlagValue("rules.limitslime",             new String[]{"allow","deny", "[blank]"}, Boolean.class),
		new FlagValue("rules.snowgravity",            new String[]{"allow","deny", "[blank]"}, Boolean.class),
		new FlagValue("rules.environment.suit",       new String[]{"allow","deny", "[blank]"}, Boolean.class),
		new FlagValue("rules.freeze.damage",          new String[]{"[integer]", "[blank]"}, 0, 255, Integer.class),
		new FlagValue("rules.freeze.stormdamage",     new String[]{"[integer]", "[blank]"}, 0, 255, Integer.class),
		new FlagValue("rules.freeze.message",         new String[]{"[text]", "[blank]"}, String.class),
		new FlagValue("rules.heat.damage",            new String[]{"[integer]", "[blank]"}, 0, 255, Integer.class),
		new FlagValue("rules.heat.message1",          new String[]{"[text]", "[blank]"}, String.class),
		new FlagValue("rules.heat.message2",          new String[]{"[text]", "[blank]"}, String.class),
		new FlagValue("rules.heat.message3",          new String[]{"[text]", "[blank]"}, String.class),
		new FlagValue("rules.heat.message4",          new String[]{"[text]", "[blank]"}, String.class),
		new FlagValue("rules.mosquito.damage",        new String[]{"[integer]", "[blank]"}, 0, 255, Integer.class),
		new FlagValue("rules.mosquito.rarity",        new String[]{"[integer]", "[blank]"}, 1, 10, Integer.class),
		new FlagValue("rules.mosquito.runfree",       new String[]{"[integer]", "[blank]"}, 10, 100, Integer.class),
		new FlagValue("rules.mosquito.message1",      new String[]{"[text]", "[blank]"}, String.class),
		new FlagValue("rules.mosquito.message2",      new String[]{"[text]", "[blank]"}, String.class),
		new FlagValue("rules.mosquito.message3",      new String[]{"[text]", "[blank]"}, String.class),
		new FlagValue("rules.mosquito.message4",      new String[]{"[text]", "[blank]"}, String.class),
		new FlagValue("rules.leech.damage",           new String[]{"[integer]", "[blank]"}, 0, 255, Integer.class),
		new FlagValue("rules.leech.rarity",           new String[]{"[integer]", "[blank]"}, 1, 10, Integer.class),
		new FlagValue("rules.leech.message1",         new String[]{"[text]", "[blank]"}, String.class),
		new FlagValue("rules.leech.message2",         new String[]{"[text]", "[blank]"}, String.class),
		new FlagValue("rules.leech.message3",         new String[]{"[text]", "[blank]"}, String.class),
		new FlagValue("rules.leech.message4",         new String[]{"[text]", "[blank]"}, String.class),
		new FlagValue("rules.leech.message5",         new String[]{"[text]", "[blank]"}, String.class),
		new FlagValue("rules.spawn.neutral.rarity",   new String[]{"[integer]", "[blank]"}, 1, 10, Integer.class),
		new FlagValue("rules.spawn.neutral.mobs",     new String[]{"[text]", "[blank]"}, String.class),
		new FlagValue("rules.spawn.neutral.on",       new String[]{"allow","deny", "[blank]"}, Boolean.class),
                new FlagValue("recipe.warm.suit.name", new String[]{"[text]", "[blank]"}, String.class),
                new FlagValue("recipe.warm.suit.description", new String[]{"[text]", "[blank]"}, String.class),
                new FlagValue("recipe.repellent.suit.name", new String[]{"[text]", "[blank]"}, String.class),
                new FlagValue("recipe.repellent.suit.description", new String[]{"[text]", "[blank]"}, String.class),
                new FlagValue("recipe.cooling.suit.name", new String[]{"[text]", "[blank]"}, String.class),
                new FlagValue("recipe.cooling.suit.description", new String[]{"[text]", "[blank]"}, String.class),
                new FlagValue("recipe.glass.suit.name", new String[]{"[text]", "[blank]"}, String.class),
                new FlagValue("recipe.glass.suit.description", new String[]{"[text]", "[blank]"}, String.class),
                new FlagValue("rules.spawn.mustafar", new String[]{"allow","deny", "[blank]"}, Boolean.class),
                new FlagValue("rules.spawn.schematic_entity", new String[]{"allow","deny", "[blank]"}, Boolean.class),
		};
        public static boolean getSchematicEntity(HothGeneratorPlugin plugin, World world) {
            return ConfigManager.getConfigBoolean(plugin, world, "rules.spawn.schematic_entity", true);
        }
        public static boolean getMustafarSpawn(HothGeneratorPlugin plugin, World world) {
            return ConfigManager.getConfigBoolean(plugin, world, "rules.spawn.mustafar", false);
        }
        public static String getGlassSuitName(HothGeneratorPlugin plugin)
	{
                return plugin.getConfig().getString("recipe.glass.suit.name", "Glass Suit");
	}
        public static String getGlassSuitDescription(HothGeneratorPlugin plugin)
	{
                return plugin.getConfig().getString("recipe.glass.suit.description", "Protect yourself from burning");
	}
	public static String getWarmSuitName(HothGeneratorPlugin plugin)
	{
                return plugin.getConfig().getString("recipe.warm.suit.name", "Warm Suit");
	}
        public static String getWarmSuitDescription(HothGeneratorPlugin plugin)
	{
                return plugin.getConfig().getString("recipe.warm.suit.description", "Protect yourself from cold weather");
	}
        public static String getRepellentSuitName(HothGeneratorPlugin plugin)
	{
                return plugin.getConfig().getString("recipe.repellent.suit.name", "Repellent Suit");
	}
        public static String getRepellentSuitDescription(HothGeneratorPlugin plugin)
	{
                return plugin.getConfig().getString("recipe.repellent.suit.description", "Protect yourself from mosquito and leech");
	}
        public static String getCoolingSuitName(HothGeneratorPlugin plugin)
	{
                return plugin.getConfig().getString("recipe.cooling.suit.name", "Cooling Suit");
	}
        public static String getCoolingSuitDescription(HothGeneratorPlugin plugin)
	{
                return plugin.getConfig().getString("recipe.cooling.suit.description", "Protect yourself from dehydration");
	}
        
	public static boolean isDebug(HothGeneratorPlugin plugin)
	{
		return plugin.getConfig().getBoolean("hoth.debug", false);
	}
	
	public static boolean isItemInfoTool(HothGeneratorPlugin plugin)
	{
		return plugin.getConfig().getBoolean("hoth.iteminfotool", false);
	}
	
	public static boolean isSmoothSnow(HothGeneratorPlugin plugin)
	{
		return plugin.getConfig().getBoolean("hoth.smoothsnow", true);
	}
	
	public static boolean isSmoothLava(HothGeneratorPlugin plugin)
	{
		return plugin.getConfig().getBoolean("hoth.smoothlava", true);
	}

	public static boolean isWorldEditSelection(HothGeneratorPlugin plugin)
	{
		return plugin.getConfig().getBoolean("hoth.worldeditselection", false);
	}
	
	public static Material getSelectionToolMaterial(HothGeneratorPlugin plugin)
	{
		int id = plugin.getConfig().getInt("hoth.selectiontoolid", 271);
		
		Material material = MaterialManager.toMaterial(id);
		
		if(material==null)
		{
			material = Material.WOODEN_AXE;
		}
		
		return material;
	}


	private static int getConfigInt(HothGeneratorPlugin plugin, World world, String tag, int def)
	{
		String name = world.getName();
		String worldPath = "hothworldsdata." + name + "." + tag;
		String defaultPath = "hoth." + tag;

		if(plugin.getWorldConfig().isSet(worldPath))
		{
			return plugin.getWorldConfig().getInt(worldPath, def);
		}
		else
		{
			return plugin.getConfig().getInt(defaultPath, def);
		}
	}
	
	private static boolean getConfigBoolean(HothGeneratorPlugin plugin, World world, String tag, boolean def)
	{
		String name = world.getName();
		String worldPath = "hothworldsdata." + name + "." + tag;
		String defaultPath = "hoth." + tag;

		if(plugin.getWorldConfig().isSet(worldPath))
		{
			return plugin.getWorldConfig().getBoolean(worldPath, def);
		}
		else
		{
			return plugin.getConfig().getBoolean(defaultPath, def);
		}
	}

	private static String getConfigString(HothGeneratorPlugin plugin, World world, String tag, String def)
	{
		String name = world.getName();
		String worldPath = "hothworldsdata." + name + "." + tag;
		String defaultPath = "hoth." + tag;

		if(plugin.getWorldConfig().isSet(worldPath))
		{
			return plugin.getWorldConfig().getString(worldPath, def);
		}
		else
		{
			return plugin.getConfig().getString(defaultPath, def);
		}
	}

	public static int getWorldSurfaceoffset(HothGeneratorPlugin plugin, World world)
	{
		int result = ConfigManager.getConfigInt(plugin, world, "world.surfaceoffset", 0);
		
		if(result<0)
		{
			result = 0;
		}
		else if(result>127)
		{
			result = 127;
		}
		return result;
	}


	
	public static int getStructureSpikesRarity(HothGeneratorPlugin plugin, World world)
	{
		int result = ConfigManager.getConfigInt(plugin, world, "structure.spikes.rarity", 2);
		
		if(result<0 || result>10)
		{
			result = 2;
		}
		return result;
	}

	public static int getStructureGardensRarity(HothGeneratorPlugin plugin, World world)
	{
		int result = ConfigManager.getConfigInt(plugin, world, "structure.gardens.rarity", 2);
		if(result<0 || result>10)
		{
			result = 2;
		}
		return result;
	}

	public static int getStructureDomesRarity(HothGeneratorPlugin plugin, World world)
	{
		int result = ConfigManager.getConfigInt(plugin, world, "structure.domes.rarity", 3);
		if(result<0 || result>10)
		{
			result = 2;
		}
		return result;
	}

	public static int getStructureDomesPlantstem(HothGeneratorPlugin plugin, World world)
	{
		return ConfigManager.getConfigInt(plugin, world, "structure.domes.plantstem", 19);
	}

	public static int getStructureDomesPlanttop(HothGeneratorPlugin plugin, World world)
	{
		return ConfigManager.getConfigInt(plugin, world, "structure.domes.planttop", 89);
	}
	
	public static int getStructureDomesFloor(HothGeneratorPlugin plugin, World world)
	{
		return ConfigManager.getConfigInt(plugin, world, "structure.domes.floor", 3);
	}

	public static int getStructureDomesFloorrandom(HothGeneratorPlugin plugin, World world)
	{
		return ConfigManager.getConfigInt(plugin, world, "structure.domes.floorrandom", 89);
	}
	
	public static boolean isStructureDomesPlaceminidome(HothGeneratorPlugin plugin, World world)
	{
		return ConfigManager.getConfigBoolean(plugin, world, "structure.domes.placeminidome", true);
	}

	public static int getStructureBasesRarity(HothGeneratorPlugin plugin, World world)
	{
		int result = ConfigManager.getConfigInt(plugin, world, "structure.bases.rarity", 2);
		if(result<0 || result>10)
		{
			result = 2;
		}
		return result;
	}

	public static boolean isStructureBasesSpawner(HothGeneratorPlugin plugin, World world)
	{
		return ConfigManager.getConfigBoolean(plugin, world, "structure.bases.spawner", true);
	}

	public static int getStructureMazesRarity(HothGeneratorPlugin plugin, World world)
	{
		int result = ConfigManager.getConfigInt(plugin, world, "structure.mazes.rarity", 2);
		if(result<0 || result>10)
		{
			result = 2;
		}
		return result;
	}
	
	public static int getStructureMazesMinrooms(HothGeneratorPlugin plugin, World world)
	{
		int min = ConfigManager.getConfigInt(plugin, world, "structure.mazes.minrooms", 8);
		int max = ConfigManager.getConfigInt(plugin, world, "structure.mazes.maxrooms", 32);
		
		if(min>max || min<1 || max<1 || max>100)
		{
			min = 8;
		}
		
		return min;
	}

	public static int getStructureMazesMaxrooms(HothGeneratorPlugin plugin, World world)
	{
		int min = ConfigManager.getConfigInt(plugin, world, "structure.mazes.minrooms", 8);
		int max = ConfigManager.getConfigInt(plugin, world, "structure.mazes.maxrooms", 32);
		
		if(min>max || min<1 || max<1 || max>100)
		{
			max = 32;
		}
		
		return max;
	}
	
	public static boolean isStructureMazesSpawner(HothGeneratorPlugin plugin, World world)
	{
		return ConfigManager.getConfigBoolean(plugin, world, "structure.mazes.spawner", true);
	}

	public static int getStructureSkeletonsRarity(HothGeneratorPlugin plugin, World world)
	{
		int result = ConfigManager.getConfigInt(plugin, world, "structure.skeletons.rarity", 2);
		if(result<0 || result>10)
		{
			result = 2;
		}
		return result;
	}
	
	public static int getStructureOasisRarity(HothGeneratorPlugin plugin, World world)
	{
		int result = ConfigManager.getConfigInt(plugin, world, "structure.oasis.rarity", 2);
		if(result<0 || result>10)
		{
			result = 2;
		}
		return result;
	}
	
	public static int getStructureSandCastleRarity(HothGeneratorPlugin plugin, World world)
	{
		int result = ConfigManager.getConfigInt(plugin, world, "structure.sandcastle.rarity", 2);
		if(result<0 || result>10)
		{
			result = 2;
		}
		return result;
	}

	public static int getStructureVillageRarity(HothGeneratorPlugin plugin, World world)
	{
		int result = ConfigManager.getConfigInt(plugin, world, "structure.village.rarity", 2);
		if(result<0 || result>10)
		{
			result = 2;
		}
		return result;
	}

	public static int getStructureSuperGardenRarity(HothGeneratorPlugin plugin, World world)
	{
		int result = ConfigManager.getConfigInt(plugin, world, "structure.supergarden.rarity", 2);
		if(result<0 || result>10)
		{
			result = 2;
		}
		return result;
	}
	
	public static int getStructureSarlaccRarity(HothGeneratorPlugin plugin, World world)
	{
		int result = ConfigManager.getConfigInt(plugin, world, "structure.sarlacc.rarity", 2);
		if(result<0 || result>10)
		{
			result = 2;
		}
		return result;
	}

	public static int getStructureHugeTreeRarity(HothGeneratorPlugin plugin, World world)
	{
		int result = ConfigManager.getConfigInt(plugin, world, "structure.hugetree.rarity", 2);
		if(result<0 || result>10)
		{
			result = 2;
		}
		return result;
	}

	public static int getStructureSpiderForestRarity(HothGeneratorPlugin plugin, World world)
	{
		int result = ConfigManager.getConfigInt(plugin, world, "structure.spiderforest.rarity", 2);
		if(result<0 || result>10)
		{
			result = 2;
		}
		return result;
	}

	public static int getStructureSwampTempleRarity(HothGeneratorPlugin plugin, World world)
	{
		int result = ConfigManager.getConfigInt(plugin, world, "structure.swamptemple.rarity", 2);
		if(result<0 || result>10)
		{
			result = 2;
		}
		return result;
	}

	public static int getStructureTreeHutRarity(HothGeneratorPlugin plugin, World world)
	{
		int result = ConfigManager.getConfigInt(plugin, world, "structure.treehut.rarity", 2);
		if(result<0 || result>10)
		{
			result = 2;
		}
		return result;
	}

	public static int getStructureMustafarBaseRarity(HothGeneratorPlugin plugin, World world)
	{
		int result = ConfigManager.getConfigInt(plugin, world, "structure.mustafarbase.rarity", 2);
		if(result<0 || result>10)
		{
			result = 2;
		}
		return result;
	}

	public static int getStructureMustafarTempleRarity(HothGeneratorPlugin plugin, World world)
	{
		int result = ConfigManager.getConfigInt(plugin, world, "structure.mustafartemple.rarity", 2);
		if(result<0 || result>10)
		{
			result = 2;
		}
		return result;
	}

	public static boolean isGenerateLogs(HothGeneratorPlugin plugin, World world)
	{
		return ConfigManager.getConfigBoolean(plugin, world, "generate.logs", true);
	}

	public static int getGenerateCavesRarity(HothGeneratorPlugin plugin, World world)
	{
		int result = ConfigManager.getConfigInt(plugin, world, "generate.caves.rarity", 2);
		if(result<0 || result>10)
		{
			result = 2;
		}
		return result;
	}
	
	public static boolean isGenerateOres(HothGeneratorPlugin plugin, World world)
	{
		return ConfigManager.getConfigBoolean(plugin, world, "generate.ores", true);
	}

	public static boolean isGenerateExtendedOre(HothGeneratorPlugin plugin)
	{
		return plugin.getConfig().getBoolean("hoth.generate.extendedore", false);
	}

	public static boolean isGenerateCactuses(HothGeneratorPlugin plugin, World world)
	{
		return ConfigManager.getConfigBoolean(plugin, world, "generate.cactuses", true);
	}

	public static boolean isGenerateShrubs(HothGeneratorPlugin plugin, World world)
	{
		return ConfigManager.getConfigBoolean(plugin, world, "generate.shrubs", true);
	}

	public static boolean isGenerateMushroomHuts(HothGeneratorPlugin plugin, World world)
	{
		return ConfigManager.getConfigBoolean(plugin, world, "generate.mushroomhuts", true);
	}

	public static boolean isRulesDropice(HothGeneratorPlugin plugin, Location location)
	{
		return plugin.getRegionManager().getBoolean("dropice", location, ConfigManager.getConfigBoolean(plugin, location.getWorld(), "rules.dropice", true));
	}

	public static boolean isRulesDroppackedice(HothGeneratorPlugin plugin, Location location)
	{
		return plugin.getRegionManager().getBoolean("droppackedice", location, ConfigManager.getConfigBoolean(plugin, location.getWorld(), "rules.droppackedice", true));
	}

	public static boolean isRulesDropsnow(HothGeneratorPlugin plugin, Location location)
	{
		return plugin.getRegionManager().getBoolean("dropsnow", location, ConfigManager.getConfigBoolean(plugin, location.getWorld(), "rules.dropsnow", true));
	}

	public static boolean isRulesFreezewater(HothGeneratorPlugin plugin, Location location)
	{
		return plugin.getRegionManager().getBoolean("freezewater", location, ConfigManager.getConfigBoolean(plugin, location.getWorld(), "rules.freezewater", true));
	}
	
	public static boolean isRulesFreezelava(HothGeneratorPlugin plugin, Location location)
	{
		return plugin.getRegionManager().getBoolean("freezelava", location, ConfigManager.getConfigBoolean(plugin, location.getWorld(), "rules.freezelava", true));
	}
	
	public static boolean isRulesPlantsgrow(HothGeneratorPlugin plugin, Location location)
	{
		return plugin.getRegionManager().getBoolean("plantsgrow", location, ConfigManager.getConfigBoolean(plugin, location.getWorld(), "rules.plantsgrow", false));
	}

	public static boolean isRulesGrassspread(HothGeneratorPlugin plugin, Location location)
	{
		return plugin.getRegionManager().getBoolean("grassspread", location, ConfigManager.getConfigBoolean(plugin, location.getWorld(), "rules.grassspread", false));
	}
	
	public static boolean isRulesStopmelt(HothGeneratorPlugin plugin, Location location)
	{
		return plugin.getRegionManager().getBoolean("stopmelt", location, ConfigManager.getConfigBoolean(plugin, location.getWorld(), "rules.stopmelt", true));
	}

	public static boolean isRulesLimitslime(HothGeneratorPlugin plugin, Location location)
	{
		return plugin.getRegionManager().getBoolean("limitslime", location, ConfigManager.getConfigBoolean(plugin, location.getWorld(), "rules.limitslime", true));
	}
	
	public static boolean isRulesVolcanoes(HothGeneratorPlugin plugin, Location location)
	{
		return plugin.getRegionManager().getBoolean("volcanoes", location, ConfigManager.getConfigBoolean(plugin, location.getWorld(), "rules.volcanoes", true));
	}

	public static boolean isRulesRFGEnable(HothGeneratorPlugin plugin, Location location)
	{
		return plugin.getRegionManager().getBoolean("rfg.enable", location, ConfigManager.getConfigBoolean(plugin, location.getWorld(), "rules.rfg.enable", true));
	}
	
	public static int getRulesRFGCoal(HothGeneratorPlugin plugin, Location location)
	{
	    int damage = plugin.getRegionManager().getInt("rfg.coal", location, ConfigManager.getConfigInt(plugin, location.getWorld(), "rules.rfg.coal", 5));
	    if(damage<0)
	    {
	    	damage = 5;
	    }
	    return damage;
	}

	public static int getRulesRFGRedstone(HothGeneratorPlugin plugin, Location location)
	{
	    int damage = plugin.getRegionManager().getInt("rfg.redstone", location, ConfigManager.getConfigInt(plugin, location.getWorld(), "rules.rfg.redstone", 10));
	    if(damage<0)
	    {
	    	damage = 10;
	    }
	    return damage;
	}

	public static String getRulesRFGName(HothGeneratorPlugin plugin, Location location)
	{
		return plugin.getRegionManager().get("rfg.name", location, ConfigManager.getConfigString(plugin, location.getWorld(), "rules.rfg.name", "Repulsor Field Generator"));
	}
	
	public static String getRulesRFGMessage1(HothGeneratorPlugin plugin, Location location)
	{
		return plugin.getRegionManager().get("rfg.message1", location, ConfigManager.getConfigString(plugin, location.getWorld(), "rules.rfg.message1", "&cYou need a Repulsor Field Generator piston to extract ore from lava."));
	}

	public static String getRulesRFGMessage2(HothGeneratorPlugin plugin, Location location)
	{
		return plugin.getRegionManager().get("rfg.message2", location, ConfigManager.getConfigString(plugin, location.getWorld(), "rules.rfg.message2", "&cYou need Redstone to power the Repulsor Field Generator."));
	}

	public static String getRulesRFGMessage3(HothGeneratorPlugin plugin, Location location)
	{
		return plugin.getRegionManager().get("rfg.message3", location, ConfigManager.getConfigString(plugin, location.getWorld(), "rules.rfg.message3", "&cYou need Coal to power the Repulsor Field Generator."));
	}

	public static String getRulesRFGMessage4(HothGeneratorPlugin plugin, Location location)
	{
		return plugin.getRegionManager().get("rfg.message4", location, ConfigManager.getConfigString(plugin, location.getWorld(), "rules.rfg.message4", "&cInventory full."));
	}

	public static boolean isRulesLavaBurn(HothGeneratorPlugin plugin, Location location)
	{
		return plugin.getRegionManager().getBoolean("lavaburn", location, ConfigManager.getConfigBoolean(plugin, location.getWorld(), "rules.lavaburn", true));
	}

	public static boolean isRulesPlaceWater(HothGeneratorPlugin plugin, Location location)
	{
		return plugin.getRegionManager().getBoolean("placewater", location, ConfigManager.getConfigBoolean(plugin, location.getWorld(), "rules.placewater", true));
	}

	public static boolean isRulesLessStone(HothGeneratorPlugin plugin, Location location)
	{
		return plugin.getRegionManager().getBoolean("lessstone", location, ConfigManager.getConfigBoolean(plugin, location.getWorld(), "rules.lessstone", true));
	}

	public static boolean isRulesSnowgravity(HothGeneratorPlugin plugin, Location location)
	{
		return plugin.getRegionManager().getBoolean("snowgravity", location, ConfigManager.getConfigBoolean(plugin, location.getWorld(), "rules.snowgravity", false));
	}

	public static int getRulesEnvironmentPeriod(HothGeneratorPlugin plugin)
	{
	    int period = plugin.getConfig().getInt("hoth.rules.environment.period", 0);

	    if(period<0)
	    {
	    	period = 5;
	    }
	    return period;
	}

	public static boolean getRulesEnvironmentSuit(HothGeneratorPlugin plugin, Location location)
	{
		return plugin.getRegionManager().getBoolean("environment.suit", location, ConfigManager.getConfigBoolean(plugin, location.getWorld(), "rules.environment.suit", false));
	}

	public static int getRulesFreezeDamage(HothGeneratorPlugin plugin, Location location)
	{
	    int damage = plugin.getRegionManager().getInt("freeze.damage", location, ConfigManager.getConfigInt(plugin, location.getWorld(), "rules.freeze.damage", 2));
	    if(damage<0)
	    {
	    	damage = 2;
	    }
	    return damage;
	}

	public static int getRulesFreezeStormdamage(HothGeneratorPlugin plugin, Location location)
	{
	    int damage = plugin.getRegionManager().getInt("freeze.stormdamage", location, ConfigManager.getConfigInt(plugin, location.getWorld(), "rules.freeze.stormdamage", 1));
	    if(damage<0)
	    {
	    	damage = 2;
	    }
	    return damage;
	}

	public static String getRulesFreezeMessage(HothGeneratorPlugin plugin, Location location)
	{
		return plugin.getRegionManager().get("freeze.message", location, ConfigManager.getConfigString(plugin, location.getWorld(), "rules.freeze.message", "&bYou are freezing. Find shelter!"));
	}
	
	
	public static int getRulesHeatDamage(HothGeneratorPlugin plugin, Location location)
	{
	    int damage = plugin.getRegionManager().getInt("heat.damage", location, ConfigManager.getConfigInt(plugin, location.getWorld(), "rules.heat.damage", 2));
	    if(damage<0)
	    {
	    	damage = 2;
	    }
	    return damage;
	}

	public static String getRulesHeatMessage1(HothGeneratorPlugin plugin, Location location)
	{
		return plugin.getRegionManager().get("heat.message1", location, ConfigManager.getConfigString(plugin, location.getWorld(), "rules.heat.message1", "&6The water removes your thirst."));
	}

	public static String getRulesHeatMessage2(HothGeneratorPlugin plugin, Location location)
	{
		return plugin.getRegionManager().get("heat.message2", location, ConfigManager.getConfigString(plugin, location.getWorld(), "rules.heat.message2", "&6Your are starting to feel thirsty."));
	}

	public static String getRulesHeatMessage3(HothGeneratorPlugin plugin, Location location)
	{
		return plugin.getRegionManager().get("heat.message3", location, ConfigManager.getConfigString(plugin, location.getWorld(), "rules.heat.message3", "&6Your feel very thirsty."));
	}

	public static String getRulesHeatMessage4(HothGeneratorPlugin plugin, Location location)
	{
		return plugin.getRegionManager().get("heat.message4", location, ConfigManager.getConfigString(plugin, location.getWorld(), "rules.heat.message4", "&6You are exhausted from the heat. Find water or shelter!"));
	}

	public static int getRulesMosquitoDamage(HothGeneratorPlugin plugin, Location location)
	{
	    int damage = plugin.getRegionManager().getInt("mosquito.damage", location, ConfigManager.getConfigInt(plugin, location.getWorld(), "rules.mosquito.damage", 1));
	    if(damage<0)
	    {
	    	damage = 1;
	    }
	    return damage;
	}
	
	public static int getRulesMosquitoRarity(HothGeneratorPlugin plugin, Location location)
	{
		int result = plugin.getRegionManager().getInt("mosquito.rarity", location, ConfigManager.getConfigInt(plugin, location.getWorld(), "rules.mosquito.rarity", 5));
		if(result<1 || result>10)
		{
			result = 2;
		}
		return result;
	}

	public static int getRulesMosquitoRunFree(HothGeneratorPlugin plugin, Location location)
	{
		int result = plugin.getRegionManager().getInt("mosquito.runfree", location, ConfigManager.getConfigInt(plugin, location.getWorld(), "rules.mosquito.runfree", 10));
		if(result<10 || result>100)
		{
			result = 10;
		}
		return result;
	}

	public static String getRulesMosquitoMessage1(HothGeneratorPlugin plugin, Location location)
	{
		return plugin.getRegionManager().get("mosquito.message1", location, ConfigManager.getConfigString(plugin, location.getWorld(), "rules.mosquito.message1", "&2You seem to have lost the mosquito swarm."));
	}

	public static String getRulesMosquitoMessage2(HothGeneratorPlugin plugin, Location location)
	{
		return plugin.getRegionManager().get("mosquito.message2", location, ConfigManager.getConfigString(plugin, location.getWorld(), "rules.mosquito.message2", "&2You hear some buzzing nearby."));
	}

	public static String getRulesMosquitoMessage3(HothGeneratorPlugin plugin, Location location)
	{
		return plugin.getRegionManager().get("mosquito.message3", location, ConfigManager.getConfigString(plugin, location.getWorld(), "rules.mosquito.message3", "&2You can see a swarm of huge mosquitos nearby."));
	}

	public static String getRulesMosquitoMessage4(HothGeneratorPlugin plugin, Location location)
	{
		return plugin.getRegionManager().get("mosquito.message4", location, ConfigManager.getConfigString(plugin, location.getWorld(), "rules.mosquito.message4", "&2Mosquitos are attacking you! Run!!"));
	}
	
	public static int getRulesLeechDamage(HothGeneratorPlugin plugin, Location location)
	{
	    int damage = plugin.getRegionManager().getInt("leech.damage", location, ConfigManager.getConfigInt(plugin, location.getWorld(), "rules.leech.damage", 1));
	    if(damage<0)
	    {
	    	damage = 1;
	    }
	    return damage;
	}
	
	
	public static int getRulesLeechRarity(HothGeneratorPlugin plugin, Location location)
	{
		int result = plugin.getRegionManager().getInt("leech.rarity", location, ConfigManager.getConfigInt(plugin, location.getWorld(), "rules.leech.rarity", 5));
		if(result<1 || result>10)
		{
			result = 2;
		}
		return result;
	}

	public static String getRulesLeechMessage1(HothGeneratorPlugin plugin, Location location)
	{
		return plugin.getRegionManager().get("leech.message1", location, ConfigManager.getConfigString(plugin, location.getWorld(), "rules.leech.message1", "&2You manage to remove all leeches."));
	}

	public static String getRulesLeechMessage2(HothGeneratorPlugin plugin, Location location)
	{
		return plugin.getRegionManager().get("leech.message2", location, ConfigManager.getConfigString(plugin, location.getWorld(), "rules.leech.message2", "&2You notice something moving in the water nearby."));
	}

	public static String getRulesLeechMessage3(HothGeneratorPlugin plugin, Location location)
	{
		return plugin.getRegionManager().get("leech.message3", location, ConfigManager.getConfigString(plugin, location.getWorld(), "rules.leech.message3", "&2You can see leeches moving in the water."));
	}

	public static String getRulesLeechMessage4(HothGeneratorPlugin plugin, Location location)
	{
		return plugin.getRegionManager().get("leech.message4", location, ConfigManager.getConfigString(plugin, location.getWorld(), "rules.leech.message4", "&2Leeches are attacking you! Get out of the water!"));
	}

	public static String getRulesLeechMessage5(HothGeneratorPlugin plugin, Location location)
	{
		return plugin.getRegionManager().get("leech.message5", location, ConfigManager.getConfigString(plugin, location.getWorld(), "rules.leech.message5", "&2Leeches are sucking you dry, run to shake them off!"));
	}

	public static int getRulesSpawnNeutralRarity(HothGeneratorPlugin plugin, Location location)
	{
	    int rarity = plugin.getRegionManager().getInt("spawn.neutral.rarity", location, ConfigManager.getConfigInt(plugin, location.getWorld(), "rules.spawn.neutral.rarity", 2));
	    if(rarity<1 || rarity>10)
	    {
	    	rarity = 2;
	    }
	    return rarity;
	}
	
	public static String getRulesSpawnNeutralMobs(HothGeneratorPlugin plugin, Location location)
	{
		return plugin.getRegionManager().get("spawn.neutral.mobs", location, ConfigManager.getConfigString(plugin, location.getWorld(), "rules.spawn.neutral.mobs", "chicken,cow,mushroom_cow,ocelot,pig,sheep,wolf"));
	}
	
	public static boolean isRulesSpawnNeutralOn(HothGeneratorPlugin plugin, World world)
	{
		return ConfigManager.getConfigBoolean(plugin, world, "rules.spawn.neutral.on", true);
	}
	
	/*
	  +structure.spikes.rarity: 2
	  +structure.gardens.rarity: 2
	  +structure.domes.rarity: 2
	  +structure.domes.plantstem: 19
	  +structure.domes.planttop: 89
	  +structure.domes.floor: 3
	  +structure.domes.floorrandom: 89
	  +structure.domes.placeminidome: true
	  +structure.bases.rarity: 2
	  +structure.bases.spawner: true
	  +structure.roomcluster.rarity: 2
	  +structure.roomcluster.minrooms: 8
	  +structure.roomcluster.maxrooms: 32
	  +structure.roomcluster.spawner: true
	  +generate.logs: true
	  +generate.caves.rarity: 2
	  +generate.ores: true
	  +generate.extendedores: false
	  +rules.dropice: true
	  +rules.droppackedice: true
	  +rules.dropsnow: true
	  +rules.freezewater: true
	  +rules.freezelava: true
	  +rules.plantsgrow: false
	  +rules.grassspread: false
	  +rules.stopmelt: true
	  +rules.limitslime: true
	  */
	
	
	/* Config - End */

	/* World configuration - Start */
	public static void addWorld(HothGeneratorPlugin plugin, String worldName, WorldType worldType)
	{
		String type = worldType.toString();
		
		FileConfiguration config = plugin.getWorldConfig();
		List<String> list = config.getStringList("hothworlds");
		list.add(worldName);
		config.set("hothworlds", list);
		config.set("hothworldsdata." + worldName.toLowerCase() + ".type", type);
		plugin.saveWorldConfig();
	}

	public static void addWorld(HothGeneratorPlugin plugin, World world, WorldType worldType)
	{
		ConfigManager.addWorld(plugin, world.getName(), worldType);
	}

	public static boolean addWorld(HothGeneratorPlugin plugin, CommandSender sender, String worldName, String type)
	{
		try
		{
			WorldType worldType = WorldType.getType(type);
		
			if(plugin.isHothWorld(worldName))
			{
				plugin.sendMessage(sender, "&bWorld " + worldName + " already exists");
				return false;
			}
			
			FileConfiguration config = plugin.getWorldConfig();
			List<String> list = config.getStringList("hothworlds");
			list.add(worldName.toLowerCase());
			config.set("hothworlds", list);
			config.set("hothworldsdata." + worldName.toLowerCase() + ".type", worldType.toString());
			
			plugin.saveWorldConfig();
			plugin.sendMessage(sender, "&bAdded world " + worldName.toLowerCase() + " of type " + worldType.toString());
			return true;
		}
		catch(InvalidWorldTypeException e)
		{
			plugin.sendMessage(sender, "&bInvalid world type : " + type);
			return false;
		}
	}

	public static boolean delWorld(HothGeneratorPlugin plugin, CommandSender sender, String worldName)
	{
		if(!plugin.isHothWorld(worldName))
		{
			plugin.sendMessage(sender, "&bThere is no such hoth world: " + worldName);
			return false;
		}
		
		FileConfiguration config = plugin.getWorldConfig();
		List<String> list = config.getStringList("hothworlds");
		list.remove(worldName.toLowerCase());
		config.set("hothworlds", list);
		config.set("hothworldsdata." + worldName.toLowerCase(), null);
		
		plugin.saveWorldConfig();
		plugin.sendMessage(sender, "&bRemoved world " + worldName.toLowerCase());
		return true;
	}
/* World configuration - End */
	
	private static class FlagValue
	{
		public String flag;
		public String[] values;
		public int min;
		public int max;
		@SuppressWarnings("rawtypes")
		public Class type;
		
		@SuppressWarnings("rawtypes")
		public FlagValue(String flag, String[] values, Class type)
		{
			this.flag = flag;
			this.values = values;
			this.min = Integer.MIN_VALUE;
			this.max = Integer.MAX_VALUE;
			this.type = type;
		}

		@SuppressWarnings("rawtypes")
		public FlagValue(String flag, String[] values, int min, int max, Class type)
		{
			this.flag = flag;
			this.values = values;
			this.min = min;
			this.max = max;
			this.type = type;
		}

	}

	public static boolean printWorldList(HothGeneratorPlugin plugin, CommandSender sender)
	{
		FileConfiguration config = plugin.getWorldConfig();
		List<String> list = config.getStringList("hothworlds");
		
		if(list!=null)
		{
			plugin.sendMessage(sender, "&bHoth worlds:");
			for(int i=0;i<list.size();i++)
			{
				String item = list.get(i).toLowerCase();
				String type = config.getString("hothworldsdata." + item + ".type","hoth");
				
				if(plugin.getServer().getWorld(item)==null)
				{
					plugin.sendMessage(sender, "&bWorld: " + item + "  Type: " + type + "  Status: Not loaded.");
				}
				else
				{
					plugin.sendMessage(sender, "&bWorld: " + item + "  Type: " + type + "  Status: Loaded.");
				}

			}
		}
		else
		{
			plugin.sendMessage(sender, "&bThere are no defined hoth worlds.");
		}
		
		return true;

	}
	
	public static boolean printWorldInfo(HothGeneratorPlugin plugin, CommandSender sender, String world)
	{
		if(!plugin.isHothWorld(world.toLowerCase()))
		{
			plugin.sendMessage(sender, "&b" + world + " is not a hoth world.");
			return false;
		}
		
		FileConfiguration config = plugin.getWorldConfig();
		
		String type = plugin.getWorldConfig().getString("hothworldsdata." + world + ".type","hoth");
		plugin.sendMessage(sender, "&bWorld: " + world + "  Type: " + type);

		World _world = plugin.getServer().getWorld(world);
		if(_world!=null)
		{
			plugin.sendMessage(sender, "&bStatus: Loaded.");
			plugin.sendMessage(sender, "&bOnline Players: " + _world.getPlayers().size());
		}
		else
		{
			plugin.sendMessage(sender, "&bStatus: Not loaded");
		}
		
		plugin.sendMessage(sender, "&bFlags:");
		
		int ctr = 0;
		for(int i=0;i<ConfigManager.validWorldFlags.length;i++)
		{
			FlagValue flag = ConfigManager.validWorldFlags[i];
			@SuppressWarnings("rawtypes")
			Class _type = flag.type;
			
			String flagName = "hothworldsdata." + world + "." + flag.flag;
			
			if(config.isSet(flagName))
			{
				ctr++;
				
				if(_type.equals(Integer.class))
				{
					int value = config.getInt(flagName);
					plugin.sendMessage(sender, "&b" + flag.flag + ": " + value);
				}
				else if(_type.equals(Boolean.class))
				{
					boolean value = config.getBoolean(flagName);
					plugin.sendMessage(sender, "&b" + flag.flag + ": " + value);
				}
				else if(_type.equals(String.class))
				{
					String value = config.getString(flagName);
					plugin.sendMessage(sender, "&b" + flag.flag + ": " + value);
				}
			}
		}
		
		if(ctr==0)
		{
			plugin.sendMessage(sender, "&bNone");
		}
		
		return true;
	}

	public static void setWorldType(HothGeneratorPlugin plugin, World world, WorldType worldType)
	{
		ConfigManager.setWorldType(plugin, world.getName(), worldType);
	}
	
	public static void setWorldType(HothGeneratorPlugin plugin, String worldName, WorldType worldType)
	{
		FileConfiguration config = plugin.getWorldConfig();
		config.set("hothworldsdata." + worldName.toLowerCase() + ".type", worldType.toString());
		plugin.saveWorldConfig();
	}

	public static boolean setWorldType(HothGeneratorPlugin plugin, CommandSender sender, String world, String type)
	{
		if(!plugin.isHothWorld(world.toLowerCase()))
		{
			plugin.sendMessage(sender, "&b" + world + " is not configured as a HothGenerator world.");
			return false;
		}
		
		try
		{
			WorldType worldType = WorldType.getType(type);

			FileConfiguration config = plugin.getWorldConfig();
			config.set("hothworldsdata." + world.toLowerCase() + ".type", worldType.toString());

			plugin.saveWorldConfig();
			plugin.sendMessage(sender, "&b" + world + " set to " + worldType.toString());
			plugin.sendMessage(sender, "&bYou need to unload and reload " + world + " or else it won't render properly with the new world type");

		
		}
		catch(InvalidWorldTypeException e)
		{
			plugin.sendMessage(sender, "&bInvalid world type : " + type);
			return false;
		}

		return true;
	}

	public static boolean setWorldFlag(HothGeneratorPlugin plugin, CommandSender sender, String world, String flag, String value)
	{
		if(!plugin.isHothWorld(world.toLowerCase()))
		{
			plugin.sendMessage(sender, "&b" + world + " is not a hoth world.");
			return false;
		}
		
		if(!ConfigManager.isValidWorldFlag(flag.toLowerCase()))
		{
			plugin.sendMessage(sender, "&bInvalid flag: " + flag);
			return false;
		}
		
		if(!ConfigManager.isValidWorldFlagValue(flag.toLowerCase(), value))
		{
			plugin.sendMessage(sender, "&bInvalid value: " + value);
			return false;
		}
		
		FileConfiguration config = plugin.getWorldConfig();
		if(value.equals(""))
		{
			config.set("hothworldsdata." + world.toLowerCase() + "." + flag.toLowerCase(), null);
			plugin.sendMessage(sender, "&bRemoved " + flag + " flag from " + world);
		}
		else
		{
			@SuppressWarnings("rawtypes")
			Class type = ConfigManager.getFlagClass(flag.toLowerCase());
			if(type.equals(Integer.class))
			{
				config.set("hothworldsdata." + world.toLowerCase() + "." + flag.toLowerCase(), new Integer(value));
			}
			else if(type.equals(Boolean.class))
			{
				config.set("hothworldsdata." + world.toLowerCase() + "." + flag.toLowerCase(), new Boolean(value.toLowerCase().equals("allow")));
			}
			else if(type.equals(String.class))
			{
				config.set("hothworldsdata." + world.toLowerCase() + "." + flag.toLowerCase(), value);
			}
			else
			{
			}
			plugin.sendMessage(sender, "&b" + flag + " was set to " + value + " in " + world);
		}
		
		plugin.saveWorldConfig();

		return true;
	}
	
	@SuppressWarnings("rawtypes")
	private static Class getFlagClass(String flag)
	{
		for(int i=0;i<ConfigManager.validWorldFlags.length;i++)
		{
			FlagValue _flag = ConfigManager.validWorldFlags[i];
			
			if(flag.equals(_flag.flag))
			{
				return _flag.type;
			}
		}
		
		return Object.class;
	}
	
	/**
	 * Check if the specified flag is a valid flag
	 * @param flag
	 * @return
	 */
	public static boolean isValidWorldFlag(String flag)
	{
		for(int i=0;i<ConfigManager.validWorldFlags.length;i++)
		{
			if(flag.equals(validWorldFlags[i].flag))
			{
				return true;
			}
		}
		
		return false;
	}
	
	private static boolean addBoolean(boolean old, boolean add)
	{
		if(old==true)
		{
			return true;
		}
		
		if(add==true)
		{
			return true;
		}
		
		return false;
	}
	
	/**
	 * Check if the specified value is valid for this flag
	 * @param flag
	 * @param value
	 * @return
	 */
	public static boolean isValidWorldFlagValue(String flag, String value)
	{
		for(int i=0;i<ConfigManager.validWorldFlags.length;i++)
		{
			if(flag.equals(ConfigManager.validWorldFlags[i].flag))
			{
				boolean result = false;
				
				for(int j=0;j<ConfigManager.validWorldFlags[i].values.length;j++)
				{
					String _value = ConfigManager.validWorldFlags[i].values[j];
					if(_value.equals("[blank]"))
					{
						if(value.equals(""))
						{
							result = ConfigManager.addBoolean(result, true);
						}
					}
					else if(_value.equals("[integer]"))
					{
						try
						{
							int val = Integer.parseInt(value);
							
							if(val<validWorldFlags[i].min || val>validWorldFlags[i].max)
							{
								result = ConfigManager.addBoolean(result, false);
							}
							else
							{
								result = ConfigManager.addBoolean(result, true);
							}
						}
						catch(NumberFormatException e)
						{
							result = ConfigManager.addBoolean(result, false);
						}
					}
					else if(_value.equals("[text]"))
					{
						result = ConfigManager.addBoolean(result, true);
					}
					else
					{
						if(_value.equals(value.toLowerCase()))
						{
							result = ConfigManager.addBoolean(result, true);
						}
					}

					if(result)
					{
						return true;
					}
				}
				
				
			}
		}
		
		return false;
	}

}
