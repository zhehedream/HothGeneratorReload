package biz.orgin.minecraft.hothgenerator;

import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

/**
 * Handles all player specific permissions. The intent is to isolate all permission strings to one class.
 * @author orgin
 *
 */
public class PermissionManager
{
	public static boolean hasFreezePermission(Player player)
	{
		return PermissionManager.getBooleanPermission(player, "hothgenerator.freeze", true);
	}
	
	public static boolean hasHeatPermission(Player player)
	{
		return PermissionManager.getBooleanPermission(player, "hothgenerator.heat", true);
	}

	public static boolean hasLeechPermission(Player player)
	{
		return PermissionManager.getBooleanPermission(player, "hothgenerator.leech", true);
	}

	public static boolean hasMosquitoPermission(Player player)
	{
		return PermissionManager.getBooleanPermission(player, "hothgenerator.mosquito", true);
	}

	private static boolean getBooleanPermission(Player player, String permission, boolean def)
	{
		PermissionDefault pdef = PermissionDefault.TRUE;
		if(!def)
		{
			pdef = PermissionDefault.FALSE;
		}
		
		return player.hasPermission(new Permission(permission, pdef));
	}
}
