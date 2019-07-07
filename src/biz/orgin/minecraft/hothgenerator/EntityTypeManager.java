package biz.orgin.minecraft.hothgenerator;

import org.bukkit.entity.EntityType;

/**
 * Handles converting materials between ID's an instances of the Material class.
 * Since all type ID functions have been deprecated in bukkit then this
 * plugin must handle ID's on its own.
 * Unknown ID's and Materials are treated with the deprecated functions for now.
 * Unfortunately any new block must be added to this class..
 * @author orgin
 *
 */
public class EntityTypeManager
{
	public static void main(String[] args)
	{
		// Generate code for toID() function
		System.out.println("/*");
		System.out.println(" * Returns a classic block type id related to the material");
		System.out.println(" */");
		System.out.println("@SuppressWarnings(\"deprecation\")");
		System.out.println("public static int toID(EntityType entityType)");
		System.out.println("{");
		boolean added = false;
		for(int i=0;i<1024;i++)
		{
			
			@SuppressWarnings("deprecation")
			EntityType entityType = EntityType.fromId(i);
			if(entityType!=null)
			{
				String name = entityType.name();
				String suffix = "";
				if(added)
				{
					suffix = "else ";
				}
				System.out.println("\t" + suffix + "if(entityType.equals(EntityType." + name + "))");
				System.out.println("\t{");
				System.out.println("\t\treturn " + i + ";");
				System.out.println("\t}");
				added = true;
			}
		}
		System.out.println("\treturn entityType.getTypeId();");
		System.out.println("}");
		
		System.out.println();
		
		// Generate code for toMaterial() function
		System.out.println("/*");
		System.out.println(" * Returns a classic block type id related to the material");
		System.out.println(" */");
		System.out.println("@SuppressWarnings(\"deprecation\")");
		System.out.println("public static EntityType toEntityType(int id)");
		System.out.println("{");
		added = false;
		for(int i=0;i<1024;i++)
		{
			@SuppressWarnings("deprecation")
			EntityType entityType = EntityType.fromId(i);
			if(entityType!=null)
			{
				String name = entityType.name();
				String suffix = "";
				if(added)
				{
					suffix = "else ";
				}
				System.out.println("\t" + suffix + "if(id==" + i + ")");
				System.out.println("\t{");
				System.out.println("\t\treturn EntityType." + name + ";");
				System.out.println("\t}");
				added = true;
			}
		}
		System.out.println("\treturn EntityType.fromId(id);");
		System.out.println("}");
	}

	// Generated code below
	
	/*
	 * Returns a classic block type id related to the material
	 */
	//@SuppressWarnings("deprecation")
	public static int toID(EntityType entityType)
	{
		if(entityType.equals(EntityType.DROPPED_ITEM))
		{
			return 1;
		}
		else if(entityType.equals(EntityType.EXPERIENCE_ORB))
		{
			return 2;
		}
		else if(entityType.equals(EntityType.LEASH_HITCH))
		{
			return 8;
		}
		else if(entityType.equals(EntityType.PAINTING))
		{
			return 9;
		}
		else if(entityType.equals(EntityType.ARROW))
		{
			return 10;
		}
		else if(entityType.equals(EntityType.SNOWBALL))
		{
			return 11;
		}
		else if(entityType.equals(EntityType.FIREBALL))
		{
			return 12;
		}
		else if(entityType.equals(EntityType.SMALL_FIREBALL))
		{
			return 13;
		}
		else if(entityType.equals(EntityType.ENDER_PEARL))
		{
			return 14;
		}
		else if(entityType.equals(EntityType.ENDER_SIGNAL))
		{
			return 15;
		}
		else if(entityType.equals(EntityType.THROWN_EXP_BOTTLE))
		{
			return 17;
		}
		else if(entityType.equals(EntityType.ITEM_FRAME))
		{
			return 18;
		}
		else if(entityType.equals(EntityType.WITHER_SKULL))
		{
			return 19;
		}
		else if(entityType.equals(EntityType.PRIMED_TNT))
		{
			return 20;
		}
		else if(entityType.equals(EntityType.FALLING_BLOCK))
		{
			return 21;
		}
		else if(entityType.equals(EntityType.FIREWORK))
		{
			return 22;
		}
		else if(entityType.equals(EntityType.TIPPED_ARROW))
		{
			return 23;
		}
		else if(entityType.equals(EntityType.SPECTRAL_ARROW))
		{
			return 24;
		}
		else if(entityType.equals(EntityType.SHULKER_BULLET))
		{
			return 25;
		}
		else if(entityType.equals(EntityType.DRAGON_FIREBALL))
		{
			return 26;
		}
		else if(entityType.equals(EntityType.ARMOR_STAND))
		{
			return 30;
		}
		else if(entityType.equals(EntityType.MINECART_COMMAND))
		{
			return 40;
		}
		else if(entityType.equals(EntityType.BOAT))
		{
			return 41;
		}
		else if(entityType.equals(EntityType.MINECART))
		{
			return 42;
		}
		else if(entityType.equals(EntityType.MINECART_CHEST))
		{
			return 43;
		}
		else if(entityType.equals(EntityType.MINECART_FURNACE))
		{
			return 44;
		}
		else if(entityType.equals(EntityType.MINECART_TNT))
		{
			return 45;
		}
		else if(entityType.equals(EntityType.MINECART_HOPPER))
		{
			return 46;
		}
		else if(entityType.equals(EntityType.MINECART_MOB_SPAWNER))
		{
			return 47;
		}
		else if(entityType.equals(EntityType.CREEPER))
		{
			return 50;
		}
		else if(entityType.equals(EntityType.SKELETON))
		{
			return 51;
		}
		else if(entityType.equals(EntityType.SPIDER))
		{
			return 52;
		}
		else if(entityType.equals(EntityType.GIANT))
		{
			return 53;
		}
		else if(entityType.equals(EntityType.ZOMBIE))
		{
			return 54;
		}
		else if(entityType.equals(EntityType.SLIME))
		{
			return 55;
		}
		else if(entityType.equals(EntityType.GHAST))
		{
			return 56;
		}
		else if(entityType.equals(EntityType.PIG_ZOMBIE))
		{
			return 57;
		}
		else if(entityType.equals(EntityType.ENDERMAN))
		{
			return 58;
		}
		else if(entityType.equals(EntityType.CAVE_SPIDER))
		{
			return 59;
		}
		else if(entityType.equals(EntityType.SILVERFISH))
		{
			return 60;
		}
		else if(entityType.equals(EntityType.BLAZE))
		{
			return 61;
		}
		else if(entityType.equals(EntityType.MAGMA_CUBE))
		{
			return 62;
		}
		else if(entityType.equals(EntityType.ENDER_DRAGON))
		{
			return 63;
		}
		else if(entityType.equals(EntityType.WITHER))
		{
			return 64;
		}
		else if(entityType.equals(EntityType.BAT))
		{
			return 65;
		}
		else if(entityType.equals(EntityType.WITCH))
		{
			return 66;
		}
		else if(entityType.equals(EntityType.ENDERMITE))
		{
			return 67;
		}
		else if(entityType.equals(EntityType.GUARDIAN))
		{
			return 68;
		}
		else if(entityType.equals(EntityType.SHULKER))
		{
			return 69;
		}
		else if(entityType.equals(EntityType.PIG))
		{
			return 90;
		}
		else if(entityType.equals(EntityType.SHEEP))
		{
			return 91;
		}
		else if(entityType.equals(EntityType.COW))
		{
			return 92;
		}
		else if(entityType.equals(EntityType.CHICKEN))
		{
			return 93;
		}
		else if(entityType.equals(EntityType.SQUID))
		{
			return 94;
		}
		else if(entityType.equals(EntityType.WOLF))
		{
			return 95;
		}
		else if(entityType.equals(EntityType.MUSHROOM_COW))
		{
			return 96;
		}
		else if(entityType.equals(EntityType.SNOWMAN))
		{
			return 97;
		}
		else if(entityType.equals(EntityType.OCELOT))
		{
			return 98;
		}
		else if(entityType.equals(EntityType.IRON_GOLEM))
		{
			return 99;
		}
		else if(entityType.equals(EntityType.HORSE))
		{
			return 100;
		}
		else if(entityType.equals(EntityType.RABBIT))
		{
			return 101;
		}
		else if(entityType.equals(EntityType.VILLAGER))
		{
			return 120;
		}
		else if(entityType.equals(EntityType.ENDER_CRYSTAL))
		{
			return 200;
		}
		return entityType.getTypeId();
	}

	/*
	 * Returns a classic block type id related to the material
	 */
	//@SuppressWarnings("deprecation")
	public static EntityType toEntityType(int id)
	{
		if(id==1)
		{
			return EntityType.DROPPED_ITEM;
		}
		else if(id==2)
		{
			return EntityType.EXPERIENCE_ORB;
		}
		else if(id==8)
		{
			return EntityType.LEASH_HITCH;
		}
		else if(id==9)
		{
			return EntityType.PAINTING;
		}
		else if(id==10)
		{
			return EntityType.ARROW;
		}
		else if(id==11)
		{
			return EntityType.SNOWBALL;
		}
		else if(id==12)
		{
			return EntityType.FIREBALL;
		}
		else if(id==13)
		{
			return EntityType.SMALL_FIREBALL;
		}
		else if(id==14)
		{
			return EntityType.ENDER_PEARL;
		}
		else if(id==15)
		{
			return EntityType.ENDER_SIGNAL;
		}
		else if(id==17)
		{
			return EntityType.THROWN_EXP_BOTTLE;
		}
		else if(id==18)
		{
			return EntityType.ITEM_FRAME;
		}
		else if(id==19)
		{
			return EntityType.WITHER_SKULL;
		}
		else if(id==20)
		{
			return EntityType.PRIMED_TNT;
		}
		else if(id==21)
		{
			return EntityType.FALLING_BLOCK;
		}
		else if(id==22)
		{
			return EntityType.FIREWORK;
		}
		else if(id==23)
		{
			return EntityType.TIPPED_ARROW;
		}
		else if(id==24)
		{
			return EntityType.SPECTRAL_ARROW;
		}
		else if(id==25)
		{
			return EntityType.SHULKER_BULLET;
		}
		else if(id==26)
		{
			return EntityType.DRAGON_FIREBALL;
		}
		else if(id==30)
		{
			return EntityType.ARMOR_STAND;
		}
		else if(id==40)
		{
			return EntityType.MINECART_COMMAND;
		}
		else if(id==41)
		{
			return EntityType.BOAT;
		}
		else if(id==42)
		{
			return EntityType.MINECART;
		}
		else if(id==43)
		{
			return EntityType.MINECART_CHEST;
		}
		else if(id==44)
		{
			return EntityType.MINECART_FURNACE;
		}
		else if(id==45)
		{
			return EntityType.MINECART_TNT;
		}
		else if(id==46)
		{
			return EntityType.MINECART_HOPPER;
		}
		else if(id==47)
		{
			return EntityType.MINECART_MOB_SPAWNER;
		}
		else if(id==50)
		{
			return EntityType.CREEPER;
		}
		else if(id==51)
		{
			return EntityType.SKELETON;
		}
		else if(id==52)
		{
			return EntityType.SPIDER;
		}
		else if(id==53)
		{
			return EntityType.GIANT;
		}
		else if(id==54)
		{
			return EntityType.ZOMBIE;
		}
		else if(id==55)
		{
			return EntityType.SLIME;
		}
		else if(id==56)
		{
			return EntityType.GHAST;
		}
		else if(id==57)
		{
			return EntityType.PIG_ZOMBIE;
		}
		else if(id==58)
		{
			return EntityType.ENDERMAN;
		}
		else if(id==59)
		{
			return EntityType.CAVE_SPIDER;
		}
		else if(id==60)
		{
			return EntityType.SILVERFISH;
		}
		else if(id==61)
		{
			return EntityType.BLAZE;
		}
		else if(id==62)
		{
			return EntityType.MAGMA_CUBE;
		}
		else if(id==63)
		{
			return EntityType.ENDER_DRAGON;
		}
		else if(id==64)
		{
			return EntityType.WITHER;
		}
		else if(id==65)
		{
			return EntityType.BAT;
		}
		else if(id==66)
		{
			return EntityType.WITCH;
		}
		else if(id==67)
		{
			return EntityType.ENDERMITE;
		}
		else if(id==68)
		{
			return EntityType.GUARDIAN;
		}
		else if(id==69)
		{
			return EntityType.SHULKER;
		}
		else if(id==90)
		{
			return EntityType.PIG;
		}
		else if(id==91)
		{
			return EntityType.SHEEP;
		}
		else if(id==92)
		{
			return EntityType.COW;
		}
		else if(id==93)
		{
			return EntityType.CHICKEN;
		}
		else if(id==94)
		{
			return EntityType.SQUID;
		}
		else if(id==95)
		{
			return EntityType.WOLF;
		}
		else if(id==96)
		{
			return EntityType.MUSHROOM_COW;
		}
		else if(id==97)
		{
			return EntityType.SNOWMAN;
		}
		else if(id==98)
		{
			return EntityType.OCELOT;
		}
		else if(id==99)
		{
			return EntityType.IRON_GOLEM;
		}
		else if(id==100)
		{
			return EntityType.HORSE;
		}
		else if(id==101)
		{
			return EntityType.RABBIT;
		}
		else if(id==120)
		{
			return EntityType.VILLAGER;
		}
		else if(id==200)
		{
			return EntityType.ENDER_CRYSTAL;
		}
		return EntityType.fromId(id);
	}
}
