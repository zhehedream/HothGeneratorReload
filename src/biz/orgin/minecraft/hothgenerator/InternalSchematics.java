package biz.orgin.minecraft.hothgenerator;

import java.io.IOException;
import java.util.List;
import java.util.Vector;

import biz.orgin.minecraft.hothgenerator.schematic.*;

public class InternalSchematics {
	
	private static List<Schematic> schematics = null;
	
	public static List<Schematic> getSchematics(HothGeneratorPlugin plugin)
	{
		if(InternalSchematics.schematics==null)
		{
			InternalSchematics.schematics = new Vector<Schematic>();
			
			InternalSchematics.schematics.add(BaseRoom1.instance);
			InternalSchematics.schematics.add(BaseRoom2.instance);
			InternalSchematics.schematics.add(BaseRoom3.instance);
			InternalSchematics.schematics.add(BaseRoom4.instance);
			InternalSchematics.schematics.add(BaseRoom5.instance);
			InternalSchematics.schematics.add(BaseRoom6.instance);
			InternalSchematics.schematics.add(BaseSection.instance);
			InternalSchematics.schematics.add(BaseTop.instance);
			InternalSchematics.schematics.add(CorridorEW.instance);
			InternalSchematics.schematics.add(CorridorNS.instance);
			InternalSchematics.schematics.add(Decor1.instance);
			InternalSchematics.schematics.add(Decor2.instance);
			InternalSchematics.schematics.add(Decor3.instance);
			InternalSchematics.schematics.add(Decor4.instance);
			InternalSchematics.schematics.add(Decor5.instance);
			InternalSchematics.schematics.add(Decor6.instance);
			InternalSchematics.schematics.add(Decor7.instance);
			InternalSchematics.schematics.add(Decor8.instance);
			InternalSchematics.schematics.add(DoorEW.instance);
			InternalSchematics.schematics.add(DoorNS.instance);
			InternalSchematics.schematics.add(DoorUD.instance);
			InternalSchematics.schematics.add(Floor1.instance);
			InternalSchematics.schematics.add(Floor2.instance);
			InternalSchematics.schematics.add(Floor3.instance);
			InternalSchematics.schematics.add(GreenGarden.instance);
			InternalSchematics.schematics.add(GreyGarden.instance);
			InternalSchematics.schematics.add(MiniDome.instance);
			InternalSchematics.schematics.add(NormalRoom.instance);
			InternalSchematics.schematics.add(StairsDown.instance);
			InternalSchematics.schematics.add(StairsUp.instance);
			
			try
			{
				InternalSchematics.schematics.add(new LoadedSchematic(plugin.getResource("schematics/oasis.sm"),"oasis"));
				InternalSchematics.schematics.add(new LoadedSchematic(plugin.getResource("schematics/sandcastle.sm"),"sandcastle"));
				InternalSchematics.schematics.add(new LoadedSchematic(plugin.getResource("schematics/skeleton.sm"),"skeleton"));
				InternalSchematics.schematics.add(new LoadedSchematic(plugin.getResource("schematics/supergarden.sm"),"supergarden"));
				InternalSchematics.schematics.add(new LoadedSchematic(plugin.getResource("schematics/swamptemple.sm"),"swamptemple"));
				InternalSchematics.schematics.add(new LoadedSchematic(plugin.getResource("schematics/treehut.sm"),"treehut"));
				InternalSchematics.schematics.add(new LoadedSchematic(plugin.getResource("schematics/villagecenter.sm"),"villagecenter"));
				InternalSchematics.schematics.add(new LoadedSchematic(plugin.getResource("schematics/villagehut1.sm"),"villagehut1"));
				InternalSchematics.schematics.add(new LoadedSchematic(plugin.getResource("schematics/villagehut2.sm"),"villagehut2"));
				InternalSchematics.schematics.add(new LoadedSchematic(plugin.getResource("schematics/villagehut3.sm"),"villagehut3"));
				InternalSchematics.schematics.add(new LoadedSchematic(plugin.getResource("schematics/villagehut4.sm"),"villagehut4"));
				InternalSchematics.schematics.add(new LoadedSchematic(plugin.getResource("schematics/villagehut5.sm"),"villagehut5"));
				InternalSchematics.schematics.add(new LoadedSchematic(plugin.getResource("schematics/villagehut6.sm"),"villagehut6"));
				InternalSchematics.schematics.add(new LoadedSchematic(plugin.getResource("schematics/villagehut7.sm"),"villagehut7"));
				InternalSchematics.schematics.add(new LoadedSchematic(plugin.getResource("schematics/villagehut8.sm"),"villagehut8"));
			}
			catch(IOException e)
			{
				plugin.getLogger().info("Error while loading schematics " + e.getMessage());
			}
		}
		
		return InternalSchematics.schematics;
	}
}
