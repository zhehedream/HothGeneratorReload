package biz.orgin.minecraft.hothgenerator;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Mushroom;
import java.util.UUID;


/**
 * Listens to player interaction events, specifically using a
 * water or lava bucket so that any placed water or lava can
 * be frozen into ice or stone.
 * Bonemealing exposed blocks are prevented.
 * @author orgin
 *
 */
public class ToolUseManager implements Listener
{
}
